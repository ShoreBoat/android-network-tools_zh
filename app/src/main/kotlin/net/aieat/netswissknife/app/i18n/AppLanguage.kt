package net.aieat.netswissknife.app.i18n

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.Locale

/**
 * Per-app language selection (二开 addition).
 *
 * Android 13+ ships a platform implementation ([LocaleManager]) that persists the
 * choice and recreates running activities on its own, so the settings screen simply
 * delegates to it. Releases below 33 have no platform API and this app deliberately
 * avoids an AppCompat dependency, so the chosen tag is kept in SharedPreferences and
 * applied by wrapping the base context in [wrap] — every Activity and the Application
 * call that during `attachBaseContext`.
 *
 * [current] is always the single source of truth for the settings screen: on API 33+
 * it reads back what the platform stored (which also covers a choice made in the system
 * Settings → Apps → Language screen), below 33 it reads our own preference.
 */
object AppLanguage {

    /** Tag meaning "follow the system language". */
    const val SYSTEM = ""

    const val CHINESE = "zh-CN"
    const val ENGLISH = "en"

    private const val PREFS = "app_language"
    private const val KEY_TAG = "tag"

    /** True when the platform owns the locale and no context wrapping is needed. */
    val isPlatformManaged: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    /** Language tag currently in effect, or [SYSTEM] when following the system. */
    fun current(context: Context): String =
        if (isPlatformManaged) {
            platformLocales(context)?.toLanguageTags().orEmpty()
        } else {
            prefs(context).getString(KEY_TAG, SYSTEM).orEmpty()
        }

    /**
     * Stores [tag] ([SYSTEM] to follow the system). On API 33+ the platform applies and
     * persists it; below 33 the caller is responsible for recreating the Activity.
     */
    fun set(context: Context, tag: String) {
        if (isPlatformManaged) {
            setPlatformLocale(context, tag)
        } else {
            prefs(context).edit().putString(KEY_TAG, tag).apply()
        }
    }

    /** Wraps [base] with the stored locale. No-op on API 33+ (platform-managed). */
    fun wrap(base: Context): Context {
        if (isPlatformManaged) return base

        val tag = prefs(base).getString(KEY_TAG, SYSTEM).orEmpty()
        if (tag.isEmpty()) return base

        val locale = Locale.forLanguageTag(tag)
        // Keep the default in sync so framework-side formatting (dates, numbers)
        // follows the same choice as the resources.
        Locale.setDefault(locale)

        val configuration = Configuration(base.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))
        return base.createConfigurationContext(configuration)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun platformLocales(context: Context): LocaleList? =
        context.getSystemService(LocaleManager::class.java)?.applicationLocales

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setPlatformLocale(context: Context, tag: String) {
        val locales = if (tag.isEmpty()) {
            LocaleList.getEmptyLocaleList()
        } else {
            LocaleList.forLanguageTags(tag)
        }
        context.getSystemService(LocaleManager::class.java)?.applicationLocales = locales
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}

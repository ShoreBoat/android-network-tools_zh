package net.aieat.netswissknife.app

import android.app.Application
import android.content.Context
import net.aieat.netswissknife.app.crash.CrashHandler
import net.aieat.netswissknife.app.i18n.AppLanguage
import net.aieat.netswissknife.app.util.AppLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NetSwissKnifeApp : Application() {

    /**
     * Applies the user's per-app language choice (二开 addition) before any other
     * component is created. No-op on Android 13+, where the platform does this itself.
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(AppLanguage.wrap(base))
    }

    override fun onCreate() {
        super.onCreate()
        AppLogger.init(this)
        installCrashHandler()
        AppLogger.i("App", "NetSwissKnife started (versionName=${packageManager.getPackageInfo(packageName, 0).versionName})")
    }

    private fun installCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            CrashHandler(
                context = this,
                defaultHandler = defaultHandler,
                onBeforeHandle = { thread, throwable ->
                    try {
                        AppLogger.e("CRASH", "Uncaught exception on thread '${thread.name}'", throwable)
                    } catch (_: Exception) {
                        // Never block crash reporting
                    }
                },
            )
        )
    }
}

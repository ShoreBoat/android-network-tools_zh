package net.aieat.netswissknife.app.ui.navigation

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WifiFind
import androidx.compose.ui.graphics.vector.ImageVector
import net.aieat.netswissknife.app.R

data class ToolInfo(
    val route: String,
    @StringRes val labelRes: Int,
    @StringRes val shortLabelRes: Int,
    val icon: ImageVector,
    @StringRes val descriptionRes: Int
)

sealed class NavRoutes(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : NavRoutes("home", "Home", Icons.Default.Home)
    object Ping : NavRoutes("ping", "Ping", Icons.Default.NetworkCheck)
    object Traceroute : NavRoutes("traceroute", "Traceroute", Icons.Default.Router)
    object Ports : NavRoutes("ports?host={host}", "Port Scanner", Icons.Default.TravelExplore) {
        const val baseRoute = "ports"

        fun createRoute(host: String?): String = host
            ?.takeIf { it.isNotBlank() }
            ?.let { "$baseRoute?host=${Uri.encode(it)}" }
            ?: baseRoute
    }
    object Lan : NavRoutes("lan", "LAN Scanner", Icons.Default.Devices)
    object Dns : NavRoutes("dns", "DNS Lookup", Icons.Default.Language)
    object DebugLogs : NavRoutes("debug_logs", "Debug Logs", Icons.Default.BugReport)
    object WifiScan : NavRoutes("wifi_scan", "Wi-Fi Scanner", Icons.Default.WifiFind)
    object TopologyDiscovery : NavRoutes("topology", "Network Topology", Icons.Default.AccountTree)
    object TlsInspector : NavRoutes("tls", "TLS Inspector", Icons.Default.Lock)
    object WhoisLookup : NavRoutes("whois", "WHOIS Lookup", Icons.AutoMirrored.Filled.ManageSearch)
    object HttpProbe : NavRoutes("httprobe", "HTTP Probe", Icons.Default.Http)
    object SubnetCalculator : NavRoutes("subnet", "Subnet Calc", Icons.Default.Calculate)
    object MdnsDiscovery : NavRoutes("mdns", "mDNS Browser", Icons.Default.CellTower)
    object SpeedTest : NavRoutes("speedtest", "Speed Test", Icons.Default.Speed)
    object WakeOnLan : NavRoutes("wol", "Wake-on-LAN", Icons.Default.PowerSettingsNew)
    object Settings : NavRoutes("settings", "Settings", Icons.Default.Settings)

    companion object {
        /** All navigable tool screens (excluding Home). */
        val allTools = listOf(
            ToolInfo("ping",       R.string.tool_ping_label,       R.string.tool_ping_short,       Icons.Default.NetworkCheck, R.string.tool_ping_desc),
            ToolInfo("traceroute", R.string.tool_traceroute_label, R.string.tool_traceroute_short, Icons.Default.Router,       R.string.tool_traceroute_desc),
            ToolInfo("ports",      R.string.tool_ports_label,      R.string.tool_ports_short,      Icons.Default.TravelExplore, R.string.tool_ports_desc),
            ToolInfo("lan",        R.string.tool_lan_label,        R.string.tool_lan_short,        Icons.Default.Devices,       R.string.tool_lan_desc),
            ToolInfo("dns",        R.string.tool_dns_label,        R.string.tool_dns_short,        Icons.Default.Language,      R.string.tool_dns_desc),
            ToolInfo("wifi_scan",  R.string.tool_wifi_label,       R.string.tool_wifi_short,       Icons.Default.WifiFind,      R.string.tool_wifi_desc),
            ToolInfo("topology",   R.string.tool_topology_label,   R.string.tool_topology_short,   Icons.Default.AccountTree,   R.string.tool_topology_desc),
            ToolInfo("tls",        R.string.tool_tls_label,        R.string.tool_tls_short,        Icons.Default.Lock,          R.string.tool_tls_desc),
            ToolInfo("whois",      R.string.tool_whois_label,      R.string.tool_whois_short,      Icons.AutoMirrored.Filled.ManageSearch, R.string.tool_whois_desc),
            ToolInfo("httprobe",   R.string.tool_httprobe_label,   R.string.tool_httprobe_short,   Icons.Default.Http,          R.string.tool_httprobe_desc),
            ToolInfo("subnet",     R.string.tool_subnet_label,     R.string.tool_subnet_short,     Icons.Default.Calculate,     R.string.tool_subnet_desc),
            ToolInfo("mdns",       R.string.tool_mdns_label,       R.string.tool_mdns_short,       Icons.Default.CellTower,     R.string.tool_mdns_desc),
            ToolInfo("speedtest",  R.string.tool_speedtest_label,  R.string.tool_speedtest_short,  Icons.Default.Speed,         R.string.tool_speedtest_desc),
            ToolInfo("wol",        R.string.tool_wol_label,        R.string.tool_wol_short,        Icons.Default.PowerSettingsNew, R.string.tool_wol_desc),
        )

        /** Default pinned routes shown in the bottom nav (max MAX_PINNED). */
        val defaultPinnedRoutes = listOf("ping", "dns", "ports")
    }
}

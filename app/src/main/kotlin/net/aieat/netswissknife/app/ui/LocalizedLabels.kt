package net.aieat.netswissknife.app.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import net.aieat.netswissknife.app.R
import net.aieat.netswissknife.core.domain.PortScanPreset
import net.aieat.netswissknife.core.network.dns.DnsRecordType
import net.aieat.netswissknife.core.network.dns.DnsServer

/**
 * Maps pure-Kotlin domain models to Android string resources.
 *
 * The :core-domain and :core-network modules have no Android dependencies and
 * therefore keep their original English labels. The app layer owns the mapping
 * to localized resources so the same models can be shown in any language.
 */

/** Localized label for a port-scan [PortScanPreset]. */
@StringRes
fun portPresetLabelRes(preset: PortScanPreset): Int = when (preset) {
    PortScanPreset.COMMON -> R.string.ports_preset_common
    PortScanPreset.WELL_KNOWN -> R.string.ports_preset_well_known
    PortScanPreset.WEB -> R.string.ports_preset_web
    PortScanPreset.DATABASE -> R.string.ports_preset_database
    PortScanPreset.MAIL -> R.string.ports_preset_mail
    PortScanPreset.REMOTE -> R.string.ports_preset_remote
    PortScanPreset.CUSTOM -> R.string.ports_preset_custom
}

/** Localized display name for a DNS [server]. */
@StringRes
fun dnsServerNameRes(server: DnsServer): Int = when (server) {
    is DnsServer.System -> R.string.dns_server_system_name
    DnsServer.Google -> R.string.dns_server_google_name
    DnsServer.Cloudflare -> R.string.dns_server_cloudflare_name
    DnsServer.OpenDns -> R.string.dns_server_opendns_name
    DnsServer.Quad9 -> R.string.dns_server_quad9_name
    DnsServer.AliDns -> R.string.dns_server_alidns_name
    DnsServer.Dnspod -> R.string.dns_server_dnspod_name
    DnsServer.Dns114 -> R.string.dns_server_114_name
    is DnsServer.Custom -> R.string.dns_server_custom_name
}

/**
 * Localized description for a DNS [server]. The custom-server description is
 * the address itself, so callers must pass it as the format argument.
 */
@StringRes
fun dnsServerDescRes(server: DnsServer): Int = when (server) {
    is DnsServer.System -> R.string.dns_server_system_desc
    DnsServer.Google -> R.string.dns_server_google_desc
    DnsServer.Cloudflare -> R.string.dns_server_cloudflare_desc
    DnsServer.OpenDns -> R.string.dns_server_opendns_desc
    DnsServer.Quad9 -> R.string.dns_server_quad9_desc
    DnsServer.AliDns -> R.string.dns_server_alidns_desc
    DnsServer.Dnspod -> R.string.dns_server_dnspod_desc
    DnsServer.Dns114 -> R.string.dns_server_114_desc
    is DnsServer.Custom -> R.string.dns_server_custom_desc
}

/** Localized description for a DNS [DnsRecordType]. */
@StringRes
fun dnsRecordTypeDescRes(type: DnsRecordType): Int = when (type) {
    DnsRecordType.A -> R.string.dns_type_a_desc
    DnsRecordType.AAAA -> R.string.dns_type_aaaa_desc
    DnsRecordType.MX -> R.string.dns_type_mx_desc
    DnsRecordType.TXT -> R.string.dns_type_txt_desc
    DnsRecordType.CNAME -> R.string.dns_type_cname_desc
    DnsRecordType.NS -> R.string.dns_type_ns_desc
    DnsRecordType.SOA -> R.string.dns_type_soa_desc
    DnsRecordType.PTR -> R.string.dns_type_ptr_desc
    DnsRecordType.SRV -> R.string.dns_type_srv_desc
    DnsRecordType.CAA -> R.string.dns_type_caa_desc
}

/**
 * Translates raw error text produced by the ViewModels (and by the ICMP ping
 * engine) into localized UI resources.
 *
 * The ViewModels and the :core-network / :core-domain modules have no Context,
 * so they emit plain English strings and the UiState keeps the raw `message`
 * untouched. Only the known, user-facing sentences are mapped here — matching
 * is by prefix where the sentence carries a dynamic part, and the dynamic part
 * is passed to the resource as a format argument. Anything else (technical
 * errno details, unknown errors) passes through verbatim.
 */
@Composable
fun localizedUiError(raw: String): String = when {
    // Per-packet errors produced by the ping engine.
    raw == "TTL exceeded" -> stringResource(R.string.ping_err_ttl_exceeded)
    raw == "Connection refused" -> stringResource(R.string.ping_err_connection_refused)
    raw.startsWith("Connection refused from ") -> stringResource(
        R.string.ping_err_connection_refused_from,
        raw.removePrefix("Connection refused from ")
    )
    raw.startsWith("Network error (errno=") -> raw

    // Errors assembled by the ViewModels.
    raw == "No network connection" -> stringResource(R.string.error_no_network)
    raw.startsWith("No response received from ") -> stringResource(
        R.string.ping_error_no_response,
        raw.removePrefix("No response received from ")
    )
    raw == "Ping failed" -> stringResource(R.string.ping_error_failed)
    raw.startsWith("No route found to ") -> stringResource(
        R.string.traceroute_error_no_route,
        raw.removePrefix("No route found to ")
    )
    raw == "Traceroute failed" -> stringResource(R.string.traceroute_error_failed)
    raw.startsWith("Scan failed: ") -> stringResource(
        R.string.error_scan_failed,
        localizedUiError(raw.removePrefix("Scan failed: "))
    )
    raw == "Unknown error" -> stringResource(R.string.error_unknown)
    raw == "Discovery failed" -> stringResource(R.string.mdns_error_discovery_failed)
    raw.startsWith("Request failed: ") -> stringResource(
        R.string.httprobe_error_request_failed,
        localizedUiError(raw.removePrefix("Request failed: "))
    )
    raw == "Unknown request error" -> stringResource(R.string.httprobe_error_unknown)
    raw.startsWith("TLS inspection failed: ") -> stringResource(
        R.string.tls_error_inspection_failed,
        localizedUiError(raw.removePrefix("TLS inspection failed: "))
    )
    raw == "Unknown TLS inspection error" -> stringResource(R.string.tls_error_unknown)
    raw == "Starting..." || raw == "Starting…" -> stringResource(R.string.topology_status_starting)

    // Technical details (errno, exception messages, …) are shown as-is.
    else -> raw
}

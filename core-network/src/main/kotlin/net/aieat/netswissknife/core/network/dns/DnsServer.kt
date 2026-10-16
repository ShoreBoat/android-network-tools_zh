package net.aieat.netswissknife.core.network.dns

/**
 * Represents a DNS server to use for lookups.
 * Includes well-known public resolvers and support for custom server addresses.
 */
sealed class DnsServer(
    val displayName: String,
    val description: String
) {
    /**
     * Uses the device's configured DNS resolver.
     * [serverAddresses] must be populated by the app layer (via ConnectivityManager / LinkProperties)
     * before performing a lookup. If empty the repository returns an explicit
     * error because the actual system resolver is unknown.
     */
    data class System(
        val serverAddresses: List<String> = emptyList(),
        val privateDnsActive: Boolean = false,
        val privateDnsHost: String? = null
    ) : DnsServer(
        displayName = "System DNS",
        description = "Uses the DNS server configured on your device"
    )

    /** Google Public DNS – 8.8.8.8 / 8.8.4.4 */
    object Google : DnsServer(
        displayName = "Google DNS",
        description = "8.8.8.8 - Google's fast & reliable public resolver"
    ) {
        const val PRIMARY = "8.8.8.8"
        const val SECONDARY = "8.8.4.4"
    }

    /** Cloudflare DNS – 1.1.1.1 / 1.0.0.1 */
    object Cloudflare : DnsServer(
        displayName = "Cloudflare DNS",
        description = "1.1.1.1 - Cloudflare's privacy-focused public resolver"
    ) {
        const val PRIMARY = "1.1.1.1"
        const val SECONDARY = "1.0.0.1"
    }

    /** OpenDNS – 208.67.222.222 / 208.67.220.220 */
    object OpenDns : DnsServer(
        displayName = "OpenDNS",
        description = "208.67.222.222 - Cisco OpenDNS with optional content filtering"
    ) {
        const val PRIMARY = "208.67.222.222"
        const val SECONDARY = "208.67.220.220"
    }

    /** Quad9 – 9.9.9.9 */
    object Quad9 : DnsServer(
        displayName = "Quad9",
        description = "9.9.9.9 - Security-focused resolver that blocks malicious domains"
    ) {
        const val PRIMARY = "9.9.9.9"
    }

    /** AliDNS (阿里公共 DNS) – 223.5.5.5 / 223.6.6.6 */
    object AliDns : DnsServer(
        displayName = "AliDNS",
        description = "223.5.5.5 - Alibaba public resolver with low latency in mainland China"
    ) {
        const val PRIMARY = "223.5.5.5"
        const val SECONDARY = "223.6.6.6"
    }

    /** DNSPod (腾讯公共 DNS) – 119.29.29.29 / 182.254.116.116 */
    object Dnspod : DnsServer(
        displayName = "DNSPod",
        description = "119.29.29.29 - Tencent public resolver with low latency in mainland China"
    ) {
        const val PRIMARY = "119.29.29.29"
        const val SECONDARY = "182.254.116.116"
    }

    /** 114DNS – 114.114.114.114 / 114.114.115.115 */
    object Dns114 : DnsServer(
        displayName = "114DNS",
        description = "114.114.114.114 - Long-running public resolver popular in mainland China"
    ) {
        const val PRIMARY = "114.114.114.114"
        const val SECONDARY = "114.114.115.115"
    }

    /** A user-specified DNS server IP address. */
    data class Custom(val address: String) : DnsServer(
        displayName = "Custom",
        description = address
    )

    companion object {
        /**
         * Built lazily to avoid JVM class-initialization cycles between the sealed parent and objects.
         *
         * Ordering matters for the dropdown: the device resolver first, then the public resolvers
         * that are fastest from mainland China (:aieat fork), then the international ones.
         */
        val presets: List<DnsServer>
            get() = listOf(System(), AliDns, Dnspod, Dns114, Google, Cloudflare, OpenDns, Quad9)
    }
}

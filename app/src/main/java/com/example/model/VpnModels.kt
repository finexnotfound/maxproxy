package com.example.model

enum class VpnState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    DISCONNECTING,
    ERROR
}

enum class VpnProtocol(
    val displayName: String,
    val shortName: String,
    val tag: String,
    val description: String
) {
    WIREGUARD(
        displayName = "MaxWireGuard (Recommended)",
        shortName = "WireGuard",
        tag = "UDP • Fastest",
        description = "Next-gen lightweight encryption with instant connection and ultra-low battery drain."
    ),
    OPENVPN_UDP(
        displayName = "OpenVPN (UDP)",
        shortName = "OpenVPN UDP",
        tag = "UDP • Fast",
        description = "Industry standard protocol optimized for high-speed streaming and gaming."
    ),
    OPENVPN_TCP(
        displayName = "OpenVPN (TCP)",
        shortName = "OpenVPN TCP",
        tag = "TCP • Stealth",
        description = "Reliable packet delivery designed to bypass restrictive firewalls and school/office blocks."
    ),
    SHADOWSOCKS(
        displayName = "Shadowsocks Stealth",
        shortName = "Shadowsocks",
        tag = "Proxy • Bypass",
        description = "Cutting-edge obfuscation proxy specifically engineered to bypass censorship and DPI."
    ),
    IKEV2(
        displayName = "IKEv2 / IPsec",
        shortName = "IKEv2",
        tag = "Mobile • Stable",
        description = "Rapid automatic reconnect when switching between cellular data and Wi-Fi networks."
    )
}

data class VpnServer(
    val id: String,
    val country: String,
    val countryCode: String,
    val flagEmoji: String,
    val city: String,
    val hostIp: String,
    val pingMs: Int,
    val loadPercent: Int,
    val isPremium: Boolean = false,
    val isFavorite: Boolean = false,
    val features: List<String> = listOf("10 Gbps", "P2P", "No-Logs")
)

data class VpnTrafficStats(
    val downloadBytes: Long = 0L,
    val uploadBytes: Long = 0L,
    val downloadSpeedBps: Long = 0L,
    val uploadSpeedBps: Long = 0L,
    val sessionDurationSeconds: Long = 0L
)

data class PublicIpInfo(
    val ip: String = "172.56.21.89",
    val country: String = "United States",
    val city: String = "Current Location",
    val isp: String = "Mobile Broadband",
    val isSecured: Boolean = false
)

data class ConnectionLog(
    val id: Long = System.currentTimeMillis(),
    val serverName: String,
    val country: String,
    val flagEmoji: String,
    val protocol: String,
    val connectedAt: Long,
    val durationSeconds: Long,
    val totalDataBytes: Long
)

data class SpeedTestResult(
    val pingMs: Int = 0,
    val jitterMs: Int = 0,
    val downloadMbps: Float = 0f,
    val uploadMbps: Float = 0f,
    val isTesting: Boolean = false,
    val progress: Float = 0f,
    val stage: String = "Idle"
)

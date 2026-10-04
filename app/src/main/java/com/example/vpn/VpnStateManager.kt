package com.example.vpn

import com.example.data.IpService
import com.example.data.ServerRepository
import com.example.model.ConnectionLog
import com.example.model.PublicIpInfo
import com.example.model.VpnProtocol
import com.example.model.VpnServer
import com.example.model.VpnState
import com.example.model.VpnTrafficStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object VpnStateManager {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _vpnState = MutableStateFlow(VpnState.DISCONNECTED)
    val vpnState: StateFlow<VpnState> = _vpnState.asStateFlow()

    private val _currentServer = MutableStateFlow(ServerRepository.getFastestServer())
    val currentServer: StateFlow<VpnServer> = _currentServer.asStateFlow()

    private val _allServers = MutableStateFlow(ServerRepository.getAllServers())
    val allServers: StateFlow<List<VpnServer>> = _allServers.asStateFlow()

    private val _trafficStats = MutableStateFlow(VpnTrafficStats())
    val trafficStats: StateFlow<VpnTrafficStats> = _trafficStats.asStateFlow()

    private val _selectedProtocol = MutableStateFlow(VpnProtocol.WIREGUARD)
    val selectedProtocol: StateFlow<VpnProtocol> = _selectedProtocol.asStateFlow()

    private val _killSwitchEnabled = MutableStateFlow(true)
    val killSwitchEnabled: StateFlow<Boolean> = _killSwitchEnabled.asStateFlow()

    private val _cleanWebEnabled = MutableStateFlow(true)
    val cleanWebEnabled: StateFlow<Boolean> = _cleanWebEnabled.asStateFlow()

    private val _publicIpInfo = MutableStateFlow(PublicIpInfo())
    val publicIpInfo: StateFlow<PublicIpInfo> = _publicIpInfo.asStateFlow()

    // Real-time sparkline throughput data (last 24 samples in KB/s)
    private val _trafficWaveform = MutableStateFlow(List(24) { 0f })
    val trafficWaveform: StateFlow<List<Float>> = _trafficWaveform.asStateFlow()

    private val _recentLogs = MutableStateFlow<List<ConnectionLog>>(
        listOf(
            ConnectionLog(
                serverName = "New York (East)",
                country = "United States",
                flagEmoji = "🇺🇸",
                protocol = "WireGuard",
                connectedAt = System.currentTimeMillis() - 7200000L,
                durationSeconds = 2410L,
                totalDataBytes = 412_500_000L
            ),
            ConnectionLog(
                serverName = "London (Docklands)",
                country = "United Kingdom",
                flagEmoji = "🇬🇧",
                protocol = "OpenVPN UDP",
                connectedAt = System.currentTimeMillis() - 28800000L,
                durationSeconds = 1250L,
                totalDataBytes = 188_200_000L
            ),
            ConnectionLog(
                serverName = "Hong Kong (Direct CN2)",
                country = "China",
                flagEmoji = "🇨🇳",
                protocol = "Shadowsocks",
                connectedAt = System.currentTimeMillis() - 86400000L,
                durationSeconds = 3600L,
                totalDataBytes = 945_000_000L
            )
        )
    )
    val recentLogs: StateFlow<List<ConnectionLog>> = _recentLogs.asStateFlow()

    private var connectionStartTime: Long = 0L

    init {
        refreshPublicIp()
    }

    fun setServer(server: VpnServer) {
        _currentServer.value = server
    }

    fun setProtocol(protocol: VpnProtocol) {
        _selectedProtocol.value = protocol
    }

    fun toggleKillSwitch() {
        _killSwitchEnabled.value = !_killSwitchEnabled.value
    }

    fun toggleCleanWeb() {
        _cleanWebEnabled.value = !_cleanWebEnabled.value
    }

    fun toggleFavorite(serverId: String) {
        _allServers.value = _allServers.value.map {
            if (it.id == serverId) it.copy(isFavorite = !it.isFavorite) else it
        }
        if (_currentServer.value.id == serverId) {
            _currentServer.value = _currentServer.value.copy(
                isFavorite = !_currentServer.value.isFavorite
            )
        }
    }

    fun setConnecting() {
        _vpnState.value = VpnState.CONNECTING
    }

    fun setConnected() {
        _vpnState.value = VpnState.CONNECTED
        connectionStartTime = System.currentTimeMillis()
        refreshPublicIp()
    }

    fun setDisconnecting() {
        _vpnState.value = VpnState.DISCONNECTING
    }

    fun setDisconnected() {
        val server = _currentServer.value
        val stats = _trafficStats.value

        if (_vpnState.value == VpnState.CONNECTED && stats.sessionDurationSeconds > 3) {
            val log = ConnectionLog(
                serverName = server.city,
                country = server.country,
                flagEmoji = server.flagEmoji,
                protocol = _selectedProtocol.value.shortName,
                connectedAt = connectionStartTime,
                durationSeconds = stats.sessionDurationSeconds,
                totalDataBytes = stats.downloadBytes + stats.uploadBytes
            )
            _recentLogs.value = listOf(log) + _recentLogs.value.take(15)
        }

        _vpnState.value = VpnState.DISCONNECTED
        _trafficStats.value = VpnTrafficStats()
        _trafficWaveform.value = List(24) { 0f }
        refreshPublicIp()
    }

    fun updateTrafficStats(stats: VpnTrafficStats) {
        _trafficStats.value = stats
        val totalSpeedKbps = ((stats.downloadSpeedBps + stats.uploadSpeedBps) / 1024f).coerceAtLeast(0f)
        val current = _trafficWaveform.value.toMutableList()
        if (current.isNotEmpty()) {
            current.removeAt(0)
            current.add(totalSpeedKbps)
            _trafficWaveform.value = current
        }
    }

    fun refreshPublicIp() {
        scope.launch {
            val current = if (_vpnState.value == VpnState.CONNECTED) _currentServer.value else null
            val ipInfo = IpService.fetchPublicIp(current)
            _publicIpInfo.value = ipInfo
        }
    }
}

package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VpnState
import com.example.ui.components.ConnectionOrb
import com.example.ui.components.GlassPillBadge
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.TrafficWaveform
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.GlassBorderActiveCyan
import com.example.ui.theme.GlassBorderActiveEmerald
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassSurfaceLight
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.vpn.VpnStateManager

@Composable
fun DashboardScreen(
    onToggleVpn: () -> Unit,
    onNavigateToServers: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vpnState by VpnStateManager.vpnState.collectAsState()
    val currentServer by VpnStateManager.currentServer.collectAsState()
    val stats by VpnStateManager.trafficStats.collectAsState()
    val protocol by VpnStateManager.selectedProtocol.collectAsState()
    val killSwitch by VpnStateManager.killSwitchEnabled.collectAsState()
    val cleanWeb by VpnStateManager.cleanWebEnabled.collectAsState()
    val ipInfo by VpnStateManager.publicIpInfo.collectAsState()
    val waveformData by VpnStateManager.trafficWaveform.collectAsState()

    val isConnected = vpnState == VpnState.CONNECTED
    val isConnecting = vpnState == VpnState.CONNECTING || vpnState == VpnState.DISCONNECTING

    val hours = stats.sessionDurationSeconds / 3600
    val minutes = (stats.sessionDurationSeconds % 3600) / 60
    val seconds = stats.sessionDurationSeconds % 60
    val durationText = if (hours > 0) "%02d:%02d:%02d".format(hours, minutes, seconds) else "%02d:%02d".format(minutes, seconds)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(NeonCyan.copy(alpha = 0.3f), Color.Transparent)
                            )
                        )
                        .border(1.dp, GlassBorderHighlight, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "MAX PROXY Logo",
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "MAX PROXY",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "LIQUID GLASS VPN",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Protocol and CleanWeb Badges
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                GlassPillBadge(
                    text = protocol.shortName,
                    textColor = TextSecondary,
                    backgroundColor = GlassSurfaceLight
                )
                if (cleanWeb) {
                    GlassPillBadge(
                        text = "Shield",
                        icon = Icons.Default.CheckCircle,
                        textColor = NeonEmerald,
                        backgroundColor = NeonEmerald.copy(alpha = 0.12f),
                        borderColor = NeonEmerald.copy(alpha = 0.3f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Connection Status Banner
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isConnected) NeonEmerald.copy(alpha = 0.12f)
                    else if (isConnecting) NeonAmber.copy(alpha = 0.12f)
                    else Color.White.copy(alpha = 0.05f)
                )
                .border(
                    1.dp,
                    if (isConnected) GlassBorderActiveEmerald
                    else if (isConnecting) NeonAmber.copy(alpha = 0.4f)
                    else GlassBorderSubtle,
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isConnected) NeonEmerald
                        else if (isConnecting) NeonAmber
                        else TextTertiary
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when (vpnState) {
                    VpnState.CONNECTED -> "SECURE & ENCRYPTED • $durationText"
                    VpnState.CONNECTING -> "CONNECTING TUNNEL..."
                    VpnState.DISCONNECTING -> "DISCONNECTING..."
                    else -> "UNPROTECTED • TAP TO SHIELD"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isConnected) NeonEmerald else if (isConnecting) NeonAmber else TextSecondary,
                letterSpacing = 0.4.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Hero Connection Orb
        ConnectionOrb(
            state = vpnState,
            onClick = onToggleVpn
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Selected Server Card (Tap to open servers sheet/screen)
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderBrush = Brush.horizontalGradient(
                colors = listOf(
                    if (isConnected) NeonEmerald.copy(alpha = 0.4f) else GlassBorderHighlight,
                    GlassBorderSubtle
                )
            ),
            onClick = onNavigateToServers
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Country Flag in Circle
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(DarkObsidian)
                            .border(1.dp, GlassBorderHighlight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentServer.flagEmoji,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentServer.country,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Ping pill
                            Text(
                                text = "• ${currentServer.pingMs} ms",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (currentServer.pingMs < 30) NeonEmerald else NeonAmber
                            )
                        }
                        Text(
                            text = currentServer.city,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Change pill button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeonCyan.copy(alpha = 0.15f))
                        .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Change",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonCyan
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Change Server",
                        tint = NeonCyan,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Live Network Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Download Card
            LiquidGlassCard(
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "DOWNLOAD",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextTertiary,
                            letterSpacing = 0.8.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    val downSpeedKb = stats.downloadSpeedBps / 1024f
                    val downSpeedText = if (downSpeedKb > 1024) "%.1f MB/s".format(downSpeedKb / 1024f) else "%.0f KB/s".format(downSpeedKb)
                    Text(
                        text = if (isConnected) downSpeedText else "0 KB/s",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isConnected) NeonCyan else TextSecondary
                    )
                    val downTotalMb = stats.downloadBytes / (1024f * 1024f)
                    Text(
                        text = if (isConnected) "%.1f MB total".format(downTotalMb) else "0.0 MB total",
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }
            }

            // Upload Card
            LiquidGlassCard(
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "UPLOAD",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextTertiary,
                            letterSpacing = 0.8.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = NeonEmerald,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    val upSpeedKb = stats.uploadSpeedBps / 1024f
                    val upSpeedText = if (upSpeedKb > 1024) "%.1f MB/s".format(upSpeedKb / 1024f) else "%.0f KB/s".format(upSpeedKb)
                    Text(
                        text = if (isConnected) upSpeedText else "0 KB/s",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isConnected) NeonEmerald else TextSecondary
                    )
                    val upTotalMb = stats.uploadBytes / (1024f * 1024f)
                    Text(
                        text = if (isConnected) "%.1f MB total".format(upTotalMb) else "0.0 MB total",
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Live Traffic Sparkline & Public IP Card
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = if (isConnected) NeonEmerald else NeonCyan,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isConnected) "MASKED VIRTUAL IP" else "PUBLIC IP (UNPROTECTED)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextTertiary,
                            letterSpacing = 0.6.sp
                        )
                    }
                    Text(
                        text = ipInfo.ip,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isConnected) NeonEmerald else TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Real-time Traffic Waveform
                TrafficWaveform(
                    dataPoints = waveformData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    lineColor = if (isConnected) NeonCyan else Color.White.copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "LIVE TUNNEL ACTIVITY",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextTertiary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = if (isConnected) "Zero Logs • 256-bit AES-GCM" else "Protection Inactive",
                        fontSize = 10.sp,
                        color = if (isConnected) NeonEmerald else TextTertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Security Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Kill Switch Toggle Pill
            QuickActionPill(
                title = "Kill Switch",
                isActive = killSwitch,
                activeColor = NeonCyan,
                onClick = { VpnStateManager.toggleKillSwitch() },
                modifier = Modifier.weight(1f)
            )

            // CleanWeb Toggle Pill
            QuickActionPill(
                title = "CleanWeb AdBlock",
                isActive = cleanWeb,
                activeColor = NeonEmerald,
                onClick = { VpnStateManager.toggleCleanWeb() },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun QuickActionPill(
    title: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isActive) activeColor.copy(alpha = 0.12f)
                else Color.White.copy(alpha = 0.05f)
            )
            .border(
                1.dp,
                if (isActive) activeColor.copy(alpha = 0.4f)
                else GlassBorderSubtle,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isActive) TextPrimary else TextSecondary
        )
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(if (isActive) activeColor else TextTertiary)
        )
    }
}

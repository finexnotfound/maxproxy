package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VpnProtocol
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.DarkNavyGlass
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.DeepSpace
import com.example.ui.theme.GlassBorderActiveCyan
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    val selectedProtocol by VpnStateManager.selectedProtocol.collectAsState()
    val killSwitch by VpnStateManager.killSwitchEnabled.collectAsState()
    val cleanWeb by VpnStateManager.cleanWebEnabled.collectAsState()
    val ipInfo by VpnStateManager.publicIpInfo.collectAsState()
    val recentLogs by VpnStateManager.recentLogs.collectAsState()

    var showProtocolSheet by remember { mutableStateOf(false) }
    var dnsLeakProtected by remember { mutableStateOf(true) }

    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Title
        Text(
            text = "Settings & Security",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Configure Tunnel Encryption & Liquid Glass Engine",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        // SECTION: Tunnel Protocol
        Text(
            text = "TUNNEL PROTOCOL",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextTertiary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showProtocolSheet = true }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.2f))
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VpnKey,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = selectedProtocol.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = selectedProtocol.tag,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(GlassSurfaceLight)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Change",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION: Security & Privacy
        Text(
            text = "SECURITY & PRIVACY SHIELD",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextTertiary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Kill Switch
                SettingToggleItem(
                    icon = Icons.Default.Lock,
                    iconTint = NeonCyan,
                    title = "Kill Switch",
                    description = "Instantly cut internet traffic if the VPN connection drops",
                    isChecked = killSwitch,
                    onCheckedChange = { VpnStateManager.toggleKillSwitch() }
                )

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(GlassBorderSubtle))

                // CleanWeb
                SettingToggleItem(
                    icon = Icons.Default.Shield,
                    iconTint = NeonEmerald,
                    title = "CleanWeb Ad & Malware Blocker",
                    description = "Block ads, phishing trackers, and malicious crypto miners at DNS level",
                    isChecked = cleanWeb,
                    onCheckedChange = { VpnStateManager.toggleCleanWeb() }
                )

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(GlassBorderSubtle))

                // DNS Leak Protection
                SettingToggleItem(
                    icon = Icons.Default.Dns,
                    iconTint = NeonAmber,
                    title = "DNS Leak Protection",
                    description = "Route all DNS queries through encrypted MAX PROXY private resolvers",
                    isChecked = dnsLeakProtected,
                    onCheckedChange = { dnsLeakProtected = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION: IP Audit
        Text(
            text = "IP ADDRESS & LEAK AUDIT",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextTertiary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Detected IP: ${ipInfo.ip}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (ipInfo.isSecured) NeonEmerald else TextPrimary
                        )
                        Text(
                            text = "ISP: ${ipInfo.isp}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (ipInfo.isSecured) NeonEmerald.copy(alpha = 0.2f)
                                else NeonAmber.copy(alpha = 0.2f)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (ipInfo.isSecured) "PROTECTED" else "UNENCRYPTED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (ipInfo.isSecured) NeonEmerald else NeonAmber
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION: Recent Connection History
        Text(
            text = "RECENT TUNNEL SESSIONS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextTertiary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
            if (recentLogs.isEmpty()) {
                Text(
                    text = "No previous connection logs.",
                    fontSize = 12.sp,
                    color = TextTertiary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                    recentLogs.take(5).forEachIndexed { index, log ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = log.flagEmoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${log.country} (${log.serverName})",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    val minutes = log.durationSeconds / 60
                                    val seconds = log.durationSeconds % 60
                                    val dataMb = log.totalDataBytes / (1024f * 1024f)
                                    Text(
                                        text = "%02d:%02d • %.1f MB • %s".format(minutes, seconds, dataMb, log.protocol),
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Text(
                                text = dateFormat.format(Date(log.connectedAt)),
                                fontSize = 10.sp,
                                color = TextTertiary
                            )
                        }

                        if (index < recentLogs.take(5).size - 1) {
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(GlassBorderSubtle))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // About & Zero-Logs Certificate Card
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderBrush = Brush.horizontalGradient(
                listOf(NeonCyan.copy(alpha = 0.3f), GlassBorderSubtle)
            )
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MAX PROXY v2.4.0 (Build 8801)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Strict Zero-Logs Architecture. Hardware RAM-only servers ensure no traffic or user metadata is ever written to disk or recorded.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // Protocol Selection Bottom Sheet
    if (showProtocolSheet) {
        ModalBottomSheet(
            onDismissRequest = { showProtocolSheet = false },
            sheetState = sheetState,
            containerColor = DarkObsidian,
            scrimColor = Color.Black.copy(alpha = 0.65f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Select VPN Protocol",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Choose encryption engine tailored to your network environment",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                VpnProtocol.values().forEach { proto ->
                    val isSelected = selectedProtocol == proto
                    LiquidGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        borderBrush = Brush.horizontalGradient(
                            listOf(
                                if (isSelected) NeonCyan else GlassBorderSubtle,
                                GlassBorderSubtle
                            )
                        ),
                        onClick = {
                            VpnStateManager.setProtocol(proto)
                            showProtocolSheet = false
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = proto.displayName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) NeonCyan else TextPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = proto.description,
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(NeonCyan),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = TextDark,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SettingToggleItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f))
                    .border(1.dp, iconTint.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(17.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextDark,
                checkedTrackColor = iconTint,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = Color.White.copy(alpha = 0.1f),
                uncheckedBorderColor = GlassBorderSubtle
            )
        )
    }
}

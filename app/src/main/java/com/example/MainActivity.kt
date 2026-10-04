package com.example

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.model.VpnServer
import com.example.model.VpnState
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ServerListScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SpeedTestScreen
import com.example.ui.theme.DarkNavyGlass
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassCardBackground
import com.example.ui.theme.GlassSurfaceLight
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.vpn.MaxProxyVpnService
import com.example.vpn.VpnStateManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MaxProxyApp()
            }
        }
    }
}

@Composable
fun MaxProxyApp() {
    val context = LocalContext.current
    val vpnState by VpnStateManager.vpnState.collectAsState()
    val currentServer by VpnStateManager.currentServer.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Android VPN Permission Launcher
    val vpnPrepareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Permission granted, start VPN service
            MaxProxyVpnService.startVpn(context, currentServer.id)
            Toast.makeText(context, "MAX PROXY Tunnel Started", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "VPN permission required to protect traffic", Toast.LENGTH_LONG).show()
        }
    }

    // Notification Permission Launcher for Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Handled */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Toggle VPN action
    val onToggleVpn: () -> Unit = {
        when (vpnState) {
            VpnState.CONNECTED, VpnState.CONNECTING -> {
                MaxProxyVpnService.stopVpn(context)
                Toast.makeText(context, "MAX PROXY Tunnel Disconnected", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val prepareIntent = VpnService.prepare(context)
                if (prepareIntent != null) {
                    vpnPrepareLauncher.launch(prepareIntent)
                } else {
                    MaxProxyVpnService.startVpn(context, currentServer.id)
                    Toast.makeText(context, "Securing Connection...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Server selection handler
    val onSelectServer: (VpnServer) -> Unit = { server ->
        VpnStateManager.setServer(server)
        if (vpnState == VpnState.CONNECTED) {
            // Switch server dynamically
            MaxProxyVpnService.stopVpn(context)
            MaxProxyVpnService.startVpn(context, server.id)
            Toast.makeText(context, "Switched to ${server.country}", Toast.LENGTH_SHORT).show()
        }
        selectedTabIndex = 0
    }

    // Hardware back handler
    BackHandler(enabled = selectedTabIndex != 0) {
        selectedTabIndex = 0
    }

    LiquidGlassBackground {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                LiquidGlassBottomNav(
                    selectedIndex = selectedTabIndex,
                    onSelectTab = { selectedTabIndex = it },
                    modifier = Modifier.navigationBarsPadding()
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .statusBarsPadding()
            ) {
                AnimatedContent(
                    targetState = selectedTabIndex,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_switch"
                ) { targetTab ->
                    when (targetTab) {
                        0 -> DashboardScreen(
                            onToggleVpn = onToggleVpn,
                            onNavigateToServers = { selectedTabIndex = 1 },
                            onNavigateToSettings = { selectedTabIndex = 3 }
                        )
                        1 -> ServerListScreen(
                            onServerSelected = onSelectServer
                        )
                        2 -> SpeedTestScreen()
                        3 -> SettingsScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun LiquidGlassBottomNav(
    selectedIndex: Int,
    onSelectTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        NavTabItem("VPN", Icons.Filled.Shield, Icons.Outlined.Shield),
        NavTabItem("Servers", Icons.Filled.Public, Icons.Outlined.Public),
        NavTabItem("Speed", Icons.Filled.Speed, Icons.Outlined.Speed),
        NavTabItem("Settings", Icons.Filled.Tune, Icons.Outlined.Tune)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GlassBorderHighlight,
                            GlassBorderSubtle
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GlassSurfaceLight,
                            DarkObsidian.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedIndex == index
                val interactionSource = remember { MutableInteractionSource() }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            if (isSelected) NeonCyan.copy(alpha = 0.16f)
                            else Color.Transparent
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onSelectTab(index) }
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                        contentDescription = tab.label,
                        tint = if (isSelected) NeonCyan else TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )

                    if (isSelected) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tab.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            letterSpacing = 0.2.sp
                        )
                    }
                }
            }
        }
    }
}

data class NavTabItem(
    val label: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
)

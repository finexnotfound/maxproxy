package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServerRepository
import com.example.model.VpnServer
import com.example.model.VpnState
import com.example.ui.components.GlassPillBadge
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.DarkNavyGlass
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.GlassBorderActiveCyan
import com.example.ui.theme.GlassBorderActiveEmerald
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassSurfaceLight
import com.example.ui.theme.GlassSurfaceSubtle
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.vpn.VpnStateManager

@Composable
fun ServerListScreen(
    onServerSelected: (VpnServer) -> Unit,
    modifier: Modifier = Modifier
) {
    val allServers by VpnStateManager.allServers.collectAsState()
    val currentServer by VpnStateManager.currentServer.collectAsState()
    val vpnState by VpnStateManager.vpnState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filterOptions = listOf("All", "Fastest", "Streaming", "P2P", "Favorites")

    val filteredServers = allServers.filter { server ->
        val matchesSearch = searchQuery.isEmpty() ||
                server.country.contains(searchQuery, ignoreCase = true) ||
                server.city.contains(searchQuery, ignoreCase = true) ||
                server.countryCode.contains(searchQuery, ignoreCase = true)

        val matchesCategory = when (selectedFilter) {
            "Fastest" -> server.pingMs < 28
            "Streaming" -> server.features.any { it.contains("Streaming", ignoreCase = true) || it.contains("Netflix", ignoreCase = true) || it.contains("BBC", ignoreCase = true) }
            "P2P" -> server.features.any { it.contains("P2P", ignoreCase = true) }
            "Favorites" -> server.isFavorite
            else -> true
        }

        matchesSearch && matchesCategory
    }

    val fastestServer = remember(allServers) {
        ServerRepository.getFastestServer()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Screen Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Global Locations",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${allServers.size} High-Speed Servers Across 12 Countries",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Input (Glass style)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search country or city...", color = TextTertiary, fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = GlassSurfaceLight,
                unfocusedContainerColor = GlassSurfaceSubtle,
                focusedBorderColor = NeonCyan.copy(alpha = 0.6f),
                unfocusedBorderColor = GlassBorderSubtle,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = NeonCyan
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Pills Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) NeonCyan.copy(alpha = 0.2f)
                            else GlassSurfaceSubtle
                        )
                        .border(
                            1.dp,
                            if (isSelected) NeonCyan.copy(alpha = 0.6f)
                            else GlassBorderSubtle,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = filter,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) NeonCyan else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Smart Connect Card (if no search filter)
            if (searchQuery.isEmpty() && selectedFilter == "All") {
                item {
                    LiquidGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderBrush = Brush.horizontalGradient(
                            colors = listOf(NeonCyan.copy(alpha = 0.5f), GlassBorderSubtle)
                        ),
                        onClick = { onServerSelected(fastestServer) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(NeonCyan.copy(alpha = 0.2f))
                                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = "Smart Connect",
                                        tint = NeonCyan,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Smart Connect",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        GlassPillBadge(
                                            text = "FASTEST",
                                            textColor = NeonCyan,
                                            backgroundColor = NeonCyan.copy(alpha = 0.15f),
                                            borderColor = NeonCyan.copy(alpha = 0.4f)
                                        )
                                    }
                                    Text(
                                        text = "${fastestServer.country} (${fastestServer.city}) • ${fastestServer.pingMs} ms",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            if (currentServer.id == fastestServer.id && vpnState == VpnState.CONNECTED) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(NeonEmerald),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Active",
                                        tint = TextDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Server Items
            items(filteredServers, key = { it.id }) { server ->
                val isSelected = currentServer.id == server.id
                val isConnected = isSelected && vpnState == VpnState.CONNECTED

                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderBrush = Brush.horizontalGradient(
                        colors = listOf(
                            if (isConnected) GlassBorderActiveEmerald
                            else if (isSelected) GlassBorderActiveCyan
                            else GlassBorderSubtle,
                            GlassBorderSubtle
                        )
                    ),
                    onClick = { onServerSelected(server) }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Flag
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(DarkObsidian)
                                        .border(1.dp, GlassBorderSubtle, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = server.flagEmoji, fontSize = 20.sp)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = server.country,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        if (isConnected) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "ACTIVE",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = NeonEmerald,
                                                letterSpacing = 0.5.sp
                                            )
                                        }
                                    }
                                    Text(
                                        text = server.city,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Ping badge
                                val pingColor = if (server.pingMs < 28) NeonEmerald else NeonAmber
                                Text(
                                    text = "${server.pingMs} ms",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = pingColor
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                // Favorite Star
                                IconButton(
                                    onClick = { VpnStateManager.toggleFavorite(server.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (server.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                        contentDescription = "Favorite",
                                        tint = if (server.isFavorite) NeonAmber else TextTertiary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Server Load & Feature Tags Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Tags
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                server.features.take(2).forEach { tag ->
                                    Text(
                                        text = tag,
                                        fontSize = 10.sp,
                                        color = TextTertiary,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color.White.copy(alpha = 0.05f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            // Load indicator
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Load ${server.loadPercent}%",
                                    fontSize = 10.sp,
                                    color = TextTertiary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color.White.copy(alpha = 0.1f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(server.loadPercent / 100f)
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (server.loadPercent > 70) NeonAmber else NeonCyan)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

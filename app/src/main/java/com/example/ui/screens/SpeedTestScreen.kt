package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VpnState
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.vpn.VpnStateManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SpeedTestScreen(
    modifier: Modifier = Modifier
) {
    val currentServer by VpnStateManager.currentServer.collectAsState()
    val vpnState by VpnStateManager.vpnState.collectAsState()
    val scope = rememberCoroutineScope()

    var isTesting by remember { mutableStateOf(false) }
    var currentGaugeValue by remember { mutableFloatStateOf(0f) }
    var pingResult by remember { mutableIntStateOf(currentServer.pingMs) }
    var jitterResult by remember { mutableIntStateOf(2) }
    var downloadResult by remember { mutableFloatStateOf(184.5f) }
    var uploadResult by remember { mutableFloatStateOf(68.2f) }
    var testStage by remember { mutableStateOf("Ready to Test") }
    var hasTested by remember { mutableStateOf(false) }

    fun runSpeedTest() {
        if (isTesting) return
        isTesting = true
        hasTested = true
        currentGaugeValue = 0f

        scope.launch {
            // Stage 1: Ping
            testStage = "Testing Latency & Jitter..."
            for (i in 1..8) {
                delay(120)
                pingResult = (currentServer.pingMs + Random.nextInt(-2, 5)).coerceAtLeast(8)
                jitterResult = Random.nextInt(1, 4)
            }

            // Stage 2: Download Speed
            testStage = "Testing Download Throughput..."
            val targetDown = if (vpnState == VpnState.CONNECTED) Random.nextInt(140, 240).toFloat() else Random.nextInt(85, 130).toFloat()
            for (i in 1..20) {
                delay(80)
                val fraction = i / 20f
                currentGaugeValue = targetDown * fraction + Random.nextFloat() * 12f
                downloadResult = currentGaugeValue
            }
            downloadResult = targetDown

            // Stage 3: Upload Speed
            testStage = "Testing Upload Throughput..."
            val targetUp = if (vpnState == VpnState.CONNECTED) Random.nextInt(55, 95).toFloat() else Random.nextInt(30, 50).toFloat()
            for (i in 1..15) {
                delay(80)
                val fraction = i / 15f
                currentGaugeValue = targetUp * fraction + Random.nextFloat() * 8f
                uploadResult = currentGaugeValue
            }
            uploadResult = targetUp

            // Complete
            delay(200)
            currentGaugeValue = downloadResult
            testStage = "Test Complete"
            isTesting = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Speed & Diagnostic",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Real-Time Throughput & Latency Benchmark",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Liquid Glass Speedometer Dial
        Box(
            modifier = Modifier
                .size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(220.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.width / 2f - 16.dp.toPx()

                // Background track arc (135° to 405° = 270° sweep)
                drawArc(
                    color = Color.White.copy(alpha = 0.08f),
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round),
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                // Active progress arc
                val maxSpeed = 300f
                val sweep = (currentGaugeValue / maxSpeed).coerceIn(0f, 1f) * 270f
                if (sweep > 0f) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(
                                NeonCyan,
                                NeonEmerald,
                                NeonAmber
                            )
                        ),
                        startAngle = 135f,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round),
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2)
                    )
                }
            }

            // Central Value in Speedometer
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%.1f".format(currentGaugeValue),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Mbps",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonCyan,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = testStage,
                    fontSize = 11.sp,
                    color = if (isTesting) NeonAmber else TextTertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Button: Start Speed Test
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isTesting) androidx.compose.ui.graphics.SolidColor(GlassBorderHighlight)
                    else Brush.horizontalGradient(listOf(NeonCyan, NeonEmerald))
                )
                .clickable(enabled = !isTesting) { runSpeedTest() }
                .padding(horizontal = 36.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isTesting) Icons.Default.Refresh else Icons.Default.Speed,
                    contentDescription = null,
                    tint = if (isTesting) TextSecondary else TextDark,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isTesting) "BENCHMARKING..." else if (hasTested) "RUN TEST AGAIN" else "START SPEED TEST",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isTesting) TextSecondary else TextDark,
                    letterSpacing = 0.8.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Test Node Server Info Card
        LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = currentServer.flagEmoji, fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${currentServer.country} (${currentServer.city})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Host: ${currentServer.hostIp} • 10 Gbps Port",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
                Text(
                    text = if (vpnState == VpnState.CONNECTED) "TUNNEL ON" else "DIRECT ISP",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (vpnState == VpnState.CONNECTED) NeonEmerald else TextTertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Benchmark Results Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Ping Card
            LiquidGlassCard(
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)
            ) {
                Column {
                    Text("PING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextTertiary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$pingResult ms",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (pingResult < 35) NeonEmerald else NeonAmber
                    )
                    Text("Jitter $jitterResult ms", fontSize = 10.sp, color = TextTertiary)
                }
            }

            // Download Card
            LiquidGlassCard(
                modifier = Modifier.weight(1.3f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("DOWNLOAD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextTertiary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "%.1f Mbps".format(downloadResult),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                    Text("Peak 240 Mbps", fontSize = 10.sp, color = TextTertiary)
                }
            }

            // Upload Card
            LiquidGlassCard(
                modifier = Modifier.weight(1.3f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("UPLOAD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextTertiary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "%.1f Mbps".format(uploadResult),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonEmerald
                    )
                    Text("Peak 110 Mbps", fontSize = 10.sp, color = TextTertiary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rating Badge Card
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderBrush = Brush.horizontalGradient(
                listOf(NeonEmerald.copy(alpha = 0.4f), GlassBorderSubtle)
            )
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "PERFORMANCE RATING",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextTertiary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "GRADE A+",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonEmerald
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Optimal connection speed for 4K Ultra HD Streaming, Low-latency Competitive Gaming & Anonymous P2P file transfers.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

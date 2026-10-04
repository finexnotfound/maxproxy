package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VpnState
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.DarkNavyGlass
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.DeepSpace
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorderActiveCyan
import com.example.ui.theme.GlassBorderActiveEmerald
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassCardBackground
import com.example.ui.theme.GlassSurfaceLight
import com.example.ui.theme.GlassSurfaceSubtle
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

/**
 * Animated liquid glass background with floating colorful radial gradient orbs
 */
@Composable
fun LiquidGlassBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val transition = rememberInfiniteTransition(label = "liquid_glass_transition")

    val orb1X by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1X"
    )

    val orb1Y by transition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1Y"
    )

    val orb2X by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2X"
    )

    val orb2Y by transition.animateFloat(
        initialValue = 0.75f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2Y"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepSpace)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Base deep dark gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkObsidian,
                        DeepSpace,
                        Color(0xFF04060C)
                    )
                )
            )

            // Liquid Orb 1: Electric Cyan/Blue
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = 0.18f),
                        ElectricBlue.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(width * orb1X, height * orb1Y),
                    radius = width * 0.75f
                ),
                radius = width * 0.75f,
                center = Offset(width * orb1X, height * orb1Y)
            )

            // Liquid Orb 2: Cyber Violet/Purple
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        CyberViolet.copy(alpha = 0.16f),
                        Color(0xFF4C1D95).copy(alpha = 0.07f),
                        Color.Transparent
                    ),
                    center = Offset(width * orb2X, height * orb2Y),
                    radius = width * 0.85f
                ),
                radius = width * 0.85f,
                center = Offset(width * orb2X, height * orb2Y)
            )

            // Liquid Orb 3: Emerald glow at bottom center
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonEmerald.copy(alpha = 0.10f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.5f, height * 0.9f),
                    radius = width * 0.65f
                ),
                radius = width * 0.65f,
                center = Offset(width * 0.5f, height * 0.9f)
            )
        }

        content()
    }
}

/**
 * Frosted liquid glass card container with hairline specular border and reflection
 */
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    borderBrush: Brush = Brush.verticalGradient(
        colors = listOf(
            GlassBorderHighlight,
            GlassBorderSubtle,
            Color.Transparent
        )
    ),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else Modifier

    Box(
        modifier = modifier
            .clip(shape)
            .then(clickModifier)
            .border(
                border = BorderStroke(1.dp, borderBrush),
                shape = shape
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GlassSurfaceLight,
                        GlassCardBackground.copy(alpha = 0.65f)
                    )
                )
            )
            .padding(contentPadding)
    ) {
        content()
    }
}

/**
 * The Hero Liquid Glass Connection Orb
 */
@Composable
fun ConnectionOrb(
    state: VpnState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "orb_pulse")

    // Pulsing outer aura ring
    val pulseScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val pulseAlpha by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Connecting rotation
    val rotationAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_angle"
    )

    val isConnected = state == VpnState.CONNECTED
    val isConnecting = state == VpnState.CONNECTING || state == VpnState.DISCONNECTING

    val primaryGlowColor = when (state) {
        VpnState.CONNECTED -> NeonEmerald
        VpnState.CONNECTING, VpnState.DISCONNECTING -> NeonAmber
        else -> NeonCyan
    }

    Box(
        modifier = modifier
            .size(240.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Outer pulsing ring when connected
        if (isConnected) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .scale(pulseScale)
                    .border(
                        width = 2.dp,
                        brush = Brush.radialGradient(
                            listOf(
                                primaryGlowColor.copy(alpha = pulseAlpha),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        // Rotating orbital dashed ring when connecting
        if (isConnecting) {
            Canvas(
                modifier = Modifier
                    .size(210.dp)
                    .rotate(rotationAngle)
            ) {
                drawCircle(
                    brush = Brush.sweepGradient(
                        listOf(
                            NeonAmber,
                            Color.Transparent,
                            NeonAmber.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        // Main Liquid Glass Outer Sphere (200.dp)
        Box(
            modifier = Modifier
                .size(190.dp)
                .clip(CircleShape)
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.5f),
                            primaryGlowColor.copy(alpha = 0.6f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isConnected) NeonEmerald.copy(alpha = 0.22f) else DarkNavyGlass,
                            DarkObsidian.copy(alpha = 0.95f),
                            Color(0xFF040711)
                        ),
                        center = Offset(100f, 100f)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Inner Specular Glass Reflection (top-left crescent highlight)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(size.width * 0.25f, size.height * 0.15f)
                    cubicTo(
                        size.width * 0.4f, size.height * 0.10f,
                        size.width * 0.65f, size.height * 0.10f,
                        size.width * 0.78f, size.height * 0.20f
                    )
                    cubicTo(
                        size.width * 0.65f, size.height * 0.22f,
                        size.width * 0.38f, size.height * 0.22f,
                        size.width * 0.25f, size.height * 0.15f
                    )
                    close()
                }
                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.5f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
            }

            // Power Icon & Status Inside Orb
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Central Power Icon inside glowing circle
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    if (isConnected) NeonEmerald.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.12f),
                                    Color.Transparent
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = if (isConnected) NeonEmerald.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.25f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "VPN Power Toggle",
                        tint = if (isConnected) NeonEmerald else if (isConnecting) NeonAmber else Color.White,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = when (state) {
                        VpnState.CONNECTED -> "CONNECTED"
                        VpnState.CONNECTING -> "CONNECTING..."
                        VpnState.DISCONNECTING -> "DISCONNECTING..."
                        else -> "TAP TO CONNECT"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = if (isConnected) NeonEmerald else if (isConnecting) NeonAmber else TextSecondary
                )
            }
        }
    }
}

/**
 * Live traffic sparkline bezier graph
 */
@Composable
fun TrafficWaveform(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = NeonCyan
) {
    if (dataPoints.isEmpty()) return

    val maxVal = (dataPoints.maxOrNull() ?: 1f).coerceAtLeast(10f)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val stepX = width / (dataPoints.size - 1).coerceAtLeast(1)

        val path = Path()
        val fillPath = Path()

        dataPoints.forEachIndexed { index, value ->
            val normY = 1f - (value / maxVal).coerceIn(0.05f, 0.95f)
            val x = index * stepX
            val y = normY * height

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                val prevVal = dataPoints[index - 1]
                val prevNormY = 1f - (prevVal / maxVal).coerceIn(0.05f, 0.95f)
                val prevX = (index - 1) * stepX
                val prevY = prevNormY * height

                val controlX1 = prevX + (x - prevX) / 2f
                val controlY1 = prevY
                val controlX2 = prevX + (x - prevX) / 2f
                val controlY2 = y

                path.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
                fillPath.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
            }
        }

        fillPath.lineTo(width, height)
        fillPath.close()

        // Gradient under curve
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    lineColor.copy(alpha = 0.25f),
                    Color.Transparent
                )
            )
        )

        // Curve stroke
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

/**
 * Minimalist Apple-style frosted pill badge
 */
@Composable
fun GlassPillBadge(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    textColor: Color = TextPrimary,
    backgroundColor: Color = GlassSurfaceLight,
    borderColor: Color = GlassBorderSubtle
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            letterSpacing = 0.2.sp
        )
    }
}

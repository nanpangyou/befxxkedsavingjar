package com.z.money.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.z.money.ui.theme.CoinGold
import com.z.money.ui.theme.InkText
import com.z.money.ui.theme.MossGreen

@Composable
fun PiggyJarIllustration(
    modifier: Modifier = Modifier,
    showJar: Boolean = false,
) {
    val primary = MaterialTheme.colorScheme.primary
    val accent = MaterialTheme.colorScheme.secondary
    val line = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
    val leaf = primary.copy(alpha = 0.72f)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        if (showJar) {
            drawRoundRect(
                color = Color.White.copy(alpha = 0.24f),
                topLeft = Offset(w * 0.14f, h * 0.1f),
                size = Size(w * 0.72f, h * 0.78f),
                cornerRadius = CornerRadius(w * 0.18f, w * 0.18f),
            )
            drawRoundRect(
                color = line,
                topLeft = Offset(w * 0.14f, h * 0.1f),
                size = Size(w * 0.72f, h * 0.78f),
                cornerRadius = CornerRadius(w * 0.18f, w * 0.18f),
                style = Stroke(3.5f),
            )
            drawRoundRect(
                color = primary,
                topLeft = Offset(w * 0.27f, h * 0.04f),
                size = Size(w * 0.46f, h * 0.18f),
                cornerRadius = CornerRadius(18f, 18f),
            )
        }

        drawLeaves(leaf, line)
        drawPig(w, h, line)
        drawCoin(center = Offset(w * 0.58f, h * 0.22f), radius = w * 0.13f, accent = accent, line = line)
        drawSparkles(accent)
    }
}

@Composable
fun WorkerIllustration(
    resting: Boolean,
    modifier: Modifier = Modifier,
) {
    val primary = MaterialTheme.colorScheme.primary
    val accent = MaterialTheme.colorScheme.secondary
    val brick = MaterialTheme.colorScheme.tertiary
    val line = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val groundY = h * 0.82f

        drawLine(
            color = primary.copy(alpha = 0.34f),
            start = Offset(w * 0.08f, groundY),
            end = Offset(w * 0.96f, groundY),
            strokeWidth = 5f,
        )
        drawWorkerBody(w, h, primary, accent, line, resting)
        if (resting) {
            drawTree(w, h, primary, line)
            drawLine(line, Offset(w * 0.34f, h * 0.62f), Offset(w * 0.18f, groundY), 5f)
            drawLine(line, Offset(w * 0.52f, h * 0.62f), Offset(w * 0.76f, groundY), 5f)
        } else {
            repeat(7) { index ->
                val x = w * (0.62f + index * 0.045f)
                val y = h * (0.58f - (index % 2) * 0.12f)
                drawRoundRect(
                    color = brick,
                    topLeft = Offset(x, y),
                    size = Size(w * 0.08f, h * 0.08f),
                    cornerRadius = CornerRadius(4f, 4f),
                )
                drawRoundRect(
                    color = line,
                    topLeft = Offset(x, y),
                    size = Size(w * 0.08f, h * 0.08f),
                    cornerRadius = CornerRadius(4f, 4f),
                    style = Stroke(1.5f),
                )
            }
        }
    }
}

@Composable
fun CoinDot(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFFFE38A), CoinGold),
                ),
            ),
    )
}

@Composable
fun CoinTrail(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        CoinDot(size = 18.dp)
        Spacer(Modifier.size(6.dp))
        CoinDot(size = 24.dp)
    }
}

private fun DrawScope.drawPig(
    w: Float,
    h: Float,
    line: Color,
) {
    val body = Color(0xFFF4A08B)
    val blush = Color(0xFFE77D68)
    drawOval(
        color = body,
        topLeft = Offset(w * 0.24f, h * 0.38f),
        size = Size(w * 0.48f, h * 0.34f),
    )
    drawOval(
        color = line,
        topLeft = Offset(w * 0.24f, h * 0.38f),
        size = Size(w * 0.48f, h * 0.34f),
        style = Stroke(3f),
    )
    drawCircle(body, radius = w * 0.095f, center = Offset(w * 0.26f, h * 0.52f))
    drawCircle(line, radius = w * 0.095f, center = Offset(w * 0.26f, h * 0.52f), style = Stroke(3f))
    drawCircle(blush.copy(alpha = 0.72f), radius = w * 0.042f, center = Offset(w * 0.44f, h * 0.56f))
    drawCircle(InkText, radius = w * 0.018f, center = Offset(w * 0.35f, h * 0.48f))
    drawCircle(InkText, radius = w * 0.018f, center = Offset(w * 0.52f, h * 0.47f))
    drawCircle(line, radius = w * 0.012f, center = Offset(w * 0.24f, h * 0.52f))
    drawCircle(line, radius = w * 0.012f, center = Offset(w * 0.29f, h * 0.52f))
    drawLine(line, Offset(w * 0.68f, h * 0.49f), Offset(w * 0.78f, h * 0.45f), 3f)
    drawLine(line, Offset(w * 0.34f, h * 0.72f), Offset(w * 0.31f, h * 0.83f), 5f)
    drawLine(line, Offset(w * 0.62f, h * 0.72f), Offset(w * 0.65f, h * 0.83f), 5f)
}

private fun DrawScope.drawCoin(center: Offset, radius: Float, accent: Color, line: Color) {
    drawCircle(accent, radius = radius, center = center)
    drawCircle(Color(0xFFFFD973), radius = radius * 0.7f, center = center)
    drawCircle(line, radius = radius, center = center, style = Stroke(3f))
    drawLine(line, Offset(center.x - radius * 0.38f, center.y), Offset(center.x + radius * 0.38f, center.y), 3f)
    drawLine(line, Offset(center.x, center.y - radius * 0.42f), Offset(center.x, center.y + radius * 0.42f), 3f)
}

private fun DrawScope.drawLeaves(leaf: Color, line: Color) {
    val w = size.width
    val h = size.height
    drawLine(line.copy(alpha = 0.42f), Offset(w * 0.16f, h * 0.78f), Offset(w * 0.24f, h * 0.48f), 3f)
    drawLine(line.copy(alpha = 0.42f), Offset(w * 0.84f, h * 0.8f), Offset(w * 0.78f, h * 0.5f), 3f)
    repeat(4) {
        drawOval(leaf, Offset(w * (0.13f + it * 0.03f), h * (0.68f - it * 0.08f)), Size(w * 0.07f, h * 0.12f))
        drawOval(leaf, Offset(w * (0.78f - it * 0.02f), h * (0.67f - it * 0.08f)), Size(w * 0.07f, h * 0.12f))
    }
}

private fun DrawScope.drawSparkles(accent: Color) {
    val w = size.width
    val h = size.height
    drawCircle(accent, radius = 4f, center = Offset(w * 0.82f, h * 0.34f))
    drawCircle(accent, radius = 3f, center = Offset(w * 0.74f, h * 0.18f))
    drawLine(accent, Offset(w * 0.86f, h * 0.2f), Offset(w * 0.86f, h * 0.1f), 3f)
    drawLine(accent, Offset(w * 0.82f, h * 0.15f), Offset(w * 0.9f, h * 0.15f), 3f)
}

private fun DrawScope.drawWorkerBody(
    w: Float,
    h: Float,
    primary: Color,
    accent: Color,
    line: Color,
    resting: Boolean,
) {
    val skin = Color(0xFFF1B282)
    val bodyCenter = if (resting) Offset(w * 0.42f, h * 0.5f) else Offset(w * 0.36f, h * 0.46f)
    drawCircle(skin, radius = w * 0.07f, center = Offset(bodyCenter.x, h * 0.26f))
    drawArc(accent, 190f, 160f, false, Offset(bodyCenter.x - w * 0.09f, h * 0.16f), Size(w * 0.18f, h * 0.12f), style = Stroke(8f))
    drawCircle(line, radius = w * 0.012f, center = Offset(bodyCenter.x - w * 0.02f, h * 0.25f))
    drawCircle(line, radius = w * 0.012f, center = Offset(bodyCenter.x + w * 0.035f, h * 0.25f))
    drawRoundRect(
        color = primary,
        topLeft = Offset(bodyCenter.x - w * 0.08f, h * 0.34f),
        size = Size(w * 0.17f, h * 0.22f),
        cornerRadius = CornerRadius(16f, 16f),
    )
    drawLine(line, Offset(bodyCenter.x - w * 0.08f, h * 0.42f), Offset(bodyCenter.x - w * 0.22f, h * 0.54f), 6f)
    drawLine(line, Offset(bodyCenter.x + w * 0.08f, h * 0.42f), Offset(bodyCenter.x + w * 0.2f, h * 0.52f), 6f)
}

private fun DrawScope.drawTree(w: Float, h: Float, primary: Color, line: Color) {
    drawLine(line.copy(alpha = 0.65f), Offset(w * 0.76f, h * 0.74f), Offset(w * 0.76f, h * 0.38f), 5f)
    drawCircle(primary.copy(alpha = 0.45f), radius = w * 0.13f, center = Offset(w * 0.74f, h * 0.34f))
    drawCircle(primary.copy(alpha = 0.55f), radius = w * 0.1f, center = Offset(w * 0.84f, h * 0.4f))
    drawCircle(MossGreen.copy(alpha = 0.42f), radius = w * 0.09f, center = Offset(w * 0.67f, h * 0.43f))
}

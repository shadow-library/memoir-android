package com.shadow.apps.memoir.ui.splash

import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadow.apps.memoir.R
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.Slate8
import com.shadow.apps.memoir.ui.theme.Slate9
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    val context = LocalContext.current
    // Flag 5: Respect system "Reduce Motion" / "Animations off" setting
    val reduceMotion = remember {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        ) == 0f
    }

    var enhanced by remember { mutableStateOf(false) }

    // Flag 3: 250ms animations (was 300ms); total splash 600ms (was 1000ms)
    val gradientBottomColor by animateColorAsState(
        targetValue = if (enhanced) Slate8 else Slate9,
        animationSpec = if (reduceMotion) snap() else tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "gradientBottom"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (enhanced) 1f else 0f,
        animationSpec = if (reduceMotion) snap() else tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "glowAlpha"
    )

    LaunchedEffect(Unit) {
        if (reduceMotion) {
            // Flag 5: Skip animation — show static brand screen briefly then dismiss
            enhanced = true
            delay(300L)
            onSplashComplete()
            return@LaunchedEffect
        }
        delay(100L)
        enhanced = true
        delay(500L)
        onSplashComplete()
    }

    // Flag 4: BoxWithConstraints so glow diameter adapts to screen size
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(Slate9, gradientBottomColor))
            ),
        contentAlignment = Alignment.Center
    ) {
        // 85% of the shorter screen dimension — scales correctly on phones and tablets
        val glowSize = minOf(maxWidth, maxHeight) * 0.85f

        Canvas(modifier = Modifier.size(glowSize)) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Cyan5.copy(alpha = 0.08f * glowAlpha),
                        Color.Transparent
                    ),
                    center = Offset(size.width / 2, size.height / 2),
                    radius = size.minDimension / 2
                )
            )
        }

        Image(
            painter = painterResource(R.drawable.ic_memoir_logo),
            contentDescription = "Shadow Memoir",
            modifier = Modifier.size(280.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(
    name = "Splash Screen",
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFF0C1220
)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(onSplashComplete = {})
}

package com.shadow.apps.memoir.ui.splash

import android.animation.ValueAnimator
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shadow.apps.memoir.R
import com.shadow.apps.memoir.domain.model.StartupDestination
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.Cyan6
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import com.shadow.apps.memoir.ui.theme.Slate0
import com.shadow.apps.memoir.ui.theme.Slate1
import com.shadow.apps.memoir.ui.theme.Slate8
import com.shadow.apps.memoir.ui.theme.Slate9
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState

// Branded wider tracking for the splash title — intentional departure from headlineLarge's 0.sp.
private val SplashTitleLetterSpacing = 2.sp

private fun <T> splashAnimSpec(reduceMotion: Boolean): AnimationSpec<T> =
    if (reduceMotion) snap() else tween(durationMillis = 250, easing = FastOutSlowInEasing)

/*
 * Route
 *
 * Displays the branded splash animation, then asks the ViewModel for startup routing.
 */
@Composable
fun SplashScreen(
    onSplashComplete: (StartupDestination) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.destination) {
        uiState.destination?.let(onSplashComplete)
    }

    SplashContent(onAnimationComplete = viewModel::resolveStartupDestination)
}

/*
 * Content
 *
 * Stateless splash animation content.
 */
@Composable
private fun SplashContent(onAnimationComplete: () -> Unit) {
    val isDarkMode = isSystemInDarkTheme()
    val isInPreview = LocalInspectionMode.current
    var enhanced by remember { mutableStateOf(isInPreview) }

    // areAnimatorsEnabled() reads a cached field in ValueAnimator — no ContentResolver IPC.
    val reduceMotion = remember { !ValueAnimator.areAnimatorsEnabled() }

    val gradientBottomColor by animateColorAsState(
        targetValue = if (enhanced) Slate8 else Slate9,
        animationSpec = splashAnimSpec(reduceMotion),
        label = "gradientBottom"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (enhanced) 1f else 0f,
        animationSpec = splashAnimSpec(reduceMotion),
        label = "glowAlpha"
    )

    LaunchedEffect(Unit) {
        if (!reduceMotion) delay(100L)
        enhanced = true
        delay(if (reduceMotion) 300L else 500L)
        onAnimationComplete()
    }

    val background = if (isDarkMode) Brush.verticalGradient(listOf(Slate9, gradientBottomColor))
    else Brush.verticalGradient(listOf(Slate0, Slate1))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Glow is centred at 40 % from the top — matches the Column's weight(0.4f) spacer
            // below, avoiding a two-pass layout just to track the logo's position.
            val center = Offset(size.width / 2f, size.height * 0.40f)
            val glowRadius = if (isDarkMode) size.minDimension * 0.60f else size.minDimension * 0.55f
            val glowAlphaMax = if (isDarkMode) 0.30f else 0.55f
            val glowColor = if (isDarkMode) Cyan5 else Cyan6

            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.00f to glowColor.copy(alpha = glowAlphaMax * glowAlpha),
                        0.30f to glowColor.copy(alpha = glowAlphaMax * 0.40f * glowAlpha),
                        1.00f to Color.Transparent,
                    ),
                    center = center,
                    radius = glowRadius
                ),
                center = center,
                radius = glowRadius
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(0.4f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.ic_memoir_logo),
                    contentDescription = null, // decorative — app name in the Text below
                    modifier = Modifier.size(200.dp),
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = SplashTitleLetterSpacing,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.splash_tagline),
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                )
            }
            Spacer(Modifier.weight(0.6f))
        }
    }
}

@Preview(
    name = "Splash Screen - Light",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun SplashScreenLightPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        SplashContent(onAnimationComplete = {})
    }
}

@Preview(
    name = "Splash Screen - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun SplashScreenDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) {
        SplashContent(onAnimationComplete = {})
    }
}

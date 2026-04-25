package com.shadow.apps.memoir.ui.onboarding.setupcomplete

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadow.apps.memoir.R
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.Emerald500
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import com.shadow.apps.memoir.ui.theme.Slate0
import com.shadow.apps.memoir.ui.theme.Slate8
import com.shadow.apps.memoir.ui.theme.Slate9

@Composable
fun SetupCompleteScreen(onContinue: () -> Unit) {
    SetupCompleteContent(onContinue = onContinue)
}

@Composable
private fun SetupCompleteContent(onContinue: () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val backgroundModifier = if (isDark) {
        Modifier.background(Brush.verticalGradient(listOf(Slate9, Slate8)))
    } else {
        Modifier.background(Slate0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(backgroundModifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(color = Emerald500.copy(alpha = 0.12f), shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(color = Emerald500, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp),
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.setup_complete_label),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.5.sp,
                color = Emerald500,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.setup_complete_headline),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.setup_complete_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.weight(1f))
        }

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Cyan5,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.setup_complete_cta),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

/**
 * Previews
 */

@Preview(name = "SetupComplete – Light", showBackground = true, showSystemUi = true)
@Composable
private fun SetupCompleteLightPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        SetupCompleteContent(onContinue = {})
    }
}

@Preview(name = "SetupComplete – Dark", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SetupCompleteDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) {
        SetupCompleteContent(onContinue = {})
    }
}

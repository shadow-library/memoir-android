package com.shadow.apps.memoir.ui.onboarding.gettingstarted

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadow.apps.memoir.R
import com.shadow.apps.memoir.ui.onboarding.components.PageDots
import com.shadow.apps.memoir.ui.theme.Cyan1
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.Cyan6
import com.shadow.apps.memoir.ui.theme.Emerald200
import com.shadow.apps.memoir.ui.theme.Emerald500
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import com.shadow.apps.memoir.ui.theme.Slate0
import com.shadow.apps.memoir.ui.theme.Slate8
import com.shadow.apps.memoir.ui.theme.Slate9
import com.shadow.apps.memoir.ui.theme.Terracotta1
import com.shadow.apps.memoir.ui.theme.Terracotta5
import com.shadow.apps.memoir.ui.theme.Violet200
import com.shadow.apps.memoir.ui.theme.Violet500

private data class FeatureItem(
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val label: String,
    val description: String,
)

/**
 * Getting Started screen that introduces the four core modules.
 *
 * @param onGetStarted Called when the user taps the primary CTA. The host
 *   (MainActivity) is responsible for persisting the "onboarding complete" flag
 *   before navigating away.
 */
@Composable
fun GettingStartedScreen(onGetStarted: () -> Unit) {
    val isDark = isSystemInDarkTheme()

    val background = if (isDark) {
        Modifier.background(Brush.verticalGradient(listOf(Slate9, Slate8)))
    } else {
        Modifier.background(Slate0)
    }

    val features = listOf(
        FeatureItem(
            icon = Icons.Outlined.AccountBalanceWallet,
            iconBg = Cyan1,
            iconTint = Cyan6,
            label = stringResource(R.string.getting_started_feature_expenses_label),
            description = stringResource(R.string.getting_started_feature_expenses_desc),
        ),
        FeatureItem(
            icon = Icons.Outlined.MonitorHeart,
            iconBg = Emerald200,
            iconTint = Emerald500,
            label = stringResource(R.string.getting_started_feature_health_label),
            description = stringResource(R.string.getting_started_feature_health_desc),
        ),
        FeatureItem(
            icon = Icons.Outlined.AutoStories,
            iconBg = Terracotta1,
            iconTint = Terracotta5,
            label = stringResource(R.string.getting_started_feature_diary_label),
            description = stringResource(R.string.getting_started_feature_diary_desc),
        ),
        FeatureItem(
            icon = Icons.Outlined.Insights,
            iconBg = Violet200,
            iconTint = Violet500,
            label = stringResource(R.string.getting_started_feature_patterns_label),
            description = stringResource(R.string.getting_started_feature_patterns_desc),
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(background),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(56.dp))

            Text(
                text = stringResource(R.string.getting_started_welcome_label),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 3.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            )

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                /** Decorative icon; the app name follows in the adjacent Text. */
                Image(
                    painter = painterResource(R.drawable.ic_memoir_logo),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Spacer(Modifier.height(36.dp))

            Text(
                text = stringResource(R.string.getting_started_headline),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.getting_started_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )

            Spacer(Modifier.height(32.dp))

            features.forEachIndexed { index, item ->
                if (index > 0) Spacer(Modifier.height(16.dp))
                FeatureRow(item)
            }

            Spacer(Modifier.height(24.dp))
        }

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PageDots(total = 5, current = 0)

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Cyan5),
            ) {
                Text(
                    text = stringResource(R.string.getting_started_cta),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(Modifier.height(36.dp))
        }
    }
}

@Composable
private fun FeatureRow(item: FeatureItem) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color = item.iconBg, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = item.iconTint,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(Modifier.size(14.dp))

        Column {
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
            )
        }
    }
}

@Preview(
    name = "Getting Started – Light",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun GettingStartedLightPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        GettingStartedScreen(onGetStarted = {})
    }
}

@Preview(
    name = "Getting Started – Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun GettingStartedDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) {
        GettingStartedScreen(onGetStarted = {})
    }
}

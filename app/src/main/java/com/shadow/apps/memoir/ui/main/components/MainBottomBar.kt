package com.shadow.apps.memoir.ui.main.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadow.apps.memoir.ui.main.TabDestination
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme

@Composable
fun MainBottomBar(
    selectedTab: TabDestination,
    onTabSelected: (TabDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                TabDestination.entries.forEach { destination ->
                    TabItem(
                        destination = destination,
                        isSelected = selectedTab == destination,
                        onTabSelected = onTabSelected,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun TabItem(
    destination: TabDestination,
    isSelected: Boolean,
    onTabSelected: (TabDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onTabSelected(destination) },
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            Icon(
                imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                contentDescription = destination.label,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(24.dp),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = destination.label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}

@Preview(name = "Bottom Bar – Light", showBackground = true)
@Composable
private fun MainBottomBarLightPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        MainBottomBar(selectedTab = TabDestination.TODAY, onTabSelected = {})
    }
}

@Preview(name = "Bottom Bar – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MainBottomBarDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) {
        MainBottomBar(selectedTab = TabDestination.TODAY, onTabSelected = {})
    }
}

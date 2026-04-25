package com.shadow.apps.memoir.ui.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadow.apps.memoir.ui.theme.Cyan5

/**
 * Step-indicator dots shown at the bottom of each Getting Started setup screen.
 *
 * @param total   Total number of pages in the flow.
 * @param current 0-based index of the currently visible page.
 */
@Composable
fun PageDots(total: Int, current: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
    ) {
        repeat(total) { index ->
            val isActive = index == current
            Box(
                modifier = Modifier
                    .width(if (isActive) 20.dp else 8.dp)
                    .height(8.dp)
                    .background(
                        color = if (isActive || index < current) {
                            Cyan5
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.20f)
                        },
                        shape = RoundedCornerShape(50),
                    ),
            )
        }
    }
}

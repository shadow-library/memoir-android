package com.shadow.apps.memoir.ui.main.diary

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme

@Composable
fun DiaryScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Diary",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Preview(name = "Diary – Light", showBackground = true, showSystemUi = true)
@Composable
private fun DiaryLightPreview() {
    ShadowMemoirTheme(darkTheme = false) { DiaryScreen() }
}

@Preview(name = "Diary – Dark", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DiaryDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) { DiaryScreen() }
}

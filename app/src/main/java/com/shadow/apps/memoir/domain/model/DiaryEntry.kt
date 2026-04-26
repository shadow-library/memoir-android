package com.shadow.apps.memoir.domain.model

import java.time.LocalDate

data class DiaryEntry(
    val id: String,
    val content: String,
    val tags: List<String>,
    val date: LocalDate,
    val createdAt: Long,
    val updatedAt: Long,
    val deviceId: String,
    val version: Int = 1,
    val schemaVersion: Int = 1,
)

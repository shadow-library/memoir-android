package com.shadow.apps.memoir.domain.repository

import com.shadow.apps.memoir.domain.model.DiaryEntry

interface DiaryRepository {
    suspend fun add(entry: DiaryEntry): Result<Unit>
}

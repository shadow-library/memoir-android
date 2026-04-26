package com.shadow.apps.memoir.domain.usecase.record

import com.shadow.apps.memoir.domain.model.DiaryEntry
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import com.shadow.apps.memoir.domain.repository.DiaryRepository
import java.util.UUID
import javax.inject.Inject

class AddDiaryEntryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val configRepository: ConfigRepository,
) {
    suspend operator fun invoke(
        content: String,
        tags: List<String>,
        date: java.time.LocalDate,
    ): Result<Unit> {
        val now = System.currentTimeMillis()
        val deviceId = configRepository.loadDeviceId() ?: ""

        val entry = DiaryEntry(
            id = UUID.randomUUID().toString(),
            content = content,
            tags = tags,
            date = date,
            createdAt = now,
            updatedAt = now,
            deviceId = deviceId,
        )

        return diaryRepository.add(entry)
    }
}

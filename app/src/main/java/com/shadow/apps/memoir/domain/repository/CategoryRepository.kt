package com.shadow.apps.memoir.domain.repository

import com.shadow.apps.memoir.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observe(): Flow<List<Category>>
    suspend fun seedDefaultsIfMissing()
}

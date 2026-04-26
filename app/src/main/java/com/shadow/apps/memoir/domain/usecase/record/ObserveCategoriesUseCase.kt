package com.shadow.apps.memoir.domain.usecase.record

import com.shadow.apps.memoir.domain.model.Category
import com.shadow.apps.memoir.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) {
    operator fun invoke(): Flow<List<Category>> = categoryRepository.observe()
}

package com.shadow.apps.memoir.domain.seed

import com.shadow.apps.memoir.domain.repository.CategoryRepository
import com.shadow.apps.memoir.domain.repository.CurrencyRepository
import javax.inject.Inject
import javax.inject.Singleton

data class SeedContext(
    val defaultCurrencyCode: String = "",
    val defaultCurrencyName: String = "",
) {
    val hasContext: Boolean = defaultCurrencyCode.isNotBlank()
}

class SeedDefinition(
    val id: String,
    val requiresContext: Boolean = false,
    val apply: suspend (SeedContext) -> Unit,
)

@Singleton
class SeedRegistry @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val currencyRepository: CurrencyRepository,
) {
    val all: List<SeedDefinition> = listOf(
        SeedDefinition(id = "categories_v1") { _ ->
            categoryRepository.seedDefaultsIfMissing()
        },
        SeedDefinition(id = "currencies_v1", requiresContext = true) { ctx ->
            currencyRepository.seedDefaults(
                defaultCurrencyCode = ctx.defaultCurrencyCode,
                defaultCurrencyName = ctx.defaultCurrencyName.ifBlank { ctx.defaultCurrencyCode },
            )
        },
    )
}

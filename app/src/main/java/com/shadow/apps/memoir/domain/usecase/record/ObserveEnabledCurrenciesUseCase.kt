package com.shadow.apps.memoir.domain.usecase.record

import com.shadow.apps.memoir.domain.model.EnabledCurrency
import com.shadow.apps.memoir.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEnabledCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) {
    operator fun invoke(): Flow<List<EnabledCurrency>> = currencyRepository.observe()
}

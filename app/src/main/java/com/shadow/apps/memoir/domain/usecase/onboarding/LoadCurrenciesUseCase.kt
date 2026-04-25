package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.model.CurrencyListResult
import com.shadow.apps.memoir.domain.repository.FxRepository
import javax.inject.Inject

/*
 * Load currencies
 *
 * Provides supported currency choices for device setup.
 */
class LoadCurrenciesUseCase @Inject constructor(
    private val fxRepository: FxRepository,
) {
    /*
     * Execution
     */
    suspend operator fun invoke(): CurrencyListResult = fxRepository.getCurrencies()
}

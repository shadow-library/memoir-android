package com.shadow.apps.memoir.di

import com.shadow.apps.memoir.data.firebase.FirebaseManager
import com.shadow.apps.memoir.data.firebase.auth.FirebaseAuthRepository
import com.shadow.apps.memoir.data.firebase.category.FirebaseCategoryRepository
import com.shadow.apps.memoir.data.firebase.currency.FirebaseCurrencyRepository
import com.shadow.apps.memoir.data.firebase.diary.FirebaseDiaryRepository
import com.shadow.apps.memoir.data.firebase.expense.FirebaseExpenseRepository
import com.shadow.apps.memoir.data.firebase.profile.FirebaseProfileRepository
import com.shadow.apps.memoir.data.fx.FrankfurterFxRepository
import com.shadow.apps.memoir.data.preferences.EncryptedConfigRepository
import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.CategoryRepository
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import com.shadow.apps.memoir.domain.repository.CurrencyRepository
import com.shadow.apps.memoir.domain.repository.DiaryRepository
import com.shadow.apps.memoir.domain.repository.ExpenseRepository
import com.shadow.apps.memoir.domain.repository.FirebaseAppRepository
import com.shadow.apps.memoir.domain.repository.FxRepository
import com.shadow.apps.memoir.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/*
 * Repository bindings
 *
 * Connects domain repository contracts to concrete data sources.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /*
     * Firebase repositories
     */
    @Binds
    abstract fun bindFirebaseAppRepository(
        repository: FirebaseManager,
    ): FirebaseAppRepository

    @Binds
    abstract fun bindAuthRepository(
        repository: FirebaseAuthRepository,
    ): AuthRepository

    @Binds
    abstract fun bindProfileRepository(
        repository: FirebaseProfileRepository,
    ): ProfileRepository

    /*
     * Stores
     */
    @Binds
    abstract fun bindConfigRepository(
        repository: EncryptedConfigRepository,
    ): ConfigRepository

    /*
     * Record repositories
     */
    @Binds
    abstract fun bindExpenseRepository(
        repository: FirebaseExpenseRepository,
    ): ExpenseRepository

    @Binds
    abstract fun bindDiaryRepository(
        repository: FirebaseDiaryRepository,
    ): DiaryRepository

    @Binds
    abstract fun bindCategoryRepository(
        repository: FirebaseCategoryRepository,
    ): CategoryRepository

    @Binds
    abstract fun bindCurrencyRepository(
        repository: FirebaseCurrencyRepository,
    ): CurrencyRepository

    /*
     * External service repositories
     */
    @Binds
    abstract fun bindFxRepository(
        repository: FrankfurterFxRepository,
    ): FxRepository
}

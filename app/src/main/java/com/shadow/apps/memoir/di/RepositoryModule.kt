package com.shadow.apps.memoir.di

import com.shadow.apps.memoir.data.firebase.FirebaseManager
import com.shadow.apps.memoir.data.firebase.auth.FirebaseAuthRepository
import com.shadow.apps.memoir.data.firebase.profile.FirebaseProfileRepository
import com.shadow.apps.memoir.data.fx.FrankfurterFxRepository
import com.shadow.apps.memoir.data.preferences.EncryptedConfigRepository
import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.ConfigRepository
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
     * External service repositories
     */
    @Binds
    abstract fun bindFxRepository(
        repository: FrankfurterFxRepository,
    ): FxRepository
}

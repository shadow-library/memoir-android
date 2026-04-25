package com.shadow.apps.memoir.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
 * Preferences
 *
 * Provides app-private preferences. Values are encrypted by EncryptedConfigStore.
 */
@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    private const val PREFS_FILE = "shadow_memoir_secure_config"

    /*
     * Stores
     */
    @Provides
    @Singleton
    fun provideSecureConfigPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences =
        context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
}

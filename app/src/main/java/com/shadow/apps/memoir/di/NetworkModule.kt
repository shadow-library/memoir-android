package com.shadow.apps.memoir.di

import com.shadow.apps.memoir.data.firebase.FirebaseAuthApi
import com.shadow.apps.memoir.data.firebase.GoogleOAuthApi
import com.shadow.apps.memoir.data.fx.FrankfurterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/*
 * Network clients
 *
 * Provides the small HTTP stack used by non-Firebase APIs.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /*
     * OkHttp
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    /*
     * Retrofit — Frankfurter currency API
     */
    @Provides
    @Singleton
    @Named("frankfurter")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.frankfurter.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideFrankfurterApi(@Named("frankfurter") retrofit: Retrofit): FrankfurterApi =
        retrofit.create(FrankfurterApi::class.java)

    /*
     * Retrofit — Firebase Identity Toolkit
     */
    @Provides
    @Singleton
    @Named("firebase")
    fun provideFirebaseRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://identitytoolkit.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideFirebaseAuthApi(@Named("firebase") retrofit: Retrofit): FirebaseAuthApi =
        retrofit.create(FirebaseAuthApi::class.java)

    /*
     * Retrofit — Google OAuth 2.0
     */
    @Provides
    @Singleton
    @Named("googleOAuth")
    fun provideGoogleOAuthRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://oauth2.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGoogleOAuthApi(@Named("googleOAuth") retrofit: Retrofit): GoogleOAuthApi =
        retrofit.create(GoogleOAuthApi::class.java)
}

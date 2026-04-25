package com.shadow.apps.memoir.di

import com.shadow.apps.memoir.data.fx.FrankfurterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
     * Retrofit
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.frankfurter.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideFrankfurterApi(retrofit: Retrofit): FrankfurterApi =
        retrofit.create(FrankfurterApi::class.java)
}

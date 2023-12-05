package com.dominate.thirtySecondsChallenge.data.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dominate.thirtySecondsChallenge.data.clients.UnsafeOkHttpClient.Companion.ignoreAllSSLErrors
import com.dominate.thirtySecondsChallenge.data.common.NetworkConstants.APP_BASE_URL
import com.dominate.thirtySecondsChallenge.data.common.NetworkConstants.APP_TIMEOUT_MINUTES
import com.dominate.thirtySecondsChallenge.data.interceptors.AppBaseInterceptor
import com.dominate.thirtySecondsChallenge.data.pref.configuration.ConfigurationPref
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideInterceptor(
        configurationPref: ConfigurationPref,
        userPref: UserPref
    ): AppBaseInterceptor = AppBaseInterceptor(
        configurationPref,
        userPref
    )

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        interceptor: AppBaseInterceptor,
        loggerInterceptor: HttpLoggingInterceptor,
        application: Application
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
            .ignoreAllSSLErrors()
            .addNetworkInterceptor(interceptor)
            .readTimeout(APP_TIMEOUT_MINUTES, TimeUnit.MINUTES)
            .writeTimeout(APP_TIMEOUT_MINUTES, TimeUnit.MINUTES)
            .connectTimeout(APP_TIMEOUT_MINUTES, TimeUnit.MINUTES)

//        if (BuildConfig.DEBUG)
//            okHttpClient
//                .addInterceptor(ChuckerInterceptor(application))
//                .addInterceptor(loggerInterceptor)

            .addInterceptor(ChuckerInterceptor(application))

            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                request.addHeader("Accept", "application/json")

                val response = chain.proceed(request.build())
                if (response.code == 401) {
                    EventBus.getDefault().post(response.code.toString())
                }

                response

            }

        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(APP_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

}
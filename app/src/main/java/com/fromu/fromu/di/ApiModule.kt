package com.fromu.fromu.di

import com.fromu.fromu.data.remote.network.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideSignupService(retrofit: Retrofit): SignupService {
        return retrofit.create(SignupService::class.java)
    }

    @Provides
    @Singleton
    fun provideInvitationService(retrofit: Retrofit): InvitationService {
        return retrofit.create(InvitationService::class.java)
    }

    @Provides
    @Singleton
    fun provideCoupleService(retrofit: Retrofit): CoupleService {
        return retrofit.create(CoupleService::class.java)
    }

    @Provides
    @Singleton
    fun provideViewService(retrofit: Retrofit): ViewService {
        return retrofit.create(ViewService::class.java)
    }

    @Provides
    @Singleton
    fun provideDiaryService(retrofit: Retrofit): DiaryService {
        return retrofit.create(DiaryService::class.java)
    }

    @Provides
    @Singleton
    fun providePushService(retrofit: Retrofit): PushService {
        return retrofit.create(PushService::class.java)
    }
}
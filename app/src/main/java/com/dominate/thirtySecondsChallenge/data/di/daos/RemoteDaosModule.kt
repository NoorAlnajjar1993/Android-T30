package com.dominate.thirtySecondsChallenge.data.di.daos

import com.dominate.thirtySecondsChallenge.data.daos.remote.applicationsetting.ApplicationSettingRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.auth.UserRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.friends.FriendsRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.gift.GiftRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.home.HomeRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.leader.LeaderRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.menu.MenuRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.notification.NotificationRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.player.PlayerRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.profile.ProfileRemoteDao
import com.dominate.thirtySecondsChallenge.data.daos.remote.store.StoreRemoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RemoteDaosModule {

    @Singleton
    @Provides
    fun provideUserRemoteDao(
        retrofit: Retrofit
    ): UserRemoteDao {
        return retrofit.create(UserRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileRemoteDao(
        retrofit: Retrofit
    ): ProfileRemoteDao {
        return retrofit.create(ProfileRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun providePlayerRemoteDao(
        retrofit: Retrofit
    ): PlayerRemoteDao {
        return retrofit.create(PlayerRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeRemoteDao(
        retrofit: Retrofit
    ): HomeRemoteDao {
        return retrofit.create(HomeRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideGiftRemoteDao(
        retrofit: Retrofit
    ): GiftRemoteDao {
        return retrofit.create(GiftRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideFriendsRemoteDao(
        retrofit: Retrofit
    ): FriendsRemoteDao {
        return retrofit.create(FriendsRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideApplicationSettingRemoteDao(
        retrofit: Retrofit
    ): ApplicationSettingRemoteDao {
        return retrofit.create(ApplicationSettingRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideStoreRemoteDao(
        retrofit: Retrofit
    ): StoreRemoteDao {
        return retrofit.create(StoreRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideLeaderRemoteDao(
        retrofit: Retrofit
    ): LeaderRemoteDao {
        return retrofit.create(LeaderRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideMenuRemoteDao(
        retrofit: Retrofit
    ): MenuRemoteDao {
        return retrofit.create(MenuRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideNotificationRemoteDao(
        retrofit: Retrofit
    ): NotificationRemoteDao {
        return retrofit.create(NotificationRemoteDao::class.java)
    }

}
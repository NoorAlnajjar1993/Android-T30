package com.dominate.thirtySecondsChallenge.data.di

import com.dominate.thirtySecondsChallenge.data.repo.applicationsetting.ApplicationSettingRepo
import com.dominate.thirtySecondsChallenge.data.repo.applicationsetting.ApplicationSettingRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.auth.UserRepo
import com.dominate.thirtySecondsChallenge.data.repo.auth.UserRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.friends.FriendsRepo
import com.dominate.thirtySecondsChallenge.data.repo.friends.FriendsRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.gift.GiftRepo
import com.dominate.thirtySecondsChallenge.data.repo.gift.GiftRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.home.HomeRepo
import com.dominate.thirtySecondsChallenge.data.repo.home.HomeRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.leader.LeaderRepo
import com.dominate.thirtySecondsChallenge.data.repo.leader.LeaderRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.menu.MenuRepo
import com.dominate.thirtySecondsChallenge.data.repo.menu.MenuRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.notification.NotificationRepo
import com.dominate.thirtySecondsChallenge.data.repo.notification.NotificationRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.player.PlayerRepo
import com.dominate.thirtySecondsChallenge.data.repo.player.PlayerRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.profile.ProfileRepo
import com.dominate.thirtySecondsChallenge.data.repo.profile.ProfileRepoImp
import com.dominate.thirtySecondsChallenge.data.repo.store.StoreRepo
import com.dominate.thirtySecondsChallenge.data.repo.store.StoreRepoImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun bindAuthRepo(authRepoImp: UserRepoImp): UserRepo

    @Singleton
    @Binds
    abstract fun bindProfileRepo(profileRepoImp: ProfileRepoImp): ProfileRepo

    @Singleton
    @Binds
    abstract fun bindPlayerRepo(playerRepoImp: PlayerRepoImp): PlayerRepo

    @Singleton
    @Binds
    abstract fun bindHomeRepo(homeRepoImp: HomeRepoImp): HomeRepo

    @Singleton
    @Binds
    abstract fun bindGiftRepo(giftRepoImp: GiftRepoImp): GiftRepo

    @Singleton
    @Binds
    abstract fun bindFriendsRepo(friendsRepoImp: FriendsRepoImp): FriendsRepo

    @Singleton
    @Binds
    abstract fun bindApplicationSettingRepo(applicationSettingRepoImp: ApplicationSettingRepoImp): ApplicationSettingRepo

    @Singleton
    @Binds
    abstract fun bindStoreRepo(storeRepoImp: StoreRepoImp): StoreRepo

    @Singleton
    @Binds
    abstract fun bindLeaderRepo(leaderRepoImp: LeaderRepoImp): LeaderRepo

    @Singleton
    @Binds
    abstract fun bindMenuRepo(menuRepoImp: MenuRepoImp): MenuRepo

    @Singleton
    @Binds
    abstract fun bindNotificationRepo(notificationRepoImp: NotificationRepoImp): NotificationRepo

}
package com.dominate.thirtySecondsChallenge.data.daos.remote.menu

import com.dominate.thirtySecondsChallenge.data.model.menu.CategoryGetMainResponseModel
import com.dominate.thirtySecondsChallenge.data.model.menu.GamesRunningResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.request.ReportPlayerAddRequestModel
import com.dominate.thirtySecondsChallenge.data.model.store.CoinsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.DiamondsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.MainCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.OffersResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path

interface MenuRemoteDao {

    @GET("api/Category/GetMain")
    fun getCategoryGetMain(
    ): Single<ResponseWrapper<List<CategoryGetMainResponseModel >>>


}

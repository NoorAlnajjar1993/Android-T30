package com.dominate.thirtySecondsChallenge.data.repo.menu

import com.dominate.thirtySecondsChallenge.data.model.menu.CategoryGetMainResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single


interface MenuRepo {

    fun getCategoryGetMain(
    ): Single<ResponseWrapper<List<CategoryGetMainResponseModel>>>

}
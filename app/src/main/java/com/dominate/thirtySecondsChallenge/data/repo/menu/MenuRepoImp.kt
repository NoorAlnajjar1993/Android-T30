package com.dominate.thirtySecondsChallenge.data.repo.menu

import com.dominate.thirtySecondsChallenge.data.daos.remote.menu.MenuRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.menu.CategoryGetMainResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class MenuRepoImp @Inject constructor(
    private val menuRemoteDao: MenuRemoteDao
) : BaseRepo(), MenuRepo {

    override fun getCategoryGetMain(): Single<ResponseWrapper<List<CategoryGetMainResponseModel>>> {
        return menuRemoteDao.getCategoryGetMain()
    }


}
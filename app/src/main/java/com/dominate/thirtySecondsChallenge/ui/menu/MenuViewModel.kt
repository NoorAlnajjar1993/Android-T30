package com.dominate.thirtySecondsChallenge.ui.menu

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.menu.CategoryGetMainResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.menu.MenuRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MenuViewModel @ViewModelInject constructor(
    private val menuRepo: MenuRepo,
) : BaseViewModel() {

    // get Category
    val categoryResult: MutableLiveData<Result<List<CategoryGetMainResponseModel>>> =
        MutableLiveData()

    val _categoryListLiveData = MutableLiveData<List<CategoryGetMainResponseModel>>()
    val categoryListLiveData: LiveData<List<CategoryGetMainResponseModel>> get() = _categoryListLiveData

    fun getCategoryGetMain(): MutableLiveData<Result<List<CategoryGetMainResponseModel>>> {
        categoryResult.value = Result.Loading
        compositeDisposable + menuRepo.getCategoryGetMain()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    categoryResult.value = Result.Success(it.data, it.message)
                } else {
                    categoryResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                categoryResult.value = Result.Error(it)
            })
        return categoryResult
    }

}
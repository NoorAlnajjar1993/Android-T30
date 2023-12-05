package com.dominate.thirtySecondsChallenge.ui.shoppingcart

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.AccessoriesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.CoinsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.DiamondsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.MainCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.OffersResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.repo.store.StoreRepo
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.dominate.thirtySecondsChallenge.data.response.Result

class ShoppingCartViewModel @ViewModelInject constructor(
    private val storeRepo: StoreRepo,
    private val userPref: UserPref,

    ) : BaseViewModel() {

    val isShow: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isPoster: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }

    val _isSendFriendsLiveData = MutableLiveData<Boolean>(false)
    val isSendFriendsLiveData: LiveData<Boolean> get() = _isSendFriendsLiveData

    // giftDialog
    val imageDialog: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val titleDialog: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val descriptionDialog: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val priceDialog: MutableLiveData<Float> by lazy { MutableLiveData<Float>() }
    val priceIsoDialog: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val titleBtn: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val typeBtn: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    val mainCategoryResult: MutableLiveData<Result<List<MainCategoryResponseModel>>> =
        MutableLiveData()

    val _mainCategoryLiveData = MutableLiveData<List<MainCategoryResponseModel>>()
    val mainCategoryListLiveData: LiveData<List<MainCategoryResponseModel>> get() = _mainCategoryLiveData

    fun getMainCategory(): MutableLiveData<Result<List<MainCategoryResponseModel>>> {
        mainCategoryResult.value = Result.Loading
        compositeDisposable + storeRepo.getMainCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let { it1 ->
                        mainCategoryResult.value = Result.Success(it1, it.message)
                        _mainCategoryLiveData.value = it1 ?: arrayListOf()
                    }

                } else {
                    mainCategoryResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                mainCategoryResult.value = Result.Error(it)
            })
        return mainCategoryResult
    }


    val itemsCoinsResult: MutableLiveData<Result<List<CoinsResponseModel>>> = MutableLiveData()

    val _itemsCoinsLiveData = MutableLiveData<List<CoinsResponseModel>>()
    val itemsCoinsListLiveData: LiveData<List<CoinsResponseModel>> get() = _itemsCoinsLiveData

    fun getItemsCoins(): MutableLiveData<Result<List<CoinsResponseModel>>> {
        itemsCoinsResult.value = Result.Loading
        compositeDisposable + storeRepo.getItemsCoins()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let { it1 ->
                        itemsCoinsResult.value = Result.Success(it1, it.message)
//                        _itemsCoinsLiveData.value = it1 ?: arrayListOf()
                    }

                } else {
                    itemsCoinsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                itemsCoinsResult.value = Result.Error(it)
            })
        return itemsCoinsResult
    }

    // get Items Diamonds
    val itemsDiamondsResult: MutableLiveData<Result<List<DiamondsResponseModel>>> =
        MutableLiveData()

    val _itemsDiamondsLiveData = MutableLiveData<List<DiamondsResponseModel>>()
    val itemsDiamondsListLiveData: LiveData<List<DiamondsResponseModel>> get() = _itemsDiamondsLiveData

    fun getItemsDiamonds(): MutableLiveData<Result<List<DiamondsResponseModel>>> {
        itemsDiamondsResult.value = Result.Loading
        compositeDisposable + storeRepo.getItemsDiamonds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let { it1 ->
                        itemsDiamondsResult.value = Result.Success(it1, it.message)
//                        _itemsDiamondsLiveData.value = it1 ?: arrayListOf()
                    }

                } else {
                    itemsDiamondsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                itemsDiamondsResult.value = Result.Error(it)
            })
        return itemsDiamondsResult
    }

    // get Items Gifts
    val itemsGiftsResult: MutableLiveData<Result<List<GiftsResponseModel>>> = MutableLiveData()

    val _itemsGiftsLiveData = MutableLiveData<List<GiftsResponseModel>>()
    val itemsGiftsListLiveData: LiveData<List<GiftsResponseModel>> get() = _itemsGiftsLiveData

    fun getItemsGifts(): MutableLiveData<Result<List<GiftsResponseModel>>> {
        itemsGiftsResult.value = Result.Loading
        compositeDisposable + storeRepo.getItemsGifts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let { it1 ->
                        itemsGiftsResult.value = Result.Success(it1, it.message)
                        _itemsGiftsLiveData.value = it1 ?: arrayListOf()
                    }

                } else {
                    itemsGiftsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                itemsGiftsResult.value = Result.Error(it)
            })
        return itemsGiftsResult
    }

    // get Items Offers
    val itemsOffersResult: MutableLiveData<Result<List<OffersResponseModel>>> = MutableLiveData()

    val _itemsOffersLiveData = MutableLiveData<List<OffersResponseModel>>()
    val itemsOffersListLiveData: LiveData<List<OffersResponseModel>> get() = _itemsOffersLiveData

    fun getItemsOffers(): MutableLiveData<Result<List<OffersResponseModel>>> {
        itemsOffersResult.value = Result.Loading
        compositeDisposable + storeRepo.getItemsOffers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let { it1 ->
                        itemsOffersResult.value = Result.Success(it1, it.message)
                        _itemsOffersLiveData.value = it1 ?: arrayListOf()
                    }

                } else {
                    itemsOffersResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                itemsOffersResult.value = Result.Error(it)
            })
        return itemsOffersResult
    }

    // get Stickers
    val itemsStickersResult: MutableLiveData<Result<List<AccessoriesResponseModel>>> =
        MutableLiveData()

    val _itemsStickersLiveData = MutableLiveData<List<AccessoriesResponseModel>>()
    val itemsStickersListLiveData: LiveData<List<AccessoriesResponseModel>> get() = _itemsStickersLiveData

    fun getStickers(): MutableLiveData<Result<List<AccessoriesResponseModel>>> {
        itemsStickersResult.value = Result.Loading
        compositeDisposable + storeRepo.getStickers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let { it1 ->
                        itemsStickersResult.value = Result.Success(it1, it.message)
                        _itemsStickersLiveData.value = it1 ?: arrayListOf()
                    }

                } else {
                    itemsStickersResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                itemsStickersResult.value = Result.Error(it)
            })
        return itemsStickersResult
    }

    // get Backgrounds
    val itemsBackgroundsResult: MutableLiveData<Result<List<AccessoriesResponseModel>>> =
        MutableLiveData()

    val _itemsBackgroundsLiveData = MutableLiveData<List<AccessoriesResponseModel>>()
    val itemsBackgroundsListLiveData: LiveData<List<AccessoriesResponseModel>> get() = _itemsBackgroundsLiveData

    fun getBackgrounds(): MutableLiveData<Result<List<AccessoriesResponseModel>>> {
        itemsBackgroundsResult.value = Result.Loading
        compositeDisposable + storeRepo.getBackgrounds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let { it1 ->
                        itemsBackgroundsResult.value = Result.Success(it1, it.message)
                        _itemsBackgroundsLiveData.value = it1 ?: arrayListOf()
                    }

                } else {
                    itemsBackgroundsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                itemsBackgroundsResult.value = Result.Error(it)
            })
        return itemsBackgroundsResult
    }

    fun clearDataGiftDialog() {
        imageDialog.value = ""
        titleDialog.value = ""
        descriptionDialog.value = ""
        priceDialog.value = 0.0f
        priceIsoDialog.value = 0
        titleBtn.value = ""
    }

}
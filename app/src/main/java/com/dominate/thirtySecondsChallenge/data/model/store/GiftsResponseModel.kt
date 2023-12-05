package com.dominate.thirtySecondsChallenge.data.model.store

import com.squareup.moshi.Json

data class GiftsResponseModel(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "image")
    val image: String,
    @field:Json(name = "imageUrl")
    val imageUrl: String,
    @field:Json(name = "price")
    val price: Float,
    @field:Json(name = "discount")
    val discount: Int,
    @field:Json(name = "priceAfterDiscount")
    val priceAfterDiscount: Float,
    @field:Json(name = "earnCoins")
    val earnCoins: Float,
    @field:Json(name = "earnDiamond")
    val earnDiamond: Float,
    @field:Json(name = "privileges")
    val privileges: String?,
    @field:Json(name = "duration")
    val duration: Int,
    @field:Json(name = "isSubscription")
    val isSubscription: Boolean,
    @field:Json(name = "currencyId")
    val currencyId: Int,
    @field:Json(name = "currencyTitle")
    val currencyTitle: String,
    @field:Json(name = "currencyLogo")
    val currencyLogo: String?,
    @field:Json(name = "currencyIsoCode")
    val currencyIsoCode: String?,
    @field:Json(name = "categoryId")
    val categoryId: Int,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "language")
    val language: String,


)

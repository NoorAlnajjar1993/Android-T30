package com.dominate.thirtySecondsChallenge.ui.shoppingcart.model

data class CurrenciesModel(

    val image : Int,
    val title:String,
    val priceCoin:String,
    val priceUsd:String,
    val isOffer:Boolean = false,
    val offer:String = "10% Off",
    val priceOffer : String = "8.000KWD"

)

package com.dominate.thirtySecondsChallenge.data.model.home

import com.squareup.moshi.Json

data class HomeResponseModel(

    @field:Json(name = "banners")
    val banners: List<BannersResponseModel>,
    @field:Json(name = "gameTypes")
    val gameTypes: List<ActiveGameTypesResponseModel>,
    @field:Json(name = "ads")
    val ads: List<AdsResponseModel>,

)

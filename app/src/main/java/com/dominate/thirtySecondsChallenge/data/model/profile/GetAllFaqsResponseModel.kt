package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class GetAllFaqsResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "faqCategoryId")
    val faqCategoryId: Int,
    @field:Json(name = "question")
    val question: String,
    @field:Json(name = "answer")
    val answer: String,
    var isSelected:Boolean = false

)

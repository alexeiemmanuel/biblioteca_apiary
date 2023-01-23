package com.aemm.libreria.model

import com.google.gson.annotations.SerializedName

data class BookDetail(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("author")
    val author: String? = null,

    @SerializedName("release")
    val release: String? = null,

    @SerializedName("edition")
    val edition: String? = null,

    @SerializedName("editorial")
    val editorial: String? = null,

    @SerializedName("isbn")
    val isbn: String? = null,

    @SerializedName("genre")
    val genre: String? = null,

    @SerializedName("synopsis")
    val synopsis: String? = null,

    @SerializedName("thumbnail")
    val thumbnail: String? = null
)

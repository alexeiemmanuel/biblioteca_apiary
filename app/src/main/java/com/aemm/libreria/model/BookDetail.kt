package com.aemm.libreria.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val editorial: Editorial? = null,

    @SerializedName("isbn")
    val isbn: String? = null,

    @SerializedName("genre")
    val genre: String? = null,

    @SerializedName("synopsis")
    val synopsis: String? = null,

    @SerializedName("thumbnail")
    val thumbnail: String? = null
) : Parcelable

@Parcelize
data class Editorial(

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("schedule")
    val schedule: String? = null,

    @SerializedName("telephone")
    val telephone: String? = null,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
) : Parcelable
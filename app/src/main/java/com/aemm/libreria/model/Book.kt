package com.aemm.libreria.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Modelo que contiene información de un libro y nos permitira utilizarla
 * al mostrar un listado de libros.
 */
@Parcelize
data class Book(

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("release")
    val release: String? = null,

    @SerializedName("synopsis")
    val synopsis: String? = null,

    @SerializedName("thumbnail")
    val thumbnail: String? = null
) : Parcelable

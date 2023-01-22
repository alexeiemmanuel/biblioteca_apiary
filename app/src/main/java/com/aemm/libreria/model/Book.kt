package com.aemm.libreria.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo que contiene información de un libro y nos permitira utilizarla
 * al mostrar un listado de libros.
 */
data class Book(

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("release")
    val release: String? = null,

    @SerializedName("thumbnail")
    val thumbnail: String? = null
)

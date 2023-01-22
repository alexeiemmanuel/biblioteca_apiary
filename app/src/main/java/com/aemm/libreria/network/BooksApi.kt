package com.aemm.libreria.network

import com.aemm.libreria.model.Book
import com.aemm.libreria.model.BookDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Interface que contiene los Endpoints para consumir de Apiary
 */
interface BooksApi {

    @GET
    fun getBooks(
        @Url url: String?
    ): Call<ArrayList<Book>>

    @GET("book/{id}")
    fun getBookDetail(
        @Path("id") id: String?
    ): Call<BookDetail>
}
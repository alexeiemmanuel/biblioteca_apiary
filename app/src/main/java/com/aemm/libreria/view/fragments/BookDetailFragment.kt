package com.aemm.libreria.view.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.navArgs
import com.aemm.libreria.R
import com.aemm.libreria.databinding.FragmentBookDetailBinding
import com.aemm.libreria.model.BookDetail
import com.aemm.libreria.network.BooksApi
import com.aemm.libreria.network.RetrofitService
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookDetailFragment : Fragment(R.layout.fragment_book_detail) {

    private var idBook: String? = null

    private lateinit var binding: FragmentBookDetailBinding

    private val args: BookDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = FragmentBookDetailBinding.bind(view)
        this.idBook = this.args.idBook


        CoroutineScope(Dispatchers.IO).launch {

            val call = RetrofitService.getRetrofit()
                .create(BooksApi::class.java)
                .getBookDetail(idBook)

            call.enqueue(object : Callback<BookDetail> {
                override fun onResponse(call: Call<BookDetail>, response: Response<BookDetail>) {

                    binding.apply {

                        tvTitle.text = response.body()?.title
                        tvAuthor.text = response.body()?.author
                        tvRelease.text = this@BookDetailFragment.requireContext()
                            .getString(R.string.book_release_label)
                            .plus(" ")
                            .plus(response.body()?.release)
                        tvSinopsys.text = response.body()?.synopsis
                        tvGenre.text = response.body()?.genre
                        loader.visibility = View.GONE

                        Glide.with(this@BookDetailFragment.requireContext())
                            .load(response.body()?.thumbnail)
                            .into(ivPhoto)
                    }
                }

                override fun onFailure(call: Call<BookDetail>, t: Throwable) {
                    AlertDialog.Builder(this@BookDetailFragment.requireContext())
                        .setTitle(getString(R.string.message_network_error_title))
                        .setMessage(getString(R.string.message_network_error_message))
                        .setPositiveButton(getString(R.string.message_network_error_dimmis), DialogInterface.OnClickListener { _, _ ->
                        })
                        .create()
                        .show()

                    binding.loader.visibility = View.GONE
                }


            })
        }

    }

}
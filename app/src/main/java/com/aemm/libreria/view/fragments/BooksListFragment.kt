package com.aemm.libreria.view.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aemm.libreria.R
import com.aemm.libreria.databinding.FragmentBooksListBinding
import com.aemm.libreria.model.Book
import com.aemm.libreria.network.BooksApi
import com.aemm.libreria.network.RetrofitService
import com.aemm.libreria.view.adapters.BookAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BooksListFragment : Fragment(R.layout.fragment_books_list) {

    private lateinit var binding: FragmentBooksListBinding

    private lateinit var adapter: BookAdapter

    private var list: ArrayList<Book> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.binding = FragmentBooksListBinding.bind(view)

        binding.refresh.setOnRefreshListener {
            getListBooks()
        }

        getListBooks()

    }

    /**
     * Método que consulta la API para obtener un lista random de Books
     */
    private fun getListBooks(){

        CoroutineScope(Dispatchers.IO).launch {

            val call = RetrofitService.getRetrofit().create(BooksApi::class.java)
                .getBooks("books")

            call.enqueue(object : Callback<ArrayList<Book>> {
                override fun onResponse(
                    call: Call<ArrayList<Book>>,
                    response: Response<ArrayList<Book>>
                ) {
                    list = response.body()!!
                    list.shuffle()
                    binding.loader.visibility = View.GONE
                    adapter = BookAdapter(this@BooksListFragment.requireContext(), list)
                    binding.reciclerView.layoutManager =  LinearLayoutManager(this@BooksListFragment.requireContext(), RecyclerView.VERTICAL, false)
                    binding.reciclerView.adapter = adapter
                    binding.reciclerView.visibility = View.VISIBLE
                    binding.refresh.isRefreshing = false

                }

                override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {
                    AlertDialog.Builder(this@BooksListFragment.requireContext())
                        .setTitle(getString(R.string.message_network_error_title))
                        .setMessage(getString(R.string.message_network_error_message))
                        .setPositiveButton(getString(R.string.message_network_error_dimmis), DialogInterface.OnClickListener { _, _ ->
                        })
                        .create()
                        .show()
                    binding.loader.visibility = View.GONE
                    binding.reciclerView.visibility = View.INVISIBLE
                    binding.refresh.isRefreshing = false
                }
            })

        }
    }
}
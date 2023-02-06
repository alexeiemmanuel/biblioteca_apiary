package com.aemm.libreria.view.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aemm.libreria.R
import com.aemm.libreria.databinding.FragmentBooksListBinding
import com.aemm.libreria.model.Book
import com.aemm.libreria.network.BooksApi
import com.aemm.libreria.network.RetrofitService
import com.aemm.libreria.view.activities.LoginActivity
import com.aemm.libreria.view.adapters.BookAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.binding = FragmentBooksListBinding.bind(view)
        firebaseAuth = FirebaseAuth.getInstance()

        user = firebaseAuth.currentUser

        if(user?.isEmailVerified != true){
            binding.refresh.isRefreshing = false

            val emailDialog = AlertDialog.Builder(this@BooksListFragment.requireContext())
            emailDialog.setTitle(getString(R.string.dialog_verfy_acount))
            emailDialog.setMessage(getString(R.string.dialog_verfy_acount_message, user?.email))

            emailDialog.setPositiveButton(
                getString(R.string.dialog_btn_positive),
                DialogInterface.OnClickListener { dialog, which ->
                    user?.sendEmailVerification()?.addOnSuccessListener {
                        val intent = Intent(this@BooksListFragment.context, LoginActivity::class.java)
                        startActivity(intent)
                        this@BooksListFragment.activity?.finish()
                        Toast.makeText(this@BooksListFragment.context, getString(R.string.dialog_create_sucess), Toast.LENGTH_SHORT).show()
                    }?.addOnFailureListener {
                        val intent = Intent(this@BooksListFragment.context, LoginActivity::class.java)
                        startActivity(intent)
                        this@BooksListFragment.activity?.finish()
                    }
                })

            emailDialog.setNegativeButton(getString(R.string.dialog_btn_negative), DialogInterface.OnClickListener { _, _ ->
                val intent = Intent(this@BooksListFragment.context, LoginActivity::class.java)
                startActivity(intent)
                this@BooksListFragment.activity?.finish()
            })

            emailDialog.create().show()

        } else {
            binding.refresh.setOnRefreshListener {
                getListBooks()
            }

            getListBooks()
        }
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
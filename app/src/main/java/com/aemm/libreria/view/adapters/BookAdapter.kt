package com.aemm.libreria.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aemm.libreria.R
import com.aemm.libreria.databinding.BookElementBinding
import com.aemm.libreria.model.Book
import com.aemm.libreria.view.activities.MainActivity
import com.aemm.libreria.view.fragments.BooksListFragmentDirections
import com.bumptech.glide.Glide

class BookAdapter(private val context: Context, private val listBooks: ArrayList<Book>): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    class ViewHolder(view: BookElementBinding):RecyclerView.ViewHolder(view.root) {
        val ivThumbnail = view.ivThumbnail
        val tvTitle = view.tvTitle
        val tvRelease = view.tvRelease
        val tvSynopsis = view.tvSynopsis
        val container = view.documentoCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BookElementBinding.inflate(LayoutInflater.from(this.context))

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = this.listBooks[position]
        holder.tvTitle.text = book.title
        holder.tvSynopsis.text = book.synopsis
        holder.tvRelease.text = context.getString(R.string.book_release_label, book.release)

        Glide.with(context)
            .load(book.thumbnail)
            .into(holder.ivThumbnail)

        holder.itemView.setOnClickListener {
            val directions = book.id?.let {idBook ->
                BooksListFragmentDirections.actionBooksListFragmentToBookDetailFragment(idBook)
            }
            if (directions != null) {
                it.findNavController().navigate(directions)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.listBooks.size
    }
}
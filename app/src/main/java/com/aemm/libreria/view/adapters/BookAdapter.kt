package com.aemm.libreria.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aemm.libreria.databinding.BookElementBinding
import com.aemm.libreria.model.Book
import com.aemm.libreria.view.activities.MainActivity
import com.bumptech.glide.Glide

class BookAdapter(private val context: Context, private val listBooks: ArrayList<Book>): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    class ViewHolder(view: BookElementBinding):RecyclerView.ViewHolder(view.root) {
        val ivThumbnail = view.ivThumbnail
        val tvTitle = view.tvTitle
        val tvRelease = view.tvRelease
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BookElementBinding.inflate(LayoutInflater.from(this.context))

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = this.listBooks[position].title
        holder.tvRelease.text = this.listBooks[position].title

        Glide.with(context)
            .load(this.listBooks[position].thumbnail)
            .into(holder.ivThumbnail)

        //Para los clicks

    }

    override fun getItemCount(): Int {
        return this.listBooks.size
    }
}
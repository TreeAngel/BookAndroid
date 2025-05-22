package com.example.bookandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookandroid.databinding.ItemBookBinding
import com.example.bookandroid.models.BookModel
import com.example.bookandroid.services.RetrofitClient

class BookAdapter : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<BookModel>() {
        override fun areItemsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffUtil)
    var books: List<BookModel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val book = books[position]
            tVBookName.text = book.name
            tVPublisher.text = book.publisher
            tVBookPrice.text = book.price.toString()
            Glide.with(root.context)
                .load("${RetrofitClient.BASE_URL}${book.imageCover}")
                .centerInside()
                .into(iVCover)
        }
    }
}
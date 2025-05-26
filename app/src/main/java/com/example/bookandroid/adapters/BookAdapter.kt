package com.example.bookandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookandroid.R
import com.example.bookandroid.databinding.ItemBookBinding
import com.example.bookandroid.models.BookModel
import com.example.bookandroid.models.WishlistModel
import com.example.bookandroid.services.RetrofitClient
import java.text.NumberFormat
import java.util.Locale

class BookAdapter(
    val onClick: (BookModel) -> Unit,
    val addToWishlist: (BookModel) -> Unit,
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    private val bookDiffUtil = object : DiffUtil.ItemCallback<BookModel>() {
        override fun areItemsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
            return oldItem == newItem
        }
    }

    private val bookDiffer = AsyncListDiffer(this, bookDiffUtil)
    var books: List<BookModel>
        get() = bookDiffer.currentList
        set(value) {
            bookDiffer.submitList(value)
        }

    private val wishlistDiffUtil = object : DiffUtil.ItemCallback<WishlistModel>() {
        override fun areItemsTheSame(oldItem: WishlistModel, newItem: WishlistModel): Boolean {
            return oldItem.createdAt == newItem.createdAt && oldItem.book == newItem.book
        }

        override fun areContentsTheSame(oldItem: WishlistModel, newItem: WishlistModel): Boolean {
            return oldItem == newItem
        }
    }

    private val wishlistDiffer = AsyncListDiffer(this, wishlistDiffUtil)
    var wishlist: List<WishlistModel>
        get() = wishlistDiffer.currentList
        set(value) {
            wishlistDiffer.submitList(value)
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
            tVBookPrice.text = formatCurrency(book.price)
            Glide.with(root.context)
                .load("${RetrofitClient.BASE_URL}${book.imageCover}")
                .centerInside()
                .into(iVCover)
            root.setOnClickListener { onClick(book) }
            iBWishlist.setOnClickListener { addToWishlist(book) }
            if (wishlist.any { it.book.id == book.id }) {
                iBWishlist.imageTintList = root.context.getColorStateList(R.color.main)
            } else {
                iBWishlist.imageTintList = root.context.getColorStateList(R.color.accent)
            }
        }
    }

    private fun formatCurrency(price: Int, locale: Locale = Locale("id", "ID")): String {
        return NumberFormat.getCurrencyInstance(locale).format(price)
    }
}
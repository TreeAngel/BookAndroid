package com.example.bookandroid.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bookandroid.R
import com.example.bookandroid.databinding.FragmentBookDetailBinding
import com.example.bookandroid.models.BookModel
import com.example.bookandroid.models.WishlistModel
import com.example.bookandroid.services.RetrofitClient
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class BookDetailFragment(private val book: BookModel) : Fragment() {

    private lateinit var binding: FragmentBookDetailBinding

    private var wishlist: WishlistModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            wishlist = RetrofitClient.wishlist.find { it.book.id == book.id }
            iBWishlist.setOnClickListener {
                if (wishlist != null) {
                    removeWishlist(wishlist!!.id)
                } else {
                    addWishlist(book.id)
                }
            }
            if (wishlist != null) {
                iBWishlist.imageTintList = context?.getColorStateList(R.color.main)
            } else {
                iBWishlist.imageTintList = context?.getColorStateList(R.color.accent)
            }
            viewBook()
        }
    }

    private fun viewBook() {
        try {
            with(binding) {
                tVBookPrice.text = formatCurrency(book!!.price)
                tVBookPublisher.text = book!!.publisher
                tVBookName.text = book!!.name
                tVBookPublishDate.text = book!!.publishDate
                tVBookDescription.text = book!!.description
                context?.let {
                    Glide.with(it)
                        .load("${RetrofitClient.BASE_URL}${book!!.imageCover}")
                        .fitCenter()
                        .into(iVCover)
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun formatCurrency(price: Int, locale: Locale = Locale("id", "ID")): String {
        return NumberFormat.getCurrencyInstance(locale).format(price)
    }

    private fun addWishlist(id: Int) {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.postWishlist(bookId = id)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Toast.makeText(
                                context,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to add to wishlist",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(
                            "Book API Error",
                            "addWishlist: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun removeWishlist(id: Int) {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.deleteWishlist(id = id)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Toast.makeText(
                                context,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        wishlist.removeAt(wishlist.indexOfFirst { it.id == id })
                        this@BookDetailFragment.wishlist = null
                        binding.iBWishlist.imageTintList =
                            context?.getColorStateList(R.color.accent)
                    } else {
                        Toast.makeText(context, "Failed to remove wishlist", Toast.LENGTH_SHORT)
                            .show()
                        Log.d(
                            "Book API Error",
                            "removeWishlist: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }
}
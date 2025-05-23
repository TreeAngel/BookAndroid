package com.example.bookandroid.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.transition.Visibility
import com.example.bookandroid.R
import com.example.bookandroid.activities.BookDetailActivity
import com.example.bookandroid.activities.CartActivity
import com.example.bookandroid.adapters.BookAdapter
import com.example.bookandroid.adapters.GenreAdapter
import com.example.bookandroid.databinding.FragmentHomeBinding
import com.example.bookandroid.models.BookModel
import com.example.bookandroid.models.GenreModel
import com.example.bookandroid.models.WishlistModel
import com.example.bookandroid.services.RetrofitClient
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var genreAdapter: GenreAdapter
    private lateinit var bookAdapter: BookAdapter

    private var genres: ArrayList<GenreModel> = arrayListOf()
    private var wishlist: ArrayList<WishlistModel> = arrayListOf()
    private var books: ArrayList<BookModel> = arrayListOf()
    private var tempBooks: ArrayList<BookModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            progressBar.visibility = View.VISIBLE
            setupGenreAdapter()
            setupBookAdapter()
            getGenres()
            getWishlist()
            getBooks(null)
            iBCart.setOnClickListener {
                context?.startActivity(
                    Intent(
                        context,
                        CartActivity::class.java
                    )
                )
            }
            inputSearch.addTextChangedListener {
                if (!it.isNullOrEmpty()) {
                    tempBooks.clear()
                    tempBooks.addAll(books.filter { item ->
                        item.name.lowercase().trim() == it.toString().lowercase().trim()
                    })
                    bookAdapter.notifyDataSetChanged()
                } else {
                    tempBooks = books
                    bookAdapter.notifyDataSetChanged()
                }
            }
            progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            if (RetrofitClient.cart.isNotEmpty()) {
                tVCartNum.visibility = View.VISIBLE
                tVCartNum.text = RetrofitClient.cart.size.toString()
            } else {
                tVCartNum.visibility = View.INVISIBLE
                tVCartNum.text = "0"
            }
        }
    }

    private fun setupGenreAdapter() = binding.rViewCategories.apply {
        genreAdapter = GenreAdapter { genre, index ->
            if (index != genreAdapter.selectedBtn) {
                getBooks(genre.name)
                genreAdapter.selectedBtn = index
                genreAdapter.notifyDataSetChanged()
            }
        }
        adapter = genreAdapter
        genreAdapter.genres = genres
    }

    private fun setupBookAdapter() = binding.rViewBooks.apply {
        bookAdapter = BookAdapter(
            onClick = { item ->
                context.startActivity(
                    Intent(
                        context,
                        BookDetailActivity::class.java
                    ).putExtra("id", item.id)
                )
            },
            addToWishlist = { item ->
                if (wishlist.any { it.book.id == item.id }) {
                    removeWishlist(item.id)
                } else {
                    addWishlist(item.id)
                }
            }
        )
        adapter = bookAdapter
        bookAdapter.wishlist = wishlist
        bookAdapter.books = tempBooks
    }

    private fun getGenres() {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.getGenres()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            genres.clear()
                            genres.addAll(it.data)
                            genreAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to retrieve genres",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(
                            "Book API Error",
                            "getGenres: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun getWishlist() {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.getWishlist()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            wishlist.clear()
                            wishlist.addAll(it.data)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to retrieve wishlist",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(
                            "Book API Error",
                            "getWishlist: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun getBooks(genre: String?) {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.getBooks(genre)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            books.clear()
                            books.addAll(it.data)
                            tempBooks = books
                            bookAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to retrieve books",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(
                            "Book API Error",
                            "getBooks: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
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
                        bookAdapter.notifyDataSetChanged()
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
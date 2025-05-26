package com.example.bookandroid.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.bookandroid.R
import com.example.bookandroid.activities.BookDetailActivity
import com.example.bookandroid.adapters.BookAdapter
import com.example.bookandroid.databinding.FragmentWishlistBinding
import com.example.bookandroid.models.BookModel
import com.example.bookandroid.models.WishlistModel
import com.example.bookandroid.services.RetrofitClient
import kotlinx.coroutines.launch

class WishlistFragment : Fragment() {

    private lateinit var binding: FragmentWishlistBinding

    private lateinit var bookAdapter: BookAdapter

    private var books: ArrayList<BookModel> = arrayListOf()
    private var tempBooks: ArrayList<BookModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            progressBar.visibility = View.VISIBLE
            setupBookAdapter()
            if (RetrofitClient.token.isNotEmpty()) {
                getWishlist()
            } else {
                rViewBooks.visibility = View.GONE
                sSort.visibility = View.GONE
                tVNotLogin.visibility = View.VISIBLE
            }
            sSort.apply {
                adapter = ArrayAdapter.createFromResource(
                    context,
                    R.array.wishlist_sort,
                    android.R.layout.simple_spinner_item
                ).also { aAdapter ->
                    aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when (selectedItem.toString().lowercase().trim()) {
                            "name" -> {
                                tempBooks.sortBy { it.name }
                                bookAdapter.notifyDataSetChanged()
                            }

                            "price" -> {
                                tempBooks.sortBy { it.price }
                                bookAdapter.notifyDataSetChanged()
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        tempBooks = books
                        bookAdapter.notifyDataSetChanged()
                    }
                }
            }
            progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            if (RetrofitClient.token.isNotEmpty()) {
                getWishlist()
                tVNotLogin.visibility = View.GONE
                if (RetrofitClient.wishlist.isEmpty()) {
                    sSort.visibility = View.INVISIBLE
                    rViewBooks.visibility = View.GONE
                    iVEmpty.visibility = View.VISIBLE
                } else {
                    sSort.visibility = View.VISIBLE
                    rViewBooks.visibility = View.VISIBLE
                    iVEmpty.visibility = View.GONE
                }
            } else {
                sSort.visibility = View.GONE
                rViewBooks.visibility = View.GONE
                tVNotLogin.visibility = View.VISIBLE
            }
        }
    }

    private fun setupBookAdapter() = binding.rViewBooks.apply {
        bookAdapter = BookAdapter(
            onClick = { item ->
                startActivity(
                    Intent(context, BookDetailActivity::class.java).putExtra(
                        "id", item.id
                    )
                )
            },
            addToWishlist = { item ->
                val wishlistItem = RetrofitClient.wishlist.find { it.book.id == item.id }
                if (wishlistItem != null) {
                    removeWishlist(wishlistId = wishlistItem.id, bookId = item.id)
                } else {
                    addWishlist(item.id)
                }
            }
        )
        adapter = bookAdapter
        bookAdapter.wishlist = RetrofitClient.wishlist
        bookAdapter.books = tempBooks
    }

    private fun getWishlist() {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.getWishlist()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.data.isNotEmpty()) {
                                binding.iVEmpty.visibility = View.GONE
                                binding.rViewBooks.visibility = View.VISIBLE
                                wishlist.clear()
                                wishlist.addAll(it.data)
                                books.clear()
                                wishlist.forEach { item ->
                                    books.add(item.book)
                                }
                                tempBooks = books
                                bookAdapter.notifyDataSetChanged()
                            } else {
                                binding.iVEmpty.visibility = View.VISIBLE
                                binding.rViewBooks.visibility = View.GONE
                                bookAdapter.notifyDataSetChanged()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Failed get wishlist", Toast.LENGTH_SHORT).show()
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

    private fun removeWishlist(wishlistId: Int, bookId: Int) {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.deleteWishlist(id = wishlistId)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Toast.makeText(
                                context,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        wishlist.removeAt(wishlist.indexOfFirst { it.id == wishlistId })
                        books.removeAt(books.indexOfFirst { it.id == bookId })
                        tempBooks = books
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
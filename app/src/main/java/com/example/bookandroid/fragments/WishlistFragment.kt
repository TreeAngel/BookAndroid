package com.example.bookandroid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookandroid.R
import com.example.bookandroid.adapters.BookAdapter
import com.example.bookandroid.databinding.FragmentWishlistBinding
import com.example.bookandroid.models.BookModel
import com.example.bookandroid.models.WishlistModel

class WishlistFragment : Fragment() {

    private lateinit var binding: FragmentWishlistBinding

    private lateinit var bookAdapter: BookAdapter

    private var wishlist: ArrayList<WishlistModel> = arrayListOf()
    private var books: ArrayList<BookModel> = arrayListOf()
    private var tempBooks: ArrayList<BookModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }
}
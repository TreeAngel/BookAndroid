package com.example.bookandroid.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.bookandroid.R
import com.example.bookandroid.databinding.ActivityBookDetailBinding
import com.example.bookandroid.fragments.BookDetailFragment
import com.example.bookandroid.models.BookModel
import com.example.bookandroid.models.TransactionItemModel
import com.example.bookandroid.services.RetrofitClient
import kotlinx.coroutines.launch

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding

    private var bookId: Int = 0
    private var book: BookModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bookId = intent.getIntExtra("id", 0)
        if (bookId <= 0) {
            AlertDialog.Builder(this@BookDetailActivity)
                .setMessage("Invalid book ID")
                .setPositiveButton("Back") { dialog, _ ->
                    dialog.dismiss()
                }
                .setOnDismissListener { this@BookDetailActivity.finish() }
                .show()
        }
        getBookDetail()
        with(binding) {
            iBBack.setOnClickListener { finish() }
            iBCart.setOnClickListener {
                startActivity(
                    Intent(
                        this@BookDetailActivity,
                        CartActivity::class.java
                    )
                )
            }
            btnAddToCart.setOnClickListener {
                with(RetrofitClient) {
                    val itemExist = cart.find { it.bookId == bookId }
                    if (itemExist != null) {
                        cart[cart.indexOf(itemExist)].qty += 1
                    } else {
                        cart.add(
                            TransactionItemModel(
                                bookId = bookId,
                                qty = 1,
                                bookName = book!!.name,
                                bookPublisher = book!!.publisher,
                                price = book!!.price,
                                imageCover = book!!.imageCover
                            )
                        )
                    }
                    Toast.makeText(this@BookDetailActivity, "Added to cart", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun getBookDetail() {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.getBook(bookId)
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            book = body.data
                            supportFragmentManager.commit {
                                replace(binding.navView.id, BookDetailFragment(book!!))
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@BookDetailActivity,
                            "Failed to retrieve book detail",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(
                            "Book API Error",
                            "getBookDetail: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }
}
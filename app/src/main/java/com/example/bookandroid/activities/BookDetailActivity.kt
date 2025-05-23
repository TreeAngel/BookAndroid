package com.example.bookandroid.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.bookandroid.R
import com.example.bookandroid.databinding.ActivityBookDetailBinding
import com.example.bookandroid.fragments.BookDetailFragment

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding

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
            supportFragmentManager.commit {
                replace(binding.navView.id, BookDetailFragment())
            }
        }
    }
}
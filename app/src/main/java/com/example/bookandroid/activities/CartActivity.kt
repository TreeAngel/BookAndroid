package com.example.bookandroid.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.bookandroid.R
import com.example.bookandroid.adapters.CartAdapter
import com.example.bookandroid.databinding.ActivityCartBinding
import com.example.bookandroid.models.TransactionItemModel
import com.example.bookandroid.models.request.PostTransactionRequest
import com.example.bookandroid.services.RetrofitClient
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAdapter()
        with(binding) {
            iBBack.setOnClickListener { this@CartActivity.finish() }
            btnCheckout.setOnClickListener {
                with(RetrofitClient) {
                    if (cart.isEmpty()) {
                        Toast.makeText(this@CartActivity, "Cart is empty", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        AlertDialog.Builder(this@CartActivity)
                            .setMessage("Checkout?")
                            .setPositiveButton("Yes") { _, _ ->
                                checkout()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun checkout() {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val request = PostTransactionRequest()
                    request.addAll(cart)
                    val response = service.postTransaction(request = request)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val status = it.status.lowercase()
                            if (status == "success" || status == "sukses") {
                                cart.clear()
                                cartAdapter.notifyDataSetChanged()
                                Toast.makeText(
                                    this@CartActivity,
                                    "Checkout success",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@CartActivity,
                            "Checkout failed",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(
                            "Book API Error",
                            "checkout: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun setupAdapter() = binding.rViewCart.apply {
        with(RetrofitClient) {
            cartAdapter = CartAdapter(
                onAdd = { item ->
                    cart.find { it == item }?.let { cartItem ->
                        cartItem.qty += 1
                        cartAdapter.notifyDataSetChanged()
                    }
                },
                onRemove = { item ->
                    cart.find { it == item }?.let { cartItem ->
                        if (cartItem.qty > 1) {
                            cartItem.qty -= 1
                        } else {
                            AlertDialog.Builder(this@CartActivity)
                                .setMessage("Remove from cart?")
                                .setPositiveButton("Yes") { _, _ ->
                                    cart.remove(cartItem)
                                    cartAdapter.notifyDataSetChanged()
                                }
                                .setNegativeButton("No") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    }
                },
                onDelete = { item ->
                    cart.find { it == item }?.let { cartItem ->
                        AlertDialog.Builder(this@CartActivity)
                            .setMessage("Remove from cart?")
                            .setPositiveButton("Yes") { _, _ ->
                                cart.remove(cartItem)
                                cartAdapter.notifyDataSetChanged()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            )
            adapter = cartAdapter
            cartAdapter.cart = cart
            cartAdapter.notifyDataSetChanged()
        }
    }
}
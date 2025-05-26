package com.example.bookandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookandroid.databinding.ItemCartBinding
import com.example.bookandroid.models.TransactionItemModel
import com.example.bookandroid.services.RetrofitClient
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    val onAdd: (TransactionItemModel) -> Unit,
    val onRemove: (TransactionItemModel) -> Unit,
    val onDelete: (TransactionItemModel) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<TransactionItemModel>() {
        override fun areItemsTheSame(
            oldItem: TransactionItemModel,
            newItem: TransactionItemModel
        ): Boolean {
            return oldItem.bookId == newItem.bookId
        }

        override fun areContentsTheSame(
            oldItem: TransactionItemModel,
            newItem: TransactionItemModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtil)
    var cart: List<TransactionItemModel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = cart.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val item = cart[position]
            tVBookName.text = item.bookName
            tVPublisher.text = item.bookPublisher
            tVBookPrice.text = formatCurrency(item.price)
            Glide.with(root.context)
                .load("${RetrofitClient.BASE_URL}${item.imageCover}")
                .fitCenter()
                .into(iVCover)
            iBAdd.setOnClickListener { onAdd(item) }
            iBRemove.setOnClickListener { onRemove(item) }
            iBTrash.setOnClickListener { onDelete(item) }
            tVQty.text = item.qty.toString()
        }
    }

    private fun formatCurrency(price: Int, locale: Locale = Locale("id", "ID")): String {
        return NumberFormat.getCurrencyInstance(locale).format(price)
    }
}
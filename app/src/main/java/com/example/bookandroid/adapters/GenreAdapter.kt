package com.example.bookandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookandroid.R
import com.example.bookandroid.databinding.ItemButtonBinding
import com.example.bookandroid.models.GenreModel

class GenreAdapter(val onClick: (GenreModel, Int) -> Unit) :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemButtonBinding) : RecyclerView.ViewHolder(binding.root)

    var selectedBtn = -1

    private val diffUtil = object : DiffUtil.ItemCallback<GenreModel>() {
        override fun areItemsTheSame(oldItem: GenreModel, newItem: GenreModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GenreModel, newItem: GenreModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtil)
    var genres: List<GenreModel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemButtonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = genres.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val genre = genres[position]
            root.text = genre.name
            root.setOnClickListener { onClick(genre, position) }
            if (selectedBtn == position) {
                root.setBackgroundColor(root.context.getColor(R.color.main))
                root.setTextColor(root.context.getColor(R.color.accent))
            } else {
                root.setBackgroundColor(root.context.getColor(R.color.accent))
                root.setTextColor(root.context.getColor(R.color.main))
            }
        }
    }
}
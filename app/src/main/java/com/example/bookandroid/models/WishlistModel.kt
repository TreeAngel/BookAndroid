package com.example.bookandroid.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class WishlistModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("book")
    val book: BookModel
) : Parcelable
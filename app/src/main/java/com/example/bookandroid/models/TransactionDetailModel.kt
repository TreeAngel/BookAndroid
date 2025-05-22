package com.example.bookandroid.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class TransactionDetailModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("qty")
    val qty: Int,
    @SerializedName("totalPrice")
    val totalPrice: Int,
    @SerializedName("book")
    val book: BookModel
) : Parcelable
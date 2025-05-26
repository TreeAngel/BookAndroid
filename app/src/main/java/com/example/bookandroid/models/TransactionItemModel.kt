package com.example.bookandroid.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class TransactionItemModel(
    @SerializedName("bookId")
    val bookId: Int,
    @SerializedName("qty")
    var qty: Int,
    val bookName: String,
    val bookPublisher: String,
    val price: Int,
    var subTotal: Int = qty * price,
    val imageCover: String
) : Parcelable
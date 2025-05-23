package com.example.bookandroid.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class TransactionItemModel(
    @SerializedName("bookId")
    val bookId: Int,
    @SerializedName("qty")
    val qty: Int
) : Parcelable
package com.example.bookandroid.models.request


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PostTransactionRequestItem(
    @SerializedName("bookId")
    val bookId: Int,
    @SerializedName("qty")
    val qty: Int
) : Parcelable
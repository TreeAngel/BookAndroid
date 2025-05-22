package com.example.bookandroid.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class TransactionModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("code")
    val code: String,
    @SerializedName("subtotal")
    val subtotal: Int,
    @SerializedName("transactionDate")
    val transactionDate: String,
    @SerializedName("details")
    val details: List<TransactionDetailModel>?
) : Parcelable
package com.example.bookandroid.models.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.bookandroid.models.TransactionModel

@Parcelize
data class GetTransactionDetailResponse(
    @SerializedName("data")
    val `data`: TransactionModel
) : Parcelable
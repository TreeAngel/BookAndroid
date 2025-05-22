package com.example.bookandroid.models.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class GeneralResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String
) : Parcelable
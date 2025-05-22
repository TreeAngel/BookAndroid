package com.example.bookandroid.models.request


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UpdateProfileRequest(
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("username")
    val username: String
) : Parcelable
package com.example.bookandroid.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UserModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("imageProfile")
    val imageProfile: String
) : Parcelable
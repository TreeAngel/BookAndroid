package com.example.bookandroid.models.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.bookandroid.models.UserModel

@Parcelize
data class UpdateUserResponse(
    @SerializedName("data")
    val `data`: UserModel
) : Parcelable
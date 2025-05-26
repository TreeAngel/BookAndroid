package com.example.bookandroid.models.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.bookandroid.models.UserModel

@Parcelize
data class GetProfileResponse(
    @SerializedName("user")
    val user: UserModel
) : Parcelable
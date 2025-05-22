package com.example.bookandroid.models.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.bookandroid.models.WishlistModel

@Parcelize
data class GetWishlistResponse(
    @SerializedName("data")
    val `data`: List<WishlistModel>
) : Parcelable
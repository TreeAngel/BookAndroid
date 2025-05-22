package com.example.bookandroid.models.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.bookandroid.models.GenreModel

@Parcelize
data class GetGenresResponse(
    @SerializedName("data")
    val `data`: List<GenreModel>
) : Parcelable
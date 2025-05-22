package com.example.bookandroid.models.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.bookandroid.models.BookModel

@Parcelize
data class GetBooksResponse(
    @SerializedName("data")
    val `data`: List<BookModel>
) : Parcelable
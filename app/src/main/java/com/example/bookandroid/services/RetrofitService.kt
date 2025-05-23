package com.example.bookandroid.services

import com.example.bookandroid.models.UserModel
import com.example.bookandroid.models.request.LoginRequest
import com.example.bookandroid.models.request.PostTransactionRequest
import com.example.bookandroid.models.request.RegisterRequest
import com.example.bookandroid.models.request.UpdateProfileRequest
import com.example.bookandroid.models.response.AuthResponse
import com.example.bookandroid.models.response.GeneralResponse
import com.example.bookandroid.models.response.GetBookResponse
import com.example.bookandroid.models.response.GetBooksResponse
import com.example.bookandroid.models.response.GetGenresResponse
import com.example.bookandroid.models.response.GetTransactionDetailResponse
import com.example.bookandroid.models.response.GetTransactionsResponse
import com.example.bookandroid.models.response.GetWishlistResponse
import com.example.bookandroid.models.response.PostTransactionResponse
import com.example.bookandroid.models.response.UpdateUserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("auth/me")
    suspend fun getProfile(@Header("Authorization") token: String = RetrofitClient.token): Response<UserModel>

    @GET("book/genres")
    suspend fun getGenres(): Response<GetGenresResponse>

    @GET("book")
    suspend fun getBooks(@Query("genreName") genreName: String?): Response<GetBooksResponse>

    @GET("book/{bookId}")
    suspend fun getBook(@Path("bookId") bookId: Int): Response<GetBookResponse>

    @POST("user/image-profile")
    suspend fun postProfilePicture(
        @Header("Authorization") token: String = RetrofitClient.token,
        @Part image: MultipartBody.Part
    ): Response<UpdateUserResponse>

    @PUT("user/profile")
    suspend fun putUpdateProfile(
        @Header("Authorization") token: String = RetrofitClient.token,
        @Body request: UpdateProfileRequest
    ): Response<UpdateUserResponse>

    @GET("user/withlist")
    suspend fun getWishlist(@Header("Authorization") token: String = RetrofitClient.token): Response<GetWishlistResponse>

    @POST("user/wishlist/{bookId}")
    suspend fun postWishlist(
        @Header("Authorization") token: String = RetrofitClient.token,
        @Path("bookId") bookId: Int
    ): Response<GeneralResponse>

    @DELETE("user/wishlist/{id}")
    suspend fun deleteWishlist(
        @Header("Authorization") token: String = RetrofitClient.token,
        @Path("id") id: Int
    ): Response<GeneralResponse>

    @GET("user/transaction")
    suspend fun getTransactions(@Header("Authorization") token: String = RetrofitClient.token): Response<GetTransactionsResponse>

    @GET("user/transaction/{transactionId}")
    suspend fun getTransactionDetail(
        @Header("Authorization") token: String = RetrofitClient.token,
        @Path("transactionId") id: Int
    ): Response<GetTransactionDetailResponse>

    @POST("user/transaction")
    suspend fun postTransaction(
        @Header("Authorization") token: String = RetrofitClient.token,
        @Body request: PostTransactionRequest
    ): Response<PostTransactionResponse>
}
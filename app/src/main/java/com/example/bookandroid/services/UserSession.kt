package com.example.bookandroid.services

import android.content.Context

object UserSession {
    private const val TOKEN_KEY: String = "BookAndroid-Token"

    fun keepSession(context: Context) {
        try {
            val token = context.getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE)
            token.edit().apply {
                putString(TOKEN_KEY, RetrofitClient.token)
                apply()
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    fun checkSession(context: Context) {
        try {
            val token = context.getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE)
                .getString(TOKEN_KEY, "")
            if (!token.isNullOrEmpty()) {
                RetrofitClient.token = token
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    fun logoutSession(context: Context) {
        try {
            val token = context.getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE)
            token.edit().apply {
                remove(TOKEN_KEY)
                apply()
            }
            RetrofitClient.token = ""
        } catch (ex: Exception) {
            throw ex
        }
    }
}
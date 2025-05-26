package com.example.bookandroid.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bookandroid.R
import com.example.bookandroid.databinding.FragmentProfileBinding
import com.example.bookandroid.models.UserModel
import com.example.bookandroid.models.request.LoginRequest
import com.example.bookandroid.services.RetrofitClient
import com.example.bookandroid.services.UserSession
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private var user: UserModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnAction.setOnClickListener {
                if (user == null) {
                    val username = inputUsername.text.toString().trim()
                    val password = inputPassword.text.toString().trim()
                    login(username, password)
                } else {
                    logout()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            if (RetrofitClient.user != null) {
                btnAction.text = "Sign Out"
                user = RetrofitClient.user
                setProfile()
            }
            if (RetrofitClient.token.isNotEmpty() && RetrofitClient.user == null) {
                getProfile()
            }
            if (RetrofitClient.token.isEmpty()) {
                btnAction.text = "Sign In"
            }
        }
    }

    private fun getProfile() {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.getProfile()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            user = it.user
                            this@ProfileFragment.user = user
                            setProfile()
                        }
                    } else {
                        Toast.makeText(context, "Failed to retrieve profile", Toast.LENGTH_SHORT)
                            .show()
                        Log.d(
                            "Book API Error",
                            "getProfile: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun logout() {
        try {
            RetrofitClient.user = null
            user = null
            context?.let { UserSession.logoutSession(it) }
            setProfile()
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun login(username: String, password: String) {
        try {
            lifecycleScope.launch {
                with(RetrofitClient) {
                    val response = service.login(LoginRequest(username, password))
                    if (response.isSuccessful) {
                        response.body()?.let {
                            user = it.user
                            token = "Bearer ${it.token}"
                            this@ProfileFragment.user = it.user
                            setProfile()
                            context?.let { it1 -> UserSession.keepSession(it1) }
                        }
                    } else {
                        Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                        Log.d(
                            "Book API Error",
                            "login: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun setProfile() {
        try {
            with(binding) {
                if (user == null) {
                    layoutUsername.visibility = View.VISIBLE
                    layoutPassword.visibility = View.VISIBLE
                    tvFullName.visibility = View.INVISIBLE
                    tVUsername.visibility = View.INVISIBLE
                    iVProfile.visibility = View.INVISIBLE
                } else {
                    layoutUsername.visibility = View.GONE
                    layoutPassword.visibility = View.GONE
                    tvFullName.visibility = View.VISIBLE
                    tVUsername.visibility = View.VISIBLE
                    iVProfile.visibility = View.VISIBLE
                    tvFullName.text = user?.fullName
                    tVUsername.text = user?.username
                    btnAction.text = "Sign Out"
                    this@ProfileFragment.context?.let {
                        Glide.with(it)
                            .load("${RetrofitClient.BASE_URL}${user!!.imageProfile}")
                            .fitCenter()
                            .into(iVProfile)
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }
}
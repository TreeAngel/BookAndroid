package com.example.bookandroid.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.bookandroid.R
import com.example.bookandroid.databinding.ActivityMainBinding
import com.example.bookandroid.fragments.HomeFragment
import com.example.bookandroid.fragments.ProfileFragment
import com.example.bookandroid.fragments.WishlistFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var wishlistFragment: WishlistFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        homeFragment = HomeFragment()
        wishlistFragment = WishlistFragment()
        profileFragment = ProfileFragment()
        with(binding) {
            bottomNav.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menuHome -> {
                        changeFragment(homeFragment)
                    }

                    R.id.menuWishlist -> {
                        changeFragment(wishlistFragment)
                    }

                    R.id.menuProfile -> {
                        changeFragment(profileFragment)
                    }
                }
                true
            }
        }
        changeFragment(homeFragment)
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(binding.navView.id, fragment)
            addToBackStack(null)
        }
    }
}
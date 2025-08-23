package com.multibahana.dummyjsonapp.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.multibahana.dummyjsonapp.R
import com.multibahana.dummyjsonapp.databinding.ActivityMainBinding
import com.multibahana.dummyjsonapp.presentation.auth.AuthViewModel
import com.multibahana.dummyjsonapp.presentation.comments.CommentsFragment
import com.multibahana.dummyjsonapp.presentation.explores.ExploresFragment
import com.multibahana.dummyjsonapp.presentation.home.HomeFragment
import com.multibahana.dummyjsonapp.presentation.posts.PostsFragment
import com.multibahana.dummyjsonapp.presentation.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val authViewModel: AuthViewModel by viewModels()

    private var selectedItemId = R.id.item_menu_home


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

        observeViewModel(savedInstanceState)
    }



    private fun observeViewModel(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    authViewModel.accessToken.collectLatest { token ->
                        token?.let { authViewModel.getMe(it) }
                    }
                }
                launch {
                    authViewModel.currentUserState.collectLatest { state ->
                        when {
                            state.isLoading -> { /* tampilkan loading */ }
                            state.user != null -> {
                                if (savedInstanceState == null) {
                                    replaceFragment(HomeFragment())
                                }
                                setupBottomNavigation()
                            }
                            state.error != null -> { /* tampilkan error */ }
                        }
                    }
                }
            }
        }
    }



    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (selectedItemId != item.itemId) {
                selectedItemId = item.itemId
                when (item.itemId) {
                    R.id.item_menu_home -> replaceFragment(HomeFragment())
                    R.id.item_menu_posts -> replaceFragment(PostsFragment())
                    R.id.item_menu_explores -> replaceFragment(ExploresFragment())
                    R.id.item_menu_comments -> replaceFragment(CommentsFragment())
                    R.id.item_menu_user -> replaceFragment(ProfileFragment())
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
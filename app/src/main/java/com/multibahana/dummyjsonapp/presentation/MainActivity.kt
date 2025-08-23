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
import kotlin.properties.Delegates
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()

    private var selectedItemId by Delegates.notNull<Int>()

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

        selectedItemId = savedInstanceState?.getInt("selectedItemId") ?: R.id.item_menu_home

        observeViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedItemId", selectedItemId)
    }

    private fun observeViewModel() {
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
                                showFragment(selectedItemId)
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
                showFragment(item.itemId)
            }
            true
        }
        binding.bottomNavigation.selectedItemId = selectedItemId
    }

    private fun showFragment(itemId: Int) {
        val fragment = when (itemId) {
            R.id.item_menu_home -> HomeFragment()
            R.id.item_menu_posts -> PostsFragment()
            R.id.item_menu_explores -> ExploresFragment()
            R.id.item_menu_comments -> CommentsFragment()
            R.id.item_menu_user -> ProfileFragment()
            else -> HomeFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}


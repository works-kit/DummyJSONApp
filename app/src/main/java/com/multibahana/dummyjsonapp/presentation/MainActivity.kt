package com.multibahana.dummyjsonapp.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.multibahana.dummyjsonapp.R
import com.multibahana.dummyjsonapp.databinding.ActivityMainBinding
import com.multibahana.dummyjsonapp.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var navController: NavController

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

        observeViewModel()
        observeNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun observeNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_menu_home -> {
                    navigateSingleTop(R.id.item_menu_home)
                    true
                }
                R.id.item_menu_posts -> {
                    navigateSingleTop(R.id.item_menu_posts)
                    true
                }
                R.id.item_menu_explores -> {
                    navigateSingleTop(R.id.item_menu_explores)
                    true
                }
                R.id.item_menu_comments -> {
                    navigateSingleTop(R.id.item_menu_comments)
                    true
                }
                R.id.item_menu_user -> {
                    navigateSingleTop(R.id.item_menu_user)
                    true
                }
                else -> false
            }
        }

        // Sinkronisasi bottom nav saat backstack berubah
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.item_menu_home -> binding.bottomNavigation.menu.findItem(R.id.item_menu_home).isChecked = true
                R.id.item_menu_posts -> binding.bottomNavigation.menu.findItem(R.id.item_menu_posts).isChecked = true
                R.id.item_menu_explores -> binding.bottomNavigation.menu.findItem(R.id.item_menu_explores).isChecked = true
                R.id.item_menu_comments -> binding.bottomNavigation.menu.findItem(R.id.item_menu_comments).isChecked = true
                R.id.item_menu_user -> binding.bottomNavigation.menu.findItem(R.id.item_menu_user).isChecked = true
            }
        }
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
                            state.isLoading -> {
                                // tampilkan loading UI
                            }

                            state.user != null -> {
                                // user berhasil login -> biarkan nav graph handle fragment
                            }

                            state.error != null -> {
                                // tampilkan error
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateSingleTop(destinationId: Int) {
        navController.navigate(destinationId, null, navOptions {
            launchSingleTop = true
            restoreState = true
        })
    }
}

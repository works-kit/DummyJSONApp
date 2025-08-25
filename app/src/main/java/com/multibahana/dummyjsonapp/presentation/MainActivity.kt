package com.multibahana.dummyjsonapp.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.multibahana.dummyjsonapp.R
import com.multibahana.dummyjsonapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.get

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val navGraphIds = listOf(
        R.navigation.nav_graph_home,
        R.navigation.nav_graph_posts,
        R.navigation.nav_graph_explores,
        R.navigation.nav_graph_comments,
        R.navigation.nav_graph_profile
    )

    private lateinit var navHostFragments: List<NavHostFragment>
    private var currentNavHostFragment: NavHostFragment? = null

    private val backStack = ArrayDeque<Int>()

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

        setupMultipleNavHosts()
        handleBackPress()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupMultipleNavHosts() {
        val fm = supportFragmentManager

        // buat semua navhostfragment
        navHostFragments = navGraphIds.mapIndexed { index, navGraphId ->
            val tag = "bottomNav#$index"
            val fragment = fm.findFragmentByTag(tag) as NavHostFragment?
                ?: NavHostFragment.create(navGraphId).also {
                    fm.beginTransaction()
                        .add(R.id.nav_host_container, it, tag)
                        .hide(it)
                        .commitNow()
                }
            fragment
        }

        // tampilkan tab pertama (Home)
        switchToNavHost(0)
        // handle klik bottom nav
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val index = when (item.itemId) {
                R.id.homeFragment -> 0
                R.id.postsFragment -> 1
                R.id.exploresFragment -> 2
                R.id.commentsFragment -> 3
                R.id.profileFragment -> 4
                else -> 0
            }
            switchToNavHost(index)
            true
        }
    }

    private fun switchToNavHost(index: Int) {
        val targetFragment = navHostFragments[index]

        if (currentNavHostFragment != targetFragment) {
            supportFragmentManager.beginTransaction()
                .hide(currentNavHostFragment ?: targetFragment)
                .show(targetFragment)
                .commitNow()
            currentNavHostFragment = targetFragment

            // update bottom nav UI
            binding.bottomNavigation.menu[index].isChecked = true

            // 👉 update history stack
            if (backStack.contains(index)) {
                backStack.remove(index)
            }
            backStack.addLast(index)
        }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentNavController = currentNavHostFragment?.navController

                    // 1. kalau masih bisa navigateUp di graph aktif → lakukan dulu
                    if (currentNavController?.navigateUp() == true) return

                    // 2. kalau di root fragment Home → keluar langsung
                    val currentIndex = navHostFragments.indexOf(currentNavHostFragment)
                    if (currentIndex == 0) { // asumsi tab Home index 0
                        finish()
                        return
                    }

                    // 3. kalau di root fragment tab lain → balik ke tab sebelumnya
                    if (backStack.size > 1) {
                        backStack.removeLast() // hapus current
                        val lastIndex = backStack.last()
                        switchToNavHost(lastIndex)
                    } else {
                        finish() // kalau history kosong → keluar app
                    }
                }
            })
    }

}

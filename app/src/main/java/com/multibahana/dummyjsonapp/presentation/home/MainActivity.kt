package com.multibahana.dummyjsonapp.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.multibahana.dummyjsonapp.R
import com.multibahana.dummyjsonapp.databinding.ActivityMainBinding
import com.multibahana.dummyjsonapp.presentation.auth.login.LoginActivity
import com.multibahana.dummyjsonapp.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val authViewModel: AuthViewModel by viewModels()

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

        lifecycleScope.launch {
            authViewModel.accessToken.collectLatest { token ->
                token?.let {
                    authViewModel.getMe(it)
                }
            }
        }

        lifecycleScope.launch {
            authViewModel.currentUserState.collectLatest { state ->
                when {
                    state.isLoading -> {
                        binding.tvUsername.text = "Loading..."
                        binding.tvEmail.text = "Loading..."
                    }
                    state.user != null -> {
                        binding.tvUsername.text = "Hi, ${state.user.getOrNull()?.username}"
                        binding.tvEmail.text = "${state.user.getOrNull()?.email}"
                        Glide.with(this@MainActivity)
                            .load(state.user?.getOrNull()?.image)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(binding.imageAvatar)
                    }
                    state.error != null -> {
                        binding.tvUsername.text = "Error: ${state.error}"
                    }
                }
            }
        }

        lifecycleScope.launch {
            authViewModel.state.collect { state ->
                if (state.isLogout) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }

        }

        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
        }

    }
}
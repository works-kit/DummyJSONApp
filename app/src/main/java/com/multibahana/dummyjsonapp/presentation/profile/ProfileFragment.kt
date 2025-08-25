package com.multibahana.dummyjsonapp.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.multibahana.dummyjsonapp.R
import com.multibahana.dummyjsonapp.databinding.FragmentProfileBinding
import com.multibahana.dummyjsonapp.presentation.auth.AuthViewModel
import com.multibahana.dummyjsonapp.presentation.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val authViewModel: AuthViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter, UI binding, button listener
        binding.btnLogout.setOnClickListener { authViewModel.logout() }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 1. Access token
                launch {
                    authViewModel.accessToken.collectLatest { token ->
                        token?.let { authViewModel.getMe(it) }
                    }
                }
                launch {
                    authViewModel.currentUserState.collectLatest { state ->
                        when {
                            state.isLoading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.linearLayoutContainer.visibility = View.GONE
                            }

                            state.user != null -> {
                                binding.progressBar.visibility = View.GONE
                                binding.linearLayoutContainer.visibility = View.VISIBLE

                                binding.tvUsername.text = "Hi, ${state.user.getOrNull()?.username}"
                                binding.tvEmail.text = "${state.user.getOrNull()?.email}"
                                Glide.with(this@ProfileFragment)
                                    .load(state.user.getOrNull()?.image)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(binding.imageAvatar)
                            }

                            state.error != null -> {
                                binding.progressBar.visibility = View.GONE
                                binding.linearLayoutContainer.visibility = View.VISIBLE

                                binding.tvUsername.text = "Error: ${state.error}"
                            }
                        }
                    }
                }

                launch {
                    authViewModel.state.collect { state ->
                        if (state.isLogout) {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    LoginActivity::class.java
                                ).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                })
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.clp.tasks.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.clp.tasks.R
import com.clp.tasks.databinding.FragmentLoginBinding
import com.clp.tasks.utils.UiState
import com.clp.tasks.viewmodel.TasksViewModel

class LoginFragment : BaseFragment() {

    private val binding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding.loginButton.setOnClickListener {
            viewModel.authenticate(requireContext())
        }
        viewModel.loginState.observe(viewLifecycleOwner){
            when(it){
                is UiState.ERROR -> {
                    Toast.makeText(requireContext(),"Login failed",Toast.LENGTH_LONG).show()
                }
                UiState.LOADING -> {}
                is UiState.SUCCESS -> {
                    Toast.makeText(requireContext(),"Login succeed",Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_login_to_tasks)
                }
            }
        }
        return binding.root
    }

}
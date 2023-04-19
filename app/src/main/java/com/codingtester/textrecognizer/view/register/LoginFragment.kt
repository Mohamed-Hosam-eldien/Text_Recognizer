package com.codingtester.textrecognizer.view.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.codingtester.textrecognizer.databinding.FragmentLoginBinding
import com.codingtester.textrecognizer.view.main.MainActivity
import com.codingtester.textrecognizer.view.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener { login() }

        // make observe on (mutable live data) to listen of data when he come to our screen
        // or any change happened on data
        viewModel.loginLiveData.observe(viewLifecycleOwner) { userResponse ->
            if(userResponse?.firebaseUser != null) {
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                binding.btnLogin.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
                Toast.makeText(requireContext(), userResponse?.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun login() {
        val email = binding.edtEmail.text.toString()
        val pass = binding.edtPass.text.toString()

        if(email.isNotEmpty() && pass.isNotEmpty()) {
            binding.btnLogin.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            lifecycleScope.launch {
                viewModel.login(email, pass)
            }
        } else {
            Toast.makeText(requireContext(), "please fill all data", Toast.LENGTH_SHORT).show()
        }
    }
}
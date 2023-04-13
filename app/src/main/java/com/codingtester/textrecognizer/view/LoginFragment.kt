package com.codingtester.textrecognizer.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.User
import com.codingtester.textrecognizer.databinding.FragmentLoginBinding
import com.codingtester.textrecognizer.utils.Constants
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.bind(view)

        binding.btnLogin.setOnClickListener { login() }

        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            if(it != null) {
                setCurrentUserData(it)
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Something wrong! please try again", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }


    private fun setCurrentUserData(user: FirebaseUser) {
        Constants.currentUser = User(user.uid, user.displayName, user.email)
    }

    private fun login() {
        val email = binding.edtEmail.text.toString()
        val pass = binding.edtPass.text.toString()

        lifecycleScope.launch {
            viewModel.login(email, pass)
        }
    }
}
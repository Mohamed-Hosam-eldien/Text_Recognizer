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
import com.codingtester.textrecognizer.databinding.FragmentRegisterBinding
import com.codingtester.textrecognizer.utils.Constants
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragView = inflater.inflate(R.layout.fragment_register, container, false)
        binding = FragmentRegisterBinding.bind(fragView)

        binding.btnCreateAccount.setOnClickListener {
            signUpNewUser()
        }

        viewModel.signupLiveData.observe(viewLifecycleOwner) {
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

    private fun signUpNewUser() {
        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val pass = binding.edtPass.text.toString()

        lifecycleScope.launch {
            viewModel.signup(name, email, pass)
        }
    }
}
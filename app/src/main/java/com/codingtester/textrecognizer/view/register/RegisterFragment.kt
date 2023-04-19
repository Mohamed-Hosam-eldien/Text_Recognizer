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
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.User
import com.codingtester.textrecognizer.databinding.FragmentRegisterBinding
import com.codingtester.textrecognizer.utils.Constants
import com.codingtester.textrecognizer.view.main.MainActivity
import com.codingtester.textrecognizer.view.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

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

        // make observe on (mutable live data) to listen of data when he come to our screen
        // or any change happened on data
        viewModel.signupLiveData.observe(viewLifecycleOwner) { response ->
            if (response?.firebaseUser != null) {
                Constants.currentUser = User(
                    response.firebaseUser!!.uid,
                    response.firebaseUser!!.displayName,
                    response.firebaseUser!!.email
                )
                requireActivity().startActivity(
                    Intent(
                        requireActivity(),
                        MainActivity::class.java
                    )
                )
                requireActivity().finish()
            } else {
                binding.btnCreateAccount.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
                Toast.makeText(requireContext(), response?.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun signUpNewUser() {
        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val pass = binding.edtPass.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty()) {
            binding.btnCreateAccount.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            try {
                lifecycleScope.launch {
                    viewModel.signup(name, email, pass)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "please fill all data", Toast.LENGTH_SHORT).show()
        }

    }
}
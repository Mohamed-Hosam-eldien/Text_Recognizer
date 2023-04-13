package com.codingtester.textrecognizer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_welcome, container, false)
        binding = FragmentWelcomeBinding.bind(fragmentView)

        binding.btnCreateAccount.setOnClickListener {
            findNavController(fragmentView)
                .navigate(R.id.action_welcomeFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            findNavController(fragmentView)
                .navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        return binding.root
    }
}
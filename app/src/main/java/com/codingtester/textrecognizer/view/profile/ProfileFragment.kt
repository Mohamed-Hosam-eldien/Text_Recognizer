package com.codingtester.textrecognizer.view.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.databinding.FragmentProfileBinding
import com.codingtester.textrecognizer.view.register.LoginActivity
import com.codingtester.textrecognizer.view.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        binding = FragmentProfileBinding.bind(view)

        binding.txtEmail.text = viewModel.currentUser?.email
        binding.txtName.text = viewModel.currentUser?.displayName

        binding.btnLogout.setOnClickListener {
            logoutFromApp()
        }

        return binding.root
    }

    private fun logoutFromApp() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout!")
            .setMessage("Are you sure you want to log out from the app?")
            .setPositiveButton("Yes") { _,_ ->
                viewModel.logout()
                requireActivity().startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("Cancel") {dialog,_ ->
                dialog.dismiss()
            }.show()
    }

}
package com.codingtester.textrecognizer.view.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.User
import com.codingtester.textrecognizer.databinding.ActivityWelcomeBinding
import com.codingtester.textrecognizer.utils.Constants
import com.codingtester.textrecognizer.view.main.MainActivity
import com.codingtester.textrecognizer.view.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        openHomeIfUserLogin()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView2) as NavHostFragment?

        navController = navHostFragment!!.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    // if user login or sign up in last time we will scape register and login screens
    private fun openHomeIfUserLogin() {
        viewModel.loginLiveData.observe(this) { response ->
            if(response?.firebaseUser != null) {
                Constants.currentUser = User(response.firebaseUser!!.uid, response.firebaseUser!!.displayName, response.firebaseUser!!.email)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    // to handle back button on toolbar
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
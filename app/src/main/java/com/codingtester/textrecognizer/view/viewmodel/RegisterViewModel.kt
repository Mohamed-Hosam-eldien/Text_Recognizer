package com.codingtester.textrecognizer.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtester.textrecognizer.data.pojo.UserResponse
import com.codingtester.textrecognizer.data.repo.register.RegisterRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * view model to communicate between fragments screens and repository
 */

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val mainRepository: RegisterRepositoryImpl
) : ViewModel() {

    private val mutableLogin: MutableLiveData<UserResponse?> = MutableLiveData()
    val loginLiveData: LiveData<UserResponse?> = mutableLogin

    private val mutableSignup: MutableLiveData<UserResponse?> = MutableLiveData()
    val signupLiveData: LiveData<UserResponse?> = mutableSignup

    val currentUser get() = mainRepository.currentUser

    init {
        if (mainRepository.currentUser != null) {
            mutableLogin.value?.firebaseUser = mainRepository.currentUser
        }
    }

    fun login(email: String, pass: String) = viewModelScope.launch {
        val user = mainRepository.login(email, pass)
        mutableLogin.value = user
    }

    fun signup(name: String, email: String, pass: String) = viewModelScope.launch {
        val user = mainRepository.signup(name, email, pass)
        mutableSignup.value = user
    }

    fun logout() {
        mainRepository.logout()
        mutableLogin.value = null
        mutableSignup.value = null
    }
}
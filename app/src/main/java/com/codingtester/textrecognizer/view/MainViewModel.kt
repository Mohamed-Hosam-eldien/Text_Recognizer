package com.codingtester.textrecognizer.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtester.textrecognizer.data.repo.IMainRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: IMainRepository
): ViewModel() {

    private val mutableLogin: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val loginLiveData: LiveData<FirebaseUser?> = mutableLogin

    private val mutableSignup: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val signupLiveData: LiveData<FirebaseUser?> = mutableSignup

    val currentUser get() = mainRepository.currentUser

    init {
        if(mainRepository.currentUser != null) {
            mutableLogin.value = mainRepository.currentUser

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
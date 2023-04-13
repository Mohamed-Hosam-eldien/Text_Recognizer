package com.codingtester.textrecognizer.data.repo

import com.google.firebase.auth.FirebaseUser

interface IMainRepository {

    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): FirebaseUser?
    suspend fun signup(name: String,email: String, password: String): FirebaseUser?
    fun logout()
}
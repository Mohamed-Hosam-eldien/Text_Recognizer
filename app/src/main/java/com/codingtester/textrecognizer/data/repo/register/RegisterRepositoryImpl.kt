package com.codingtester.textrecognizer.data.repo.register

import com.codingtester.textrecognizer.data.pojo.UserResponse
import com.codingtester.textrecognizer.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    // current user is variable once user create account and sign in or login to account
    // he prevent user to login everytime he open application
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    suspend fun login(email: String, password: String): UserResponse {
        return try {
            val currentUser = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            UserResponse(currentUser.user, "")
        } catch (ex: Exception) {
            UserResponse(null, ex.message)
        }
    }

    suspend fun signup(name: String, email: String, password: String): UserResponse {
        return try {
            val newUser = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            newUser.user?.updateProfile(profileUpdates)?.await()
            UserResponse(newUser.user, "")
        } catch (ex: Exception) {
            UserResponse(null, ex.message)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}
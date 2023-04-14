package com.codingtester.textrecognizer.data.repo.register

import com.codingtester.textrecognizer.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): IRegisterRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): FirebaseUser? {
        return try {
            val currentUser = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            currentUser.user
        } catch (ex : Exception) {
            null
        }
    }

    override suspend fun signup(name: String, email: String, password: String): FirebaseUser? {
        return try {
            val newUser = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            newUser.user?.updateProfile(profileUpdates)?.await()
            newUser.user
        } catch (ex: Exception) {
            null
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
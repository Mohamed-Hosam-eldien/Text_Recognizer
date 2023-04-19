package com.codingtester.textrecognizer.data.pojo

import com.google.firebase.auth.FirebaseUser

data class UserResponse(
    var firebaseUser: FirebaseUser?,
    val errorMessage: String?)
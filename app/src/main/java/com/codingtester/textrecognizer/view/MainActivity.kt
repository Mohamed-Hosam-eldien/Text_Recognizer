package com.codingtester.textrecognizer.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.utils.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("TAG", "current user : -->  ${Constants.currentUser.id}" +
                " \n ${Constants.currentUser.email} \n ${Constants.currentUser.name}")

    }
}
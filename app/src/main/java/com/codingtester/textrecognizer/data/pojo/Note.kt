package com.codingtester.textrecognizer.data.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Int,
    val title: String,
    val dateInMilliSecond: Double
) : Parcelable
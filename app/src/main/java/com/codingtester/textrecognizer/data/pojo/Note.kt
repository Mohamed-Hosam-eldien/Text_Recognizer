package com.codingtester.textrecognizer.data.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Long = 0,
    val title: String = "",
    val dateInMilliSecond: Long=0
) : Parcelable
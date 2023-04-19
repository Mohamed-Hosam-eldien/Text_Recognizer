package com.codingtester.textrecognizer.data.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Board(
    val id: Long=0,
    val title: String="",
    val dateInMilliSecond: Long=0,
    var noteList: List<Note> = emptyList()
): Parcelable
// we implement Parcelable to can send this object from fragment to another
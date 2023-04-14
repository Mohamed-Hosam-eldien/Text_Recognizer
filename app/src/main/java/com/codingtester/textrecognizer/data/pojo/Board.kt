package com.codingtester.textrecognizer.data.pojo

data class Board(
    val id: Long,
    val title: String,
    val dateInMilliSecond: Long,
    val noteList: List<Note> = emptyList()
)

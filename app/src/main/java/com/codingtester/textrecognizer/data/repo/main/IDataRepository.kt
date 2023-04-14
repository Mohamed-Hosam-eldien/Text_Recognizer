package com.codingtester.textrecognizer.data.repo.main

import androidx.lifecycle.MutableLiveData
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.data.pojo.Note

interface IDataRepository {

    suspend fun addNewBoard(userId: String, board: Board)

    suspend fun addNewNote(userId: String, boardName: String, note: Note)

    suspend fun getAllBoards(userId: String, liveData: MutableLiveData<List<Board>>)

    suspend fun deleteBoard()

    suspend fun deleteNote()
}
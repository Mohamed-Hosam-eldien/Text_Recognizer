package com.codingtester.textrecognizer.data.repo.data

import androidx.lifecycle.MutableLiveData
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.data.pojo.Note

interface IDataRepository {

    suspend fun addNewBoard(userId: String, board: Board)

    suspend fun addNewNote(userId: String, boardId: String, note: Note)

    suspend fun getAllBoards(userId: String, liveData: MutableLiveData<List<Board>>)

    suspend fun getNotesByBoardId(userId: String, boardId: String, liveData: MutableLiveData<List<Note>>)

    suspend fun deleteBoard(userId: String, boardId: String)

    suspend fun deleteNote(boardName: String, userId: String, noteId: Long)
}
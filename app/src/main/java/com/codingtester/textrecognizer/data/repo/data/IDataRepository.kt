package com.codingtester.textrecognizer.data.repo.data

import androidx.lifecycle.MutableLiveData
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.data.pojo.Note

interface IDataRepository {

    suspend fun addNewBoard(board: Board)

    suspend fun addNewNote(boardId: String, note: Note)

    suspend fun getAllBoards(liveData: MutableLiveData<List<Board>>)

    suspend fun getNotesByBoardId(boardId: String, liveData: MutableLiveData<List<Note>>)

    suspend fun deleteBoard(boardId: String)

    suspend fun deleteNote(boardId: String, noteId: Long)
}
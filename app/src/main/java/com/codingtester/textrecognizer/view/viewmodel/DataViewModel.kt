package com.codingtester.textrecognizer.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.data.pojo.Note
import com.codingtester.textrecognizer.data.repo.data.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * view model to communicate between fragments screens and repository
 */

@HiltViewModel
class DataViewModel @Inject constructor(
    private val dataRepo: DataRepositoryImpl
) : ViewModel() {

    private val mutableBoards: MutableLiveData<List<Board>> = MutableLiveData()
    val boardsLiveData: LiveData<List<Board>> = mutableBoards

    private val mutableNotes: MutableLiveData<List<Note>> = MutableLiveData()
    val notesLiveData: LiveData<List<Note>> = mutableNotes

    fun addNewBoard(board: Board) {
        dataRepo.addNewBoard(board)
    }

    fun addNewNote(boardId: String, note: Note) {
        dataRepo.addNewNote(boardId, note)
    }

    fun getAllBoards() {
        dataRepo.getAllBoards(mutableBoards)
    }

    fun getNotesByBoardId(boardId: String) {
        dataRepo.getNotesByBoardId(boardId, mutableNotes)
    }

    fun deleteBoard(boardId: String) {
        dataRepo.deleteBoard(boardId)
    }

    fun deleteNote(boardId: String, noteId: Long) {
        dataRepo.deleteNote(boardId, noteId)
    }
}
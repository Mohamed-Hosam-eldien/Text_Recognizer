package com.codingtester.textrecognizer.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.data.pojo.Note
import com.codingtester.textrecognizer.data.repo.data.IDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val dataRepo: IDataRepository
) : ViewModel() {


    private val mutableBoards: MutableLiveData<List<Board>> = MutableLiveData()
    val boardsLiveData: LiveData<List<Board>> = mutableBoards

    private val mutableNotes: MutableLiveData<List<Note>> = MutableLiveData()
    val notesLiveData: LiveData<List<Note>> = mutableNotes

    fun addNewBoard(userId: String, board: Board) = viewModelScope.launch {
        dataRepo.addNewBoard(userId, board)
    }

    fun addNewNote(userId: String, boardId: String, note: Note) = viewModelScope.launch {
        dataRepo.addNewNote(userId, boardId, note)
    }

    fun getAllBoards(userId: String) = viewModelScope.launch {
        dataRepo.getAllBoards(userId, mutableBoards)
    }

    fun getNotesByBoardId(userId: String, boardId: String) = viewModelScope.launch {
        dataRepo.getNotesByBoardId(userId, boardId, mutableNotes)
    }

    fun deleteBoard(userId: String, boardId: String) =  viewModelScope.launch {
        dataRepo.deleteBoard(userId, boardId)
    }

    fun deleteNote(boardId: String, userId: String, noteId: Long) = viewModelScope.launch {
        dataRepo.deleteNote(boardId, userId, noteId)
    }
}
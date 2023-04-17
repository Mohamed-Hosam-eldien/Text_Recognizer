package com.codingtester.textrecognizer.view

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

    fun addNewNote(userId: String, boardName: String, note: Note) = viewModelScope.launch {
        dataRepo.addNewNote(userId, boardName, note)
    }

    fun getAllBoards(userId: String) = viewModelScope.launch {
        dataRepo.getAllBoards(userId, mutableBoards)
    }

    fun getNotesByBoardName(userId: String, boardName: String) = viewModelScope.launch {
        dataRepo.getNotesByBoardName(userId, boardName, mutableNotes)
    }

    fun deleteNote(boardName: String, userId: String, noteId: Long) = viewModelScope.launch {
        dataRepo.deleteNote(boardName, userId, noteId)
    }
}
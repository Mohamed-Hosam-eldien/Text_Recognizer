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

    fun addNewBoard(board: Board) = viewModelScope.launch {
        dataRepo.addNewBoard(board)
    }

    fun addNewNote(boardId: String, note: Note) = viewModelScope.launch {
        dataRepo.addNewNote(boardId, note)
    }

    fun getAllBoards() = viewModelScope.launch {
        dataRepo.getAllBoards(mutableBoards)
    }

    fun getNotesByBoardId(boardId: String) = viewModelScope.launch {
        dataRepo.getNotesByBoardId(boardId, mutableNotes)
    }

    fun deleteBoard(boardId: String) =  viewModelScope.launch {
        dataRepo.deleteBoard(boardId)
    }

    fun deleteNote(boardId: String, noteId: Long) = viewModelScope.launch {
        dataRepo.deleteNote(boardId, noteId)
    }
}
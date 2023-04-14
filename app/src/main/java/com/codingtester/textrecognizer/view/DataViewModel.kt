package com.codingtester.textrecognizer.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.data.pojo.Note
import com.codingtester.textrecognizer.data.repo.main.IDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val dataRepo: IDataRepository
) : ViewModel() {


    private val mutableBoards: MutableLiveData<List<Board>> = MutableLiveData()
    val boardsLiveData: LiveData<List<Board>> = mutableBoards

    fun addNewBoard(userId: String, board: Board) = viewModelScope.launch {
        dataRepo.addNewBoard(userId, board)
    }

    fun addNewNote(userId: String, boardName: String, note: Note) = viewModelScope.launch {
        dataRepo.addNewNote(userId, boardName, note)
    }

    fun getAllBoards(userId: String) = viewModelScope.launch {
        dataRepo.getAllBoards(userId, mutableBoards)
    }

}
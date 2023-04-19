package com.codingtester.textrecognizer.data.repo.data

import androidx.lifecycle.MutableLiveData
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.data.pojo.Note
import com.codingtester.textrecognizer.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

/**
 * this class is repository class that responsible to handling all data
 * with firebase like [add - delete - retrieve]
 */

class DataRepositoryImpl @Inject constructor() {

    // reference for users table (user path)
    private val databaseRef = FirebaseDatabase.getInstance().getReference(Constants.USERS)

    // current user id
    private val userId = Constants.currentUser.id

    fun addNewBoard(board: Board) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(board.id.toString())
            .setValue(board)
    }

    fun addNewNote(boardId: String, note: Note) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardId)
            .child(Constants.NOTES)
            .push()
            .setValue(note)
    }

    fun getAllBoards(liveData: MutableLiveData<List<Board>>) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allBoards: List<Board> = snapshot.children.map { data ->
                        data.getValue(Board::class.java)!!
                    }
                    liveData.postValue(allBoards)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun getNotesByBoardId(
        boardId: String,
        liveData: MutableLiveData<List<Note>>
    ) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardId)
            .child(Constants.NOTES)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // iterate on all notes and set theme in list of notes
                    // and add list to (mutable live data)
                    val allNotes: List<Note> = snapshot.children.map { data ->
                        data.getValue(Note::class.java)!!
                    }
                    liveData.postValue(allNotes)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun deleteBoard(boardId: String) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardId)
            .removeValue()
    }

    fun deleteNote(boardId: String, noteId: Long) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardId)
            .child(Constants.NOTES)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // iterate on all notes to get note by id to delete it
                    snapshot.children.forEach {
                        val note: Note? = it.getValue(Note::class.java)
                        if(note?.id == noteId) {
                            it.ref.removeValue()
                            return@forEach
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
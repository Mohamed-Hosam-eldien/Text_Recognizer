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

class DataRepositoryImpl @Inject constructor() : IDataRepository {

    private val databaseRef = FirebaseDatabase.getInstance().getReference(Constants.USERS)

    override suspend fun addNewBoard(userId: String, board: Board) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(board.title)
            .setValue(board)
    }

    override suspend fun addNewNote(userId: String, boardName: String, note: Note) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardName)
            .child(Constants.NOTES)
            .push()
            .setValue(note)
    }

    override suspend fun getAllBoards(userId: String, liveData: MutableLiveData<List<Board>>) {
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

    override suspend fun getNotesByBoardName(
        userId: String,
        boardName: String,
        liveData: MutableLiveData<List<Note>>
    ) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardName)
            .child(Constants.NOTES)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allNotes: List<Note> = snapshot.children.map { data ->
                        data.getValue(Note::class.java)!!
                    }
                    liveData.postValue(allNotes)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override suspend fun deleteBoard() {}

    override suspend fun deleteNote(boardName: String, userId: String, noteId: Long) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardName)
            .child(Constants.NOTES)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
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
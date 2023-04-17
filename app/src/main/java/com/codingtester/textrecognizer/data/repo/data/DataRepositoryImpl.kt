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
            .child(board.id.toString())
            .setValue(board)
    }

    override suspend fun addNewNote(userId: String, boardId: String, note: Note) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardId)
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

    override suspend fun getNotesByBoardId(
        userId: String,
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
                    val allNotes: List<Note> = snapshot.children.map { data ->
                        data.getValue(Note::class.java)!!
                    }
                    liveData.postValue(allNotes)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override suspend fun deleteBoard(userId: String, boardId: String) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardId)
            .removeValue()
    }

    override suspend fun deleteNote(boardId: String, userId: String, noteId: Long) {
        databaseRef
            .child(userId)
            .child(Constants.BOARDS)
            .child(boardId)
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
package com.codingtester.textrecognizer.view.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.Note


class NoteAdapter(private val onClickBoard: OnClickNote): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var noteList: List<Note> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_layout, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]
        holder.txtNoteTitle.text = note.title
        holder.itemView.setOnClickListener {
            onClickBoard.onClickToNote(note)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNoteTitle: TextView = itemView.findViewById(R.id.txtNoteTitle)
    }

    fun updatePopularList(newBoards: List<Note>) {
        val recipeDiffUtil = NoteDiffUtil(noteList, newBoards)
        val diffResult = DiffUtil.calculateDiff(recipeDiffUtil)
        noteList = newBoards
        diffResult.dispatchUpdatesTo(this)
    }

}
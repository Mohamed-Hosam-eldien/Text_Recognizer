package com.codingtester.textrecognizer.view.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.Note


class NoteAdapter(private val onClickBoard: OnClickNote): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var noteList: List<Note> = emptyList()
    private var isLinear: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(isLinear) {
            ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.note_layout_linear, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.note_layout_grid, parent, false))
        }
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]
        holder.txtNoteTitle.text = if(isLinear) {
            note.title
        } else {
            textFormat(note.title)
        }

        holder.clickToSave?.setOnClickListener {
            onClickBoard.onClickToSaveFile(note)
        }

        holder.clickToDelete?.setOnClickListener {
            onClickBoard.onClickToDelete(note.id)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNoteTitle: TextView = itemView.findViewById(R.id.txtNoteTitle)
        val clickToSave: CardView? = itemView.findViewById(R.id.clickSaveToWord)
        val clickToDelete: CardView? = itemView.findViewById(R.id.clickDelete)
    }

    fun updatePopularList(newBoards: List<Note>, isLinear_ : Boolean) {
        isLinear = isLinear_
        val recipeDiffUtil = NoteDiffUtil(noteList, newBoards)
        val diffResult = DiffUtil.calculateDiff(recipeDiffUtil)
        noteList = newBoards
        diffResult.dispatchUpdatesTo(this)
    }

    private fun textFormat(text: String): String {
        return if(text.length <= 50) {
            text
        } else {
            text.substring(0, 50).plus("...")
        }
    }

}
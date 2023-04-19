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

/**
 * adapter to set all notes when comes from firebase in recycler view
 */
class NoteAdapter(private val onClickNote: OnClickNote): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var noteList: List<Note> = emptyList()
    private var isLinear: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // when change style of note adapter will change layout of view holder
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
            onClickNote.onClickToSaveFile(note)
        }

        holder.clickToDelete?.setOnClickListener {
            onClickNote.onClickToDelete(note.id)
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

    // to prevent show all text character on text view (only when adapter style changed)
    private fun textFormat(text: String): String {
        return if(text.length <= 50) {
            text
        } else {
            text.substring(0, 50).plus("...")
        }
    }

}
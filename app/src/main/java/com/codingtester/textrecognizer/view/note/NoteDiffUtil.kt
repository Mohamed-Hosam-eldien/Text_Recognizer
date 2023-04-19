package com.codingtester.textrecognizer.view.note

import androidx.recyclerview.widget.DiffUtil
import com.codingtester.textrecognizer.data.pojo.Note

// diff util to update board adapter when data changed
class NoteDiffUtil(
    private val oldItems:List<Note>,
    private val newItems:List<Note>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] === newItems[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

}
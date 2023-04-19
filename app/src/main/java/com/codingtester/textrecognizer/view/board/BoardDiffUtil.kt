package com.codingtester.textrecognizer.view.board

import androidx.recyclerview.widget.DiffUtil
import com.codingtester.textrecognizer.data.pojo.Board


// diff util to update board adapter when data changed
class BoardDiffUtil(
    private val oldItems:List<Board>,
    private val newItems:List<Board>
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
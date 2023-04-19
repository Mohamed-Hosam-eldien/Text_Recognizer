package com.codingtester.textrecognizer.view.note

import com.codingtester.textrecognizer.data.pojo.Note

/**
 * interface to connect between note fragment and adapter
 */
interface OnClickNote {
    fun onClickToDelete(id: Long)
    fun onClickToSaveFile(note: Note)
}
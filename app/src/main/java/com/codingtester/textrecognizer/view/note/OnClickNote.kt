package com.codingtester.textrecognizer.view.note

import com.codingtester.textrecognizer.data.pojo.Note

interface OnClickNote {
    fun onClickToDelete(id: Long)
    fun onClickToSaveWord(note: Note)
}
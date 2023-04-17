package com.codingtester.textrecognizer.view.board

import com.codingtester.textrecognizer.data.pojo.Board

interface OnClickBoard {
    fun onClickToBoard(board: Board)
    fun onClickToDelete(id: String)
}
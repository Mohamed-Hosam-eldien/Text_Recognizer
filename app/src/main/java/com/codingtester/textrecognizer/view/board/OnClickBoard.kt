package com.codingtester.textrecognizer.view.board

import com.codingtester.textrecognizer.data.pojo.Board

/**
 * interface to connect between board fragment and adapter
 */
interface OnClickBoard {
    fun onClickToBoard(board: Board)
    fun onClickToDelete(boardId: String)
}
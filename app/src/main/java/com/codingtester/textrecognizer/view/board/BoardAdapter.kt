package com.codingtester.textrecognizer.view.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.view.board.BoardAdapter.ViewHolder
import java.text.SimpleDateFormat
import java.util.*

class BoardAdapter(private val onClickBoard: OnClickBoard): RecyclerView.Adapter<ViewHolder>() {

    private var boardsList: List<Board> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_layout, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = boardsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val board = boardsList[position]
        holder.txtBoardName.text = board.title
        holder.txtDate.text = getTimeFromMilleSecond(board.dateInMilliSecond)

        holder.itemView.setOnClickListener {
            onClickBoard.onClickToBoard(board)
        }

        holder.imgDelete.setOnClickListener {
            onClickBoard.onClickToDelete(board.id.toString())
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtBoardName: TextView = itemView.findViewById(R.id.txtBoardName)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
    }

    private fun getTimeFromMilleSecond(dateInMilliSecond: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(dateInMilliSecond))
    }

    fun updatePopularList(newBoards: List<Board>) {
        val recipeDiffUtil = BoardDiffUtil(boardsList, newBoards)
        val diffResult = DiffUtil.calculateDiff(recipeDiffUtil)
        boardsList = newBoards
        diffResult.dispatchUpdatesTo(this)
    }

}
package com.codingtester.textrecognizer.view.board


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.databinding.AddBoardLayoutBinding
import com.codingtester.textrecognizer.view.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardDialog : DialogFragment() {

    private lateinit var binding: AddBoardLayoutBinding
    private val viewModel by viewModels<DataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.add_board_layout, container, false)
        binding = AddBoardLayoutBinding.bind(view)

        binding.btnAdd.setOnClickListener {
            val boarderName = binding.edtBoardName.text.toString()
            if (boarderName.isEmpty()) {
                binding.edtBoardName.error = "required"
            } else {
                binding.btnAdd.visibility = View.GONE
                binding.progress.visibility = View.VISIBLE
                addNewBoard(boarderName)
            }
        }

        return binding.root
    }


    private fun addNewBoard(boardName: String) {
        // add new board to firebase
        val board = Board(System.currentTimeMillis(), boardName, System.currentTimeMillis(), emptyList())
        viewModel.addNewBoard(board)
        dialog!!.dismiss()
        Toast.makeText(requireContext(), "board has been added successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        // set size of width to dialog to customize it with different screens
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
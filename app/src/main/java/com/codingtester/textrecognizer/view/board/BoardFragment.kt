package com.codingtester.textrecognizer.view.board

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.databinding.FragmentBoardBinding
import com.codingtester.textrecognizer.view.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BoardFragment : Fragment(), OnClickBoard {

    private lateinit var binding: FragmentBoardBinding
    private val dataViewModel by viewModels<DataViewModel>()
    private val boardAdapter by lazy { BoardAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataViewModel.getAllBoards()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBoardBinding.inflate(inflater, container, false)

        binding.btnAddBoard.setOnClickListener { showBoardDialog() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerBoard.apply {
            adapter = boardAdapter
        }

        // make observe on (mutable live data) to listen of data when he come to our screen
        // or any change happened on data
        dataViewModel.boardsLiveData.observe(viewLifecycleOwner) { boards ->
            if(boards.isEmpty()) {
                binding.recyclerBoard.visibility = View.GONE
                binding.imgEmpty.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
            } else {
                binding.recyclerBoard.visibility = View.VISIBLE
                binding.imgEmpty.visibility = View.GONE
                binding.progress.visibility = View.GONE
                boardAdapter.updatePopularList(boards)
            }
        }
    }

    private fun showBoardDialog() {
        BoardDialog().show(requireActivity().supportFragmentManager, "BoardDialog")
    }

    override fun onClickToBoard(board: Board) {
        // move to note fragment
        val action = BoardFragmentDirections.actionBoardFragmentToNoteFragment(board)
        findNavController().navigate(action)
    }

    override fun onClickToDelete(boardId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Board!")
            .setMessage("Are you sure you want to delete this board?")
            .setPositiveButton("Yes") { dialog,_ ->
                lifecycleScope.launch {
                    dataViewModel.deleteBoard(boardId)
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Board removed successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") {dialog,_ ->
                dialog.dismiss()
            }.show()
    }
}
package com.codingtester.textrecognizer.view.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.databinding.FragmentBoardBinding
import com.codingtester.textrecognizer.view.DataViewModel
import com.codingtester.textrecognizer.view.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardFragment : Fragment(), OnClickBoard {

    private lateinit var binding: FragmentBoardBinding
    private val dataViewModel by viewModels<DataViewModel>()
    private val userViewModel by viewModels<RegisterViewModel>()
    private val boardAdapter by lazy { BoardAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_board, container, false)
        binding = FragmentBoardBinding.bind(view)

        binding.btnAddBoard.setOnClickListener { showBoardDialog() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataViewModel.getAllBoards(userViewModel.currentUser?.uid!!)

        binding.recyclerBoard.apply {
            adapter = boardAdapter
        }

        dataViewModel.boardsLiveData.observe(viewLifecycleOwner) { boards ->
            boardAdapter.updatePopularList(boards)
        }
    }

    private fun showBoardDialog() {
        BoardDialog().show(requireActivity().supportFragmentManager, "BoardDialog")
    }

    override fun onClickToBoard(board: Board) {
        val action = BoardFragmentDirections.actionBoardFragmentToNoteFragment(board)
        findNavController().navigate(action)
    }

}
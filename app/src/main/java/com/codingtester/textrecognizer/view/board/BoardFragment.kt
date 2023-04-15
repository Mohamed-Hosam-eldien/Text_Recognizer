package com.codingtester.textrecognizer.view.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataViewModel.getAllBoards(userViewModel.currentUser?.uid!!)
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

        dataViewModel.boardsLiveData.observe(viewLifecycleOwner) { boards ->
            boardAdapter.updatePopularList(boards)
        }

        binding.recyclerBoard.apply {
            adapter = boardAdapter
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
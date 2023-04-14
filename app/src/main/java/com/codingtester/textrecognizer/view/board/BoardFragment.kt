package com.codingtester.textrecognizer.view.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.databinding.FragmentBoardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardFragment : Fragment() {

    private lateinit var binding: FragmentBoardBinding

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


    }

    private fun showBoardDialog() {
        BoardDialog().show(requireActivity().supportFragmentManager, "BoardDialog")
    }

}
package com.codingtester.textrecognizer.view.note

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.data.pojo.Note
import com.codingtester.textrecognizer.databinding.FragmentNoteBinding
import com.codingtester.textrecognizer.view.DataViewModel
import com.codingtester.textrecognizer.view.RegisterViewModel

class NoteFragment : Fragment(), OnClickNote {

    private val args: NoteFragmentArgs by navArgs()
    private lateinit var binding: FragmentNoteBinding
    private lateinit var currentBoard: Board

    private val noteAdapter by lazy { NoteAdapter(this) }
    private val dataViewModel by viewModels<DataViewModel>()
    private val userViewModel by viewModels<RegisterViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentBoard = args.board
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        binding = FragmentNoteBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerNotes.apply {
            noteAdapter.updatePopularList(currentBoard.noteList)
            adapter = noteAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding.btnTakePhoto.setOnClickListener {  }
    }

    override fun onClickToNote(note: Note) {

    }

}
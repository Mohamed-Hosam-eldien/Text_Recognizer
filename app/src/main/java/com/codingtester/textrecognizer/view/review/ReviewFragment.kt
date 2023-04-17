package com.codingtester.textrecognizer.view.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.Note
import com.codingtester.textrecognizer.databinding.FragmentReviewBinding
import com.codingtester.textrecognizer.view.viewmodel.DataViewModel
import com.codingtester.textrecognizer.view.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private val dataViewModel by viewModels<DataViewModel>()
    private val userViewModel by viewModels<RegisterViewModel>()

    private lateinit var binding: FragmentReviewBinding
    private var text = ""
    private var boardId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        text = arguments?.getString("textAfterRec") ?: ""
        boardId = arguments?.getString("boardId") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.bind(
            inflater.inflate(
                R.layout.fragment_review, container, false
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtText.setText(text)

        binding.clickSave.setOnClickListener { saveNoteToFirebase() }
    }

    private fun saveNoteToFirebase() {
        binding.clickSave.visibility = View.GONE
        binding.reviewProgressBar.visibility = View.VISIBLE
        val note = Note(System.currentTimeMillis(), binding.edtText.text.toString(), System.currentTimeMillis())
        dataViewModel.addNewNote(userViewModel.currentUser?.uid!!, boardId , note)

        findNavController().popBackStack(R.id.reviewFragment, true)
        Toast.makeText(requireContext(), "Note has been added successfully", Toast.LENGTH_LONG).show()
    }

}
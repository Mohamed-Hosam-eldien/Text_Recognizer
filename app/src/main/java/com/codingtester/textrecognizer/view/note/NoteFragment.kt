package com.codingtester.textrecognizer.view.note

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.data.pojo.Board
import com.codingtester.textrecognizer.data.pojo.Note
import com.codingtester.textrecognizer.databinding.FragmentNoteBinding
import com.codingtester.textrecognizer.view.DataViewModel
import com.codingtester.textrecognizer.view.RegisterViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.*

@AndroidEntryPoint
class NoteFragment : Fragment(), OnClickNote {

    private val args: NoteFragmentArgs by navArgs()
    private lateinit var binding: FragmentNoteBinding
    private lateinit var currentBoard: Board
    private lateinit var imageBitmap: Bitmap
    private var isLinear = true

    private val noteAdapter by lazy { NoteAdapter(this) }
    private val dataViewModel by viewModels<DataViewModel>()
    private val userViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentBoard = args.board
        dataViewModel.getNotesByBoardName(userViewModel.currentUser?.uid!!, currentBoard.title)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        binding = FragmentNoteBinding.bind(view)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = currentBoard.title

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerNotes.apply {
            noteAdapter.updatePopularList(currentBoard.noteList, true)
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        dataViewModel.notesLiveData.observe(viewLifecycleOwner) { notes ->
            currentBoard.noteList = notes
            noteAdapter.updatePopularList(notes, true)
        }

        binding.btnTakePhoto.setOnClickListener {
            if (isCameraPermissionGranted()) {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(requireActivity(), this)
            } else {
                requestCameraPermission()
            }
        }

        binding.btnStyle.setOnClickListener {
            changeAdapterStyle()
        }
    }

    private fun changeAdapterStyle() {
        if (isLinear) {
            isLinear = false
            binding.recyclerNotes.apply {
                noteAdapter.updatePopularList(currentBoard.noteList, false)
                adapter = noteAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
            binding.btnStyle.setImageResource(R.drawable.baseline_linear_24)
        } else {
            isLinear = true
            binding.recyclerNotes.apply {
                noteAdapter.updatePopularList(currentBoard.noteList, true)
                adapter = noteAdapter
                layoutManager = LinearLayoutManager(requireContext())
                binding.btnStyle.setImageResource(R.drawable.baseline_grid_view_24)
            }
        }
    }

    override fun onClickToDelete(id: Long) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Note!")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { dialog,_ ->
                lifecycleScope.launch {
                    dataViewModel.deleteNote(currentBoard.title, userViewModel.currentUser?.uid!!, id)
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "note removed successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") {dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    override fun onClickToSaveFile(note: Note) {
        val dialog = SaveFileDialog()

        val args = Bundle()
        args.putLong("noteId", note.id)
        args.putString("noteTitle", note.title)

        dialog.arguments = args
        dialog.show(requireActivity().supportFragmentManager, "saveFile")
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val cropImageResult = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {

                val uri = cropImageResult.uri
                imageBitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                } else {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(requireActivity().contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
                getTextFromBitmap(imageBitmap)
            }
        }
    }

    private fun getTextFromBitmap(imageBitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(imageBitmap, 0)

        //creating TextRecognizer instance
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        //process the image
        recognizer.process(inputImage).addOnSuccessListener { textAfterRecognize -> //Task completed successfully
            navigateToReviewFragment(textAfterRecognize.text)
        }.addOnFailureListener { e -> // Task failed with an exception
            e.printStackTrace()
        }
    }

    private fun navigateToReviewFragment(text: String) {
        if (text.isBlank() || text.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Failed to recognize text! try again",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val bundle = Bundle()
            bundle.putString("textAfterRec", text)
            bundle.putString("boardName", currentBoard.title)
            findNavController().navigate(R.id.action_noteFragment_to_reviewFragment, bundle)
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.CAMERA
            ), 100
        )
    }

    private fun isCameraPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED)
    }

}
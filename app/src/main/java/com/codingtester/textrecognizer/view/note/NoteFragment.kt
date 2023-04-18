package com.codingtester.textrecognizer.view.note

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
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
import com.codingtester.textrecognizer.view.viewmodel.DataViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteFragment : Fragment(), OnClickNote {

    private val args: NoteFragmentArgs by navArgs()
    private lateinit var binding: FragmentNoteBinding
    private lateinit var currentBoard: Board
    private var isLinear = true

    private val noteAdapter by lazy { NoteAdapter(this) }
    private val dataViewModel by viewModels<DataViewModel>()

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage
                .activity()
                .getIntent(requireActivity())
                .setType("image/*")
                .setAction(MediaStore.ACTION_IMAGE_CAPTURE)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentBoard = args.board
        dataViewModel.getNotesByBoardId(currentBoard.id.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(inflater, container, false)
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
            if (notes.isEmpty()) {
                setEmptyData()
            } else {
                setNotesData(notes)
            }
        }

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                getTextFromBitmap(uri)
            }
        }

        binding.btnTakePhoto.setOnClickListener {
            if (isCameraPermissionGranted()) {
                cropActivityResultLauncher.launch(null)
            } else {
                requestCameraPermission()
            }
        }

        binding.btnStyle.setOnClickListener {
            changeAdapterStyle()
        }
    }

    private fun setNotesData(notes: List<Note>) {
        binding.recyclerNotes.visibility = View.VISIBLE
        binding.imgEmpty.visibility = View.GONE
        binding.btnStyle.visibility = View.VISIBLE
        currentBoard.noteList = notes
        noteAdapter.updatePopularList(notes, true)
    }

    private fun setEmptyData() {
        binding.recyclerNotes.visibility = View.GONE
        binding.imgEmpty.visibility = View.VISIBLE
        binding.btnStyle.visibility = View.GONE
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
            .setPositiveButton("Yes") { dialog, _ ->
                lifecycleScope.launch {
                    dataViewModel.deleteNote(currentBoard.id.toString(), id)
                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Note removed successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
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

    private fun getTextFromBitmap(imageUri: Uri) {
        val inputImage = InputImage.fromFilePath(requireContext(), imageUri)

        //creating TextRecognizer instance
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        //process the image
        recognizer.process(inputImage)
            .addOnSuccessListener { textAfterRecognize -> //Task completed successfully
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
            bundle.putString("boardId", currentBoard.id.toString())
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
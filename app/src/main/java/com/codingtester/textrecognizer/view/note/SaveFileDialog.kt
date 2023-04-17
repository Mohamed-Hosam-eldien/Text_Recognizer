package com.codingtester.textrecognizer.view.note

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.codingtester.textrecognizer.R
import com.codingtester.textrecognizer.databinding.SaveFileLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.*

@AndroidEntryPoint
class SaveFileDialog : DialogFragment() {

    private lateinit var binding: SaveFileLayoutBinding
    private var filePath: String =
        Environment.getExternalStorageDirectory().path + "/Text Recognizer"
    private var fileExe = "txt"

    private var noteId: Long? = null
    private var noteTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = arguments?.getLong("noteId")
        noteTitle = arguments?.getString("noteTitle")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SaveFileLayoutBinding.inflate(inflater, container, false)

        binding.radioGroupFile.setOnCheckedChangeListener { _, radioID ->
            when (radioID) {
                R.id.radioTxt -> {
                    fileExe = "txt"
                }
                R.id.radioWord -> {
                    fileExe = "word"
                }
            }
        }

        binding.btnSave.setOnClickListener {
            handleSaveFileProcess()
        }

        return binding.root
    }

    private fun handleSaveFileProcess() {
        val fileName = binding.edtFileName.text.toString()
        if (fileName.isNotEmpty()) {
            if (isStoragePermissionGranted()) {
                binding.btnSave.visibility = View.GONE
                binding.progress.visibility = View.VISIBLE
                when (fileExe) {
                    "txt" -> {
                        saveToTextFile(fileName)
                    }
                    "word" -> {
                        saveToWordFile(fileName)
                    }
                }
            } else {
                requestPermission()
            }

        } else {
            binding.edtFileName.error = "required"
        }
    }

    private fun saveToTextFile(fileName: String) {
        makeFileDir()
        val print = PrintWriter("$filePath/${fileName}.txt")
        print.write("Note Id : $noteId \nNote Title :  $noteTitle")
        print.close()
        Toast.makeText(requireContext(), "File saved successfully", Toast.LENGTH_SHORT).show()
        dialog!!.dismiss()
    }

    private fun saveToWordFile(fileName: String) {
        addParagraph(fileName, createWordDoc(), makeFileDir())
    }

    private fun createWordDoc(): XWPFDocument {
        return XWPFDocument()
    }

    private fun makeFileDir(): File {
        val file = File(filePath)
        if (!file.exists()) {
            file.mkdir()
        }
        return file
    }

    private fun addParagraph(fileName: String, targetDoc: XWPFDocument, file: File) {
        //creating a paragraph in our document and setting its alignment
        val paragraph1 = targetDoc.createParagraph()
        paragraph1.alignment = ParagraphAlignment.LEFT

        //creating a run for adding text
        val sentenceRun1 = paragraph1.createRun()

        //format the text
        sentenceRun1.isBold = true
        sentenceRun1.fontSize = 14
        sentenceRun1.fontFamily = "Comic Sans MS"
        sentenceRun1.setText("Note Id : $noteId \n Note Title : $noteTitle")
        //add a sentence break
        sentenceRun1.addBreak()

        saveToWordDoc(fileName, targetDoc, file)
    }

    private fun saveToWordDoc(fileName: String, targetDoc: XWPFDocument, file: File) {
        val wordFile = File(file, "${fileName}.docx")

        try {
            val fileOut = FileOutputStream(wordFile)
            targetDoc.write(fileOut)
            fileOut.close()
            Toast.makeText(requireContext(), "File saved successfully", Toast.LENGTH_SHORT).show()
            dialog!!.dismiss()
        } catch (e: Exception) {
            binding.btnSave.visibility = View.VISIBLE
            binding.progress.visibility = View.GONE
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 200
        )
    }

    private fun isStoragePermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
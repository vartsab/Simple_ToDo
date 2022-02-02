package com.example.simpletodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfNotes = mutableListOf<String>()
    lateinit var adapter: NoteItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : NoteItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                listOfNotes.removeAt(position)
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }

        loadItems()

        val recyclerView = findViewById<RecyclerView>(R.id.rvNotes)
        adapter = NoteItemAdapter(listOfNotes, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager (this)

        val inputTextField = findViewById<EditText>(R.id.etAddNoteField)

        findViewById<Button>(R.id.btAdd).setOnClickListener {
            val userInputtedNote = inputTextField.text.toString()
            listOfNotes.add(userInputtedNote)
            adapter.notifyItemInserted(listOfNotes.size - 1)
            inputTextField.setText("")
            saveItems()
        }
    }

    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    fun loadItems() {
        try {
            listOfNotes = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfNotes)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}
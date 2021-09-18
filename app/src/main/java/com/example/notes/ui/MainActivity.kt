package com.example.notes.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.NoteAdapter
import com.example.notes.R
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.db.Note
import com.example.notes.listners.NotesListner
import com.example.notes.viewModel.noteViewModel
import java.io.Serializable

class MainActivity : AppCompatActivity(), NotesListner{
    lateinit var viewModel : noteViewModel
    val REQUEST_CODE_ADD_NOTE :Int =1
    val REQUEST_CODE_UPDATE_NOTE :Int =2
    val REQUEST_CODE_SHOW_NOTE: Int =3
    var noteclickedpostion :Int =-1
    lateinit var adapter: NoteAdapter
    lateinit var note: Note
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

        }
    }
    var updateLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView=findViewById(R.id.recyclerview)
        recyclerView.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        adapter = NoteAdapter(this,this)
        recyclerView.adapter=adapter
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(noteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer {
                    adapter.updateList(it)
        })
        val delete:ImageView=findViewById(R.id.add)
        delete.setOnClickListener {
            viewModel.deleteAll()
            adapter.notifyDataSetChanged()
        }
    }
    fun addnote(view: View)
    {
        val intent:Intent= Intent(this,CreateNoteActivity::class.java)
        resultLauncher.launch(intent)
    }

    override fun onNoteClicked(note: Note, position: Int) {
        noteclickedpostion=position
        val intent =Intent(this,CreateNoteActivity::class.java)
        intent.putExtra("isvieworupdate",true)
        intent.putExtra("note",note as Serializable)
        updateLauncher.launch(intent)
    }
}


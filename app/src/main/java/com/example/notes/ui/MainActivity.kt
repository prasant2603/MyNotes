package com.example.notes.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SearchView
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
    var noteclickedpostion :Int =-1
    lateinit var adapter: NoteAdapter
    lateinit var note: Note
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {

        }
    }
    var updateLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Recycler View
        val recyclerView: RecyclerView=findViewById(R.id.recyclerview)
        recyclerView.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        adapter = NoteAdapter(this,this)
        recyclerView.adapter=adapter

        //View Model Initiliazation
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(noteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer {
                    adapter.updateList(it)
        })

        //SearchView Implement
        val searchView: SearchView = findViewById(R.id.search_bar)
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!=null)
                {
                    SearchDatabase(query)
                }
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    searchView.focusable=View.FOCUSABLE
                }
                if(newText!=null)
                {
                    SearchDatabase(newText)
                }
                return true
            }
        })
    }
    fun SearchDatabase(query :String)
    {
        val searchQuery= "%$query%"
        viewModel.searchDatabase(searchQuery).observe(this, Observer {
            adapter.updateList(it)
        })
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


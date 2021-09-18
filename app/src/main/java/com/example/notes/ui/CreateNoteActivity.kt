package com.example.notes.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.notes.R
import com.example.notes.db.Note
import com.example.notes.viewModel.noteViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateNoteActivity : AppCompatActivity()
{
    var imageUri: Uri? =null
    lateinit var imageRemove:ImageView
    lateinit var imageView: ImageView
    val loadImage = registerForActivityResult(ActivityResultContracts.OpenDocument(), ActivityResultCallback {
        if(it!=null) {
            imageView.setImageURI(it)
            imageView.visibility = View.VISIBLE
            imageRemove.visibility =View.VISIBLE
            imageUri=it
        }
        else {
            imageUri=it
            Toast.makeText(this, "Image not laoded", Toast.LENGTH_SHORT).show()
        }
    })
    lateinit var viewModel :noteViewModel
    lateinit var inputTitle:EditText
    lateinit var inputSubTitle:EditText
    lateinit var inputNote:EditText
    lateinit var dateTime:TextView
    lateinit var selectNoteColor :String
    lateinit var viewSubtitleIndicator :View
    var alreadyAvailableNote : Note? =null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        inputTitle = findViewById(R.id.inputNoteTitle)
        inputSubTitle = findViewById(R.id.inputNoteSubTitle)
        inputNote = findViewById(R.id.inputNote)
        dateTime = findViewById(R.id.textDateTime)
        imageView= findViewById(R.id.imageNote)
        imageRemove= findViewById(R.id.imageRemove)
        viewSubtitleIndicator = findViewById(R.id.subTitleIndicator)

        //View Model Declaration
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(noteViewModel::class.java)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm:ss")
        val formatted = current.format(formatter)
        dateTime.setText(formatted)

        val imageback: ImageView=findViewById(R.id.imageBack)
        val imageSave: ImageView=findViewById(R.id.imageSave)

        imageback.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        imageSave.setOnClickListener {
            saveNote()
        }
        //Remove Image
        imageRemove.setOnClickListener {
            imageView.visibility=View.GONE
            imageUri=null
            imageRemove.visibility=View.GONE
        }
        //Remove Note
        findViewById<LinearLayout>(R.id.layoutDeleteNote).setOnClickListener {
            alreadyAvailableNote?.let { it1 -> viewModel.delete(it1) }
            Toast.makeText(this,"Note Deleted ",Toast.LENGTH_LONG).show()
            findViewById<LinearLayout>(R.id.layoutDeleteNote).visibility=View.GONE
            finish()
        }

        selectNoteColor="#FDBE38"
        if(intent.getBooleanExtra("isvieworupdate",false))
        {
            alreadyAvailableNote = intent.getSerializableExtra("note") as Note
            setViewOrUpdate()
        }
        initMiscellaneous()
        setSubTitleIndicator()
    }
    private fun setViewOrUpdate()
    {
        inputTitle.setText(alreadyAvailableNote?.title)
        inputSubTitle.setText(alreadyAvailableNote?.subtitle)
        inputNote.setText(alreadyAvailableNote?.imageUri)
        dateTime.setText(alreadyAvailableNote?.datetime)
        if (alreadyAvailableNote!=null)
            findViewById<LinearLayout>(R.id.layoutDeleteNote).visibility=View.VISIBLE
        if(alreadyAvailableNote?.imageUri!=null)
        {
            imageUri= alreadyAvailableNote?.imageUri?.toUri()
            imageView.setImageURI(imageUri)
            imageView.visibility=View.VISIBLE
            imageRemove.visibility=View.VISIBLE
        }
    }
    private fun saveNote()
    {
        if(inputTitle.text.toString().trim().isEmpty())
        {
            Toast.makeText(this,"Note Title Cannot be Empty",Toast.LENGTH_SHORT).show()
            return
        }
        else if(inputSubTitle.text.toString().isEmpty() && inputNote.text.toString().isEmpty())
        {
            Toast.makeText(this,"Note Cannot be Empty",Toast.LENGTH_SHORT).show()
            return
        }
        val title :String =inputTitle.text.toString()
        val subtitle: String =inputSubTitle.text.toString()
        val notetext: String =inputNote.text.toString()
        val datetime: String=dateTime.text.toString()
        val color: String=selectNoteColor
        val image: Uri? = imageUri
        val note = Note(title,datetime,subtitle,notetext,image.toString(),color)
        if(alreadyAvailableNote!=null)
        {
            note.id= alreadyAvailableNote!!.id
        }
        viewModel.addNote(note)
        val intent=Intent()
        setResult(RESULT_OK,intent)
        finish()
    }
    private fun initMiscellaneous()
    {
        val layoutMiscellaneous: LinearLayout= findViewById(R.id.layout_misc)
        val bottomSheetBehavior  = BottomSheetBehavior.from(layoutMiscellaneous)
        layoutMiscellaneous.findViewById<TextView>(R.id.textmisc).setOnClickListener {
            if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            else
                bottomSheetBehavior.state= BottomSheetBehavior.STATE_COLLAPSED
        }
        val imagecolor1 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor1)
        val imagecolor2 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor2)
        val imagecolor3 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor3)
        val imagecolor4 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor4)
        val imagecolor5 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor5)
        layoutMiscellaneous.findViewById<View>(R.id.view_color1).setOnClickListener {
            selectNoteColor = "#333333"
            imagecolor1.setImageResource(R.drawable.ic_done)
            imagecolor2.setImageResource(0)
            imagecolor3.setImageResource(0)
            imagecolor4.setImageResource(0)
            imagecolor5.setImageResource(0)
            setSubTitleIndicator()
        }
        layoutMiscellaneous.findViewById<View>(R.id.view_color2).setOnClickListener {
            selectNoteColor = "#FDBE38"
            imagecolor2.setImageResource(R.drawable.ic_done)
            imagecolor1.setImageResource(0)
            imagecolor3.setImageResource(0)
            imagecolor4.setImageResource(0)
            imagecolor5.setImageResource(0)
            setSubTitleIndicator()
        }
        layoutMiscellaneous.findViewById<View>(R.id.view_color3).setOnClickListener {
            selectNoteColor = "#FB8A4D"
            imagecolor3.setImageResource(R.drawable.ic_done)
            imagecolor1.setImageResource(0)
            imagecolor2.setImageResource(0)
            imagecolor4.setImageResource(0)
            imagecolor5.setImageResource(0)
            setSubTitleIndicator()
        }
        layoutMiscellaneous.findViewById<View>(R.id.view_color4).setOnClickListener {
            selectNoteColor = "#3DA4F3"
            imagecolor4.setImageResource(R.drawable.ic_done)
            imagecolor1.setImageResource(0)
            imagecolor3.setImageResource(0)
            imagecolor2.setImageResource(0)
            imagecolor5.setImageResource(0)
            setSubTitleIndicator()
        }
        layoutMiscellaneous.findViewById<View>(R.id.view_color5).setOnClickListener {
            selectNoteColor = "#171716"
            imagecolor5.setImageResource(R.drawable.ic_done)
            imagecolor1.setImageResource(0)
            imagecolor3.setImageResource(0)
            imagecolor4.setImageResource(0)
            imagecolor2.setImageResource(0)
            setSubTitleIndicator()
        }
        if(alreadyAvailableNote!=null && alreadyAvailableNote?.color?.isNotEmpty() == true)
        {
            when(alreadyAvailableNote?.color)
            {
                "#FDBE38" ->layoutMiscellaneous.findViewById<View>(R.id.view_color2).performClick()
                "#FB8A4D" ->layoutMiscellaneous.findViewById<View>(R.id.view_color3).performClick()
                "#3DA4F3" ->layoutMiscellaneous.findViewById<View>(R.id.view_color4).performClick()
                "#171716" ->layoutMiscellaneous.findViewById<View>(R.id.view_color5).performClick()
            }
        }
        layoutMiscellaneous.findViewById<ImageView>(R.id.addImage).setOnClickListener {
            bottomSheetBehavior.state=BottomSheetBehavior.STATE_COLLAPSED
            loadImage.launch(arrayOf("image/*"))
        }
    }
    private fun setSubTitleIndicator()
    {
        val gradientDrawable :GradientDrawable = viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(selectNoteColor))
    }
}
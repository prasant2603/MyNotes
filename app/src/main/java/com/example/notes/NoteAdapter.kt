package com.example.notes

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.NoteAdapter.NoteViewHolder
import com.example.notes.db.Note
import com.example.notes.listners.NotesListner
import com.makeramen.roundedimageview.RoundedImageView

class NoteAdapter(val context: Context,val notesListner: NotesListner) : RecyclerView.Adapter<NoteViewHolder>()
{
    val allNotes = ArrayList<Note>()
    class NoteViewHolder(itemView :View) :RecyclerView.ViewHolder(itemView)
    {
        val title=itemView.findViewById<TextView>(R.id.textTitle)
        val subtitle=itemView.findViewById<TextView>(R.id.textSubTitle)
        val datetime=itemView.findViewById<TextView>(R.id.DateTime)
        val layout=itemView.findViewById<LinearLayout>(R.id.layoutNote)
        val imageView=itemView.findViewById<RoundedImageView>(R.id.imageNote)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder=NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note,parent,false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currnote= allNotes[position]

        //Handling Click on Notes Item
        holder.layout.setOnClickListener {
            notesListner.onNoteClicked(currnote,position)
        }

        holder.title.setText(currnote.title.toString())

        if(currnote.subtitle?.trim()?.isEmpty() == true)
            holder.subtitle.visibility=View.GONE
        else
            holder.subtitle.setText(currnote.subtitle.toString())

        //Layout BackGround
        val gradientDrawable: GradientDrawable = holder.layout.background as GradientDrawable
        if(currnote.color?.isNotEmpty() == true)
            gradientDrawable.setColor(Color.parseColor(currnote.color))
        else
            gradientDrawable.setColor(Color.parseColor("#333333"))

        //ImageView
        if(currnote.imageUri?.toUri()!=null) {
            holder.imageView.setImageURI(currnote.imageUri.toUri())
            holder.imageView.visibility= View.VISIBLE
        }
        //Date and Time
        holder.datetime.setText(currnote.datetime.toString())
    }
    override fun getItemCount(): Int {
        return allNotes.size
    }
    fun updateList(newList : List<Note>)
    {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }
}
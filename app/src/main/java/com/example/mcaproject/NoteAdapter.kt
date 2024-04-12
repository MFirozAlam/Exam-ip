package com.example.mcaproject

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.mcaproject.databinding.NotesItemBinding

class NoteAdapter(private val notes: List<NotesItems>,private val itemClickListener: OnItemClickListener):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    lateinit var selectedValue:String
    interface OnItemClickListener {

        fun onDeleteClick(noteId: String)
        fun onUpdateClick(noteId: String, title: String, description: String)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val mBinding = NotesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(mBinding,selectedValue)
    }

    fun setValue(selectedValue: String){
         this.selectedValue = selectedValue
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.mBinding.updateButton.setOnClickListener {
            itemClickListener.onUpdateClick(note.noteId,note.title,note.description)
        }
        holder.mBinding.deleteButton.setOnClickListener {
            itemClickListener.onDeleteClick(note.noteId)
        }

    }
    override fun getItemCount(): Int {
        return notes.size

    }

    fun updateList(noteList: MutableList<NotesItems>) {
        var noteItems = noteList
        notifyDataSetChanged()

    }

    class NoteViewHolder(val mBinding:NotesItemBinding,val selectedValue: String):
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(notes: NotesItems) {
            mBinding.titleTextview.text=notes.title
            mBinding.descriptionTextView.text=notes.description
            if(selectedValue=="parent"){
                mBinding.updateButton.visibility=View.INVISIBLE
                mBinding.deleteButton.visibility=View.INVISIBLE
            }
        }

    }
}
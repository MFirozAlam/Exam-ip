package com.example.mcaproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcaproject.databinding.ActivityAllNotesBinding
import com.example.mcaproject.databinding.DailogUpdateNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.getValue
import java.util.EventListener

class AllNotes : AppCompatActivity(), NoteAdapter.OnItemClickListener {
    private lateinit var mBinding: ActivityAllNotesBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    lateinit var selectedValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAllNotesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        selectedValue = intent.getStringExtra("selectedValue").toString()


        recyclerView = mBinding.noteRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let { user ->
            val noteReference: DatabaseReference =
                databaseReference.child("users").child(user.uid).child("notes")
            val addValueEventListener =
                noteReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val noteList: MutableList<NotesItems> = mutableListOf<NotesItems>()
                        for (noteSnapshot: DataSnapshot in snapshot.children) {
                            val note: NotesItems? = noteSnapshot.getValue(NotesItems::class.java)
                            note?.let {
                                noteList.add(it)
                            }

                        }
                        noteList.reverse()

                        val adapter = NoteAdapter(noteList, this@AllNotes)
                        adapter.setValue(selectedValue)
                        recyclerView.adapter = adapter


                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

        }


    }

    override fun onDeleteClick(noteId: String) {
        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let { user ->
            val noteRefrence:DatabaseReference = databaseReference.child("users").child(user.uid).child("notes")
            noteRefrence.child(noteId).removeValue()
        }
    }

    override fun onUpdateClick(noteId: String,currentTitle:String,currentDescription:String) {
        val dialogBinding:DailogUpdateNoteBinding= DailogUpdateNoteBinding.inflate(LayoutInflater.from(this))
        val dialog= AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update Notes")
            .setPositiveButton("Update"){dialog, _->
                val newTitle:String=dialogBinding.updateNoteTitle.text.toString()
                val newDescription:String=dialogBinding.updateNoteDescription.text.toString()
                updateNoteDatabase(noteId,newTitle,newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel1"){dialog,_->
                dialog.dismiss()
            }
            .create()
        dialogBinding.updateNoteTitle.setText(currentTitle)
        dialogBinding.updateNoteDescription.setText(currentDescription)
        dialog.show()

    }

    private fun updateNoteDatabase(noteId: String, newTitle: String, newDescription: String) {
        val currentUser:FirebaseUser?=auth.currentUser
        currentUser?.let { user ->
            val noteReference: DatabaseReference =
                databaseReference.child("users").child(user.uid).child("notes")
            val updateNote = NotesItems(newTitle, newDescription, noteId)
            noteReference.child(noteId).setValue(updateNote)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note Update Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Note Update Is Not Successful", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

        }

    }
}


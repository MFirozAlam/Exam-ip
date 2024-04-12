package com.example.mcaproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mcaproject.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddNoteActivity : AppCompatActivity() {
    private lateinit var mbinding: ActivityAddNoteBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var  auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        val db = Firebase.firestore
        databaseReference =FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        mbinding.saveNote.setOnClickListener{

            val title: String = mbinding.etTitle.text.toString()
            val description :String = mbinding.etdiscription.text.toString()

            if(title.isEmpty() && description.isEmpty()){
                Toast.makeText(this, "Fill both field", Toast.LENGTH_SHORT).show()
            }else{
                val currentUser :FirebaseUser? = auth.currentUser
                currentUser?.let { user->
                    val noteKey:String? =  databaseReference.child("users").child(user.uid).child("notes").push().key
                    val noteItem = NotesItems(title,description,noteKey ?:"")
                    if(noteKey!= null)
                        databaseReference.child("users").child(user.uid).child("notes").child(noteKey).setValue(noteItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this,"Note Save successful", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this,"Fail to Save Notes: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    // Log the error for debugging
                                    Log.e("AddNoteActivity", "Failed to save note", task.exception)
                                }
                            }

                }

            }


        }

    }
}
package com.example.mcaproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mcaproject.databinding.ActivityDatabaseBinding

class DatabaseActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDatabaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var selectedValue=intent.getStringExtra("selectedID")
        mBinding = ActivityDatabaseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.createnew.setOnClickListener {
            startActivity(Intent(this,AddNoteActivity::class.java))
        }
        mBinding.openall.setOnClickListener {
            val intent = Intent(this,AllNotes::class.java)
            intent.putExtra("selectedValue", selectedValue)
            startActivity(intent)
        }
    }
}
package com.example.mcaproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.util.jar.Attributes.Name

class FormActivity : Activity() {

    private lateinit var yourName: EditText
    private lateinit var parentNameName: EditText
    private lateinit var address: EditText
    private lateinit var mobile: EditText
    private lateinit var studentGpa: EditText
    private lateinit var dob: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var radioGroup: RadioGroup


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        yourName = findViewById(R.id.yourName)
        parentNameName = findViewById(R.id.parentName)
        address = findViewById(R.id.address)
        mobile = findViewById(R.id.mobile)
        studentGpa = findViewById(R.id.stream)
        dob = findViewById(R.id.dob)
        dob.text = "DOB: ${LocalDate.now()}"

         radioGroup =
            findViewById<RadioGroup>(R.id.chooseOne)

         radioGroup.setOnCheckedChangeListener { group, checkedId ->
                var radioButton = group.findViewById<RadioButton>(checkedId)
                if (R.id.radio_one == checkedId) {
                    Toast.makeText(this, "${radioButton.text}+Is selected", Toast.LENGTH_SHORT)
                        .show()
                }
                if (R.id.radio_two == checkedId) {
                    Toast.makeText(this, "${radioButton.text}+Is selected", Toast.LENGTH_SHORT)
                        .show()
                }
                if (R.id.radio_three == checkedId) {
                    Toast.makeText(this, "${radioButton.text}+Is selected", Toast.LENGTH_SHORT)
                        .show()
                }

            }


        /*findViewById<RadioButton>(R.id.radio_one).setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("RADIO", "student is checked: $isChecked")
        }

        findViewById<RadioButton>(R.id.radio_two).setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("RADIO", "parent is checked: $isChecked")
        }
        findViewById<RadioButton>(R.id.radio_three).setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("RADIO", "teacher is checked: $isChecked")
        }*/


        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            val name = yourName.text.toString()
            val parentName = parentNameName.text.toString()
            val address = address.text.toString()
            val mobile = mobile.text.toString()
            val gpa = studentGpa.text.toString()
            if (name.isEmpty() || address.isEmpty() || mobile.isEmpty() || gpa.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(applicationContext, "Form submitted", Toast.LENGTH_SHORT).show()
            }

            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId==R.id.radio_one){
                val intent = Intent(this, WebActivity::class.java)
                startActivity(intent)
            }
            if (selectedId==R.id.radio_two|| selectedId==R.id.radio_three){
                val intent = Intent(this, DatabaseActivity::class.java)
                if(selectedId==R.id.radio_two){
                    intent.putExtra("selectedID","parent")
                }
                if(selectedId==R.id.radio_three){
                    intent.putExtra("selectedID","teacher")
                }
                startActivity(intent)
            }

        }

        /**Google SignOut
         * */
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        findViewById<Button>(R.id.btnSignOut).setOnClickListener {
            GoogleSignIn.getClient(this, gso).signOut()
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}


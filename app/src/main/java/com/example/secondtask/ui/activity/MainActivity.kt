package com.example.secondtask.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import com.example.secondtask.R
import com.example.secondtask.databinding.ActivityMainBinding
import com.example.secondtask.ui.activity.LoginActivity
import com.example.secondtask.ui.activity.RegistrationActivity

import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.butregister.setOnClickListener {
            var intent = Intent(this@MainActivity, RegistrationActivity::class.java
            )
            startActivity(intent)
            var email:String=mainBinding.editEmail.text.toString()
            var password:String=mainBinding.editPassword.text.toString()

            auth.createUserWithEmailAndPassword(email,password).
            addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Login Sucessfull", Toast.LENGTH_LONG).show()

                    //navigate to dashboard
                    var intent=Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext,it.exception?.message, Toast.LENGTH_LONG).show()

                }
            }
        }




        mainBinding.butlogin.setOnClickListener {
            var intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            var email:String=mainBinding.editEmail.text.toString()
            var password:String=mainBinding.editPassword.text.toString()

            auth.createUserWithEmailAndPassword(email,password).
            addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Registration Sucessfull", Toast.LENGTH_LONG).show()

                    //navigate to dashboard
                    var intent=Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext,it.exception?.message, Toast.LENGTH_LONG).show()

                }
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}


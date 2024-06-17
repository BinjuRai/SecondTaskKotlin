package com.example.secondtask.ui.activity



import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import com.example.secondtask.R
import com.example.secondtask.databinding.ActivityForgetProductBinding
import com.google.firebase.auth.FirebaseAuth


class ForgetActivity : AppCompatActivity() {

    lateinit var forgetProductBinding: ActivityForgetProductBinding
    var auth:FirebaseAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        forgetProductBinding=ActivityForgetProductBinding.inflate(layoutInflater)
        setContentView(forgetProductBinding.root)

        forgetProductBinding.btnV.setOnClickListener {
            var email:String=forgetProductBinding.ForgetEmail.text.toString()

            auth.sendPasswordResetEmail(email).
            addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Registration Sucessfull", Toast.LENGTH_LONG).show()
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
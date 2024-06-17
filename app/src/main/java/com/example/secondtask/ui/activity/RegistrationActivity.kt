package com.example.secondtask.ui.activity



import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud.model.UserModel
import com.example.secondtask.R
import com.example.secondtask.databinding.ActivityRegistrationBinding
import com.example.secondtask.utils.ImageUtils

import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.util.UUID

class RegistrationActivity : AppCompatActivity() {
    lateinit var registrationBinding: ActivityRegistrationBinding

    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("user")


    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    var imageUri : Uri? = null

    lateinit var imageUtils: ImageUtils

    lateinit var userViewmodel: UserModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        registrationBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(registrationBinding.root)

        imageUtils = ImageUtils(this)
        imageUtils.registerActivity { url ->
            url.let {
                imageUri = url
                Picasso.get().load(url).into(registrationBinding.imagebrowse)
            }
        }

        registrationBinding.imagebrowse.setOnClickListener{
            imageUtils.launchGallery(this)
        }
        registrationBinding.buttonregister2.setOnClickListener {
            if (imageUri != null){
                uploadImage()
            }else{
                Toast.makeText(applicationContext,"Please upload image first",Toast.LENGTH_LONG)
                    .show()
            }
        }

        registrationBinding.buttonregister2.setOnClickListener {
            var name : String = registrationBinding.editname.text.toString()
            var email : String = registrationBinding.editemail.text.toString()
            var number : Int = registrationBinding.editNumber.text.toString().toInt()
            var password : String = registrationBinding.editPassword.text.toString()

            Toast.makeText(applicationContext, "Registration Success", Toast.LENGTH_LONG).show()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun uploadImage(){
        val imageName = UUID.randomUUID().toString()
        imageUri?.let {
            userViewmodel.uploadImage(imageName,it){ success, imageUrl ->
                if (success){
                    addUser(imageUrl.toString(),imageName.toString())
                }
            }

        }

    }
    fun addUser(url:String, imageName: String){
        var name : String = registrationBinding.editname.text.toString()
        var email : String = registrationBinding.editemail.text.toString()
        var number : Int = registrationBinding.editNumber.text.toString().toInt()
        var password : String = registrationBinding.editPassword.text.toString()
        var data = UserModel("",name,email,number,password,url,imageName)


    }


}




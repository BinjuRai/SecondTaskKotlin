package com.example.secondtask.activity

import com.example.crud.model.UserModel
import com.example.secondtask.R
import com.example.secondtask.repository.ProductRepositoryImpl
import com.example.secondtask.utils.ImageUtils
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

import com.example.userauthentication.databinding.ActivityUpdateUserBinding
import com.example.userauthentication.databinding.EditUserBinding
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UpdateUserActivity : AppCompatActivity() {
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("user")

    lateinit var updateUserBinding: ActivityUpdateUserBinding
    var id = ""
    var imageName= ""

    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    var imageUri : Uri? = null

    lateinit var  userViewModel: UserModel

    lateinit var editBinding: EditUserBinding

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
    lateinit var imageUtils: ImageUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        updateUserBinding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(updateUserBinding.root)

        val repo = ProductRepositoryImpl()
        userViewModel = UserModel(repo)

        imageUtils = ImageUtils(this)
        imageUtils.registerActivity {
            imageUri = it

            Picasso.get().load(it).into(updateUserBinding.imageupdate)
        }
        var user: UserModel?= intent.getParcelableExtra("user")
        id = user?.id.toString()
        imageName = user?.imageName.toString()
        updateUserBinding.updateemail.setText(user?.email)
        updateUserBinding.updateNumber.setText(user?.number.toString())
        updateUserBinding.updatePassword.setText(user?.password)
        Picasso.get().load(user?.url).into(updateUserBinding.imageupdate)

        updateUserBinding.buttonupdate.setOnClickListener {
            uploadImage()
        }
        updateUserBinding.imageupdate.setOnClickListener {
            imageUtils.launchGallery(this@UpdateUserActivity)

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun updateUser(url:String){
        var updatedemail : String = updateUserBinding.updateemail.text.toString()
        var updatednumber : Int = updateUserBinding.updateNumber.text.toString().toInt()
        var updatedpassword : String = updateUserBinding.updatePassword.text.toString()

        var data = mutableMapOf<String,Any>()
        data["email"]= updatedemail
        data["number"]= updatednumber
        data["password"]= updatedpassword
        data["url"]= url

        userViewModel.updateUser(id,data){
                success,message ->
            if (success){
                Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun uploadImage() {
        imageUri?.let{
            userViewModel.uploadImage(imageName,it){
                success,imageUrl ->
                if(success){
                    updateUser(imageUrl.toString())
                }
            }
        }

    }
}


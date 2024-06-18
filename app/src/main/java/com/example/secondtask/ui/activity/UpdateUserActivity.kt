package com.example.secondtask.ui.activity

import com.example.crud.model.UserModel
import com.example.secondtask.R
import com.example.secondtask.repository.UserRepositoryImpl
import com.example.secondtask.utils.ImageUtils
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.secondtask.databinding.ActivityUpdateUserBinding
import com.example.secondtask.utils.LoadingUtils

import com.example.secondtask.viewmodel.userViewModel
import com.google.firebase.auth.FirebaseAuth


import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UpdateUserActivity : AppCompatActivity() {
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("user")

    lateinit var updateUserBinding: ActivityUpdateUserBinding


    var id = ""
    var imageName = ""

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null

    lateinit var userViewModel: userViewModel

    lateinit var imageUtils: ImageUtils

    lateinit var loadingUtils: LoadingUtils

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        updateUserBinding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(updateUserBinding.root)

        val repo = UserRepositoryImpl()
        userViewModel = userViewModel(repo)

        loadingUtils = LoadingUtils(this)


        imageUtils = ImageUtils(this)
        imageUtils.registerActivity { url ->
            url.let {
                imageUri = url
                Picasso.get().load(url).into(updateUserBinding.ImageViewU)
            }
        }
        var user: UserModel? = intent.getParcelableExtra("user")
        id = user?.id.toString()
        imageName= user?.imageName.toString()
        updateUserBinding.editUEmail.setText(user?.email)
        updateUserBinding.editUNumber.setText(user?.number.toString())
        updateUserBinding.editUPassword.setText(user?.password)

        Picasso.get().load(user?.url).into(updateUserBinding.ImageViewU)

        updateUserBinding.btnupdate.setOnClickListener {

            if(imageUri == null){
                updateUser(user?.url.toString())
            }else{

                uploadImage()
            }
        }
        updateUserBinding.ImageViewU.setOnClickListener {
            imageUtils.launchGallery(this@UpdateUserActivity)

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun updateUser(url: String) {
        loadingUtils.showLoading()
        var updatedemail: String = updateUserBinding.editUEmail.text.toString()
        var updatednumber: Int = updateUserBinding.editUNumber.text.toString().toInt()
        var updatedpassword: String = updateUserBinding.editUPassword.text.toString()

        var data = mutableMapOf<String, Any>()
        data["email"] = updatedemail
        data["number"] = updatednumber
        data["password"] = updatedpassword
        data["url"] = url

        userViewModel.updateUser(id, data) { success, message ->
            if (success) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                loadingUtils.dismiss()
            } else {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                loadingUtils.dismiss()
            }

        }

    }


    fun uploadImage() {
        loadingUtils.showLoading()
        imageUri?.let {
            userViewModel.uploadImage(imageName, it) { success, imageUrl ->
                if (success) {
                    updateUser(imageUrl.toString())
                }
            }

//            userViewModel.uploadImage(imageName,imageUri) { success, message ->
//                if (success) {
//                    LoadingUtils.dismiss()
//                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                    finish()
//                } else {
//                    LoadingUtils.dismiss()
//                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                }
//
//            }


        }

    }
}



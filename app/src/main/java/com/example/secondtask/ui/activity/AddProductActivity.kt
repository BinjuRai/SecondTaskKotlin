package com.example.secondtask.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud.R
import com.example.crud.databinding.ActivityAddProduct2Binding
import com.example.crud.model.ProductModel
import com.example.crud.reposotory.ProductRepositoryImpl
import com.example.crud.utils.ImageUtils
import com.example.crud.utils.LoadingUtils
import com.example.crud.viewmodel.ProductViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class AddProductActivity : AppCompatActivity() {

    lateinit var loadingUtils: LoadingUtils

    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("products")

    lateinit var imageUtils: ImageUtils
    lateinit var productViewModel: ProductViewModel

    lateinit var addProductBinding: ActivityAddProductBinding

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null
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
        addProductBinding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(addProductBinding.root)

        loadingUtils= LoadingUtils(this)

        imageUtils= ImageUtils(this )
        imageUtils.registerActivity{url ->
            url.let{
                imageUri= it
                Picasso.get().load(it).into(addProductBinding.imageBrowse)
            }
        }
        var repo=ProductRepositoryImpl()
        productViewModel= ProductViewModel(repo)


        addProductBinding.imageBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }


        addProductBinding.btnsave.setOnClickListener {
            if (imageUri != null) {
                uploadImage()
            } else {
                Toast.makeText(
                    applicationContext, "Please upload image first",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun registerActivityForResult() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->

                val resultcode = result.resultCode
                val imageData = result.data
                if (resultcode == RESULT_OK && imageData != null) {
                    imageUri = imageData.data
                    imageUri?.let {
                        Picasso.get().load(it).into(addProductBinding.imageBrowse)
                    }
                }

            })
    }

    fun uploadImage() {
        loadingUtils.showLoading()
        var imageName = UUID.randomUUID().toString()

        imageUri?.let {
            productViewModel.uploadImage(imageName,it){ success,imageUrl->
                if(success){
                    addProduct(imageUrl.toString(),imageName.toString())
                }
            }
        }
    }

    fun addProduct(url: String, imageName: String) {
        var name: String = addProductBinding.editTextProductName.text.toString()
        var price: Int = addProductBinding.editTextProductPrice.text.toString().toInt()
        var description: String = addProductBinding.editTextProductDes.text.toString()
        var data = ProductModel("", name, price, description, url, imageName)

        productViewModel.addProduct(data) { success, message ->
            if (success) {
                loadingUtils.dismiss()
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                finish()
            } else {
                loadingUtils.dismiss()
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }

        }

    }

}
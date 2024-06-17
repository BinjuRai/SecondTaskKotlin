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


import com.example.crud.model.ProductModel
import com.example.secondtask.R

import com.example.secondtask.databinding.ActivityUpdateProductBinding
import com.example.secondtask.repository.ProductRepositoryImpl
import com.example.secondtask.utils.ImageUtils
import com.example.secondtask.viewmodel.ProductViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class UpdateProductActivity : AppCompatActivity() {
    lateinit var updateProductBinding: ActivityUpdateProductBinding
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref=firebaseDatabase.reference.child("products")

    var id =""
    var imageName=""


    lateinit var imageUtils: ImageUtils
    lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        updateProductBinding=ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(updateProductBinding.root)

        val repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)
        imageUtils= ImageUtils(this)
        imageUtils.registerActivity {
            imageUri=it
            Picasso.get().load(it).into(updateProductBinding.ImageViewU)
        }

        var product:ProductModel?= intent.getParcelableExtra("product")
        id=product?.id.toString()
        imageName=product?.imageName.toString()


        updateProductBinding.editUTextProductName.setText(product?.name)
        updateProductBinding.editUTextProductPrice.setText(product?.price.toString())
        updateProductBinding.editUTextProductDes.setText((product?.description))

        Picasso.get().load(product?.url).into(updateProductBinding.ImageViewU)
        updateProductBinding.btnupdate.setOnClickListener{

            if(imageUri == null){
                updateProduct(product?.url.toString())
            }else{
                uploadImage()
            }

        }
        updateProductBinding.ImageViewU.setOnClickListener{
            imageUtils.launchGallery(this@UpdateProductActivity)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    var imageUri : Uri? = null
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
    fun updateProduct(url:String){
        var updatedname:String=updateProductBinding.editUTextProductName.text.toString()
        var updatedprice:Int=updateProductBinding.editUTextProductPrice.text.toString().toInt()
        var updateddescription:String=updateProductBinding.editUTextProductDes.text.toString()



        var data = mutableMapOf<String,Any>()
        data["name"]=updatedname
        data["price"]=updatedprice
        data["description"]=updateddescription
        data["url"]=url

        productViewModel.updateProduct(id,data){
                success,message->
            if(success){
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun uploadImage(){
        imageUri?.let {
            productViewModel.uploadImage(imageName, it){
                    success,imageUri ->
                if(success){
                    updateProduct(imageUri.toString())
                }
            }
        }
    }

}



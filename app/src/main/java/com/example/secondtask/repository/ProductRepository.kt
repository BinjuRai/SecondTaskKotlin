package com.example.secondtask.repository

import android.net.Uri
import com.example.crud.model.ProductModel

interface ProductRepository {

    fun uploadImage(imageName: String, imageUri: Uri, callback: (Boolean, String?) -> Unit)

    fun addProduct(productModel: ProductModel, callback:(Boolean, String?)-> Unit)

    fun getAllProduct( callback: (List<ProductModel>?, Boolean, String?)->Unit)

    fun updateProduct(id: String,data:MutableMap<String,Any>,callback: (Boolean, String?) -> Unit)

    fun deleteData(id:String,callback: (Boolean, String?) -> Unit)


    fun deleteImage(imageName:String,callback: (Boolean, String?) -> Unit)
}
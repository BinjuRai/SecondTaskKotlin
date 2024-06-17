package com.example.secondtask.repository

import android.net.Uri
import com.example.crud.model.UserModel

interface UserRepository {

    fun uploadImage(imageName: String, imageUri: Uri, callback: (Boolean, String?) -> Unit)

    fun addProduct(productModel: UserModel, callback:(Boolean, String?)-> Unit)

    fun getAllProduct( callback: (List<UserModel>?, Boolean, String?)->Unit)

    fun updateProduct(id: String,data:MutableMap<String,Any>,callback: (Boolean, String?) -> Unit)

    fun deleteData(id:String,callback: (Boolean, String?) -> Unit)


    fun deleteImage(imageName:String,callback: (Boolean, String?) -> Unit)
}
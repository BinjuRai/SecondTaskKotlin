package com.example.secondtask.repository



import android.net.Uri
import android.util.Log
import com.example.crud.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserRepositoryImpl: UserRepository {
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("products")
    var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef: StorageReference = firebaseStorage.reference.child("products")

    override fun uploadImage(imageName:String, imageUrl: Uri, callback: (Boolean,  String?) -> Unit) {
//        var imageName = UUID.randomUUID().toString()

        var imageReference = storageRef.child(imageName)
        imageUrl?.let { url ->
            imageReference.putFile(url).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { downloadUrl ->
                    var imageUrl = downloadUrl.toString()
                    callback(true,imageUrl)
                }
            }.addOnFailureListener {
                callback(false,"")

            }
        }
    }


    override fun addProduct(productModel: UserModel, callback: (Boolean, String?) -> Unit) {

        var id = ref.push().key.toString()

        productModel.id=id

        ref.child(id).setValue(productModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true,"Data upload successfully")

            } else {
                callback(false,"Unable to upload data")
            }

        }
    }

    override fun getAllProduct(callback: (List<UserModel>?, Boolean, String?) -> Unit) {
        ref.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var productList = mutableListOf<UserModel>()

                for (eachData in snapshot.children) {
                    var product = eachData.getValue(UserModel::class.java)
                    if (product != null) {
                        Log.d("data from firebase", product.name)
                        Log.d("data from firebase", product.password)
                        Log.d("data from firebase", product.phone.toString())

                        productList.add(product)

                    }


                }

                callback(productList,true,"data fetched")
            }
            override fun onCancelled(error: DatabaseError) {

                callback(null,false,"unable to fetch data ${error.message}")
            }

        })

    }



    override fun updateProduct(id: String, data:MutableMap<String,Any>, callback: (Boolean, String?) -> Unit) {
        data.let {
            ref.child(id).updateChildren(it).addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Your data has been updated")
                }else{
                    callback(false,"Unable to update data")
                }
            }
        }
    }

    override fun deleteData(id: String, callback: (Boolean, String?) -> Unit) {
        ref.child(id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Data has been deleted")

            } else {
                callback(false, "Unable to delete data")

            }
        }
    }
    override fun deleteImage(imageName: String, callback: (Boolean, String?) -> Unit) {
        storageRef.child("products").child(imageName).delete()
            .addOnCompleteListener { if (it.isSuccessful) {
                callback(true, "Image deleted")
            } else {
                callback(false,"Unable to delete image")

            }

            }
    }}
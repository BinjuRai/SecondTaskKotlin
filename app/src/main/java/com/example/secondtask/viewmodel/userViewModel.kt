package com.example.secondtask.viewmodel



import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crud.model.UserModel
import com.example.secondtask.repository.UserRepository


class userViewModel(val repository: UserRepository) : ViewModel() {
    fun updateUser(id: String , data: MutableMap<String,Any>,callback: (Boolean, String?) -> Unit){
        repository.updateProduct(id,data,callback)
    }

    fun uploadImage(imageName:String,imageUrl: Uri, callback: (Boolean,  String?) -> Unit) {
        repository.uploadImage(imageName, imageUrl){
                success,imageUrl ->
            callback(success,imageUrl)
        }

    }
    fun addProduct(productModel: UserModel, callback: (Boolean, String?) -> Unit) {
        repository.addProduct(productModel, callback)
    }

    private var _usertList=MutableLiveData<List<UserModel>?>()
    var userList=MutableLiveData<List<UserModel>?>()
        get() = _usertList

    var _loadingState=MutableLiveData<Boolean>()
    var loadingState=MutableLiveData<Boolean>()
        get() = _loadingState
    fun fetchProduct(){
        _loadingState.value=true
        repository.getAllProduct{userList,success,messgae ->
            if(userList!=null){
                _loadingState.value=false
                _usertList.value=userList
            }
        }
    }
    fun deleteData(id:String,callback: (Boolean, String?) -> Unit){
        repository.deleteData(id,callback)
    }
    fun deleteImage(imageName:String,callback: (Boolean, String?) -> Unit){
        repository.deleteImage(imageName,callback)
    }



}
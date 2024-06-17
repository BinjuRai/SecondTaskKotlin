package com.example.secondtask.viewmodel



import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crud.model.ProductModel
import com.example.secondtask.repository.ProductRepository


class ProductViewModel(val repository: ProductRepository) : ViewModel() {
    fun updateProduct(id: String , data: MutableMap<String,Any>,callback: (Boolean, String?) -> Unit){
        repository.updateProduct(id,data,callback)
    }

    fun uploadImage(imageName:String,imageUrl: Uri, callback: (Boolean,  String?) -> Unit) {
        repository.uploadImage(imageName, imageUrl){
                success,imageUrl ->
            callback(success,imageUrl)
        }

    }
    fun addProduct(productModel: ProductModel, callback: (Boolean, String?) -> Unit) {
        repository.addProduct(productModel, callback)
    }
    private var _productList=MutableLiveData<List<ProductModel>?>()
    var productList=MutableLiveData<List<ProductModel>?>()
        get() = _productList

    var _loadingState=MutableLiveData<Boolean>()
    var loadingState=MutableLiveData<Boolean>()
        get() = _loadingState
    fun fetchProduct(){
        _loadingState.value=true
        repository.getAllProduct{productList,success,messgae ->
            if(productList!=null){
                _loadingState.value=false
                _productList.value=productList
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
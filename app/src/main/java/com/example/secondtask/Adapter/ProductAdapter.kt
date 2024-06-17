package com.example.crud.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


import com.example.crud.model.UserModel
import com.example.secondtask.R
import com.example.secondtask.activity.UpdateProductActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ProductAdapter (var context:Context,var data: ArrayList<UserModel>)
    :RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){
    class ProductViewHolder(view: View):RecyclerView.ViewHolder(view){
        var productName :TextView=view.findViewById(R.id.productName)
        var productPrice:TextView=view.findViewById(R.id.productPrice)
        var productDescription:TextView=view.findViewById(R.id.productDes)
        var btnEdit:TextView=view.findViewById(R.id.btnEdit)
        var progressBar:ProgressBar=view.findViewById(R.id.progressBar)
        var imageView:ImageView=view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var view =LayoutInflater.from(parent.context).inflate(R.layout.sample_product,
            parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.productName.text=data[position].name
        holder.productPrice.text=data[position].price.toString()
        holder.productDescription.text=data[position].description

        var image=data[position].url
        Picasso.get().load(image).into(holder.imageView,object :Callback{
            override fun onSuccess() {
                holder.progressBar.visibility=View.INVISIBLE
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context,e?.localizedMessage,Toast.LENGTH_LONG).show()
            }
        })

        holder.btnEdit.setOnClickListener{
            var intent = Intent(context, UpdateProductActivity::class.java)
            intent.putExtra("product",data[position])
            context.startActivity(intent)
        }
    }
    fun getProductID(position: Int):String{
        return data[position].id
    }
    fun getImageName(position: Int):String{
        return data[position].imageName
    }
    fun updateData(products:List<UserModel>){
        data.clear()
        data.addAll(products)
        notifyDataSetChanged()


    }

}
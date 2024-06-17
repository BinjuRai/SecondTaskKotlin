package com.example.secondtask.ui.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.adapter.ProductAdapter


import com.example.secondtask.activity.AddProductActivity
import com.example.secondtask.databinding.ActivityDashboardBinding
import com.example.secondtask.repository.ProductRepositoryImpl
import com.example.secondtask.viewmodel.ProductViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DashboardActivity : AppCompatActivity() {
    lateinit var dashboardBinding: ActivityDashboardBinding


    lateinit var productAdapter: ProductAdapter
    lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        dashboardBinding=ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding.root)

        val repo= ProductRepositoryImpl()
        productViewModel=ProductViewModel(repo)
        productViewModel.fetchProduct()

        productAdapter=ProductAdapter( this@DashboardActivity,ArrayList())

        dashboardBinding.recyclerView.apply {
            layoutManager=LinearLayoutManager(this@DashboardActivity)
            adapter= productAdapter
        }

        productViewModel.loadingState.observe(this) { loading ->
            if (loading) {
                dashboardBinding.progressBar.visibility = View.VISIBLE

            } else {
                dashboardBinding.progressBar.visibility = View.GONE
            }
        }

        productViewModel.productList.observe(this){products->
            products?.let{
                productAdapter.updateData(it)
            }

        }

        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT
                or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id=productAdapter.getProductID(viewHolder.adapterPosition)
                var imageName=productAdapter.getImageName(viewHolder.adapterPosition)

                productViewModel.deleteData(id){
                        success,message->
                    if(success){
                        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                        productViewModel.deleteImage(imageName){
                                success,message->
                        }
                    }else{
                        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                    }
                }

            }
        }).attachToRecyclerView(dashboardBinding.recyclerView)

        dashboardBinding.floatingActionButton.setOnClickListener{
            var intent=Intent(this@DashboardActivity,
                AddProductActivity::class.java)
            startActivity(intent)
        }


    }
}
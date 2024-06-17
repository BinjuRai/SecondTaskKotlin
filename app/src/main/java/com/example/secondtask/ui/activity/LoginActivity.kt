package com.example.secondtask.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud.adapter.UserAdapter
import com.example.secondtask.R
import com.example.secondtask.activity.UpdateUserActivity
import com.example.secondtask.databinding.ActivityLoginBinding
import com.example.secondtask.repository.UserRepositoryImpl
import com.example.secondtask.viewmodel.userViewModel



import android.content.Intent

import android.view.View
import android.widget.Toast

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding
    lateinit var userAdapter: UserAdapter
    lateinit var userViewModel: userViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        val repo = UserRepositoryImpl()
        userViewModel = userViewModel(repo)

        userViewModel.fetchProduct()

        userAdapter = UserAdapter(this@LoginActivity, ArrayList())
        loginBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@LoginActivity)
            adapter = userAdapter
        }
        userViewModel.loadingState.observe(this) { loading ->
            if (loading) {
                loginBinding.progressBar2.visibility = View.VISIBLE
            } else {
                loginBinding.progressBar2.visibility = View.GONE
            }
        }

        userViewModel.userList.observe(this) { user ->
            user?.let {

                userAdapter.updateData(it)
            }
        }
        ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id = userAdapter.getUserId(viewHolder.adapterPosition)
                var imageName = userAdapter.getImageName(viewHolder.adapterPosition)

                userViewModel.deleteData(id){
                        success,message ->
                    if(success){
                        Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
                        userViewModel.deleteImage(imageName){
                                success,message ->
                        }
                    }else{
                        Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }).attachToRecyclerView(loginBinding.recyclerView)
        loginBinding.floatingActionButton.setOnClickListener {
            var intent = Intent(this@LoginActivity, UpdateUserActivity::class.java)
            startActivity(intent)
        }
    }
}

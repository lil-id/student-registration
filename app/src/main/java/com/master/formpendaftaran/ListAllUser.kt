package com.master.formpendaftaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.master.formpendaftaran.room.userDataModel
import com.master.formpendaftaran.room.userRegisterDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListAllUser : AppCompatActivity(), View.OnClickListener {

    val db by lazy { userRegisterDB(this) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterListAllUser: listAllUserAdapter
    private lateinit var buttonBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_all_data)

        recyclerView = findViewById(R.id.recyler_data_pendaftar)
        buttonBack = findViewById(R.id.buttonBack2)
        buttonBack.setOnClickListener(this)

        setupRecylerViewAllUser()

    }

    override fun onStart() {
        super.onStart()
        showData()
    }

    private fun showData() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = db.queryRegister().getUser()
            withContext(Dispatchers.Main) {
                adapterListAllUser.setData( data )
            }
        }
    }

    private fun setupRecylerViewAllUser() {
        adapterListAllUser = listAllUserAdapter(arrayListOf(), object : listAllUserAdapter.OnAdapterListener{
            override fun onClick(data: userDataModel) {
                startActivity(
                    Intent(applicationContext, UserDetail::class.java)
                        .putExtra("user_id", data.id_user)
                )
            }
        })
        recyclerView.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@ListAllUser)
            adapter = adapterListAllUser
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.buttonBack2 -> {
                val moveIntent = Intent(this@ListAllUser, MainActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }
}
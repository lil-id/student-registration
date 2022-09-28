package com.master.formpendaftaran

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.master.formpendaftaran.room.userRegisterDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class UserDetail : AppCompatActivity(), View.OnClickListener {

    val db by lazy { userRegisterDB(this) }

    private var user_id: Int = 0
    private lateinit var nameUser: TextView
    private lateinit var addressUser: TextView
    private lateinit var locationRegister: TextView
    private lateinit var genderUser: TextView
    private lateinit var telephoneUser: TextView
    private lateinit var buttonBackToMenu: ImageButton
    private lateinit var buttonBack: ImageView
    private lateinit var pictureUserProfile: com.makeramen.roundedimageview.RoundedImageView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        pictureUserProfile = findViewById(R.id.picture_user_profile)
        nameUser = findViewById(R.id.name_user_profile)
        addressUser = findViewById(R.id.address_user_profile)
        locationRegister = findViewById(R.id.location_user_profile)
        genderUser = findViewById(R.id.user_gender_profile)
        telephoneUser = findViewById(R.id.user_telepon_profile)
        buttonBack = findViewById(R.id.imageView6)

        getDataUser()

        buttonBackToMenu = findViewById(R.id.imageButton4)
        buttonBackToMenu.setOnClickListener(this)

        buttonBack = findViewById(R.id.imageView6)
        buttonBack.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataUser() {
        user_id = intent.getIntExtra("user_id", 0)
        CoroutineScope(Dispatchers.IO).launch {

            val dataUser = db.queryRegister().getDetailUserBasedId(user_id)[0]

            var byte: ByteArray? = Base64.getDecoder().decode(dataUser.img)
            var bitmap: Bitmap = BitmapFactory.decodeByteArray(byte, 0, byte?.size!!)

            pictureUserProfile.setImageBitmap(bitmap)
            nameUser.setText(dataUser.nama)
            addressUser.setText(dataUser.alamat)
            locationRegister.setText(dataUser.lokasi_pendaftaran)
            genderUser.setText(dataUser.gender)
            telephoneUser.setText(dataUser.no_hp.toString())
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.imageButton4 -> {
                val moveIntent = Intent(this@UserDetail, MainActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.imageView6 -> {
                val moveIntent = Intent(this@UserDetail, ListAllUser::class.java)
                startActivity(moveIntent)
            }
        }
    }
}
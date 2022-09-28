package com.master.formpendaftaran

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.master.formpendaftaran.room.userDataModel
import com.master.formpendaftaran.room.userRegisterDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val db by lazy { userRegisterDB(this) }

    private lateinit var username: EditText
    private lateinit var address: EditText
    private lateinit var telephone: EditText
    private lateinit var gender: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var myLocation: ImageButton
    private lateinit var chooseButton: ImageButton
    private lateinit var buttonRegister: ImageButton
    private lateinit var buttonDoneRegisteredBefore: TextView
    private val IMG_REQUEST_CODE = 100
    private lateinit var nameFile: TextView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationValue: TextView
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.edtName)
        address = findViewById(R.id.edtAlamat)
        telephone = findViewById(R.id.edtTelepon)
        radioGroup = findViewById(R.id.radioGroup2)
        locationValue = findViewById(R.id.locationValue)
        nameFile = findViewById(R.id.nameFile)
        myLocation = findViewById(R.id.myLocation)
        chooseButton = findViewById(R.id.choosePhoto)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonDoneRegisteredBefore = findViewById(R.id.link_registered)

        geocoder = Geocoder(this, Locale.getDefault()) // inisialisasi geocoder

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        myLocation.setOnClickListener {
            getLocationPermission()
        }

        chooseButton.setOnClickListener {
           getPhotoPermission()
        }

        buttonDoneRegisteredBefore.setOnClickListener(this)

        buttonRegister.setOnClickListener {
            saveData()
        }
    }

    private fun getRadioButtonData(): String {

        var getRadioButtonValue = ""

        val selectRadioButtonId: Int = radioGroup.checkedRadioButtonId

        if (selectRadioButtonId != 1) {
            gender = findViewById(selectRadioButtonId)
            getRadioButtonValue = gender.text.toString()
        }
        return getRadioButtonValue
    }

    private fun getPhotoPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        } else {
            selectImage()
        }
    }

    private fun selectImage() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"), IMG_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage()
        } else {
            Toast.makeText(applicationContext, "Akses tidak diizinkan", Toast.LENGTH_SHORT).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMG_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            var uri: Uri? = data.data
            try {
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                var stream: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                var byte = stream.toByteArray()
                var encode: String = Base64.getEncoder().encodeToString(byte)
                nameFile.text = encode
                nameFile.visibility = View.GONE

                Toast.makeText(applicationContext, "Berhasil memilih foto", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        else {
            Toast.makeText(applicationContext, "Akses tidak diizinkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        } else {
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val locationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude

                            var getAddress = geocoder.getFromLocation(latitude, longitude, 1)
                            locationValue.text = getAddress.get(0).getAddressLine(0).toString()
                        }
                    }
                }
            }
            val client = LocationServices.getFusedLocationProviderClient(applicationContext)
            client.requestLocationUpdates(locationRequest, locationCallback, null)

        }
    }

    private fun saveData() {
        CoroutineScope(Dispatchers.IO).launch {
            db.queryRegister().addUser(
                userDataModel(0, username.text.toString(), address.text.toString(), telephone.text.toString().toLong(),
                    getRadioButtonData(), locationValue.text.toString(), nameFile.text.toString())
            )
            finish()
        }
        val moveIntent = Intent(this@MainActivity, ListAllUser::class.java)
        startActivity(moveIntent)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.link_registered -> {
                val moveIntent = Intent(this@MainActivity, ListAllUser::class.java)
                startActivity(moveIntent)
            }
        }
    }
}
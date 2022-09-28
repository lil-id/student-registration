package com.master.formpendaftaran.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class userDataModel (
    @PrimaryKey(autoGenerate = true) val id_user: Int,
    var nama: String,
    var alamat: String,
    var no_hp: Long,
    var gender: String,
    var lokasi_pendaftaran: String,
    val img: String,
)
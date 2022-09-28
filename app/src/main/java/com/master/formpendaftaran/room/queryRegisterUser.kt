package com.master.formpendaftaran.room

import androidx.room.*

@Dao
interface queryRegisterUser {

    @Insert
    fun addUser(addUser: userDataModel)

    @Query("SELECT * FROM userDataModel WHERE id_user=:idSaldo")
    fun getDetailUserBasedId(idSaldo: Int): List<userDataModel>

    @Query("SELECT * FROM userDataModel")
    fun getUser(): List<userDataModel>
}
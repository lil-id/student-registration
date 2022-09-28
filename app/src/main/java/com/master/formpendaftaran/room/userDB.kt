package com.master.formpendaftaran.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [userDataModel::class]
)

abstract class userRegisterDB : RoomDatabase(){

    abstract fun queryRegister() : queryRegisterUser

    companion object {

        @Volatile private var instance : userRegisterDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            userRegisterDB::class.java,
            "UserDB"
        ).build()

    }
}
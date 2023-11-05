package com.example.ricky_kwong_myruns2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Run::class], version = 1)
//RunDatabase from XD lecture
abstract class RunDatabase: RoomDatabase() {
    abstract val dao: RunDao
    companion object{
        //The Volatile keyword guarantees visibility of changes to the INSTANCE variable across threads
        @Volatile
        private var INSTANCE: RunDatabase? = null

        fun getInstance(context: Context) : RunDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        RunDatabase::class.java, "comment_table").build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
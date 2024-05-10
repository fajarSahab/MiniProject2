package org.d3if3015.miniproject2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3015.miniproject2.model.Pesanan

@Database(entities = [Pesanan::class], version = 1, exportSchema = false)
abstract class PesananDb : RoomDatabase() {
    abstract val dao: PesananDao

    companion object{
        @Volatile
        private var INSTANCE: PesananDb? = null

        fun getInstance(context: Context): PesananDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PesananDb::class.java,
                        "catatan.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
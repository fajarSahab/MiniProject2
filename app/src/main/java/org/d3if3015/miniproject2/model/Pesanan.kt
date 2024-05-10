package org.d3if3015.miniproject2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "daftar_pesanan")
data class Pesanan (
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,
        val nama: String,
        val pesananMakanan: String,
        val pesananMinuman: String,
        val tanggal: String
    )

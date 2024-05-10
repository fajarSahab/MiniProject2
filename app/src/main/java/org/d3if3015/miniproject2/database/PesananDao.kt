package org.d3if3015.miniproject2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3015.miniproject2.model.Pesanan

@Dao
interface PesananDao {
    @Insert
    suspend fun insert(pesanan: Pesanan)

    @Update
    suspend fun update(pesanan: Pesanan)

    @Query("SELECT * FROM daftar_pesanan ORDER BY tanggal DESC")
    fun getPesanan(): Flow<List<Pesanan>>

    @Query("SELECT * FROM daftar_pesanan WHERE id = :id")
    suspend fun getPesananById(id:Long): Pesanan

    @Query("DELETE FROM daftar_pesanan WHERE id = :id")
    suspend fun deleteById(id: Long)
}
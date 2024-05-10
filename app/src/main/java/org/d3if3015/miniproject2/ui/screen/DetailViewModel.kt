package org.d3if3015.miniproject2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3015.miniproject2.database.PesananDao
import org.d3if3015.miniproject2.model.Pesanan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel (private val dao: PesananDao) : ViewModel(){
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(nama: String, pesananmakan: List<String>, pesananminum: List<String>) {
        val pesanan = Pesanan(
            tanggal = formatter.format(Date()),
            nama = nama,
            pesananMakanan = pesananmakan.toString(),
            pesananMinuman = pesananminum.toString()

        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(pesanan)
        }
    }

    suspend fun getPesanan(id: Long): Pesanan? {
        return dao.getPesananById(id)
    }


    fun update(id: Long, nama: String, pesananmakan: List<String>, pesananminum: List<String>) {
        val pesanan = Pesanan(
            id      = id,
            tanggal = formatter.format(Date()),
            nama   = nama,
            pesananMakanan = pesananmakan.toString(),
            pesananMinuman = pesananminum.toString()
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(pesanan)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}

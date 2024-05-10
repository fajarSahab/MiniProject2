package org.d3if3015.miniproject2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3015.miniproject2.database.PesananDao
import org.d3if3015.miniproject2.model.Pesanan

class MainViewModel(dao: PesananDao): ViewModel() {
    val data: StateFlow<List<Pesanan>> = dao.getPesanan().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

}
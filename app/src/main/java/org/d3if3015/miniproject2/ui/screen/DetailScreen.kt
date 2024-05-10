package org.d3if3015.miniproject2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3015.miniproject2.R
import org.d3if3015.miniproject2.database.PesananDb
import org.d3if3015.miniproject2.ui.theme.MiniProject2Theme
import org.d3if3015.miniproject2.util.ViewModelFactory


const val KEY_ID_CATATAN = "idCatatan"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id:Long? = null){
    val context = LocalContext.current
    val db = PesananDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    var nama by rememberSaveable { mutableStateOf("") }
    var pesananmakan by rememberSaveable { (mutableStateOf(listOf<String>()))}
    var pesananminum by rememberSaveable { (mutableStateOf(listOf<String>()))}
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true){
        if (id == null)return@LaunchedEffect
        val data = viewModel.getPesanan(id)?: return@LaunchedEffect
        nama = data.nama
        pesananmakan = listOf(data.pesananMakanan)
        pesananminum = listOf(data.pesananMinuman)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_pesanan))
                    else
                        Text(text = stringResource(id = R.string.edit_pesanan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (nama == "" || pesananmakan.isEmpty() || pesananminum.isEmpty()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(nama, pesananmakan, pesananminum)
                        } else {
                            viewModel.update(id, nama, pesananmakan, pesananminum)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(id = R.string.simpan)
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ){pading ->
        FormPesanan(
            nama = nama,
            onNamaChange = { nama = it },
            pesananMakananList = pesananmakan,
            onPesananMakananChange = { pesananmakan = it },
            pesananMinumanList = pesananminum,
            onPesananMinumanChange = { pesananminum = it },
            modifier = Modifier.padding(pading)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun FormPesanan(
    nama: String,
    onNamaChange: (String) -> Unit,
    pesananMakananList: List<String>,
    onPesananMakananChange: (List<String>) -> Unit,
    pesananMinumanList: List<String>,
    onPesananMinumanChange: (List<String>) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nama,
            onValueChange = { onNamaChange(it) },
            label = { Text(text = stringResource(id = R.string.nama)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            val makananList = listOf("Nasi Goreng", "Mie Goreng", "Kentang Goreng", "Mix Platter", "Onion Rings")
            makananList.forEach { pesananOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onPesananMakananChange(
                                pesananMakananList
                                    .toMutableList()
                                    .apply {
                                        if (contains(pesananOption)) {
                                            remove(pesananOption)
                                        } else {
                                            add(pesananOption)
                                        }
                                    }
                            )
                        }
                ) {
                    Checkbox(
                        checked = pesananMakananList.contains(pesananOption),
                        onCheckedChange = {
                            onPesananMakananChange(
                                pesananMakananList.toMutableList().apply {
                                    if (contains(pesananOption)) {
                                        remove(pesananOption)
                                    } else {
                                        add(pesananOption)
                                    }
                                }
                            )
                        }
                    )
                    Text(
                        text = pesananOption,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            val minumanList = listOf("kirSendiri brown sugar", "kirSendiri blueberry", "Strawberry smoothies", "Es teh", "Lemon tea")
            minumanList.forEach { pesananMinumOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onPesananMinumanChange(
                                pesananMinumanList
                                    .toMutableList()
                                    .apply {
                                        if (contains(pesananMinumOption)) {
                                            remove(pesananMinumOption)
                                        } else {
                                            add(pesananMinumOption)
                                        }
                                    }
                            )
                        }
                ) {
                    Checkbox(
                        checked = pesananMinumanList.contains(pesananMinumOption),
                        onCheckedChange = {
                            onPesananMinumanChange(
                                pesananMinumanList.toMutableList().apply {
                                    if (contains(pesananMinumOption)) {
                                        remove(pesananMinumOption)
                                    } else {
                                        add(pesananMinumOption)
                                    }
                                }
                            )
                        }
                    )
                    Text(
                        text = pesananMinumOption,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    MiniProject2Theme {
        DetailScreen(rememberNavController())
    }
}

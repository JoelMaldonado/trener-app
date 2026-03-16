package com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Correos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jjmf.colegiotrenerandroid.ui.components.SelectHijo.SelectHijo
import com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Correos.components.CorreoDetalleDialog
import com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Correos.components.CorreoFilterRow
import com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Correos.components.CorreoItemCard
import com.jjmf.colegiotrenerandroid.ui.theme.ColorFondo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CorreoScreen(
    viewModel: CorreoViewModel = hiltViewModel()
) {

    val filteredCorreos = viewModel.filteredCorreos()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorFondo
    ) { paddingValues ->
        val bottomPadding = paddingValues.calculateBottomPadding()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 0.dp,
                    bottom = bottomPadding,
                    start = 0.dp,
                    end = 0.dp
                )
                .background(ColorFondo)
        ) {
            SelectHijo(
                click = viewModel::seleccionarHijo
            )

            Spacer(modifier = Modifier.height(12.dp))

            CorreoFilterRow(
                meses = viewModel.meses,
                dias = viewModel.dias,
                selectedMes = viewModel.selectedMes,
                selectedDia = viewModel.selectedDia,
                searchQuery = viewModel.searchQuery,
                onMesSelected = viewModel::onMesSelected,
                onDiaSelected = viewModel::onDiaSelected,
                onSearchChanged = viewModel::onSearchChanged,
                isSearchEnabled = viewModel.hasData,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                viewModel.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator()
                    }
                }

                viewModel.error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = viewModel.error ?: "Ocurrió un error",
                                textAlign = TextAlign.Center,
                                color = Color.Red
                            )
                            Button(onClick = viewModel::retry) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                filteredCorreos.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = "No hay correos para mostrar",
                            textAlign = TextAlign.Center,
                            color = Color.LightGray
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        items(filteredCorreos) { correo ->
                            CorreoItemCard(
                                correo = correo,
                                onVer = { viewModel.openDetalle(correo) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (viewModel.isDialogOpen && viewModel.selectedCorreo != null) {
        CorreoDetalleDialog(
            correo = viewModel.selectedCorreo!!,
            onDismiss = { viewModel.closeDetalle() }
        )
    }
}

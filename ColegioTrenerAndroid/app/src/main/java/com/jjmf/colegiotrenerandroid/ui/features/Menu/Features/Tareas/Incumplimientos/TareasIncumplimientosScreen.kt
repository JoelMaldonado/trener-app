package com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Tareas.Incumplimientos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jjmf.colegiotrenerandroid.ui.components.SelectHijo.SelectHijo
import com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Tareas.Incumplimientos.components.CardTareaIncumplida
import com.jjmf.colegiotrenerandroid.ui.theme.ColorS1

@Composable
fun TareasIncumplimientosScreen(
    viewModel: TareasIncumplimientosViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        SelectHijo(
            click = {
                viewModel.listarIncumplimientos(it)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = Color.Black, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorS1.copy(0.2f)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .background(ColorS1)
                        .padding(vertical = 3.dp, horizontal = 8.dp),
                    text = "Total Acumulado",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = viewModel.totalAcumulado.toString(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .background(ColorS1)
                        .padding(vertical = 3.dp, horizontal = 8.dp),
                    text = "Trimestre",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = viewModel.trimestre?.num ?: "Sin Definir",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            if (!viewModel.isLoadingList) {
                if (viewModel.list.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.list.groupBy { it.semana }.values.toList()) {
                            CardTareaIncumplida(it)
                        }
                    }
                } else {
                    Text(
                        text = "No hay datos para mostrar",
                        modifier = Modifier.padding(top = 30.dp)
                    )
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.padding(top = 30.dp))
            }
        }
    }
}

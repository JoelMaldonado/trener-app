package com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Administrativos.Inscripciones.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jjmf.colegiotrenerandroid.domain.model.Inscripcion
import com.jjmf.colegiotrenerandroid.ui.theme.ColorS1


@Composable
fun CardInscripcion(
    ctacli: String,
    title: String,
    data: List<Inscripcion>,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorS1)
            ) {
                /*Text(
                    text = "Codigo: 154",
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.CenterStart),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )*/
                data.forEach {
                    Text(
                        text = "Codigo: ${it.codtipoinscripcion.toString()}",
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(Alignment.CenterStart),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "$title",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            data.forEach {
                ItemInscripcion(ctacli = ctacli, data = it)
            }
        }
    }
}
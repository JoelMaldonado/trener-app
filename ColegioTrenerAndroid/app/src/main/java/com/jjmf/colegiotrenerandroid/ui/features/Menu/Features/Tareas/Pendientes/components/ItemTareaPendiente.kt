package com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Tareas.Pendientes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jjmf.colegiotrenerandroid.domain.model.EstadoCalPendienteDia
import com.jjmf.colegiotrenerandroid.ui.theme.ColorGreen
import com.jjmf.colegiotrenerandroid.ui.theme.ColorT1
import com.jjmf.colegiotrenerandroid.ui.theme.ColorYellow
import com.jjmf.colegiotrenerandroid.util.format

@Composable
fun ItemTareaPendiente(data: EstadoCalPendienteDia) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(data.estadoColor())
            .padding(vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = data.curso?.uppercase() ?: "Sin Definir",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.White
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
            text = "${data.estado}",
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Tarea: ")
                }
                append("${data.tarea}")
            },
            fontSize = 12.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Fecha asignación: ")
                }
                append("${data.fechaasignacion?.format()}")
            },
            fontSize = 12.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Fecha entrega: ")
                }
                append("${data.fechaentrega?.format()}")
            },
            fontSize = 12.sp
        )
    }
}

private fun EstadoCalPendienteDia.estadoColor(): Color {
    return when (estado?.trim()) {
        "Pendiente" -> ColorYellow
        "Revisado" -> ColorGreen
        "No hizo tarea", "No hizo la tarea" -> ColorT1
        else -> ColorT1
    }
}
package com.jjmf.colegiotrenerandroid.domain.model

import java.time.LocalDate

data class CorreoMasivo(
    val remitente: String,
    val asunto: String,
    val mensaje: String,
    val fecha: LocalDate,
    val hora: String,
    val adjuntoUrl: String?,
    val adjuntoNombre: String?,
    val adjuntoBase64: String?,
    val leido: Boolean = false
)

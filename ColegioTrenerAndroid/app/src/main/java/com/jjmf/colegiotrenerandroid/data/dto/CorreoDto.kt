package com.jjmf.colegiotrenerandroid.data.dto

import com.jjmf.colegiotrenerandroid.domain.model.CorreoMasivo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CorreoDto(
    val profesor: String?,
    val codusu: String?,
    val codgra: String?,
    val codremite: String?,
    val asunto: String?,
    val cuerpo: String?,
    val rutafile: String?,
    val fecenvio: String?,
    val horpro: String?,
    val urlpdf: String?,
    val rutapdf: String?
) {

    fun toDomain(): CorreoMasivo {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
        val fecha = runCatching {
            fecenvio?.trim()?.takeIf { it.isNotBlank() }?.let {
                LocalDate.parse(it, formatter)
            }
        }.getOrNull() ?: LocalDate.now()

        val mensaje = cuerpo
            ?.replace("\r\n", "\n")
            ?.trim()
            ?: ""

        val adjuntoRaw = urlpdf?.trim()
        val adjunto = adjuntoRaw
            ?.takeIf { it.isNotBlank() && !it.equals("null", ignoreCase = true) }

        val adjuntoBase64Raw = rutapdf?.trim()
        val adjuntoBase64 = adjuntoBase64Raw
            ?.takeIf { 
                it.isNotBlank() && 
                !it.equals("null", ignoreCase = true) &&
                !it.startsWith("ERROR:", ignoreCase = true)
            }

        val adjuntoNombre = adjuntoBase64?.let {
            val rutaNormalizada = rutafile
                ?.trim()
                ?.takeIf { ruta -> ruta.isNotBlank() && !ruta.equals("null", ignoreCase = true) }
                ?.replace("\\", "/")

            rutaNormalizada
                ?.substringAfterLast("fakepath/", "")
                ?.trim()
                ?.takeIf { it.isNotBlank() }
                ?: rutaNormalizada
                    ?.substringAfterLast("/")
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }
        } ?: "Documento_adjunto.pdf"

        return CorreoMasivo(
            remitente = profesor?.trim().takeUnless { it.isNullOrEmpty() }
                ?: codremite?.trim().takeUnless { it.isNullOrEmpty() }
                ?: codusu?.trim().orEmpty(),
            asunto = asunto?.trim().orEmpty(),
            mensaje = mensaje,
            fecha = fecha,
            hora = horpro?.trim().orEmpty(),
            adjuntoUrl = adjunto,
            adjuntoNombre = adjuntoNombre,
            adjuntoBase64 = adjuntoBase64,
            leido = false
        )
    }
}

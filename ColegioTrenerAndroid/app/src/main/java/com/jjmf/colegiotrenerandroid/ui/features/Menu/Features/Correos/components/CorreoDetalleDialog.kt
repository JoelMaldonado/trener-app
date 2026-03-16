package com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Correos.components

import android.content.Intent
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import android.net.Uri
import java.util.regex.Pattern
import com.jjmf.colegiotrenerandroid.domain.model.CorreoMasivo
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CorreoDetalleDialog(
    correo: CorreoMasivo,
    onDismiss: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val context = LocalContext.current
    val adjuntoBase64 = correo.adjuntoBase64
    val adjuntoNombre = correo.adjuntoNombre ?: "Documento_adjunto.pdf"

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA68E4E),
                    contentColor = Color.White
                )
            ) {
                Text("Cerrar", fontWeight = FontWeight.SemiBold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Fecha y Hora fijos arriba
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Fecha: ${correo.fecha.format(formatter)}",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Hora: ${correo.hora}",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Asunto fijo arriba
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "Asunto:", fontWeight = FontWeight.SemiBold)
                    Surface(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = correo.asunto,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // De fijo arriba
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "De:", fontWeight = FontWeight.SemiBold)
                    Surface(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = correo.remitente,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Mensaje scrolleable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "Mensaje:", fontWeight = FontWeight.SemiBold)
                    Surface(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        val mensajeTexto = correo.mensaje.ifBlank { "Sin mensaje" }
                        val annotatedString = remember(mensajeTexto) {
                            buildAnnotatedStringWithLinks(mensajeTexto)
                        }
                        
                        ClickableText(
                            text = annotatedString,
                            modifier = Modifier.padding(12.dp),
                            onClick = { offset ->
                                annotatedString.getStringAnnotations(
                                    tag = "URL",
                                    start = offset,
                                    end = offset
                                ).firstOrNull()?.let { annotation ->
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "No se pudo abrir el enlace", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        )
                    }
                }

                // Adjunto fijo abajo (si existe)
                if (!adjuntoBase64.isNullOrBlank() && 
                    !adjuntoBase64.startsWith("ERROR:", ignoreCase = true) &&
                    isValidBase64(adjuntoBase64)) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "Adjunto:", fontWeight = FontWeight.SemiBold)
                        Surface(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = adjuntoNombre.ifBlank { "Documento_adjunto.pdf" },
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    val resultado = runCatching {
                                        val bytes = Base64.decode(adjuntoBase64, Base64.DEFAULT)
                                        val nombreParaMostrar = adjuntoNombre.ifBlank { "Documento_adjunto.pdf" }
                                        val nombreSeguro = nombreParaMostrar.replace("[^A-Za-z0-9._-]".toRegex(), "_")
                                        val archivo = File(context.cacheDir, nombreSeguro.ifBlank { "adjunto_correo.pdf" })
                                        FileOutputStream(archivo).use { salida ->
                                            salida.write(bytes)
                                            salida.flush()
                                        }
                                        archivo.deleteOnExit()

                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.fileprovider",
                                            archivo
                                        )

                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            setDataAndType(uri, "application/pdf")
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }
                                        context.startActivity(intent)
                                    }

                                    if (resultado.isFailure) {
                                        Toast.makeText(context, "No se pudo abrir el adjunto", Toast.LENGTH_SHORT).show()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Visibility,
                                        contentDescription = null,
                                        tint = Color(0xFFA68E4E)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        title = {
            Text(
                text = "Detalle del correo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    )
}

private fun buildAnnotatedStringWithLinks(text: String): androidx.compose.ui.text.AnnotatedString {
    val urlPattern = Pattern.compile(
        "(?:(?:https?|ftp)://|www\\.)[\\w\\-]+(?:\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:/~+#]*[\\w\\-@?^=%&/~+#])?",
        Pattern.CASE_INSENSITIVE
    )
    
    val matcher = urlPattern.matcher(text)
    val annotatedString = buildAnnotatedString {
        var lastIndex = 0
        var foundAny = false
        
        while (matcher.find()) {
            foundAny = true
            val start = matcher.start()
            val end = matcher.end()
            val url = matcher.group()
            
            // Agregar texto antes del link
            if (start > lastIndex) {
                append(text.substring(lastIndex, start))
            }
            
            // Agregar el link con estilo
            val fullUrl = if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("ftp://")) {
                "https://$url"
            } else {
                url
            }
            
            pushStringAnnotation(tag = "URL", annotation = fullUrl)
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF1976D2),
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(url)
            }
            pop()
            
            lastIndex = end
        }
        
        // Agregar el texto restante después del último link, o todo el texto si no hay links
        if (foundAny) {
            if (lastIndex < text.length) {
                append(text.substring(lastIndex))
            }
        } else {
            append(text)
        }
    }
    
    return annotatedString
}

private fun isValidBase64(base64String: String): Boolean {
    return try {
        Base64.decode(base64String, Base64.DEFAULT)
        true
    } catch (e: Exception) {
        false
    }
}

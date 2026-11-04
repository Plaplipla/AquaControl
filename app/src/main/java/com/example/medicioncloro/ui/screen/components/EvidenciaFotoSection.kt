package com.example.medicioncloro.ui.screen.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.medicioncloro.ui.theme.CorporateDarkBlue
import com.example.medicioncloro.ui.theme.CorporateTextDark
import com.example.medicioncloro.ui.viewmodel.EvidenciaFotoViewModel

@Composable
fun EvidenciaFotoSection(viewModel: EvidenciaFotoViewModel) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    val camara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bmp -> if (bmp != null) viewModel.fotoDesdeCamara(bmp) }

    val galeria = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) viewModel.fotoDesdeGaleria(uri) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Evidencia fotográfica (opcional)",
            fontWeight = FontWeight.Bold,
            color = CorporateTextDark
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { camara.launch(null) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                // ÍCONO OSCURO AQUÍ
                Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = CorporateDarkBlue)
                Spacer(Modifier.width(4.dp))
                // TEXTO OSCURO AQUÍ
                Text("Cámara", color = CorporateDarkBlue, fontWeight = FontWeight.Medium)
            }
            OutlinedButton(
                onClick = { galeria.launch("image/*") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                // ÍCONO OSCURO AQUÍ
                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = CorporateDarkBlue)
                Spacer(Modifier.width(4.dp))
                // TEXTO OSCURO AQUÍ
                Text("Galería", color = CorporateDarkBlue, fontWeight = FontWeight.Medium)
            }
        }

        AnimatedVisibility(
            visible = estado.bitmap != null || estado.uri != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box {
                when {
                    estado.bitmap != null -> Image(
                        bitmap = estado.bitmap!!.asImageBitmap(),
                        contentDescription = "Evidencia",
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    estado.uri != null -> AsyncImage(
                        model = estado.uri, contentDescription = "Evidencia",
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                IconButton(
                    onClick = { viewModel.limpiar() },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Quitar foto", tint = CorporateDarkBlue)
                }
            }
        }

        if (estado.origen.isNotBlank()) {
            Text("Imagen desde: ${estado.origen}",
                style = MaterialTheme.typography.bodySmall,
                color = CorporateDarkBlue)
        }
    }
}
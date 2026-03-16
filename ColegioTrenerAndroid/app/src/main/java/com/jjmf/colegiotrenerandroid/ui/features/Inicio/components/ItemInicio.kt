package com.jjmf.colegiotrenerandroid.ui.features.Inicio.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jjmf.colegiotrenerandroid.R

@Composable
fun ItemInicio(
    text: String,
    @DrawableRes ic: Int,
    isVertical: Boolean = true,
    click: () -> Unit
) {
    if (isVertical) {

        Column(
            modifier = Modifier
                .heightIn(min = 90.dp)
                .clickable {
                    click()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = ic),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    } else {
        Row(
            modifier = Modifier.clickable {
                click()
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Image(
                painter = painterResource(id = ic),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(text = text, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}
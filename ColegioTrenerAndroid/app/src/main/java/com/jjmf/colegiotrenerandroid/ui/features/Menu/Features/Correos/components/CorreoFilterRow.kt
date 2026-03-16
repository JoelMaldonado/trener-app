package com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Correos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.jjmf.colegiotrenerandroid.ui.theme.ColorP1
import androidx.compose.ui.text.input.TextFieldValue


@Composable
fun CorreoFilterRow(
    meses: List<String>,
    dias: List<String>,
    selectedMes: String,
    selectedDia: String,
    searchQuery: String,
    onMesSelected: (String) -> Unit,
    onDiaSelected: (String) -> Unit,
    onSearchChanged: (String) -> Unit,
    isSearchEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilterDropdown(
            modifier = Modifier.weight(1.2f),
            value = selectedMes,
            options = meses,
            onSelected = onMesSelected
        )

        FilterDropdown(
            modifier = Modifier.width(90.dp),
            value = selectedDia,
            options = dias,
            textAlign = TextAlign.Center,
            maxVisibleItems = 10,
            onSelected = onDiaSelected
        )

        SearchBox(
            modifier = Modifier.weight(1.2f),
            value = searchQuery,
            onValueChange = onSearchChanged,
            enabled = isSearchEnabled
        )
    }
}

@Composable
private fun FilterDropdown(
    modifier: Modifier,
    value: String,
    options: List<String>,
    textAlign: TextAlign = TextAlign.Start,
    maxVisibleItems: Int? = null,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var size by remember { mutableStateOf(Size.Zero) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable { expanded = true }
            .onGloballyPositioned { size = it.size.toSize() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                textAlign = textAlign,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = ColorP1
            )
        }
        val dropdownModifier = Modifier
            .width(with(density) { size.width.toDp() })
            .background(Color.White)
            .let { baseModifier ->
                maxVisibleItems?.let { maxItems ->
                    baseModifier.heightIn(max = 48.dp * maxItems)
                } ?: baseModifier
            }

        DropdownMenu(
            modifier = dropdownModifier,
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            textAlign = textAlign,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchBox(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                value = TextFieldValue(
                    text = value,
                    selection = TextRange(value.length)
                ),
                onValueChange = if (enabled) { tfv ->
                    onValueChange(tfv.text)
                } else {
                    {}
                },
                cursorBrush = SolidColor(if (enabled) ColorP1 else Color.Transparent),
                singleLine = true,
                readOnly = !enabled,
                textStyle = TextStyle(fontSize = 14.sp, color = if (enabled) Color.Black else Color(0xFFB2B2B2)),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Buscar remitente",
                            fontSize = 12.sp,
                            color = Color(0xFFB2B2B2)
                        )
                    }
                    innerTextField()
                }
            )
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (enabled) ColorP1 else Color(0xFFB2B2B2)
            )
        }
    }
}

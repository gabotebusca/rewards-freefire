package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.IntegrationInstructions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FlutterCodeSnippet
import com.example.ui.components.GamingCard
import com.example.ui.components.GamingGradientButton
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DiamondBlue
import com.example.ui.theme.EmberGold
import com.example.ui.theme.GamingBorder
import com.example.ui.theme.GamingDarkBg
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.GamingSurfaceVariant
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

@Composable
fun FlutterCodeScreen(
    snippets: List<FlutterCodeSnippet>
) {
    var selectedIndex by remember { mutableStateOf(0) }
    val currentSnippet = snippets.getOrElse(selectedIndex) { snippets.first() }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBg)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.IntegrationInstructions,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "CÓDIGO FLUTTER & CI/CD",
                        color = TextWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Estructura completa de entregables requerida",
                        color = TextGray,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(CyberCyan.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${snippets.size} ARCHIVOS",
                    color = CyberCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal Chip Selector for files
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(snippets) { index, snippet ->
                val isSelected = index == selectedIndex
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedIndex = index },
                    label = {
                        Text(
                            text = snippet.fileName,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberCyan,
                        selectedLabelColor = GamingDarkBg,
                        containerColor = GamingSurface,
                        labelColor = TextGray
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) CyberCyan else GamingBorder,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Info Card about the current file
        GamingCard(
            borderColor = GamingBorder,
            backgroundColor = GamingSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = currentSnippet.fileName,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(EmberGold.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = currentSnippet.category,
                                color = EmberGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = currentSnippet.description,
                        color = TextGray,
                        fontSize = 11.sp
                    )
                }

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(currentSnippet.code))
                        Toast.makeText(context, "Archivo copiado al portapapeles", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.testTag("copy_snippet_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copiar Código",
                        tint = CyberCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Code Viewer Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF06090F))
                .border(1.dp, GamingBorder, RoundedCornerShape(12.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                item {
                    Text(
                        text = currentSnippet.code,
                        color = Color(0xFFC9D1D9),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Copy entire file button
        GamingGradientButton(
            text = "COPIAR ${currentSnippet.fileName.uppercase()}",
            onClick = {
                clipboardManager.setText(AnnotatedString(currentSnippet.code))
                Toast.makeText(context, "¡${currentSnippet.fileName} copiado con éxito!", Toast.LENGTH_SHORT).show()
            },
            icon = Icons.Default.ContentCopy,
            modifier = Modifier.fillMaxWidth(),
            gradientColors = listOf(CyberCyan, DiamondBlue)
        )
    }
}

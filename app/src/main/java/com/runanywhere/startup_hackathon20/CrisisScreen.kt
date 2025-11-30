package com.runanywhere.startup_hackathon20

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.runanywhere.startup_hackathon20.data.CrisisReport
import java.text.SimpleDateFormat
import java.util.*

// --- HOLO-GLASS UI PALETTE ---
private val GlowRed = Color(0xFFFF3333)
private val GlowBlue = Color(0xFF2F81F7)
private val GlowTeal = Color(0xFF03DAC5)
private val DeepSpaceBlack = Color(0xFF050505)
private val SpaceDark = Color(0xFF0A0A0A)

// Glass Gradients
private val GlassGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFFFFF).copy(alpha = 0.15f),
        Color(0xFFFFFFFF).copy(alpha = 0.05f)
    ),
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)

private val BorderGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFFFFF).copy(alpha = 0.5f),
        Color(0xFFFFFFFF).copy(alpha = 0.1f)
    )
)

/**
 * Holographic Animated Background
 * Creates a slow-moving mesh gradient effect behind the glass UI
 */
@Composable
fun HolographicBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpaceBlack)
    ) {
        // Red Orb (Static)
        Box(
            modifier = Modifier
                .offset(x = (-100).dp, y = (-100).dp)
                .size(400.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(GlowRed.copy(alpha = 0.2f), Color.Transparent)
                    )
                )
        )
        // Blue Orb (Animated)
        Box(
            modifier = Modifier
                .offset(x = 200.dp, y = 400.dp)
                .offset(y = (offset * 0.1f).dp)
                .size(300.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.Blue.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )
        // Teal Orb (Slower animation)
        Box(
            modifier = Modifier
                .offset(x = 100.dp, y = 600.dp)
                .offset(y = (offset * 0.05f).dp)
                .size(250.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(GlowTeal.copy(alpha = 0.1f), Color.Transparent)
                    )
                )
        )
    }
}

/**
 * GridZero Holo-Glass Main Screen
 */
@Composable
fun CrisisScreen(viewModel: CrisisViewModel = viewModel()) {
    val reports by viewModel.reports.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentModelId by viewModel.currentModelId.collectAsState()
    val availableModels by viewModel.availableModels.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val totalBytesSaved by viewModel.totalBytesSaved.collectAsState()

    var showModelSelector by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    // Root Container with Z-Index management
    Box(modifier = Modifier.fillMaxSize()) {
        // Layer 1: Animated Holographic Background
        HolographicBackground()

        // Layer 2: Foreground Glass Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            // --- 3D Floating Header ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // Pulsing Status Orb
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.8f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .scale(scale)
                        .background(GlowRed, shape = RoundedCornerShape(50))
                        .graphicsLayer { shadowElevation = 20f }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "GRID",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp
                )
                Text(
                    text = "ZERO",
                    color = GlowRed,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp,
                    style = LocalTextStyle.current.copy(
                        shadow = Shadow(
                            color = GlowRed,
                            blurRadius = 20f
                        )
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                // Model Status Icon (Settings)
                IconButton(
                    onClick = { showModelSelector = !showModelSelector },
                    modifier = Modifier.graphicsLayer { shadowElevation = 10f }
                ) {
                    Icon(
                        if (currentModelId != null) Icons.Default.CheckCircle else Icons.Default.Settings,
                        "Model Status",
                        tint = if (currentModelId != null) GlowTeal else Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Status Bar (Glass Panel)
            GlassPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = statusMessage,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    if (totalBytesSaved > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "📡 ${formatBytes(totalBytesSaved)} BANDWIDTH SAVED",
                            color = GlowTeal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    downloadProgress?.let { progress ->
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp),
                            color = GlowTeal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error Display
            error?.let { errorMsg ->
                GlassPanel(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚠️ $errorMsg",
                            color = GlowRed,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.clearError() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, "Dismiss", tint = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Model Selector (Expanded)
            if (showModelSelector) {
                GlassPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TACTICAL AI SYSTEMS",
                                color = GlowRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            IconButton(onClick = { viewModel.refreshModels() }) {
                                Icon(
                                    Icons.Default.Refresh,
                                    "Refresh",
                                    tint = Color.White.copy(alpha = 0.5f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (availableModels.isEmpty()) {
                            Text(
                                text = "No models available. Initializing...",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        } else {
                            availableModels.forEach { model ->
                                HoloModelItem(
                                    model = model,
                                    isLoaded = model.id == currentModelId,
                                    onDownload = { viewModel.downloadModel(model.id) },
                                    onLoad = { viewModel.loadModel(model.id) }
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- Glass Input Console ---
            GlassPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "COMMAND LINE",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Input data stream...", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GlowRed.copy(alpha = 0.5f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                            cursorColor = GlowRed,
                            focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.3f)
                        ),
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        enabled = currentModelId != null && !isAnalyzing
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3D Pressable Button
                    Button(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.analyzeFieldReport(inputText)
                                inputText = ""
                            }
                        },
                        enabled = currentModelId != null && !isAnalyzing && inputText.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .graphicsLayer { shadowElevation = 10f },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GlowRed.copy(alpha = 0.8f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "INITIATE ANALYSIS",
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    if (currentModelId == null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚠️ Load AI model first (tap settings icon)",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Live Feed Header ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVE STREAMS [${reports.size}]",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )

                if (reports.isNotEmpty()) {
                    IconButton(onClick = { viewModel.clearReports() }) {
                        Icon(
                            Icons.Default.Delete,
                            "Clear",
                            tint = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Live Feed (Scrollable) ---
            if (reports.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.White.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "NO ACTIVE INCIDENTS",
                            color = Color.White.copy(alpha = 0.3f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Awaiting field reports...",
                            color = Color.White.copy(alpha = 0.2f),
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(reports.reversed()) { report ->
                        GlassIncidentCard(report)
                    }
                }
            }
        }
    }
}

/**
 * Reusable Glass Panel Component
 */
@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(GlassGradient)
            .border(1.dp, BorderGradient, RoundedCornerShape(24.dp))
    ) {
        content()
    }
}

/**
 * Holographic Incident Card with 3D Press Effect
 */
@Composable
fun GlassIncidentCard(report: CrisisReport) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = "scale"
    )

    val statusColor = when (report.severity.lowercase()) {
        "critical" -> GlowRed
        "moderate" -> Color(0xFFF7B93E)
        else -> GlowTeal
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null) {}
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        statusColor.copy(alpha = 0.1f),
                        Color(0xFF1E1E1E).copy(alpha = 0.4f)
                    )
                )
            )
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(
                        statusColor.copy(alpha = 0.5f),
                        Color.Transparent
                    )
                ),
                RoundedCornerShape(24.dp)
            )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Glowing Badge
                Surface(
                    color = statusColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = report.incidentType.uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Text(
                    text = formatTimestamp(report.timestamp),
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = report.locationName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        blurRadius = 4f,
                        offset = Offset(2f, 2f)
                    )
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Metadata Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "👤 ${report.casualties} casualties",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Text(
                    text = "⚡ ${report.sentiment}",
                    color = statusColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Resource Grid
            if (report.resourcesNeeded.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    report.resourcesNeeded.take(3).forEach { res ->
                        Box(
                            modifier = Modifier
                                .background(
                                    Color.Black.copy(alpha = 0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.1f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = res,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Holographic Model Item
 */
@Composable
fun HoloModelItem(
    model: com.runanywhere.sdk.models.ModelInfo,
    isLoaded: Boolean,
    onDownload: () -> Unit,
    onLoad: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isLoaded)
                    Brush.linearGradient(
                        colors = listOf(
                            GlowTeal.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                else
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
            )
            .border(
                1.dp,
                if (isLoaded) GlowTeal.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(12.dp)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = model.name,
                color = if (isLoaded) GlowTeal else Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            if (isLoaded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✓ OPERATIONAL",
                    color = GlowTeal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDownload,
                        modifier = Modifier.weight(1f),
                        enabled = !model.isDownloaded,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GlowBlue.copy(alpha = 0.6f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            if (model.isDownloaded) "✓ READY" else "DOWNLOAD",
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = onLoad,
                        modifier = Modifier.weight(1f),
                        enabled = model.isDownloaded,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GlowRed.copy(alpha = 0.6f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("LOAD", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// --- UTILITY FUNCTIONS ---

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatBytes(bytes: Long): String {
    return when {
        bytes < 1024 -> "${bytes}B"
        bytes < 1024 * 1024 -> "${bytes / 1024}KB"
        else -> String.format("%.1fMB", bytes / (1024.0 * 1024.0))
    }
}

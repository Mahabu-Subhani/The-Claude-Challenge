package com.runanywhere.startup_hackathon20

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.runanywhere.startup_hackathon20.data.CrisisReport
import java.text.SimpleDateFormat
import java.util.*

// --- CYBER-TACTICAL THEME DEFINITION ---
val DarkBg = Color(0xFF050505)
val CardBg = Color(0xFF121212)
val CriticalRed = Color(0xFFFF3333)
val WarningYellow = Color(0xFFFFD54F)
val SafeTeal = Color(0xFF03DAC5)
val TextSecondary = Color(0xFF888888)

/**
 * GridZero Tactical Dashboard - CYBER-TACTICAL DESIGN
 *
 * Aesthetic: Military-Grade / High Contrast / Information Density
 * Philosophy: Function over form. Dark mode only. Critical data first.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrisisScreen(viewModel: CrisisViewModel = viewModel()) {
    val reports by viewModel.reports.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentModelId by viewModel.currentModelId.collectAsState()
    val availableModels by viewModel.availableModels.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val totalBytesSaved by viewModel.totalBytesSaved.collectAsState()

    var showModelSelector by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
    ) {
        // --- 1. HEADER SECTION ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Blinking Status Light Simulation
            val infiniteTransition = rememberInfiniteTransition(label = "blink")
            val alpha by infiniteTransition.animateFloat(
                initialValue = if (isAnalyzing) 0.3f else 0.5f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "statusBlink"
            )

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        CriticalRed.copy(alpha = if (isAnalyzing) alpha else 0.5f),
                        shape = RoundedCornerShape(50)
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "GRID",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Text(
                text = "ZERO",
                color = CriticalRed,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Model Status & Settings
            IconButton(onClick = { showModelSelector = !showModelSelector }) {
                Icon(
                    if (currentModelId != null) Icons.Default.CheckCircle else Icons.Default.Warning,
                    "Model Status",
                    tint = if (currentModelId != null) SafeTeal else WarningYellow
                )
            }
        }

        Text(
            text = "OFFLINE SITUATION COMMAND // V1.0",
            color = TextSecondary,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bandwidth Savings Display
        if (totalBytesSaved > 0) {
            Text(
                text = "BANDWIDTH SAVED: ${formatBytes(totalBytesSaved)}",
                color = SafeTeal,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Model Selector Overlay
        AnimatedVisibility(visible = showModelSelector) {
            ModelSelector(
                models = availableModels,
                currentModelId = currentModelId,
                onDownload = { modelId -> viewModel.downloadModel(modelId) },
                onLoad = { modelId -> viewModel.loadModel(modelId) },
                onRefresh = { viewModel.refreshModels() },
                onDismiss = { showModelSelector = false },
                downloadProgress = downloadProgress
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- 2. INPUT SECTION (Tactical Radio) ---
        Card(
            colors = CardDefaults.cardColors(containerColor = CardBg),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "INCOMING TRANSMISSION",
                        color = CriticalRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    if (isAnalyzing) {
                        Text(
                            text = "PROCESSING...",
                            color = WarningYellow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "Enter field report (e.g. 'Collapse at library, 3 trapped, need crane...')",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = CriticalRed,
                        unfocusedBorderColor = TextSecondary,
                        cursorColor = CriticalRed,
                        focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.3f),
                        disabledContainerColor = Color.Black.copy(alpha = 0.1f)
                    ),
                    enabled = currentModelId != null && !isAnalyzing,
                    maxLines = 3,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Button
                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.analyzeFieldReport(inputText)
                            inputText = ""
                        }
                    },
                    enabled = !isAnalyzing && inputText.isNotBlank() && currentModelId != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CriticalRed,
                        disabledContainerColor = CriticalRed.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ANALYZING",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "PROCESS INTEL",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }

                // Error Display
                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ERROR: $error",
                        color = CriticalRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. LIVE FEED SECTION ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TACTICAL FEED [${reports.size}]",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )

            if (reports.isNotEmpty()) {
                TextButton(onClick = { viewModel.clearReports() }) {
                    Text(
                        text = "CLEAR",
                        color = CriticalRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Feed Content
        if (reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "NO ACTIVE INCIDENTS",
                        color = TextSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Awaiting field reports...",
                        color = TextSecondary.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(reports.reversed()) { report ->
                    IncidentCard(report)
                }
            }
        }
    }
}

@Composable
fun StatusBar(
    statusMessage: String,
    totalBytesSaved: Long,
    downloadProgress: Float?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF1A1A1A),
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status message
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF00FF00),
                    modifier = Modifier.weight(1f)
                )

                // Bandwidth savings
                if (totalBytesSaved > 0) {
                    Text(
                        text = "📡 ${formatBytes(totalBytesSaved)} saved",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Download progress bar
            downloadProgress?.let { progress ->
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    color = Color(0xFF00FF00),
                    trackColor = Color(0xFF2A2A2A)
                )
            }
        }
    }
}

@Composable
fun ErrorBanner(errorMessage: String, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFB00020)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚠️ $errorMessage",
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, "Dismiss", tint = Color.White)
            }
        }
    }
}

@Composable
fun DemoInstructions() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A3A1A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "🎯 DEMO MODE",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FF00),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Try these field reports:",
                color = Color(0xFFCCCCCC),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            val examples = listOf(
                "Unit 4, structural collapse at library, severe flooding, three civilians trapped, need heavy lift gear immediately!",
                "This is medic team 2, multiple casualties at North Hospital, approximately 15 injured, need ambulances and medical supplies ASAP",
                "Fire at downtown sector, building fully engulfed, spreading to adjacent structures, need all available fire teams",
                "Civil unrest at city hall, large crowd, situation escalating, need police backup and crowd control"
            )

            examples.forEach { example ->
                Text(
                    "• ${example.take(80)}...",
                    color = Color(0xFF888888),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TacticalFeed(
    reports: List<CrisisReport>,
    isAnalyzing: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF0A0A0A)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "ACTIVE INCIDENTS",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF00),
                    fontSize = 18.sp
                )

                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF00FF00),
                        strokeWidth = 2.dp
                    )
                }
            }

            // Incident list
            if (reports.isEmpty() && !isAnalyzing) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Place,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "NO ACTIVE INCIDENTS",
                            color = Color(0xFF666666),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Awaiting field reports...",
                            color = Color(0xFF444444),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reports.reversed()) { report ->
                        IncidentCard(report)
                    }
                }
            }
        }
    }
}

/**
 * Cyber-Tactical Incident Card
 * Design: High contrast, color-coded severity strip, badge system
 */
@Composable
fun IncidentCard(report: CrisisReport) {
    // Determine status color based on severity
    val statusColor = when (report.severity.lowercase()) {
        "critical" -> CriticalRed
        "moderate" -> WarningYellow
        else -> SafeTeal
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Left Status Strip - Visual severity indicator
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(statusColor)
            )

            Column(modifier = Modifier.padding(12.dp)) {
                // Top Row: Type and Timestamp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = report.incidentType.uppercase(),
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = formatTimestamp(report.timestamp),
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Main Location Header
                Text(
                    text = report.locationName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Badges Row - Casualties and Sentiment
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Casualties Badge
                    TacticalBadge(
                        text = "CASUALTIES: ${report.casualties}",
                        bgColor = Color(0xFF2C2C2C),
                        textColor = Color.White
                    )

                    // Sentiment Badge
                    TacticalBadge(
                        text = report.sentiment.uppercase(),
                        bgColor = Color(0xFF2C2C2C),
                        textColor = Color.White
                    )
                }

                // Resource Requirements (if any)
                if (report.resourcesNeeded.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "REQUIRED ASSETS:",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        report.resourcesNeeded.take(3).forEach { resource ->
                            TacticalBadge(
                                text = resource.uppercase(),
                                bgColor = statusColor.copy(alpha = 0.15f),
                                textColor = statusColor
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tactical Badge Component
 * Compact, high-contrast information chip
 */
@Composable
fun TacticalBadge(text: String, bgColor: Color, textColor: Color) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(2.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun RadioInput(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isEnabled: Boolean,
    isAnalyzing: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFF1A1A1A),
        tonalElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Input field
            TextField(
                value = inputText,
                onValueChange = onInputChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = {
                    Text(
                        "📻 FIELD REPORT INPUT\n\nSpeak or type chaotic situation report...\nAI will extract structured tactical data.",
                        color = Color(0xFF666666)
                    )
                },
                enabled = isEnabled && !isAnalyzing,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A2A2A),
                    unfocusedContainerColor = Color(0xFF2A2A2A),
                    disabledContainerColor = Color(0xFF1A1A1A),
                    focusedTextColor = Color(0xFF00FF00),
                    unfocusedTextColor = Color(0xFF00FF00),
                    cursorColor = Color(0xFF00FF00)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Submit button
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isEnabled && inputText.isNotBlank() && !isAnalyzing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FF00),
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0xFF333333),
                    disabledContentColor = Color(0xFF666666)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("ANALYZING...", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                } else {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("TRANSMIT REPORT", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            // Status hint
            if (!isEnabled) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "⚠️ Load a tactical AI model to begin operations",
                    color = Color(0xFFFF9800),
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Tactical Model Selector
 * Compact, mission-critical AI model management
 */
@Composable
fun ModelSelector(
    models: List<com.runanywhere.sdk.models.ModelInfo>,
    currentModelId: String?,
    onDownload: (String) -> Unit,
    onLoad: (String) -> Unit,
    onRefresh: () -> Unit,
    onDismiss: () -> Unit,
    downloadProgress: Float?
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TACTICAL AI SYSTEMS",
                    color = CriticalRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                Row {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, "Refresh", tint = SafeTeal, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, "Close", tint = CriticalRed, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Download Progress Bar
            downloadProgress?.let { progress ->
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = CriticalRed,
                    trackColor = Color(0xFF2C2C2C)
                )
                Text(
                    text = "DOWNLOADING: ${(progress * 100).toInt()}%",
                    color = WarningYellow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (models.isEmpty()) {
                Text(
                    text = "NO MODELS AVAILABLE // CHECK INITIALIZATION",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                models.forEach { model ->
                    TacticalModelItem(
                        model = model,
                        isLoaded = model.id == currentModelId,
                        onDownload = { onDownload(model.id) },
                        onLoad = { onLoad(model.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Tactical Model Item
 * Individual AI model card with download/load controls
 */
@Composable
fun TacticalModelItem(
    model: com.runanywhere.sdk.models.ModelInfo,
    isLoaded: Boolean,
    onDownload: () -> Unit,
    onLoad: () -> Unit
) {
    Surface(
        color = if (isLoaded) SafeTeal.copy(alpha = 0.1f) else Color(0xFF1A1A1A),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Status strip
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(if (isLoaded) SafeTeal else TextSecondary.copy(alpha = 0.3f))
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = model.name,
                    color = if (isLoaded) SafeTeal else Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                if (isLoaded) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "OPERATIONAL",
                        color = SafeTeal,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onDownload,
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            enabled = !model.isDownloaded,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CriticalRed,
                                disabledContainerColor = TextSecondary.copy(alpha = 0.3f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                if (model.isDownloaded) "READY" else "DOWNLOAD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Button(
                            onClick = onLoad,
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            enabled = model.isDownloaded,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SafeTeal,
                                disabledContainerColor = TextSecondary.copy(alpha = 0.3f),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                "LOAD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// Utility functions
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

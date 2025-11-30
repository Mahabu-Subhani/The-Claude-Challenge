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

/**
 * GridZero Tactical Dashboard
 *
 * This is NOT a chat screen. This is a TACTICAL OPERATIONS CENTER.
 * - Top: Live incident feed with color-coded severity
 * - Bottom: Radio input for field reports
 * - Real-time data extraction from chaotic voice/text reports
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    var showDemo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("GRIDZERO", fontWeight = FontWeight.Bold)
                        Text(
                            "Offline Tactical Intelligence",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    // Demo button
                    IconButton(onClick = { showDemo = !showDemo }) {
                        Icon(Icons.Default.Info, "Demo")
                    }

                    // Clear reports
                    if (reports.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearReports() }) {
                            Icon(Icons.Default.Delete, "Clear")
                        }
                    }

                    // Model selector
                    IconButton(onClick = { showModelSelector = !showModelSelector }) {
                        Icon(
                            if (currentModelId != null) Icons.Default.CheckCircle else Icons.Default.Warning,
                            "Model Status",
                            tint = if (currentModelId != null) Color(0xFF4CAF50) else Color(
                                0xFFFF9800
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A),
                    titleContentColor = Color(0xFF00FF00),
                    actionIconContentColor = Color(0xFF00FF00)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0A0A0A))
        ) {
            // Status Bar
            StatusBar(
                statusMessage = statusMessage,
                totalBytesSaved = totalBytesSaved,
                downloadProgress = downloadProgress
            )

            // Error Banner
            error?.let { errorMessage ->
                ErrorBanner(errorMessage, onDismiss = { viewModel.clearError() })
            }

            // Demo Instructions
            AnimatedVisibility(visible = showDemo) {
                DemoInstructions()
            }

            // Model Selector
            AnimatedVisibility(visible = showModelSelector) {
                ModelSelector(
                    models = availableModels,
                    currentModelId = currentModelId,
                    onDownload = { modelId -> viewModel.downloadModel(modelId) },
                    onLoad = { modelId -> viewModel.loadModel(modelId) },
                    onRefresh = { viewModel.refreshModels() },
                    onDismiss = { showModelSelector = false }
                )
            }

            // Tactical Feed (Top Half)
            TacticalFeed(
                reports = reports,
                isAnalyzing = isAnalyzing,
                modifier = Modifier.weight(1f)
            )

            // Radio Input (Bottom)
            RadioInput(
                inputText = inputText,
                onInputChange = { inputText = it },
                onSubmit = {
                    if (inputText.isNotBlank()) {
                        viewModel.analyzeFieldReport(inputText)
                        inputText = ""
                    }
                },
                isEnabled = currentModelId != null && !isAnalyzing,
                isAnalyzing = isAnalyzing,
                modifier = Modifier.fillMaxWidth()
            )
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

@Composable
fun IncidentCard(report: CrisisReport) {
    // Color coding by severity
    val cardColor = when (report.severity.lowercase()) {
        "critical" -> Color(0xFFB00020)  // RED - Critical
        "moderate" -> Color(0xFFFFCC00)  // YELLOW - Moderate
        else -> Color(0xFF03DAC5)        // TEAL - Low
    }

    val textColor = when (report.severity.lowercase()) {
        "critical" -> Color.White
        "moderate" -> Color.Black
        else -> Color.Black
    }

    // Pulsing animation for critical incidents
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = if (report.severity.lowercase() == "critical") 0.7f else 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (report.severity.lowercase() == "critical") alpha else 1f),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = report.locationName,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        fontSize = 16.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = textColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = report.incidentType.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Casualties
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${report.casualties} casualties",
                        color = textColor,
                        fontSize = 14.sp
                    )
                }

                // Sentiment indicator
                val sentimentIcon = when (report.sentiment.lowercase()) {
                    "panic" -> "🚨"
                    "urgent" -> "⚡"
                    else -> "✓"
                }
                Text(
                    text = "$sentimentIcon ${report.sentiment}",
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Resources needed
            if (report.resourcesNeeded.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "RESOURCES NEEDED:",
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    report.resourcesNeeded.forEach { resource ->
                        Surface(
                            shape = CircleShape,
                            color = textColor.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = resource,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = textColor,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Timestamp
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatTimestamp(report.timestamp),
                color = textColor.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
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

@Composable
fun ModelSelector(
    models: List<com.runanywhere.sdk.models.ModelInfo>,
    currentModelId: String?,
    onDownload: (String) -> Unit,
    onLoad: (String) -> Unit,
    onRefresh: () -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF1A1A1A)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TACTICAL AI MODELS",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF00FF00),
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, "Refresh", tint = Color(0xFF00FF00))
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, "Close", tint = Color(0xFF00FF00))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (models.isEmpty()) {
                Text(
                    text = "No models available. Check initialization...",
                    color = Color(0xFF888888)
                )
            } else {
                models.forEach { model ->
                    ModelItem(
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

@Composable
fun ModelItem(
    model: com.runanywhere.sdk.models.ModelInfo,
    isLoaded: Boolean,
    onDownload: () -> Unit,
    onLoad: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isLoaded) Color(0xFF1A3A1A) else Color(0xFF2A2A2A)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = model.name,
                style = MaterialTheme.typography.titleSmall,
                color = if (isLoaded) Color(0xFF00FF00) else Color(0xFFCCCCCC),
                fontWeight = FontWeight.Bold
            )

            if (isLoaded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✓ LOADED & OPERATIONAL",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDownload,
                        modifier = Modifier.weight(1f),
                        enabled = !model.isDownloaded,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00FF00),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(if (model.isDownloaded) "✓ Downloaded" else "Download")
                    }

                    Button(
                        onClick = onLoad,
                        modifier = Modifier.weight(1f),
                        enabled = model.isDownloaded,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00FF00),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Load")
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

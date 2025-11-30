package com.runanywhere.startup_hackathon20

import android.service.autofill.Validators.or
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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

// --- REFINED TACTICAL UI PALETTE ---
// This palette is inspired by modern IDEs like VS Code and GitHub's dark theme.
// It's professional, easy on the eyes, and still feels 'technical' or 'tactical'.

private val TacBlack = Color(0xFF0D1117) // Background - deep, dark, slightly blue
private val TacDark = Color(0xFF161B22)   // Top bars, input areas
private val TacSurface = Color(0xFF21262D) // Cards and elevated surfaces
private val TacPrimaryText = Color(0xFFC9D1D9) // Main text - soft white
private val TacSecondaryText = Color(0xFF8B949E) // Secondary text, icons, hints
private val TacGreen = Color(0xFF238636)       // A more subdued, professional green
private val TacGreenBright = Color(0xFF30A14E) // For highlights, buttons, and success states
private val TacRed = Color(0xFFDA3633)         // For critical errors and alerts
private val TacOrange = Color(0xFFF7B93E)      // For warnings and moderate severity
private val TacBlue = Color(0xFF2F81F7)        // For informational and low severity


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
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("GRIDZERO", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(
                            "OFFLINE TACTICAL INTELLIGENCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = TacSecondaryText,
                            letterSpacing = 1.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDemo = !showDemo }) {
                        Icon(Icons.Default.Info, "Demo", tint = TacSecondaryText)
                    }
                    if (reports.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearReports() }) {
                            Icon(Icons.Default.Delete, "Clear", tint = TacSecondaryText)
                        }
                    }
                    IconButton(onClick = { showModelSelector = !showModelSelector }) {
                        Icon(
                            if (currentModelId != null) Icons.Default.CheckCircle else Icons.Default.Warning,
                            "Model Status",
                            tint = if (currentModelId != null) TacGreenBright else TacOrange
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TacDark,
                    titleContentColor = TacPrimaryText,
                    actionIconContentColor = TacSecondaryText
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(TacBlack)
        ) {
            StatusBar(
                statusMessage = statusMessage,
                totalBytesSaved = totalBytesSaved,
                downloadProgress = downloadProgress
            )

            error?.let { errorMessage ->
                ErrorBanner(errorMessage, onDismiss = { viewModel.clearError() })
            }

            AnimatedVisibility(visible = showDemo) {
                DemoInstructions()
            }

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
            }

            TacticalFeed(
                reports = reports,
                isAnalyzing = isAnalyzing,
                modifier = Modifier.weight(1f)
            )

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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(TacDark)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = TacSecondaryText,
                modifier = Modifier.weight(1f)
            )
            if (totalBytesSaved > 0) {
                Text(
                    text = "📡 ${formatBytes(totalBytesSaved)} SAVED",
                    style = MaterialTheme.typography.bodySmall,
                    color = TacGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        downloadProgress?.let { progress ->
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = TacGreenBright,
                trackColor = TacSurface
            )
        }
    }
}

@Composable
fun ErrorBanner(errorMessage: String, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = TacRed
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚠️ $errorMessage",
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
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
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = TacSurface),
        border = BorderStroke(1.dp, TacGreen.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "🎯 DEMO MODE",
                fontWeight = FontWeight.Bold,
                color = TacGreenBright,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Try these field reports:",
                color = TacPrimaryText,
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
                    "• $example",
                    color = TacSecondaryText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 4.dp),
                    lineHeight = 16.sp
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
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "ACTIVE INCIDENTS",
                fontWeight = FontWeight.Bold,
                color = TacPrimaryText,
                fontSize = 18.sp
            )
            if (isAnalyzing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = TacBlue,
                    strokeWidth = 2.dp
                )
            }
        }
        if (reports.isEmpty() && !isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Storage,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TacSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "NO ACTIVE INCIDENTS",
                        color = TacSecondaryText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Awaiting field reports...",
                        color = TacSecondaryText.copy(alpha = 0.7f),
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

@Composable
fun IncidentCard(report: CrisisReport) {
    val severityColor = when (report.severity.lowercase()) {
        "critical" -> TacRed
        "moderate" -> TacOrange
        else -> TacBlue
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val animatedBorderColor by infiniteTransition.animateColor(
        initialValue = if (report.severity.lowercase() == "critical") severityColor else Color.Transparent,
        targetValue = if (report.severity.lowercase() == "critical") severityColor.copy(alpha = 0.5f) else Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "borderColor"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TacSurface),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(2.dp, if (report.severity.lowercase() == "critical") animatedBorderColor else severityColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        tint = severityColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = report.locationName,
                        fontWeight = FontWeight.Bold,
                        color = TacPrimaryText,
                        fontSize = 18.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = severityColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = report.incidentType.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = severityColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = TacDark, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Casualties",
                        tint = TacSecondaryText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${report.casualties} casualties",
                        color = TacPrimaryText,
                        fontSize = 14.sp
                    )
                }

                val sentimentIcon = when (report.sentiment.lowercase()) {
                    "panic" -> "🚨"
                    "urgent" -> "⚡"
                    else -> "✓"
                }
                Text(
                    text = "$sentimentIcon ${report.sentiment}",
                    color = TacPrimaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            if (report.resourcesNeeded.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "RESOURCES NEEDED:",
                    color = TacSecondaryText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    report.resourcesNeeded.forEach { resource ->
                        Surface(
                            shape = CircleShape,
                            color = TacBlue.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = resource,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = TacBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = formatTimestamp(report.timestamp),
                color = TacSecondaryText.copy(alpha = 0.7f),
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.End)
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
        modifier = modifier.fillMaxWidth(),
        color = TacDark,
        tonalElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = inputText,
                onValueChange = onInputChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = {
                    Text(
                        "📻 FIELD REPORT INPUT

Speak or type chaotic situation report...
AI will extract structured tactical data.",
                        color = TacSecondaryText.copy(alpha = 0.7f),
                        lineHeight = 18.sp
                    )
                },
                enabled = isEnabled && !isAnalyzing,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = TacSurface,
                    unfocusedContainerColor = TacSurface,
                    disabledContainerColor = TacDark,
                    focusedTextColor = TacPrimaryText,
                    unfocusedTextColor = TacPrimaryText,
                    cursorColor = TacGreenBright,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isEnabled && inputText.isNotBlank() && !isAnalyzing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TacGreenBright,
                    contentColor = Color.White,
                    disabledContainerColor = TacSurface,
                    disabledContentColor = TacSecondaryText
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = TacPrimaryText,
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

            if (!isEnabled) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "⚠️ Load a tactical AI model to begin operations",
                    color = TacOrange,
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
    onDismiss: () -> Unit,
    downloadProgress: Float?
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = TacDark,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 8.dp
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
                    color = TacPrimaryText,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, "Refresh", tint = TacSecondaryText)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, "Close", tint = TacSecondaryText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (models.isEmpty()) {
                Text(
                    text = "No models available. Check initialization...",
                    color = TacSecondaryText
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(models) { model ->
                        CrisisModelItem(
                            model = model,
                            isLoaded = model.id == currentModelId,
                            onDownload = { onDownload(model.id) },
                            onLoad = { onLoad(model.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CrisisModelItem(
    model: com.runanywhere.sdk.models.ModelInfo,
    isLoaded: Boolean,
    onDownload: () -> Unit,
    onLoad: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLoaded) TacGreen.copy(alpha = 0.1f) else TacSurface
        ),
        border = if(isLoaded) BorderStroke(1.dp, TacGreenBright) else BorderStroke(1.dp, TacDark)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = model.name,
                style = MaterialTheme.typography.titleSmall,
                color = if (isLoaded) TacGreenBright else TacPrimaryText,
                fontWeight = FontWeight.Bold
            )

            if (isLoaded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✓ LOADED & OPERATIONAL",
                    style = MaterialTheme.typography.bodySmall,
                    color = TacGreen,
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
                            containerColor = TacBlue,
                            contentColor = Color.White,
                            disabledContainerColor = TacSurface,
                            disabledContentColor = TacSecondaryText
                        )
                    ) {
                        Text(if (model.isDownloaded) "✓ DOWNLOADED" else "DOWNLOAD")
                    }

                    Button(
                        onClick = onLoad,
                        modifier = Modifier.weight(1f),
                        enabled = model.isDownloaded,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TacGreenBright,
                            contentColor = Color.White,
                            disabledContainerColor = TacSurface,
                            disabledContentColor = TacSecondaryText
                        )
                    ) {
                        Text("LOAD")
                    }
                }
            }
        }
    }
}

// --- UTILITY FUNCTIONS ---

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss z", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatBytes(bytes: Long): String {
    return when {
        bytes < 1024 -> "${bytes}B"
        bytes < 1024 * 1024 -> "${bytes / 1024}KB"
        else -> String.format("%.1fMB", bytes / (1024.0 * 1024.0))
    }
}

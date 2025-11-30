package com.runanywhere.startup_hackathon20

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.runanywhere.startup_hackathon20.data.CrisisReport
import java.text.SimpleDateFormat
import java.util.*

// --- MAGMA GLASS DESIGN SYSTEM ---
// Strict High-Contrast Palette for Maximum Readability

private val VoidBlack = Color(0xFF000000)           // Pure black OLED background
private val ObsidianSurface = Color(0xFF1A1A1A)     // Dark card surface (90% opacity)
private val DeepGrey = Color(0xFF111111)            // Input/console background
private val StrokeGrey = Color(0xFF333333)          // Borders (inactive)
private val TextPrimary = Color(0xFFFFFFFF)         // Pure white text
private val TextSecondary = Color(0xFF888888)       // Steel grey secondary text

// Magma Gradient (Fire accent)
private val MagmaGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFF4D4D), Color(0xFFFF8E53))
)

// Severity Colors (High Contrast)
private val SeverityCritical = Color(0xFFFF3333)
private val SeverityModerate = Color(0xFFFF8E53)
private val SeverityLow = Color(0xFF03DAC5)

/**
 * GridZero Magma Glass Interface
 *
 * Premium Tactical UI with:
 * - Pure black OLED background
 * - High-contrast obsidian cards
 * - Haptic feedback on all interactions
 * - Physics-based touch responses
 * - Crystal clear readability
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

    var showModelSelector by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    // Haptics
    val view = LocalView.current
    val vibrate = { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) }
    val vibrateStrong = { view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
    ) {

        // Ambient Fire Glow (Bottom Right Light Source)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 100.dp)
                .size(400.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF4D4D).copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
                .blur(80.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Header with Settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GRID // ZERO",
                        color = TextPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp
                    )
                    Text(
                        text = statusMessage,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                }

                // Settings Icon
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val scale by animateFloatAsState(
                    targetValue = if (isPressed) 0.92f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "scale"
                )

                Box(
                    modifier = Modifier
                        .scale(scale)
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ObsidianSurface.copy(alpha = 0.9f))
                        .border(
                            1.dp,
                            if (currentModelId != null) SeverityLow else Color(0xFF333333),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = rememberRipple(color = Color.White)
                        ) {
                            vibrate()
                            showModelSelector = !showModelSelector
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (currentModelId != null) Icons.Default.CheckCircle else Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = if (currentModelId != null) SeverityLow else TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error Display
            error?.let { errorMsg ->
                ObsidianErrorCard(errorMsg, onDismiss = {
                    vibrate()
                    viewModel.clearError()
                })
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Model Selector (Expanded)
            if (showModelSelector) {
                ObsidianModelSelector(
                    models = availableModels,
                    currentModelId = currentModelId,
                    downloadProgress = downloadProgress,
                    onDownload = { modelId ->
                        vibrateStrong()
                        viewModel.downloadModel(modelId)
                    },
                    onLoad = { modelId ->
                        vibrateStrong()
                        viewModel.loadModel(modelId)
                    },
                    onRefresh = {
                        vibrate()
                        viewModel.refreshModels()
                    },
                    onDismiss = {
                        vibrate()
                        showModelSelector = false
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Input Console
            ObsidianInput(
                text = inputText,
                onTextChange = { inputText = it },
                onProcess = { text ->
                    vibrateStrong()
                    viewModel.analyzeFieldReport(text)
                    inputText = ""
                },
                isBusy = isAnalyzing,
                isEnabled = currentModelId != null
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Active Streams Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIVE INTELLIGENCE [${reports.size}]",
                    color = Color(0xFFFF4D4D),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                if (reports.isNotEmpty()) {
                    TextButton(onClick = {
                        vibrate()
                        viewModel.clearReports()
                    }) {
                        Text(
                            "CLEAR",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // The List
            if (reports.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = TextSecondary.copy(alpha = 0.3f),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            "NO ACTIVE INCIDENTS",
                            color = TextSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            "Awaiting field reports...",
                            color = TextSecondary.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(reports.reversed()) { report ->
                        ObsidianCard(
                            report = report,
                            onClick = { vibrate() }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Obsidian Input Console
 * High-contrast command input with haptic feedback
 */
@Composable
fun ObsidianInput(
    text: String,
    onTextChange: (String) -> Unit,
    onProcess: (String) -> Unit,
    isBusy: Boolean,
    isEnabled: Boolean
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DeepGrey)
            .border(
                width = 1.dp,
                color = if (isFocused) Color(0xFFFF4D4D) else StrokeGrey,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Text(
            text = "COMMAND INPUT",
            color = Color(0xFFFF4D4D),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Enter field report: e.g., 'Collapse at library, 3 trapped, need crane'",
                    color = TextSecondary.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = TextPrimary,
                backgroundColor = Color.Transparent,
                cursorColor = Color(0xFFFF4D4D),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            enabled = isEnabled && !isBusy,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Process Button with Physics
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.96f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "buttonScale"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .scale(scale)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isEnabled && text.isNotBlank() && !isBusy)
                        MagmaGradient
                    else
                        Brush.horizontalGradient(listOf(Color(0xFF333333), Color(0xFF222222)))
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = Color.White),
                    enabled = isEnabled && text.isNotBlank() && !isBusy
                ) {
                    if (text.isNotBlank()) {
                        onProcess(text)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isBusy) {
                CircularProgressIndicator(
                    color = TextPrimary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "PROCESS INTEL",
                    color = if (isEnabled && text.isNotBlank()) TextPrimary else TextSecondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }

        if (!isEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "⚠️ Load tactical AI model via settings (top-right icon)",
                color = Color(0xFFFF8E53),
                fontSize = 11.sp
            )
        }
    }
}

/**
 * Obsidian Card - High Contrast Dark Card
 * Vertical status strip on left, white text, clear hierarchy
 */
@Composable
fun ObsidianCard(
    report: CrisisReport,
    onClick: () -> Unit
) {
    val severityColor = when (report.severity.lowercase()) {
        "critical" -> SeverityCritical
        "moderate" -> SeverityModerate
        else -> SeverityLow
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cardScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurface.copy(alpha = 0.9f))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = Color.White)
            ) {
                onClick()
            }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Vertical Status Strip (Left)
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(120.dp)
                    .background(severityColor)
            )

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Location Title
                    Text(
                        text = report.locationName,
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    // Timestamp
                    Text(
                        text = formatTimestamp(report.timestamp),
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Incident Type Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(severityColor.copy(alpha = 0.2f))
                        .border(1.dp, severityColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = report.incidentType.uppercase(),
                        color = severityColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Metadata Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "👤 ${report.casualties} casualties",
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "⚡ ${report.sentiment}",
                        color = severityColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Resources
                if (report.resourcesNeeded.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "REQUIRED:",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        report.resourcesNeeded.take(3).forEach { resource ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DeepGrey)
                                    .border(1.dp, StrokeGrey, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = resource,
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Obsidian Error Card
 */
@Composable
fun ObsidianErrorCard(errorMsg: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SeverityCritical.copy(alpha = 0.15f))
            .border(1.dp, SeverityCritical.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚠️ $errorMsg",
                color = TextPrimary,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * Obsidian Model Selector
 */
@Composable
fun ObsidianModelSelector(
    models: List<com.runanywhere.sdk.models.ModelInfo>,
    currentModelId: String?,
    downloadProgress: Float?,
    onDownload: (String) -> Unit,
    onLoad: (String) -> Unit,
    onRefresh: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurface.copy(alpha = 0.95f))
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.2f), Color.Transparent)
                ),
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TACTICAL AI SYSTEMS",
                color = Color(0xFFFF4D4D),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Row {
                IconButton(onClick = onRefresh, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Refresh,
                        "Refresh",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Close,
                        "Close",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (models.isEmpty()) {
            Text(
                text = "No models available. Initializing...",
                color = TextSecondary,
                fontSize = 13.sp
            )
        } else {
            models.forEach { model ->
                ObsidianModelItem(
                    model = model,
                    isLoaded = model.id == currentModelId,
                    onDownload = { onDownload(model.id) },
                    onLoad = { onLoad(model.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        downloadProgress?.let { progress ->
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = Color(0xFFFF4D4D),
                trackColor = DeepGrey
            )
            Text(
                text = "Downloading: ${(progress * 100).toInt()}%",
                color = TextSecondary,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * Obsidian Model Item
 */
@Composable
fun ObsidianModelItem(
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
                    SeverityLow.copy(alpha = 0.1f)
                else
                    DeepGrey
            )
            .border(
                1.dp,
                if (isLoaded) SeverityLow.copy(alpha = 0.5f) else StrokeGrey,
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = model.name,
                color = if (isLoaded) SeverityLow else TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            if (isLoaded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✓ OPERATIONAL",
                    color = SeverityLow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Download Button
                    val downloadInteraction = remember { MutableInteractionSource() }
                    val downloadPressed by downloadInteraction.collectIsPressedAsState()
                    val downloadScale by animateFloatAsState(
                        targetValue = if (downloadPressed) 0.95f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "downloadScale"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .scale(downloadScale)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (model.isDownloaded)
                                    Color(0xFF1A1A1A)
                                else
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color(0xFF2F81F7),
                                            Color(0xFF1E5AAD)
                                        )
                                    )
                            )
                            .clickable(
                                interactionSource = downloadInteraction,
                                indication = rememberRipple(color = Color.White),
                                enabled = !model.isDownloaded
                            ) {
                                onDownload()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (model.isDownloaded) "✓ READY" else "DOWNLOAD",
                            color = if (model.isDownloaded) TextSecondary else TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Load Button
                    val loadInteraction = remember { MutableInteractionSource() }
                    val loadPressed by loadInteraction.collectIsPressedAsState()
                    val loadScale by animateFloatAsState(
                        targetValue = if (loadPressed) 0.95f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "loadScale"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .scale(loadScale)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (model.isDownloaded)
                                    MagmaGradient
                                else
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color(0xFF333333),
                                            Color(0xFF222222)
                                        )
                                    )
                            )
                            .clickable(
                                interactionSource = loadInteraction,
                                indication = rememberRipple(color = Color.White),
                                enabled = model.isDownloaded
                            ) {
                                onLoad()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "LOAD",
                            color = if (model.isDownloaded) TextPrimary else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
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

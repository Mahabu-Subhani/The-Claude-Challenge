package com.runanywhere.startup_hackathon20

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.runanywhere.startup_hackathon20.data.CrisisReport
import java.text.SimpleDateFormat
import java.util.*

// --- NEON-TACTICAL DESIGN SYSTEM ---
private val VoidBlack = Color(0xFF050505)
private val GlassSurface = Color(0xFF151515)

// Module Colors (Neon Glow)
private val CommandRed = Color(0xFFFF3333)
private val IntelCyan = Color(0xFF00E5FF)
private val SystemViolet = Color(0xFFD500F9)

// Utility Colors
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFF888888)

// Tab Navigation
enum class Tab { COMMAND, INTEL, SYSTEM }

/**
 * Cyber-Command HUD Interface v3.0
 *
 * Modular 3D Interface with:
 * - Physics-based tilt reactions
 * - Neon glow modules
 * - 3D depth and layering
 * - Floating glass dock navigation
 */
@Composable
fun CrisisScreen(viewModel: CrisisViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(Tab.COMMAND) }
    val view = LocalView.current

    Scaffold(
        containerColor = VoidBlack,
        bottomBar = {
            NeonDock(
                selected = currentTab,
                onSelect = { tab ->
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    currentTab = tab
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Animated mesh gradient background
            AnimatedMeshBackground()

            // Module Switcher with animation
            AnimatedContent(
                targetState = currentTab,
                label = "tabSwitch"
            ) { tab ->
                when (tab) {
                    Tab.COMMAND -> CommandModule(viewModel)
                    Tab.INTEL -> IntelModule(viewModel)
                    Tab.SYSTEM -> SystemModule(viewModel)
                }
            }
        }
    }
}

/**
 * 3D Tilt Interaction Modifier
 * Creates physics-based tilt effect on touch
 */
fun Modifier.tiltOnTouch(): Modifier = composed {
    var rotationX by remember { mutableStateOf(0f) }
    var rotationY by remember { mutableStateOf(0f) }
    val scale by animateFloatAsState(
        targetValue = if (rotationX != 0f || rotationY != 0f) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "tiltScale"
    )

    this
        .graphicsLayer {
            this.rotationX = rotationX
            this.rotationY = rotationY
            this.scaleX = scale
            this.scaleY = scale
            cameraDistance = 12f * density
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    val centerX = size.width / 2f
                    val centerY = size.height / 2f
                    rotationX = (centerY - offset.y) / 10f
                    rotationY = (offset.x - centerX) / 10f
                    tryAwaitRelease()
                    rotationX = 0f
                    rotationY = 0f
                }
            )
        }
}

/**
 * Animated Mesh Background
 * Subtle radial gradient with slow movement
 */
@Composable
fun AnimatedMeshBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "meshAnim")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "meshOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF111111),
                        VoidBlack
                    ),
                    center = androidx.compose.ui.geometry.Offset(offset, offset)
                )
            )
    )
}

/**
 * Neon Dock - 3D Floating Navigation
 * Bottom navigation bar with glass morphism and neon indicators
 */
@Composable
fun NeonDock(
    selected: Tab,
    onSelect: (Tab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glass pill background
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(GlassSurface.copy(alpha = 0.9f))
                .border(
                    1.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    RoundedCornerShape(30.dp)
                )
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DockItem(
                icon = Icons.Default.Terminal,
                label = "COMMAND",
                color = CommandRed,
                isSelected = selected == Tab.COMMAND,
                onClick = { onSelect(Tab.COMMAND) }
            )
            DockItem(
                icon = Icons.Default.Radar,
                label = "INTEL",
                color = IntelCyan,
                isSelected = selected == Tab.INTEL,
                onClick = { onSelect(Tab.INTEL) }
            )
            DockItem(
                icon = Icons.Default.Settings,
                label = "SYSTEM",
                color = SystemViolet,
                isSelected = selected == Tab.SYSTEM,
                onClick = { onSelect(Tab.SYSTEM) }
            )
        }
    }
}

/**
 * Dock Item - Individual tab button
 */
@Composable
fun RowScope.DockItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "dockScale"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .scale(scale)
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (isSelected)
                    color.copy(alpha = 0.2f)
                else
                    Color.Transparent
            )
            .border(
                width = 1.dp,
                color = if (isSelected) color else Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (isSelected) color else TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                color = if (isSelected) color else TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

/**
 * MODULE 1: Command Console
 * Reactor core design with centered input
 */
@Composable
fun CommandModule(viewModel: CrisisViewModel) {
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val currentModelId by viewModel.currentModelId.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val view = LocalView.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header
            Text(
                text = "COMMAND INTERFACE",
                color = CommandRed,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Reactor Core Input
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .tiltOnTouch()
                    .border(
                        3.dp,
                        Brush.sweepGradient(
                            colors = listOf(
                                CommandRed,
                                Color.Transparent,
                                CommandRed,
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
                    .background(Color(0xFF1A0000), CircleShape)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                // Inner glow
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    CommandRed.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "FIELD REPORT...",
                                color = TextSecondary.copy(alpha = 0.5f),
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = CommandRed,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        ),
                        enabled = currentModelId != null && !isAnalyzing,
                        maxLines = 4
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Launch Button
            val buttonInteraction = remember { MutableInteractionSource() }
            val buttonPressed by buttonInteraction.collectIsPressedAsState()
            val buttonScale by animateFloatAsState(
                targetValue = if (buttonPressed) 0.92f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "buttonScale"
            )

            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .scale(buttonScale)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (currentModelId != null && inputText.isNotBlank() && !isAnalyzing)
                            Brush.horizontalGradient(
                                listOf(CommandRed, Color(0xFFFF6666))
                            )
                        else
                            Brush.horizontalGradient(
                                listOf(Color(0xFF333333), Color(0xFF222222))
                            )
                    )
                    .border(
                        1.dp,
                        if (currentModelId != null && inputText.isNotBlank())
                            CommandRed.copy(alpha = 0.5f)
                        else
                            Color.Transparent,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable(
                        interactionSource = buttonInteraction,
                        indication = null,
                        enabled = currentModelId != null && inputText.isNotBlank() && !isAnalyzing
                    ) {
                        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                        viewModel.analyzeFieldReport(inputText)
                        inputText = ""
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        color = TextPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "◉ LAUNCH",
                        color = if (currentModelId != null && inputText.isNotBlank())
                            TextPrimary
                        else
                            TextSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )
                }
            }

            if (currentModelId == null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "⚠ LOAD AI MODEL VIA SYSTEM TAB",
                    color = Color(0xFFFF8E53),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * MODULE 2: Intel Stream
 * 3D stacked glass slabs with neon borders
 */
@Composable
fun IntelModule(viewModel: CrisisViewModel) {
    val reports by viewModel.reports.collectAsState()
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "LIVE INTELLIGENCE",
                    color = IntelCyan,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "${reports.size} ACTIVE STREAMS",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            if (reports.isNotEmpty()) {
                TextButton(onClick = {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    viewModel.clearReports()
                }) {
                    Text(
                        "CLEAR",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Intel Stream
        if (reports.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
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
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        "NO ACTIVE STREAMS",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        "Awaiting field intelligence...",
                        color = TextSecondary.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(reports.reversed()) { report ->
                    IntelCard(
                        report = report,
                        onClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Intel Card - 3D Glass Slab with Neon Border
 */
@Composable
fun IntelCard(
    report: CrisisReport,
    onClick: () -> Unit
) {
    val severityColor = when (report.severity.lowercase()) {
        "critical" -> CommandRed
        "moderate" -> Color(0xFFFF8E53)
        else -> IntelCyan
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .tiltOnTouch()
            .clickable(onClick = onClick)
    ) {
        // Neon border strip (left)
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(140.dp)
                .background(severityColor)
                .blur(8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(140.dp)
                    .background(severityColor)
            )

            // Glass slab content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(GlassSurface.copy(alpha = 0.9f))
                    .border(
                        1.dp,
                        Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = report.locationName.uppercase(),
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = formatTimestamp(report.timestamp),
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Type badge
                    Text(
                        text = "▸ ${report.incidentType.uppercase()}",
                        color = severityColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Metadata
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CASUALTIES: ${report.casualties}",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "STATUS: ${report.sentiment.uppercase()}",
                            color = severityColor,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Resources
                    if (report.resourcesNeeded.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "ASSETS: ${
                                report.resourcesNeeded.joinToString(" / ").uppercase()
                            }",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

/**
 * MODULE 3: System Configuration
 * Model management and settings
 */
@Composable
fun SystemModule(viewModel: CrisisViewModel) {
    val availableModels by viewModel.availableModels.collectAsState()
    val currentModelId by viewModel.currentModelId.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Header
        Text(
            text = "SYSTEM CONTROL",
            color = SystemViolet,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = statusMessage,
            color = TextSecondary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(32.dp))

        // AI Models Section
        Text(
            text = "TACTICAL AI SYSTEMS",
            color = SystemViolet,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (availableModels.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(GlassSurface.copy(alpha = 0.5f))
                    .border(1.dp, SystemViolet.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Initializing AI systems...",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(availableModels) { model ->
                    SystemModelCard(
                        model = model,
                        isLoaded = model.id == currentModelId,
                        onDownload = {
                            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                            viewModel.downloadModel(model.id)
                        },
                        onLoad = {
                            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                            viewModel.loadModel(model.id)
                        }
                    )
                }
            }
        }

        downloadProgress?.let { progress ->
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = SystemViolet,
                trackColor = GlassSurface
            )
            Text(
                text = "DOWNLOADING: ${(progress * 100).toInt()}%",
                color = SystemViolet,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * System Model Card - 3D AI Model Configuration
 */
@Composable
fun SystemModelCard(
    model: com.runanywhere.sdk.models.ModelInfo,
    isLoaded: Boolean,
    onDownload: () -> Unit,
    onLoad: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .tiltOnTouch()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isLoaded)
                    SystemViolet.copy(alpha = 0.15f)
                else
                    GlassSurface.copy(alpha = 0.9f)
            )
            .border(
                2.dp,
                if (isLoaded) SystemViolet else Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = model.name.uppercase(),
                color = if (isLoaded) SystemViolet else TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            if (isLoaded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "◉ OPERATIONAL",
                    color = SystemViolet,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Download Button
                    val downloadInteraction = remember { MutableInteractionSource() }
                    val downloadPressed by downloadInteraction.collectIsPressedAsState()
                    val downloadScale by animateFloatAsState(
                        targetValue = if (downloadPressed) 0.92f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "downloadScale"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .scale(downloadScale)
                            .clip(RoundedCornerShape(12.dp))
                            .then(
                                if (model.isDownloaded)
                                    Modifier.background(Color(0xFF1A1A1A))
                                else
                                    Modifier.background(
                                        Brush.horizontalGradient(
                                            listOf(IntelCyan, Color(0xFF0099CC))
                                        )
                                    )
                            )
                            .clickable(
                                interactionSource = downloadInteraction,
                                indication = null,
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
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Load Button
                    val loadInteraction = remember { MutableInteractionSource() }
                    val loadPressed by loadInteraction.collectIsPressedAsState()
                    val loadScale by animateFloatAsState(
                        targetValue = if (loadPressed) 0.92f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "loadScale"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .scale(loadScale)
                            .clip(RoundedCornerShape(12.dp))
                            .then(
                                if (model.isDownloaded)
                                    Modifier.background(
                                        Brush.horizontalGradient(
                                            listOf(SystemViolet, Color(0xFFAA00CC))
                                        )
                                    )
                                else
                                    Modifier.background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF333333), Color(0xFF222222))
                                        )
                                    )
                            )
                            .clickable(
                                interactionSource = loadInteraction,
                                indication = null,
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
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
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

package com.runanywhere.startup_hackathon20

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.runanywhere.startup_hackathon20.data.CrisisReport

// --- 1. THE NEON-TACTICAL DESIGN SYSTEM ---

// Color Palette (Cyberpunk Neon)
val VoidBlack = Color(0xFF050505)
val MagmaRed = Color(0xFFFF3333)
val CyberCyan = Color(0xFF00E5FF)
val DeepViolet = Color(0xFFD500F9)
val ObsidianGlass = Color(0xFF151515).copy(alpha = 0.9f)
val WhiteText = Color(0xFFFFFFFF)

// Typography
val TacticalFont = FontFamily.Monospace

// Enum for Navigation
enum class Tab { COMMAND, INTEL, SETTINGS }

// --- 2. MAIN SCREEN COMPOSABLE ---

@Composable
fun CrisisScreen(viewModel: CrisisViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(Tab.COMMAND) }

    Scaffold(
        containerColor = VoidBlack,
        bottomBar = {
            NeonDock(
                selected = currentTab,
                onSelect = { currentTab = it }
            )
        }
    ) { padding ->
        // Background Mesh Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF111111), VoidBlack),
                        radius = 1200f
                    )
                )
        ) {
            // 3D Module Switcher
            AnimatedContent(
                targetState = currentTab,
                label = "ModuleSwitch",
                modifier = Modifier.fillMaxSize()
            ) { tab ->
                when (tab) {
                    Tab.COMMAND -> CommandModule(viewModel)
                    Tab.INTEL -> IntelModule(viewModel)
                    Tab.SETTINGS -> SettingsModule(viewModel)
                }
            }
        }
    }
}

// --- 3. ADVANCED INTERACTION: PHYSICS MODIFIER ---

fun Modifier.tiltOnTouch(
    tiltStrength: Float = 10f,
    scaleStrength: Float = 0.95f
): Modifier = composed {
    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }
    val isPressed = rotationX != 0f || rotationY != 0f

    // Smooth physics animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleStrength else 1f,
        animationSpec = tween(100),
        label = "Scale"
    )
    val animatedRotX by animateFloatAsState(targetValue = rotationX, label = "RotX")
    val animatedRotY by animateFloatAsState(targetValue = rotationY, label = "RotY")

    val view = LocalView.current

    this
        .graphicsLayer {
            this.rotationX = animatedRotX
            this.rotationY = animatedRotY
            this.scaleX = scale
            this.scaleY = scale
            cameraDistance = 16f * density // Increases 3D perspective depth
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    // Trigger Haptics
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)

                    // Calculate Tilt Physics
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    // Invert Y logic because pressing top should tilt top-away (negative X rot)
                    rotationX = (centerY - offset.y) / tiltStrength
                    rotationY = (offset.x - centerX) / tiltStrength

                    tryAwaitRelease()

                    // Reset Physics
                    rotationX = 0f
                    rotationY = 0f
                }
            )
        }
}

// --- 4. MODULE 1: COMMAND CONSOLE (Magma Theme) ---

@Composable
fun CommandModule(viewModel: CrisisViewModel) {
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val currentModelId by viewModel.currentModelId.collectAsState()
    var input by remember { mutableStateOf("") }
    val view = LocalView.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "COMMAND LINK // ACTIVE",
            color = MagmaRed,
            fontFamily = TacticalFont,
            fontSize = 12.sp,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // THE REACTOR INPUT
        Box(
            modifier = Modifier
                .size(320.dp)
                .tiltOnTouch(tiltStrength = 15f) // High tilt for reactor
                .border(
                    2.dp,
                    Brush.sweepGradient(listOf(MagmaRed, Color.Transparent, MagmaRed)),
                    CircleShape
                )
                .background(Color(0xFF110000).copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Inner Core
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .border(1.dp, MagmaRed.copy(alpha = 0.3f), CircleShape)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(color = MagmaRed)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("UPLOADING PACKET...", color = MagmaRed, fontFamily = TacticalFont)
                } else {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        placeholder = {
                            Text(
                                "ENTER TACTICAL DATA",
                                color = MagmaRed.copy(0.5f),
                                fontSize = 10.sp,
                                fontFamily = TacticalFont
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MagmaRed,
                            unfocusedTextColor = MagmaRed,
                            cursorColor = MagmaRed,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontFamily = TacticalFont,
                            color = MagmaRed,
                            fontSize = 14.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        ),
                        enabled = currentModelId != null && !isAnalyzing,
                        maxLines = 3
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Launch Trigger
        Button(
            onClick = {
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                viewModel.analyzeFieldReport(input)
                input = ""
            },
            enabled = !isAnalyzing && input.isNotBlank() && currentModelId != null,
            modifier = Modifier
                .width(200.dp)
                .height(56.dp)
                .tiltOnTouch(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MagmaRed,
                disabledContainerColor = Color(0xFF333333)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "TRANSMIT",
                fontFamily = TacticalFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        if (currentModelId == null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "⚠ LOAD AI MODEL VIA SETTINGS TAB",
                color = Color(0xFFFF8E53),
                fontSize = 11.sp,
                fontFamily = TacticalFont
            )
        }
    }
}

// --- 5. MODULE 2: INTEL STREAM (Cyan Theme) ---

@Composable
fun IntelModule(viewModel: CrisisViewModel) {
    val reports by viewModel.reports.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LIVE INTELLIGENCE STREAM [${reports.size}]",
                color = CyberCyan,
                fontFamily = TacticalFont,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )

            if (reports.isNotEmpty()) {
                val view = LocalView.current
                TextButton(onClick = {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    viewModel.clearReports()
                }) {
                    Text(
                        "CLEAR",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontFamily = TacticalFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (reports.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "NO ACTIVE STREAMS",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = TacticalFont
                    )
                    Text(
                        "Awaiting field intelligence...",
                        color = Color.Gray.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontFamily = TacticalFont
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(reports.reversed()) { report ->
                    GlassIntelCard(report)
                }
            }
        }
    }
}

@Composable
fun GlassIntelCard(report: CrisisReport) {
    val severityColor = when (report.severity.lowercase()) {
        "critical" -> MagmaRed
        "moderate" -> Color.Yellow
        else -> CyberCyan
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .tiltOnTouch()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianGlass)
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(severityColor.copy(0.5f), Color.Transparent)
                ),
                RoundedCornerShape(12.dp)
            )
    ) {
        // Severity Indicator Bar
        Box(
            modifier = Modifier
                .width(6.dp)
                .fillMaxHeight()
                .background(severityColor)
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    report.incidentType.uppercase(),
                    color = severityColor,
                    fontFamily = TacticalFont,
                    fontSize = 10.sp
                )
                Text(
                    "T-${report.timestamp.toString().takeLast(4)}",
                    color = Color.Gray,
                    fontFamily = TacticalFont,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = report.locationName,
                color = WhiteText,
                fontFamily = TacticalFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                report.resourcesNeeded.take(3).forEach { res ->
                    Text(
                        "[$res] ",
                        color = CyberCyan,
                        fontFamily = TacticalFont,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

// --- 6. MODULE 3: SETTINGS (Violet Theme) ---

@Composable
fun SettingsModule(viewModel: CrisisViewModel) {
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
            text = "SYSTEM CONFIGURATION",
            color = DeepViolet,
            fontFamily = TacticalFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "GRID ZERO // OFFLINE",
            color = Color.Gray,
            fontFamily = TacticalFont,
            fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = statusMessage,
            color = Color.Gray,
            fontFamily = TacticalFont,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // AI Models Section
        Text(
            text = "TACTICAL AI SYSTEMS",
            color = DeepViolet,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TacticalFont,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (availableModels.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianGlass.copy(alpha = 0.5f))
                    .border(1.dp, DeepViolet.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Initializing AI systems...",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontFamily = TacticalFont
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
                color = DeepViolet,
                trackColor = ObsidianGlass
            )
            Text(
                text = "DOWNLOADING: ${(progress * 100).toInt()}%",
                color = DeepViolet,
                fontSize = 11.sp,
                fontFamily = TacticalFont,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

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
                    DeepViolet.copy(alpha = 0.15f)
                else
                    ObsidianGlass
            )
            .border(
                2.dp,
                if (isLoaded) DeepViolet else Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = model.name.uppercase(),
                color = if (isLoaded) DeepViolet else WhiteText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TacticalFont
            )

            if (isLoaded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "◉ OPERATIONAL",
                    color = DeepViolet,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TacticalFont
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Download Button
                    Button(
                        onClick = onDownload,
                        enabled = !model.isDownloaded,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (model.isDownloaded)
                                Color(0xFF1A1A1A)
                            else
                                CyberCyan,
                            disabledContainerColor = Color(0xFF1A1A1A)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (model.isDownloaded) "✓ READY" else "DOWNLOAD",
                            color = if (model.isDownloaded) Color.Gray else WhiteText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = TacticalFont
                        )
                    }

                    // Load Button
                    Button(
                        onClick = onLoad,
                        enabled = model.isDownloaded,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (model.isDownloaded)
                                DeepViolet
                            else
                                Color(0xFF333333),
                            disabledContainerColor = Color(0xFF333333)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "LOAD",
                            color = if (model.isDownloaded) WhiteText else Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = TacticalFont
                        )
                    }
                }
            }
        }
    }
}

// --- 7. THE NEON DOCK (Navigation) ---

@Composable
fun NeonDock(selected: Tab, onSelect: (Tab) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(35.dp))
            .background(ObsidianGlass)
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(35.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            DockItem(
                Tab.COMMAND,
                Icons.Default.AddCircle,
                MagmaRed,
                selected == Tab.COMMAND
            ) {
                onSelect(Tab.COMMAND)
            }
            DockItem(
                Tab.INTEL,
                Icons.Default.List,
                CyberCyan,
                selected == Tab.INTEL
            ) {
                onSelect(Tab.INTEL)
            }
            DockItem(
                Tab.SETTINGS,
                Icons.Default.Settings,
                DeepViolet,
                selected == Tab.SETTINGS
            ) {
                onSelect(Tab.SETTINGS)
            }
        }
    }
}

@Composable
fun DockItem(
    tab: Tab,
    icon: ImageVector,
    color: Color,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.2f else 1f,
        label = "dockScale"
    )
    val alpha = if (isActive) 1f else 0.4f

    val view = LocalView.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures {
                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                    onClick()
                }
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = tab.name,
            tint = color.copy(alpha = alpha),
            modifier = Modifier.size(28.dp)
        )
        if (isActive) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(4.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

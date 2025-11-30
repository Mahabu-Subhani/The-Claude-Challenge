package com.runanywhere.startup_hackathon20

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
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

// --- 1. THE NEON-TACTICAL DESIGN SYSTEM (V2 - ULTRA) ---

// Color Palette
val VoidBlack = Color(0xFF020202)
val MagmaRed = Color(0xFFFF2A2A) // Brighter Red
val CyberCyan = Color(0xFF00F0FF) // Neon Cyan
val DeepViolet = Color(0xFFBD00FF)
val ObsidianGlass = Color(0xFF0A0A0A).copy(alpha = 0.95f) // Darker, heavier glass
val WhiteText = Color(0xFFFFFFFF)

// Typography
val TacticalFont = FontFamily.Monospace

// Navigation
enum class Tab { COMMAND, INTEL, SETTINGS }

// --- 2. MAIN SCREEN COMPOSABLE ---

@Composable
fun CrisisScreen(viewModel: CrisisViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(Tab.COMMAND) }

    // Root Scaffold
    Scaffold(
        containerColor = VoidBlack,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                NeonDock(
                    selected = currentTab,
                    onSelect = { currentTab = it }
                )
            }
        }
    ) { padding ->
        // Background: Live Radar Grid
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VoidBlack)
        ) {
            LiveGridBackground()

            // Content Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
                    .padding(bottom = 100.dp)
            ) {
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
}

// --- 3. CUSTOM DRAWING ANIMATIONS ---

@Composable
fun LiveGridBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "grid")
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanline"
    )

    Canvas(modifier = Modifier
        .fillMaxSize()
        .graphicsLayer { alpha = 0.3f }) {
        val width = size.width
        val height = size.height
        val gridSize = 60.dp.toPx()

        // Draw Vertical Grid Lines
        for (x in 0..((width / gridSize).toInt())) {
            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = Offset(x * gridSize, 0f),
                end = Offset(x * gridSize, height),
                strokeWidth = 1f
            )
        }
        // Draw Horizontal Grid Lines
        for (y in 0..((height / gridSize).toInt())) {
            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = Offset(0f, y * gridSize),
                end = Offset(width, y * gridSize),
                strokeWidth = 1f
            )
        }

        // Draw Active Scanline
        drawLine(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, MagmaRed.copy(0.5f), Color.Transparent)
            ),
            start = Offset(0f, scanLineY % height),
            end = Offset(width, scanLineY % height),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
fun TacticalLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(size.width / 2, 0f)
                    lineTo(size.width, size.height * 0.7f)
                    lineTo(size.width / 2, size.height)
                    lineTo(0f, size.height * 0.7f)
                    close()
                }
                drawPath(
                    path = path,
                    color = MagmaRed.copy(alpha = pulseAlpha),
                    style = Stroke(width = 4f)
                )
                drawCircle(
                    color = MagmaRed,
                    radius = 4.dp.toPx(),
                    center = center
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "GRID ZERO",
            color = Color.White,
            fontFamily = TacticalFont,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            letterSpacing = 4.sp
        )
        Text(
            text = "TACTICAL // ONLINE",
            color = MagmaRed,
            fontFamily = TacticalFont,
            fontSize = 10.sp,
            letterSpacing = 2.sp
        )
    }
}

// --- 4. ADVANCED INTERACTION: PHYSICS (SPRING) ---

fun Modifier.tiltOnTouch(
    tiltStrength: Float = 8f,
    scaleStrength: Float = 0.96f
): Modifier = composed {
    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }

    val animatedRotX by animateFloatAsState(
        targetValue = rotationX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "RotX"
    )
    val animatedRotY by animateFloatAsState(
        targetValue = rotationY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "RotY"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (rotationX != 0f) scaleStrength else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "Scale"
    )

    val view = LocalView.current

    this
        .graphicsLayer {
            this.rotationX = animatedRotX
            this.rotationY = animatedRotY
            this.scaleX = animatedScale
            this.scaleY = animatedScale
            cameraDistance = 12f * density
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)

                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    rotationX = (centerY - offset.y) / tiltStrength
                    rotationY = (offset.x - centerX) / tiltStrength

                    tryAwaitRelease()

                    rotationX = 0f
                    rotationY = 0f
                }
            )
        }
}

// --- 5. MODULE 1: COMMAND CONSOLE ---

@Composable
fun CommandModule(viewModel: CrisisViewModel) {
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val currentModelId by viewModel.currentModelId.collectAsState()
    var input by remember { mutableStateOf("") }
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 1. LOGO
        TacticalLogo()

        Spacer(modifier = Modifier.weight(1f))

        // 2. INPUT TERMINAL
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .tiltOnTouch(tiltStrength = 20f)
                .background(ObsidianGlass, RoundedCornerShape(16.dp))
                .border(1.dp, MagmaRed.copy(0.3f), RoundedCornerShape(16.dp))
        ) {
            // Corner Brackets
            Canvas(modifier = Modifier.fillMaxSize()) {
                val s = 20.dp.toPx()
                val t = 2.dp.toPx()
                val c = MagmaRed.copy(0.6f)
                // Top Left
                drawLine(c, Offset(0f, 0f), Offset(s, 0f), t)
                drawLine(c, Offset(0f, 0f), Offset(0f, s), t)
                // Bottom Right
                drawLine(c, Offset(size.width, size.height), Offset(size.width - s, size.height), t)
                drawLine(c, Offset(size.width, size.height), Offset(size.width, size.height - s), t)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(color = MagmaRed)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "ENCRYPTING PACKET...",
                        color = MagmaRed,
                        fontFamily = TacticalFont,
                        fontSize = 12.sp
                    )
                } else {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        placeholder = {
                            Text(
                                "ENTER COMMAND SEQUENCE",
                                color = Color.Gray,
                                fontSize = 12.sp,
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
                            fontSize = 16.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        ),
                        enabled = currentModelId != null && !isAnalyzing,
                        maxLines = 5
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. TRANSMIT TRIGGER
        Button(
            onClick = {
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                viewModel.analyzeFieldReport(input)
                input = ""
            },
            enabled = !isAnalyzing && input.isNotBlank() && currentModelId != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .tiltOnTouch(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MagmaRed,
                disabledContainerColor = Color(0xFF333333)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "EXECUTE TRANSMISSION",
                    fontFamily = TacticalFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
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

        Spacer(modifier = Modifier.weight(1.5f))
    }
}

// --- 6. MODULE 2: INTEL STREAM ---

@Composable
fun IntelModule(viewModel: CrisisViewModel) {
    val reports by viewModel.reports.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(CyberCyan, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "LIVE FEED [${reports.size}]",
                color = CyberCyan,
                fontFamily = TacticalFont,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.weight(1f))

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
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(reports.reversed()) { report ->
                    DataSlab(report)
                }
            }
        }
    }
}

@Composable
fun DataSlab(report: CrisisReport) {
    val color = when (report.severity.lowercase()) {
        "critical" -> MagmaRed
        "moderate" -> Color.Yellow
        else -> CyberCyan
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .tiltOnTouch()
            .background(ObsidianGlass, RoundedCornerShape(4.dp))
            .border(1.dp, color.copy(0.3f), RoundedCornerShape(4.dp))
    ) {
        // ID Strip
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(100.dp)
                .background(color.copy(0.1f))
                .border(1.dp, color.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = report.incidentType.take(3).uppercase(),
                color = color,
                fontFamily = TacticalFont,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer { rotationZ = -90f }
            )
        }

        // Data Content
        Column(
            modifier = Modifier
                .padding(12.dp)
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    report.locationName.uppercase(),
                    color = WhiteText,
                    fontFamily = TacticalFont,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    report.timestamp.toString().takeLast(4),
                    color = Color.Gray,
                    fontFamily = TacticalFont,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                report.resourcesNeeded.take(3).forEach {
                    Text(
                        ":: $it",
                        color = color,
                        fontFamily = TacticalFont,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

// --- 7. MODULE 3: SETTINGS ---

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
            .padding(24.dp)
    ) {
        Text(
            text = "SYSTEM CONTROL",
            color = DeepViolet,
            fontFamily = TacticalFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = statusMessage,
            color = Color.Gray,
            fontFamily = TacticalFont,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

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
                            color = if (model.isDownloaded) Color.Gray else Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = TacticalFont
                        )
                    }

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
                            color = if (model.isDownloaded) Color.Black else Color.Gray,
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

// --- 8. THE FLOATING DOCK ---

@Composable
fun NeonDock(selected: Tab, onSelect: (Tab) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .graphicsLayer {
                shadowElevation = 20f
                shape = RoundedCornerShape(40.dp)
                clip = true
            }
            .background(ObsidianGlass)
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(40.dp)),
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
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (isActive) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

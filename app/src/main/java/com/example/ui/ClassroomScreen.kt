package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AITeacher
import com.example.model.ClassroomLecture
import com.example.model.FacultyRepository
import com.example.ui.theme.ChalkCyan
import com.example.ui.theme.ChalkWhite
import com.example.ui.theme.ChalkYellow
import com.example.ui.theme.ChalkboardBorder
import com.example.ui.theme.ChalkboardGreen
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSecondary
import com.example.util.VoiceTeacherManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomScreen(
    onNavigateToDoubtRoom: (AITeacher) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val voiceManager = remember { VoiceTeacherManager(context) }
    val isSpeaking by voiceManager.isSpeaking.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            voiceManager.shutdown()
        }
    }

    val lectures = FacultyRepository.sampleLectures
    var selectedLecture by remember { mutableStateOf(lectures.first()) }
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    val currentTeacher = remember(selectedLecture) {
        FacultyRepository.defaultFacultyList.find { it.id == selectedLecture.teacherId }
            ?: FacultyRepository.defaultFacultyList.first()
    }

    val currentStep = selectedLecture.steps.getOrNull(currentStepIndex) ?: selectedLecture.steps.first()

    // Auto-speak lecture step when step changes
    LaunchedEffect(currentStepIndex, selectedLecture) {
        selectedAnswerIndex = null
        isAnswerSubmitted = false
        voiceManager.speak(
            text = "${currentStep.subtopicTitle}. ${currentStep.teacherSpeech}",
            pitch = currentTeacher.voicePitch,
            speed = currentTeacher.voiceSpeed
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Live AI Masterclass",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "${currentTeacher.name} • ${selectedLecture.subject}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IndigoPrimary
                ),
                actions = {
                    IconButton(
                        onClick = { onNavigateToDoubtRoom(currentTeacher) },
                        modifier = Modifier.testTag("ask_doubt_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Ask 1-on-1 Doubt",
                            tint = ChalkYellow
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Lecture Switcher Tabs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    lectures.forEach { lec ->
                        val isSelected = lec.id == selectedLecture.id
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedLecture = lec
                                currentStepIndex = 0
                            },
                            label = { Text(lec.subject, fontSize = 13.sp) },
                            modifier = Modifier.testTag("filter_lecture_${lec.id}")
                        )
                    }
                }
            }

            // Realistic Teacher Header Card with Live Waveform Animation
            item {
                RealisticTeacherCard(
                    teacher = currentTeacher,
                    isSpeaking = isSpeaking,
                    onToggleVoice = {
                        if (isSpeaking) {
                            voiceManager.stopSpeaking()
                        } else {
                            voiceManager.speak(
                                text = currentStep.teacherSpeech,
                                pitch = currentTeacher.voicePitch,
                                speed = currentTeacher.voiceSpeed
                            )
                        }
                    },
                    onAskDoubt = { onNavigateToDoubtRoom(currentTeacher) }
                )
            }

            // Interactive Smart Blackboard / Chalkboard
            item {
                SmartChalkboard(
                    stepTitle = currentStep.subtopicTitle,
                    chalkContent = currentStep.chalkboardContent,
                    keyEquation = currentStep.keyEquationOrDiagram
                )
            }

            // Teacher Live Speech Subtitles
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = IndigoSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${currentTeacher.name}'s Explanation",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (isSpeaking) {
                                LiveVoiceWaveAnimation()
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentStep.teacherSpeech,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Socratic Comprehension Check Quiz
            if (currentStep.comprehensionCheckQuestion != null && currentStep.comprehensionCheckOptions != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🧠 Concept Check",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = IndigoSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = currentStep.comprehensionCheckQuestion,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            currentStep.comprehensionCheckOptions.forEachIndexed { index, option ->
                                val isSelected = selectedAnswerIndex == index
                                val isCorrect = currentStep.correctOptionIndex == index

                                val (bgColor, borderColor) = when {
                                    !isAnswerSubmitted -> {
                                        if (isSelected) Pair(IndigoSecondary.copy(alpha = 0.15f), IndigoSecondary)
                                        else Pair(MaterialTheme.colorScheme.surfaceVariant, Color.Transparent)
                                    }
                                    isCorrect -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
                                    isSelected -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
                                    else -> Pair(MaterialTheme.colorScheme.surfaceVariant, Color.Transparent)
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = bgColor,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable(enabled = !isAnswerSubmitted) {
                                            selectedAnswerIndex = index
                                            isAnswerSubmitted = true
                                            if (isCorrect) {
                                                voiceManager.speak(
                                                    "Excellent work! ${currentStep.explanationAfterAnswer ?: "That is exactly right!"}",
                                                    currentTeacher.voicePitch,
                                                    currentTeacher.voiceSpeed
                                                )
                                            } else {
                                                voiceManager.speak(
                                                    "Good attempt! ${currentStep.explanationAfterAnswer ?: "Let's review the chalkboard derivation."}",
                                                    currentTeacher.voicePitch,
                                                    currentTeacher.voiceSpeed
                                                )
                                            }
                                        }
                                        .testTag("quiz_option_$index")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${('A' + index)}. ",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = option,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isAnswerSubmitted && isCorrect) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Correct",
                                                tint = Color(0xFF2E7D32)
                                            )
                                        } else if (isAnswerSubmitted && isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Incorrect",
                                                tint = Color(0xFFC62828)
                                            )
                                        }
                                    }
                                }
                            }

                            AnimatedVisibility(visible = isAnswerSubmitted) {
                                Column(modifier = Modifier.padding(top = 10.dp)) {
                                    Text(
                                        text = currentStep.explanationAfterAnswer ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (selectedAnswerIndex == currentStep.correctOptionIndex) Color(0xFF2E7D32) else Color(0xFFC62828),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Step Navigation Controls
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (currentStepIndex > 0) currentStepIndex--
                        },
                        enabled = currentStepIndex > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("prev_step_button")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Prev Step")
                    }

                    Text(
                        text = "Step ${currentStepIndex + 1} of ${selectedLecture.steps.size}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Button(
                        onClick = {
                            if (currentStepIndex < selectedLecture.steps.size - 1) currentStepIndex++
                        },
                        enabled = currentStepIndex < selectedLecture.steps.size - 1,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IndigoPrimary
                        ),
                        modifier = Modifier.testTag("next_step_button")
                    ) {
                        Text("Next Step")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RealisticTeacherCard(
    teacher: AITeacher,
    isSpeaking: Boolean,
    onToggleVoice: () -> Unit,
    onAskDoubt: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = IndigoPrimary
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Realistic Human Teacher Portrait with Glow & Live Pulse
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(76.dp)
                    .scale(pulseScale)
            ) {
                if (isSpeaking) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(ChalkYellow.copy(alpha = 0.6f), Color.Transparent)
                                )
                            )
                    )
                }

                Image(
                    painter = painterResource(id = teacher.getTeacherImageRes()),
                    contentDescription = teacher.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isSpeaking) 2.5.dp else 1.5.dp,
                            color = if (isSpeaking) ChalkYellow else Color.White.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = teacher.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = teacher.qualifications,
                    style = MaterialTheme.typography.bodySmall,
                    color = ChalkYellow,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⭐ ${teacher.rating} • ${teacher.doubtsSolvedCount} Doubts Solved",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onToggleVoice,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSpeaking) Color(0xFFC62828) else ChalkYellow,
                            contentColor = if (isSpeaking) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp).testTag("voice_toggle_btn")
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Voice",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isSpeaking) "Pause" else "Speak", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onAskDoubt,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp).testTag("ask_doubt_btn")
                    ) {
                        Text("1-on-1 Doubt", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SmartChalkboard(
    stepTitle: String,
    chalkContent: String,
    keyEquation: String?
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = ChalkboardGreen
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(3.dp, ChalkboardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "DIGITAL CHALKBOARD",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                    color = ChalkCyan
                )
                Text(
                    text = "Step Active",
                    style = MaterialTheme.typography.labelSmall,
                    color = ChalkYellow
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stepTitle,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                color = ChalkWhite
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = chalkContent,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 20.sp
                ),
                color = ChalkWhite
            )

            if (keyEquation != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color.Black.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ChalkYellow.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚡ Key: $keyEquation",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        ),
                        color = ChalkYellow,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LiveVoiceWaveAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val h1 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(tween(300), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(400), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(tween(250), RepeatMode.Reverse),
        label = "h3"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(24.dp)
    ) {
        Box(modifier = Modifier.width(3.dp).height(h1.dp).background(IndigoSecondary, RoundedCornerShape(2.dp)))
        Box(modifier = Modifier.width(3.dp).height(h2.dp).background(IndigoSecondary, RoundedCornerShape(2.dp)))
        Box(modifier = Modifier.width(3.dp).height(h3.dp).background(IndigoSecondary, RoundedCornerShape(2.dp)))
        Box(modifier = Modifier.width(3.dp).height(h1.dp).background(IndigoSecondary, RoundedCornerShape(2.dp)))
    }
}

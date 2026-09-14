package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.model.FacultyRepository
import com.example.model.TeacherDoubtMessage
import com.example.ui.theme.ChalkCyan
import com.example.ui.theme.ChalkWhite
import com.example.ui.theme.ChalkYellow
import com.example.ui.theme.ChalkboardBorder
import com.example.ui.theme.ChalkboardGreen
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSecondary
import com.example.util.GeminiTeacherService
import com.example.util.VoiceTeacherManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDoubtRoomScreen(
    initialTeacher: AITeacher,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val voiceManager = remember { VoiceTeacherManager(context) }
    val isSpeaking by voiceManager.isSpeaking.collectAsState()
    val isListening by voiceManager.isListening.collectAsState()

    var activeTeacher by remember { mutableStateOf(initialTeacher) }
    var inputText by remember { mutableStateOf("") }
    var isLoadingResponse by remember { mutableStateOf(false) }
    var showTeacherPicker by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            TeacherDoubtMessage(
                id = "msg_init",
                sender = "TEACHER",
                message = "Namaste! I am ${initialTeacher.name}. Ask me any question or doubt in ${initialTeacher.subject}—from foundational concepts to tricky JEE/NEET questions. You can type or tap the microphone to speak!",
                chalkboardEquation = "★ 1-ON-1 Socratic Doubt Room ★\nTeacher: ${initialTeacher.name}\nSubject: ${initialTeacher.subject}\nMethod: Socratic Derivations & Direct Intuition",
                keyTakeaways = listOf(
                    "Ask by typing or voice query.",
                    "Get step-by-step digital chalkboard solutions.",
                    "Listen to teacher voice explanations."
                ),
                followUpPrompts = listOf(
                    "Explain Moment of Inertia from scratch",
                    "How to distinguish SN1 vs SN2 easily?",
                    "Shortcut for King's Rule in Calculus"
                )
            )
        )
    }

    val listState = rememberLazyListState()

    DisposableEffect(Unit) {
        onDispose {
            voiceManager.shutdown()
        }
    }

    fun handleSendQuestion(doubtText: String) {
        if (doubtText.isBlank() || isLoadingResponse) return
        val currentDoubt = doubtText.trim()
        inputText = ""

        messages.add(
            TeacherDoubtMessage(
                id = "student_${System.currentTimeMillis()}",
                sender = "STUDENT",
                message = currentDoubt
            )
        )

        isLoadingResponse = true
        coroutineScope.launch {
            val response = GeminiTeacherService.askTeacherDoubt(
                teacher = activeTeacher,
                studentDoubt = currentDoubt,
                chatHistory = messages.toList()
            )

            val teacherMsg = TeacherDoubtMessage(
                id = "teacher_${System.currentTimeMillis()}",
                sender = "TEACHER",
                message = response.teacherSpeech,
                chalkboardEquation = response.chalkboardContent,
                keyTakeaways = response.keyTakeaways,
                followUpPrompts = response.followUpPrompts
            )
            messages.add(teacherMsg)
            isLoadingResponse = false

            // Auto speak teacher response
            voiceManager.speak(
                text = response.teacherSpeech,
                pitch = activeTeacher.voicePitch,
                speed = activeTeacher.voiceSpeed
            )

            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showTeacherPicker = true }
                    ) {
                        Image(
                            painter = painterResource(id = activeTeacher.getTeacherImageRes()),
                            contentDescription = activeTeacher.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, ChalkYellow, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = activeTeacher.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = "Switch Teacher",
                                    tint = ChalkYellow,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Active • ${activeTeacher.subject} Master",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isSpeaking) voiceManager.stopSpeaking()
                            else {
                                val lastTeacherMsg = messages.lastOrNull { it.sender == "TEACHER" }
                                if (lastTeacherMsg != null) {
                                    voiceManager.speak(lastTeacherMsg.message, activeTeacher.voicePitch, activeTeacher.voiceSpeed)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = "Voice Speech",
                            tint = if (isSpeaking) Color(0xFFFF8A80) else ChalkYellow
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IndigoPrimary
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Follow-up suggestion chips
                    val lastTeacherMsg = messages.lastOrNull { it.sender == "TEACHER" }
                    if (lastTeacherMsg != null && lastTeacherMsg.followUpPrompts.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 6.dp)
                        ) {
                            items(lastTeacherMsg.followUpPrompts) { prompt ->
                                SuggestionChip(
                                    onClick = { handleSendQuestion(prompt) },
                                    label = { Text(prompt, fontSize = 12.sp, maxLines = 1) },
                                    modifier = Modifier.testTag("prompt_chip_${prompt.take(10)}")
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Speech to Text Mic Button
                        IconButton(
                            onClick = {
                                if (isListening) {
                                    voiceManager.stopListening()
                                } else {
                                    voiceManager.startListening { voiceResult ->
                                        inputText = voiceResult
                                        handleSendQuestion(voiceResult)
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    if (isListening) Color(0xFFC62828) else IndigoSecondary.copy(alpha = 0.15f),
                                    CircleShape
                                )
                                .testTag("voice_mic_button")
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "Voice Input",
                                tint = if (isListening) Color.White else IndigoSecondary
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Ask ${activeTeacher.name} a doubt...", fontSize = 13.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("doubt_input_field"),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoSecondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { handleSendQuestion(inputText) },
                            enabled = inputText.isNotBlank() && !isLoadingResponse,
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    if (inputText.isNotBlank() && !isLoadingResponse) IndigoPrimary else Color.LightGray,
                                    CircleShape
                                )
                                .testTag("send_doubt_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send Doubt",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { msg ->
                if (msg.sender == "STUDENT") {
                    StudentChatBubble(message = msg.message)
                } else {
                    TeacherChatBubble(
                        teacher = activeTeacher,
                        message = msg,
                        onSpeak = {
                            voiceManager.speak(msg.message, activeTeacher.voicePitch, activeTeacher.voiceSpeed)
                        }
                    )
                }
            }

            if (isLoadingResponse) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = IndigoSecondary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${activeTeacher.name} is formulating the chalk derivation...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // Faculty Picker Bottom Sheet
    if (showTeacherPicker) {
        ModalBottomSheet(
            onDismissRequest = { showTeacherPicker = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Select Your AI Teacher / Faculty",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = IndigoPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                FacultyRepository.defaultFacultyList.forEach { fac ->
                    val isSelected = fac.id == activeTeacher.id
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) IndigoSecondary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, IndigoSecondary) else null,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                activeTeacher = fac
                                showTeacherPicker = false
                                messages.add(
                                    TeacherDoubtMessage(
                                        id = "switch_${System.currentTimeMillis()}",
                                        sender = "TEACHER",
                                        message = "Hello! I am ${fac.name}. I am now ready to guide you through ${fac.subject} concepts. What would you like to explore?"
                                    )
                                )
                            }
                            .testTag("faculty_item_${fac.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = fac.getTeacherImageRes()),
                                contentDescription = fac.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = fac.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${fac.subject} • ${fac.expertise}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "⭐ ${fac.rating} • ${fac.doubtsSolvedCount} solved",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StudentChatBubble(message: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp).copy(bottomEnd = androidx.compose.foundation.shape.CornerSize(2.dp)),
            color = IndigoPrimary,
            modifier = Modifier.padding(start = 48.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
fun TeacherChatBubble(
    teacher: AITeacher,
    message: TeacherDoubtMessage,
    onSpeak: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = teacher.getTeacherImageRes()),
            contentDescription = teacher.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(1.dp, IndigoSecondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = teacher.name,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = IndigoPrimary
                )
                IconButton(
                    onClick = onSpeak,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Listen",
                        tint = IndigoSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(18.dp).copy(topStart = androidx.compose.foundation.shape.CornerSize(2.dp)),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = message.message,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )

                    // Chalkboard Equation
                    if (!message.chalkboardEquation.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ChalkboardGreen,
                            border = androidx.compose.foundation.BorderStroke(2.dp, ChalkboardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "★ CHALKBOARD DERIVATION ★",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChalkCyan
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = message.chalkboardEquation,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = ChalkWhite,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // Key Takeaways
                    if (message.keyTakeaways.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            message.keyTakeaways.forEach { takeaway ->
                                Row(verticalAlignment = Alignment.Top) {
                                    Text("💡 ", fontSize = 12.sp)
                                    Text(
                                        text = takeaway,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

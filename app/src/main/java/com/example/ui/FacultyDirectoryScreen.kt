package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AITeacher
import com.example.model.FacultyRepository
import com.example.ui.theme.AccentGold
import com.example.ui.theme.ChalkYellow
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyDirectoryScreen(
    onSelectTeacher: (AITeacher) -> Unit,
    onStartLiveClass: (AITeacher) -> Unit,
    modifier: Modifier = Modifier
) {
    val facultyList = remember { mutableStateListOf(*FacultyRepository.defaultFacultyList.toTypedArray()) }
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AI Faculty & Mentors",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "World-Class Socratic Teachers • Google Gemini Flow",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IndigoPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = AccentGold,
                contentColor = Color.Black,
                modifier = Modifier.testTag("create_custom_teacher_fab")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create Guru")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New AI Guru", fontWeight = FontWeight.Bold)
                }
            }
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
            item {
                Text(
                    text = "Distinguished National Faculty",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = IndigoPrimary
                )
                Text(
                    text = "Each AI teacher features photorealistic human demeanor, deep subject pedagogy, audio speech, and real-time blackboard derivations.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(facultyList) { teacher ->
                FacultyProfileCard(
                    teacher = teacher,
                    onAskDoubt = { onSelectTeacher(teacher) },
                    onStartLiveClass = { onStartLiveClass(teacher) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }

    if (showCreateDialog) {
        var customName by remember { mutableStateOf("") }
        var customSubject by remember { mutableStateOf("") }
        var customExpertise by remember { mutableStateOf("") }
        var customStyle by remember { mutableStateOf("Encouraging & Intuitive") }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = {
                Text("Create Custom AI Teacher", fontWeight = FontWeight.Bold, color = IndigoPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Teacher Name (e.g. Prof. Arvind Kumar)") },
                        modifier = Modifier.fillMaxWidth().testTag("custom_teacher_name_input")
                    )
                    OutlinedTextField(
                        value = customSubject,
                        onValueChange = { customSubject = it },
                        label = { Text("Subject (e.g. Quantum Physics, Vedic Math)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = customExpertise,
                        onValueChange = { customExpertise = it },
                        label = { Text("Specialty / Topics") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = customStyle,
                        onValueChange = { customStyle = it },
                        label = { Text("Teaching Style & Philosophy") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customName.isNotBlank() && customSubject.isNotBlank()) {
                            val newTeacher = AITeacher(
                                id = "custom_${System.currentTimeMillis()}",
                                name = customName.trim(),
                                title = "Custom AI Specialist",
                                subject = customSubject.trim(),
                                expertise = if (customExpertise.isNotBlank()) customExpertise.trim() else "Core Fundamentals",
                                teachingStyle = customStyle.trim(),
                                personality = "Supportive, knowledgeable, Socratic mentor.",
                                language = "Hinglish / English",
                                difficultyLevel = "Adaptive",
                                curriculum = "CBSE & Competitive Exams",
                                teachingMethodology = "Intuition -> Practice",
                                voiceName = "Adaptive Mentor Voice",
                                avatarColorHex = 0xFF0D47A1,
                                systemInstructions = "You are $customName, an expert teacher in $customSubject. Teach step-by-step with clear chalk formulas.",
                                avatarDrawableRes = com.example.R.drawable.img_teacher_physics,
                                isCustom = true
                            )
                            facultyList.add(newTeacher)
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.testTag("save_custom_teacher_btn")
                ) {
                    Text("Create Teacher")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun FacultyProfileCard(
    teacher: AITeacher,
    onAskDoubt: () -> Unit,
    onStartLiveClass: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = teacher.getTeacherImageRes()),
                    contentDescription = teacher.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, IndigoSecondary, CircleShape)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = teacher.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = IndigoPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = AccentGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${teacher.rating}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Text(
                        text = teacher.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = teacher.qualifications,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "“${teacher.teachingMotto}”",
                style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                color = IndigoSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "📚 Specialties: ${teacher.expertise}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onStartLiveClass,
                    modifier = Modifier.weight(1f).testTag("faculty_live_class_${teacher.id}")
                ) {
                    Icon(imageVector = Icons.Default.PlayCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live Lecture", fontSize = 12.sp)
                }

                Button(
                    onClick = onAskDoubt,
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.weight(1f).testTag("faculty_ask_doubt_${teacher.id}")
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ask 1-on-1", fontSize = 12.sp)
                }
            }
        }
    }
}

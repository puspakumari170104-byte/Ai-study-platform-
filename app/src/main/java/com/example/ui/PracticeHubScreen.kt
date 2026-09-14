package com.example.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MistakeType
import com.example.model.PracticeQuestion
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
fun PracticeHubScreen(
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

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Daily DPP Practice", "Formula Vault", "Mistake Notebook")

    val sampleQuestions = remember {
        listOf(
            PracticeQuestion(
                id = "q_rot_1",
                programId = "jee",
                subjectId = "Physics",
                chapterTitle = "Rotational Motion",
                questionText = "A thin uniform circular disc of mass M and radius R rotates about its central perpendicular axis. What is its moment of inertia about a tangent in its plane?",
                options = listOf(
                    "¼ M R²",
                    "½ M R²",
                    "⁵/₄ M R²",
                    "⁷/₄ M R²"
                ),
                correctOptionIndex = 2,
                socraticHint = "First find I about diameter in plane using perpendicular axis theorem, then apply parallel axis theorem with distance d = R.",
                deepExplanation = "1. About central perpendicular axis: I_z = ½ M R².\n2. By Perpendicular Axis Theorem: I_x + I_y = I_z ⇒ 2 I_diameter = ½ M R² ⇒ I_dia = ¼ M R².\n3. By Parallel Axis Theorem for tangent in plane: I_tangent = I_dia + M R² = ¼ M R² + M R² = ⁵/₄ M R².",
                formulaUsed = "I_tangent = I_dia + M·R² = 5/4 M·R²",
                difficulty = "Moderate",
                defaultMistakeType = MistakeType.CONCEPTUAL
            ),
            PracticeQuestion(
                id = "q_chem_1",
                programId = "jee",
                subjectId = "Chemistry",
                chapterTitle = "Chemical Bonding & Molecular Structure",
                questionText = "Which among the following molecules exhibits a zero dipole moment despite containing polar covalent bonds?",
                options = listOf(
                    "NH₃ (Ammonia)",
                    "BF₃ (Boron Trifluoride)",
                    "SO₂ (Sulfur Dioxide)",
                    "H₂O (Water)"
                ),
                correctOptionIndex = 1,
                socraticHint = "Look for symmetrical planar geometry where bond dipole vectors cancel each other completely.",
                deepExplanation = "In BF₃, the geometry is Trigonal Planar (sp² hybridization) with 120° bond angles. The three identical B-F bond dipole moments cancel out vectorially to give net dipole moment μ = 0.",
                formulaUsed = "Vector sum: μ_net = ∑ μ_i = 0 (Trigonal Planar)",
                difficulty = "Foundation",
                defaultMistakeType = MistakeType.MEMORY
            ),
            PracticeQuestion(
                id = "q_math_1",
                programId = "jee",
                subjectId = "Mathematics",
                chapterTitle = "Definite Integrals",
                questionText = "Evaluate the definite integral: I = ∫[0 to π/2] (sin⁴ x) / (sin⁴ x + cos⁴ x) dx",
                options = listOf(
                    "π / 4",
                    "π / 2",
                    "π / 8",
                    "1"
                ),
                correctOptionIndex = 0,
                socraticHint = "Apply King's Property: replace x with (0 + π/2 - x) and add the two equations together.",
                deepExplanation = "Using King's Property: I = ∫[0 to π/2] (cos⁴ x) / (cos⁴ x + sin⁴ x) dx.\nAdding both equations: 2I = ∫[0 to π/2] 1 dx = π/2 ⇒ I = π/4.",
                formulaUsed = "∫[0 to a] f(x) dx = ∫[0 to a] f(a - x) dx",
                difficulty = "Moderate",
                defaultMistakeType = MistakeType.CALCULATION
            )
        )
    }

    val selectedAnswers = remember { mutableStateMapOf<String, Int>() }
    val revealedExplanations = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Practice & Mastery Engine",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndigoPrimary)
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = IndigoPrimary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> {
                    // Daily DPP
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Today's High-Yield Problem Set",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = IndigoPrimary
                            )
                        }

                        itemsIndexed(sampleQuestions) { index, q ->
                            val userAns = selectedAnswers[q.id]
                            val isRevealed = revealedExplanations[q.id] ?: false

                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Q${index + 1} • ${q.subjectId} (${q.chapterTitle})",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = IndigoSecondary
                                        )
                                        Text(
                                            text = q.difficulty,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = q.questionText,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    q.options.forEachIndexed { optIndex, option ->
                                        val isSelected = userAns == optIndex
                                        val isCorrect = q.correctOptionIndex == optIndex

                                        val bgColor = when {
                                            userAns == null -> if (isSelected) IndigoSecondary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                                            isCorrect -> Color(0xFFE8F5E9)
                                            isSelected -> Color(0xFFFFEBEE)
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = bgColor,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 3.dp)
                                                .clickable(enabled = userAns == null) {
                                                    selectedAnswers[q.id] = optIndex
                                                    revealedExplanations[q.id] = true
                                                    if (optIndex == q.correctOptionIndex) {
                                                        voiceManager.speak("Correct! ${q.deepExplanation}")
                                                    } else {
                                                        voiceManager.speak("Incorrect. Let's inspect the derivation: ${q.deepExplanation}")
                                                    }
                                                }
                                                .testTag("dpp_q${index}_opt$optIndex")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("${('A' + optIndex)}. ", fontWeight = FontWeight.Bold)
                                                Text(option, modifier = Modifier.weight(1f), fontSize = 13.sp)
                                                if (userAns != null && isCorrect) {
                                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF2E7D32))
                                                } else if (userAns != null && isSelected) {
                                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Incorrect", tint = Color(0xFFC62828))
                                                }
                                            }
                                        }
                                    }

                                    AnimatedVisibility(visible = isRevealed) {
                                        Column(modifier = Modifier.padding(top = 12.dp)) {
                                            Surface(
                                                color = ChalkboardGreen,
                                                shape = RoundedCornerShape(10.dp),
                                                border = androidx.compose.foundation.BorderStroke(1.5.dp, ChalkboardBorder),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(12.dp)) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Text(
                                                            text = "⚡ Chalkboard Solution",
                                                            fontFamily = FontFamily.Monospace,
                                                            fontWeight = FontWeight.Bold,
                                                            color = ChalkYellow,
                                                            fontSize = 11.sp
                                                        )
                                                        IconButton(
                                                            onClick = { voiceManager.speak(q.deepExplanation) },
                                                            modifier = Modifier.size(22.dp)
                                                        ) {
                                                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Audio", tint = ChalkCyan, modifier = Modifier.size(16.dp))
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = q.deepExplanation,
                                                        fontFamily = FontFamily.Monospace,
                                                        color = ChalkWhite,
                                                        fontSize = 12.sp,
                                                        lineHeight = 18.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
                1 -> {
                    // Formula Vault
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Formula & Quick Revision Sheets",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = IndigoPrimary
                            )
                        }

                        val formulas = listOf(
                            Pair("Parallel Axis Theorem", "I = I_com + M · d²  (Steiner's Law)"),
                            Pair("King's Rule in Integrals", "∫[a to b] f(x)dx = ∫[a to b] f(a+b-x)dx"),
                            Pair("SN2 Rate Law", "Rate = k · [R-X] · [Nu⁻]  (Walden Inversion)"),
                            Pair("Bohr's Angular Momentum", "L = m · v · r = (n · h) / (2 · π)"),
                            Pair("Carnot Efficiency", "η = 1 - (T_cold / T_hot)")
                        )

                        items(formulas.size) { i ->
                            val f = formulas[i]
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(f.first, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = IndigoPrimary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(f.second, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                                    }
                                    IconButton(onClick = { voiceManager.speak("${f.first}: ${f.second}") }) {
                                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Voice", tint = IndigoSecondary)
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Mistake Notebook
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "AI Remediation & Error Notebook",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = IndigoPrimary
                            )
                            Text(
                                text = "Every error is analyzed into Conceptual Gaps, Calculation Slips, or Examiner Traps.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        val mistakeItems = listOf(
                            Triple("Calculus: King's Rule Lower Bound", MistakeType.CALCULATION, "Forgot to check if lower bound was 0 or negative."),
                            Triple("Rotational: Tangent in-plane vs Perp-plane", MistakeType.CONCEPTUAL, "Used central perpendicular axis instead of in-plane diameter as starting axis."),
                            Triple("Chemistry: Solvation in Polar Aprotic", MistakeType.MEMORY, "Mixed up acetone polar aprotic with ethanol protic.")
                        )

                        items(mistakeItems.size) { i ->
                            val item = mistakeItems[i]
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(item.second.badgeColorHex).copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(item.first, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Surface(
                                            color = Color(item.second.badgeColorHex).copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = item.second.displayName,
                                                color = Color(item.second.badgeColorHex),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(item.third, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("💡 Remedy: ${item.second.advice}", fontSize = 11.sp, color = IndigoSecondary, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

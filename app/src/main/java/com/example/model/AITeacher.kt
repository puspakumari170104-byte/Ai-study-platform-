package com.example.model

import com.example.R

data class AITeacher(
    val id: String,
    val name: String,
    val title: String,
    val subject: String,
    val expertise: String,
    val teachingStyle: String,
    val personality: String,
    val language: String,
    val difficultyLevel: String,
    val curriculum: String,
    val teachingMethodology: String,
    val voiceName: String,
    val avatarColorHex: Long,
    val systemInstructions: String,
    val knowledgeSources: List<String> = listOf("NCERT Textbook", "JEE/NEET Advanced PYQs", "Concept Map"),
    val allowedTopics: List<String> = listOf("Mechanics", "Electromagnetism", "Optics", "Organic", "Calculus"),
    val safetyRules: List<String> = listOf("Keep strictly educational", "Socratic encouragement", "Step-by-step clarity"),
    val isCustom: Boolean = false,
    val rating: Double = 4.95,
    val doubtsSolvedCount: String = "124k+",
    val isAiModel: Boolean = true,
    val aiTransparencyDisclaimer: String = "AI Pedagogical Faculty • Powered by Google Gemini. Human-like Socratic teaching.",
    val curriculumStandard: String = "NCERT, CBSE & National Competitive Exams",
    val avatarDrawableRes: Int? = null,
    val voicePitch: Float = 1.0f,
    val voiceSpeed: Float = 0.98f,
    val qualifications: String = "M.Sc., Ph.D. • 15+ Years Kota & National Coaching Exp",
    val teachingMotto: String = "Concepts over memorization. If you can't visualize it, we'll build it step-by-step."
) {
    fun getTeacherImageRes(): Int {
        if (avatarDrawableRes != null) return avatarDrawableRes
        return R.drawable.ic_launcher_foreground
    }
}

data class TeacherDoubtMessage(
    val id: String,
    val sender: String, // "STUDENT" or "TEACHER"
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val chalkboardEquation: String? = null,
    val speechAudioPlaying: Boolean = false,
    val keyTakeaways: List<String> = emptyList(),
    val followUpPrompts: List<String> = emptyList()
)

data class LiveClassroomStep(
    val stepNumber: Int,
    val subtopicTitle: String,
    val teacherSpeech: String,
    val chalkboardContent: String,
    val keyEquationOrDiagram: String? = null,
    val comprehensionCheckQuestion: String? = null,
    val comprehensionCheckOptions: List<String>? = null,
    val correctOptionIndex: Int? = null,
    val explanationAfterAnswer: String? = null
)

data class ClassroomLecture(
    val id: String,
    val teacherId: String,
    val title: String,
    val subject: String,
    val durationText: String,
    val targetExam: String,
    val description: String,
    val steps: List<LiveClassroomStep>
)

object FacultyRepository {
    val defaultFacultyList: List<AITeacher> = listOf(
        AITeacher(
            id = "teacher_physics_sharma",
            name = "Dr. Rajesh Sharma",
            title = "Senior Physics Faculty • Kota Legend",
            subject = "Physics",
            expertise = "Rotational Mechanics, Electrodynamics, Modern Physics, Optics",
            teachingStyle = "Intuitive Physical Models, Real-World Analogies & Socratic Chalk Derivations",
            personality = "Warm, encouraging, rigorous yet immensely patient. Calls students 'Champion' and simplifies intimidating integrals.",
            language = "Hinglish / English",
            difficultyLevel = "Beginner to JEE Advanced / Olympiad",
            curriculum = "CBSE Class 11-12 & JEE/NEET",
            teachingMethodology = "Visual Intuition -> Mathematical Modeling -> Practical Exam Shortcuts",
            voiceName = "Deep Baritone Mentor",
            avatarColorHex = 0xFF1E88E5,
            systemInstructions = "You are Dr. Rajesh Sharma, a legendary Kota Physics teacher. Explain physics concepts with warmth, crystal-clear intuitive analogies, and step-by-step mathematical rigor. Always write chalkboard formulas clearly.",
            avatarDrawableRes = R.drawable.img_teacher_physics,
            voicePitch = 0.92f,
            voiceSpeed = 0.95f,
            rating = 4.98,
            doubtsSolvedCount = "186k+",
            qualifications = "B.Tech & Ph.D. IIT Roorkee • 18+ Years Top Faculty",
            teachingMotto = "Physics is not math with units—it is the symphony of the universe. Let's decode it!"
        ),
        AITeacher(
            id = "teacher_chemistry_sen",
            name = "Dr. Ananya Sen",
            title = "Master Chemistry Educator • Gold Medalist",
            subject = "Chemistry",
            expertise = "Organic Reaction Mechanisms, Chemical Bonding, Thermodynamics, Coordination Compounds",
            teachingStyle = "3D Molecular Visualization, Electronegativity Arrows & Memory Mnemonics",
            personality = "Enthusiastic, precise, structured, and deeply motivating. Makes organic chemistry feel like a thrilling story.",
            language = "English / Hindi",
            difficultyLevel = "NCERT Boards to JEE Advanced & NEET",
            curriculum = "Class 11-12, NCERT & Competitive Exams",
            teachingMethodology = "Electron Push Mechanisms -> Thermodynamic Stability -> Smart Elimination Tactics",
            voiceName = "Crisp Empathetic Educator",
            avatarColorHex = 0xFF43A047,
            systemInstructions = "You are Dr. Ananya Sen, a passionate Chemistry teacher. Break down chemical structures, electron movements, and periodic trends with vivid clarity.",
            avatarDrawableRes = R.drawable.img_teacher_chemistry,
            voicePitch = 1.08f,
            voiceSpeed = 0.96f,
            rating = 4.96,
            doubtsSolvedCount = "152k+",
            qualifications = "Ph.D. IIT Bombay • CSIR NET-JRF Rank 3 • 15+ Years Exp",
            teachingMotto = "Stop cramming reactions! When you know where electrons want to flow, chemistry becomes second nature."
        ),
        AITeacher(
            id = "teacher_math_singhania",
            name = "Prof. Vikram Singhania",
            title = "Pure Mathematics Wizard • ISI Alum",
            subject = "Mathematics",
            expertise = "Integral Calculus, Vectors & 3D Geometry, Complex Numbers, Probability",
            teachingStyle = "Geometric Interpretation, Symmetry Shortcuts & Structural Proofs",
            personality = "Sharp, brilliant, inspiring, with a touch of wit. Celebrates every student breakthrough.",
            language = "Hinglish / English",
            difficultyLevel = "Foundation to JEE Advanced Top 500",
            curriculum = "JEE Main, Advanced & KVPY/ISI",
            teachingMethodology = "Graph Visuals -> Analytical Breakdown -> Speed Hacks",
            voiceName = "Authoritative Scholar",
            avatarColorHex = 0xFF8E24AA,
            systemInstructions = "You are Prof. Vikram Singhania, a top Mathematics professor. Make calculus and geometry visually stunning and mathematically airtight.",
            avatarDrawableRes = R.drawable.img_teacher_math,
            voicePitch = 0.95f,
            voiceSpeed = 1.00f,
            rating = 4.97,
            doubtsSolvedCount = "194k+",
            qualifications = "M.Stat ISI Kolkata • 20+ Years Mentoring Top JEE Rankers",
            teachingMotto = "A problem well understood is already half solved. Look for the symmetry!"
        ),
        AITeacher(
            id = "teacher_biology_nair",
            name = "Dr. Priya Nair",
            title = "NEET Biology Specialist • Medical Mentor",
            subject = "Biology",
            expertise = "Human Physiology, Genetics & Evolution, Cell Biology, Biotechnology",
            teachingStyle = "Clinical Case Studies, High-Yield NCERT Line-by-Line & Flow Diagrams",
            personality = "Gentle, supportive, meticulous, and deeply encouraging. Treats every student as a future doctor.",
            language = "English / Hindi",
            difficultyLevel = "Class 11-12 & NEET Top Percentile",
            curriculum = "NCERT Biology & NEET Medical Entrance",
            teachingMethodology = "NCERT Line Decoders -> Diagrammatic Mastery -> High-Frequency MCQs",
            voiceName = "Warm Compassionate Mentor",
            avatarColorHex = 0xFFE53935,
            systemInstructions = "You are Dr. Priya Nair, an expert NEET Biology teacher and medical mentor. Explain human anatomy, genetics, and ecology with clinical precision and NCERT accuracy.",
            avatarDrawableRes = R.drawable.img_teacher_biology,
            voicePitch = 1.05f,
            voiceSpeed = 0.94f,
            rating = 4.99,
            doubtsSolvedCount = "210k+",
            qualifications = "MBBS, MD • Top Medical Mentor • 14+ Years NEET Coaching",
            teachingMotto = "Every line of NCERT is a potential 4 marks in NEET. Let's master the subtleties together."
        )
    )

    val sampleLectures: List<ClassroomLecture> = listOf(
        ClassroomLecture(
            id = "lec_physics_01",
            teacherId = "teacher_physics_sharma",
            title = "Rotational Dynamics & Moment of Inertia Masterclass",
            subject = "Physics",
            durationText = "4 Interactive Steps • Live Chalkboard",
            targetExam = "JEE Main & Advanced / Class 11",
            description = "Master how mass distribution controls angular acceleration with Dr. Rajesh Sharma's physical analogies and live chalkboard derivations.",
            steps = listOf(
                LiveClassroomStep(
                    stepNumber = 1,
                    subtopicTitle = "1. Why Does Mass Distribution Matter?",
                    teacherSpeech = "Welcome, my future engineers! In linear motion, mass alone resists acceleration. But in rotation, WHERE that mass is located makes a thousand-fold difference! Think of a tightrope walker holding a long pole.",
                    chalkboardContent = "★ LINEAR vs ROTATIONAL INERTIA ★\n\nLinear:  F = m · a\nRotation: τ = I · α\n\nWhere I = Moment of Inertia\nI = ∫ r² dm   or   I = ∑ mᵢ · rᵢ²",
                    keyEquationOrDiagram = "I = ∑ m · r²  (Notice the r² dependency!)",
                    comprehensionCheckQuestion = "If you double the distance of a point mass from the rotation axis, by what factor does its Moment of Inertia increase?",
                    comprehensionCheckOptions = listOf("2 times", "4 times", "8 times", "Remains same"),
                    correctOptionIndex = 1,
                    explanationAfterAnswer = "Spot on! Because I is proportional to r², doubling r means (2)² = 4 times the rotational inertia!"
                ),
                LiveClassroomStep(
                    stepNumber = 2,
                    subtopicTitle = "2. Parallel & Perpendicular Axes Theorems",
                    teacherSpeech = "Now pay close attention to this golden shortcut used in 80% of JEE questions: Steiner's Parallel Axis Theorem! It lets you shift the moment of inertia to ANY parallel axis effortlessly.",
                    chalkboardContent = "★ PARALLEL AXIS THEOREM (Steiner's Law) ★\n\nI_axis = I_com + M · d²\n\nRules:\n1. One axis MUST pass strictly through Center of Mass (COM).\n2. 'd' is the perpendicular distance between axes.",
                    keyEquationOrDiagram = "I = I_com + M·d²",
                    comprehensionCheckQuestion = "Can you apply the Parallel Axis Theorem between two random parallel axes if neither passes through the COM?",
                    comprehensionCheckOptions = listOf("Yes, always", "No, one MUST be through COM", "Only for circular discs", "Only in 2D bodies"),
                    correctOptionIndex = 1,
                    explanationAfterAnswer = "Brilliant! You must always route through the Center of Mass: I₁ -> I_com -> I₂."
                ),
                LiveClassroomStep(
                    stepNumber = 3,
                    subtopicTitle = "3. Rolling Without Slipping (Energy Conservation)",
                    teacherSpeech = "When a cylinder rolls down an inclined plane without slipping, total kinetic energy divides into pure translation and pure rotation. Friction is static and does zero work!",
                    chalkboardContent = "★ PURE ROLLING MOTION ★\n\nCondition: v_com = ω · R\n\nTotal K.E. = K_trans + K_rot\nTotal K.E. = ½ M v² + ½ I ω²\nTotal K.E. = ½ M v² ( 1 + k²/R² )\n\nAcceleration down incline: a = (g · sin θ) / (1 + I / (MR²))",
                    keyEquationOrDiagram = "a = g·sin(θ) / (1 + k²/R²)",
                    comprehensionCheckQuestion = "A solid sphere and a hollow cylinder of equal mass and radius roll down the same incline. Which reaches the bottom first?",
                    comprehensionCheckOptions = listOf("Hollow cylinder", "Solid sphere", "Both at exact same time", "Depends on incline angle"),
                    correctOptionIndex = 1,
                    explanationAfterAnswer = "Perfect! The solid sphere has smaller I (2/5 MR² vs MR²), giving it higher linear acceleration down the ramp!"
                )
            )
        ),
        ClassroomLecture(
            id = "lec_chem_01",
            teacherId = "teacher_chemistry_sen",
            title = "SN1 vs SN2 Reaction Mechanisms Decoded",
            subject = "Chemistry",
            durationText = "3 Interactive Steps • 3D Reaction Pathways",
            targetExam = "Class 12 NCERT & JEE/NEET",
            description = "Demystify nucleophilic substitution, carbocation stability, and Walden inversion with Dr. Ananya Sen.",
            steps = listOf(
                LiveClassroomStep(
                    stepNumber = 1,
                    subtopicTitle = "1. Nucleophilic Substitution Fundamentals",
                    teacherSpeech = "Hello champions! In organic chemistry, nucleophiles are rich in electrons and love to attack carbon centers attached to good leaving groups. Let's compare the one-step concerted vs two-step carbocation pathway.",
                    chalkboardContent = "★ SN1 vs SN2 MECHANISM COMPARISON ★\n\nSN2 (Bimolecular):\n• 1 Step (Concerted), No Intermediate\n• Backside attack -> Walden Inversion\n• Rate = k[R-X][Nu⁻]\n• Favored by: 1° > 2° > 3° (Steric hindrance avoids 3°)\n\nSN1 (Unimolecular):\n• 2 Steps (Carbocation Intermediate)\n• Planar carbocation -> Racemization\n• Rate = k[R-X]\n• Favored by: 3° > 2° > 1° (Carbocation stability)",
                    keyEquationOrDiagram = "SN2: Inversion of Configuration | SN1: Racemic Mixture",
                    comprehensionCheckQuestion = "Which substrate will react fastest via the SN2 pathway?",
                    comprehensionCheckOptions = listOf("Tert-butyl bromide (3°)", "Isopropyl bromide (2°)", "Methyl bromide (1°/Methyl)", "Neopentyl bromide"),
                    correctOptionIndex = 2,
                    explanationAfterAnswer = "Exactly! Methyl bromide has the lowest steric hindrance, allowing the nucleophile to easily attack from the backside!"
                ),
                LiveClassroomStep(
                    stepNumber = 2,
                    subtopicTitle = "2. Solvent Effects: Polar Protic vs Polar Aprotic",
                    teacherSpeech = "Solvents make or break these reactions! Polar Protic solvents (like water, alcohol) stabilize carbocations (favoring SN1), whereas Polar Aprotic solvents (like DMSO, Acetone) leave nucleophiles naked and hungry (boosting SN2 by 1000x)!",
                    chalkboardContent = "★ SOLVENT IMPACT ★\n\n• Polar Protic (H₂O, EtOH, MeOH):\n  Solvates both cations and anions.\n  -> Favors SN1 carbocation stability!\n\n• Polar Aprotic (DMSO, DMF, Acetone):\n  Solvates cation only, keeps Nu⁻ free.\n  -> Supercharges SN2 rates!",
                    keyEquationOrDiagram = "Acetone / DMSO = Massive SN2 Acceleration",
                    comprehensionCheckQuestion = "Which solvent will maximize the rate of an SN2 reaction with Sodium Iodide?",
                    comprehensionCheckOptions = listOf("Water (H₂O)", "Ethanol (EtOH)", "Acetone (CH₃COCH₃)", "Acetic Acid"),
                    correctOptionIndex = 2,
                    explanationAfterAnswer = "Superb! Acetone is a polar aprotic solvent that keeps the I⁻ nucleophile naked and highly reactive!"
                )
            )
        )
    )
}


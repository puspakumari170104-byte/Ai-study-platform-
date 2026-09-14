package com.example.model

enum class MistakeType(val displayName: String, val badgeColorHex: Long, val advice: String) {
    CONCEPTUAL("Conceptual Mistake", 0xFFEF4444, "Re-watch the AI Teacher's lecture breakdown and revise core principles from first principles."),
    CALCULATION("Calculation Mistake", 0xFFF59E0B, "Practice step-by-step scratchpad derivation. Watch unit conversions and sign rules."),
    MISREADING("Misreading", 0xFFEC4899, "Underline keywords like 'NOT', 'EXCEPT', and unit dimensions before answering."),
    MEMORY("Memory Mistake", 0xFF8B5CF6, "Add to active formula flashcards; review using spaced repetition schedules."),
    GUESS("Guess", 0xFF06B6D4, "Avoid blind negative marking slips. Eliminate 2 options first or mark for review."),
    TIME_PRESSURE("Time-Pressure Mistake", 0xFF3B82F6, "Practice pacing: skip lengthy questions on first pass and return with calm focus."),
    SILLY("Silly Mistake", 0xFFF97316, "Double check bubble/option matching and re-read the question stem before finalizing."),
    CONCEPTUAL_GAP("Conceptual Mistake", 0xFFEF4444, "Re-watch the AI Teacher's lecture breakdown and revise core principles."),
    CALCULATION_SLIP("Calculation Mistake", 0xFFF59E0B, "Practice step-by-step scratchpad derivation. Watch unit conversions."),
    FORMULA_RECALL("Memory Mistake", 0xFF8B5CF6, "Add to active formula flashcards; review using spaced repetition."),
    TRAP_QUESTION("Examiner Trap", 0xFFEC4899, "Identify negative markers, 'NOT' conditions, and tricky exception clauses.")
}

enum class QuestionType(val displayName: String) {
    SINGLE_MCQ("Single Choice MCQ"),
    MULTI_CORRECT("Multiple Correct"),
    NUMERICAL_VALUE("Numerical Value")
}

enum class ContentReviewStatus(val displayName: String, val badgeColorHex: Long) {
    AI_GENERATED_PENDING_REVIEW("AI Pending Review", 0xFFF59E0B),
    ADMIN_VERIFIED("Admin Verified", 0xFF3B82F6),
    PRODUCTION_TRUSTED("Production Trusted", 0xFF10B981),
    REJECTED("Rejected / Flagged", 0xFFEF4444)
}

enum class PracticeEngineMode(val displayName: String, val description: String) {
    TOPIC("Topic Practice", "Drill deep on specific micro-topics"),
    CHAPTER("Chapter Practice", "Comprehensive chapter-wide problem sets"),
    SUBJECT("Subject Practice", "Full subject breadth test"),
    MIXED("Mixed Practice", "Real exam multi-subject interleaving"),
    WEAK_TOPIC("Weak-Topic Practice", "Targeted practice on error patterns"),
    REVISION("Revision Practice", "Spaced repetition on previously solved topics"),
    DIFFICULTY_BASED("Difficulty Practice", "Filter by Foundation, Moderate, or Challenger"),
    AI_GENERATED("AI Generated", "Dynamic questions crafted by AI"),
    CUSTOM("Custom Practice", "Personalized question count, timer, and topics")
}

data class PracticeQuestion(
    val id: String,
    val programId: String,
    val subjectId: String,
    val chapterTitle: String,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val socraticHint: String,
    val deepExplanation: String,
    val formulaUsed: String,
    val difficulty: String, // "Foundation", "Moderate", "Challenger"
    val defaultMistakeType: MistakeType = MistakeType.CONCEPTUAL,
    val questionType: QuestionType = QuestionType.SINGLE_MCQ,
    val sourceMetadata: String = "NEET 2023 PYQ",
    val reviewStatus: ContentReviewStatus = ContentReviewStatus.PRODUCTION_TRUSTED,
    val lifecycleState: ContentLifecycleState = ContentLifecycleState.PUBLISHED,
    val sourceMetadataObj: ContentSourceMetadata? = null,
    val topicName: String = "General",
    val subjectName: String = "Physics"
)

data class MockTest(
    val id: String,
    val programId: String,
    val subjectId: String?,
    val title: String,
    val testType: String = "Chapter Test",
    val testCategory: TestType = TestType.CHAPTER,
    val totalQuestions: Int,
    val durationMinutes: Int,
    val positiveMarks: Int = 4,
    val negativeMarks: Int = 1,
    val sections: List<TestSection> = emptyList(),
    val questions: List<PracticeQuestion>
)

data class TestScoreSummary(
    val testId: String,
    val testTitle: String,
    val totalQuestions: Int,
    val attemptedCount: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val skippedCount: Int,
    val totalScore: Int,
    val maximumMarks: Int,
    val accuracyPercentage: Int,
    val predictedPercentile: Double,
    val predictedRank: String,
    val timeSpentSeconds: Int,
    val weakTopics: List<String>,
    val mistakeBreakdown: Map<MistakeType, Int>,
    val detailedAnalysis: DetailedTestAnalysis? = null
)

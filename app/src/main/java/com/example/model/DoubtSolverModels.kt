package com.example.model

enum class DoubtQueryType(val displayName: String) {
    TEXT("Text Question"),
    LESSON_BASED("Lesson Related"),
    TEST_BASED("Test Related"),
    MISTAKE_BASED("Mistake Book"),
    VOICE("Voice Query"),
    IMAGE("Image / Diagram"),
    LESSON_CONCEPT("Lesson Concept"),
    TEST_QUESTION("Test Question"),
    MISTAKE_CORRECTION("Mistake Correction")
}

data class DoubtContext(
    val programTitle: String = "NEET UG",
    val subjectName: String = "Biology",
    val chapterTitle: String = "Class 11 Biology – Cell: The Unit of Life",
    val topicTitle: String = "Mitochondria & ATP Generation",
    val gradeLevel: String = "Class 11 (NEET/Board Level)",
    val isAutoBound: Boolean = true,
    val currentChapter: String = chapterTitle,
    val currentTopic: String = topicTitle,
    val targetGradeOrLevel: String = gradeLevel,
    val relatedMistakeSnippet: String? = null
)

data class DoubtSolution(
    val id: String,
    val queryText: String = "",
    val queryType: DoubtQueryType = DoubtQueryType.TEXT,
    val imageDescription: String? = null,
    val audioTranscript: String? = null,
    val contextUsed: DoubtContext = DoubtContext(),
    val syllabusLevel: String = "Class 11 Standard Syllabus",
    val directAnswer: String,
    val stepByStepExplanation: List<String>,
    val keyFormulasOrRules: List<String> = emptyList(),
    val keyFormulas: List<String> = keyFormulasOrRules,
    val analogy: String? = null,
    val visualDiagramDescription: String? = null,
    val followUpCheckQuestion: String = "",
    val followUpOptions: List<String> = emptyList(),
    val followUpCorrectIndex: Int = 0,
    val checkUnderstandingQuestion: String = followUpCheckQuestion,
    val checkUnderstandingOptions: List<String> = followUpOptions,
    val correctCheckOptionIndex: Int = followUpCorrectIndex,
    val nextRecommendedLesson: String = "Class 11 Biology – Cell Structure & Function",
    val pedagogicalMethodUsed: String = "Step-by-step Socratic",
    val contextCalibratedTo: String = "Class 11 Level",
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = true
)

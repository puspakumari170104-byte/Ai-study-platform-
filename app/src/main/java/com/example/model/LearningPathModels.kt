package com.example.model

data class LessonExample(
    val title: String,
    val problemStatement: String,
    val stepByStepSolution: List<String>,
    val finalAnswer: String,
    val examTip: String
)

data class QuickQuizItem(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class LessonDetail(
    val id: String,
    val topicId: String,
    val programId: String,
    val subjectId: String,
    val chapterTitle: String,
    val topicTitle: String,
    val lessonNumber: Int,
    val estimatedMinutes: Int,
    val teacherExplanation: String,
    val textContent: String,
    val visualsDescription: String,
    val visualsType: String = "DIAGRAM",
    val examples: List<LessonExample>,
    val formulae: List<String>,
    val summary: List<String>,
    val quickQuiz: List<QuickQuizItem>,
    val practiceQuestions: List<PracticeQuestion>,
    val initialNotes: List<String> = emptyList(),
    val lifecycleState: ContentLifecycleState = ContentLifecycleState.PUBLISHED,
    val sourceMetadata: ContentSourceMetadata? = null
)

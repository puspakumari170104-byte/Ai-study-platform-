package com.example.model

data class AIMentorGuidance(
    val greeting: String,
    val dailyQuote: String,
    val streakDays: Int,
    val targetExamDaysLeft: Int,
    val primaryFocusSubject: String,
    val highYieldAlert: String,
    val suggestedPlan: List<DailyStudyTask>,
    val weakAreaWarning: String,
    val predictedPerformanceBadge: String
)

data class DailyStudyTask(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationMinutes: Int,
    val taskType: String, // "LIVE_CLASS", "PRACTICE_SET", "WEAKNESS_REVISION", "MOCK_TEST"
    val subjectName: String,
    val isCompleted: Boolean = false
)

data class MentorChatMessage(
    val id: String,
    val isMentor: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val actionSuggestion: String? = null
)

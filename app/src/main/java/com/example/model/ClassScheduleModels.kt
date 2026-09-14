package com.example.model

data class ScheduledAIClass(
    val id: String,
    val title: String,
    val subjectId: String,
    val subjectName: String,
    val topicTitle: String,
    val teacherId: String,
    val teacherName: String,
    val teacherSubject: String,
    val scheduledTime: String,
    val durationMinutes: Int = 45,
    val isLiveNow: Boolean = false,
    val hasReminderSet: Boolean = false,
    val isAiGenerated: Boolean = false,
    val difficulty: String = "NEET Target"
)

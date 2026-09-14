package com.example.model

data class LearningGoal(
    val targetExam: String = "NEET UG 2025",
    val targetScore: String = "680+ / 720",
    val targetRank: String = "Top 1,000 AIR",
    val dailyTargetMinutes: Int = 120,
    val targetDate: String = "May 2025"
)

data class StudentPreferences(
    val language: TeachingLanguage = TeachingLanguage.ENGLISH,
    val autoPlayVoice: Boolean = true,
    val showFormulaSheetDuringPractice: Boolean = true,
    val dailyNotificationReminder: Boolean = true,
    val reminderTime: String = "19:00"
)

data class ComprehensiveStudentProfile(
    val studentName: String = "Aman Kumar",
    val activeProgramTitle: String = "NEET UG (Medical Entrance)",
    val activeProgramId: String = "neet_ug",
    val learningGoal: LearningGoal = LearningGoal(),
    val preferences: StudentPreferences = StudentPreferences(),
    val subscriptionTier: SubscriptionTier = SubscriptionTier.PREMIUM,
    val totalStudyHours: Float = 58.0f,
    val syllabusCompletionPercentage: Int = 42,
    val totalMistakesTracked: Int = 8,
    val resolvedMistakesCount: Int = 5,
    val totalTestsAttempted: Int = 12,
    val averageTestAccuracy: Int = 78,
    val savedNotesCount: Int = 14,
    val currentStreak: Int = 14,
    val totalXp: Int = 1850
)

package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "user_default_1",
    val name: String = "Aman Kumar",
    val preferredLanguage: String = "HINDI",
    val subscriptionStatus: String = "FREE",
    val targetExam: String = "NEET UG",
    val classLevel: String = "Class 12",
    val targetYear: String = "2026",
    val email: String = "student@studyaistudio.in",
    val streakDays: Int = 14,
    val totalStudyMinutes: Int = 3480,
    val isRegistered: Boolean = true,
    val isInstitutePassActive: Boolean = false,
    val unlockedPlanName: String = "",
    val utrNumber: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val title: String,
    val examCategory: String,
    val description: String,
    val instructorName: String,
    val totalLectures: Int = 0,
    val totalDurationHours: Int = 0,
    val rating: Double = 4.9,
    val enrolledCount: Int = 1250,
    val isEnrolled: Boolean = false,
    val isProOnly: Boolean = false,
    val priceInr: Int = 0,
    val badge: String = "Popular",
    val iconName: String = "school",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "utr_payments")
data class UtrPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val utrNumber: String,
    val userId: String = "user_default_1",
    val userName: String = "Aman Kumar",
    val planId: String,
    val planName: String,
    val amountInr: Int,
    val upiIdUsed: String,
    val status: String = "VERIFIED_ACTIVE",
    val timestamp: Long = System.currentTimeMillis(),
    val remarks: String = "Instant UPI Pass Unlock"
)

@Entity(tableName = "mistakes")
data class MistakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val programId: String,
    val subjectId: String,
    val chapterTitle: String,
    val questionText: String,
    val studentAnswer: String,
    val correctAnswer: String,
    val explanation: String,
    val mistakeTypeName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false,
    val studentNotes: String = "",
    val revisionCount: Int = 0,
    val lastRevisedTimestamp: Long = 0L
)

@Entity(tableName = "test_records")
data class TestRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testId: String,
    val programId: String,
    val title: String,
    val score: Int,
    val totalMarks: Int,
    val accuracy: Int,
    val predictedPercentile: Double,
    val timeSpentSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "student_profile")
data class StudentProfileEntity(
    @PrimaryKey val id: Int = 1,
    val isRegistered: Boolean = true,
    val studentName: String = "Aman Kumar",
    val selectedLanguage: String = "ENGLISH",
    val activeProgramId: String = "neet_ug",
    val targetExam: String = "NEET UG",
    val classLevel: String = "Class 12",
    val targetYear: String = "2026",
    val dailyGoalMinutes: Int = 120,
    val completedMinutesToday: Int = 45,
    val streakDays: Int = 14,
    val totalStudyMinutes: Int = 3480,
    val isInstitutePassActive: Boolean = false,
    val unlockedPlanId: String = "",
    val unlockedPlanName: String = "",
    val utrNumber: String = "",
    val planUnlockedTimestamp: Long = 0L
)

@Entity(tableName = "bookmarked_notes")
data class BookmarkedNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val programId: String,
    val subjectName: String,
    val chapterTitle: String = "General",
    val title: String,
    val content: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = true,
    val highlightedPhrases: String = "",
    val personalAnnotation: String = ""
)

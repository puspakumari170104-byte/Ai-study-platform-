package com.example.model

data class StudyPlanParameters(
    val goal: String = "NEET",
    val examination: String = "NEET UG 2027",
    val targetDays: Int = 180,
    val currentLevel: String = "Intermediate",
    val availableHoursPerDay: Float = 5.0f,
    val preferredSchedule: String = "Balanced Morning & Evening",
    val testFrequency: String = "Weekly Full Mock",
    val weakSubjects: List<String> = listOf("Physics (Mechanics)")
)

data class DailyScheduleBlock(
    val timeSlot: String,
    val activityTitle: String,
    val category: String, // "LEARNING", "PRACTICE", "REVISION", "TEST"
    val durationMinutes: Int,
    val subject: String,
    val description: String
)

data class WeeklyMilestone(
    val weekNumber: Int,
    val theme: String,
    val physicsChapters: List<String>,
    val chemistryChapters: List<String>,
    val biologyChapters: List<String>,
    val practiceQuestionsTarget: Int,
    val scheduledMockTitle: String
)

data class ChapterSequenceItem(
    val sequenceNumber: Int,
    val subject: String,
    val chapterTitle: String,
    val prerequisiteChapter: String?,
    val highYieldWeightage: String,
    val estimatedDays: Int,
    val status: String = "Upcoming",
    val orderIndex: Int = sequenceNumber,
    val examWeightage: String = highYieldWeightage
)

data class PracticeScheduleSummary(
    val dailyQuestionTarget: Int,
    val weeklyQuestionTarget: Int,
    val weakTopicQuota: Int,
    val pyqQuota: Int
)

data class ScheduledTestItem(
    val testName: String,
    val scheduledDay: Int,
    val testType: String,
    val totalMarks: Int,
    val title: String = testName,
    val targetDay: Int = scheduledDay,
    val testFormat: String = testType,
    val durationMinutes: Int = 180
)

data class SpacedRevisionItem(
    val chapterName: String,
    val intervalDay: Int,
    val revisionType: String,
    val status: String,
    val chapterTitle: String = chapterName
)

data class PersonalizedStudyPlan(
    val id: String,
    val parameters: StudyPlanParameters,
    val dailyPlan: List<DailyScheduleBlock>,
    val weeklyPlan: List<WeeklyMilestone>,
    val chapterSequence: List<ChapterSequenceItem>,
    val practiceSchedule: PracticeScheduleSummary,
    val testSchedule: List<ScheduledTestItem>,
    val revisionSchedule: List<SpacedRevisionItem>,
    val isBehindSchedule: Boolean = false,
    val daysBehind: Int = 0,
    val recalculatedMessage: String? = null,
    val generatedDate: String = "Today"
)

package com.example.model

data class LearningProgram(
    val id: String,
    val title: String,
    val category: String, // "Medical Entrance", "Engineering", "Civil Services", "Tech & Coding", "Board Exams", "Custom Goal"
    val description: String,
    val iconName: String,
    val enrolledStudents: String,
    val targetExamYear: String,
    val subjects: List<Subject>
)

data class Subject(
    val id: String,
    val programId: String,
    val name: String,
    val tag: String,
    val colorHex: Long,
    val defaultTeacherId: String,
    val totalChapters: Int,
    val masteryPercentage: Int,
    val courses: List<Course> = emptyList()
)

data class Course(
    val id: String,
    val subjectId: String,
    val title: String,
    val description: String,
    val chapters: List<Chapter>
)

data class Chapter(
    val id: String,
    val courseId: String,
    val subjectId: String,
    val number: Int,
    val title: String,
    val highYieldWeightage: String, // e.g., "10-14% Exam Weightage"
    val estimatedHours: Int,
    val masteryLevel: Int, // 0..100
    val topics: List<Topic>
)

data class Topic(
    val id: String,
    val chapterId: String,
    val title: String,
    val summary: String,
    val highYieldNotes: String,
    val formulaSheet: List<String>,
    val keyPoints: List<String>,
    val masteryScore: Int // 0..100
)

package com.example.model

enum class ContentLifecycleState(val displayName: String, val badgeColorHex: Long) {
    DRAFT("Draft", 0xFF64748B),
    REVIEW("Under Review", 0xFFF59E0B),
    APPROVED("Faculty Approved", 0xFF3B82F6),
    PUBLISHED("Published", 0xFF10B981)
}

enum class ContentLicenseType(val displayName: String, val badgeText: String) {
    ORIGINAL_AI_EXPLANATION("Original AI Educational Explanation", "Original AI"),
    OPEN_EDUCATIONAL_RESOURCE("Open Educational Resource (NCERT/OER)", "NCERT/OER"),
    PUBLIC_DOMAIN("Public Domain Historical/Scientific Facts", "Public Domain"),
    PROPRIETARY_LICENSED("Properly Licensed Educational Material", "Licensed"),
    VERIFIED_FACULTY_CONTRIBUTED("Verified Faculty Authored", "Faculty Contributed")
}

data class ContentSourceMetadata(
    val sourceName: String,
    val licenseType: ContentLicenseType,
    val attribution: String,
    val curriculumStandard: String = "NMC NEET & NTA JEE Syllabi",
    val verifiedByFaculty: String = "Academic Review Board",
    val copyrightCleared: Boolean = true
)

enum class ReportCategory(val displayName: String, val badgeColorHex: Long) {
    FACTUAL_INACCURACY("Factual Inaccuracy", 0xFFEF4444),
    CURRICULUM_MISALIGNMENT("Out of Exam Syllabus", 0xFFF59E0B),
    AMBIGUOUS_QUESTION("Ambiguous Question / Typo", 0xFF3B82F6),
    AI_SAFETY_FLAG("Safety / Behavioral Policy Flag", 0xFFDC2626),
    COPYRIGHT_INQUIRY("Copyright / Source Attribution Query", 0xFF8B5CF6)
}

enum class ReportStatus(val displayName: String, val badgeColorHex: Long) {
    OPEN("Open", 0xFFEF4444),
    IN_INVESTIGATION("In Review", 0xFFF59E0B),
    RESOLVED("Resolved", 0xFF10B981),
    DISMISSED("Dismissed", 0xFF64748B)
}

data class UserReport(
    val id: String,
    val contentId: String,
    val contentType: String,
    val reportedBy: String,
    val category: ReportCategory,
    val description: String,
    val status: ReportStatus = ReportStatus.OPEN,
    val timestamp: Long = System.currentTimeMillis(),
    val adminResolutionNote: String = ""
)

data class AIPromptConfiguration(
    val id: String,
    val title: String,
    val roleType: String,
    val systemPrompt: String,
    val safetyRules: List<String>,
    val pedagogicalStyle: String,
    val transparencyStatement: String = "AI Educational Persona • Not a human teacher.",
    val lastUpdated: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

data class AdminAnalyticsSummary(
    val activeLearners: Int = 18450,
    val dailyQuestionsSolved: Int = 142800,
    val questionsInReviewQueue: Int = 14,
    val totalPublishedItems: Int = 3420,
    val promptSafetyAuditScore: Int = 100,
    val openUserReports: Int = 3,
    val averageResolutionTimeHours: Double = 1.8,
    val activeSubscriptions: Int = 4210,
    val monthlyRecurringRevenueInr: Long = 416790
)

enum class AdminTab(val displayName: String, val iconName: String) {
    OVERVIEW("Overview", "Dashboard"),
    CONTENT_LIFECYCLE("Review Queue", "RateReview"),
    CURRICULUM_MANAGER("Curriculum", "MenuBook"),
    TEACHERS_PROMPTS("AI Teachers & Prompts", "Psychology"),
    QUESTIONS_TESTS("Questions & Tests", "Quiz"),
    USER_REPORTS("Reports & Safety", "ReportProblem"),
    PLANS_PRICING("Subscriptions", "Payments")
}

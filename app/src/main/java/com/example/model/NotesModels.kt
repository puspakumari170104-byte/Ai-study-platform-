package com.example.model

enum class NoteCategory(val displayName: String, val badge: String, val description: String) {
    FULL_NOTES("Full Notes", "Complete", "Comprehensive textbook-depth explanations with derivations"),
    SHORT_NOTES("Short Notes", "Concise", "Crisp bullet points for rapid chapter memorization"),
    QUICK_REVISION("Quick Revision", "High-Yield", "Ultra-fast exam morning recap cards with key traps"),
    FORMULA_SHEET("Formula Sheets", "Equations", "Master list of formulas, constants, and dimensional units"),
    CONCEPT_SUMMARY("Concept Summaries", "Foundations", "First-principles mental models, analogies, and core rules"),
    AI_PERSONAL_NOTES("AI Personal Notes", "Personalized", "AI-synthesized notes tailored to your doubts and mistakes")
}

data class StudyNote(
    val id: String,
    val programId: String,
    val subjectName: String,
    val chapterTitle: String,
    val title: String,
    val category: NoteCategory,
    val content: String,
    val keyFormulas: List<String> = emptyList(),
    val highYieldPoints: List<String> = emptyList(),
    val isBookmarked: Boolean = false,
    val highlightedPhrases: List<String> = emptyList(),
    val personalNotes: String = "",
    val lifecycleState: ContentLifecycleState = ContentLifecycleState.PUBLISHED,
    val sourceMetadata: ContentSourceMetadata? = null,
    val timestamp: Long = System.currentTimeMillis()
)

package com.example.model

enum class TeachingLanguage(val code: String, val displayName: String, val promptInstruction: String) {
    ENGLISH(
        code = "en",
        displayName = "English",
        promptInstruction = "Explain clearly and pedagogically in English. Keep explanations concise, structured, and exam-oriented."
    ),
    HINDI(
        code = "hi",
        displayName = "हिंदी (Hindi)",
        promptInstruction = "Explain in simple, clear Hindi (using Devanagari script). Maintain standard scientific equations, variables, and physics/chemistry terminology in English letters (e.g. F=ma, Work-Energy Theorem) while explaining the reasoning and steps in natural Hindi."
    ),
    HINGLISH(
        code = "hinglish",
        displayName = "Hinglish (Colloquial)",
        promptInstruction = "Explain like a friendly Kota coaching teacher in conversational Hinglish (Roman script, e.g., 'Dhyan se samjho: jab force aur displacement ke beech angle 90 degrees hota hai tab work done zero hota hai'). Keep mathematical formulas in standard English notation."
    );

    companion object {
        fun fromCode(code: String): TeachingLanguage {
            return values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}

package com.example.util

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("EN", "English", "English"),
    HINDI("HI", "Hindi", "हिंदी")
}

object AppStrings {
    fun get(key: String, language: AppLanguage): String {
        return if (language == AppLanguage.HINDI) {
            hindiStrings[key] ?: englishStrings[key] ?: key
        } else {
            englishStrings[key] ?: key
        }
    }

    private val englishStrings = mapOf(
        "nav_home" to "Home",
        "nav_learn" to "Learn",
        "nav_teachers" to "Teachers",
        "nav_practice" to "Practice",
        "nav_tests" to "Tests",
        "nav_mentor" to "Mentor",
        "nav_progress" to "Progress",
        "nav_profile" to "Profile",
        "app_title" to "Study With AI",
        "app_tagline" to "Your Virtual AI Coaching Institute",
        "greeting_prefix" to "Welcome back,",
        "daily_streak" to "Day Streak",
        "study_hours_today" to "Study Goal Today",
        "start_learning_btn" to "Start Learning",
        "todays_agenda" to "Today's Study Plan",
        "live_classes" to "Live 3D Classes",
        "explore_curriculum" to "Curriculum",
        "dpp_practice" to "Daily Practice",
        "mock_tests" to "Test Series",
        "ai_doubt_solve" to "Instant Doubt Resolver",
        "formula_vault" to "Formula & Notes Vault",
        "subjects" to "Subjects",
        "physics" to "Physics",
        "chemistry" to "Chemistry",
        "mathematics" to "Mathematics",
        "biology" to "Biology",
        "submit" to "Submit",
        "continue" to "Continue",
        "verify" to "Verify",
        "unlock_premium" to "Unlock Pro Pass",
        "ask_doubt" to "Ask a Doubt",
        "start_test" to "Start Test",
        "view_analysis" to "View Analysis",
        "switch_language" to "Switch Language",
        "registration_title" to "Student Registration",
        "select_language_prompt" to "Choose your learning language:"
    )

    private val hindiStrings = mapOf(
        "nav_home" to "होम",
        "nav_learn" to "सीखें",
        "nav_teachers" to "शिक्षक",
        "nav_practice" to "अभ्यास",
        "nav_tests" to "टेस्ट",
        "nav_mentor" to "मेंटर",
        "nav_progress" to "प्रगति",
        "nav_profile" to "प्रोफाइल",
        "app_title" to "Study With AI (कोचिंग)",
        "app_tagline" to "आपका वर्चुअल AI कोचिंग संस्थान",
        "greeting_prefix" to "नमस्ते,",
        "daily_streak" to "दिन की निरंतरता",
        "study_hours_today" to "आज का अध्ययन लक्ष्य",
        "start_learning_btn" to "पढ़ाई शुरू करें",
        "todays_agenda" to "आज का टाइमटेबल",
        "live_classes" to "लाइव 3D क्लास",
        "explore_curriculum" to "पाठ्यक्रम",
        "dpp_practice" to "दैनिक अभ्यास (DPP)",
        "mock_tests" to "मॉक टेस्ट सीरीज",
        "ai_doubt_solve" to "संदेह निवारण",
        "formula_vault" to "सूत्र संग्रह",
        "subjects" to "विषय",
        "physics" to "भौतिक विज्ञान",
        "chemistry" to "रसायन विज्ञान",
        "mathematics" to "गणित",
        "biology" to "जीव विज्ञान",
        "submit" to "जमा करें",
        "continue" to "जारी रखें",
        "verify" to "सत्यापित करें",
        "unlock_premium" to "प्रो पास अनलॉक करें",
        "ask_doubt" to "संदेह पूछें",
        "start_test" to "टेस्ट शुरू करें",
        "view_analysis" to "विश्लेषण देखें",
        "switch_language" to "भाषा बदलें",
        "registration_title" to "विद्यार्थी पंजीकरण",
        "select_language_prompt" to "अपनी सीखने की भाषा चुनें:"
    )
}

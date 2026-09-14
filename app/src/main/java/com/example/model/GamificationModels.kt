package com.example.model

data class StudentAchievement(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val isUnlocked: Boolean,
    val unlockedDate: String? = null,
    val category: String = "Learning"
)

data class DailyGoal(
    val id: String,
    val title: String,
    val currentProgress: Int,
    val targetProgress: Int,
    val unit: String,
    val isCompleted: Boolean = currentProgress >= targetProgress,
    val xpReward: Int = 50
)

data class GamificationProfile(
    val totalXp: Int = 1850,
    val currentLevel: Int = 4,
    val levelTitle: String = "Formula Master",
    val nextLevelXp: Int = 2500,
    val currentStreak: Int = 14,
    val longestStreak: Int = 21,
    val dailyGoals: List<DailyGoal> = listOf(
        DailyGoal("dg1", "Daily Study Target", 45, 90, "mins", false, 60),
        DailyGoal("dg2", "Solve Practice Questions", 12, 15, "questions", false, 50),
        DailyGoal("dg3", "Revise Mistake Traps", 2, 2, "mistakes", true, 40)
    ),
    val achievements: List<StudentAchievement> = listOf(
        StudentAchievement("ach_1", "Streak Titan", "Maintain study streak for 7 consecutive days", 100, true, "Yesterday", "Streak"),
        StudentAchievement("ach_2", "Doubt Slayer", "Resolve 10 conceptual doubts with AI Faculty", 150, true, "3 days ago", "Doubt"),
        StudentAchievement("ach_3", "Error Exterminator", "Revise and resolve 5 mistakes in Mistake Book", 200, true, "5 days ago", "Mastery"),
        StudentAchievement("ach_4", "Century Test Club", "Score above 85% in any chapter diagnostic mock", 250, false, null, "Practice"),
        StudentAchievement("ach_5", "Curriculum Milestone", "Complete all Mechanics topics in Physics", 300, false, null, "Mastery")
    )
)

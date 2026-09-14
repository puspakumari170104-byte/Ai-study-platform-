package com.example.model

enum class SubscriptionTier(val displayName: String) {
    FREE("Free Tier"),
    PREMIUM("Institute Pass (Premium)")
}

enum class SubscriptionBillingCycle(val label: String, val periodSuffix: String) {
    MONTHLY("Monthly", "/month"),
    QUARTERLY("Quarterly", "/quarter"),
    ANNUAL("Annual", "/year")
}

data class SubscriptionPlan(
    val id: String,
    val title: String,
    val tag: String,
    val priceInr: Int,
    val originalPriceInr: Int,
    val billingCycle: SubscriptionBillingCycle,
    val description: String,
    val features: List<String>,
    val isPopular: Boolean = false,
    val savingsBadge: String? = null
)

data class TierEntitlements(
    val tier: SubscriptionTier,
    val dailyDoubtsRemaining: Int,
    val maxDailyDoubts: Int,
    val aiClassesRemainingPerWeek: Int,
    val maxAiClassesPerWeek: Int,
    val mockTestsRemainingPerMonth: Int,
    val maxMockTestsPerMonth: Int,
    val hasAdvancedMistakeAnalysis: Boolean,
    val hasAdaptiveStudyPlan: Boolean,
    val hasFullLengthMocks: Boolean,
    val hasUnlimitedFacultyVoice: Boolean
) {
    companion object {
        fun freeTier() = TierEntitlements(
            tier = SubscriptionTier.FREE,
            dailyDoubtsRemaining = 3,
            maxDailyDoubts = 5,
            aiClassesRemainingPerWeek = 2,
            maxAiClassesPerWeek = 2,
            mockTestsRemainingPerMonth = 2,
            maxMockTestsPerMonth = 3,
            hasAdvancedMistakeAnalysis = false,
            hasAdaptiveStudyPlan = false,
            hasFullLengthMocks = false,
            hasUnlimitedFacultyVoice = false
        )

        fun premiumTier() = TierEntitlements(
            tier = SubscriptionTier.PREMIUM,
            dailyDoubtsRemaining = 999,
            maxDailyDoubts = 999,
            aiClassesRemainingPerWeek = 999,
            maxAiClassesPerWeek = 999,
            mockTestsRemainingPerMonth = 999,
            maxMockTestsPerMonth = 999,
            hasAdvancedMistakeAnalysis = true,
            hasAdaptiveStudyPlan = true,
            hasFullLengthMocks = true,
            hasUnlimitedFacultyVoice = true
        )
    }
}

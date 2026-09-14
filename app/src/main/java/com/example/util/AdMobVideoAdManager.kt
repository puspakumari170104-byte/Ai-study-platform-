package com.example.util

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AdMobVideoAdManager {
    private const val TAG = "AdMobVideoAdManager"
    const val TEST_REWARDED_VIDEO_ID = "ca-app-pub-3940256099942544/5224354917"
    const val TEST_INTERSTITIAL_VIDEO_ID = "ca-app-pub-3940256099942544/1033173712"

    private var activeRewardedAdUnitId = TEST_REWARDED_VIDEO_ID
    private var activeInterstitialAdUnitId = TEST_INTERSTITIAL_VIDEO_ID

    private var rewardedAd: RewardedAd? = null
    private var interstitialAd: InterstitialAd? = null

    private val _isRewardedAdLoaded = MutableStateFlow(false)
    val isRewardedAdLoaded: StateFlow<Boolean> = _isRewardedAdLoaded.asStateFlow()

    private val _isInterstitialAdLoaded = MutableStateFlow(false)
    val isInterstitialAdLoaded: StateFlow<Boolean> = _isInterstitialAdLoaded.asStateFlow()

    private val _isAdLoading = MutableStateFlow(false)
    val isAdLoading: StateFlow<Boolean> = _isAdLoading.asStateFlow()

    private val _adStatusMessage = MutableStateFlow("AdMob Ready")
    val adStatusMessage: StateFlow<String> = _adStatusMessage.asStateFlow()

    private val _totalRewardsEarned = MutableStateFlow(0)
    val totalRewardsEarned: StateFlow<Int> = _totalRewardsEarned.asStateFlow()

    private var isInitialized = false

    fun initialize(context: Context) {
        if (isInitialized) return
        try {
            MobileAds.initialize(context) { initializationStatus ->
                isInitialized = true
                Log.d(TAG, "AdMob SDK Initialized: ${initializationStatus.adapterStatusMap}")
                _adStatusMessage.value = "AdMob SDK Initialized"
                preloadRewardedVideo(context)
                preloadInterstitialVideo(context)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize AdMob SDK", e)
            _adStatusMessage.value = "AdMob Init: ${e.localizedMessage}"
        }
    }

    fun setCustomAdUnitIds(rewardedId: String? = null, interstitialId: String? = null) {
        if (!rewardedId.isNullOrBlank()) activeRewardedAdUnitId = rewardedId
        if (!interstitialId.isNullOrBlank()) activeInterstitialAdUnitId = interstitialId
    }

    fun getActiveRewardedId(): String = activeRewardedAdUnitId
    fun getActiveInterstitialId(): String = activeInterstitialAdUnitId

    fun preloadRewardedVideo(context: Context, onResult: ((Boolean) -> Unit)? = null) {
        if (_isRewardedAdLoaded.value && rewardedAd != null) {
            onResult?.invoke(true)
            return
        }
        _isAdLoading.value = true
        _adStatusMessage.value = "Loading Short Video Ad..."
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            activeRewardedAdUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    _isRewardedAdLoaded.value = true
                    _isAdLoading.value = false
                    _adStatusMessage.value = "Short Video Ad Ready"
                    onResult?.invoke(true)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    rewardedAd = null
                    _isRewardedAdLoaded.value = false
                    _isAdLoading.value = false
                    _adStatusMessage.value = "Video Ad Failed: ${loadAdError.message}"
                    onResult?.invoke(false)
                }
            }
        )
    }

    fun showRewardedVideo(
        activity: Activity,
        rewardTitle: String = "Bonus Study Unlock",
        onUserEarnedReward: (RewardItem) -> Unit,
        onAdClosed: () -> Unit = {}
    ) {
        val currentAd = rewardedAd
        if (currentAd != null) {
            currentAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    _adStatusMessage.value = "Playing Video Ad..."
                }

                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    _isRewardedAdLoaded.value = false
                    _adStatusMessage.value = "Ad Completed"
                    onAdClosed()
                    preloadRewardedVideo(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    rewardedAd = null
                    _isRewardedAdLoaded.value = false
                    _adStatusMessage.value = "Ad Show Error: ${adError.message}"
                    onAdClosed()
                    preloadRewardedVideo(activity)
                }
            }
            currentAd.show(activity) { rewardItem ->
                _totalRewardsEarned.value += 1
                _adStatusMessage.value = "Reward Claimed: ${rewardItem.amount} ${rewardItem.type}"
                onUserEarnedReward(rewardItem)
            }
        } else {
            _adStatusMessage.value = "Video Ad loading... please try in a moment"
            preloadRewardedVideo(activity) { loaded ->
                if (loaded) {
                    showRewardedVideo(activity, rewardTitle, onUserEarnedReward, onAdClosed)
                }
            }
        }
    }

    fun preloadInterstitialVideo(context: Context, onResult: ((Boolean) -> Unit)? = null) {
        if (_isInterstitialAdLoaded.value && interstitialAd != null) {
            onResult?.invoke(true)
            return
        }
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            activeInterstitialAdUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    _isInterstitialAdLoaded.value = true
                    onResult?.invoke(true)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    _isInterstitialAdLoaded.value = false
                    onResult?.invoke(false)
                }
            }
        )
    }

    fun showInterstitialVideo(activity: Activity, onAdDismissed: () -> Unit = {}) {
        val currentAd = interstitialAd
        if (currentAd != null) {
            currentAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    _isInterstitialAdLoaded.value = false
                    onAdDismissed()
                    preloadInterstitialVideo(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    _isInterstitialAdLoaded.value = false
                    onAdDismissed()
                    preloadInterstitialVideo(activity)
                }
            }
            currentAd.show(activity)
        } else {
            onAdDismissed()
            preloadInterstitialVideo(activity)
        }
    }
}

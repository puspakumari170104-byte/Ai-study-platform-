export interface CodeFile {
  path: string;
  name: string;
  category: 'Entry & Manifest' | 'UI Screens' | 'Data & Database' | 'AI & API' | 'Build & CI/CD' | 'Utilities';
  language: 'kotlin' | 'xml' | 'yaml' | 'json' | 'markdown' | 'bash';
  description: string;
  content: string;
}

export const androidCodeFiles: CodeFile[] = [
  {
    path: '.github/workflows/build-apk.yml',
    name: 'build-apk.yml',
    category: 'Build & CI/CD',
    language: 'yaml',
    description: 'GitHub Actions workflow: Builds APK and creates GitHub Release for 1-click download to Android phone.',
    content: `name: Build & Release Android APK

on:
  push:
    branches: [ main, master ]
    tags:
      - 'v*'
  workflow_dispatch:

permissions:
  contents: write

jobs:
  build:
    name: Build Android APK
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Code Repository
        uses: actions/checkout@v4

      - name: Set up Java JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Setup Android SDK
        uses: android-actions/setup-android@v3

      - name: Grant Execute Permission to Gradle Wrapper
        run: chmod +x gradlew

      - name: Build Debug APK with Gradle
        run: ./gradlew assembleDebug --stacktrace

      - name: Upload Debug APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: StudyWithAI-Debug-APK
          path: app/build/outputs/apk/debug/app-debug.apk

      - name: Create GitHub Release
        if: startsWith(github.ref, 'refs/tags/') || github.event_name == 'workflow_dispatch'
        uses: softprops/action-gh-release@v2
        with:
          tag_name: \${{ github.ref_name || 'v1.0.0' }}
          name: Study With AI - Android APK Release
          body: |
            ## 📱 Download Study With AI for Android Phone
            Tap 'app-debug.apk' below to download and install on your phone!
          files: app/build/outputs/apk/debug/app-debug.apk`
  },
  {
    path: 'app/build.gradle.kts',
    name: 'build.gradle.kts (app)',
    category: 'Build & CI/CD',
    language: 'kotlin',
    description: 'Gradle App module config: Jetpack Compose, Room DB, Retrofit, AdMob SDK, Gemini AI.',
    content: `import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.studywithai.vqelmp"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "\${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
    create("debugConfig") {
      storeFile = file("\${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug { signingConfig = signingConfigs.getByName("debugConfig") }
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  "ksp"(libs.androidx.room.compiler)
  implementation(libs.play.services.ads)
  implementation(libs.retrofit)
  implementation(libs.kotlinx.coroutines.android)
}`
  },
  {
    path: 'app/src/main/AndroidManifest.xml',
    name: 'AndroidManifest.xml',
    category: 'Entry & Manifest',
    language: 'xml',
    description: 'Android Manifest: Permissions (Internet, Audio), Application config, and AdMob sample ID.',
    content: `<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyApplication">

        <!-- Google AdMob Application ID (Official Sample ID for development & testing) -->
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-3940256099942544~3347511713" />

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.MyApplication">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>`
  },
  {
    path: 'app/src/main/java/com/example/MainActivity.kt',
    name: 'MainActivity.kt',
    category: 'Entry & Manifest',
    language: 'kotlin',
    description: 'Primary Activity with Modal Navigation Drawer, Bottom Navigation Bar, and Screen Routing.',
    content: `package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.util.AdMobVideoAdManager
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize Google AdMob SDK for Short Video Rewards
        AdMobVideoAdManager.initialize(applicationContext)
        setContent {
            MyApplicationTheme {
                StudyWithAiApp()
            }
        }
    }
}

@Composable
fun StudyWithAiApp(viewModel: MainViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { /* Navigation Drawer content */ }
    ) {
        Scaffold(
            bottomBar = { /* Bottom Bar */ }
        ) { innerPadding ->
            Crossfade(targetState = currentScreen) { screen ->
                when (screen) {
                    AppScreen.DASHBOARD -> HomeScreen(viewModel)
                    AppScreen.CLASSROOM -> ClassroomScreen(viewModel)
                    AppScreen.CURRICULUM -> CurriculumScreen(viewModel)
                    AppScreen.TEACHERS -> TeachersScreen(viewModel)
                    AppScreen.PRACTICE -> PracticeScreen(viewModel)
                    AppScreen.TEST_CENTER -> TestCenterScreen(viewModel)
                    AppScreen.ANALYTICS_MISTAKES -> AnalyticsMistakesScreen(viewModel)
                    AppScreen.MENTOR -> MentorScreen(viewModel)
                    AppScreen.DOUBT_SOLVER -> DoubtSolverScreen(viewModel)
                    AppScreen.STUDY_PLAN -> StudyPlanScreen(viewModel)
                    AppScreen.SUBSCRIPTION -> SubscriptionScreen(viewModel)
                    AppScreen.ADMIN_PANEL -> AdminPanelScreen(viewModel)
                    else -> HomeScreen(viewModel)
                }
            }
        }
    }
}`
  },
  {
    path: 'app/src/main/java/com/example/viewmodel/MainViewModel.kt',
    name: 'MainViewModel.kt',
    category: 'Data & Database',
    language: 'kotlin',
    description: 'Core ViewModel managing StateFlows, Room DB flows, TTS engine, Test Analysis, and AI requests.',
    content: `package com.example.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiAiService
import com.example.data.db.AppDatabase
import com.example.data.repository.CoachingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

enum class AppScreen {
    ONBOARDING, DASHBOARD, CLASSROOM, CURRICULUM, TEACHERS,
    PRACTICE, TEST_CENTER, ANALYTICS_MISTAKES, MENTOR,
    SUBSCRIPTION, DOUBT_SOLVER, STUDY_PLAN, LESSON_DETAIL,
    NOTES, PROFILE, CLASS_SCHEDULER, ADMIN_PANEL
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val aiService = GeminiAiService()
    val repository = CoachingRepository(database, aiService)

    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun submitTest() {
        // Evaluate test, calculate percentile, and save mistakes to Room DB
    }
}`
  },
  {
    path: 'app/src/main/java/com/example/data/api/GeminiAiService.kt',
    name: 'GeminiAiService.kt',
    category: 'AI & API',
    language: 'kotlin',
    description: 'Gemini 2.5 API integration for Socratic doubt solving, 3D live class step generation, and mistake analysis.',
    content: `package com.example.data.api

import com.example.BuildConfig
import com.example.model.AITeacher
import com.example.model.DoubtContext
import com.example.model.DoubtSolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class GeminiAiService {
    private val client = OkHttpClient()

    suspend fun askTeacherDoubt(teacher: AITeacher, doubt: String, topic: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineTeacherResponse(teacher, doubt, topic)
        }
        // Call Gemini 2.5 Flash endpoint with persona system instructions
        val prompt = "Student doubt: \\"$doubt\\". Topic: \\"$topic\\". Respond as Professor \${teacher.name}."
        callGeminiApi(prompt, teacher.systemInstructions, apiKey) ?: getOfflineTeacherResponse(teacher, doubt, topic)
    }

    suspend fun solveDoubtWithContext(doubtText: String, context: DoubtContext): DoubtSolution {
        // Resolves doubt calibrated to Class 11/12 NCERT syllabus level
        // Returns step-by-step logic, analogy, formula sheet, and concept verification question
    }
}`
  },
  {
    path: 'app/src/main/java/com/example/util/AdMobVideoAdManager.kt',
    name: 'AdMobVideoAdManager.kt',
    category: 'Utilities',
    language: 'kotlin',
    description: 'Official Google AdMob Short Video Ads Manager: Rewarded Video & Interstitial Video with reward callbacks.',
    content: `package com.example.util

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AdMobVideoAdManager {
    // Official Google AdMob Test Ad Unit IDs
    const val TEST_REWARDED_VIDEO_ID = "ca-app-pub-3940256099942544/5224354917"
    const val TEST_INTERSTITIAL_VIDEO_ID = "ca-app-pub-3940256099942544/1033173712"

    private var rewardedAd: RewardedAd? = null
    private val _isRewardedAdLoaded = MutableStateFlow(false)
    val isRewardedAdLoaded: StateFlow<Boolean> = _isRewardedAdLoaded

    fun initialize(context: Context) {
        MobileAds.initialize(context) {
            preloadRewardedVideo(context)
        }
    }

    fun preloadRewardedVideo(context: Context) {
        RewardedAd.load(context, TEST_REWARDED_VIDEO_ID, AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    _isRewardedAdLoaded.value = true
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    _isRewardedAdLoaded.value = false
                }
            }
        )
    }

    fun showRewardedVideo(activity: Activity, onUserEarnedReward: (RewardItem) -> Unit) {
        rewardedAd?.show(activity) { rewardItem ->
            onUserEarnedReward(rewardItem)
            preloadRewardedVideo(activity)
        }
    }
}`
  },
  {
    path: 'app/src/main/java/com/example/data/db/AppDatabase.kt',
    name: 'AppDatabase.kt',
    category: 'Data & Database',
    language: 'kotlin',
    description: 'Room Database definition with DAOs for User, Courses, UTR Payments, Mistakes Notebook, and Test Records.',
    content: `package com.example.data.db

import android.content.Context
import androidx.room.*

@Database(
    entities = [
        UserEntity::class,
        CourseEntity::class,
        UtrPaymentEntity::class,
        StudentProfileEntity::class,
        MistakeEntity::class,
        TestRecordEntity::class,
        BookmarkedNoteEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun utrPaymentDao(): UtrPaymentDao
    abstract fun coachingDao(): CoachingDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "study_with_ai_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}`
  },
  {
    path: 'app/src/main/java/com/example/ui/screens/HomeScreen.kt',
    name: 'HomeScreen.kt',
    category: 'UI Screens',
    language: 'kotlin',
    description: 'Jetpack Compose Home Dashboard: Daily missions, 1-on-1 continues, upcoming classes, and AdMob rewards.',
    content: `package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Welcome to Study With AI", style = MaterialTheme.typography.titleLarge)
            // Today's Mission & Action Card
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Today's Mission: Laws of Motion & Friction")
                    Button(onClick = { viewModel.navigateTo(AppScreen.CLASSROOM) }) {
                        Text("Continue Learning")
                    }
                }
            }
            // AdMob Short Video Rewards Card
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                // Watch 30s Short Video to earn 4K video passes & doubt credits
            }
        }
    }
}`
  },
  {
    path: 'app/src/main/java/com/example/ui/screens/ClassroomScreen.kt',
    name: 'ClassroomScreen.kt',
    category: 'UI Screens',
    language: 'kotlin',
    description: 'Live 3D AI Classroom with interactive digital blackboard, student interruption, and 4K character video.',
    content: `package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.viewmodel.MainViewModel

@Composable
fun ClassroomScreen(viewModel: MainViewModel) {
    val currentStep by viewModel.currentClassStep.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 4K Character Video Lecture Stage / 3D Smart Board Studio
        // Interactive Whiteboard Canvas
        // Socratic "I don't understand" teaching method switcher
        // Instant Verbal / Text Interruption Bar
    }
}`
  },
  {
    path: 'app/src/main/java/com/example/ui/screens/PracticeScreen.kt',
    name: 'PracticeScreen.kt',
    category: 'UI Screens',
    language: 'kotlin',
    description: 'Adaptive DPP Practice Engine with 9 practice modes, Socratic hints, and auto mistake logging.',
    content: `package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.viewmodel.MainViewModel

@Composable
fun PracticeScreen(viewModel: MainViewModel) {
    val questions by viewModel.practiceQuestions.collectAsState()
    // 9 Practice Modes: Topic, Chapter, Subject, Weak Topic, Revision, Challenger
    // Question card with +4/-1 Marking
    // Socratic Hint trigger
    // Deep explanation and mistake classification
}`
  },
  {
    path: 'app/src/main/java/com/example/ui/screens/TestCenterScreen.kt',
    name: 'TestCenterScreen.kt',
    category: 'UI Screens',
    language: 'kotlin',
    description: 'NTA Exam Pattern Mock Test Center: Real-time countdown timer, question palette, review marks, and AIR prediction.',
    content: `package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.viewmodel.MainViewModel

@Composable
fun TestCenterScreen(viewModel: MainViewModel) {
    val isTestActive by viewModel.isTestActive.collectAsState()
    // Active Test view with Question Palette & Timer
    // Test Score Summary with AI Performance Explanation, Accuracy & Pacing Breakdown
    // Chapter & Topic performance metrics
}`
  },
  {
    path: 'app/src/main/java/com/example/ui/screens/AnalyticsMistakesScreen.kt',
    name: 'AnalyticsMistakesScreen.kt',
    category: 'UI Screens',
    language: 'kotlin',
    description: 'My Mistakes Book: Permanent cognitive error repository tracking conceptual, calculation, memory, and trap slips.',
    content: `package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.viewmodel.MainViewModel

@Composable
fun AnalyticsMistakesScreen(viewModel: MainViewModel) {
    val mistakes by viewModel.mistakes.collectAsState()
    // AI Daily Mistake Recommendation
    // Filter by Mistake Type (Conceptual, Calculation Slip, Memory, Examiner Trap)
    // 1-Click "Retest My Mistakes" targeted exam sprint
}`
  },
  {
    path: 'app/src/main/java/com/example/ui/screens/SubscriptionScreen.kt',
    name: 'SubscriptionScreen.kt',
    category: 'UI Screens',
    language: 'kotlin',
    description: 'Affordable Institute Pass: ₹99/mo subscription, verified Merchant UPI IDs, and instant 12-digit UTR unlock.',
    content: `package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.viewmodel.MainViewModel

@Composable
fun SubscriptionScreen(viewModel: MainViewModel) {
    // 1. Choose Plan (₹99 Monthly, ₹249 Quarterly, ₹799 Annual)
    // 2. Verified Merchant UPI Payment (7339956247@ptyes, 7339956247@ybl)
    // 3. Enter 12-digit UTR Reference ID & Instant Pass Unlock
    // 4. Watch Short Video for Free 4K Pass
}`
  },
  {
    path: 'metadata.json',
    name: 'metadata.json',
    category: 'Build & CI/CD',
    language: 'json',
    description: 'Platform project metadata: App name, description, and Gemini API capability.',
    content: `{
  "name": "Study With AI",
  "description": "AI-powered education and coaching platform with interactive classrooms, practice engine, test mastery, and GitHub APK download workflow.",
  "requestFramePermissions": [],
  "majorCapabilities": ["MAJOR_CAPABILITY_SERVER_SIDE_GEMINI_API"]
}`
  },
  {
    path: 'README.md',
    name: 'README.md',
    category: 'Build & CI/CD',
    language: 'markdown',
    description: 'Full Documentation: How to download directly into Android phone from GitHub, GitHub Actions, and Android Studio.',
    content: `# Study With AI - Android Coaching Platform
Direct APK download from GitHub Releases, CI/CD workflow, and full Kotlin source code.`
  }
];

# 🎓 Study With AI - Android Coaching Platform

[![Build & Release Android APK](https://github.com/puspakumari170104/Study-With-AI/actions/workflows/build-apk.yml/badge.svg)](https://github.com/puspakumari170104/Study-With-AI/actions/workflows/build-apk.yml)
[![Direct APK Download](https://img.shields.io/badge/Download-Android%20APK%20(Direct)-brightgreen?logo=android&logoColor=white)](#-how-to-download-directly-into-your-android-phone-from-github)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue?logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4?logo=android)](https://developer.android.com/jetpack/compose)

> **Study With AI** is a comprehensive, production-grade Android academic coaching and exam mastery platform. Built with modern **Kotlin**, **Jetpack Compose (Material 3)**, **Room Database**, **Google AdMob (Rewarded & Interstitial Video Ads)**, and **Gemini 2.5 AI**.

---

## 📱 Instant Installation on Android Phone via GitHub Push

Whenever you push this project to GitHub (or click **Push to GitHub** in Google AI Studio), GitHub Actions automatically compiles the Android APK and publishes a ready-to-install release.

### 🌟 How to Install Directly on Your Android Phone:

#### Step 1: Push to GitHub
- In Google AI Studio, click **Settings** / **Export** -> **Push to GitHub** (or run `git push origin main` from your terminal).
- The automated CI/CD pipeline (`.github/workflows/build-apk.yml`) triggers immediately.

#### Step 2: Download the APK from GitHub Releases
1. On your **Android phone**, open your GitHub repository in your browser (e.g. Chrome).
2. Scroll to the **Releases** section on the right side (or navigate to `https://github.com/YOUR_USERNAME/Study-With-AI/releases`).
3. Under the latest release (**`v1.0.0-latest`**), expand **Assets**.
4. Tap **`app-debug.apk`** to download the APK directly onto your phone.

#### Step 3: Install the APK on Your Phone
1. When download completes, tap the notification or open the file from your **Downloads** folder.
2. If Android displays: *"For your security, your phone is not allowed to install unknown apps from this source"*:
   - Tap **Settings** in the popup.
   - Toggle on **"Allow from this source"** for your browser or file manager.
   - Tap the back button and tap **Install**.
3. Tap **Open** and enjoy **Study With AI**!

---

### ⚡ Alternative: Download from GitHub Actions Run Artifacts
1. Go to the **Actions** tab in your GitHub repository.
2. Tap the topmost workflow run (**Build & Release Android APK**).
3. Scroll down to the **Artifacts** section at the bottom.
4. Tap **`StudyWithAI-Android-APK`** to download.

---

### 💻 Local 1-Click Build via Gradle
If you want to build locally on your computer:
- **macOS / Linux**:
  ```bash
  chmod +x gradlew
  ./gradlew assembleDebug
  ```
- **Windows**:
  ```cmd
  gradlew.bat assembleDebug
  ```
The compiled APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.

---

## 🚀 Key Features

- **🏛️ 1-on-1 Master AI Faculty**: Bilingual (Hindi/English/Hinglish) teaching with Kota coaching pedagogy.
- **🎙️ 24/7 Socratic Doubt Solver**: Contextual step-by-step doubt resolution adapted to the student's curriculum.
- **📐 Interactive Whiteboard Derivations**: Smart digital chalkboard rendering vector mechanics, formulas, and diagrams.
- **🎯 Adaptive DPP & NTA Mock Tests**: Chapter tests, PYQ practice drills, and full-length simulated mock exams with negative marking.
- **📓 Intelligent Mistake Notebook**: Cognitive root-cause categorization (calculation slip, conceptual gap, trap question, time pressure) with spaced revision schedules.
- **📅 Adaptive Study Plan Recalculator**: Automatically rebalances syllabus milestones when study days are missed.
- **💰 Google AdMob Video Rewards**: Watch short 30-second rewarded videos to unlock 4K video passes and doubt credits.

---

## 📂 Project Architecture

```
├── .github/workflows/
│   └── build-apk.yml          # Automated CI/CD workflow to build & release APK
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml # Permissions, Application config, AdMob metadata
│   │   ├── java/com/example/
│   │   │   ├── MainActivity.kt # Primary Edge-to-Edge Activity & Nav Drawer
│   │   │   ├── model/          # Learning Programs, AI Teachers, Questions, Tests
│   │   │   ├── data/           # Room Database, Gemini API, Repositories
│   │   │   ├── viewmodel/      # MainViewModel with TTS, StateFlow, Coroutines
│   │   │   ├── ui/screens/     # M3 Compose Screens (Home, Classroom, Practice, etc.)
│   │   │   └── util/           # AdMob Manager & Bilingual Language System
│   │   └── res/                # Drawables, Vector Icons, Strings, Themes
│   └── build.gradle.kts        # Android Gradle Plugin, Compose BOM, Room, KSP
├── gradlew & gradlew.bat       # Standalone Gradle Wrapper for 1-click builds
└── README.md
```

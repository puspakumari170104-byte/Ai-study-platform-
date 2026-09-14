# 🎓 Study With AI - Android Coaching Platform

[![Build & Release Android APK](https://github.com/puspakumari170104/Study-With-AI/actions/workflows/build-apk.yml/badge.svg)](https://github.com/puspakumari170104/Study-With-AI/actions/workflows/build-apk.yml)
[![Direct APK Download](https://img.shields.io/badge/Download-Android%20APK%20(Direct)-brightgreen?logo=android&logoColor=white)](#-how-to-download-directly-into-your-android-phone-from-github)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue?logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4?logo=android)](https://developer.android.com/jetpack/compose)

> **Study With AI** is a comprehensive, production-grade Android academic coaching and exam mastery platform. Built with modern **Kotlin**, **Jetpack Compose (Material 3)**, **Room Database**, **Google AdMob (Rewarded & Interstitial Video Ads)**, and **Gemini 2.5 AI**.

---

## 📱 How to Download & Install Directly on Android Phone

Whenever you push code or trigger the build on GitHub, GitHub Actions automatically compiles the Android APK and makes it immediately downloadable.

### 🌟 2 Easy Ways to Download the Single APK (No ZIP needed!):

#### Option 1: Direct Download from GitHub Releases (Recommended)
1. On your **Android phone**, open your GitHub repository in your browser (e.g., Chrome).
2. Tap on **Releases** (or navigate directly to the Releases page).
3. Under the latest release (**`Study With AI`**), look at **Assets**.
4. Tap **`StudyWithAI-v1.0.apk`** to download the single APK directly onto your phone without extracting any zip file.

#### Option 2: Download from GitHub Actions
1. Go to the **Actions** tab in your GitHub repository.
2. Tap the latest workflow run (**Build & Release Android APK**).
3. Scroll down to the **Artifacts** section at the bottom.
4. Tap **`StudyWithAI-v1.0-APK`** to download.

---

### 🚀 3-Step Phone Installation Guide:
1. **Download**: Tap **`StudyWithAI-v1.0.apk`** to download the file.
2. **Open**: Once the download completes, tap the file in your phone's notification shade or in your phone's **Downloads** folder.
3. **Install**: Tap **Install**. If Android prompts with:
   *"For your security, your phone is not allowed to install unknown apps from this source"*:
   - Tap **Settings** in the dialog.
   - Turn ON **"Allow from this source"** for your browser or file manager.
   - Return to the previous screen and tap **Install**.
4. Tap **Open** and enjoy **Study With AI**!

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

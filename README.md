# 🎓 Study With AI - Android Coaching Platform

[![Build & Release Android APK](https://github.com/puspakumari170104/Study-With-AI/actions/workflows/build-apk.yml/badge.svg)](https://github.com/puspakumari170104/Study-With-AI/actions/workflows/build-apk.yml)
[![Direct APK Download](https://img.shields.io/badge/Download-Android%20APK%20(Direct)-brightgreen?logo=android&logoColor=white)](#-how-to-download-directly-into-your-android-phone-from-github)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue?logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4?logo=android)](https://developer.android.com/jetpack/compose)

> **Study With AI** is a comprehensive, production-grade Android academic coaching and exam mastery platform. Built with modern **Kotlin**, **Jetpack Compose (Material 3)**, **Room Database**, **Google AdMob (Rewarded & Interstitial Video Ads)**, and **Gemini 2.5 AI**.

---

## 📱 How to Download the Installable APK to Your Phone

### 🌐 Method 1: Export to GitHub (Direct Standalone APK to Your Phone)
Because AI Studio is a web development environment, its **Export** menu provides source code (`.zip`) and **Push to GitHub**. When you push to GitHub, our pre-configured CI/CD workflow builds the standalone APK automatically:
1. In AI Studio, tap **Export** in the top right > **Push to GitHub**.
2. Open your GitHub repository in your phone's browser.
3. Tap **Releases** (or go to **Actions** > latest run).
4. Under **Assets**, tap **`StudyWithAI-v1.0.apk`** to download it directly onto your phone without extracting any zip files.
5. Tap to install!

---

### 💻 Method 2: Offline Build via Android Studio (No GitHub Needed)
If you do not want to use GitHub:
1. In AI Studio, tap **Export** > **Download as .zip file**.
2. Unzip the file on your PC or laptop.
3. Open the folder in **Android Studio**.
4. Click **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)**.
5. Send the compiled `app-debug.apk` to your phone via USB cable, Google Drive, or messaging apps.

---

### ⚡ Method 3: Instant Interactive Preview in AI Studio (Immediate Testing)
You can use the full application right now without downloading anything:
- Tap the **Preview** tab next to **Code** in AI Studio to use the live interactive streaming Android emulator in your browser.

---

### 🚀 Google Play Store Publishing (Ready to Publish)
The app is fully configured to comply with Google Play Developer policies:
- **Target SDK**: Android 16 (API Level 36) — compliant with 2026 Google Play requirements.
- **Min SDK**: Android 7.0 (API Level 24) — compatible with over 95% of active devices.
- **Play Store Bundle (AAB)**: Run `./gradlew bundleRelease` in Android Studio or GitHub Actions.
- **Permissions**: Zero sensitive storage permissions, completely compliant with privacy standards.
- **Monetization & Ads**: Google Mobile Ads (AdMob) integrated with rewarded video ads and interstitial ads.

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

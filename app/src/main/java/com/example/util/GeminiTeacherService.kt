package com.example.util

import android.util.Log
import com.example.BuildConfig
import com.example.model.AITeacher
import com.example.model.TeacherDoubtMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiTeacherService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    suspend fun askTeacherDoubt(
        teacher: AITeacher,
        studentDoubt: String,
        chatHistory: List<TeacherDoubtMessage>
    ): TeacherDoubtResponse = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isNullOrBlank()) {
            return@withContext generateSmartFallbackResponse(teacher, studentDoubt)
        }

        try {
            val systemPrompt = """
                You are ${teacher.name}, a world-class teacher and ${teacher.title}.
                Teaching Style: ${teacher.teachingStyle}
                Subject: ${teacher.subject}
                Expertise: ${teacher.expertise}
                Teaching Philosophy: ${teacher.teachingMotto}
                Personality: ${teacher.personality}
                
                Pedagogical Instructions:
                1. Speak directly, warmly, and encouragingly like an empathetic human teacher in a real 1-on-1 session.
                2. Explain the core intuition first using a simple real-life analogy.
                3. Include a digital chalkboard block using tags [CHALKBOARD]...[/CHALKBOARD] containing formatted step-by-step mathematical derivations, reaction mechanisms, or visual ASCII flowcharts.
                4. Include 2-3 key takeaways inside [TAKEAWAYS]...[/TAKEAWAYS] (separated by newlines).
                5. Ask an engaging Socratic follow-up comprehension question to test if the student truly understood.
                6. Provide 2 short suggested follow-up questions the student might want to ask next inside [FOLLOWUPS]...[/FOLLOWUPS].
            """.trimIndent()

            val contentsArray = JSONArray()

            // Include last 3 turns of history for context
            val recentHistory = chatHistory.takeLast(6)
            for (msg in recentHistory) {
                val role = if (msg.sender == "STUDENT") "user" else "model"
                contentsArray.put(
                    JSONObject().apply {
                        put("role", role)
                        put("parts", JSONArray().put(JSONObject().put("text", msg.message)))
                    }
                )
            }

            // Current question
            contentsArray.put(
                JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().put(JSONObject().put("text", studentDoubt)))
                }
            )

            val requestBodyJson = JSONObject().apply {
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
                })
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 1200)
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey")
                .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: ""
                Log.w("GeminiTeacherService", "Gemini API error ${response.code}: $errorBody")
                return@withContext generateSmartFallbackResponse(teacher, studentDoubt)
            }

            val responseBody = response.body?.string() ?: ""
            val json = JSONObject(responseBody)
            val candidates = json.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val content = candidates.getJSONObject(0).optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val fullText = parts.getJSONObject(0).optString("text", "")
                    return@withContext parseTeacherResponse(fullText, teacher)
                }
            }

            generateSmartFallbackResponse(teacher, studentDoubt)
        } catch (e: Exception) {
            Log.e("GeminiTeacherService", "Exception calling Gemini API", e)
            generateSmartFallbackResponse(teacher, studentDoubt)
        }
    }

    private fun parseTeacherResponse(rawText: String, teacher: AITeacher): TeacherDoubtResponse {
        var mainSpeech = rawText
        var chalkboard: String? = null
        val takeaways = mutableListOf<String>()
        val followups = mutableListOf<String>()

        val chalkboardMatch = Regex("\\[CHALKBOARD\\]([\\s\\S]*?)\\[/CHALKBOARD\\]").find(rawText)
        if (chalkboardMatch != null) {
            chalkboard = chalkboardMatch.groupValues[1].trim()
            mainSpeech = mainSpeech.replace(chalkboardMatch.value, "").trim()
        }

        val takeawaysMatch = Regex("\\[TAKEAWAYS\\]([\\s\\S]*?)\\[/TAKEAWAYS\\]").find(rawText)
        if (takeawaysMatch != null) {
            val listText = takeawaysMatch.groupValues[1].trim()
            listText.lines().filter { it.isNotBlank() }.forEach { line ->
                takeaways.add(line.replace(Regex("^[•\\-*\\d.]+\\s*"), "").trim())
            }
            mainSpeech = mainSpeech.replace(takeawaysMatch.value, "").trim()
        }

        val followupsMatch = Regex("\\[FOLLOWUPS\\]([\\s\\S]*?)\\[/FOLLOWUPS\\]").find(rawText)
        if (followupsMatch != null) {
            val fText = followupsMatch.groupValues[1].trim()
            fText.lines().filter { it.isNotBlank() }.forEach { line ->
                followups.add(line.replace(Regex("^[•\\-*\\d.]+\\s*"), "").trim())
            }
            mainSpeech = mainSpeech.replace(followupsMatch.value, "").trim()
        }

        if (takeaways.isEmpty()) {
            takeaways.add("Core Insight: Build fundamental physical and mathematical models before applying formulas.")
            takeaways.add("Exam Tip: Watch out for boundary conditions and unit conversions.")
        }
        if (followups.isEmpty()) {
            followups.add("Can you show a previous year JEE/NEET question on this?")
            followups.add("What is the most common mistake students make here?")
        }

        return TeacherDoubtResponse(
            teacherSpeech = mainSpeech,
            chalkboardContent = chalkboard,
            keyTakeaways = takeaways,
            followUpPrompts = followups
        )
    }

    private fun generateSmartFallbackResponse(teacher: AITeacher, doubt: String): TeacherDoubtResponse {
        val lowerDoubt = doubt.lowercase()
        return when {
            lowerDoubt.contains("rotation") || lowerDoubt.contains("inertia") || lowerDoubt.contains("torque") -> {
                TeacherDoubtResponse(
                    teacherSpeech = "Great question, champion! In ${teacher.subject}, let's break this down. In linear motion, mass alone resists change in motion. But in rotational dynamics, WHERE that mass is located relative to the axis of rotation is what creates Rotational Inertia (I). That's why spinning with your arms spread is much harder than spinning with your arms tucked in!",
                    chalkboardContent = "★ ROTATIONAL DYNAMICS SUMMARY ★\n\n1. Moment of Inertia: I = ∫ r² dm = ∑ mᵢ rᵢ²\n2. Torque: τ = I · α  (Analogous to F = m · a)\n3. Kinetic Energy: K_rot = ½ I ω²\n4. Parallel Axis Theorem: I = I_com + M · d²\n\nKey Rule: If radius increases 2x, Inertia increases 4x!",
                    keyTakeaways = listOf(
                        "Moment of Inertia depends on mass AND squared distance from rotation axis.",
                        "Parallel Axis theorem requires one axis strictly passing through the Center of Mass.",
                        "In rolling without slipping, total Energy = ½ M v² + ½ I ω²."
                    ),
                    followUpPrompts = listOf(
                        "Show me the derivation of Parallel Axis Theorem",
                        "Which rolls faster: a solid cylinder or a hollow sphere?"
                    )
                )
            }
            lowerDoubt.contains("sn1") || lowerDoubt.contains("sn2") || lowerDoubt.contains("organic") || lowerDoubt.contains("reaction") -> {
                TeacherDoubtResponse(
                    teacherSpeech = "Excellent question! In Organic Chemistry, nucleophiles always look for electron-deficient carbon atoms. SN2 is like an aggressive one-step backside attack causing umbrella-like inversion (Walden Inversion), whereas SN1 is a two-step patient process that forms a flat planar carbocation intermediate!",
                    chalkboardContent = "★ NUCLEOPHILIC SUBSTITUTION MECHANISMS ★\n\n[SN2 Pathway - Bimolecular]:\n• 1 Step Concerted Mechanism\n• Backside attack -> Inversion of configuration\n• Reactivity Order: Methyl > 1° > 2° > 3°\n• Preferred Solvent: Polar Aprotic (Acetone, DMSO)\n\n[SN1 Pathway - Unimolecular]:\n• 2 Step Mechanism via Carbocation\n• Planar intermediate -> Racemic mixture (50% retention, 50% inversion)\n• Reactivity Order: 3° > 2° > 1° (Carbocation Stability)\n• Preferred Solvent: Polar Protic (H₂O, EtOH)",
                    keyTakeaways = listOf(
                        "SN2 rate depends on [Substrate] and [Nucleophile]; favored in 1° halides.",
                        "SN1 rate depends only on [Substrate]; favored in 3° halides.",
                        "Polar aprotic solvents supercharge SN2 by leaving nucleophiles unhindered."
                    ),
                    followUpPrompts = listOf(
                        "Why does polar protic solvent favor SN1?",
                        "What is the difference between E1 and E2 elimination?"
                    )
                )
            }
            lowerDoubt.contains("calculus") || lowerDoubt.contains("integral") || lowerDoubt.contains("derivative") || lowerDoubt.contains("math") -> {
                TeacherDoubtResponse(
                    teacherSpeech = "Welcome! In Mathematics, calculus is the language of continuous change. When finding integrals, always search for hidden symmetry and trigonometric substitutions that collapse the integrand into standard elementary forms.",
                    chalkboardContent = "★ INTEGRAL CALCULUS MASTER HACKS ★\n\n1. King's Property (Definite Integrals):\n   ∫[a to b] f(x) dx = ∫[a to b] f(a + b - x) dx\n\n2. Integration by Parts:\n   ∫ u v dx = u ∫ v dx - ∫ [ u' (∫ v dx) ] dx   (ILATE rule)\n\n3. Standard Form:\n   ∫ eˣ [ f(x) + f'(x) ] dx = eˣ · f(x) + C",
                    keyTakeaways = listOf(
                        "King's property simplifies 70% of JEE Advanced trigonometric definite integrals.",
                        "Always check if the integrand matches the standard eˣ(f(x) + f'(x)) form.",
                        "Symmetry about x = (a+b)/2 allows cancellation of odd terms."
                    ),
                    followUpPrompts = listOf(
                        "Explain King's Property with an example",
                        "How to solve Leibniz rule for differentiating integrals?"
                    )
                )
            }
            else -> {
                TeacherDoubtResponse(
                    teacherSpeech = "Namaste, dear student! As ${teacher.name}, let's tackle this concept together. \"$doubt\" is a high-yield concept. Let's build it from first principles so that you never forget it under exam pressure!",
                    chalkboardContent = "★ CONCEPT MASTER BOARD ★\n\nTopic: ${teacher.subject} • ${teacher.expertise}\n\n1. Define Fundamental Definitions & Boundary Constraints\n2. Establish Governing Laws & Conservation Principles\n3. Execute Dimensional & Unit Verification\n4. Highlight High-Frequency Examiner Traps & Shortcuts",
                    keyTakeaways = listOf(
                        "Always identify given variables and target quantities first.",
                        "Check dimensions and limiting cases (e.g. θ=0, θ=90°) to verify answers.",
                        "Practice structured step-by-step problem breakdown."
                    ),
                    followUpPrompts = listOf(
                        "Can you explain this with a real-life analogy?",
                        "Give me a quick practice question on this concept."
                    )
                )
            }
        }
    }

    private fun getApiKey(): String? {
        return try {
            val buildConfigClass = Class.forName("com.example.BuildConfig")
            val field = buildConfigClass.getField("GEMINI_API_KEY")
            field.get(null) as? String
        } catch (_: Exception) {
            null
        }
    }
}

data class TeacherDoubtResponse(
    val teacherSpeech: String,
    val chalkboardContent: String?,
    val keyTakeaways: List<String>,
    val followUpPrompts: List<String>
)

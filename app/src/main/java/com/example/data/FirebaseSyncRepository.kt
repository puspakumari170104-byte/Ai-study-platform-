package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.db.MistakeEntity
import com.example.data.db.TestRecordEntity
import com.example.data.db.UserEntity
import com.example.data.db.UtrPaymentEntity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseSyncRepository(context: Context) {
    private val tag = "FirebaseSyncRepository"
    private val databaseId = context.getString(R.string.firestore_database_id)
    private val db = FirebaseFirestore.getInstance(databaseId)
    private val auth: FirebaseAuth = Firebase.auth

    val currentUserId: String?
        get() = auth.currentUser?.uid

    val isAuthenticated: Boolean
        get() = auth.currentUser != null

    private fun requireUserId(): String {
        return auth.currentUser?.uid ?: error("User must be authenticated to access Firestore")
    }

    suspend fun saveUserProfile(user: UserEntity) {
        val uid = auth.currentUser?.uid ?: user.id
        val userData = hashMapOf<String, Any>(
            "id" to uid,
            "name" to user.name,
            "email" to (auth.currentUser?.email ?: user.email),
            "preferredLanguage" to user.preferredLanguage,
            "targetExam" to user.targetExam,
            "classLevel" to user.classLevel,
            "targetYear" to user.targetYear,
            "isInstitutePassActive" to user.isInstitutePassActive,
            "streakDays" to user.streakDays,
            "totalStudyMinutes" to user.totalStudyMinutes
        )

        try {
            db.collection("users").document(uid)
                .set(userData, SetOptions.merge())
                .await()
            Log.d(tag, "Successfully synced user profile to Firestore for: $uid")
        } catch (e: Exception) {
            Log.e(tag, "Failed to sync user profile to Firestore", e)
        }
    }

    suspend fun saveMistake(mistake: MistakeEntity) {
        val uid = requireUserId()
        val docId = if (mistake.id > 0) "mistake_${mistake.id}" else "mistake_${System.currentTimeMillis()}"
        val mistakeData = hashMapOf<String, Any>(
            "id" to docId,
            "userId" to uid,
            "programId" to mistake.programId,
            "subjectId" to mistake.subjectId,
            "chapterTitle" to mistake.chapterTitle,
            "questionText" to mistake.questionText,
            "studentAnswer" to mistake.studentAnswer,
            "correctAnswer" to mistake.correctAnswer,
            "explanation" to mistake.explanation,
            "mistakeTypeName" to mistake.mistakeTypeName,
            "isResolved" to mistake.isResolved,
            "studentNotes" to mistake.studentNotes,
            "timestamp" to mistake.timestamp
        )

        try {
            db.collection("users").document(uid)
                .collection("mistakes").document(docId)
                .set(mistakeData, SetOptions.merge())
                .await()
            Log.d(tag, "Synced mistake record: $docId")
        } catch (e: Exception) {
            Log.e(tag, "Failed to sync mistake to Firestore", e)
        }
    }

    suspend fun saveTestRecord(record: TestRecordEntity) {
        val uid = requireUserId()
        val docId = if (record.id > 0) "test_${record.id}" else "test_${System.currentTimeMillis()}"
        val testData = hashMapOf<String, Any>(
            "id" to docId,
            "userId" to uid,
            "testId" to record.testId,
            "title" to record.title,
            "score" to record.score,
            "totalMarks" to record.totalMarks,
            "accuracy" to record.accuracy,
            "timeSpentSeconds" to record.timeSpentSeconds,
            "timestamp" to record.timestamp
        )

        try {
            db.collection("users").document(uid)
                .collection("test_records").document(docId)
                .set(testData, SetOptions.merge())
                .await()
            Log.d(tag, "Synced test record: $docId")
        } catch (e: Exception) {
            Log.e(tag, "Failed to sync test record to Firestore", e)
        }
    }

    suspend fun savePayment(payment: UtrPaymentEntity) {
        val uid = requireUserId()
        val docId = "utr_${payment.utrNumber}"
        val paymentData = hashMapOf<String, Any>(
            "id" to docId,
            "userId" to uid,
            "userName" to payment.userName,
            "utrNumber" to payment.utrNumber,
            "planId" to payment.planId,
            "planName" to payment.planName,
            "amountInr" to payment.amountInr,
            "upiIdUsed" to payment.upiIdUsed,
            "status" to payment.status,
            "timestamp" to payment.timestamp
        )

        try {
            db.collection("users").document(uid)
                .collection("payments").document(docId)
                .set(paymentData, SetOptions.merge())
                .await()
            Log.d(tag, "Synced payment transaction: $docId")
        } catch (e: Exception) {
            Log.e(tag, "Failed to sync payment to Firestore", e)
        }
    }

    fun observeRemoteProfile(userId: String): Flow<UserEntity?> = callbackFlow {
        var listener: ListenerRegistration? = null
        try {
            listener = db.collection("users").document(userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e(tag, "Profile listener error", error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val entity = UserEntity(
                            id = snapshot.getString("id") ?: userId,
                            name = snapshot.getString("name") ?: "Aman Kumar",
                            preferredLanguage = snapshot.getString("preferredLanguage") ?: "HINDI",
                            targetExam = snapshot.getString("targetExam") ?: "NEET UG",
                            classLevel = snapshot.getString("classLevel") ?: "Class 12",
                            targetYear = snapshot.getString("targetYear") ?: "2026",
                            email = snapshot.getString("email") ?: "",
                            streakDays = (snapshot.getLong("streakDays") ?: 14).toInt(),
                            totalStudyMinutes = (snapshot.getLong("totalStudyMinutes") ?: 3480).toInt(),
                            isInstitutePassActive = snapshot.getBoolean("isInstitutePassActive") ?: false,
                            isRegistered = true
                        )
                        trySend(entity)
                    } else {
                        trySend(null)
                    }
                }
        } catch (e: Exception) {
            Log.e(tag, "Failed to attach profile listener", e)
        }

        awaitClose {
            listener?.remove()
        }
    }
}

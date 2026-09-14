package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserById(userId: String = "user_default_1"): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    fun getDefaultUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET preferredLanguage = :language, updatedAt = :timestamp WHERE id = :userId")
    suspend fun updateLanguage(userId: String, language: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE users SET subscriptionStatus = :status, isInstitutePassActive = :isPassActive, unlockedPlanName = :planName, utrNumber = :utr, updatedAt = :timestamp WHERE id = :userId")
    suspend fun updateSubscriptionStatus(
        userId: String,
        status: String,
        isPassActive: Boolean,
        planName: String,
        utr: String,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("UPDATE users SET name = :name, preferredLanguage = :language, targetExam = :targetExam, classLevel = :classLevel, targetYear = :targetYear, isRegistered = 1, updatedAt = :timestamp WHERE id = :userId")
    suspend fun registerUser(
        userId: String,
        name: String,
        language: String,
        targetExam: String,
        classLevel: String,
        targetYear: String,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}

package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CoachingDao {
    @Query("SELECT * FROM mistakes WHERE programId = :programId ORDER BY timestamp DESC")
    fun getMistakesByProgram(programId: String): Flow<List<MistakeEntity>>

    @Query("SELECT * FROM mistakes ORDER BY timestamp DESC")
    fun getAllMistakes(): Flow<List<MistakeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: MistakeEntity)

    @Update
    suspend fun updateMistake(mistake: MistakeEntity)

    @Query("UPDATE mistakes SET isResolved = :isResolved WHERE id = :id")
    suspend fun updateMistakeResolution(id: Int, isResolved: Boolean)

    @Query("UPDATE mistakes SET mistakeTypeName = :newType WHERE id = :id")
    suspend fun updateMistakeClassification(id: Int, newType: String)

    @Query("UPDATE mistakes SET studentNotes = :notes WHERE id = :id")
    suspend fun updateMistakeNotes(id: Int, notes: String)

    @Query("UPDATE mistakes SET revisionCount = revisionCount + 1, lastRevisedTimestamp = :timestamp WHERE id = :id")
    suspend fun recordMistakeRevision(id: Int, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM mistakes WHERE id = :id")
    suspend fun deleteMistake(id: Int)

    @Query("SELECT * FROM test_records WHERE programId = :programId ORDER BY timestamp DESC")
    fun getTestRecords(programId: String): Flow<List<TestRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestRecord(record: TestRecordEntity)

    @Query("SELECT * FROM student_profile WHERE id = 1")
    fun getStudentProfile(): Flow<StudentProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: StudentProfileEntity)

    @Query("UPDATE student_profile SET selectedLanguage = :language WHERE id = 1")
    suspend fun updateStudentLanguage(language: String)

    @Query("UPDATE student_profile SET isInstitutePassActive = :isActive, unlockedPlanId = :planId, unlockedPlanName = :planName, utrNumber = :utr, planUnlockedTimestamp = :timestamp WHERE id = 1")
    suspend fun unlockInstitutePass(isActive: Boolean, planId: String, planName: String, utr: String, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM utr_payments ORDER BY timestamp DESC")
    fun getAllUtrPayments(): Flow<List<UtrPaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUtrPayment(payment: UtrPaymentEntity)

    @Query("SELECT * FROM bookmarked_notes WHERE programId = :programId ORDER BY timestamp DESC")
    fun getNotes(programId: String): Flow<List<BookmarkedNoteEntity>>

    @Query("SELECT * FROM bookmarked_notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<BookmarkedNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: BookmarkedNoteEntity)

    @Query("UPDATE bookmarked_notes SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateNoteBookmark(id: Int, isBookmarked: Boolean)

    @Query("UPDATE bookmarked_notes SET personalAnnotation = :annotation WHERE id = :id")
    suspend fun updateNoteAnnotation(id: Int, annotation: String)

    @Query("UPDATE bookmarked_notes SET highlightedPhrases = :highlights WHERE id = :id")
    suspend fun updateNoteHighlights(id: Int, highlights: String)

    @Query("DELETE FROM bookmarked_notes WHERE id = :id")
    suspend fun deleteNote(id: Int)
}

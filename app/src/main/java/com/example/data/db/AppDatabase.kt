package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "study_with_ai_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedInitialData(database)
                    }
                }
            }
        }

        private suspend fun seedInitialData(database: AppDatabase) {
            database.userDao().insertOrUpdateUser(
                UserEntity(
                    id = "user_default_1",
                    name = "Aman Kumar",
                    preferredLanguage = "HINDI",
                    subscriptionStatus = "FREE",
                    targetExam = "NEET UG",
                    classLevel = "Class 12",
                    targetYear = "2026",
                    streakDays = 14,
                    totalStudyMinutes = 3480,
                    isRegistered = true,
                    isInstitutePassActive = false
                )
            )
            val initialCourses = listOf(
                CourseEntity(
                    id = "course_neet_ug_2026",
                    title = "NEET UG Comprehensive Ranker Batch (Physics, Chem, Bio)",
                    examCategory = "NEET",
                    description = "Complete NCERT line-by-line mastery with AI Socratic faculty and daily practice numericals.",
                    instructorName = "Dr. Vikram Seth & Dr. Ananya Sharma",
                    totalLectures = 140,
                    totalDurationHours = 180,
                    rating = 4.95,
                    enrolledCount = 4820,
                    isEnrolled = true,
                    isProOnly = false,
                    priceInr = 0,
                    badge = "Top Rated"
                ),
                CourseEntity(
                    id = "course_jee_adv_2026",
                    title = "JEE Main & Advanced Kota Pedagogy Masterclass",
                    examCategory = "JEE",
                    description = "Advanced mechanics, calculus, organic synthesis with high-yield shortcut tricks and PYQs.",
                    instructorName = "Prof. R.K. Verma & Er. Alok Sinha",
                    totalLectures = 160,
                    totalDurationHours = 210,
                    rating = 4.92,
                    enrolledCount = 3910,
                    isEnrolled = true,
                    isProOnly = false,
                    priceInr = 0,
                    badge = "Bestseller"
                )
            )
            database.courseDao().insertCourses(initialCourses)
        }
    }
}

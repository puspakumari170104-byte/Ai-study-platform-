package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UtrPaymentDao {
    @Query("SELECT * FROM utr_payments ORDER BY timestamp DESC")
    fun getAllUtrPayments(): Flow<List<UtrPaymentEntity>>

    @Query("SELECT * FROM utr_payments WHERE userId = :userId ORDER BY timestamp DESC")
    fun getPaymentsByUserId(userId: String): Flow<List<UtrPaymentEntity>>

    @Query("SELECT * FROM utr_payments WHERE utrNumber = :utr LIMIT 1")
    fun getPaymentByUtr(utr: String): Flow<UtrPaymentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUtrPayment(payment: UtrPaymentEntity)

    @Update
    suspend fun updatePayment(payment: UtrPaymentEntity)

    @Query("UPDATE utr_payments SET status = :newStatus WHERE utrNumber = :utr")
    suspend fun updatePaymentStatus(utr: String, newStatus: String)

    @Query("DELETE FROM utr_payments WHERE id = :id")
    suspend fun deletePayment(id: Int)
}

package com.example.assigment.ui.theme.Dao

import androidx.room.*
import com.example.assigment.ui.theme.Entity.Account
import com.example.assigment.ui.theme.Enum.RoleType
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("SELECT * FROM accounts WHERE accountId = :accountId")
    suspend fun getAccountById(accountId: String): Account?

    @Query("SELECT * FROM accounts WHERE email = :email")
    suspend fun getAccountByEmail(email: String): Account?

    @Query("SELECT * FROM accounts WHERE role = :role")
    fun getAccountsByRole(role: RoleType): Flow<List<Account>>

    @Query("SELECT * FROM accounts WHERE role = 'PROVIDER'")
    fun getAllProviders(): Flow<List<Account>>

    @Query("SELECT * FROM accounts WHERE role = 'REQUESTER'")
    fun getAllRequesters(): Flow<List<Account>>

    @Query("SELECT * FROM accounts WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): Account?

    @Query("UPDATE accounts SET rating = :rating WHERE accountId = :accountId")
    suspend fun updateProviderRating(accountId: String, rating: Float)


    @Query("SELECT * FROM accounts WHERE role = 'PROVIDER' AND rating >= :minRating")
    fun getProvidersByRating(minRating: Float): Flow<List<Account>>
} 
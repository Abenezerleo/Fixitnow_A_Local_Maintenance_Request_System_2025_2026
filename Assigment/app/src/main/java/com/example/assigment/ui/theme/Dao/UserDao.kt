package com.example.assigment.ui.theme.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.assigment.ui.theme.Entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: User): Long

    @Update
    suspend fun editUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM User WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("UPDATE User SET isActive = 0 WHERE userId = :userId")
    suspend fun deactivateUser(userId: Long)

    @Query("SELECT * FROM User ORDER BY name ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT COUNT(*) FROM User")
    suspend fun getTotalUserCount(): Int

    @Query("SELECT COUNT(*) FROM User WHERE isActive = 1")
    suspend fun getActiveUserCount(): Int
}
package com.technology.mycow.spargrisenkt.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.technology.mycow.spargrisenkt.db.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user : User)

    @Update
    fun updateUser(user : User)

    @Query("DELETE from user")
    fun deleteAllUser()

    @Query("DELETE from user WHERE userid = :userId")
    fun deleteUser(userId : Long)

    @Query("SELECT * from user ORDER BY userid ASC")
    fun getAllUser() : LiveData<List<User>>

    @Query("SELECT * from user WHERE userid = :userId")
    fun getUserByUserId(userId: Long) : LiveData<User>

}
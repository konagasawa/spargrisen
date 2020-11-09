package com.technology.mycow.spargrisenkt.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
//import com.technology.mycow.spargrisenkt.db.entity.TaskWithUser

//@Dao
//interface TaskWithUserDao {

//    //START: UserChild - TaskByUserChild, One to Many Relationship
//    @Transaction
//    @Query("SELECT * from user WHERE iconid = :iconId")
//    fun getTaskWithUserByIconId(iconId: Int): LiveData<List<TaskWithUser>>
//    //END: UserChild - TaskByUserChild, One to Many Relationship


//}
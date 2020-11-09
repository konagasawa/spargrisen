package com.technology.mycow.spargrisenkt.db.dao

//import androidx.room.*
//import com.technology.mycow.spargrisenkt.db.entity.TaskList
//import com.technology.mycow.spargrisenkt.db.entity.UserWithTaskList
//import com.technology.mycow.spargrisenkt.db.relation.UserWithTaskListsAndTasks
//
//@Dao
//interface UserWithTaskListAndTasksDao {
//
//    @Transaction
//    @Query("SELECT * FROM user")
//    fun getUserWithTaskList() : List<UserWithTaskList>
//
//    @Transaction
//    @Query("SELECT * FROM user")
//    fun getUsersWithTaskListAndTasks() : List<UserWithTaskListsAndTasks>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertTaskList(taskList: TaskList)
//
//}
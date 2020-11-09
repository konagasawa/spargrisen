package com.technology.mycow.spargrisenkt.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.technology.mycow.spargrisenkt.db.entity.Task
import com.technology.mycow.spargrisenkt.db.entity.TaskAssignUpdate
import com.technology.mycow.spargrisenkt.db.entity.TaskCompleteUpdate
import com.technology.mycow.spargrisenkt.db.entity.TaskWithPointAndStatusAndUser

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Update(entity = Task::class)
    fun updateTaskToAssign(taskAssignUpdate: TaskAssignUpdate)

    @Update(entity = Task::class)
    fun updateTaskToComplete(taskCompleteUpdate: TaskCompleteUpdate)

    @Query("DELETE FROM task")
    fun deleteAllTask()

    @Query("DELETE FROM task WHERE taskid = :taskId")
    fun deleteTask(taskId: Long)

    @Query("DELETE FROM task WHERE usercreatorid =:userId")
    fun deleteAllTaskByUser(userId: Long)

    @Query("SELECT * FROM task ORDER BY taskid DESC")
    fun getAllTask() : LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE usercreatorid =:userId ORDER BY taskid DESC")
    fun getTaskByUser(userId: Long) : LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE usercreatorid =:userId AND taskstatus =:taskStatus ORDER BY taskid DESC")
    fun getTaskByUserSortedByStatus(userId: Long, taskStatus: String): LiveData<List<Task>>

    @Query("SELECT taskid, usercreatorid, pointoftask, completeddate FROM task WHERE usercreatorid =:userId AND (completeddate > :startDate AND completeddate < :endDate) ")
    fun getPointOfTask(userId: Long, startDate: Long, endDate: Long): List<TaskWithPointAndStatusAndUser>

    @Query("SELECT * FROM task WHERE taskstatus =:taskStatus ORDER BY taskid DESC")
    fun getTaskWithSortedByStatus(taskStatus: String): LiveData<List<Task>>

}
package com.technology.mycow.spargrisenkt.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskid")
    var taskId : Long,

    @ColumnInfo(name = "usercreatorid") //100 is a default as not assigned to user. Assign userID if task is assigned to user
    var userCreatorId: Long,

    @ColumnInfo(name = "taskname")
    var taskName: String,

    @ColumnInfo(name = "taskdescription")
    var taskDescription: String,

    @ColumnInfo(name = "pointoftask")
    var pointOfTask: Int,

    @ColumnInfo(name = "taskstatus")
    var taskStatus: String,

    @ColumnInfo(name = "createddate")
    var createdDate: Long,

    @ColumnInfo(name = "assigneddate")
    var assignedDate: Long,

    @ColumnInfo(name = "completeddate")
    var completedDate: Long


)

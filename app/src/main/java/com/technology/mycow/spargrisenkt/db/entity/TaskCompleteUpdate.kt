package com.technology.mycow.spargrisenkt.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completetaskupdate")
class TaskCompleteUpdate {

    @PrimaryKey
    @ColumnInfo(name = "taskid")
    var taskId: Long = 0 //100 as default before

    @ColumnInfo(name = "taskstatus")
    var taskStatus: String = ""

    @ColumnInfo(name = "completeddate")
    var completedDate: Long = 0

}
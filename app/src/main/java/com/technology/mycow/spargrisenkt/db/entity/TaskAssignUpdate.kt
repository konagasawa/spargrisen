package com.technology.mycow.spargrisenkt.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assigntaskupdate")
class TaskAssignUpdate {
//    @ColumnInfo
//    @PrimaryKey(autoGenerate = true)
//    var taskAssignUdpateId: Long = 0

    @PrimaryKey
    @ColumnInfo(name = "taskid")
    var taskId: Long = 0 //100 as default before

    @ColumnInfo(name = "usercreatorid")
    var userCreatorId: Long = 0 //100 as default before

    @ColumnInfo(name = "taskstatus")
    var taskStatus: String = ""

    @ColumnInfo(name = "assigneddate")
    var assignedDate: Long = 0
}


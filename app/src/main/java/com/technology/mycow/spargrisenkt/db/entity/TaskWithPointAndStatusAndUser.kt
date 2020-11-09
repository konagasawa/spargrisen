package com.technology.mycow.spargrisenkt.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskwithpointandstatusanduser")
class TaskWithPointAndStatusAndUser {

    @PrimaryKey
    @ColumnInfo(name = "taskid")
    var taskId: Long = 0 //100 as default before

    @ColumnInfo(name = "usercreatorid")
    var userCreatorId: Long = 0 //100 as default before

    @ColumnInfo(name = "pointoftask")
    var pointOfTask: Int = 0

    @ColumnInfo(name = "completeddate")
    var completedDate: Long = 0

}
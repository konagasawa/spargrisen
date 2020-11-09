package com.technology.mycow.spargrisenkt.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(

    //TODO: dataBinding to give a default value for all entities
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userid")
    var userId : Long,

    @ColumnInfo(name = "username")
    var userName : String,

    @ColumnInfo(name = "iconTag") //current iconid is set by mutablelist index. 100 is a default (non assigned), set by repository
    var iconTag: String,

    @ColumnInfo(name = "rateofpoint")
    var rateOfPoint: Int,

    @ColumnInfo(name = "userstatus")
    var userStatus : String
)
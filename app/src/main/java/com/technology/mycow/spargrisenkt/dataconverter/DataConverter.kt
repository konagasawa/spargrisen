package com.technology.mycow.spargrisenkt.dataconverter

import androidx.room.TypeConverter
import java.util.*

object DataConverter {
    @TypeConverter
    fun fromTimestamp(timestamp : Long?) : Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date : Date?) : Long? {
        return date?.time
    }


}
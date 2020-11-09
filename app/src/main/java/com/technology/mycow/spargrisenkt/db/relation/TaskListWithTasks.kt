package com.technology.mycow.spargrisenkt.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.technology.mycow.spargrisenkt.db.entity.Task
//import com.technology.mycow.spargrisenkt.db.entity.TaskList
//import com.technology.mycow.spargrisenkt.db.entity.TaskListTaskCrossRef

//data class TaskListWithTasks (
//
//    @Embedded val taskList: TaskList,
//    @Relation(
//        parentColumn = "tasklistid",
//        entityColumn = "taskid",
//        associateBy = Junction(TaskListTaskCrossRef::class) //may require @Junction instead of Junction
//    )
//    val tasks: List<Task>
//
//)
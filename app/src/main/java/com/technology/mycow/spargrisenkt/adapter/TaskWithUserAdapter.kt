package com.technology.mycow.spargrisenkt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.technology.mycow.spargrisenkt.R
//import com.technology.mycow.spargrisenkt.db.entity.TaskWithUser

class TaskWithUserAdapter internal constructor(context: Context) : RecyclerView.Adapter<TaskWithUserAdapter.TaskWithUserViewHolder>(){

    private val mLayoutInflater = LayoutInflater.from(context)
//    private var mTaskWithUser = emptyList<TaskWithUser>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskWithUserViewHolder {
        val view = mLayoutInflater.inflate(R.layout.task_list_item, parent, false)
        return TaskWithUserViewHolder(view)
    }


    override fun onBindViewHolder(holder: TaskWithUserViewHolder, position: Int) {
//        var taskWithUser = mTaskWithUser[position]
//        if(taskWithUser != null){
//
//
//
////            holder.mTaskName.text = taskWithUser.get.taskName
////            holder.mPointOfTask.text = taskWithUser.pointOfTask.toString()
//        } else {
//            holder.mTaskName.text = "NO ENTRY"
//        }
    }

    override fun getItemCount(): Int {
        var taskSize = 0
//        if(mTaskWithUser != null){
//            taskSize = mTaskWithUser.size
//        }
        return taskSize
    }

    inner class TaskWithUserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTaskName: TextView
        var mPointOfTask: TextView

        init {
            mTaskName = itemView.findViewById(R.id.taskListTaskNameTv)
            mPointOfTask = itemView.findViewById(R.id.taskListPointOfTaskTv)
        }

    }
}

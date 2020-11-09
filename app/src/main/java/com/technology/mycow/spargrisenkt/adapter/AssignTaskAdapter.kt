package com.technology.mycow.spargrisenkt.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.db.entity.Task
import com.technology.mycow.spargrisenkt.view.MainActivity

class AssignTaskAdapter internal constructor(context: Context): RecyclerView.Adapter<AssignTaskAdapter.AssignTaskViewHolder>(){

    private val mInflater : LayoutInflater = LayoutInflater.from(context)
    private var mTask = emptyList<Task>()
    private var mTaskPositionAt: Task? = null
    lateinit var mHolderItemTouchedListener: HolderItemTouchedListener

    private var fm: FragmentManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignTaskViewHolder {
        val view = mInflater.inflate(R.layout.task_list_item, parent, false)
        return AssignTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignTaskViewHolder, position: Int) {
        var taskItem = mTask[position]
        if(taskItem != null){
            holder.mTaskName.text = taskItem.taskName
            holder.mTaskPoint.text = taskItem.pointOfTask.toString()
        } else {
            holder.mTaskName.text = "NO ENTRY"
        }
    }

    fun setTask(task: List<Task>){
        mTask = task
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        var taskSize = 0
        if(mTask != null){
            taskSize = mTask.size
        }
        return taskSize
    }

    fun getTaskPositionAt(): Task? {
        return mTaskPositionAt
    }

    fun setTaskPositionAt(position: Int){
        mTaskPositionAt = mTask[position]
    }

    fun getFragmentManager(fragmentManager: FragmentManager){
        fm = fragmentManager
    }

    fun setOnHolderItemTouchedListener(holderItemTouchedListener: HolderItemTouchedListener){
        mHolderItemTouchedListener = holderItemTouchedListener
    }

    inner class AssignTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mTaskName: TextView
        val mTaskPoint: TextView
        var view: View = itemView


        init {
            mTaskName = itemView.findViewById<TextView>(R.id.taskListTaskNameTv)
            mTaskPoint = itemView.findViewById<TextView>(R.id.taskListPointOfTaskTv)
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            Log.d(LOG_MSG, "TOUCH: " + adapterPosition)
            setTaskPositionAt(adapterPosition)
            mHolderItemTouchedListener.onHolderItemTouchedListener(mTaskPositionAt!!)
        }

    }

    interface HolderItemTouchedListener {
        fun onHolderItemTouchedListener(taskPositionAt: Task)
    }

    companion object {
        private const val LOG_MSG = "Assign_Task_Adapter_Log"
    }


}
package com.technology.mycow.spargrisenkt.adapter


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.db.entity.Task
import org.w3c.dom.Text

class TaskListAdapter internal constructor(context: Context): RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>(){

    private val mLayoutInfrater: LayoutInflater = LayoutInflater.from(context)
    private var mTask = emptyList<Task>()

    private var mTaskPositionAt: Task? = null
    lateinit var mHolderTaskItemTouchedListener: HolderTaskItemTouchedListener
    lateinit var mHolderTaskItemClickedListener: HolderTaskItemClickedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val view = mLayoutInfrater.inflate(R.layout.task_list_item, parent, false)
        return TaskListViewHolder(view)
    }


    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        var task = mTask[position]
        if(task != null){
            holder.mTaskName.text = task.taskName
            holder.mPointOfTask.text = task.pointOfTask.toString()
            when (task.taskStatus) {
                holder.taskStatusOngoing -> holder.mTaskStatus.setTextColor(holder.taskStatusOngoingColor)
                holder.taskStatusCompleted -> holder.mTaskStatus.setTextColor(holder.taskStatusCompletedColor)
                holder.taskStatusUnassigned -> holder.mTaskStatus.setTextColor(holder.taskStatusUnassignedColor)
                else -> holder.mTaskStatus.setTextColor(Color.WHITE)
            }
            holder.mTaskStatus.text = task.taskStatus
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

    fun removeTaskCanceled(){
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        Log.d(LOG_MSG, "TOUCH: " + position)
        setTaskPositionAt(position)
        mHolderTaskItemTouchedListener.onHolderTaskItemTouchedListener(mTaskPositionAt!!)
    }

    fun setHolderTaskItemTouchedListener(holderTaskItemTouchedListener: HolderTaskItemTouchedListener){
        mHolderTaskItemTouchedListener = holderTaskItemTouchedListener
    }

    fun setHolderTaskItemClickedListener(holderTaskItemClickedListener: HolderTaskItemClickedListener){
        mHolderTaskItemClickedListener = holderTaskItemClickedListener
    }

    inner class TaskListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mTaskName : TextView
        var mPointOfTask: TextView
        var mTaskStatus : TextView
        var taskStatusOngoing : String
        var taskStatusCompleted : String
        var taskStatusUnassigned : String
        var taskStatusOngoingColor : Int
        var taskStatusCompletedColor : Int
        var taskStatusUnassignedColor : Int

        init {
            mTaskName = itemView.findViewById(R.id.taskListTaskNameTv)
            mPointOfTask = itemView.findViewById(R.id.taskListPointOfTaskTv)
            mTaskStatus = itemView.findViewById(R.id.taskListTaskStatus)
            taskStatusOngoing = itemView.resources.getString(R.string.taskOngoing)
            taskStatusCompleted = itemView.resources.getString(R.string.taskCompleted)
            taskStatusUnassigned = itemView.resources.getString(R.string.taskUnassigned)

            taskStatusOngoingColor = itemView.resources.getColor(R.color.colorTaskListItemStatusOngoingText, null)
            taskStatusCompletedColor = itemView.resources.getColor(R.color.colorTaskListItemStatusCompletedText, null)
            taskStatusUnassignedColor = itemView.resources.getColor(R.color.colorTaskListItemStatusUnassignedText, null)

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.d(LOG_MSG, "TOUCH: " + adapterPosition)
            setTaskPositionAt(adapterPosition)
            val selectedTask = getTaskPositionAt()
            mHolderTaskItemClickedListener.onHolderItemClickedListener(selectedTask!!)
        }

    }


    interface HolderTaskItemTouchedListener{
        fun onHolderTaskItemTouchedListener(taskPositionAt: Task)
    }

    interface HolderTaskItemClickedListener {
        fun onHolderItemClickedListener(taskPositionAt : Task)
    }

    companion object {
        private const val LOG_MSG = "TaskListAdapter_LOG"
    }

}
package com.technology.mycow.spargrisenkt.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.adapter.TaskListAdapter
import com.technology.mycow.spargrisenkt.db.entity.Task
import com.technology.mycow.spargrisenkt.helper.SwipeToDeleteCallback
import com.technology.mycow.spargrisenkt.model.ConstantCollection
import com.technology.mycow.spargrisenkt.model.SetArgumentsToFragment
import com.technology.mycow.spargrisenkt.viewmodel.TaskViewModel

class TaskListFragment : Fragment(), ActionPromptDialogFragment.ActionPromptDialogListener{

    private var args = Bundle()
    private var mTaskViewModel: TaskViewModel? = null
    private var mTask = mutableListOf<Task>()
    //private var mTaskToDelete: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(mTaskViewModel == null){
            val factory = TaskViewModel.Factory(requireActivity().application)
            mTaskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)
        }

    }

    private fun subscribeToViewModel(){
        val mTaskListAdapter = TaskListAdapter(requireContext())
        val mTaskListRecyclerView = ((activity) as MainActivity).findViewById<RecyclerView>(R.id.listRecyclerView)
        mTaskListRecyclerView.adapter = mTaskListAdapter
        mTaskListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(requireContext(), mTaskListAdapter))
        itemTouchHelper.attachToRecyclerView(mTaskListRecyclerView)

        if(mTaskViewModel != null){
            mTaskViewModel!!.task.observe(viewLifecycleOwner, Observer { createdTask ->
                Log.d(TaskListFragment.LOG_MSG, "TASK CREATED")
                mTaskListAdapter.setTask(createdTask)
                mTaskListAdapter.setHolderTaskItemTouchedListener(holderTaskItemTouchListener())
                mTaskListAdapter.setHolderTaskItemClickedListener(holderTaskItemClickListener())
            })
        }

        if(mTaskViewModel?.mIsTaskDeleteCanceled != null){
            mTaskViewModel!!.mIsTaskDeleteCanceled.observe(viewLifecycleOwner, Observer { newValue ->
                mTaskListAdapter.removeTaskCanceled()
            })
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.task_list_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        subscribeToViewModel()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val LOG_MSG = "TASK_LIST_LOG"

        val sInstance : Fragment
            get() = TaskListFragment()
    }

    fun holderTaskItemTouchListener(): TaskListAdapter.HolderTaskItemTouchedListener{
        return object: TaskListAdapter.HolderTaskItemTouchedListener {
            override fun onHolderTaskItemTouchedListener(taskPositionAt: Task) {
                mTaskViewModel?.mTaskToDelete?.postValue(taskPositionAt)
                showActionPromptDialog()
            }
        }
    }

    fun holderTaskItemClickListener(): TaskListAdapter.HolderTaskItemClickedListener{
        return object: TaskListAdapter.HolderTaskItemClickedListener {
            override fun onHolderItemClickedListener(taskPositionAt: Task) {
                openAnotherFragment(taskPositionAt)
            }
        }
    }

    fun openAnotherFragment(taskPositionAt: Task){

        var createTaskFragment: Fragment = CreateTaskFragment.sInstance

        args.putString(ConstantCollection.TASK_ITEM_NAME, taskPositionAt.taskName)
        args.putString(ConstantCollection.TASK_ITEM_DESCRIPTION, taskPositionAt.taskDescription)
        args.putString(ConstantCollection.TASK_ITEM_POINT_OF_TASK, (taskPositionAt.pointOfTask).toString())
        createTaskFragment.arguments = args

        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.taskListFragment, createTaskFragment, ConstantCollection.FRAGMENT_TAG_TASK_INFO)
            .addToBackStack(null)
            .commit()

    }

    fun showActionPromptDialog() {
        // Create an instance of the dialog fragment and show it

        var mArgs = Bundle()
        mArgs.putInt(ConstantCollection.DIALOG_TITLE_ID, R.string.action_prompt_delete_task_title)
        mArgs.putInt(ConstantCollection.DIALOG_MESSAGE_ID, R.string.action_prompt_delete_task_message)
        val dialog = ActionPromptDialogFragment()
        dialog.arguments = mArgs
        dialog.setTargetFragment(this, 100)
        dialog.show(requireActivity().supportFragmentManager, "ActionPromptDialogFragmentByTaskList")

    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        var aa = mTaskViewModel?.mTaskToDelete?.value?.taskId
        if(mTaskViewModel?.mTaskToDelete != null){
            mTaskViewModel?.deleteTask(mTaskViewModel!!.mTaskToDelete.value!!.taskId)
            mTaskViewModel?.mIsTaskDeleteCanceled?.postValue(false)
        }
        Log.d(LOG_MSG, "POSITIVE")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Log.d(LOG_MSG, "NEGATIVE")
        mTaskViewModel?.mIsTaskDeleteCanceled?.postValue(true)
    }


}
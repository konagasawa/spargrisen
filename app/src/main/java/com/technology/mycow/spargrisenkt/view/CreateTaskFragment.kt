package com.technology.mycow.spargrisenkt.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.GestureDetectorCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.technology.mycow.spargrisenkt.AppExecutors
import com.technology.mycow.spargrisenkt.R
//import com.technology.mycow.spargrisenkt.databinding.CreateUserFragmentBinding
import com.technology.mycow.spargrisenkt.db.SpargrisenDB
import com.technology.mycow.spargrisenkt.db.entity.Task
import com.technology.mycow.spargrisenkt.db.entity.TaskCompleteUpdate
import com.technology.mycow.spargrisenkt.helper.TouchGestureListener
import com.technology.mycow.spargrisenkt.model.ConstantCollection
import com.technology.mycow.spargrisenkt.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.create_task_fragment.*
import java.util.*

class CreateTaskFragment : Fragment() {

    //var mCreateTaskFragmentBinding : CreateUserFragmentBinding? = null
    private var mTaskViewModel : TaskViewModel? = null
    private var mSpargrisenDB : SpargrisenDB? = null

    private lateinit var fragmentTag : String
    private lateinit var taskNameEt : EditText
    private lateinit var taskDescriptionEt : EditText
    private lateinit var pointEt : EditText
    private lateinit var taskViewTitle : TextView
    private lateinit var createTaskButton : ImageButton
    private lateinit var completeTaskButton : ImageButton
    private lateinit var backTaskButton: ImageButton
    private lateinit var completedTaskButton : ImageButton
    private lateinit var createTaskButtonLabel : TextView
    private lateinit var completeTaskButtonLabel : TextView
    private lateinit var completedTaskButtonLabel : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //SET SOFT KEYBOARD PUSH VIEW
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        if(mTaskViewModel == null){
            val factory = TaskViewModel.Factory(requireActivity().application)
            mTaskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)
        }
        mSpargrisenDB = SpargrisenDB.getInstance(requireContext().applicationContext, AppExecutors())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        mCreateTaskFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.create_task_fragment, container, false)
//        return mCreateTaskFragmentBinding!!.root
        return inflater.inflate(R.layout.create_task_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

         val createTaskLayout = requireActivity().findViewById<NestedScrollView>(R.id.createTaskNestedScrollView)
        createTaskLayout.setOnTouchListener(object : TouchGestureListener(requireContext()){
            override fun onSwipeToRight() {
                Log.d(LOG_MSG, "SWIPE RIGHT")
                requireActivity().supportFragmentManager.popBackStack(null,0)
            }

            override fun onSwipeToLeft() {
                Log.d(LOG_MSG, "SWIPE LEFT")
                requireActivity().supportFragmentManager.popBackStack(null,0)
            }
        })

        setViewProperties()

        //COMPLETE TASK
        completeTaskButton.setOnClickListener{
            val date = Date().time
            val taskCompleteUpdate: TaskCompleteUpdate = TaskCompleteUpdate()
            taskCompleteUpdate.taskId = requireArguments().getLong(ConstantCollection.TASK_ITEM_ID)
            taskCompleteUpdate.completedDate = date
            taskCompleteUpdate.taskStatus = resources.getString(R.string.taskCompleted)
            mTaskViewModel?.updateTaskToComplete(taskCompleteUpdate)
            //showToastMessage("Congratulation!! \n\"${arguments?.getString(ConstantCollection.TASK_ITEM_NAME)}\" Is Completed")

            showAnimationAndBack()
        }

        //BACK TO TASK LIST FRAGMENT
        backTaskButton.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack(null,0)
        }

        createTaskButton?.setOnClickListener {
            if(!taskNameEt?.text.toString().isBlank() && !pointEt?.text.toString().isBlank()){
                val taskName = taskNameEt?.text.toString()
                val taskDescription = taskDescriptionEt?.text.toString()
                val pointStr = pointEt?.text.toString()
                val pointInt = pointStr.toInt()

                if(mSpargrisenDB != null){
                    val date = Date().time
                    val task = Task(0,
                        100,
                        taskName,
                        taskDescription,
                        pointInt,
                        resources.getString(R.string.taskUnassigned),
                        date,
                        0,
                        0)
                    mTaskViewModel!!.insertTask(task)
                    clearInputEntries()
                    showToastMessage("New Task \"${taskName}\" Is Created")
                }
            } else {
                showToastMessage("Task Name & Point of Task Is Required!!")
            }
        }

        softKeyboardHide()


    }

    private val argsButtonId : Int?
        get() {
            return arguments.let { this.arguments?.getInt(ConstantCollection.USER_BUTTON_TAG) }
        }

    private fun clearInputEntries(){
        taskNameEt.text.clear()
        taskDescriptionEt.text.clear()
        pointEt.text.clear()
    }

    private fun showAnimationAndBack(){

        var taskCompletionAnimationFragment : Fragment = TaskCompletionAnimationFragment.sInstance
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.contentMain, taskCompletionAnimationFragment, ConstantCollection.FRAGMENT_TAG_COMPLETE_TASK)
                .addToBackStack(ConstantCollection.FRAGMENT_TAG_TASK_LIST_BY_USER)
                .commit()
//        val intent = Intent(requireContext(), CelebrateCompleteValueAnimatorAnimationActivity::class.java)
//        startActivity(intent)



    }

    private fun setViewProperties(){

        fragmentTag = this.tag.toString() //return this fragment tag, that is "CREATE_TASK_FRAGMENT"
        taskNameEt = requireActivity().findViewById<EditText>(R.id.taskNameEt)
        taskDescriptionEt = requireActivity().findViewById<EditText>(R.id.taskDescriptionEt)
        pointEt = requireActivity().findViewById<EditText>(R.id.pointEt)
        taskViewTitle = requireActivity().findViewById<TextView>(R.id.taskTitle)
        createTaskButton = requireActivity().findViewById<ImageButton>(R.id.createTaskBtn)
        completeTaskButton = requireActivity().findViewById<ImageButton>(R.id.completeTaskBtn)
        backTaskButton = requireActivity().findViewById<ImageButton>(R.id.backTaskBtn)
        completedTaskButton = requireActivity().findViewById<ImageButton>(R.id.completedTaskBtn)
        createTaskButtonLabel = requireActivity().findViewById<TextView>(R.id.createTaskBtnLabel)
        completeTaskButtonLabel = requireActivity().findViewById<TextView>(R.id.completeTaskBtnLabel)
        completedTaskButtonLabel = requireActivity().findViewById<TextView>(R.id.completedTaskBtnLabel)

        when (fragmentTag){
            ConstantCollection.FRAGMENT_TAG_CREATE_TASK -> {
                taskTitle.text = resources.getString(R.string.task_title_create)
                createTaskButton.visibility = View.VISIBLE
                createTaskButton.setBackgroundResource(R.drawable.spargrisen_submit_text_lightbluee)
                backTaskButton.visibility = View.INVISIBLE
                backTaskButton.setBackgroundResource(R.drawable.spagrisen_back_darkgreen)
                completeTaskButton.visibility = View.INVISIBLE
                completedTaskButton.visibility = View.INVISIBLE

            }
            ConstantCollection.FRAGMENT_TAG_TASK_INFO -> {
                createTaskButton.visibility = View.INVISIBLE
                backTaskButton.visibility = View.VISIBLE
                backTaskButton.setBackgroundResource(R.drawable.spagrisen_back_darkgreen)
                completeTaskButton.visibility = View.INVISIBLE
                completedTaskButton.visibility = View.INVISIBLE

                taskTitle.text = resources.getString(R.string.task_title_info)
                taskNameEt.setText(arguments?.getString(ConstantCollection.TASK_ITEM_NAME))
                taskDescriptionEt.setText(arguments?.getString(ConstantCollection.TASK_ITEM_DESCRIPTION))
                pointEt.setText(arguments?.getString(ConstantCollection.TASK_ITEM_POINT_OF_TASK))

                taskNameEt.isEnabled = false
                taskDescriptionEt.isEnabled = false
                pointEt.isEnabled = false
//                view?.setOnTouchListener { v, event ->
//                    mDetector.onTouchEvent(event).let {
//                        if(event.action == MotionEvent.ACTION_MOVE){
//                            Log.d(LOG_MSG, "MOVED!!")
//                        }
//                    }
//                    v.onTouchEvent(event).let {
//                        if(event.action == MotionEvent.ACTION_MOVE){
//                            Log.d(LOG_MSG, "MOVED!")
//                        }
//                    }
//                     true
//                }
            }
            ConstantCollection.FRAGMENT_TAG_TASK_COMPLETE -> {

                mTaskViewModel?.mTaskStatus?.postValue(arguments?.getString(ConstantCollection.TASK_ITEM_STATUS))

                if(arguments?.getString(ConstantCollection.TASK_ITEM_STATUS) == resources.getString(R.string.taskCompleted)){
                    createTaskButton.visibility = View.INVISIBLE
                    backTaskButton.visibility = View.INVISIBLE
                    backTaskButton.setBackgroundResource(R.drawable.spagrisen_back_darkgreen)
                    completeTaskButton.visibility = View.INVISIBLE
                    completedTaskButton.setBackgroundResource(R.drawable.spargrisen_cherryyellow_completed_icon)
                    completedTaskButton.visibility = View.VISIBLE

                    taskTitle.text = resources.getString(R.string.task_title_completed)

                } else {
                    createTaskButton.visibility = View.INVISIBLE
                    backTaskButton.visibility = View.INVISIBLE
                    backTaskButton.setBackgroundResource(R.drawable.spagrisen_back_darkgreen)
                    completeTaskButton.visibility = View.VISIBLE
                    completeTaskButton.setBackgroundResource(R.drawable.spargrisen_complete_crown_lightbluee)
                    completedTaskButton.visibility = View.INVISIBLE

                    taskTitle.text = resources.getString(R.string.task_title_complete)
                }

                taskNameEt.setText(arguments?.getString(ConstantCollection.TASK_ITEM_NAME))
                taskDescriptionEt.setText(arguments?.getString(ConstantCollection.TASK_ITEM_DESCRIPTION))
                pointEt.setText(arguments?.getString(ConstantCollection.TASK_ITEM_POINT_OF_TASK))


                taskNameEt.isEnabled = false
                taskDescriptionEt.isEnabled = false
                pointEt.isEnabled = false

            }
        }
    }

    private fun softKeyboardHide(){

        val ctFrag: View? = requireActivity().findViewById(R.id.createTaskFragment)
        ctFrag?.setOnTouchListener { v: View, event: MotionEvent ->
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(ctFrag != null){
                imm.hideSoftInputFromWindow(ctFrag!!.windowToken, 0)
            }
            true
        }
        val ctLL: View? = requireActivity().findViewById(R.id.createTaskLinearLayout)
        ctLL?.setOnTouchListener { v: View, event: MotionEvent ->
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(ctLL != null){
                imm.hideSoftInputFromWindow(ctLL!!.windowToken, 0)
            }
            true
        }
        val ttLL: View? = requireActivity().findViewById(R.id.taskTitleLinearLayout)
        ttLL?.setOnTouchListener { v: View, event: MotionEvent ->
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(ctLL != null){
                imm.hideSoftInputFromWindow(ctLL!!.windowToken, 0)
            }
            true
        }
        val ttTV: View? = requireActivity().findViewById(R.id.taskTitle)
        ttTV?.setOnTouchListener { v: View, event: MotionEvent ->
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(ctLL != null){
                imm.hideSoftInputFromWindow(ctLL!!.windowToken, 0)
            }
            true
        }



    }

    override fun onDestroy() {
        super.onDestroy()

        //mCreateTaskFragmentBinding = null
    }

    private fun showToastMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val LOG_MSG = "CREATE_TASK_FRAGMENT"

        val sInstance: Fragment
            get() = CreateTaskFragment()
    }

}
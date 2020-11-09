package com.technology.mycow.spargrisenkt.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Placeholder
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technology.mycow.spargrisenkt.AppExecutors
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.adapter.TaskListAdapter
import com.technology.mycow.spargrisenkt.db.SpargrisenDB
import com.technology.mycow.spargrisenkt.db.entity.Task
import com.technology.mycow.spargrisenkt.helper.SwipeToDeleteCallback
import com.technology.mycow.spargrisenkt.model.ConstantCollection
import com.technology.mycow.spargrisenkt.viewmodel.TaskViewModel
import com.technology.mycow.spargrisenkt.viewmodel.UserViewModel

class TaskListByUserFragment : Fragment(), ActionPromptDialogFragment.ActionPromptDialogListener {

    private var args = Bundle()
    private var mUserViewModel: UserViewModel? = null
    private var mTaskViewModel: TaskViewModel? = null
    private var mSpargrisenDB: SpargrisenDB? = null
    private var btnArrayList = mutableListOf<Button>()
    private val buttonArrayList = mutableListOf<Button>()
    private val drawableArrayList = mutableListOf<Drawable>()

    private var buttonId: String? = "100" //100 is a default as not assigned to user. Assign userID if task is assigned to user
    private val btnId: MutableLiveData<Int>? = null

    var temp: LiveData<List<Task>>? = null
    var newTaskListByUser: LiveData<List<Task>>? = null

    private var imageViewIconTag: String = ""
    private val mUserIdAndNameMap = mutableMapOf<Long, String>()
    private val mUserIdAndIconTagMap = mutableMapOf<Long, String>()
    private var mUserId: Long? = 0
    private var mTaskStatus: String = ""

    private lateinit var taskListByUserPlaceHolderText : TextView
    private lateinit var taskListByUserText1 : TextView
    private lateinit var taskListByUserText2 : TextView
    private lateinit var taskListByUserText3 : TextView
    private lateinit var taskListByUserText4 : TextView
    private lateinit var taskListByUserText5 : TextView

    private lateinit var fragmentTag : String
    private lateinit var placeHolder : Placeholder
    private lateinit var taskListByUserUserImage1 : ImageView
    private lateinit var taskListByUserUserImage2 : ImageView
    private lateinit var taskListByUserUserImage3 : ImageView
    private lateinit var taskListByUserUserImage4 : ImageView
    private lateinit var taskListByUserUserImage5 : ImageView

    private lateinit var taskOngoingSortBtn : TextView
    private lateinit var taskOngoingSortLine : ImageView
    private lateinit var taskCompletedSortBtn : TextView
    private lateinit var taskCompletedSortLine : ImageView


    //private var mTaskToDelete: Task? = null
    //private var mIsTaskDeleteCanceled = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSpargrisenDB = SpargrisenDB.getInstance(requireActivity().applicationContext, AppExecutors())
        if(mUserViewModel == null){
            val userViewModelFactory = UserViewModel.Factory(requireActivity().application)
            mUserViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
        }
        if(mTaskViewModel == null){
            val taskViewModelFactory = TaskViewModel.Factory(requireActivity().application)
            mTaskViewModel = ViewModelProvider(this, taskViewModelFactory).get(TaskViewModel::class.java)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.task_list_by_user_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        initializeView()

        subscribeToViewModel()
        setUserObserver()
        setUserIcons()
        setTaskStatusSortButtons()


        //NEED TO CHANGE TO USERID/USERCREATORID. BUTTON MUST ASSOCIATE WITH USERID
        //Listen button changes, and set task list by user
//        val buttonIdObserver = Observer<Long> { id ->
//            if(id != null){
//                mTaskViewModel?.getTaskByUser(id)?.observe(viewLifecycleOwner, Observer { item ->
//                    mTaskListAdapter.setTask(item)
//                })
//            }
//        }
//        mTaskViewModel?.buttonId?.observe(viewLifecycleOwner, buttonIdObserver)

    }

    private fun initializeView(){

        taskListByUserPlaceHolderText = requireActivity().findViewById<TextView>(R.id.taskListByUserUserIconPlaceHolder_text)
        taskListByUserText1 = requireActivity().findViewById<TextView>(R.id.userImage_1_text)
        taskListByUserText2 = requireActivity().findViewById<TextView>(R.id.userImage_2_text)
        taskListByUserText3 = requireActivity().findViewById<TextView>(R.id.userImage_3_text)
        taskListByUserText4 = requireActivity().findViewById<TextView>(R.id.userImage_4_text)
        taskListByUserText5 = requireActivity().findViewById<TextView>(R.id.userImage_5_text)

        fragmentTag = this.tag.toString() //return this fragment tag, that is "TASK_LIST_BY_USER_FRAGMENT"
        placeHolder = requireActivity().findViewById<Placeholder>(R.id.taskListByUserUserIconPlaceHolder)
        taskListByUserUserImage1 = requireActivity().findViewById<ImageView>(R.id.userImage_1)
        taskListByUserUserImage2 = requireActivity().findViewById<ImageView>(R.id.userImage_2)
        taskListByUserUserImage3 = requireActivity().findViewById<ImageView>(R.id.userImage_3)
        taskListByUserUserImage4 = requireActivity().findViewById<ImageView>(R.id.userImage_4)
        taskListByUserUserImage5 = requireActivity().findViewById<ImageView>(R.id.userImage_5)

        taskOngoingSortBtn = requireActivity().findViewById<TextView>(R.id.taskOngoingSortTv)
        taskOngoingSortLine = requireActivity().findViewById<ImageView>(R.id.taskOngoingSortLine)
        taskCompletedSortBtn = requireActivity().findViewById<TextView>(R.id.taskCompletedSortTv)
        taskCompletedSortLine = requireActivity().findViewById<ImageView>(R.id.taskCompletedSortLine)

    }

    private fun setUserIconTextVisibility(){

        taskListByUserText1.visibility = View.VISIBLE
        taskListByUserText2.visibility = View.VISIBLE
        taskListByUserText3.visibility = View.VISIBLE
        taskListByUserText4.visibility = View.VISIBLE
        taskListByUserText5.visibility = View.VISIBLE

    }

    private fun setUserIconDisable(){
        taskListByUserUserImage1.isClickable = false
        taskListByUserUserImage2.isClickable = false
        taskListByUserUserImage3.isClickable = false
        taskListByUserUserImage4.isClickable = false
        taskListByUserUserImage5.isClickable = false
    }

    private fun subscribeToViewModel(){

        val mTaskListAdapter = TaskListAdapter(requireContext())
        val mTaskListRecyclerView = ((activity) as MainActivity).findViewById<RecyclerView>(R.id.listRecyclerView)
        mTaskListRecyclerView.adapter = mTaskListAdapter
        mTaskListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(requireContext(), mTaskListAdapter))
        itemTouchHelper.attachToRecyclerView(mTaskListRecyclerView)

        //Set all task list
        if(mTaskViewModel != null){

            mUserViewModel?.userIdByUseIcon?.observe(viewLifecycleOwner, Observer { userId ->
                mUserId = userId
                mTaskViewModel?.mTaskStatus?.observe(viewLifecycleOwner, Observer { taskStatus ->
                    var count = requireActivity().supportFragmentManager.backStackEntryCount
                    val ongoing = resources.getString(R.string.taskOngoing)
                    val completed = resources.getString(R.string.taskCompleted)
                    when (taskStatus) {
                        completed -> {
                            mTaskViewModel!!.getTaskByUserSortedByStatus(mUserId!!, taskStatus).observe(viewLifecycleOwner, Observer { newTaskListByUserSortedbyStatus ->
                                taskCompletedSortLine.isSelected = true
                                taskOngoingSortLine.isSelected = false
                                mTaskListAdapter.setTask(newTaskListByUserSortedbyStatus)
                                mTaskListAdapter.setHolderTaskItemTouchedListener(holderTaskItemTouchListener())
                                mTaskListAdapter.setHolderTaskItemClickedListener(holderTaskItemClickListener())
                            })
                        }
                        ongoing -> {
                            mTaskViewModel!!.getTaskByUserSortedByStatus(mUserId!!, taskStatus).observe(viewLifecycleOwner, Observer { newTaskListByUserSortedbyStatus ->
                                taskCompletedSortLine.isSelected = false
                                taskOngoingSortLine.isSelected = true
                                mTaskListAdapter.setTask(newTaskListByUserSortedbyStatus)
                                mTaskListAdapter.setHolderTaskItemTouchedListener(holderTaskItemTouchListener())
                                mTaskListAdapter.setHolderTaskItemClickedListener(holderTaskItemClickListener())
                            })
                        }
                    }
                })
            })

        }


        if(mTaskViewModel?.mIsTaskDeleteCanceled != null){
            mTaskViewModel!!.mIsTaskDeleteCanceled.observe(viewLifecycleOwner, Observer { newValue ->
                mTaskListAdapter.removeTaskCanceled()
            })
        }

    }


    private fun setUserObserver(){

        mUserViewModel?.user?.observe(viewLifecycleOwner, Observer { newUser ->

            //MAKE DISABLE
            setUserIconDisable()

            for(i in newUser.indices){
                mUserIdAndNameMap[newUser[i].userId] = newUser[i].userName
                mUserIdAndIconTagMap[newUser[i].userId] = newUser[i].iconTag

                //SET NAME AND MAKE CLICKABLE
                when (newUser[i].iconTag) {
                    taskListByUserUserImage1.tag.toString() -> {
                        taskListByUserText1.text = newUser[i].userName
                        taskListByUserUserImage1.isClickable = true
                    }
                    taskListByUserUserImage2.tag.toString() -> {
                        taskListByUserText2.text = newUser[i].userName
                        taskListByUserUserImage2.isClickable = true
                    }
                    taskListByUserUserImage3.tag.toString() -> {
                        taskListByUserText3.text = newUser[i].userName
                        taskListByUserUserImage3.isClickable = true
                    }
                    taskListByUserUserImage4.tag.toString() -> {
                        taskListByUserText4.text = newUser[i].userName
                        taskListByUserUserImage4.isClickable = true
                    }
                    taskListByUserUserImage5.tag.toString() -> {
                        taskListByUserText5.text = newUser[i].userName
                        taskListByUserUserImage5.isClickable = true
                    }
                }
            }
        })

        //SET USER ID OF CURRENTLY SELECTED ICON FOR PLACEHOLDER
        mUserViewModel?.imageViewPlaceHolderIconUserNameLiveData?.observe(viewLifecycleOwner,
            Observer { userName ->
                //assignTaskPlaceHolderText.text = iconTag

                var userId : Long = 0
                //GET USER ID BY USERNAME, THEN USE USER ID TO GET IMAGE TAG. THEN SET EMPTY TEXT TO CURRENT SELECTED ICON
                mUserIdAndIconTagMap.let {
                    if(mUserIdAndNameMap.containsValue(userName)){
                        mUserIdAndNameMap.forEach{ item ->
                            if(userName == item.value){
                                userId = item.key
                                return@forEach
                            }
                        }
                    }
                }
            })

        //GET USER NAME OF CURRENTLY SELECTED ICON FOR PLACEHOLDER
        mUserViewModel?.imageViewIconTagLiveData?.observe(viewLifecycleOwner, Observer { currIcon ->
            if(mUserIdAndIconTagMap.containsValue(currIcon)){
                mUserIdAndIconTagMap.forEach { key, value ->
                    Log.d(LOG_MSG, "WHAT IT t" + key)
                    Log.d(LOG_MSG, "WHAT IT u" + value)
                    if(value == currIcon){
                        //mUserId = key.toLong()

                        setUserIconTextVisibility()
                        when (value) {
                            taskListByUserUserImage1.tag.toString() -> taskListByUserText1.visibility = View.INVISIBLE
                            taskListByUserUserImage2.tag.toString() -> taskListByUserText2.visibility = View.INVISIBLE
                            taskListByUserUserImage3.tag.toString() -> taskListByUserText3.visibility = View.INVISIBLE
                            taskListByUserUserImage4.tag.toString() -> taskListByUserText4.visibility = View.INVISIBLE
                            taskListByUserUserImage5.tag.toString() -> taskListByUserText5.visibility = View.INVISIBLE
                        }

                        mUserViewModel?.userIdByUseIcon?.postValue(key.toLong())
                        if(mUserIdAndNameMap.containsKey(key)){
                            mUserIdAndNameMap.forEach{ userKey, userValue ->
                                if(userKey == key){
                                    mUserViewModel!!.imageViewPlaceHolderIconUserNameLiveData.postValue(userValue)
                                    return@forEach
                                }
                            }
                        }
                    }
                }
            } else {
                setUserIconTextVisibility()
            }
        })

    }

    private fun setUserIcons(){

        val fragmentTag = this.tag //return this fragment tag, that is "TASK_LIST_BY_USER_FRAGMENT"
        val placeHolder = requireActivity().findViewById<Placeholder>(R.id.taskListByUserUserIconPlaceHolder)
        val createUserImage1 = requireActivity().findViewById<ImageView>(R.id.userImage_1)
        val createUserImage2 = requireActivity().findViewById<ImageView>(R.id.userImage_2)
        val createUserImage3 = requireActivity().findViewById<ImageView>(R.id.userImage_3)
        val createUserImage4 = requireActivity().findViewById<ImageView>(R.id.userImage_4)
        val createUserImage5 = requireActivity().findViewById<ImageView>(R.id.userImage_5)

//        val icons: TypedArray = resources.obtainTypedArray(R.array.icons)
//        val drawable: Drawable = icons.getDrawable(0)!!

        placeHolder.apply {
            val onClickListener: (View) -> Unit = {view ->
                TransitionManager.beginDelayedTransition(rootView as ViewGroup)
                placeHolder.setContentId(view.id)

                //To observe icon currently selected
                mUserViewModel?.imageViewIconTagLiveData?.postValue(view.tag.toString())

                imageViewIconTag = view.tag.toString() //This is for buttonId

//                var createUserFragment: Fragment = CreateUserFragment.sInstance
//                createUserFragment = SetArgumentsToFragment(createUserFragment, args, imageViewIconTag, ConstantCollection.USER_BUTTON_TAG).setArguments()
//                createUserFragment = SetArgumentsToFragment(createUserFragment, args, fragmentTag!!, ConstantCollection.FRAGMENT_TAG).setArguments()

            }
            createUserImage1.setOnClickListener(onClickListener)
            createUserImage2.setOnClickListener(onClickListener)
            createUserImage3.setOnClickListener(onClickListener)
            createUserImage4.setOnClickListener(onClickListener)
            createUserImage5.setOnClickListener(onClickListener)
        }

    }

    private fun setTaskStatusSortButtons(){

        taskCompletedSortBtn.setOnClickListener { view ->
            Log.d(LOG_MSG, "Completed Pressed")
            mTaskViewModel?.mTaskStatus?.postValue(resources.getString(R.string.taskCompleted))
        }

        taskOngoingSortBtn.setOnClickListener { view ->
            Log.d(LOG_MSG, "Ongoing Pressed")
            mTaskViewModel?.mTaskStatus?.postValue(resources.getString(R.string.taskOngoing))
        }
    }

    companion object {
        private const val LOG_MSG = "Task_List_by_User_Fragment"

        val sInstance : Fragment
            get() = TaskListByUserFragment()
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

        args.putLong(ConstantCollection.TASK_ITEM_ID, taskPositionAt.taskId)
        args.putString(ConstantCollection.TASK_ITEM_NAME, taskPositionAt.taskName)
        args.putString(ConstantCollection.TASK_ITEM_DESCRIPTION, taskPositionAt.taskDescription)
        args.putString(ConstantCollection.TASK_ITEM_POINT_OF_TASK, (taskPositionAt.pointOfTask).toString())
        args.putString(ConstantCollection.TASK_ITEM_STATUS, taskPositionAt.taskStatus)
        createTaskFragment.arguments = args

        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.taskListByUserFragment, createTaskFragment, ConstantCollection.FRAGMENT_TAG_TASK_COMPLETE)
            .addToBackStack(ConstantCollection.FRAGMENT_TAG_TASK_LIST_BY_USER)
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
        dialog.show(requireActivity().supportFragmentManager, "ActionPromptDialogFragmentByTaskListByUser")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        if(mTaskViewModel?.mTaskToDelete != null){
            mTaskViewModel?.deleteTask(mTaskViewModel!!.mTaskToDelete!!.value!!.taskId)
            mTaskViewModel?.mIsTaskDeleteCanceled?.postValue(false)
        }
        Log.d(LOG_MSG, "POSITIVE")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Log.d(LOG_MSG, "NEGATIVE")
        mTaskViewModel?.mIsTaskDeleteCanceled?.postValue(true)
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_MSG, "ON RESUME!")
    }


}
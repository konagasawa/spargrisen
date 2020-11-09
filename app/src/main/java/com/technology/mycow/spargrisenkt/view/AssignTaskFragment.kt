package com.technology.mycow.spargrisenkt.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionManager
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Placeholder
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technology.mycow.spargrisenkt.AppExecutors
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.adapter.AssignTaskAdapter
import com.technology.mycow.spargrisenkt.adapter.AssignTaskAdapter.HolderItemTouchedListener
import com.technology.mycow.spargrisenkt.db.SpargrisenDB
import com.technology.mycow.spargrisenkt.db.entity.Task
import com.technology.mycow.spargrisenkt.db.entity.TaskAssignUpdate
import com.technology.mycow.spargrisenkt.model.ConstantCollection
import com.technology.mycow.spargrisenkt.model.SetArgumentsToFragment
import com.technology.mycow.spargrisenkt.viewmodel.TaskViewModel
import com.technology.mycow.spargrisenkt.viewmodel.UserViewModel
import java.util.*

class AssignTaskFragment : Fragment(), ActionPromptDialogFragment.ActionPromptDialogListener {

    private var args = Bundle()
    private var mUserViewModel: UserViewModel? = null
    private var mTaskViewModel: TaskViewModel? = null
    private var mSpargrisenDB: SpargrisenDB? = null
    private var btnArrayList = mutableListOf<Button>()
    private val buttonArrayList = mutableListOf<Button>()
    private val drawableArrayList = mutableListOf<Drawable>()
    private var fragmentId: Int? = null
    private var menuItemId: Int? = null

    //private var btnId : String? = null
    private var buttonId: String? = null

    private var imageViewIconTag: String = ""
    private val mUserIdAndNameMap = mutableMapOf<Long, String>()
    private val mUserIdAndIconTagMap = mutableMapOf<Long, String>()
    private var mUserId: Long? = null
    private var mUserName: String? = ""

    //private val mTaskPositionAt = MutableLiveData<Task>()

    private lateinit var assignTaskPlaceHolderText : TextView
    private lateinit var assignTaskText1 : TextView
    private lateinit var assignTaskText2 : TextView
    private lateinit var assignTaskText3 : TextView
    private lateinit var assignTaskText4 : TextView
    private lateinit var assignTaskText5 : TextView

    private lateinit var fragmentTag : String
    private lateinit var placeHolder : Placeholder
    private lateinit var assignTaskUserImage1 : ImageView
    private lateinit var assignTaskUserImage2 : ImageView
    private lateinit var assignTaskUserImage3 : ImageView
    private lateinit var assignTaskUserImage4 : ImageView
    private lateinit var assignTaskUserImage5 : ImageView

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
        return inflater.inflate(R.layout.assign_task_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

//        btnArrayList = createUserButton()
//        createTableLayoutWithButton(btnArrayList)

        initializeView()

        subscribeToViewModel()
        setUserIcons()
        setUserObserver()

    }

    private val argsMenuItemId: Int?
        get() {
            return arguments.let { arguments?.getInt(ConstantCollection.MENU_ITEM_ID) }
        }

    private fun initializeView(){

        assignTaskPlaceHolderText = requireActivity().findViewById<TextView>(R.id.assignTaskUserIconPlaceHolder_text)
        assignTaskText1 = requireActivity().findViewById<TextView>(R.id.userImage_1_text)
        assignTaskText2 = requireActivity().findViewById<TextView>(R.id.userImage_2_text)
        assignTaskText3 = requireActivity().findViewById<TextView>(R.id.userImage_3_text)
        assignTaskText4 = requireActivity().findViewById<TextView>(R.id.userImage_4_text)
        assignTaskText5 = requireActivity().findViewById<TextView>(R.id.userImage_5_text)

        fragmentTag = this.tag.toString() //return this fragment tag, that is "ASSIGN_TASK_FRAGMENT"
        placeHolder = requireActivity().findViewById<Placeholder>(R.id.assignTaskUserIconPlaceHolder)
        assignTaskUserImage1 = requireActivity().findViewById<ImageView>(R.id.userImage_1)
        assignTaskUserImage2 = requireActivity().findViewById<ImageView>(R.id.userImage_2)
        assignTaskUserImage3 = requireActivity().findViewById<ImageView>(R.id.userImage_3)
        assignTaskUserImage4 = requireActivity().findViewById<ImageView>(R.id.userImage_4)
        assignTaskUserImage5 = requireActivity().findViewById<ImageView>(R.id.userImage_5)

    }

    private fun setUserIconTextVisibility(){

        assignTaskText1.visibility = View.VISIBLE
        assignTaskText2.visibility = View.VISIBLE
        assignTaskText3.visibility = View.VISIBLE
        assignTaskText4.visibility = View.VISIBLE
        assignTaskText5.visibility = View.VISIBLE

    }

    private fun setUserIconDisable(){
        assignTaskUserImage1.isClickable = false
        assignTaskUserImage2.isClickable = false
        assignTaskUserImage3.isClickable = false
        assignTaskUserImage4.isClickable = false
        assignTaskUserImage5.isClickable = false
    }

    private fun subscribeToViewModel(){
        val mAssignTaskAdapter = AssignTaskAdapter(requireContext())
        val mAssignTaskRecyclerView = ((activity) as MainActivity).findViewById<RecyclerView>(R.id.listRecyclerView)
//        val mAssignTaskRecyclerView = requireActivity().findViewById<RecyclerView>(R.id.listRecyclerView)
        mAssignTaskRecyclerView.adapter = mAssignTaskAdapter
        mAssignTaskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        if(mTaskViewModel != null){
//            mTaskViewModel!!.task.observe(viewLifecycleOwner, Observer { taskItem ->
//                Log.d(LOG_MSG, "SET TASK")
//                mAssignTaskAdapter.setTask(taskItem)
//                mAssignTaskAdapter.setOnHolderItemTouchedListener(holderItemTouchedListener())
//            })

//            mTaskViewModel!!.getObjTaskWithIconId().observe(viewLifecycleOwner, Observer { taskWithIconid ->
//                Log.d(LOG_MSG, "TASK CREATED")
//                //mTaskListAdapter.setTask(taskWithIconid)
//
//            })
            mTaskViewModel!!.taskWithSortedByStatus(resources.getString(R.string.taskUnassigned)).observe(viewLifecycleOwner, Observer { taskByStatus ->
                Log.d(LOG_MSG, "TASK WITH STATUS")
                mAssignTaskAdapter.setTask(taskByStatus)
                mAssignTaskAdapter.setOnHolderItemTouchedListener(holderItemTouchedListener())
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
                    assignTaskUserImage1.tag.toString() -> {
                        assignTaskText1.text = newUser[i].userName
                        assignTaskUserImage1.isClickable = true
                    }
                    assignTaskUserImage2.tag.toString() -> {
                        assignTaskText2.text = newUser[i].userName
                        assignTaskUserImage2.isClickable = true
                    }
                    assignTaskUserImage3.tag.toString() -> {
                        assignTaskText3.text = newUser[i].userName
                        assignTaskUserImage3.isClickable = true
                    }
                    assignTaskUserImage4.tag.toString() -> {
                        assignTaskText4.text = newUser[i].userName
                        assignTaskUserImage4.isClickable = true
                    }
                    assignTaskUserImage5.tag.toString() -> {
                        assignTaskText5.text = newUser[i].userName
                        assignTaskUserImage5.isClickable = true
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
                                mUserViewModel?.userName?.postValue(item.value)
                                userId = item.key
                                return@forEach
                            }
                        }
                    }
                }
            })

        mUserViewModel?.userIdByUseIcon?.observe(viewLifecycleOwner, Observer { userId ->
            mUserId = userId
        })

        mUserViewModel?.userName?.observe(viewLifecycleOwner, Observer { userName ->
            mUserName = userName
        })

        //GET USER NAME OF CURRENTLY SELECTED ICON FOR PLACEHOLDER
        mUserViewModel?.imageViewIconTagLiveData?.observe(viewLifecycleOwner, Observer { currIcon ->
            if(mUserIdAndIconTagMap.containsValue(currIcon)){
                mUserIdAndIconTagMap.forEach { key, value ->
                    Log.d(LOG_MSG, "WHAT IT t" + key)
                    Log.d(LOG_MSG, "WHAT IT u" + value)
                    if(value == currIcon){

                        setUserIconTextVisibility()

                        //MAKE TEXT INVISIBLE WHEN USER ICON IS TOUCHED
                        when (value) {
                            assignTaskUserImage1.tag.toString() -> assignTaskText1.visibility = View.INVISIBLE
                            assignTaskUserImage2.tag.toString() -> assignTaskText2.visibility = View.INVISIBLE
                            assignTaskUserImage3.tag.toString() -> assignTaskText3.visibility = View.INVISIBLE
                            assignTaskUserImage4.tag.toString() -> assignTaskText4.visibility = View.INVISIBLE
                            assignTaskUserImage5.tag.toString() -> assignTaskText5.visibility = View.INVISIBLE
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
            assignTaskUserImage1.setOnClickListener(onClickListener)
            assignTaskUserImage2.setOnClickListener(onClickListener)
            assignTaskUserImage3.setOnClickListener(onClickListener)
            assignTaskUserImage4.setOnClickListener(onClickListener)
            assignTaskUserImage5.setOnClickListener(onClickListener)
        }

    }

//    Create Button programmatically
//    private fun createUserButton() : MutableList<Button> {
//
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_pink, null))
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_blue, null))
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_green, null))
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_red, null))
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_yellow, null))
//
//        val fragmentTag = this.tag!! //return this fragment tag, that is "SelectUserFragment"
//        for (i in drawableArrayList.indices) {
//            val userBtn: Button = Button(requireActivity().applicationContext)
//            userBtn.background = drawableArrayList!![i]
//            userBtn.id = i
//            userBtn.setOnClickListener { view ->
//                var assignTaskFragment: Fragment = AssignTaskFragment.sInstance
//                assignTaskFragment = SetArgumentsToFragment(assignTaskFragment, args, i.toString(), ConstantCollection.USER_BUTTON_TAG).setArguments()
//                assignTaskFragment = SetArgumentsToFragment(assignTaskFragment, args, fragmentTag, ConstantCollection.FRAGMENT_TAG).setArguments()
//                buttonId = assignTaskFragment.arguments?.getString(ConstantCollection.USER_BUTTON_TAG)
//
//            }
//            buttonArrayList!!.add(userBtn)
//        }
//        return buttonArrayList
//    }
//
//    private fun createTableLayoutWithButton(buttonArrayList: MutableList<Button>){
//
//        val tableLayout = requireActivity().findViewById<TableLayout>(R.id.assignTaskToUserTableLayout)
//        val row = TableRow(requireActivity().application?.applicationContext)
//        row.layoutParams = TableRow.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//        val iterator = buttonArrayList.iterator();
//        for((index, value) in iterator.withIndex()!!){
//            tableLayout?.setColumnShrinkable(index, true)
//            row.addView(value as View)
//        }
//        tableLayout?.addView(row, TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.MATCH_PARENT))
//
//    }

    companion object {
        private const val LOG_MSG = "ASSIGN_TASK_LOG"

        val sInstance: Fragment
            get() = AssignTaskFragment()
    }

    private fun showToastMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    fun holderItemTouchedListener(): HolderItemTouchedListener {
        return object : HolderItemTouchedListener {
            override fun onHolderItemTouchedListener(task: Task) {
                mTaskViewModel?.mTaskPositionAt?.postValue(task)
                val mm = mUserId
                if(mUserId != null && mUserId!! > 0){
                    showActionPromptDialog()
                } else {
                    showToastMessage("Select Child First")
                }
            }
        }
    }

    fun showActionPromptDialog() {
        // Create an instance of the dialog fragment and show it
        var mArgs = Bundle()
        mArgs.putInt(ConstantCollection.DIALOG_TITLE_ID, R.string.action_prompt_assign_task_title)
        mArgs.putInt(ConstantCollection.DIALOG_MESSAGE_ID, R.string.action_prompt_assign_task_message)
        val dialog = ActionPromptDialogFragment()
        dialog.arguments = mArgs
        dialog.setTargetFragment(this, 100)
        dialog.show(requireActivity().supportFragmentManager, "ActionPromptDialogFragmentByAssignTask")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {

        val taskAssignUpdate : TaskAssignUpdate = TaskAssignUpdate()
        val taskId = mTaskViewModel?.mTaskPositionAt?.value!!.taskId
        val date = Date().time
        taskAssignUpdate.taskId = taskId
//        taskAssignUpdate.userCreatorId = buttonId!!.toLong()
        taskAssignUpdate.userCreatorId = mUserId!!
        taskAssignUpdate.taskStatus = resources.getString(R.string.taskOngoing)
        taskAssignUpdate.assignedDate = date
        mTaskViewModel?.updateTaskToAssign(taskAssignUpdate)
        showToastMessage("\"${mTaskViewModel?.mTaskPositionAt?.value!!.taskName}\" Is Assigned to \"${mUserName}\"")
        Log.d(LOG_MSG, "POSITIVE")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        //showToastMessage("Assign Task Is Canceled")
        Log.d(LOG_MSG, "NEGATIVE")
    }


}
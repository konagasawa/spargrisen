package com.technology.mycow.spargrisenkt.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Placeholder
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.technology.mycow.spargrisenkt.AppExecutors
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.db.SpargrisenDB
//import com.technology.mycow.spargrisenkt.db.entity.TaskList
import com.technology.mycow.spargrisenkt.db.entity.User
import com.technology.mycow.spargrisenkt.helper.TouchGestureListener
import com.technology.mycow.spargrisenkt.model.ConstantCollection
import com.technology.mycow.spargrisenkt.viewmodel.UserViewModel
import java.text.NumberFormat
import java.util.*

class CreateUserFragment : Fragment() {

//    var mCreateUserFragmentBinding : CreateUserFragmentBinding? = null
    private var args = Bundle()
//    private var mTaskViewModel: TaskViewModel? = null
    private var mUserViewModel: UserViewModel? = null
    private var mSpargrisenDB: SpargrisenDB? = null

    private var imageViewIconTag: String = ""
    private val mUserIdAndNameMap = mutableMapOf<Long, String>()
    private val mUserIdAndIconTagMap = mutableMapOf<Long, String>()

    private var countUser: Int = 0

    private lateinit var createUserPlaceHolderText : TextView
    private lateinit var createUserText1 : TextView
    private lateinit var createUserText2 : TextView
    private lateinit var createUserText3 : TextView
    private lateinit var createUserText4 : TextView
    private lateinit var createUserText5 : TextView

    private lateinit var fragmentTag : String
    private lateinit var placeHolder : Placeholder
    private lateinit var createUserImage1 : ImageView
    private lateinit var createUserImage2 : ImageView
    private lateinit var createUserImage3 : ImageView
    private lateinit var createUserImage4 : ImageView
    private lateinit var createUserImage5 : ImageView

    private lateinit var userNameEt : EditText
    private lateinit var rateOfPointEt : EditText
    private lateinit var pointUnitTv : TextView
    private lateinit var createUserBtn : ImageButton

    //SHOW/HIDE CHECKED ANIMATION ICON
    private lateinit var userIconCreatedCheckImage : ImageView
    private lateinit var userIconCreatedCheckAnimation : AnimationDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        if(mUserViewModel == null){
            val factory = UserViewModel.Factory(requireActivity().application)
            mUserViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        }
//        if(mTaskViewModel == null){
//            mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
//        }
        mSpargrisenDB = SpargrisenDB.getInstance(requireActivity().applicationContext, AppExecutors())

        //subscribeToViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        mCreateUserFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.create_user_fragment, container, false)
//        return mCreateUserFragmentBinding!!.root
        return inflater.inflate(R.layout.create_user_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        val createTaskLayout = requireActivity().findViewById<NestedScrollView>(R.id.createUserNestedScrollView)
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

        initializeView()
        setCreateUserButton()

        //SHOW/HIDE CHECKED ANIMATION ICON
        setCheckAnimation()

        setUserIcons()
        softKeyboardHide()
        setUserObserver()

    }

    //This may not need. Can take as "val fragmentTag = this.tag"
    private val argsFragmentTag : String?
        get() {
            return arguments.let { this.arguments?.getString(ConstantCollection.FRAGMENT_TAG) }
        }

    private fun initializeView(){

        createUserPlaceHolderText = requireActivity().findViewById<TextView>(R.id.createUserIconPlaceHolder_text)
        createUserText1 = requireActivity().findViewById<TextView>(R.id.userImage_1_text)
        createUserText2 = requireActivity().findViewById<TextView>(R.id.userImage_2_text)
        createUserText3 = requireActivity().findViewById<TextView>(R.id.userImage_3_text)
        createUserText4 = requireActivity().findViewById<TextView>(R.id.userImage_4_text)
        createUserText5 = requireActivity().findViewById<TextView>(R.id.userImage_5_text)

        fragmentTag = this.tag.toString() //return this fragment tag, that is "CreateUserFragment"
        placeHolder = requireActivity().findViewById<Placeholder>(R.id.createUserIconPlaceHolder)
        createUserImage1 = requireActivity().findViewById<ImageView>(R.id.userImage_1)
        createUserImage2 = requireActivity().findViewById<ImageView>(R.id.userImage_2)
        createUserImage3 = requireActivity().findViewById<ImageView>(R.id.userImage_3)
        createUserImage4 = requireActivity().findViewById<ImageView>(R.id.userImage_4)
        createUserImage5 = requireActivity().findViewById<ImageView>(R.id.userImage_5)

        userNameEt = requireActivity()?.findViewById<EditText>(R.id.userNameEt)
        rateOfPointEt = requireActivity()?.findViewById<EditText>(R.id.rateOfPointEt)
        pointUnitTv = requireActivity()?.findViewById<TextView>(R.id.pointUnitTv)
        pointUnitTv.text = getCurrencyUnit(0)

        createUserBtn = requireActivity()?.findViewById<ImageButton>(R.id.createUserBtn)
        createUserBtn.setBackgroundResource(R.drawable.spargrisen_submit_text_lightbluee)

        //SHOW/HIDE CHECKED ANIMATION ICON
        userIconCreatedCheckImage = requireActivity().findViewById<ImageView>(R.id.userIconCreatedCheckImage)
        userIconCreatedCheckImage.visibility = View.INVISIBLE

    }

    private fun cleanTextOnUserIcon(){

        createUserPlaceHolderText.text = ""
        createUserText1.text = ""
        createUserText2.text = ""
        createUserText3.text = ""
        createUserText4.text = ""
        createUserText5.text = ""

    }

    //SHOW/HIDE CHECKED ANIMATION ICON
    private fun setCheckAnimation(){
        userIconCreatedCheckImage.apply {
            setBackgroundResource(R.drawable.pink_check_animation)
            userIconCreatedCheckAnimation = background as AnimationDrawable
        }
    }

    private fun setUserObserver(){

        mUserViewModel?.user?.observe(viewLifecycleOwner, Observer { newUser ->

            mUserViewModel!!.countActiveUsers.postValue(newUser.size)

            for (i in newUser.indices) {
                mUserIdAndNameMap[newUser[i].userId] = newUser[i].userName
                mUserIdAndIconTagMap[newUser[i].userId] = newUser[i].iconTag

                //SET NAME AND DISABLE CLICKLISTENER ON ICON If USER HAS ALREADY CREATED
                when (newUser[i].iconTag) {
                    createUserImage1.tag.toString() -> {
                        createUserText1.text = newUser[i].userName
                        createUserImage1.setOnClickListener(null)
                        createUserImage1.setImageResource(R.drawable.spargrisen_pink_alpha)
                    }
                    createUserImage2.tag.toString() -> {
                        createUserText2.text = newUser[i].userName
                        createUserImage2.setOnClickListener(null)
                        createUserImage2.setImageResource(R.drawable.spargrisen_blue_alpha)
                    }
                    createUserImage3.tag.toString() -> {
                        createUserText3.text = newUser[i].userName
                        createUserImage3.setOnClickListener(null)
                        createUserImage3.setImageResource(R.drawable.spargrisen_green_alpha)
                    }
                    createUserImage4.tag.toString() -> {
                        createUserText4.text = newUser[i].userName
                        createUserImage4.setOnClickListener(null)
                        createUserImage4.setImageResource(R.drawable.spargrisen_red_alpha)
                    }
                    createUserImage5.tag.toString() -> {
                        createUserText5.text = newUser[i].userName
                        createUserImage5.setOnClickListener(null)
                        createUserImage5.setImageResource(R.drawable.spargrisen_yellow_alpha)
                    }
                }
            }
        })

        //SET USER NAME OF CURRENTLY SELECTED ICON FOR PLACEHOLDER
        mUserViewModel?.imageViewPlaceHolderIconUserNameLiveData?.observe(viewLifecycleOwner,
            Observer { userName ->

                //createUserPlaceHolderText.text = userName

                var userId : Long = 0
                //GET USER ID BY USERNAME, THEN USE UER ID TO GET IMAGE TAG. THEN SET EMPTY TEXT TO CURRENT SELECTED ICON
                mUserIdAndIconTagMap.let {
                    if(mUserIdAndNameMap.containsValue(userName)){
                        mUserIdAndNameMap.forEach{ item ->
                            if(userName == item.value){
                                userId = item.key
                                return@forEach
                            }
                        }
                        if(userId > 0){
                            mUserIdAndIconTagMap.forEach{ item ->
                                when (item.value) {
                                    createUserImage1.tag.toString() -> createUserText1.text = ""
                                    createUserImage2.tag.toString() -> createUserText2.text = ""
                                    createUserImage3.tag.toString() -> createUserText3.text = ""
                                    createUserImage4.tag.toString() -> createUserText4.text = ""
                                    createUserImage5.tag.toString() -> createUserText5.text = ""
                                }
                            }
                        }
                    }
                }
            })

        //IF SIZE IS 5, THEN EDITTEXT AND BUTTON DISABLE
        mUserViewModel?.countActiveUsers?.observe(viewLifecycleOwner, Observer { activeUserCount ->
            if(activeUserCount >= 5){
                userNameEt.isEnabled = false
                userNameEt.isClickable = false
                rateOfPointEt.isEnabled = false
                rateOfPointEt.isClickable = false
                createUserBtn.isEnabled = false
                createUserBtn.isClickable = false
            }
        })

        //GET USER NAME OF CURRENTLY SELECTED ICON FOR PLACEHOLDER
        mUserViewModel?.imageViewIconTagLiveData?.observe(viewLifecycleOwner, Observer { currIcon ->
            if(mUserIdAndIconTagMap.containsValue(currIcon)){
                mUserIdAndIconTagMap.forEach { key, value ->
                    Log.d(LOG_MSG, "WHAT IT t" + key)
                    Log.d(LOG_MSG, "WHAT IT u" + value)
                    if(value == currIcon){
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
            }
        })

    }

    private fun setUserIcons(){

//        val icons: TypedArray = resources.obtainTypedArray(R.array.icons)
//        val drawable: Drawable = icons.getDrawable(0)!!

        cleanTextOnUserIcon()

        placeHolder.apply {
            val onClickListener: (View) -> Unit = {view ->
                TransitionManager.beginDelayedTransition(rootView as ViewGroup)
                placeHolder.setContentId(view.id)

                //To observe icon currently selected
                mUserViewModel?.imageViewIconTagLiveData?.postValue(view.tag.toString())

                imageViewIconTag = view.tag.toString()
//                var createUserFragment: Fragment = CreateUserFragment.sInstance
//                createUserFragment = SetArgumentsToFragment(createUserFragment, args, imageViewIconTag, ConstantCollection.USER_BUTTON_TAG).setArguments()
//                createUserFragment = SetArgumentsToFragment(createUserFragment, args, fragmentTag!!, ConstantCollection.FRAGMENT_TAG).setArguments()


                //SHOW/HIDE CHECKED ANIMATION ICON
                userIconCreatedCheckImage.visibility = View.INVISIBLE
            }


            createUserImage1.setOnClickListener(onClickListener)
            createUserImage2.setOnClickListener(onClickListener)
            createUserImage3.setOnClickListener(onClickListener)
            createUserImage4.setOnClickListener(onClickListener)
            createUserImage5.setOnClickListener(onClickListener)

        }

    }

    private fun setCreateUserButton(){

        //CHECK ICON IS SELECTED
        createUserBtn.setOnClickListener {
            if(!imageViewIconTag.isNullOrEmpty()){
                //CHECK ICON IS ALREADY CREATED
                if(!mUserIdAndIconTagMap.containsValue(imageViewIconTag)){

                    //New user will be created
                    if(!userNameEt.text.toString().isBlank()
                            && !rateOfPointEt.text.toString().isBlank()){
                        val userName = userNameEt.text.toString()
                        val rateOfPointStr = rateOfPointEt.text.toString()
                        val rateOfPointInt = rateOfPointStr.toInt()

                        if(mSpargrisenDB != null){
                            val date = Date().time
                            val user = User(
                                    0,
                                    userName,
                                    imageViewIconTag,
                                    rateOfPointInt,
                                    resources.getString(R.string.userTaskOngoing))
                            mUserViewModel!!.insertUser(user)
                            clearInputEntries()
                            showToastMessage("Child \"${userName}\" Is Created")

                            //SHOW/HIDE CHECKED ANIMATION ICON
                            userIconCreatedCheckImage.visibility = View.VISIBLE
                            userIconCreatedCheckAnimation.setVisible(true, true)
                            userIconCreatedCheckAnimation.start()

                        }

                    } else {
                        showToastMessage("All Input Is Required!!")
                    }
                } else {
                    showToastMessage("Select Another Icon")
                }
            } else {
                showToastMessage("Select Icon Is Required!!")

            }
        }

    }

    private fun clearInputEntries(){
        userNameEt.text.clear()
        userNameEt.text.clear()
        rateOfPointEt.text.clear()
    }

    private fun getCurrencyUnit(price: Int): String {

        val currencyFormatted = NumberFormat.getCurrencyInstance()

        return currencyFormatted.currency.currencyCode
    }

    private fun softKeyboardHide(){

        val ctFrag: View? = requireActivity().findViewById(R.id.createUserFragment)
        ctFrag?.setOnTouchListener { v: View, event: MotionEvent ->
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(ctFrag != null){
                imm.hideSoftInputFromWindow(ctFrag!!.windowToken, 0)
            }
            true
        }
        val ctUserIconLL: View? = requireActivity().findViewById(R.id.createUserIconLinearLayout)
        ctUserIconLL?.setOnTouchListener { v: View, event: MotionEvent ->
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(ctUserIconLL != null){
                imm.hideSoftInputFromWindow(ctUserIconLL!!.windowToken, 0)
            }
            true
        }
        val ctUserInputLL: View? = requireActivity().findViewById(R.id.createUserInputLinearLayout)
        ctUserInputLL?.setOnTouchListener { v: View, event: MotionEvent ->
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(ctUserInputLL != null){
                imm.hideSoftInputFromWindow(ctUserInputLL!!.windowToken, 0)
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
//        mCreateUserFragmentBinding = null
    }

    private fun showToastMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val LOG_MSG = "CREATE_USER_FRAGMENT_LOG"

        val sInstance: Fragment
            get() = CreateUserFragment()
    }


}
package com.technology.mycow.spargrisenkt.view

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.icu.util.Currency
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Placeholder
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.technology.mycow.spargrisenkt.AppExecutors
import com.technology.mycow.spargrisenkt.R
//import com.technology.mycow.spargrisenkt.databinding.CalculatePointFragmentBinding
import com.technology.mycow.spargrisenkt.db.SpargrisenDB
import com.technology.mycow.spargrisenkt.db.entity.Task
import com.technology.mycow.spargrisenkt.db.entity.TaskWithPointAndStatusAndUser
import com.technology.mycow.spargrisenkt.viewmodel.TaskViewModel
import com.technology.mycow.spargrisenkt.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.create_user_fragment.*
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*


class CalculatePointFragment : Fragment() {

    private var args = Bundle()
    private var mUserViewModel: UserViewModel? = null
    private var mTaskViewModel: TaskViewModel? = null
    private var mSpargrisenDB: SpargrisenDB? = null
    private var btnArrayList = mutableListOf<Button>()
    private val buttonArrayList = mutableListOf<Button>()
    private val drawableArrayList = mutableListOf<Drawable>()

    //private var buttonId: String? = "100" //100 is a default as not assigned to user. Assign userID if task is assigned to user

    private var tasks : LiveData<List<Task>>? = null

    //private var mUser = mutableListOf<User>()

    //COROUTINE INITIALIZE
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)

    //private lateinit var mBinding: CalculatePointFragmentBinding
    private var monthButtonV : Int = 100
    private var monthConstant : Int = 100

    private var mUserRateOfPoint: Int = 0
    private var mPointOfTask: Int = 0
    private var mPointOfTaskLiveData = MutableLiveData<Int>()

    private var imageViewIconTag: String = ""
    private val mUserIdAndNameMap = mutableMapOf<Long, String>()
    private val mUserIdAndIconTagMap = mutableMapOf<Long, String>()
    private var mUserId: Long? = 0


    private lateinit var calculatePointPlaceHolderText : TextView
    private lateinit var calculatePointText1 : TextView
    private lateinit var calculatePointText2 : TextView
    private lateinit var calculatePointText3 : TextView
    private lateinit var calculatePointText4 : TextView
    private lateinit var calculatePointText5 : TextView

    private lateinit var fragmentTag : String
    private lateinit var placeHolder : Placeholder
    private lateinit var calculatePointUserImage1 : ImageView
    private lateinit var calculatePointUserImage2 : ImageView
    private lateinit var calculatePointUserImage3 : ImageView
    private lateinit var calculatePointUserImage4 : ImageView
    private lateinit var calculatePointUserImage5 : ImageView

    private lateinit var pointTotalTv : TextView
    private lateinit var circleImage : ImageView
    private lateinit var pointGrisImage: ImageView

    private lateinit var circleAnimation: AnimationDrawable
    private val totalPriceHandler = Handler()
    private var totalPrice : Int = 0

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
        return inflater.inflate(R.layout.calculate_point_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        initializeView()
        initializedMonthButton()
        setUserObserver()
        setUserIcons()
        setAnimateCircle()

        mUserViewModel?.userIdByUseIcon?.observe(viewLifecycleOwner, Observer { newUserId ->
            mUserId = newUserId
            mUserViewModel?.userByUserId?.observe(viewLifecycleOwner, Observer { newUser ->
                if(newUser != null){
                    mUserRateOfPoint = newUser.rateOfPoint
                }
            })
        })

//        mUserViewModel?.userIdByUseIcon?.observe(viewLifecycleOwner, Observer { newUserId ->
//            mUserId = newUserId
//        })

        //SET BY setMonthButtonState
        mTaskViewModel?.monthButtonValue?.observe(viewLifecycleOwner, Observer { monthValue ->
            Log.d(LOG_MSG, "MONTH BUTTON CLICKED" + monthValue)
            monthButtonV = monthValue
        })

        //SET BY setMonthConstant
        mTaskViewModel?.monthConstant?.observe(viewLifecycleOwner, Observer { monthConstantValue ->
            monthConstant = monthConstantValue
        })

        //SET DISPLAY BY TOTAL
        mTaskViewModel?.totalPriceLiveData?.observe(viewLifecycleOwner, Observer { totalPrice ->
            if(totalPrice > 0){
                circleImage.visibility = View.VISIBLE
                circleAnimation.setVisible(true, true)
                circleAnimation.start()

                viewLifecycleOwner.lifecycle.coroutineScope.launch {
                    val runnableTask = Runnable {
                        totalPriceHandler.removeCallbacksAndMessages(null)

                        //SET ANIMATION IS FINISHED
                        mTaskViewModel?.totalPriceToShowDelay?.postValue(totalPrice)
                        }
                    totalPriceHandler.postDelayed(runnableTask, 1000)
                }
            } else {
                totalPriceHandler.removeCallbacksAndMessages(null)
                circleImage.visibility = View.INVISIBLE
                circleAnimation.setVisible(false, false)
                pointTotalTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorCalculateTotalPriceDefaultText))
                pointTotalTv.text = convertPriceToCurrency(totalPrice)
            }
        })

        //SET FLAG TO SHOW TOTAL PRICE AFTER ANIMATION
        mTaskViewModel?.totalPriceToShowDelay?.observe(viewLifecycleOwner, Observer { totalPrice ->
            pointTotalTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorCalculateTotalPriceText))
            pointTotalTv.text = convertPriceToCurrency(totalPrice)
        })

        //CALCULATE TOTAL PRICE
        mPointOfTaskLiveData.observe(viewLifecycleOwner, Observer { totalPoint ->
            if(mUserRateOfPoint != null && totalPoint > 0) {
                if (mUserRateOfPoint != 0 && totalPoint > 0){

                    totalPrice = totalPoint * mUserRateOfPoint
                    mTaskViewModel?.totalPriceLiveData?.postValue(totalPrice)
                } else {
                    mTaskViewModel?.totalPriceLiveData?.postValue(0)
                }
            } else {
                mTaskViewModel?.totalPriceLiveData?.postValue(0)
            }
        })
    }

    private fun initializeView(){

        calculatePointPlaceHolderText = requireActivity().findViewById<TextView>(R.id.calculatePointUserIconPlaceHolder_text)
        calculatePointText1 = requireActivity().findViewById<TextView>(R.id.userImage_1_text)
        calculatePointText2 = requireActivity().findViewById<TextView>(R.id.userImage_2_text)
        calculatePointText3 = requireActivity().findViewById<TextView>(R.id.userImage_3_text)
        calculatePointText4 = requireActivity().findViewById<TextView>(R.id.userImage_4_text)
        calculatePointText5 = requireActivity().findViewById<TextView>(R.id.userImage_5_text)

        fragmentTag = this.tag.toString() //return this fragment tag, that is "CALCULATE_POINT_FRAGMENT"
        placeHolder = requireActivity().findViewById<Placeholder>(R.id.calculatePointUserIconPlaceHolder)
        calculatePointUserImage1 = requireActivity().findViewById<ImageView>(R.id.userImage_1)
        calculatePointUserImage2 = requireActivity().findViewById<ImageView>(R.id.userImage_2)
        calculatePointUserImage3 = requireActivity().findViewById<ImageView>(R.id.userImage_3)
        calculatePointUserImage4 = requireActivity().findViewById<ImageView>(R.id.userImage_4)
        calculatePointUserImage5 = requireActivity().findViewById<ImageView>(R.id.userImage_5)

        pointTotalTv = requireActivity().findViewById<TextView>(R.id.pointTotalTv)
        pointTotalTv.text = convertPriceToCurrency(0)
        circleImage = requireActivity().findViewById<ImageView>(R.id.pointCirlceImage)
        circleImage.visibility = View.INVISIBLE
        pointGrisImage = requireActivity().findViewById<ImageView>(R.id.pointGrisImage)
        pointGrisImage.visibility = View.INVISIBLE
    }

    private fun setUserIconTextVisibility(){

        calculatePointText1.visibility = View.VISIBLE
        calculatePointText2.visibility = View.VISIBLE
        calculatePointText3.visibility = View.VISIBLE
        calculatePointText4.visibility = View.VISIBLE
        calculatePointText5.visibility = View.VISIBLE

    }

    private fun setUserIconDisable(){
        calculatePointUserImage1.isClickable = false
        calculatePointUserImage2.isClickable = false
        calculatePointUserImage3.isClickable = false
        calculatePointUserImage4.isClickable = false
        calculatePointUserImage5.isClickable = false
    }

    private fun setAnimateCircle(){
            circleImage.apply {
            setBackgroundResource(R.drawable.point_circle_animation)
            circleAnimation = background as AnimationDrawable
        }
    }

    private fun calculatePoint(userId: Long, startDate: Long, endDate: Long){

        mPointOfTask = 0
        var point: List<TaskWithPointAndStatusAndUser>

        coroutineScope.launch(Dispatchers.IO) {
            //point = mTaskViewModel!!.getPointOfTask(2, 1603180800000, 1603663200000)
            point = mTaskViewModel!!.getPointOfTask(userId, startDate, endDate)
            point.map { item ->
                mPointOfTask += item.pointOfTask
            }
            mPointOfTaskLiveData.postValue(mPointOfTask)
        }
    }

    private fun convertPriceToCurrency(price: Int): String {

        val currencyFormatted = NumberFormat.getCurrencyInstance()
        val currencyCode = currencyFormatted.currency.currencyCode
        currencyFormatted.maximumFractionDigits = 0

        return currencyFormatted.format(price)
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
                    calculatePointUserImage1.tag.toString() -> {
                        calculatePointText1.text = newUser[i].userName
                        calculatePointUserImage1.isClickable = true
                    }
                    calculatePointUserImage2.tag.toString() -> {
                        calculatePointText2.text = newUser[i].userName
                        calculatePointUserImage2.isClickable = true
                    }
                    calculatePointUserImage3.tag.toString() -> {
                        calculatePointText3.text = newUser[i].userName
                        calculatePointUserImage3.isClickable = true
                    }
                    calculatePointUserImage4.tag.toString() -> {
                        calculatePointText4.text = newUser[i].userName
                        calculatePointUserImage4.isClickable = true
                    }
                    calculatePointUserImage5.tag.toString() -> {
                        calculatePointText5.text = newUser[i].userName
                        calculatePointUserImage5.isClickable = true
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

                        mTaskViewModel?.totalPriceLiveData?.postValue(0)
                        pointTotalTv.text = convertPriceToCurrency(0)

                        setUserIconTextVisibility()
                        when (value) {
                            calculatePointUserImage1.tag.toString() -> calculatePointText1.visibility = View.INVISIBLE
                            calculatePointUserImage2.tag.toString() -> calculatePointText2.visibility = View.INVISIBLE
                            calculatePointUserImage3.tag.toString() -> calculatePointText3.visibility = View.INVISIBLE
                            calculatePointUserImage4.tag.toString() -> calculatePointText4.visibility = View.INVISIBLE
                            calculatePointUserImage5.tag.toString() -> calculatePointText5.visibility = View.INVISIBLE
                        }

                        if(mUserIdAndNameMap.containsKey(key)){
                            mUserIdAndNameMap.forEach{ userKey, userValue ->
                                if(userKey == key){
                                    mUserViewModel?.setUserByUserId(key.toLong())
                                    mUserViewModel?.userIdByUseIcon?.postValue(key.toLong())

                                    mUserViewModel!!.imageViewPlaceHolderIconUserNameLiveData.postValue(userValue)
                                    return@forEach
                                }
                            }
                        }
                    }
                }
            } else {
                setUserIconTextVisibility()
                mUserViewModel?.setUserByUserId(0) //No Entry Found
                mUserViewModel?.userIdByUseIcon?.postValue(0) //No Entry Found
                mUserRateOfPoint = 0 //Initiazlize previously calculated

                mTaskViewModel?.totalPriceLiveData?.postValue(0)
                pointTotalTv.text = convertPriceToCurrency(0)
            }
        })


    }

    private fun setUserIcons(){

        placeHolder.apply {
            val onClickListener: (View) -> Unit = {view ->
                TransitionManager.beginDelayedTransition(rootView as ViewGroup)
                placeHolder.setContentId(view.id)

                //To observe icon currently selected
                mUserViewModel?.imageViewIconTagLiveData?.postValue(view.tag.toString())

                imageViewIconTag = view.tag.toString() //This is for buttonId

            }
            calculatePointUserImage1.setOnClickListener(onClickListener)
            calculatePointUserImage2.setOnClickListener(onClickListener)
            calculatePointUserImage3.setOnClickListener(onClickListener)
            calculatePointUserImage4.setOnClickListener(onClickListener)
            calculatePointUserImage5.setOnClickListener(onClickListener)
        }

    }

    private fun createTargetDateForMonth(targetMonthV: Int, targetMonthConstant: Int): MutableList<Long> {
        val targetMonthList = mutableListOf<Long>()

        val date = Date()
        var calendar = Calendar.getInstance()

        var currentYear = calendar.get(1) //This year
        var currentMonth = calendar.get(2) + 1 //This month, index 0 is January, so need +1 to get current
        var targetYear = currentYear

        //Change year to past
        if(currentMonth - targetMonthV < 0){
            targetYear = currentYear - 1
        }

        //Log.d(LOG_MSG, "BUTTON ID: " + buttonId)

        val calFirstTimeStampOfTargetMonth = Calendar.getInstance()
        val calLastTimeStampOfTargetMonth = Calendar.getInstance()
        //FIRST TIMESTAMP OF CURRENT MONTH
        calFirstTimeStampOfTargetMonth.set(Calendar.YEAR, targetYear);
        calFirstTimeStampOfTargetMonth.set(Calendar.MONTH, targetMonthConstant);
        calFirstTimeStampOfTargetMonth.set(
            targetYear,
            targetMonthConstant,
            calFirstTimeStampOfTargetMonth.getActualMinimum(Calendar.DAY_OF_MONTH),
            calFirstTimeStampOfTargetMonth.getActualMinimum(Calendar.HOUR_OF_DAY),
            calFirstTimeStampOfTargetMonth.getActualMinimum(Calendar.MINUTE),
            calFirstTimeStampOfTargetMonth.getActualMinimum(Calendar.SECOND));
        calFirstTimeStampOfTargetMonth.set(Calendar.MILLISECOND,calFirstTimeStampOfTargetMonth.getActualMinimum(Calendar.MILLISECOND));
        //Log.d(LOG_MSG, "CALNDAR: " + calFirstTimeStampOfTargetMonth.time)

        //LAST TIMSTAMP OF CURRENT MONTH
        calLastTimeStampOfTargetMonth.set(Calendar.YEAR, targetYear);
        calLastTimeStampOfTargetMonth.set(Calendar.MONTH, targetMonthConstant);
        calLastTimeStampOfTargetMonth.set(
            targetYear,
            targetMonthConstant,
            calLastTimeStampOfTargetMonth.getActualMaximum(Calendar.DAY_OF_MONTH),
            calLastTimeStampOfTargetMonth.getActualMaximum(Calendar.HOUR_OF_DAY),
            calLastTimeStampOfTargetMonth.getActualMaximum(Calendar.MINUTE),
            calLastTimeStampOfTargetMonth.getActualMaximum(Calendar.SECOND));
        calLastTimeStampOfTargetMonth.set(Calendar.MILLISECOND,calLastTimeStampOfTargetMonth.getActualMaximum(Calendar.MILLISECOND));
        //Log.d(LOG_MSG, "CALNDAR: " + calLastTimeStampOfTargetMonth.time)

        targetMonthList.add(calFirstTimeStampOfTargetMonth.timeInMillis)
        targetMonthList.add(calLastTimeStampOfTargetMonth.timeInMillis)
        return targetMonthList
    }

    fun initializedMonthButton(){
        var btnList = mutableListOf<Button>()
        btnList.add(requireActivity().findViewById<Button>(R.id.btnJanuary))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnFebruary))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnMarch))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnApril))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnMay))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnJune))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnJuly))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnAugust))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnSeptember))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnOctober))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnNovember))
        btnList.add(requireActivity().findViewById<Button>(R.id.btnDecember))

        var aa = requireActivity().findViewById<Button>(R.id.btnDecember)


        var targetTimeRange = mutableListOf<Long>()
        for(i in btnList){
            i.setOnClickListener { view ->
                when (view.id){
                    R.id.btnJanuary -> {
                        mTaskViewModel?.setMonthButtonState(1)
                        mTaskViewModel?.setMonthConstant(Calendar.JANUARY)
                        targetTimeRange = createTargetDateForMonth(1, Calendar.JANUARY)
                    }
                    R.id.btnFebruary -> {
                        mTaskViewModel?.setMonthButtonState(2)
                        mTaskViewModel?.setMonthConstant(Calendar.FEBRUARY)
                        targetTimeRange = createTargetDateForMonth(2, Calendar.FEBRUARY)
                    }
                    R.id.btnMarch -> {
                        mTaskViewModel?.setMonthButtonState(3)
                        mTaskViewModel?.setMonthConstant(Calendar.MARCH)
                        targetTimeRange = createTargetDateForMonth(3, Calendar.MARCH)
                    }
                    R.id.btnApril -> {
                        mTaskViewModel?.setMonthButtonState(4)
                        mTaskViewModel?.setMonthConstant(Calendar.APRIL)
                        targetTimeRange = createTargetDateForMonth(4, Calendar.APRIL)
                    }
                    R.id.btnMay -> {
                        mTaskViewModel?.setMonthButtonState(5)
                        mTaskViewModel?.setMonthConstant(Calendar.MAY)
                        targetTimeRange = createTargetDateForMonth(5, Calendar.MAY)
                    }
                    R.id.btnJune -> {
                        mTaskViewModel?.setMonthButtonState(6)
                        mTaskViewModel?.setMonthConstant(Calendar.JUNE)
                        targetTimeRange = createTargetDateForMonth(6, Calendar.JUNE)
                    }
                    R.id.btnJuly -> {
                        mTaskViewModel?.setMonthButtonState(7)
                        mTaskViewModel?.setMonthConstant(Calendar.JULY)
                        targetTimeRange = createTargetDateForMonth(7, Calendar.JULY)
                    }
                    R.id.btnAugust -> {
                        mTaskViewModel?.setMonthButtonState(8)
                        mTaskViewModel?.setMonthConstant(Calendar.AUGUST)
                        targetTimeRange = createTargetDateForMonth(8, Calendar.AUGUST)
                    }
                    R.id.btnSeptember -> {
                        mTaskViewModel?.setMonthButtonState(9)
                        mTaskViewModel?.setMonthConstant(Calendar.SEPTEMBER)
                        targetTimeRange = createTargetDateForMonth(9, Calendar.SEPTEMBER)
                    }
                    R.id.btnOctober -> {
                        mTaskViewModel?.setMonthButtonState(10)
                        mTaskViewModel?.setMonthConstant(Calendar.OCTOBER)
                        targetTimeRange = createTargetDateForMonth(10, Calendar.OCTOBER)
                    }
                    R.id.btnNovember -> {
                        mTaskViewModel?.setMonthButtonState(11)
                        mTaskViewModel?.setMonthConstant(Calendar.NOVEMBER)
                        targetTimeRange = createTargetDateForMonth(11, Calendar.NOVEMBER)
                    }
                    R.id.btnDecember -> {
                        mTaskViewModel?.setMonthButtonState(12)
                        mTaskViewModel?.setMonthConstant(Calendar.DECEMBER)
                        targetTimeRange = createTargetDateForMonth(12, Calendar.DECEMBER)
                        var aa: Long = targetTimeRange[0]
                    }
                    else -> {
                        Log.d(LOG_MSG, "NO ACTION SET MONTH BUTTON ONCLICKLISTENER")
                    }
                }
                calculatePoint(mUserId!!, targetTimeRange[0], targetTimeRange[1])
            }
        }
    }

    companion object {
        private const val LOG_MSG = "Calculate_Fragment"

        val sInstance : Fragment
            get() = CalculatePointFragment()
    }
}
package com.technology.mycow.spargrisenkt.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.model.ConstantCollection
import com.technology.mycow.spargrisenkt.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskCompletionAnimationFragment : Fragment() {

    private var mTaskViewModel : TaskViewModel? = null
    private var mIsCompleted : Boolean? = null

    protected lateinit var pinkGris : ImageView
    protected lateinit var blueGris : ImageView
    protected lateinit var greenGris : ImageView
    protected lateinit var redGris : ImageView
    protected lateinit var yellowGris : ImageView
    protected lateinit var brownGris : ImageView
    protected lateinit var limeGris : ImageView
    protected lateinit var mintGris : ImageView
    protected lateinit var orangeGris : ImageView
    protected lateinit var purpleGris : ImageView
    protected lateinit var skyblueGris : ImageView

    protected lateinit var blueGrisSecond : ImageView
    protected lateinit var greenGrisSecond : ImageView
    protected lateinit var yellowGrisSecond : ImageView
    protected lateinit var limeGrisSecond : ImageView
    protected lateinit var pinkGrisSecond : ImageView
    protected lateinit var redGrisSecond : ImageView
    protected lateinit var brownGrisSecond : ImageView
    protected lateinit var mintGrisSecond : ImageView
    protected lateinit var orangeGrisSecond : ImageView
    protected lateinit var purpleGrisSecond : ImageView
    protected lateinit var skyblueGrisSecond : ImageView

    protected lateinit var taskCompleteMessageTv : TextView

    protected lateinit var frameLayout : View
    protected var screenHeight = 0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TO PREVENT ANIMATION FROM RELOAD ACTIVITY BY ROTATING FOR NOW
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if(mTaskViewModel == null){
            val factory = TaskViewModel.Factory(requireActivity().application)
            mTaskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.task_completion_animation_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        setIcons()
//        frameLayout = requireActivity().findViewById(R.id.container)
//        frameLayout.setOnClickListener{ onStartAnimation(requireContext()) }

        mTaskViewModel?.isTaskCompleted?.observe(viewLifecycleOwner, Observer { isCompleted ->
            if(isCompleted){
                mIsCompleted = isCompleted
                onStartAnimation(requireContext())
            } else {
                mIsCompleted = isCompleted
            }
        })
        mTaskViewModel?.isTaskCompleted?.postValue(true)

    }

    override fun onResume() {
        super.onResume()

        val displayMetrics = DisplayMetrics()
        requireActivity().window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels.toFloat()


    }

    private fun setIcons(){

        pinkGris =  requireActivity().findViewById(R.id.pinkGris)
        blueGris =  requireActivity().findViewById(R.id.blueGris)
        greenGris =  requireActivity().findViewById(R.id.greenGris)
        redGris =  requireActivity().findViewById(R.id.redGris)
        yellowGris =  requireActivity().findViewById(R.id.yellowGris)
        brownGris =  requireActivity().findViewById(R.id.brownGris)
        limeGris =  requireActivity().findViewById(R.id.limeGris)
        mintGris =  requireActivity().findViewById(R.id.mintGris)
        orangeGris =  requireActivity().findViewById(R.id.orangeGris)
        purpleGris =  requireActivity().findViewById(R.id.purpleGris)
        skyblueGris =  requireActivity().findViewById(R.id.skyblueGris)

        blueGrisSecond =  requireActivity().findViewById(R.id.blueGrisSecond)
        greenGrisSecond =  requireActivity().findViewById(R.id.greenGrisSecond)
        yellowGrisSecond =  requireActivity().findViewById(R.id.yellowGrisSecond)
        limeGrisSecond =  requireActivity().findViewById(R.id.limeGrisSecond)

        taskCompleteMessageTv =  requireActivity().findViewById(R.id.taskCompleteMessageTv)

//        pinkGrisSecond =  requireActivity().findViewById(R.id.pinkGrisSecond)
//        redGrisSecond =  requireActivity().findViewById(R.id.redGrisSecond)
//        brownGrisSecond =  requireActivity().findViewById(R.id.brownGrisSecond)
//        mintGrisSecond =  requireActivity().findViewById(R.id.mintGrisSecond)
//        orangeGrisSecond =  requireActivity().findViewById(R.id.orangeGrisSecond)
//        purpleGrisSecond =  requireActivity().findViewById(R.id.purpleGrisSecond)
//        skyblueGrisSecond =  requireActivity().findViewById(R.id.skyblueGrisSecond)

    }

    companion object {
        private const val LOG_MSG = "TaskCompletionAnimationFragment"

        val sInstance : Fragment get() = TaskCompletionAnimationFragment()

    }


    fun onStartAnimation(context: Context) {

        //Center top
        val valueAnimator_first = ValueAnimator.ofFloat(0f, -screenHeight + 900f)
        valueAnimator_first.addUpdateListener {
            val value = it.animatedValue as Float
            pinkGris.translationY = value
        }

        //Left top
        val valueAnimator_second = ValueAnimator.ofFloat(0f, -screenHeight + 700f)
        valueAnimator_second.addUpdateListener {
            val value = it.animatedValue as Float
            blueGris.translationY = value
            blueGris.translationX = value / 3
        }

        //Right top
        val valueAnimator_third = ValueAnimator.ofFloat(0f, -screenHeight + 700f)
        valueAnimator_third.addUpdateListener {
            val value = it.animatedValue as Float
            greenGris.translationY = value
            greenGris.translationX = value / -3
        }

        //Center Bottom
        val valueAnimator_forth = ValueAnimator.ofFloat(0f, -screenHeight + 1320f)
        valueAnimator_forth.addUpdateListener {
            val value = it.animatedValue as Float
            redGris.translationY = value
        }

        //Left middle
        val valueAnimator_fifth = ValueAnimator.ofFloat(0f, -screenHeight + 1000f)
        valueAnimator_fifth.addUpdateListener {
            val value = it.animatedValue as Float
            yellowGris.translationY = value
            yellowGris.translationX = value / 2.2f
        }

        //Right middle
        val valueAnimator_sixth = ValueAnimator.ofFloat(0f, -screenHeight + 1000f)
        valueAnimator_sixth.addUpdateListener {
            val value = it.animatedValue as Float
            brownGris.translationY = value
            brownGris.translationX = value / -2.2f
        }

        //Center Left top
        val valueAnimator_seventh = ValueAnimator.ofFloat(0f, -screenHeight + 770f)
        valueAnimator_seventh.addUpdateListener {
            val value = it.animatedValue as Float
            limeGris.translationY = value
            limeGris.translationX = value / 4.9f
        }

        //Center Right top
        val valueAnimator_eighth = ValueAnimator.ofFloat(0f, -screenHeight + 770f)
        valueAnimator_eighth.addUpdateListener {
            val value = it.animatedValue as Float
            mintGris.translationY = value
            mintGris.translationX = value / -4.9f
        }

        //Left Bottom
        val valueAnimator_ninth = ValueAnimator.ofFloat(0f, -screenHeight + 1150f)
        valueAnimator_ninth.addUpdateListener {
            val value = it.animatedValue as Float
            orangeGris.translationY = value
            orangeGris.translationX = value / 4.0f
        }

        //Right Bottom
        val valueAnimator_tenth = ValueAnimator.ofFloat(0f, -screenHeight + 1150f)
        valueAnimator_tenth.addUpdateListener {
            val value = it.animatedValue as Float
            purpleGris.translationY = value
            purpleGris.translationX = value / -4.0f
        }

        //Left middle top
        val valueAnimator_eleventh = ValueAnimator.ofFloat(0f, -screenHeight + 860f)
        valueAnimator_eleventh.addUpdateListener {
            val value = it.animatedValue as Float
            skyblueGris.translationY = value
            skyblueGris.translationX = value / 2.4f
        }

        //Right middle top
        val valueAnimator_twelvnth = ValueAnimator.ofFloat(0f, -screenHeight + 860f)
        valueAnimator_twelvnth.addUpdateListener {
            val value = it.animatedValue as Float
            yellowGrisSecond.translationY = value
            yellowGrisSecond.translationX = value / -2.4f
        }

        //Center left
        val valueAnimator_thirteenth = ValueAnimator.ofFloat(0f, -screenHeight + 1030f)
        valueAnimator_thirteenth.addUpdateListener {
            val value = it.animatedValue as Float
            greenGrisSecond.translationY = value
            greenGrisSecond.translationX = value / 4.2f
        }

        //Center right
        val valueAnimator_fourteenth = ValueAnimator.ofFloat(0f, -screenHeight + 1030f)
        valueAnimator_fourteenth.addUpdateListener {
            val value = it.animatedValue as Float
            limeGrisSecond.translationY = value
            limeGrisSecond.translationX = value / -4.2f
        }

        //Center center
        val valueAnimator_fifteenth = ValueAnimator.ofFloat(0f, -screenHeight + 1020f)
        valueAnimator_fifteenth.addUpdateListener {
            val value = it.animatedValue as Float
            blueGrisSecond.translationY = value
        }

        //MESSAGE
        val valueAnimator_sixteenth = ValueAnimator.ofFloat(0f, -screenHeight + 980f)
        valueAnimator_sixteenth.addUpdateListener {
            val value = it.animatedValue as Float
            taskCompleteMessageTv.translationY = value
            //taskCompleteMessageTv.text = resources.getString(R.string.taskCompleteMessage)
        }


        val bouncer = AnimatorSet().apply {
            play(valueAnimator_first).with(valueAnimator_second)
            play(valueAnimator_first).with(valueAnimator_third)
            play(valueAnimator_first).with(valueAnimator_forth)
            play(valueAnimator_first).with(valueAnimator_fifth)
            play(valueAnimator_first).with(valueAnimator_sixth)
            play(valueAnimator_first).with(valueAnimator_seventh)
            play(valueAnimator_first).with(valueAnimator_eighth)
            play(valueAnimator_first).with(valueAnimator_ninth)
            play(valueAnimator_first).with(valueAnimator_tenth)
            play(valueAnimator_first).with(valueAnimator_eleventh)
            play(valueAnimator_first).with(valueAnimator_twelvnth)
            play(valueAnimator_first).with(valueAnimator_thirteenth)
            play(valueAnimator_first).with(valueAnimator_fourteenth)
            play(valueAnimator_first).with(valueAnimator_fifteenth)
            play(valueAnimator_first).with(valueAnimator_sixteenth)

        }


        runAnimation(bouncer)

    }

    fun runAnimation(bouncer: AnimatorSet) {

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            if(mIsCompleted == true){
                val animatorSet = AnimatorSet()
                animatorSet.apply {
                    play(bouncer)
                    val fm: FragmentManager = requireActivity().supportFragmentManager
                    duration = ConstantCollection.DEFAULT_ANIMATION_DURATION
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            Log.d(LOG_MSG, "END")

                            fm.popBackStack(ConstantCollection.FRAGMENT_TAG_TASK_LIST_BY_USER, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    //                    val intent = Intent(context, MainActivity::class.java)
    //                    startActivity(intent)
    //                    finish()
                        }
                    })
                    start()
                }
                mTaskViewModel?.isTaskCompleted?.postValue(false)
            }
        }

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        //TO PREVENT ANIMATION FROM RELOAD ACTIVITY BY ROTATING FOR NOW
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }



}
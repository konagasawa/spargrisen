package com.technology.mycow.spargrisenkt.helper

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.technology.mycow.spargrisenkt.view.CreateTaskFragment
import java.lang.Exception
import kotlin.math.abs

internal open class TouchGestureListener(context: Context) : View.OnTouchListener {

    private val gestureDetector : GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener(){

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            try {
                val diffY = e2!!.y - e1!!.y
                val diffX = e2!!.x - e1!!.x
                if(abs(diffX) > abs(diffY)){
                    if(diffX > 0){
                        Log.d(LOG_MSG, "SWIPE RIGHT" + diffX)
                        onSwipeToRight()
                    } else {
                        Log.d(LOG_MSG, "SWIPE LEFT" + diffY)
                        onSwipeToLeft()
                    }
                }

            } catch (e: Exception){
                Log.d(LOG_MSG, "Touch Gesture Exception: " + e)
            }

            return false
        }
    }

    companion object {
        private const val LOG_MSG = "TouchGestureListener"
    }

    open fun onSwipeToRight(){}
    open fun onSwipeToLeft(){}

}
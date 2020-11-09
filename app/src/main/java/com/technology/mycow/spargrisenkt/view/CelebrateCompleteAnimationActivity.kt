package com.technology.mycow.spargrisenkt.view

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.technology.mycow.spargrisenkt.R

//NOT USING NOW. MIGRATED ON TASKCOMPLETIONANIMATIONFRAGMENT
abstract class CelebrateCompleteAnimationActivity : AppCompatActivity() {

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

        setContentView(R.layout.activity_celebrate_complete_animation)

        setIcons()
        frameLayout = findViewById(R.id.container)
        frameLayout.setOnClickListener{ onStartAnimation(applicationContext) }

    }

    override fun onResume() {
        super.onResume()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels.toFloat()

        onStartAnimation(applicationContext)

    }

    override fun onPause() {
        super.onPause()

    }

    private fun setIcons(){

        pinkGris = findViewById(R.id.pinkGris)
        blueGris = findViewById(R.id.blueGris)
        greenGris = findViewById(R.id.greenGris)
        redGris = findViewById(R.id.redGris)
        yellowGris = findViewById(R.id.yellowGris)
        brownGris = findViewById(R.id.brownGris)
        limeGris = findViewById(R.id.limeGris)
        mintGris = findViewById(R.id.mintGris)
        orangeGris = findViewById(R.id.orangeGris)
        purpleGris = findViewById(R.id.purpleGris)
        skyblueGris = findViewById(R.id.skyblueGris)

        blueGrisSecond = findViewById(R.id.blueGrisSecond)
        greenGrisSecond = findViewById(R.id.greenGrisSecond)
        yellowGrisSecond = findViewById(R.id.yellowGrisSecond)
        limeGrisSecond = findViewById(R.id.limeGrisSecond)

        taskCompleteMessageTv = findViewById(R.id.taskCompleteMessageTv)

//        pinkGrisSecond = findViewById(R.id.pinkGrisSecond)
//        redGrisSecond = findViewById(R.id.redGrisSecond)
//        brownGrisSecond = findViewById(R.id.brownGrisSecond)
//        mintGrisSecond = findViewById(R.id.mintGrisSecond)
//        orangeGrisSecond = findViewById(R.id.orangeGrisSecond)
//        purpleGrisSecond = findViewById(R.id.purpleGrisSecond)
//        skyblueGrisSecond = findViewById(R.id.skyblueGrisSecond)

    }

    protected abstract fun onStartAnimation(context: Context)

    companion object {
        val DEFAULT_ANIMATION_DURATION = 2000L
    }


}
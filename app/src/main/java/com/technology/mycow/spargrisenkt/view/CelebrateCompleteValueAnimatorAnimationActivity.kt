package com.technology.mycow.spargrisenkt.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.core.app.ActivityCompat
import com.technology.mycow.spargrisenkt.R

//NOT USING NOW. MIGRATED ON TASKCOMPLETIONANIMATIONFRAGMENT
class CelebrateCompleteValueAnimatorAnimationActivity() : CelebrateCompleteAnimationActivity() {

    override fun onStartAnimation(context: Context) {

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
        val valueAnimator_sixteen = ValueAnimator.ofFloat(0f, -screenHeight + 980f)
        valueAnimator_sixteen.addUpdateListener {
            val value = it.animatedValue as Float
            taskCompleteMessageTv.translationY = value
            taskCompleteMessageTv.text = resources.getString(R.string.taskCompleteMessage)
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
            play(valueAnimator_first).with(valueAnimator_sixteen)
        }
        val animatorSet = AnimatorSet()
        animatorSet.apply {
            play(bouncer)
            duration = 4000L
            addListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    Log.d(LOG_MSG, "END")

                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            })
            start()
        }


//        valueAnimator_first.interpolator = LinearInterpolator()
//        valueAnimator_first.duration = DEFAULT_ANIMATION_DURATION
//        valueAnimator_first.start()


//        pinkGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        blueGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        greenGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        redGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        yellowGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        brownGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        limeGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        mintGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//        orangeGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        purpleGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        skyblueGris.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        pinkGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        blueGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        greenGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        redGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        yellowGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        brownGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        limeGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        mintGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        orangeGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        purpleGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()
//
//        skyblueGrisSecond.animate()
//                .x(50f)
//                .y(50f)
//                .alpha(0.5f)
//                .setDuration(DEFAULT_ANIMATION_DURATION)
//                .start()


    }

    companion object {
        private const val LOG_MSG = "CelecbrateCompleteValueAnimatorAnimationActivity"
    }

}
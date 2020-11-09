package com.technology.mycow.spargrisenkt.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.view.*


class SplashActivity : AppCompatActivity()

{

    var splashBinding: ActivitySplashBinding? = null

    // Called when the activity is first created.
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        hideSystemUI()

        val intent = Intent(this, MainActivity::class.java)

        //TEST TO OPEN ANIMATION
        //val intent = Intent(this, CelebrateCompleteValueAnimatorAnimationActivity::class.java)

        val handler = Handler()
        val runnable = Runnable {
            startActivity(intent)
            handler.removeCallbacksAndMessages(null)
            finish()
        }
        handler.postDelayed(runnable, 2000)

    }

    private fun hideSystemUI(){

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    companion object {
        private const val LOG_MSG = "SPLASH"
    }


}
package ru.harlion.curtainspb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.harlion.curtainspb.ui.auth.AuthFragment
import ru.harlion.curtainspb.ui.splash.SplashFragment
import ru.harlion.curtainspb.utils.replaceFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    var timer: Timer? = null
    var mTimerTask: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(SplashFragment(), false)

        timer = Timer()
        mTimerTask = MyTimerTask()
        timer!!.schedule(mTimerTask, 2000)

    }

    internal inner class MyTimerTask : TimerTask() {
        override fun run() {
            //    finish()

            replaceFragment(AuthFragment(),false)
        }
    }
}
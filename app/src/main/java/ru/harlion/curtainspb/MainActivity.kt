package ru.harlion.curtainspb

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.harlion.curtainspb.repo.AuthPrefs
import ru.harlion.curtainspb.repo.data.DataRepository
import ru.harlion.curtainspb.ui.auth.authorization.AuthFragment
import ru.harlion.curtainspb.ui.main_menu.MainMenuFragment
import ru.harlion.curtainspb.ui.splash.SplashFragment
import ru.harlion.curtainspb.utils.replaceFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private var timer: Timer? = null
    private var mTimerTask: TimerTask? = null
    private lateinit var prefs: AuthPrefs

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        DataRepository.context = newBase!!.applicationContext
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = AuthPrefs(this.getSharedPreferences("user", MODE_PRIVATE))

        if (savedInstanceState == null) {
            replaceFragment(SplashFragment(), false)

            timer = Timer()
            mTimerTask = MyTimerTask()
            timer!!.schedule(mTimerTask, 2000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer!!.cancel()
    }

    internal inner class MyTimerTask : TimerTask() {
        override fun run() {
            if (prefs.hasToken()) {
                replaceFragment(MainMenuFragment(), false)
            } else {
                replaceFragment(AuthFragment(), false)
            }
        }
    }
}
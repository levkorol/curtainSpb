package ru.harlion.curtainspb.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.harlion.curtainspb.R

//открыть активити
fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

//из активити в фрагмент
fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    supportFragmentManager.beginTransaction()
        .also { transition ->
            if (addStack) {
                transition.addToBackStack(null)
            }
        }
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .replace(R.id.fragmentContainer, fragment)
        .commit()
}

//во фрагментах
fun Fragment.replaceFragment(fragment: Fragment) {
    activity?.supportFragmentManager?.beginTransaction()
        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ?.addToBackStack(null)
        ?.replace(R.id.fragmentContainer, fragment)
        ?.commit()
}
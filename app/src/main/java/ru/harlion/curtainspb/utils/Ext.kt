package ru.harlion.curtainspb.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast

fun Context.showToast(message: String) {
    val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}
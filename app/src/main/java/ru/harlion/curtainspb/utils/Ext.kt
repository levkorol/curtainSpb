package ru.harlion.curtainspb.utils

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import ru.harlion.curtainspb.ui.main_menu.saved_projects.ShimmerImageDrawable

fun Context.showToast(message: String) {
    val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun ImageView.downloadAndSetImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(ShimmerImageDrawable())
        .into(this)
}

val Int.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density

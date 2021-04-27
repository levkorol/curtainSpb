package ru.harlion.curtainspb.utils

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider


private val OVAL_OUTLINE = object : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setOval(0, 0, view.width, view.height)
    }
}
fun View.ovalOutline() {
    outlineProvider = OVAL_OUTLINE
}

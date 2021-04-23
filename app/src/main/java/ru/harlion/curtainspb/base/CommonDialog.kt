package ru.harlion.curtainspb.base

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.dialog_common.*
import ru.harlion.curtainspb.R

class CommonDialog(val context: Context) {
    private var dialog: Dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_common)
        dialog.window!!.apply {
            setBackgroundDrawable(null)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            decorView.apply {
                layoutParams =
                    ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                val inset = (50 * context.resources.displayMetrics.density).toInt()
                background = InsetDrawable(
                    context.getDrawable(R.drawable.bg_common_dialog),
                    inset,
                    0,
                    inset,
                    0
                )
                setPadding(inset, 0, inset, 0)
            }
        }
    }

    fun setMessage(msg: String) {
        dialog.d_common_message_tv.text = msg
        dialog.d_common_message_tv.visibility = View.VISIBLE
    }

    fun setPositiveButton(title: String, onClickListener: View.OnClickListener?) {
        dialog.d_common_positive_button.visibility = View.VISIBLE
        dialog.d_common_positive_button.text = title
        dialog.d_common_positive_button.setOnClickListener {
            onClickListener?.onClick(it)
            dialog.dismiss()
        }
    }

    fun setNegativeButton(title: String, onClickListener: View.OnClickListener?) {
        dialog.d_common_negative_button.visibility = View.VISIBLE
        dialog.d_common_negative_button.text = title
        dialog.d_common_negative_button.setOnClickListener {
            onClickListener?.onClick(it)
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
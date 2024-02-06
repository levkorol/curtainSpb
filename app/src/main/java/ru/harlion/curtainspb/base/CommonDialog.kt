package ru.harlion.curtainspb.base

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.databinding.DialogCommonBinding

class CommonDialog(val context: Context) {
    private lateinit var binding: DialogCommonBinding
    private var dialog: Dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        binding = DialogCommonBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
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

    fun setTitle(msg: String) {
        binding.dCommonTitleTv.text = msg
        binding.dCommonTitleTv.visibility = View.VISIBLE
    }

    fun setMessage(msg: String) {
        binding.dCommonMessageTv.text = msg
        binding.dCommonMessageTv.visibility = View.VISIBLE
    }

    fun setPositiveButton(title: String, onClickListener: View.OnClickListener?) {
        binding.dCommonPositiveButton.visibility = View.VISIBLE
        binding.dCommonPositiveButton.text = title
        binding.dCommonPositiveButton.setOnClickListener {
            onClickListener?.onClick(it)
            dialog.dismiss()
        }
    }

    fun setNegativeButton(title: String, onClickListener: View.OnClickListener?) {
        binding.dCommonNegativeButton.visibility = View.VISIBLE
        binding.dCommonNegativeButton.text = title
        binding.dCommonNegativeButton.setOnClickListener {
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
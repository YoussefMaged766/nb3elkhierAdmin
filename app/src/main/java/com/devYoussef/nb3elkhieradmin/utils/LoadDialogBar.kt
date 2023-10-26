package com.devYoussef.nb3elkhieradmin.utils


import android.app.Dialog
import android.content.Context
import com.devYoussef.nb3elkhieradmin.R


class LoadDialogBar (private  val context: Context) {

    private var dialog: Dialog? = null
    fun show() {


        dialog = Dialog(context)
        dialog?.setContentView(R.layout.loading)
        dialog?.setCancelable(false)
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog?.show()
    }

    fun hide() {
        dialog?.dismiss()
    }

}
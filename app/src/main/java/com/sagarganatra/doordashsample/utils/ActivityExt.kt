package com.sagarganatra.doordashsample.utils

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sagarganatra.doordashsample.R
import kotlinx.android.synthetic.main.dialog_loading.view.*

fun Activity.createLoadingDialog(inMessage: String): AlertDialog {
    val builder = AlertDialog.Builder(this)
    builder.setCancelable(false)
    val view = View.inflate(this, R.layout.dialog_loading, null)
    view.loading_text.text = inMessage
    builder.setView(view)
    return builder.create()
}
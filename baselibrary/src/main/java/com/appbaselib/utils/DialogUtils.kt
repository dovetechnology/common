package com.appbaselib.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

object DialogUtils {

    fun getDefaultProgressDialog(mContext: Context): ProgressDialog {

        return ProgressDialog(mContext)

    }

    fun getDefaultDialog(mContext: Context, content: String,
                         positiveText: String = "确定", negativeButton: String = "取消", title: String = "", mOnClickListener: (dialog: DialogInterface?) -> Unit): AlertDialog.Builder {

        return AlertDialog.Builder(mContext).setTitle(title).setMessage(content).setNegativeButton(negativeButton, null).setPositiveButton(positiveText, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                mOnClickListener(dialog)
            }

        })

    }

    fun getDefaultDialog(mContext: Context, content: View,
                         positiveText: String = "确定", negativeButton: String = "取消", title: String = "", mOnClickListener: (dialog: DialogInterface?, view: View) -> Unit): AlertDialog.Builder {

        return AlertDialog.Builder(mContext).setTitle(title).setView(content).setNegativeButton(negativeButton, null).setPositiveButton(positiveText, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                mOnClickListener(dialog, content)
            }

        })

    }

}

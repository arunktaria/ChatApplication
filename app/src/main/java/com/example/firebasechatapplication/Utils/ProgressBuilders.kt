package com.example.firebasechatapplication.Utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.firebasechatapplication.R

object ProgressBuilders {
   lateinit var progress:Dialog
    fun show(context: Context, text:String)
    {
        progress = Dialog(context)
        progress.setContentView(R.layout.progresslayout)
        progress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progress.setTitle(text)
        progress.show()

    }
    fun dismiss()
    {
        progress.dismiss()
    }
}
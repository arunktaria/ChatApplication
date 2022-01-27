package com.example.firebasechatapplication.Utils

import android.content.Context
import android.widget.Toast
import com.example.firebasechatapplication.BaseApplications.BaseApplication

object ToastBuilder {

    fun toast(context: Context,text:String)
    {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }

}
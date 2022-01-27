package com.example.firebasechatapplication.BaseApplications

import android.app.Application
import android.content.Context
import com.example.firebasechatapplication.Repository.Repository

class BaseApplication : Application(){

    lateinit var context: Context
    lateinit var repository: Repository
    override fun onCreate() {
        super.onCreate()
        context=applicationContext
        repository= Repository()

    }


}
package com.example.firebasechatapplication.Utils

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager

class UserDataSharedPreference() {

    companion object{

        fun setValues( context: Context, email:String, password:String)
        {
            val preferences=PreferenceManager.getDefaultSharedPreferences(context)
            val editor=preferences.edit()
            editor.putString("email",email)
            editor.putString("password",password)
            editor.apply()
            editor.commit()


        }
        fun getUserPreferences(context: Context) : Bundle
        {
            val preferences=PreferenceManager.getDefaultSharedPreferences(context)
            val username=preferences.getString("email","null").toString()
            val password=preferences.getString("password","null").toString()
            val builder=Bundle()
            builder.putString("email",username)
            builder.putString("password",password)
            return builder
        }

    }


}
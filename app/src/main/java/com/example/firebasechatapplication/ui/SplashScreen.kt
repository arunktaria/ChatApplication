package com.example.firebasechatapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatapplication.R
import com.example.firebasechatapplication.Repository.Repository
import com.example.firebasechatapplication.UserData
import com.example.firebasechatapplication.Utils.UserDataSharedPreference
import com.example.firebasechatapplication.ViewModels.Factories.LoginViewModelFactory
import com.example.firebasechatapplication.ViewModels.LoginSignUpViewModel
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var viewmodel:LoginSignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        auth = FirebaseAuth.getInstance()
        val repository = Repository()
         viewmodel = ViewModelProvider(
            this,
            LoginViewModelFactory(repository)
        ).get(LoginSignUpViewModel::class.java)

        checkingLogins()



        Handler(Looper.getMainLooper()).postDelayed(Runnable {
        finish()
        },2000)


    }

    fun checkingLogins()
    {
        val bundle = UserDataSharedPreference.getUserPreferences(this)
        val ob = UserData()
        ob.email = bundle.getString("email", "null").toString()
        ob.password = bundle.getString("password", "null").toString()
       viewmodel.splashlogin(FirebaseAuth.getInstance(), this, ob)

    }

}
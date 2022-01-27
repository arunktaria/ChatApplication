package com.example.firebasechatapplication.ViewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.firebasechatapplication.PostUserData
import com.example.firebasechatapplication.UserData
import com.example.firebasechatapplication.Repository.Repository
import com.example.firebasechatapplication.Utils.UserDataSharedPreference
import com.example.firebasechatapplication.Utils.ProgressBuilders
import com.example.firebasechatapplication.Utils.ToastBuilder
import com.example.firebasechatapplication.ui.ProfileListMain
import com.example.firebasechatapplication.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class LoginSignUpViewModel(val repository: Repository) : ViewModel()
{
     var progress= ProgressBuilders
fun signUp(auth: FirebaseAuth, context: Context, userLoginsData: UserData, database:DatabaseReference) {

    progress.show(context,"")
    auth.createUserWithEmailAndPassword(userLoginsData.email.toString(),userLoginsData.password.toString())
        .addOnCompleteListener {
            if (it.isSuccessful)
            {
                val ob= PostUserData()
                ob.username=userLoginsData.username.toString()
                ob.uid=auth.uid.toString()
                ob.email=userLoginsData.email.toString()
                ob.password= userLoginsData.password.toString()
                database.setValue(ob)
                ToastBuilder.toast(context,"success to sign in ")
                context.startActivity(Intent(context, LoginActivity::class.java))
                progress.dismiss()
            }else
            {
                ToastBuilder.toast(context,it.exception.toString()+"  "+userLoginsData.email.toString())
                progress.dismiss()
            }
        }.addOnCanceledListener {
            ToastBuilder.toast(context,"fail to sign in")
           progress.dismiss()
            }

}



    fun Login(auth: FirebaseAuth, context: Context, userLoginsData1: UserData) : Boolean
    {
        var issuccess=false
        progress.show(context,"")
        auth.signInWithEmailAndPassword(userLoginsData1.email.toString(),userLoginsData1.password.toString())
            .addOnCompleteListener {
                if (it.isSuccessful)
                {
                    UserDataSharedPreference.setValues(context,userLoginsData1.email.toString(),userLoginsData1.password)

                    ToastBuilder.toast(context,"success Login ")
                    context.startActivity(Intent(context, ProfileListMain::class.java))
                   progress.dismiss()
                    issuccess=true
                }else
                {
                    ToastBuilder.toast(context,"something went wrong!"+it.exception.toString())
                    progress.dismiss()
                    issuccess=false
                }
            }.addOnCanceledListener {
                ToastBuilder.toast(context,"fail to sign in")
                progress.dismiss()
            }
        return issuccess
    }



    fun splashlogin(auth: FirebaseAuth, context: Context, userLoginsData1: UserData) : Boolean
    {
        var issuccess=true

        auth.signInWithEmailAndPassword(userLoginsData1.email.toString(),userLoginsData1.password.toString())
            .addOnCompleteListener {
                if (it.isSuccessful)
                { issuccess=true
                    context.startActivity(Intent(context, ProfileListMain::class.java))
                }else
                {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                }
            }.addOnCanceledListener {
                context.startActivity(Intent(context, LoginActivity::class.java))
                ToastBuilder.toast(context,"fail to sign in")
            }
        return issuccess
    }


}
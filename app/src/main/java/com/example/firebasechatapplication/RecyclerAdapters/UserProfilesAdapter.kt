package com.example.firebasechatapplication.RecyclerAdapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup


import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapplication.ui.MessageHandlerActivity
import com.example.firebasechatapplication.UserData

import com.example.firebasechatapplication.databinding.UserslistlayoutBinding

class UserProfilesAdapter(val context:Context, val list:ArrayList<UserData>) :RecyclerView.Adapter<UserProfilesAdapter.ViewHolderClass>()
{
class ViewHolderClass(var view: UserslistlayoutBinding) : RecyclerView.ViewHolder(view.root)
{


    fun bind(list: UserData, context: Context)
    {
        view.chatuser=list
        view.chatlayoutid.setOnClickListener {
            Log.d("TAG", "bind: "+list.uid)
            val intent=Intent(context, MessageHandlerActivity::class.java)
            intent.apply {
                putExtra("uid",list.uid)
                putExtra("username",list.username)
                putExtra("profilephotourl",list.profileimage)
            }
            context.startActivity(intent)
        }


     }

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val binding=UserslistlayoutBinding.inflate(LayoutInflater.from(context))
        return ViewHolderClass(binding)

    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

    holder.bind(list[position],context)



    }

    override fun getItemCount(): Int {
        return list.size
    }
}
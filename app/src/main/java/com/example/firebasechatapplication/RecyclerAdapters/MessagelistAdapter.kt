package com.example.firebasechatapplication.RecyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapplication.UserChatsModel
import com.example.firebasechatapplication.databinding.OurchatlayoutBinding
import com.example.firebasechatapplication.databinding.SenderlayoutBinding
import com.google.firebase.auth.FirebaseAuth

class MessagelistAdapter(val context: Context, val list:ArrayList<UserChatsModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    var auth:FirebaseAuth= FirebaseAuth.getInstance()

    val itemour:Int=1
    val itemsender:Int=2
    class senderViewHolder(val view: SenderlayoutBinding) : RecyclerView.ViewHolder(view.root)
    {
        fun bind(list: UserChatsModel) {
            view.chat=list
        }

    }

    class ourViewholder1(val view: OurchatlayoutBinding) : RecyclerView.ViewHolder(view.root)
    {
        fun bind(list: UserChatsModel)
        {
            view.chat=list
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == itemour) {
            val   binding1 = OurchatlayoutBinding.inflate(LayoutInflater.from(context))
       return ourViewholder1(binding1)
        }
        if (viewType == itemsender)
        {
         val  binding2 = SenderlayoutBinding.inflate(LayoutInflater.from(context))
          return senderViewHolder(binding2)
        }
        val binding =OurchatlayoutBinding.inflate(LayoutInflater.from(context))

        return ourViewholder1(binding)
    }

    override fun getItemViewType(position: Int): Int {
        if (!auth.currentUser?.uid.toString().equals(list.get(position).senderuid)) {
            return itemour
        } else
        {
            return itemsender
        }
        return itemour
    }

    override fun getItemCount(): Int {
        return list.size
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!auth.currentUser?.uid.toString().equals(list.get(position).senderuid)) {
            (holder as ourViewholder1).bind(list.get(position))
        } else {
            (holder as senderViewHolder).bind(list.get(position))
        }
    }
}

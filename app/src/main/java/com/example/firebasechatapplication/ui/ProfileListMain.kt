package com.example.firebasechatapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatapplication.RecyclerAdapters.UserProfilesAdapter

import com.example.firebasechatapplication.UserChatsModel
import com.example.firebasechatapplication.UserData
import com.example.firebasechatapplication.Utils.ToastBuilder
import com.example.firebasechatapplication.ViewModels.PostMessageViewModel
import com.example.firebasechatapplication.databinding.ActivityChatListMainBinding
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileListMain : AppCompatActivity() {
var TAG="TAG"
   lateinit var binding:ActivityChatListMainBinding
    lateinit var adapter: FirebaseListAdapter<UserChatsModel>
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    var context=this
    var list=ArrayList<UserData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatListMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference.child("user")


        list= arrayListOf()

        binding.recyclerview.layoutManager=LinearLayoutManager(this)
        var adapter= UserProfilesAdapter(context,list)
        binding.recyclerview.adapter=adapter
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               for(i in snapshot.children)
               {
                   val ob=i.getValue(UserData::class.java)
                   list.add(ob!!)
                   Log.d(TAG, "onDataChange: "+ob.username)
                   adapter.notifyDataSetChanged()

               }

            }

            override fun onCancelled(error: DatabaseError) {
                ToastBuilder.toast(context,error.message.toString())
            }
        })



checkcalling()
    }



fun checkcalling()
{
    val senderId=auth.currentUser?.uid.toString()
    val databaser=FirebaseDatabase.getInstance().reference
    val viewmodel = ViewModelProvider(this).get(PostMessageViewModel::class.java)
  /*
    for (i in 0..list.size)
    {
    var struid=auth.currentUser?.uid+list.get(i)
    }

    viewmodel.checkourIncomingCall(databaser,senderId+recieverId)

    viewmodel.tempourstatus.observe(this, Observer {
        Log.d("TAG", "status: "+it.toString())

        if(it.toString().equals("active"))
        {
            val intent= Intent(MessageHandlerActivity@this, CallingScreen::class.java)
            intent.putExtra("sender",senderId)
            intent.putExtra("reciever",recieverId)
            intent.putExtra("username",i.getStringExtra("username").toString())
            startActivity(intent)
        }
    })
*/

}



}
package com.example.firebasechatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatapplication.ViewModels.CheckingCallsViewModel
import com.example.firebasechatapplication.ViewModels.PostMessageViewModel
import com.example.firebasechatapplication.databinding.ActivityAudioCallBinding
import com.example.firebasechatapplication.databinding.ActivityCallingScreenBinding
import com.google.firebase.database.FirebaseDatabase

class AudioCallingScreen : AppCompatActivity() {
    lateinit var binding: ActivityAudioCallBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioCallBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val checkCalls = ViewModelProvider(this).get(CheckingCallsViewModel::class.java)
        val database = FirebaseDatabase.getInstance().reference


        var rec = intent.getStringExtra("reciever").toString()
        var send = intent.getStringExtra("sender").toString()

        binding.callinguser.text=intent.getStringExtra("username")

        checkCalls.checkourIncomingVoiceCall(database, send + rec)

        checkCalls.tempourstatus.observe(this, Observer {
            if (it.toString().equals("inactive")) {
                this.finish()
            }
        })

        binding.endcall.setOnClickListener {
            database.child("videocall").child(rec + send).child("videocall")
                .setValue("inactive").addOnCompleteListener {
                    database.child("videocall").child(send + rec).child("videocall")
                        .setValue("inactive")
                  this.finish()

                }
        }


        binding.callpick.setOnClickListener {

            database.child("videocall").child(rec + send).child("videocall")
                .setValue("active").addOnCompleteListener {
                    database.child("videocall").child(send + rec).child("videocall")
                        .setValue("active")
                }
            this.finish()
            val intent = (Intent(MessageHandlerActivity@ this, VideoCallActivitytemp::class.java))
            intent.putExtra("sender", send)
            intent.putExtra("reciever", rec)
            startActivity(intent)


        }

    }
}


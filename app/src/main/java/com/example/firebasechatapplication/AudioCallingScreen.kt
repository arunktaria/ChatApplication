package com.example.firebasechatapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatapplication.ViewModels.CheckingCallsViewModel
import com.example.firebasechatapplication.ViewModels.PostMessageViewModel
import com.example.firebasechatapplication.databinding.ActivityAudioCallBinding
import com.example.firebasechatapplication.databinding.ActivityAudioCallPickedupBinding
import com.example.firebasechatapplication.databinding.ActivityCallingScreenBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine

class AudioCallingScreen : AppCompatActivity() {
    lateinit var binding: ActivityAudioCallBinding
    lateinit var rec :String
    lateinit var send:String
    lateinit var mRtcEngine: RtcEngine
    val firebase=FirebaseDatabase.getInstance().reference
    var appId:String="9241e157864c4536989fde803607d02a"
    var token:String="0069241e157864c4536989fde803607d02aIAB49V3UYSKFYgj2j46qe70wAiFdqDL80P+9N8RyCTjgwqgy6Q0AAAAAEADC8VeAazLsYQEAAQBfMuxh"



    lateinit var database :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

         rec = intent.getStringExtra("reciever").toString()
         send = intent.getStringExtra("sender").toString()

        val checkCalls = ViewModelProvider(this).get(CheckingCallsViewModel::class.java)
        database = FirebaseDatabase.getInstance().reference

        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),100)
        }


        binding.callinguser.text=intent.getStringExtra("username")

        checkCalls.checkourIncomingVoiceCall(database, send + rec)

        checkCalls.tempourvoicestatus.observe(this, Observer {
            if (it.toString().equals("inactive"))
            {
                    this.finish()
                    Log.d("TAG", "in state of call ending by  client: ")

            }

        })

        checkCalls.checkingCallConntion(database,send+rec)
        checkCalls.tempcall.observe(this, Observer {
            binding.audiocalluserclient.text=it.toString()


        })




        binding.endcall.setOnClickListener {
            database.child("voicecall").child(rec + send).child("voicecall")
                .setValue("inactive").addOnCompleteListener {
                    database.child("voicecall").child(send + rec).child("voicecall")
                        .setValue("inactive")
                  this.finish()

                }
        }


        binding.callpick.setOnClickListener {

            database.child("voicecall").child(rec + send).child("voicecall")
                .setValue("active").addOnCompleteListener {
                    database.child("voicecall").child(send + rec).child("voicecall")
                        .setValue("active").addOnCompleteListener {
                            database.child("callconnect").child(rec + send).child("callconnect")
                                .setValue("connected").addOnCompleteListener {
                                    database.child("callconnect").child(send + rec)
                                        .child("callconnect")
                                        .setValue("connected").addOnCompleteListener {
                                            binding.audiocalluserclient.text="connected"
                                           initializeAndJoinChannel()

                                        }




                                }

                        }
                }




        }

    }


    private fun initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(this, appId, mRtcEventHandler)
        } catch (e: Exception) {

        }

    }



    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        // Listen for the remote user joining the channel to get the uid of the user.
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread {
                /* val remoteFrame = RtcEngine.CreateRendererView(this@VideoCallActivitytemp)
                 mRtcEngine.enableVideo()
                 remoteFrame.setZOrderMediaOverlay(true)
                 binding.localVideoViewContainer.addView(remoteFrame)
                 mRtcEngine!!.setupRemoteVideo(VideoCanvas(remoteFrame, VideoCanvas.RENDER_MODE_FIT, uid))
 */


            }
        }

    }







    override fun onDestroy() {
        super.onDestroy()
        //    mRtcEngine?.leaveChannel()
        //   RtcEngine.destroy()

        database.child("voicecall").child(send+rec).child("voicecall")
            .setValue("inactive").addOnCompleteListener {
                database.child("voicecall").child(  rec+send).child("voicecall")
                    .setValue("inactive")
            }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==100)
        {
            Toast.makeText(this,"permission granted", Toast.LENGTH_SHORT).show()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }





}





























/*
 val intent = (Intent(MessageHandlerActivity@ this, AudioCallActivity::class.java))
                                            intent.putExtra("sender", send)
                                            intent.putExtra("reciever", rec)
                                            startActivity(intent)
 */

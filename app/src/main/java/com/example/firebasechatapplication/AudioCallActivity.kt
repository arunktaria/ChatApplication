package com.example.firebasechatapplication

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatapplication.ViewModels.CheckingCallsViewModel
import com.example.firebasechatapplication.ViewModels.PostMessageViewModel
import com.example.firebasechatapplication.databinding.ActivityAudioCallPickedupBinding
import com.example.firebasechatapplication.databinding.VideocalllayoutBinding
import com.google.firebase.database.FirebaseDatabase
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine

class AudioCallActivity : AppCompatActivity() {
    lateinit var mRtcEngine: RtcEngine
    lateinit var binding : ActivityAudioCallPickedupBinding
    lateinit var senderId:String
    lateinit var recieverId:String
    val firebase=FirebaseDatabase.getInstance().reference
    var appId:String="9241e157864c4536989fde803607d02a"
    var token:String="0069241e157864c4536989fde803607d02aIAB49V3UYSKFYgj2j46qe70wAiFdqDL80P+9N8RyCTjgwqgy6Q0AAAAAEADC8VeAazLsYQEAAQBfMuxh"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAudioCallPickedupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        senderId=intent.getStringExtra("sender").toString()
        recieverId=intent.getStringExtra("reciever").toString()

        val checkCalls = ViewModelProvider(this).get(CheckingCallsViewModel::class.java)
        val checkcalls = ViewModelProvider(this).get(CheckingCallsViewModel::class.java)
        val database= FirebaseDatabase.getInstance().reference




        checkCalls.checkingCallConntion(database,senderId+recieverId)
        checkCalls.tempcall.observe(this, Observer {
            binding.conntextview.text=it.toString()


        })


        binding.endcall.setOnClickListener {
            firebase.child("voicecall").child(senderId+recieverId).child("voicecall")
                .setValue("inactive").addOnCompleteListener {
                    firebase.child("voicecall").child(  recieverId+senderId).child("voicecall")
                        .setValue("inactive").addOnCompleteListener {


                            firebase.child("callconnect").child(senderId + recieverId).child("callconnect")
                                .setValue("disconnected").addOnCompleteListener {
                                    firebase.child("callconnect").child(recieverId+ senderId)
                                        .child("callconnect")
                                        .setValue("disconnected")
                                }

                        }
                }
            finish()

        }


        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),100)
        }
       // initializeAndJoinChannel()




        checkcalls.checkourIncomingVoiceCall(database,recieverId+senderId)

         checkcalls.tempourvoicestatus.observe(this, Observer {
             Log.d("TAG", "checking user call is ended "+it.toString())
            if(it.toString().equals("inactive"))
            {
                finish()
            }

            Log.d("TAG", "in calling endd "+it.toString())
        })


    }
























  /*
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

*/





    override fun onDestroy() {
        super.onDestroy()
    //    mRtcEngine?.leaveChannel()
     //   RtcEngine.destroy()

        firebase.child("voicecall").child(senderId+recieverId).child("voicecall")
            .setValue("inactive").addOnCompleteListener {
                firebase.child("voicecall").child(  recieverId+senderId).child("voicecall")
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
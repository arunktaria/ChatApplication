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
import com.example.firebasechatapplication.databinding.VideocalllayoutBinding
import com.google.firebase.database.FirebaseDatabase
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration


class VideoCallActivitytemp : AppCompatActivity() {
    lateinit var mRtcEngine: RtcEngine
    lateinit var binding : VideocalllayoutBinding
    lateinit var senderId:String
    lateinit var recieverId:String
    var appId:String="9241e157864c4536989fde803607d02a"
    var token:String="0069241e157864c4536989fde803607d02aIAB49V3UYSKFYgj2j46qe70wAiFdqDL80P+9N8RyCTjgwqgy6Q0AAAAAEADC8VeAazLsYQEAAQBfMuxh"
    val firebase=FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= VideocalllayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnend.setOnClickListener {
            firebase.child("videocall").child(senderId+recieverId).child("videocall")
                .setValue("inactive").addOnCompleteListener {
                    firebase.child("videocall").child(  recieverId+senderId).child("videocall")
                        .setValue("inactive")
                }
            finish()
        }

        senderId=intent.getStringExtra("sender").toString()
        recieverId=intent.getStringExtra("reciever").toString()

        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),100)
        }
        initializeAndJoinChannel()


        val checkCalls = ViewModelProvider(this).get(CheckingCallsViewModel::class.java)
        val database=FirebaseDatabase.getInstance().reference


       // var sen=intent.getStringExtra("reciever").toString()
       // var rec=intent.getStringExtra("sender").toString()
        checkCalls.checkourIncomingVideoCall(database,recieverId+senderId)
        checkCalls.tempourstatus.observe(this, Observer {

            if(it.toString().equals("inactive"))
            {
                finish()

            }

            Log.d("TAG", "in calling endd "+it.toString())
        })


    }


    // Kotlin
    private fun initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(this, appId, mRtcEventHandler)
        } catch (e: Exception) {

        }

        // By default, video is disabled, and you need to call enableVideo to start a video stream.
        setupSession()
        setlocalcamera()


    }


    fun setlocalcamera()
    { // Call CreateRendererView to create a SurfaceView object and add it as a child to the FrameLayout.
        val localFrame = RtcEngine.CreateRendererView(this)
        localFrame.setZOrderMediaOverlay(true)
        binding.remoteVideoViewContainer.addView(localFrame)
        // Pass the SurfaceView object to Agora so that it renders the local video.
        mRtcEngine!!.setupLocalVideo(VideoCanvas(localFrame, VideoCanvas.RENDER_MODE_FILL, 0))
        // Join the channel with a token.
        mRtcEngine!!.joinChannel(token, "arun", "", 0)
    }


    private fun setupSession() {
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
        mRtcEngine.enableVideo()
        mRtcEngine.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }





    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        // Listen for the remote user joining the channel to get the uid of the user.
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread {
                val remoteFrame = RtcEngine.CreateRendererView(this@VideoCallActivitytemp)
                mRtcEngine.enableVideo()
                remoteFrame.setZOrderMediaOverlay(true)
                binding.localVideoViewContainer.addView(remoteFrame)
                mRtcEngine!!.setupRemoteVideo(VideoCanvas(remoteFrame, VideoCanvas.RENDER_MODE_FIT, uid))

            }
        }
    }







    override fun onDestroy() {
        super.onDestroy()
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()

        firebase.child("videocall").child(senderId+recieverId).child("videocall")
            .setValue("inactive").addOnCompleteListener {
                firebase.child("videocall").child(  recieverId+senderId).child("videocall")
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
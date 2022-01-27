package com.example.firebasechatapplication.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CheckingCallsViewModel : ViewModel() {

    private  val ourstatus= MutableLiveData<String>()
    val tempourstatus: LiveData<String>
        get() = ourstatus

    fun checkourIncomingVideoCall(databaseReference: DatabaseReference, id:String)
    {
        databaseReference.child("videocall").child(id).child("videocall").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var status=snapshot.value.toString()
                    ourstatus.postValue(status)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: "+error.message.toString())
                }
            }
        )
    }

    private  val ourvoicestatus= MutableLiveData<String>()
    val tempourvoicestatus: LiveData<String>
        get() =ourvoicestatus

    fun checkourIncomingVoiceCall(databaseReference: DatabaseReference, id:String)
    {
        databaseReference.child("voicecall").child(id).child("voicecall").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var status=snapshot.value.toString()
                    ourvoicestatus.postValue(status)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: "+error.message.toString())
                }
            }
        )
    }









}
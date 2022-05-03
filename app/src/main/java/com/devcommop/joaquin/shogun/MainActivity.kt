package com.devcommop.joaquin.shogun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.devcommop.joaquin.shogun.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic" //subsribe  a topic called as myTopic
class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.sendBtn.setOnClickListener{
            val title = binding.titleET.text.toString()
            val message = binding.messageET.text.toString()
            val token = binding.tokenET.text.toString()
            if(title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    data = NotificationData(title = title, message = message),
                    to =  TOPIC
                ).also{
                    sendNotification(it)
                }
            }
        }
    }
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch{
        try{
            val response = RetrofitInstance.api.postNotification(notification = notification)
            if(response.isSuccessful){
                Log.d("### MainAct","Response : ${Gson().toJson(response)}")
            }else{
                Log.d("### MainAct",response.errorBody().toString())
            }
        }catch(e: Exception){
            Log.d("### MainAct",e.toString())
        }
    }
}
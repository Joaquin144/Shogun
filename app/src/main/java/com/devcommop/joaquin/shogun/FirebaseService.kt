package com.devcommop.joaquin.shogun

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"
class FirebaseService : FirebaseMessagingService(){

    override fun onMessageReceived(message: RemoteMessage) {
        //jab bhi mere phone ke subscribed topics ka notifcation ayega tab yeh function call hoga
        super.onMessageReceived(message)
        //todo : since message mil chuka hai humko toh ab hum uska notifcaiton bana kar show karnege
        val intent = Intent(this,MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()//we want to generate unique ids otherwise older notifcations would be overwritten

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)//MainActivity ko top of stack lane ke liye baki sabko destroy kar do
        val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)//FLAG_ONE_SHOT mane sirf ek baar prayog hoga iska uske baad nahi
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])//FB se Map<String,String> ke roop me data milega humko
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_baseline_check_24)
            .setAutoCancel(true)//notifcaiont will cancel when user will click in its panel
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId,notification)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager:NotificationManager){
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply{
            description = "MY Channel Description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}
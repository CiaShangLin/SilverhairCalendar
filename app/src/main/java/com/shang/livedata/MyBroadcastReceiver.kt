package com.shang.livedata

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.shang.livedata.Main.MainActivity

class MyBroadcastReceiver:BroadcastReceiver (){
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            "time"->{
                /*val notificationHelper = NotificationHelper(context!!)
                val nb = notificationHelper.getChannelNotification("","")
                notificationHelper.getManager().notify(1, nb.build())*/
                var intent=Intent(context,MainActivity::class.java).apply {
                    this.action="com.shang.livedata"
                }
                context?.startActivity(intent)
            }
        }
    }
}
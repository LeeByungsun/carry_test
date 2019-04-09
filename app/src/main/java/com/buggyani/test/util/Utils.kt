package com.buggyani.test.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.buggyani.test.MainActivity
import com.buggyani.test.R
import com.buggyani.test.util.GlobalStatic.NOTI_MESSAGE
import java.text.SimpleDateFormat

/**
 * Created by bslee on 2019-02-16.
 */

class Utils(context: Context) {

    init {
        mContext = context
    }

    companion object {
        lateinit var mContext: Context
        private val TAG = Utils::class.java.simpleName

        fun getData(year: Int, month: Int, day: Int): String {
            var inDate = String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", day)
            Log.e(TAG, "date = $inDate")
            val inSdf = SimpleDateFormat("yyyyMMdd")
            val outSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            val date = inSdf.parse(inDate)
            Log.e(TAG, "date = ${date.toString()}")
            val formattedDate = outSdf.format(date)
            return formattedDate.toString().replace("+", "%2b")// +만 encoding되지 않고 : 도 encoding 되어 개별변경
        }

        /**
         * 노티피케이션 사용
         */
        fun NotificationSomethings(context: Context, title: String? = null, msg: String, channelId: String? = null) {
            Log.e(TAG, "NotificationSomethings : $title    $msg")
            val notificationIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(context, channelId!!)
            builder.setContentIntent(pendingIntent)
            builder.setContentTitle(title)
            builder.setContentText(msg)
            builder.setTicker(msg)
            builder.setWhen(System.currentTimeMillis())
            builder.setSmallIcon(R.mipmap.ic_launcher_round)
            builder.setDefaults(Notification.DEFAULT_LIGHTS)
            builder.setAutoCancel(true)
            nm.notify(NOTI_MESSAGE, builder.build())
        }
    }
}

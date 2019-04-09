package com.buggyani.test.fcm


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import com.buggyani.test.R

/**
 * Created by bslee on 2019-02-16.
 */

object NotificationManager {

    private val GROUP_TEST = "testGroup"

    private val smallIcon: Int
        get() = android.R.drawable.stat_notify_chat

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createChannel(context: Context) {

        val group1 = NotificationChannelGroup(GROUP_TEST, GROUP_TEST)
        getManager(context).createNotificationChannelGroup(group1)

        val channelMessage = NotificationChannel(Channel.MESSAGE,
                context.getString(R.string.notification_channel_message_title), android.app.NotificationManager.IMPORTANCE_DEFAULT)
        channelMessage.description = context.getString(R.string.notification_channel_message_description)
        channelMessage.group = GROUP_TEST
        channelMessage.lightColor = Color.GREEN
        channelMessage.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getManager(context).createNotificationChannel(channelMessage)

        val channelComment = NotificationChannel(Channel.COMMENT,
                context.getString(R.string.notification_channel_comment_title), android.app.NotificationManager.IMPORTANCE_DEFAULT)
        channelComment.description = context.getString(R.string.notification_channel_comment_description)
        channelComment.group = GROUP_TEST
        channelComment.lightColor = Color.BLUE
        channelComment.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        getManager(context).createNotificationChannel(channelComment)

        val channelNotice = NotificationChannel(Channel.NOTICE,
                context.getString(R.string.notification_channel_notice_title), android.app.NotificationManager.IMPORTANCE_HIGH)
        channelNotice.description = context.getString(R.string.notification_channel_notice_description)
        channelNotice.group = GROUP_TEST
        channelNotice.lightColor = Color.RED
        channelNotice.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        getManager(context).createNotificationChannel(channelNotice)
    }

    private fun getManager(context: Context): android.app.NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun deleteChannel(context: Context, @Channel channel: String) {
        getManager(context).deleteNotificationChannel(channel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun sendNotification(context: Context, id: Int, @Channel channel: String, title: String, body: String) {
        val builder = Notification.Builder(context, channel)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(smallIcon)
                .setAutoCancel(true)
        getManager(context).notify(id, builder.build())
    }

    annotation class Channel {
        companion object {
            val MESSAGE = "message"
            val COMMENT = "comment"
            val NOTICE = "notice"
        }
    }

}

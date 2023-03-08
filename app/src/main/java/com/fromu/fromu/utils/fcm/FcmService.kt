package com.fromu.fromu.utils.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.R
import com.fromu.fromu.model.AppRunningState
import com.fromu.fromu.ui.SplashActivity
import com.fromu.fromu.utils.Logger
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FcmService : FirebaseMessagingService() {
    private var notificationId = 0

    companion object {
        val CHANNEL_ID = "CHN_ID"
    }

    init {
        val token: Task<String> = FirebaseMessaging.getInstance().token
        token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Logger.d("FCM_SERVICE", task.result)
            }
        }

        notificationId = System.currentTimeMillis().toInt()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Logger.d("FCM_SERVICE", token)

        FromUApplication.prefManager.setFcmId(token)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("rak", "메세지 왔다")


        val title: String = message.notification?.title.toString()
        val body: String = message.notification?.body.toString()

        Logger.e("rak", "${title}, ${body}")


        if (message.data.isNotEmpty())
            showNotification(message.data["title"], message.data["body"])
    }

    private fun showNotification(title: String?, message: String?, imgUri: String? = null) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

        }

        //TODO 추후 앱이 켜져 있을 때는 splash가 아닌 다른 곳으로 이동하도록 처리
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        val notificationManager = NotificationManagerCompat.from(applicationContext)

        val notificationChannel = NotificationChannel(CHANNEL_ID, "CHM_NAME", NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.setSound(uri, null)
        notificationManager.createNotificationChannel(notificationChannel)

        if (FromUApplication.RUNNING_FLAG == AppRunningState.NOT_RUNNING) { //앱이 꺼져 있는 경우
            notificationManager.notify(notificationId, setAppNotRunning(title, message).build())
        } else { //앱이 켜져 있는 경우
            notificationManager.notify(notificationId, setAppNotRunning(title, message).build())
        }
    }

    private fun setAppNotRunning(title: String?, message: String?): NotificationCompat.Builder {
        val intent = Intent(this, SplashActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_NO_CREATE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title) //타이틀
            .setContentText(message) // 콘텐츠
            .setVibrate(longArrayOf(1000, 1000, 1000)) //진동
            .setAutoCancel(true) //알람 클릭하면 알람이 자동으로 삭제 되도록 설정
            .setOnlyAlertOnce(true) //알림이 한 번만 울리게 설정
            .setContentIntent(pendingIntent)
    }
}
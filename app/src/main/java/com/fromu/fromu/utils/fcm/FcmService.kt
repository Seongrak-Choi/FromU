package com.fromu.fromu.utils.fcm

import com.fromu.fromu.utils.Logger
import com.fromu.fromu.utils.PrefManager
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {
    private var notificationId = 0

    @Inject
    lateinit var prefManager: PrefManager

    init {
        val token: Task<String> = FirebaseMessaging.getInstance().token
        token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Logger.d("FCM_SERVICE", task.result)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Logger.d("FCM_SERVICE", token)

        prefManager.setFcmId(token)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

    }
}
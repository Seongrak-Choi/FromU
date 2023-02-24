package com.fromu.fromu.fcm

import com.fromu.fromu.utils.PrefManager
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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
                Timber.tag("FCM_SERVICE").d(task.result)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Timber.tag("FCM_SERVICE").d(token)

        prefManager.setFcmId(token)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

    }
}
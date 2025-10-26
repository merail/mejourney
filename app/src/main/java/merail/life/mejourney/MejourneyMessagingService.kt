package merail.life.mejourney

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.EntryPointAccessors
import merail.life.mejourney.di.MessagingServiceDependencies

class MejourneyMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MejourneyMessagingService"
    }

    private val logger by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            MessagingServiceDependencies::class.java,
        ).logger()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            logger.d(TAG, "Push notification was received: ${message.data}")
            sendNotification(
                messageTitle = it.title.orEmpty(),
                messageBody = it.body.orEmpty(),
                messageData = message.data,
            )
        }
    }

    override fun onNewToken(token: String) {
        logger.d(TAG, "Refreshed token: $token")
    }

    private fun sendNotification(
        messageTitle: String,
        messageBody: String,
        messageData: Map<String, String>,
    ) {
        val requestCode = 0
        val intent = Intent(this, MainActivity::class.java).apply {
            action = (System.currentTimeMillis().toString())
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            messageData.forEach { (key, value) ->
                putExtra(key, value)
            }
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(merail.life.design.R.drawable.ic_notification)
            .setColor(getColor(merail.life.design.R.color.ocean))
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            channelId,
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        notificationManager.createNotificationChannel(channel)

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
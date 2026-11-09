package pt.demo.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotifyWorker(private val ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val texto = when (inputData.getString("tipo")) {
            "calculo" -> "7 × 7 = ${7 * 7}"
            else -> "Reunião às 15h"
        }

        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel("demo", "Demo", NotificationManager.IMPORTANCE_DEFAULT)
        )

        val notificacao = NotificationCompat.Builder(ctx, "demo")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Aviso")
            .setContentText(texto)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notificacao)
        return Result.success()
    }
}

package pt.demo.notif;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotifyWorker extends Worker {

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context ctx = getApplicationContext();

        String tipo = getInputData().getString("tipo");
        String texto = "calculo".equals(tipo) ? "7 × 7 = " + (7 * 7) : "Reunião às 15h";

        NotificationManager manager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                    new NotificationChannel("demo", "Demo", NotificationManager.IMPORTANCE_DEFAULT));
        }

        Notification notificacao = new NotificationCompat.Builder(ctx, "demo")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Aviso")
                .setContentText(texto)
                .setAutoCancel(true)
                .build();

        manager.notify((int) System.currentTimeMillis(), notificacao);
        return Result.success();
    }
}

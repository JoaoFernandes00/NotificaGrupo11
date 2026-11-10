package pt.demo.notif;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.concurrent.TimeUnit;

@CapacitorPlugin(name = "BackgroundTask")
public class BackgroundTaskPlugin extends Plugin {

    @PluginMethod
    public void schedule(PluginCall call) {
        String tipo = call.getString("tipo", "lembrete");
        long segundos = call.getInt("segundos", 5).longValue();

        OneTimeWorkRequest pedido = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(segundos, TimeUnit.SECONDS)
                .setInputData(new Data.Builder().putString("tipo", tipo).build())
                .build();

        WorkManager.getInstance(getContext()).enqueue(pedido);
        call.resolve();
    }
}
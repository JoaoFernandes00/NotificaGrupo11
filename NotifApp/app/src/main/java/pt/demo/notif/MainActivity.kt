package pt.demo.notif

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        val titulo = TextView(this).apply {
            text = "Notificações e Tarefas em Background"
            textSize = 20f
            gravity = Gravity.CENTER
        }

        val botaoLocal = Button(this).apply {
            text = "Agendar notificação local (10s)"
            setOnClickListener { agendar("lembrete", 10) }
        }

        val botaoBackground = Button(this).apply {
            text = "Executar tarefa em background (7 × 7)"
            setOnClickListener { agendar("calculo", 5) }
        }

        val aviso = TextView(this).apply {
            text = "Depois de carregar, coloque a app em segundo plano."
            gravity = Gravity.CENTER
        }

        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(48, 48, 48, 48)
            addView(titulo)
            addView(botaoLocal)
            addView(botaoBackground)
            addView(aviso)
        })
    }

    private fun agendar(tipo: String, segundos: Long) {
        val pedido = OneTimeWorkRequestBuilder<NotifyWorker>()
            .setInitialDelay(segundos, TimeUnit.SECONDS)
            .setInputData(workDataOf("tipo" to tipo))
            .build()
        WorkManager.getInstance(this).enqueue(pedido)
        Toast.makeText(this, "Agendado", Toast.LENGTH_SHORT).show()
    }
}

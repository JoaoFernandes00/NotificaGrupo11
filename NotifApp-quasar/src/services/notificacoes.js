import { Capacitor, registerPlugin } from '@capacitor/core'
import { LocalNotifications } from '@capacitor/local-notifications'

// Plugin nativo (Android) que usa o WorkManager - ver native-android/
const BackgroundTask = registerPlugin('BackgroundTask')

const nativo = () => Capacitor.isNativePlatform()

export async function pedirPermissao () {
  if (nativo()) {
    const atual = await LocalNotifications.checkPermissions()
    if (atual.display === 'granted') return true
    const pedido = await LocalNotifications.requestPermissions()
    return pedido.display === 'granted'
  }

  // Fallback para desenvolvimento no browser
  if (!('Notification' in window)) return false
  if (Notification.permission === 'granted') return true
  return (await Notification.requestPermission()) === 'granted'
}

function notificarNoBrowser (texto, segundos) {
  setTimeout(() => new Notification('Aviso', { body: texto }), segundos * 1000)
}

/** Notificação local agendada (Capacitor Local Notifications) */
export async function agendarLocal (segundos = 10) {
  const texto = 'Reunião às 15h'

  if (!nativo()) {
    notificarNoBrowser(texto, segundos)
    return
  }

  await LocalNotifications.schedule({
    notifications: [
      {
        id: Math.floor(Math.random() * 2147483647),
        title: 'Aviso',
        body: texto,
        schedule: {
          at: new Date(Date.now() + segundos * 1000),
          allowWhileIdle: true
        }
      }
    ]
  })
}

/** Tarefa em background (WorkManager) que calcula 7 × 7 e notifica */
export async function agendarBackground (segundos = 5) {
  if (!nativo()) {
    // No browser não existe background real: apenas simula
    notificarNoBrowser(`7 × 7 = ${7 * 7}`, segundos)
    return
  }

  await BackgroundTask.schedule({ tipo: 'calculo', segundos })
}

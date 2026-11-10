// Prepara o projeto Android do Capacitor e injeta o código nativo (WorkManager).
// Uso: npm run setup:android   (é idempotente, pode correr-se várias vezes)
import { execSync } from 'node:child_process'
import { existsSync, mkdirSync, copyFileSync, readFileSync, writeFileSync } from 'node:fs'
import { dirname, join, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'

const raiz = resolve(dirname(fileURLToPath(import.meta.url)), '..')
const cap = join(raiz, 'src-capacitor')
const android = join(cap, 'android')
const run = (cmd) => execSync(cmd, { cwd: cap, stdio: 'inherit', shell: true })

console.log('> npm install (src-capacitor)')
run('npm install')

// O "cap add" precisa que a pasta www exista e tenha um index.html
mkdirSync(join(cap, 'www'), { recursive: true })
if (!existsSync(join(cap, 'www', 'index.html'))) {
  writeFileSync(join(cap, 'www', 'index.html'), '<!doctype html><title>placeholder</title>')
}

if (!existsSync(android)) {
  console.log('> cap add android')
  run('npx cap add android')
}

// 1. Copiar ficheiros nativos
const destino = join(android, 'app/src/main/java/pt/demo/notif')
mkdirSync(destino, { recursive: true })
for (const f of ['MainActivity.java', 'BackgroundTaskPlugin.java', 'NotifyWorker.java']) {
  copyFileSync(join(raiz, 'native-android', f), join(destino, f))
  console.log(`> copiado ${f}`)
}

// 2. Dependência do WorkManager
const gradlePath = join(android, 'app/build.gradle')
let gradle = readFileSync(gradlePath, 'utf8')
if (!gradle.includes('androidx.work:work-runtime')) {
  gradle = gradle.replace(
    /dependencies\s*\{/,
    'dependencies {\n    implementation "androidx.work:work-runtime:2.9.1"'
  )
  writeFileSync(gradlePath, gradle)
  console.log('> adicionado WorkManager ao app/build.gradle')
}

// 3. Permissão de notificações (Android 13+)
const manifestPath = join(android, 'app/src/main/AndroidManifest.xml')
let manifest = readFileSync(manifestPath, 'utf8')
if (!manifest.includes('POST_NOTIFICATIONS')) {
  manifest = manifest.replace(
    '</manifest>',
    '    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />\n</manifest>'
  )
  writeFileSync(manifestPath, manifest)
  console.log('> adicionada permissão POST_NOTIFICATIONS')
}

console.log('\nPronto. Agora: npm run build:android')

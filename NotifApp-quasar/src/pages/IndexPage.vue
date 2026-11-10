<template>
  <q-page class="column items-center justify-center q-pa-lg q-gutter-md text-center">
    <div class="text-h6">Notificações e Tarefas em Background</div>

    <q-btn
      color="primary"
      no-caps
      label="Agendar notificação local (10s)"
      @click="local"
    />

    <q-btn
      color="secondary"
      no-caps
      label="Executar tarefa em background (7 × 7)"
      @click="background"
    />

    <div class="text-caption text-grey-8">
      Depois de carregar, coloque a app em segundo plano.
    </div>
  </q-page>
</template>

<script setup>
import { onMounted } from 'vue'
import { useQuasar } from 'quasar'
import { pedirPermissao, agendarLocal, agendarBackground } from '@/services/notificacoes'
defineOptions({ name: 'IndexPage' })

const $q = useQuasar()

async function executar (acao) {
  try {
    if (!(await pedirPermissao())) {
      $q.notify({ type: 'negative', message: 'Permissão de notificações negada' })
      return
    }
    await acao()
    $q.notify({ type: 'positive', message: 'Agendado', timeout: 1500 })
  } catch (e) {
    $q.notify({ type: 'negative', message: `Erro: ${e.message ?? e}` })
  }
}

const local = () => executar(() => agendarLocal(10))
const background = () => executar(() => agendarBackground(5))

// Pede a permissão logo ao abrir (como na app original)
onMounted(() => { pedirPermissao().catch(() => {}) })
</script>

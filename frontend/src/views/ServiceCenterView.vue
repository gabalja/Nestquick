<script setup>
import PageNav from '../components/serviceCenter/item/PageNav.vue'

import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const remountKey = ref(0)

//라우터 전환마다 PageNav remount
router.beforeEach(() => {
  remountKey.value += 1
})
</script>

<template>
  <div class="container-fluid">
    <div class="row pnav">
      <PageNav :key="remountKey" />
    </div>
    <div class="route">
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
.container-fluid {
  height: 92vh;
}

.pnav {
  position: fixed;
  width: 100%; /* 수평으로 확장 */
  position: relative;
  justify-content: center;
}

.route {
  display: flex;
  flex-direction: column;
}
</style>

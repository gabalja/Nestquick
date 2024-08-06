<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { detail } from '../../api/sale'
const { VITE_NESTQUICK_API_URL } = import.meta.env

const route = useRoute()
const router = useRouter()

// route를 통해 params으로 넘어온 값을 사용한다
const { sale_no } = route.params

// 매물 정보 가져오기
const sale = ref({})

onMounted(() => {
  getSale()
})

const getSale = () => {
  console.log('getsale start')
  detail(sale_no, ({ data }) => {
    sale.value = data
  }),
    (error) => {
      console.log(error)
    }
  console.log('getsale end')
}
</script>

<template>
  <div class="main mt-5 mx-auto d-flex justify-content-between">
    <div class="justify-content-center">
      <img class="m-5" :src="`${VITE_NESTQUICK_API_URL}/image${sale.image}`"/>
    </div>
    <aside>
      <v-banner sticky="true" class="justify-content-center mt-5 mr-5 shadow">
        <v-banner-text class="text-center">
          <div>매물번호 : {{ sale.sale_no }}</div>
          <div>{{ sale.sale_type }} {{ sale.price }}</div>
          <div>위치 : {{ sale.address }}</div>
          <div>관리비 : {{ sale.maintenance_cost }}</div>
          <div>{{ sale.seller_id }}</div>
          <button>중개인정보보기</button>
          <button>북마크</button>
          <button>공유하기</button>
        </v-banner-text>
      </v-banner>
    </aside>
  </div>
</template>

<style scoped>
.main {
  /* background-color: beige; */
  width: 90%;
  min-width: 1000px;
}

aside {
  width: 400px;
  height: 1500px;
}
.v-banner {
  height: 500px;
  width: 100%;
  background-color: transparent;
}
.v-banner button {
  width: 100%;
  background-color: red;
}

.v-banner-text {
  width: 300px;
}

img {
  width: 80%;
}
</style>

<script setup>
import { ref } from 'vue';
import { useSaleStore } from "../../stores/sale";
import { useRouter } from "vue-router";

const saleStore = useSaleStore()
const router = useRouter()

const title = ref('')
const content = ref('')

async function regist() {
  console.log(title.value +" "+ content.value)
  await saleStore.regist(title.value, content.value)

  router.push('/sale')
}

</script>

<template>

등록

  <v-form class="login-form">

    <v-container>
        <v-row><v-text-field label="제목" v-model="title"/></v-row>
        <v-row><v-text-field label="내용" v-model="content"/></v-row>
        <v-row><v-btn @click="regist">등록</v-btn></v-row>
    </v-container>

  </v-form>
</template>

<style scoped></style>
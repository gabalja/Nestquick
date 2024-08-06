<script setup>
import { storeToRefs } from 'pinia'
import { ref, onMounted } from 'vue'

import { useDealStore } from '@/stores/deal'
const dealStore = useDealStore()
const { recentAptCode } = storeToRefs(dealStore)

import { useSaleStore } from '@/stores/sale'
const saleStore = useSaleStore()
const { bookmarks } = storeToRefs(saleStore)

import { aptinfo, addBookmark, deleteBookmark } from '@/api/deal'

const recentAptInfo = ref([])
onMounted(() => {
  recentAptCode.value.forEach(element => {
    aptinfo(
      (element),
      ({ data }) => {
        recentAptInfo.value.push(data)
      }, (err) => {
        console.log(err)
      }
    )
  });
})

const clickBookmark = (aptCode) => {
  // 북마크 리스트에 없을때는 add
  // 있을때는 delete
  if (!bookmarks.value.includes(aptCode)) {
    addBookmark(
      { code: aptCode },
      () => {
        bookmarks.value.push(aptCode)
      },
      (err) => {
        console.log(err)
      }
    )
  } else {
    deleteBookmark(
      { code: aptCode },
      () => {
        bookmarks.value = bookmarks.value.filter((bmark) => bmark != aptCode)
      },
      (err) => {
        console.log(err)
      }
    )
  }
}
</script>

<template>
  <div class="d-flex align-items-center px-5">
    <v-row align="center" justify="start">
      <v-col v-for="info in recentAptInfo" :key="info.aptCode" cols="auto">
        <v-card class="mx-auto" width="300" hover>
          <v-card-item>
            <div>
              <div class="text-overline mb-1">
                {{ info.dongCode }}
              </div>
              <div class="text-h6 mb-1">
                {{ info.apartmentName }}
              </div>
              <div class="text-caption">주소 : {{ info.dong }} {{ info.jibun }}</div>
              <div class="text-caption">건축년도 : {{ info.buildYear }} </div>
            </div>
          </v-card-item>

          <v-card-actions>
            <button v-show="bookmarks.includes(info.aptCode)" @click="clickBookmark(info.aptCode)">
              💙
            </button>
            <button v-show="!bookmarks.includes(info.aptCode)" @click="clickBookmark(info.aptCode)">
              ➕
            </button>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

  </div>
</template>

<style scoped>
.card {
  max-width: 250px;
  margin: 0;
  padding: 0;
}

.card-text {
  margin: 0;
}

.card-img-top {
  max-width: 200px;
  height: 15rem;
  object-fit: cover;
}
</style>
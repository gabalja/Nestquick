<script setup>
import { ref } from 'vue'
import { useSaleStore } from '@/stores/sale'
import { storeToRefs } from 'pinia'
import { addBookmark, deleteBookmark } from '@/api/deal'

const saleStore = useSaleStore()
const { aptimgurl, bookmarks } = storeToRefs(saleStore)
defineProps({
  aptSaleInfo: Object
})

const clickHeart = (aptCode) => {
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
  <div class="row shadow-sm">
    <div class="col-5" style="position: relative">
      <div
        v-if="bookmarks.includes(aptSaleInfo.apt_id)"
        @click="clickHeart(aptSaleInfo.apt_id)"
        style="position: absolute; top: 0.3em; left: 0.3em"
      >
        💛
      </div>
      <img :src="`${aptimgurl[aptSaleInfo.image]}`" />
    </div>
    <div class="col-7 ps-2 align-self-center">
      <h5 class="fw-bold">{{ aptSaleInfo.sale_type }} {{ aptSaleInfo.price }}</h5>
      <div>{{ aptSaleInfo.build_type }} · {{ aptSaleInfo.title }}</div>

      <div class="desc">
        {{ aptSaleInfo.floor }}층 · {{ aptSaleInfo.area }}㎡ · 관리비
        {{ aptSaleInfo.maintenance_cost }}원
      </div>
      <div class="desc text-truncate">{{ aptSaleInfo.description }}</div>
    </div>
  </div>
</template>

<style scoped>
.desc {
  font-weight: lighter;
  font-size: small;
}
.row {
  margin: 1em 0.3em 1em 0.5em;
  height: 160px;
  background-color: rgba(255, 255, 255, 0.575);
  border: 1px solid #355b8c3d;
  border-radius: 0.3em;
  padding: 1em;
}

.row:hover {
  background-color: rgba(255, 255, 255, 0.87);
  border: 1px solid #f2cb0596;
}

.row > div {
  padding: 0;
}
img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>

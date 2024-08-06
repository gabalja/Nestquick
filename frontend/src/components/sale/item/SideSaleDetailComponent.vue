<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { useSaleStore } from '@/stores/sale'
import { useDealStore } from '@/stores/deal'
import { storeToRefs } from 'pinia'
import { addBookmark, deleteBookmark } from '@/api/deal'

const props = defineProps({ saleDetail: Object })

const saleStore = useSaleStore()
const { optionList, aptimgurl, bookmarks } = storeToRefs(saleStore)

const userStore = useUserStore()
const { isLogin } = storeToRefs(userStore)

const dealStore = useDealStore()
const { recentAptCode } = storeToRefs(dealStore)

// 창이 열렸을 때 최근 본 매물 저장
onMounted(() => {
  if(!recentAptCode.value.includes(props.saleDetail.apt_id))
    recentAptCode.value.push(props.saleDetail.apt_id)
})
// 매물 정보가 바뀌었을 때도 추가
watch(
  () => props.saleDetail,
  (val) => {
    if(!recentAptCode.value.includes(val.apt_id))
      recentAptCode.value.push(val.apt_id)
  },
  // 객체나 배열에 있는 내용이 바뀔 때는 배열 안에 있는 내용이 바뀌니까
  // 깊은 감시!
  { deep: true }
)

// 북마크 버튼
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
  <!-- <div>{{ saleDetail }}</div> -->
  <div class="d-flex justify-content-center">
    <h3 class="fw-bold me-2">{{ saleDetail.title }}</h3>
    <!-- 로그인한 경우에만 북마크 버튼 표시 -->
    <template v-if="isLogin">
      <button
        v-show="bookmarks.includes(saleDetail.apt_id)"
        @click="clickBookmark(saleDetail.apt_id)"
      >
        💙
      </button>
      <button
        v-show="!bookmarks.includes(saleDetail.apt_id)"
        @click="clickBookmark(saleDetail.apt_id)"
      >
        ➕
      </button>
    </template>
  </div>
  <div class="mainimg"><img :src="`${aptimgurl[saleDetail.image]}`" /></div>
  <h4 class="my-3">{{ saleDetail.sale_type }} {{ saleDetail.price }}</h4>
  <div class="title">
    <div class="text-start">정보</div>
    <div>
      <div class="row mx-5">
        <div class="col">✔️ {{ saleDetail.build_type }}</div>
        <div class="col">✔️ {{ saleDetail.area }}㎡</div>
        <div class="w-100"></div>
        <div class="col">✔️ {{ saleDetail.floor }}층</div>
        <div class="col">✔️ 관리비 {{ saleDetail.maintenance_cost }}원</div>
      </div>
      <div class="info-small">
        <div>위치 : {{ saleDetail.address }}</div>
        <div>{{ saleDetail.seller_id }}</div>
        <div>{{ saleDetail.regist_date }}</div>
      </div>
    </div>
  </div>
  <div class="title">
    <div class="text-start">옵션</div>
    <div class="row justify-content-start mx-3">
      <template v-for="(option, index) in saleDetail.options" :key="index">
        <div class="col-4">
          <!-- 해당되는 아이콘 URL 하고 싶은데... -->
          <!-- <div><img width="70" :src="`${aptimgurl[saleDetail.image]}`" /></div> -->
          <div>💙 {{ option }}</div>
        </div>
        <div class="w-100" v-if="index % 3 == 2"></div>
      </template>
    </div>
  </div>
  <div class="title">
    <div class="text-start">설명</div>
    <div class="mx-5">{{ saleDetail.description }}</div>
  </div>
</template>

<style scoped>
.mainimg {
  height: 300px;
}
.mainimg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.title {
  margin-left: 1em;
}
.title > div:first-child {
  font-weight: bold;
  font-size: large;
}

.title > div:nth-child(2) div {
  margin: 0.2em 0;
  /* background-color: rebeccapurple; */
}

.info-small {
  font-weight: lighter;
  font-size: small;
}
</style>

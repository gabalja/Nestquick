<script setup>
import { useUserStore } from '@/stores/user'
import { useSaleStore } from '@/stores/sale'
import { useDealStore } from '@/stores/deal'
import { storeToRefs } from 'pinia'
import { addBookmark, deleteBookmark } from '@/api/deal'
import { ref, watch, onMounted, computed } from 'vue'
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale
} from 'chart.js'

const userStore = useUserStore()
const saleStore = useSaleStore()
const dealStore = useDealStore()
const { bookmarks } = storeToRefs(saleStore)
const { isLogin } = storeToRefs(userStore)
const { recentAptCode } = storeToRefs(dealStore)

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  ArcElement,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale
)

const props = defineProps({
  aptDealList: Object,
  aptInfo: Object
})

// 창이 열렸을 때 최근 본 매물 저장
onMounted(() => {
  if(!recentAptCode.value.includes(props.aptInfo.aptCode))
    recentAptCode.value.push(props.aptInfo.aptCode)
})
// 아파트 정보가 바뀌었을 떄도 저장
watch(
  () => props.aptInfo,
  (val) => {
    if(!recentAptCode.value.includes(val.aptCode))
      recentAptCode.value.push(val.aptCode)
  },
  // 객체나 배열에 있는 내용이 바뀔 때는 배열 안에 있는 내용이 바뀌니까
  // 깊은 감시!
  { deep: true }
)
const value = ref([2015, 2022])

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

// 원본 데이터
// 필터링을 위해 객체 타입 배열 데이터로 변경
const dateAmount = ref([])
// 필터링한 데이터
const filteredDateAmount = ref([])

watch(
  () => props.aptDealList,
  (val) => {
    // 이때 그래프를 그리는 labels과 datasets의 data가 바껴야
    let yaOptions = []

    val.forEach((amount) => {
      // 객체 타입으로 묶어서 넣기
      yaOptions.push({
        year: amount.dealYear,
        month: amount.dealMonth,
        // // dealAmount가 문자열 타입이라 , 기준으로 split 해서 앞부분으로만 데이터로 사용
        amount: amount.dealAmount.split(',')[0]
      })
    })
    dateAmount.value = yaOptions
    // 현재 선택되어있는 년도 value 유지
    filteredDateAmount.value = dateAmount.value.filter(
      (item) => item.year >= value.value[0] && item.year <= value.value[1]
    )
  },
  // 객체나 배열에 있는 내용이 바뀔 때는 배열 안에 있는 내용이 바뀌니까
  // 깊은 감시!
  { deep: true }
)

watch(
  () => value,  // 슬라이더 값이 바뀌면
  (val) => {
    filteredDateAmount.value = dateAmount.value.filter( // 년도로 필터링해서 목록 재구성
      (item) => item.year >= val.value[0] && item.year <= val.value[1]
    )
  },
  // 객체나 배열에 있는 내용이 바뀔 때는 배열 안에 있는 내용이 바뀌니까
  // 깊은 감시!
  { deep: true }
)

// 그래프 데이터 및 옵션
// ref변수로 만들어서 반응형으로 동작할 수 있도록!
const chartData = computed(function () {
  return {
    // labels: datedata.value,
    // map 함수를 이용해 yearmonth만 배열로 만듦
    labels: filteredDateAmount.value.map((item) => item.year + '.' + item.month),
    datasets: [
      {
        label: '실거래가',
        // ponintStyle: 'star',
        pointRadius: 1,
        borderWidth: 1,
        borderColor: '#F2CB05',
        backgroundColor: '#F2CB05',
        // map 함수를 이용해 amount만 배열로 만듦
        data: filteredDateAmount.value.map((item) => item.amount),
        // 선 둥글게
        tension: 0.3
      }
    ]
  }
})

// 그래프 옵션
const chartOptions = {
  responsive: true,
  maintainAspectRatio: false
}

// computed와 filter
</script>

<template>
  <div>
    <Line id="my-chart-id" :options="chartOptions" :data="chartData" />
  </div>
  <div>
    <h2 class="mt-3">{{ aptInfo.apartmentName }}</h2>
    <!-- 로그인한 경우에만 북마크 버튼 표시 -->
    <template v-if="isLogin">
      <button v-show="bookmarks.includes(aptInfo.aptCode)" @click="clickBookmark(aptInfo.aptCode)">
        💙
      </button>
      <button v-show="!bookmarks.includes(aptInfo.aptCode)" @click="clickBookmark(aptInfo.aptCode)">
        ➕
      </button>
    </template>
  </div>
  <div>{{ aptInfo.dong }} {{ aptInfo.jibun }}</div>
  <!-- 슬라이더를 옮기면 value 값이 바뀜 바뀌면 computed? -->

  <v-range-slider
    class="mt-2 px-5"
    v-model="value"
    step="1"
    max="2022"
    min="2015"
    :thumb-label="true"
  ></v-range-slider>

  <table class="table table-hover">
    <thead>
      <tr class="text-center">
        <th scope="col">거래년</th>
        <th scope="col">거래월</th>
        <th scope="col">거래일</th>
        <th scope="col">거래금액</th>
        <th scope="col">층</th>
        <th scope="col">전용면적</th>
      </tr>
    </thead>
    <tbody>
      <tr class="text-center" v-for="aptDeal in aptDealList" :key="aptDeal.no">
        <th>{{ aptDeal.dealYear }}</th>
        <th>{{ aptDeal.dealMonth }}</th>
        <th>{{ aptDeal.dealDay }}</th>
        <th>{{ aptDeal.dealAmount }}</th>
        <th>{{ aptDeal.floor }}</th>
        <th>{{ aptDeal.area }}</th>
      </tr>
    </tbody>
  </table>
</template>

<style scoped>
canvas {
  height: 300px;
  background-color: #ffffffd8;
  padding: 20px;
  border-radius: 0.2em;
  border: 1px solid rgba(128, 128, 128, 0.158);
}
</style>

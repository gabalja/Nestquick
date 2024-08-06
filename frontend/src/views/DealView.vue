<script setup>
import { ref, onMounted, watch } from 'vue'
import { aptListFromDB, aptDealListFromDB } from '@/api/deal'
import { listSido, listGugun, listDong } from '@/api/map'

import KakaoMap from '@/components/common/KakaoMap.vue'
import VSelect from '@/components/common/SearchFilterComponent.vue'
import SideAptDealComponent from '@/components/deal/SideAptDealComponent.vue'

import { useSaleStore } from '@/stores/sale'
import { storeToRefs } from 'pinia'
const saleStore = useSaleStore()
const { bookmarks } = storeToRefs(saleStore)

const sidoList = ref([])
const gugunList = ref([{ text: '구군선택', value: '' }])
const dongList = ref([{ text: '동선택', value: '' }])
const originAptInfoList = ref([])
const aptInfoList = ref([])
// 왼쪽 아파트 목록에서 선택한 아파트
const selectApt = ref({})

// 자식 컴포넌트에서 넘어온, 마커를 통해 선택된 아파트
const selectedApt = ref({})
const aptDealList = ref([])

const selectfilter = ref('')

const aptName = ref('')

const filter = ['restaurant', 'market', 'medical', 'edu', 'leisure']

onMounted(() => {
  getSidoList()
})

const getSidoList = () => {
  listSido(
    ({ data }) => {
      let options = []
      options.push({ text: '시도선택', value: '' })
      data.forEach((sido) => {
        options.push({ text: sido.sidoName, value: sido.sidoCode })
      })
      sidoList.value = options
    },
    (err) => {
      console.log(err)
    }
  )
}

const onChangeSido = (val) => {
  listGugun(
    { sido: val },
    ({ data }) => {
      let options = []
      options.push({ text: '구군선택', value: '' })
      data.forEach((gugun) => {
        options.push({ text: gugun.gugunName, value: gugun.gugunCode })
      })
      gugunList.value = options
    },
    (err) => {
      console.log(err)
    }
  )
}

const onChangeGugun = (val) => {
  listDong(
    { gugun: val },
    ({ data }) => {
      let options = []
      options.push({ text: '동선택', value: '' })
      data.forEach((dong) => {
        options.push({ text: dong.dongName, value: dong.dongCode })
      })
      dongList.value = options
    },
    (err) => {
      console.log(err)
    }
  )
}

const onChangeDong = (val) => {
  // 아파트 목록이 없는 경우 => 안내 문구 띄움
  dealToggle.value = false
  getAptList(val)
}

const getAptList = (val) => {
  aptListFromDB(
    { code: val },
    ({ data }) => {
      originAptInfoList.value = data
      aptInfoList.value = data
    },
    (err) => {
      console.log(err)
    }
  )
}

// aptName이 바뀔때마다 aptInfoList 목록 변경
watch(
  () => aptName.value,
  (val) => {
    aptInfoList.value = originAptInfoList.value.filter((item) => item.apartmentName.includes(val))
  },
  // 객체나 배열에 있는 내용이 바뀔 때는 배열 안에 있는 내용이 바뀌니까
  // 깊은 감시!
  { deep: true }
)

const viewApt = (apt) => {
  selectApt.value = apt
  selectedApt.value = apt
}

// 카카오맵(자식컴포넌트)에서 넘어온 값 val
const selectAptInfo = function (val) {
  selectedApt.value = val
}

const dealToggle = ref(false)

// selectedApt가 바뀔때 마다 해당 아파트 deal 정보 가져오기
watch(
  () => selectedApt.value,
  (val) => {
    // 요청 보내기
    dealToggle.value = true
    aptDealListFromDB(
      { code: val.aptCode },
      ({ data }) => {
        aptDealList.value = data
      },
      (err) => {
        console.log(err)
      }
    )
  },
  // 객체나 배열에 있는 내용이 바뀔 때는 배열 안에 있는 내용이 바뀌니까
  // 깊은 감시!
  { deep: true }
)
const selectFilter = (val) => {
  selectfilter.value = val
}
</script>

<template>
  <div class="container-fluid text-center mt-3 dealDiv" style="position: relative">
    <div class="row mx-5 mb-3 align-items-end selectInput">
      <div class="col">
        <VSelect :selectOption="sidoList" @onFilterSelect="onChangeSido" />
      </div>
      <div class="col">
        <VSelect :selectOption="gugunList" @onFilterSelect="onChangeGugun" />
      </div>
      <div class="col">
        <VSelect :selectOption="dongList" @onFilterSelect="onChangeDong" />
      </div>
      <div class="col">
        <div class="input-group">
          <input type="text" class="form-control" placeholder="아파트 이름" v-model="aptName" />
        </div>
      </div>
    </div>
    <div class="category">
      <ul>
        <li>
          <span
            ><img
              @click="selectFilter('restaurant')"
              src="@/assets/img/restaurant1.png"
              width="35"
              alt="음식점"
          /></span>
          음식점
        </li>
        <li>
          <span
            ><img
              @click="selectFilter('market')"
              src="@/assets/img/market1.png"
              width="35"
              alt="상점"
          /></span>
          상점
        </li>
        <li>
          <span
            ><img @click="selectFilter('edu')" src="@/assets/img/edu1.png" width="35" alt="교육"
          /></span>
          교육
        </li>
        <li>
          <span
            ><img
              @click="selectFilter('medical')"
              src="@/assets/img/medical1.png"
              width="35"
              alt="의료"
          /></span>
          의료
        </li>
        <li>
          <span
            ><img
              @click="selectFilter('leisure')"
              src="@/assets/img/leisure1.png"
              width="35"
              alt="취미"
          /></span>
          취미
        </li>
      </ul>
    </div>
    <KakaoMap
      :aptList="aptInfoList"
      :selectApt="selectApt"
      @select-apt-code="selectAptInfo"
      :selectfilter="selectfilter"
    />

    <div class="row d-flex justify-content-between" style="height: 0px">
      <div class="aptList">
        <table class="w-100">
          <thead>
            <tr class="text-center">
              <th scope="col">아파트명</th>
              <th scope="col">행정동</th>
            </tr>
          </thead>
          <tbody>
            <tr
              class="text-center"
              v-for="apt in aptInfoList"
              :key="apt.aptCode"
              @click="viewApt(apt)"
              :class="{ marked: bookmarks.includes(apt.aptCode) }"
            >
              <th>{{ apt.apartmentName }}</th>
              <td>{{ apt.dong }}</td>
            </tr>
          </tbody>
        </table>
        <div v-show="!aptInfoList.length">검색 결과가 없습니다!</div>
      </div>
      <!-- aptDealList의 길이로 판단하면 목록 가져오기 전에 렌더링이 안되기 때문에 따로 변수를 두고 토글 -->
      <div class="aptDeal" v-if="dealToggle">
        <SideAptDealComponent :apt-deal-list="aptDealList" :apt-info="selectedApt" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.selectInput {
  position: absolute;
  top: 27px;
  left: 260px;
  z-index: 17;
  padding: 0.7em;
  background-color: rgba(255, 255, 255, 0.25);
}

.dealDiv {
  position: relative;
  min-width: 90vw;
}

tr {
  height: 3em;
}

thead {
  background-color: #ffffff;
}

tbody {
  background-color: #ffffffbe;
}

tbody > tr {
  border-top: 1px solid rgba(128, 128, 128, 0.418);
}

.marked {
  font-weight: bold;
  color: #355b8c;
}

.marked:hover {
  font-weight: bold;
  background-color: #f2cb054d;
  color: #000;
}

.category,
.category * {
  margin: 0;
  padding: 0;
  color: #000;
}

.category {
  position: absolute;
  overflow: hidden;
  top: 30px;
  left: 40px;
  width: 252px;
  height: 60px;
  z-index: 10;
  border: 1px solid black;
  font-family: 'Malgun Gothic', '맑은 고딕', sans-serif;
  font-size: 12px;
  text-align: center;
  background-color: #fff;
}

.category .menu_selected {
  background: #ff5f4a;
  color: #fff;
  border-left: 1px solid #915b2f;
  border-right: 1px solid #915b2f;
  margin: 0 -1px;
}

.category li {
  list-style: none;
  float: left;
  width: 50px;
  height: 45px;
  padding-top: 5px;
  cursor: pointer;
}

.category .ico_comm {
  display: block;
  margin: 0 auto 2px;
  width: 22px;
  height: 26px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/category.png') no-repeat;
}

.aptList {
  position: relative;
  overflow: hidden;
  top: -79vh;
  left: 40px;
  z-index: 10;
  width: 400px;
  overflow-y: scroll;
  background-color: rgba(255, 255, 255, 0.7);
  height: 75vh;
  padding: 1.5em;
}

.aptDeal {
  padding: 30px;
  position: relative;
  overflow: hidden;
  top: -87vh;
  left: -40px;
  height: 83vh;
  width: 500px;
  z-index: 10;
  overflow-y: scroll;
  background-color: rgba(255, 255, 255, 0.7);
}

/* 아래의 모든 코드는 영역::코드로 사용 */
.aptList::-webkit-scrollbar,
.aptDeal::-webkit-scrollbar {
  width: 10px;
  /* 스크롤바의 너비 */
}

.aptList::-webkit-scrollbar-thumb,
.aptDeal::-webkit-scrollbar-thumb {
  height: 10%;
  /* 스크롤바의 길이 */
  background: #2179f459;
  /* 스크롤바의 색상 */
  border-radius: 10px;
}

.aptList::-webkit-scrollbar-thumb:hover,
.aptDeal::-webkit-scrollbar-thumb:hover {
  height: 30%;
  /* 스크롤바의 길이 */
  background: #217af4;
  /* 스크롤바의 색상 */

  border-radius: 10px;
}

.aptList::-webkit-scrollbar-track,
.aptDeal::-webkit-scrollbar-track {
  background: #f2cb050a;
  /*스크롤바 뒷 배경 색상*/
}
</style>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { aptListFromDB } from '@/api/deal'
import { list, detail } from '@/api/sale'
import { listSido, listGugun, listDong } from '@/api/map'

import KakaoMap from '@/components/common/KakaoMap.vue'
import VSelect from '@/components/common/SearchFilterComponent.vue'
import SideSaleDetailComponent from '@/components/sale/item/SideSaleDetailComponent.vue'
import SaleIListItem from '@/components/sale/item/SaleIListItem.vue'

const sidoList = ref([])
const gugunList = ref([{ text: '구군선택', value: '' }])
const dongList = ref([{ text: '동선택', value: '' }])
// 동에 해당되는 아파트 리스트를 가져와 마커를 찍는다
const aptInfoList = ref([])
// 자식 컴포넌트로 보낼 변수값(마커 이동)
const selectApt = ref({})

// 원본 매물 목록
const origAptSaleList = ref([])
// 필터링 된 매물 목록
const aptSaleList = ref([])

// 매물 상세 영역 토글 변수
const saleToggle = ref(false)

// input에 입력된 아파트 이름
const aptName = ref('')

// 매물 상세 정보
const saleDetail = ref({})

const sale_type = ref([])

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
  // 아파트 목록이 없는 경우 => 안내 문구
  getAptList(val)
}

// 아파트 목록을 가져왔으면
// 해당 아파트 코드로 매물 검색해서 가져오기
const getAptList = (val) => {
  aptListFromDB(
    { code: val },
    ({ data }) => {
      aptInfoList.value = data
    },
    (err) => {
      console.log(err)
    }
  )
}

// aptInfoList이 바뀌면 새로운 매물 목록 요청
watch(
  () => aptInfoList.value,
  (val) => {
    saleToggle.value = false
    origAptSaleList.value = []
    // 필터링 조건들 초기화
    sale_type.value = []
    aptName.value = ''
    val.forEach((apt) => {
      list({ aptCode: apt.aptCode }, ({ data }) => {
        
        data.forEach((sale) => {
          // sale 속성 추가해서 넘기기
          sale.lat = apt.lat
          sale.lng = apt.lng
          
          origAptSaleList.value.push(sale)
        })
      })
    })
    aptSaleList.value = origAptSaleList.value
  },
  // 객체나 배열에 있는 내용이 바뀔 때는 배열 안에 있는 내용이 바뀌니까
  // 깊은 감시!
  { deep: true }
)

// aptName이 바뀔때마다 aptInfoList 목록 변경
watch(
  () => aptName.value,
  (val) => {
    saleToggle.value = false
    aptSaleList.value = origAptSaleList.value.filter((item) => item.title.includes(val))
  },
  // 객체나 배열에 있는 내용이 바뀔 때는 배열 안에 있는 내용이 바뀌니까
  // 깊은 감시!
  { deep: true }
)

// 마커 이동을 위한 함수
const viewApt = (sale) => {
  selectApt.value = sale
  // 매물 상세 정보를 가져오자
  saleToggle.value = true
  detail(sale.sale_no, ({ data }) => {
    
    saleDetail.value = data
  })
}

// 카카오맵(자식컴포넌트)에서 넘어온 값 val
const selectAptInfo = function (val) {
  
  aptName.value = val.apartmentName
}

watch(
  () => sale_type.value,
  (val) => {
    aptName.value = ''
    if (sale_type.value.length)
      aptSaleList.value = origAptSaleList.value.filter(
        (item) => val.includes(item.sale_type) // && item.title.includes(aptName.value)
      )
    else aptSaleList.value = origAptSaleList.value //.filter((item) => item.title.includes(val))
  }
)
</script>

<template>
  <div class="container-fluid text-center mt-3 saleDiv">
    <div class="row mx-5 mb-3 align-items-end inputSelect">
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
        <input type="text" class="form-control" placeholder="아파트 이름" v-model="aptName" />
      </div>
    </div>
    <div class="category">
      <div class="saletypeFilter d-flex justify-content-around">
        <input type="checkbox" v-model="sale_type" value="전세" name="charter" id="charter" />
        <label for="charter">전세</label>
        <input type="checkbox" v-model="sale_type" value="월세" name="monthly" id="monthly" />
        <label for="monthly">월세</label>
        <input type="checkbox" v-model="sale_type" value="매매" name="purchase" id="purchase" />
        <label for="purchase">매매</label>
      </div>
    </div>
    <KakaoMap :aptList="aptInfoList" :selectApt="selectApt" @select-apt-code="selectAptInfo" />

    <div class="row d-flex justify-content-between" style="height: 0px">
      <div class="aptList">
        <!-- 사진, 찜한 아파트 여부, 거래타입, 아파트이름, 층, 면적, 관리비, 설명 -->
        <div v-for="sale in aptSaleList" :key="sale.sale_no">
          <SaleIListItem :apt-sale-info="sale" @click="viewApt(sale)" />
        </div>
        <div v-show="!aptSaleList.length">검색 결과가 없습니다!</div>
      </div>
      <div class="aptSale" v-if="saleToggle">
        <!-- <div class="aptSale"> -->
        <!-- 매물의 상세 정보를 볼 수 있는 컴포넌트 만들기 -->
        <SideSaleDetailComponent :saleDetail="saleDetail" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.inputSelect {
  position: absolute;
  top: 27px;
  left: 260px;
  z-index: 17;
  padding: 0.7em;
  background-color: rgba(255, 255, 255, 0.25);
}

.saleDiv {
  position: relative;
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
  /* border: 1px solid black; */
  font-family: 'Malgun Gothic', '맑은 고딕', sans-serif;
  font-size: 12px;
  text-align: center;
  background-color: #ffffffd2;
}

.saletypeFilter {
  margin-top: 1.7em;
}

.saletypeFilter {
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

.aptSale {
  padding: 30px;
  position: relative;
  overflow: hidden;
  top: -87vh;
  left: -40px;
  height: 83vh;
  width: 500px;
  z-index: 10;
  overflow-y: scroll;
  background-color: rgba(255, 255, 255, 0.9);
  /* border-radius: 3%; */
}

/* 아래의 모든 코드는 영역::코드로 사용 */
.aptList::-webkit-scrollbar,
.aptSale::-webkit-scrollbar {
  width: 10px;
  /* 스크롤바의 너비 */
}

.aptList::-webkit-scrollbar-thumb,
.aptSale::-webkit-scrollbar-thumb {
  height: 10%;
  /* 스크롤바의 길이 */
  background: #2179f459;
  /* 스크롤바의 색상 */
  border-radius: 10px;
}

.aptList::-webkit-scrollbar-thumb:hover,
.aptSale::-webkit-scrollbar-thumb:hover {
  height: 30%;
  /* 스크롤바의 길이 */
  background: #217af4;
  /* 스크롤바의 색상 */

  border-radius: 10px;
}

.aptList::-webkit-scrollbar-track,
.aptSale::-webkit-scrollbar-track {
  background: #f2cb051e;
  /*스크롤바 뒷 배경 색상*/
}
</style>

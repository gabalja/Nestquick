<script setup>
import { ref, watch, onMounted } from 'vue'
import { shoplist } from '@/api/store'
import Loading from './LoadingComponent.vue'

const emit = defineEmits(['selectAptCode'])

var map
const positions = ref([])
const markers = ref([])
const overlays = ref([])
const storeMarkers = ref([])

const loadingStatus = ref(false)

// aptList에 위도경도 말고 다른 정보도 넘어오고있음
const props = defineProps({ aptList: Array, selectApt: Object, selectfilter: String })

// 필터 변경 시
watch(
  () => props.selectfilter,
  (filter) => {
    //console.log(map.getCenter()) //위도, 경도
    loadingStatus.value = true
    // 요청 보내서 마커 찍기
    shoplist(
      { filter: filter, lat: map.getCenter().Ma, lon: map.getCenter().La },
      ({ data }) => {
        // 넘어온 데이터로 마커 구성해서 찍기
        positions.value = []
        data.forEach((apt) => {
          let obj = {}
          obj.latlng = new kakao.maps.LatLng(apt.lat, apt.lon)
          obj.title = apt.name
          obj.aptcode = apt

          positions.value.push(obj)
        })
        loadStoreMarkers(storeMarkers)
        loadingStatus.value = false
      },
      (err) => {
        console.log(err)
      }
    )
  },
  { deep: true }
)

//지도를 옮겼을 때에도...마커가 변경되어야할 거 같은데

// 얘가 마커 선택 시 지도 중심 이동시키는 코드
watch(
  () => props.selectApt.value,
  () => {
    // 이동할 위도 경도 위치를 생성합니다
    var moveLatLon = new kakao.maps.LatLng(props.selectApt.lat, props.selectApt.lng)

    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon)
  },
  { deep: true }
)

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap()
  } else {
    const script = document.createElement('script')
    // autoload=false 꼭!
    // kakao 객체는 아래 스크립트가 로드될 때 생성된다
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${
      import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
    }&libraries=services,clusterer`
    /* global kakao */
    script.onload = () => kakao.maps.load(() => initMap())
    document.head.appendChild(script)
  }
})
// 이거랑 initmap까지 하고 돌아가는지 꼭 확인하기

// 마커를 뿌릴 props의 statios가 바뀔때마다 마커 로드
watch(
  () => props.aptList.value,
  () => {
    positions.value = []
    props.aptList.forEach((apt) => {
      let obj = {}
      obj.latlng = new kakao.maps.LatLng(apt.lat, apt.lng)
      obj.title = apt.apartmentName
      obj.aptcode = apt

      positions.value.push(obj)
    })
    // 새로운 아파트 리스트를 불러왔으면 필터 마커 지움
    deleteMarkers(storeMarkers)
    // 새로운 아파트 마커 로드
    if (props.aptList.length > 0) loadMarkers(markers)
    else deleteMarkers(markers)
  },
  // 객체나 배열에 있는 내용이 바뀔 때는 배열 안에 있는 내용이 바뀌니까
  // 깊은 감시!
  { deep: true }
)

const initMap = () => {
  const container = document.getElementById('map')
  const options = {
    center: new kakao.maps.LatLng(33.450701, 126.570667),
    level: 3
  }
  map = new kakao.maps.Map(container, options)
  kakao.maps.event.addListener(map, 'center_changed', function () {
    //console.log('중심 바뀜')
    deleteMarkers(overlays)
  })

  // loadMarkers();
}

const loadMarkers = (markers) => {
  // 현재 표시되어있는 marker들이 있다면 map에 등록된 marker를 제거한다.
  deleteMarkers(markers)
  // 마커를 생성합니다
  markers.value = []
  positions.value.forEach((position) => {
    const marker = new kakao.maps.Marker({
      map: map, // 마커를 표시할 지도
      position: position.latlng, // 마커를 표시할 위치
      title: position.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됨.
      clickable: true // // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
      // image: markerImage // 마커의 이미지
    })

    // add 클릭 이벤트 하면 마커에 이벤트 설정

    // 마커를 생성하면서 클릭 이벤트도 등록하는군!
    kakao.maps.event.addListener(marker, 'click', function () {
      deleteMarkers(overlays)
      // 마커를 눌렀을 때, 부모 컴포넌트로 선택한 아파트 코드 전달
      emit('selectAptCode', position.aptcode)
    })

    markers.value.push(marker)
  })

  // 4. 지도를 이동시켜주기
  // 배열.reduce( (누적값, 현재값, 인덱스, 요소)=>{ return 결과값}, 초기값);
  const bounds = positions.value.reduce(
    (bounds, position) => bounds.extend(position.latlng),
    new kakao.maps.LatLngBounds()
  )

  // 마커가 한 화면에 다 나오도록 level을 자동 설정한다
  map.setBounds(bounds)
}

// // 커스텀 오버레이를 닫기 위해 호출되는 함수입니다
// function closeOverlay() {
//   overlay.setMap(null);
// }

// 필터마커 로드 함수
const loadStoreMarkers = (markers) => {
  // 현재 표시되어있는 marker들이 있다면 map에 등록된 marker를 제거한다.
  deleteMarkers(markers)
  deleteMarkers(overlays)

  // 마커 이미지를 생성합니다
  const imgSrc = '/src/assets/img/marker/' + props.selectfilter + '.png'
  // 마커 이미지의 이미지 크기 입니다
  const imgSize = new kakao.maps.Size(35, 35)
  const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize)

  // 마커를 생성합니다
  markers.value = []
  positions.value.forEach((position) => {
    const marker = new kakao.maps.Marker({
      map: map, // 마커를 표시할 지도
      position: position.latlng, // 마커를 표시할 위치
      title: position.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됨.
      clickable: true, // // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
      image: markerImage // 마커의 이미지
    })

    // add 클릭 이벤트 하면 마커에 이벤트 설정

    // 마커를 생성하면서 클릭 이벤트도 등록하는군!
    // 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
    kakao.maps.event.addListener(marker, 'click', function () {
      deleteMarkers(overlays)
      // marker.get객체키() 하면 값 가져와짐
      var content =
        '<div class="wrap">' +
        '    <div class="info">' +
        '        <div class="title">' +
        marker.getTitle() +
        '        </div>' +
        '        <div class="body">' +
        '            <div class="desc">' +
        '                <div class="jibun ellipsis">' +
        position.aptcode.indsSclsNm +
        '</div>' +
        '                <div class="ellipsis">' +
        position.aptcode.rdnmAdr +
        '</div>' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '</div>'

      // 마커 위에 커스텀오버레이를 표시합니다
      // 마커를 중심으로 커스텀 오버레이를 표시하기위해 CSS를 이용해 위치를 설정했습니다
      var overlay = new kakao.maps.CustomOverlay({
        content: content,
        map: map,
        position: marker.getPosition()
      })
      overlays.value.push(overlay)
      overlay.setMap(map)
    })

    markers.value.push(marker)
  })

  // // 4. 지도를 이동시켜주기
  // // 배열.reduce( (누적값, 현재값, 인덱스, 요소)=>{ return 결과값}, 초기값);
  // const bounds = positions.value.reduce(
  //   (bounds, position) => bounds.extend(position.latlng),
  //   new kakao.maps.LatLngBounds()
  // )

  // // 마커가 한 화면에 다 나오도록 level을 자동 설정한다
  // map.setBounds(bounds)
}

const deleteMarkers = (markers) => {
  if (markers.value.length > 0) {
    markers.value.forEach((marker) => marker.setMap(null))
  }
}
</script>

<template>
  <div id="loading" v-show="loadingStatus">
    <Loading />
  </div>
  <div id="map"></div>
</template>

<style>
#loading {
  position: absolute;
  z-index: 20;
  width: 100vw;
  height: 100vh;
  background-color: #5085bb4b;
}

#loading > div {
  position: relative;
  left: 45vw;
  top: 40vh;
}

#map {
  width: 100%;
  height: 90vh;
}

.wrap {
  position: absolute;
  left: 10px;
  bottom: 30px;
  width: 288px;
  /* 오버레이 네모부분 높이 */
  height: 112px;
  margin-left: -144px;
  text-align: left;
  overflow: hidden;
  font-size: 12px;
  font-family: 'Malgun Gothic', dotum, '돋움', sans-serif;
  line-height: 1.5;
}

.wrap * {
  padding: 0;
  margin: 0;
}

.wrap .info {
  width: 286px;
  height: 100px;
  border-radius: 5px;
  border-bottom: 2px solid #ccc;
  border-right: 1px solid #ccc;
  overflow: hidden;
  background: #fff;
}

.wrap .info:nth-child(1) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}

.info .title {
  padding: 5px 0 0 10px;
  height: 30px;
  background: #eee;
  border-bottom: 1px solid #ddd;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

/* .info .close {
  position: absolute;
  top: 10px;
  right: 10px;
  color: #888;
  width: 17px;
  height: 17px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/overlay_close.png');
} */
/* .info .close:hover {
  cursor: pointer;
} */
.info .body {
  position: relative;
  overflow: hidden;
  text-align: center;
}

.info .desc {
  position: relative;
  margin: 13px 0 0 0;
  height: 50px;
}

.desc .ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.desc .jibun {
  font-size: 11px;
  color: #888;
  margin-top: -2px;
}

/* .info .img {
  position: absolute;
  top: 6px;
  left: 5px;
  width: 73px;
  height: 71px;
  border: 1px solid #ddd;
  color: #888;
  overflow: hidden;
} */
.info:after {
  content: '';
  position: absolute;
  margin-left: -12px;
  left: 50%;
  bottom: 0;
  width: 22px;
  height: 12px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png');
}

.info .link {
  color: #5085bb;
}
</style>

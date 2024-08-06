<script setup>
import { ref, onMounted } from 'vue'
import { localAxios } from "../../util/http-commons"
import { popularApt } from '@/api/deal'

const local = localAxios()

const topAptsInfo = ref([])
const announcementList = ref([])

const month = new Date().getMonth() + 1

onMounted(() => {
  local.get(`service/announcement/recentList/${month}`).then((response) => { announcementList.value = response.data })

  popularApt({},
    ({ data }) => {
      //console.log(data)
      topAptsInfo.value = data
    },
    (err) => {
      console.log(err)
    }
  )
})
</script>
<template>
  <div class="container d-flex flex-column flex-wrap justify-content-center align-items-center">
    <div class="w-100 text-center mb-4 mainsentence">
      <p>어떤 집을 찾고 계세요?</p>
    </div>
    <div class="w-100 d-flex align-items-center justify-content-center">
      <div class="col-6 d-flex justify-content-center">
        <div class="wrapclass">
          <div class="searchWindow">
            <input type="text" class="searchTerm" placeholder="주소를 입력해주세요" />
            <button type="submit" class="searchButton">
              <img src="@/assets/img/magnifier.png" width="20" height="20" id="mag" />
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="w-100 mt-5 d-flex justify-content-center gap-4">
      <div class="col-4 d-flex ">
        <div class="newsform animate__animated animate__bounceInLeft">
          <p class="newstitle">공지사항</p>
          <table class="newsitem table table-hover">
            <tr class="text-center">
              <th>제목</th>
              <th>공지일</th>
            </tr>
            <tr class="text-center with-border-bottom" v-for="announcement in announcementList"
              :key="announcement.announcementNo">
              <td class="text-start col-8">
                <router-link
                  :to="{ name: 'announcementView', params: { announcementNo: announcement.announcementNo, currentPage: 1 } }">
                  {{ announcement.title }}
                </router-link>
              </td>
              <td>{{ announcement.registDate.slice(5, 10) }}</td>
            </tr>
          </table>
        </div>
      </div>
      <div class=" col-4 d-flex justify-content-center">
        <div class="newsform animate__animated animate__bounceInRight">
          <p class="newstitle">실시간 인기 아파트</p>
          <ul class="newsitem" style="list-style: none; padding-left: 0px">
            <li v-for="info in topAptsInfo" :key="info.aptCode">
              🏢 {{ info.apartmentName }} ( {{ info.dong }} )
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  <div class="carousel-container">
    <Carousel :autoplay="3000" :wrap-around="true">
      <Slide v-for="slide in slides" :key="slide">
        <div class="carousel__item">
          <img class="slideimg" :src="slide" />
        </div>
      </Slide>
    </Carousel>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
import { Carousel, Slide } from 'vue3-carousel'
import city6 from '@/assets/img/city6.jpg'
import city7 from '@/assets/img/city7.jpeg'
import city8 from '@/assets/img/city8.jpg'
import city9 from '@/assets/img/city9.jpg'
import city10 from '@/assets/img/city10.jpg'

export default defineComponent({
  components: {
    Carousel,
    Slide
  },
  data() {
    return {
      slides: [city6, city7, city8, city9, city10]
    }
  }
})
</script>

<style scoped>
.carousel__item {
  filter: brightness(60%);
}

.slideimg {
  max-height: 100vh;
  /* 이미지의 최대 높이를 화면 높이(100vh)로 설정 */
  max-width: 100vw;
  /* 이미지의 최대 너비를 화면 너비에 맞게 자동으로 조절 */
  min-height: 100vh;
  /* 이미지의 최소 높이를 지정 (원하는 크기로 조절) */
  min-width: 100vw;
  /* 이미지의 최소 너비를 지정 (원하는 크기로 조절) */
  padding-left: 0.5rem;
}

.carousel-container {
  position: absolute;
  /* Carousel을 절대적 위치로 설정 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  /* 다른 컨텐츠 위에 나타나도록 z-index 설정 */
  opacity: 0.6;
}

.container {
  height: 92vh;
  min-width: 100%;
  margin: 0;
  /* 기본 마진 제거 */
  padding-top: 0rem;
  /* 기본 패딩 제거 */
  position: relative;
  /* 상대적 위치 설정 */
  z-index: 3;
}

@font-face {
  font-family: 'bm';
  src: url('@/fonts/baemin/BMDOHYEON_ttf.ttf');
}

/*샌드박스 어그로체*/
@font-face {
  font-family: 'SBAggro';
  font-weight: 700;
  font-style: normal;
  src: url('https://cdn.jsdelivr.net/gh/webfontworld/sandbox/SBAggroBold.eot');
  src:
    url('https://cdn.jsdelivr.net/gh/webfontworld/sandbox/SBAggroBold.eot?#iefix') format('embedded-opentype'),
    url('https://cdn.jsdelivr.net/gh/webfontworld/sandbox/SBAggroBold.woff2') format('woff2'),
    url('https://cdn.jsdelivr.net/gh/webfontworld/sandbox/SBAggroBold.woff') format('woff'),
    url('https://cdn.jsdelivr.net/gh/webfontworld/sandbox/SBAggroBold.ttf') format('truetype');
  font-display: swap;
}

.mainsentence {
  font-family: 'SBAggro';
  font-size: 50px;
  /* 원하는 폰트 크기 설정 */
  -webkit-text-stroke: 1px white;
  /* 테두리 두께와 색상 설정 */
  background: #5fc3f3;
  background: linear-gradient(to top, #5fc3f3 0%, #004493 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  /*
  animation: bounce;
  animation-duration: 2s;
  animation-iteration-count: infinite;
  */
}

.newsform {
  width: 500px;
  /* 원하는 너비로 설정 */
  height: 300px;
  /* 원하는 높이로 설정 */
  border: 2px solid slategray;
  background-color: snow;
  border-radius: 20px;
  padding: 1.5rem;
}

.newstitle {
  font-family: 'bm';
  font-size: 25px;
  border-bottom: 2px solid;
}

.newsitem {
  font-family: 'bm';
  font-size: 15px;
}

.searchWindow {
  width: 100%;
  position: relative;
  display: flex;
}

.searchTerm {
  width: 100%;
  border: 3px solid #004493;
  background-color: white;
  text-align: center;
  border-right: none;
  padding: 5px;
  height: 3rem;
  outline: none;
  border-radius: 5px 0 0 5px;
  color: #9dbfaf;
}

.searchTerm:focus {
  color: #004493;
}

.searchButton {
  width: 3rem;
  height: 3rem;
  border: 1px solid #004493;
  background: #004493;
  text-align: center;
  color: #fff;
  border-radius: 0 5px 5px 0;
  cursor: pointer;
  font-size: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.wrapclass {
  width: 80%;
}

#mag {
  filter: invert(100%) sepia(1%) saturate(1130%) hue-rotate(125deg) brightness(109%) contrast(100%);
}

a {
  color: black;
  text-decoration: none;
}

.with-border-bottom {
  border-bottom: 1px solid #ccc;
}

.newsitem .text-start {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>

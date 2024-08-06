<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { detail } from '../../api/sale'

const route = useRoute()
const router = useRouter()

// route를 통해 params으로 넘어온 값을 사용한다
const { sale_no } = route.params

// 매물 정보 가져오기
const sale = ref({})

onMounted(() => {
  getSale()
})

const saletype = ref(['전세', '월세', '매매'])
const buildtype = ref(['원룸', '투·쓰리룸', '오피스텔', '아파트'])
const optionitems = ref([
  '에어컨',
  '옷장',
  '신발장',
  '냉장고',
  '세탁기',
  '전자레인지',
  '싱크대',
  '조리대',
  'TV'
])

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
const url = ref(null)

const changeImage = function () {
  //createObjectURL에 넣어주는 데이터 타입은 Blob, File, MediaSource
  url.value = URL.createObjectURL(sale.value.imageFile[0])
  console.log(url.value)
}
</script>

<template>
  <v-form class="login-form">
    <v-container>
      <v-row>
        <v-col cols="4">
          <v-img id="preview" class="bg-white" width="300" :aspect-ratio="1" :src="url" cover>
          </v-img>
          <v-file-input
            hide-details="auto"
            accept="image/png, image/jpeg, image/bmp"
            placeholder="Pick an avatar"
            label="Avatar"
            variant="underlined"
            v-model="sale.imageFile"
            @change="changeImage"
          >
          </v-file-input>
        </v-col>
        <v-col>
          <v-row>
            <v-col>
              <v-text-field
                hide-details="auto"
                label="건물이름"
                color="#F2CB05"
                variant="solo"
                v-model="sale.title"
              />
            </v-col>
            <v-col>
              <v-select
                hide-details="auto"
                :items="saletype"
                variant="solo"
                label="거래 종류"
                color="#F2CB05"
                v-model="sale.sale_type"
              >
              </v-select>
            </v-col>
            <v-col
              ><v-select
                hide-details="auto"
                :items="buildtype"
                variant="solo"
                label="건물 종류"
                color="#F2CB05"
                v-model="sale.build_type"
              >
              </v-select>
            </v-col>
          </v-row>
          <v-row>
            <v-col
              ><v-text-field
                hide-details="auto"
                label="실거래가"
                variant="solo"
                color="#F2CB05"
                v-model="sale.price"
            /></v-col>
            <v-col
              ><v-text-field
                hide-details="auto"
                label="해당 층"
                color="#F2CB05"
                variant="solo"
                v-model="sale.floor"
            /></v-col>
            <v-col
              ><v-text-field hide-details="auto" label="면적" variant="solo" v-model="sale.area"
            /></v-col>
          </v-row>
          <v-row>
            <v-col
              ><v-text-field
                hide-details="auto"
                label="건축년도"
                color="#F2CB05"
                variant="solo"
                v-model="sale.build_year"
            /></v-col>
            <v-col
              ><v-text-field
                hide-details="auto"
                label="관리비"
                color="#F2CB05"
                variant="solo"
                v-model="sale.maintenance_cost"
            /></v-col>
            <v-col
              ><v-text-field
                hide-details="auto"
                label="설명"
                color="#F2CB05"
                variant="solo"
                v-model="sale.description"
            /></v-col>
          </v-row>
          <v-row>
            <v-select
              v-model="sale.options"
              :items="optionitems"
              chips
              label="옵션"
              color="#F2CB05"
              variant="underlined"
              multiple
            ></v-select>
          </v-row>
        </v-col>
      </v-row>

      <v-row><v-btn @click="registSale">등록</v-btn></v-row>
    </v-container>
  </v-form>
</template>
<style scoped>
.main {
  background-color: beige;
  width: 90%;
  min-width: 1000px;
}

aside {
  width: 350px;
  height: 1500px;
}
.v-banner {
  height: 500px;
  width: 100%;
  background-color: yellowgreen;
}
.v-banner button {
  width: 100%;
  background-color: red;
}

.v-banner-text {
  width: 300px;
}
</style>

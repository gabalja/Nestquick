<script setup>
import { ref } from 'vue'
import { regist } from '@/api/sale'
import { useRouter } from 'vue-router'

//const saleStore = useSaleStore()
const router = useRouter()

const sale = ref({
  apt_id: 11110000000002,
  seller_id: 'ssafy',
  title: '',
  description: '',
  address: '',
  sale_type: '',
  price: 0,
  maintenance_cost: 0,
  build_type: '',
  build_year: 0,
  area: 0.0,
  floor: 0,
  imageFile: null,
  options: []
})

async function registSale() {
  console.log(sale.value)
  await regist(sale.value)

  router.push('/sale')
}

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
//const value = ref(['foo', 'bar', 'fizz', 'buzz'])

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

<style scoped></style>

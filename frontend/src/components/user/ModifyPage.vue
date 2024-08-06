<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from "vue-router";
import { localAxios } from "../../util/http-commons";
import { useUserStore } from '@/stores/user'

import SearchFilterComponent from '@/components/common/SearchFilterComponent.vue'
import PasswordChangeModal from '../user/PwdChange.vue'

const route = useRoute()
const router = useRouter()
const local = localAxios()
const userStore = useUserStore()

const isModalVisible = ref(false)

const openModal = () => {
  isModalVisible.value = true
}

const closeModal = () => {
  isModalVisible.value = false
}

const selectOption = ref([
  { text: "domain", value: "" },
  { text: "naver.com", value: "naver.com" },
  { text: "google.com", value: "google.com" },
  { text: "ssafy.com", value: "ssafy.com" }
])

const changeFilter = (filter) => {
  userDto.value.emailDomain = filter;
}

const { userId } = route.params

const userDto = ref({
  userId: userId,
  userName: '',
  emailId: '',
  emailDomain: '',
  phoneNumber: '',
  img: '',
  imageFile: null
})

const modify = async () => {
  console.log(userDto.value)
  await userStore.userModify(userDto.value)
  router.push({ name: 'userLogin' })
}

const mvMypage = () => {
  if (confirm("회원정보 수정을 취소하시겠습니까? 작성내용은 저장되지 않습니다."))
    router.push({ name: 'myPage' })
}

onMounted(() => {
  local.get(`user/modify/${userId}`).then(({ data }) => {
    userDto.value = data.userInfo
    url.value = VITE_NESTQUICK_API_URL + '/image/' + userDto.value.img
  }).catch((error) => { alert("에러 발생 !!! " + error) })
  //VITE_NESTQUICK_API_URL+'/image/'+userDto.value.img
  console.log(userDto.value.img)
})

const { VITE_NESTQUICK_API_URL } = import.meta.env

// src에 저렇게 요청하면 되는데 img에 값이 안넘어옴...
const url = ref('')
//src="@/assets/img/picture_icon.png"

const changeImage = function (val) {
  // 해당 이벤트를 val이라는 변수에 받아옴
  // 그 안에 들어있는 파일정보
  console.log(val.target.files[0])
  //createObjectURL에 넣어주는 데이터 타입은 Blob, File, MediaSource
  url.value = URL.createObjectURL(val.target.files[0])
  console.log(url.value)
}

</script>

<template>
  <div class="container-fluid d-flex justify-content-center align-items-center">
    <div class="col-6 custom-form shadow rounded-3 py-4">
      <div class="text-center my-4">
        <p class="startword">회원 정보 수정</p>
      </div>
      <div class="d-flex my-3 mx-5">
        <div class="d-flex align-items-center">
          <div class="d-flex flex-column">
            <img :src="url" class="img-fluid rounded-circle border border-primary p-3"
              style="width: 200px; height: 200px" />
            <input type="file" id="file" style="display: none;" @change="changeImage" />
            <label for="file" class="btn btn-outline-primary col-auto mt-2">
              <img src="@/assets/img/picture_icon.png" style="width: 20px; height: 20px; margin-right: 1rem" />프로필 수정
            </label>
          </div>
        </div>
        <form class="d-flex flex-column align-items-center w-75" @submit.prevent="onSubmit">
          <div class="my-3 w-75 d-flex align-items-center inputs">
            <label for="userid" class="form-label col-3">아이디</label>
            <input type="text" class="form-control" id="userid" name="userid" :value="userDto.userId" readonly required />
          </div>
          <div class="my-3 w-75 d-flex align-items-center inputs">
            <label for="username" class="form-label col-3">이름</label>
            <input type="text" class="form-control" id="username" name="username" v-model="userDto.userName" required />

          </div>
          <div class="my-3 w-75 d-flex align-items-center inputs">
            <label for="email" class="form-label col-3">이메일</label>
            <input type="text" class="form-control mr-2" id="email" name="email" v-model="userDto.emailId" required />
            <label for="domain" class="form-label col-1 text-center">@</label>
            <SearchFilterComponent :selectOption="selectOption" @onFilterSelect="changeFilter" />
          </div>
          <div class="my-3 w-75 d-flex align-items-center inputs">
            <label for="tel" class="form-label col-3">전화번호</label>
            <input type="tel" class="form-control" id="tel" name="tel" v-model="userDto.phoneNumber" required />
          </div>
          <div class="my-3 d-flex align-items-center justify-content-end gap-3">
            <button class="btn btn-primary" @click="openModal">비밀번호 변경</button>
            <button class="btn btn-secondary" @click="modify">수정</button>
            <button class="btn btn-primary" @click="mvMypage">취소</button>
          </div>
        </form>
        <PasswordChangeModal :showModal="isModalVisible" :closeModal="closeModal" :userId="userId" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.container-fluid {
  height: 92vh;
  background-color: #f2f2f2;
}

.custom-form {
  background-color: rgb(238, 244, 251);
}

/*notosans 폰트*/
@font-face {
  font-family: 'ntsans-sb';
  src: url('@/fonts/notosans/NotoSansKR-SemiBold.ttf');
}

.startword {
  font-size: 2rem;
  font-family: 'ntsans-sb';
}

.inputs {
  font-size: 20px;
}

.form-label {
  margin: 0;
}
</style>

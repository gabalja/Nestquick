<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { idcheck, signup } from '@/api/user'

import SearchFilterComponent from '@/components/common/SearchFilterComponent.vue'

const router = useRouter()

const selectOption = ref([
  { text: 'domain', value: '' },
  { text: 'naver.com', value: 'naver.com' },
  { text: 'google.com', value: 'google.com' },
  { text: 'ssafy.com', value: 'ssafy.com' }
])

const inputId = ref('')
const inputPwd = ref('')
const inputPwdCheck = ref('')
const inputPhonenumber = ref('')

const isValidId = ref(false)
const isValidPwd = ref(false)
const isValidEmail = ref(false)
const isValidPhonenumber = ref(false)

//id
function idChange() {
  showIdMsg.value = false
  isValidId.value = false
}

const showIdMsg = ref(false)
function idCheck() {
  idcheck(
    inputId.value,
    ({ data }) => {
      if (data) isValidId.value = true
      else showIdMsg.value = true
    },
    (error) => {
      alert('에러 발생 !!! ' + error)
    }
  )
}

//pwd
const showPwdMsg = ref(false)
const pwdPass = ref(false)
const pwdRegex = /^(?=.*[!@#$%^&*(),.?":{}|<>])(?=.*[a-zA-Z])(?=.*\d).{10,20}$/
function pwdCheck() {
  isValidPwd.value = false
  showPwdCheckMsg.value = false
  pwdCheckPass.value = false

  showPwdMsg.value = pwdRegex.test(inputPwd.value) ? false : true
  pwdPass.value = !showPwdMsg.value
}

const showPwdCheckMsg = ref(false)
const pwdCheckPass = ref(false)
function pwdSecondCheck() {
  isValidPwd.value = false
  showPwdCheckMsg.value = false
  pwdCheckPass.value = false

  showPwdCheckMsg.value = inputPwd.value === inputPwdCheck.value ? false : true
  pwdCheckPass.value = !showPwdCheckMsg.value

  if (pwdPass.value && pwdCheckPass.value) isValidPwd.value = true
}

//email
const changeFilter = (filter) => {
  userDto.value.emailDomain = filter
}

//phoneNumber
const phonenumberRegex = /^\d{11}$/
function phonenumberCheck() {
  isValidPhonenumber.value = false

  isValidPhonenumber.value = phonenumberRegex.test(inputPhonenumber.value) ? true : false
}

//회원가입
const userDto = ref({
  userId: '',
  userPwd: '',
  userName: '',
  emailId: '',
  emailDomain: '',
  phoneNumber: ''
})

function signUp() {
  isValidEmail.value =
    userDto.value.emailId !== '' && userDto.value.emailDomain !== '' ? true : false
  userDto.value.userId = inputId.value
  userDto.value.userPwd = inputPwd.value
  userDto.value.phoneNumber = inputPhonenumber.value

  if (isValidId.value && isValidPwd.value && isValidEmail.value && isValidPhonenumber.value) {
    signup(
      userDto.value,
      ({ data }) => {
        alert(data.message)
        router.push({ name: 'userLogin' })
      },
      (error) => {
        alert('에러 발생 !!! ' + error)
      }
    )
  } else {
    alert('입력 내용을 다시 한 번 확인해주세요.')
  }
}
</script>

<template>
  <div class="container-fluid d-flex justify-content-center align-items-center">
    <div class="col-4 custom-form shadow rounded-3 py-4">
      <div class="text-center my-4">
        <p class="startword">회원가입</p>
      </div>
      <form
        class="d-flex flex-column align-items-center justify-content-start"
        @submit.prevent="onSubmit"
      >
        <div class="my-3 w-75 d-flex align-items-center regist_inputs">
          <label for="username" class="form-label col-3">이름</label>
          <input
            type="text"
            class="form-control"
            id="username"
            name="username"
            v-model="userDto.userName"
          />
        </div>

        <div class="my-3 w-75 d-flex align-items-center regist_inputs input-group">
          <label for="userid" class="form-label col-3">아이디</label>
          <input
            type="text"
            class="form-control"
            id="userid"
            name="userid"
            v-model="inputId"
            @change="idChange"
          />
          <button class="btn checkbtn col-auto" @click="idCheck">중복확인</button>
        </div>
        <div class="mx-auto w-75 text-center" v-show="isValidId">
          <span style="font-size: smaller; color: green">사용가능한 아이디입니다.</span>
        </div>
        <div class="mx-auto w-75 text-center" v-show="showIdMsg">
          <span style="font-size: smaller; color: red">이미 존재하는 아이디입니다.</span>
        </div>

        <div class="my-3 w-75 d-flex align-items-center regist_inputs">
          <label for="userpwd" class="form-label col-4">비밀번호</label>
          <input
            type="password"
            class="form-control"
            id="userpwd"
            name="userpwd"
            v-model="inputPwd"
            @change="pwdCheck"
          />
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="40"
            height="40"
            fill="currentColor"
            class="bi bi-check"
            viewBox="0 0 16 16"
            v-show="pwdPass"
          >
            <path
              d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425a.267.267 0 0 1 .02-.022z"
            />
          </svg>
        </div>
        <div class="mx-auto w-75 text-center" v-show="showPwdMsg">
          <span style="font-size: smaller; color: red">영문, 숫자, 특수문자 포함 10~20자</span>
        </div>

        <div class="my-3 w-75 d-flex align-items-center regist_inputs">
          <label for="pwdcheck" class="form-label col-4">비밀번호 확인</label>
          <input
            type="password"
            class="form-control"
            id="pwdcheck"
            name="pwdcheck"
            v-model="inputPwdCheck"
            @change="pwdSecondCheck"
          />
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="40"
            height="40"
            fill="currentColor"
            class="bi bi-check"
            viewBox="0 0 16 16"
            v-show="pwdPass && pwdCheckPass"
          >
            <path
              d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425a.267.267 0 0 1 .02-.022z"
            />
          </svg>
        </div>
        <div class="mx-auto w-75 text-center" v-show="showPwdCheckMsg">
          <span style="font-size: smaller; color: red">비밀번호가 일치하지 않습니다.</span>
        </div>

        <div class="my-3 w-75 d-flex align-items-center regist_inputs">
          <label for="email" class="form-label col-3">이메일</label>
          <input
            type="text"
            class="form-control mr-2"
            id="email"
            name="email"
            v-model="userDto.emailId"
          />
          <label for="domain" class="form-label col-1 text-center">@</label>
          <SearchFilterComponent :selectOption="selectOption" @onFilterSelect="changeFilter" />
        </div>
        <div class="my-3 w-75 d-flex align-items-center regist_inputs">
          <label for="tel" class="form-label col-3">전화번호</label>
          <input
            type="tel"
            class="form-control"
            id="tel"
            name="tel"
            placeholder="-없이 번호만 입력"
            v-model="inputPhonenumber"
            @change="phonenumberCheck"
          />
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="40"
            height="40"
            fill="currentColor"
            class="bi bi-check"
            viewBox="0 0 16 16"
            v-show="isValidPhonenumber"
          >
            <path
              d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425a.267.267 0 0 1 .02-.022z"
            />
          </svg>
        </div>

        <div class="my-3">
          <button class="btn lgbtn" type="submit" @click="signUp">회원가입</button>
        </div>
      </form>
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

.regist_inputs {
  font-size: 20px;
}

.lgbtn {
  padding: 1em;
  width: 200px;
  background: #3e4684;
  color: #ffffff;
  border: none;
  border-radius: 30px;
  font-weight: 600;
}

.checkbtn {
  background: #3ca9e2;
  color: #ffffff;
}

.form-label {
  margin: 0;
}
</style>

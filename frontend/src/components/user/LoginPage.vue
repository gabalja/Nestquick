<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const userDto = ref({
  userId: '',
  userPwd: ''
})

const login = async () => {
  await userStore.userLogin(userDto.value)
  router.push({ name: 'main' })
}

const mvJoin = () => {
  router.push({ name: 'userSignup' })
}
</script>

<template>
  <div class="container-fluid d-flex justify-content-center align-items-center">
    <div class="col-4 custom-form shadow rounded-3 py-4">
      <div class="d-flex justify-content-center">
        <img src="@/assets/img/nestquick_logo_new.png" height="200" />
      </div>
      <div class="text-center my-2">
        <p class="login-start">로그인</p>
      </div>
      <form @submit.prevent="onSubmit" class="d-flex flex-column align-items-center">
        <div class="my-3 w-75 d-flex align-items-center inputs">
          <label for="userid" class="form-label col-3 text-center">아이디</label>
          <input
            type="text"
            class="form-control"
            id="userid"
            name="userid"
            v-model="userDto.userId"
          />
        </div>
        <div class="my-3 w-75 d-flex align-items-center inputs">
          <label for="userpwd" class="form-label col-3 text-center">비밀번호</label>
          <input
            type="password"
            class="form-control"
            id="userpwd"
            name="userpwd"
            v-model="userDto.userPwd"
          />
        </div>
        <button class="btn lgbtn my-3" type="submit" @click="login">로그인</button>
      </form>
      <div class="my-3 d-flex align-items-center justify-content-center gap-3">
        <router-link class="nav-link" :to="{ name: 'idPwdFind' }"
          ><button type="button" class="btn otherbtn">아이디 / 비밀번호 찾기</button></router-link
        >
        <router-link class="nav-link" :to="{ name: 'userSignup' }"
          ><button type="button" class="btn otherbtn" @click="mvJoin">회원가입</button></router-link
        >
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

.inputs {
  font-size: 1.5rem;
}

.login-start {
  font-size: 2.5rem;
  font-family: 'ntsans-sb';
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

.otherbtn {
  border: none;
}
</style>

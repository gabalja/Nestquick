<!--모달 닫고 열기-->
<script setup>
import { ref, defineProps } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const props = defineProps(['showModal', 'closeModal', 'userId']);
const userStore = useUserStore()
const router = useRouter()

const pwdInfo = ref({
  userId: props.userId,
  currentPwd: '',
  newPwd: ''
})
const inputPwdCheck = ref('')

const pwdRegex = /^(?=.*[!@#$%^&*(),.?":{}|<>])(?=.*[a-zA-Z])(?=.*\d).{10,20}$/

const modify = async () => {
  if (pwdRegex.test(pwdInfo.value.newPwd) && inputPwdCheck.value === pwdInfo.value.newPwd) {
    await userStore.pwdModify(pwdInfo.value)
    router.push({ name: 'userLogin' })
  } else alert("입력내용을 확인해주세요.")
}
</script>

<template>
  <div class="modal" v-if="props.showModal">
    <div class="modal-content shadow-lg">
      <div class="pwd-title">비밀번호 변경</div>
      <!--입력값 수정-->
      <div class="pwd-input">
        <div class="pb-2 pwd_label">현재 비밀번호:</div>
        <input type="password" placeholder="현재 비밀번호" class="pwd-in" v-model="pwdInfo.currentPwd" />
      </div>
      <div class="pwd-input">
        <div class="pb-2 pwd_label">새 비밀번호:</div>
        <input type="password" placeholder="새 비밀번호(영문, 숫자, 특수문자 조합 10~20자)" class="pwd-in" v-model="pwdInfo.newPwd"
          @change="pwdCheck" />

      </div>

      <div class="pwd-input">
        <div class="pb-2 pwd_label">비밀번호 확인:</div>
        <input type="password" placeholder="비밀번호 확인" class="pwd-in" v-model="inputPwdCheck" @change="pwdSecondCheck" />
      </div>
      <!--입력 후 상태 저장-->
      <div class="d-flex justify-content-evenly gap-2 mt-1">
        <button class="btn change-btn" @click="modify">변경하기</button>
        <button class="btn close-btn" @click="props.closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>



<style scoped>
/*모달 위치, 배경*/
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

/*모달 스타일*/
.modal-content {
  background-color: rgb(238, 244, 251);
  padding: 2rem;
  width: 30%;
  height: 55%;
}

/*제목용 NotoSansKR-Bold*/
@font-face {
  font-family: 'ntsans-bold';
  src: url('@/fonts/notosans/NotoSansKR-Bold.ttf');
}

/*본문용 NanumSquare_acB*/
@font-face {
  font-family: 'nnsq_acb';
  src: url('@/fonts/nanum/NanumFontSetup_TTF_SQUARE/NanumSquare_acB.ttf');
}

/*제목 설정*/
.pwd-title {
  font-weight: 700;
  font-size: 2rem;
  margin-bottom: 2rem;
  font-family: 'ntsans-bold';
}

/*입력창*/
.pwd-in {
  background-color: lightgray;
  border-radius: 10px;
  font-size: 1rem;
  height: 70%;
  padding-left: 0.5rem;
  width: 100%;
}

/*비밀번호 입력마다 마진 주기*/
.pwd-input {
  margin-bottom: 3rem;
}

/*라벨 폰트 설정*/
.pwd_label {
  font-size: 1.2rem;
  font-family: 'nnsq_acb';
}

/*수정 완료 버튼*/
.change-btn {
  padding: 1em;
  width: 150px;
  background: #0053a1;
  color: #ffffff;
  border: none;
  border-radius: 30px;
  font-weight: 600;
}

/*수정 완료 버튼*/
.close-btn {
  padding: 1em;
  width: 150px;
  background: #e74c3c;
  color: #ffffff;
  border: none;
  border-radius: 30px;
  font-weight: 600;
}
</style>

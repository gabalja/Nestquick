<!--선택창을 쓰기 위함-->
<script setup>
import { ref } from 'vue'
import { localAxios } from '../../util/http-commons'

const local = localAxios()
// Define reactive variables using ref
const findWhat = ref('findId')
const findSource = ref('')

const inputName = ref('')
const inputId = ref('')
const inputEmail = ref('')
const inputTel = ref('')

const findInfo = ref({
  findWhat: '',
  findSource: '',
  firstInput: '',
  secondInput: ''
})

const find = () => {
  console.log(findWhat.value)

  findInfo.value.findWhat = findWhat.value
  findInfo.value.findSource = findSource.value

  if (findWhat.value === 'findId') {
    findInfo.value.firstInput = inputName.value
  } else {
    findInfo.value.firstInput = inputId.value
  }

  if (findSource.value === 'emailFind') {
    findInfo.value.secondInput = inputEmail.value
  } else {
    findInfo.value.secondInput = inputTel.value
  }

  local
    .post('/user/find', findInfo.value)
    .then(({ data }) => {
      if (!data.answer) {
        alert('입력하신 회원정보를 찾을 수 없습니다.')
      } else {
        console.log(data.confirm)
        const userInput = prompt('인증번호를 입력해주세요.')
        if (userInput === data.confirm + '') {
          alert('인증되었습니다. ' + data.answer)
        } else {
          alert('인증번호를 다시 입력해주세요.')
        }
      }
    })
    .catch((error) => {
      alert('An error occurred!!! ' + error)
    })
}
</script>

<template>
  <div class="container-fluid d-flex justify-content-center align-items-center">
    <div class="col-4 custom-form shadow rounded-3 p-4">
      <!--상단 이미지-->
      <div class="d-flex justify-content-center">
        <img src="@/assets/img/nestquick_logo_new.png" height="100" />
      </div>
      <!--제목-->
      <div class="text-center my-4">
        <div class="find-start mb-3">아이디 / 비밀번호 찾기</div>
        <div style="font-size: 1rem">아이디, 비밀번호 중 선택해주세요.</div>
      </div>
      <!--선택 버튼-->
      <div class="d-flex justify-content-center my-3">
        <div class="btn-group" style="width: 80%" role="group">
          <input
            type="radio"
            class="btn-check"
            id="chooseId"
            checked
            v-model="findWhat"
            value="findId"
          />
          <label class="btn btn-outline-primary" for="chooseId">아이디</label>
          <input type="radio" class="btn-check" id="choosePwd" value="findPwd" v-model="findWhat" />
          <label class="btn btn-outline-primary" for="choosePwd">비밀번호</label>
        </div>
      </div>
      <!--아이디 찾기-->
      <div id="findId" v-if="findWhat === 'findId'">
        <div class="d-flex justify-content-center my-2 find-font" style="font-size: 1.3rem">
          아이디를 찾을 방법을 선택해주세요.
        </div>
        <!--라디오 버튼 선택-->
        <div class="d-flex justify-content-center gap-4 my-2 pt-2">
          <!--이메일, 전화번호 중 선택-->
          <div>
            <input type="radio" id="emailFind" value="emailFind" v-model="findSource" />
            <label for="emailFind" class="find-font" style="font-size: 1.1rem">이메일로 찾기</label>
          </div>
          <div>
            <input type="radio" id="phoneFind" value="phoneFind" v-model="findSource" />
            <label for="phoneFind" class="find-font" style="font-size: 1.1rem"
              >전화번호로 찾기</label
            >
          </div>
        </div>
        <!--이메일로 찾기-->
        <div
          id="findEmail"
          v-if="findSource === 'emailFind'"
          class="d-flex flex-column align-items-center"
        >
          <input
            type="text"
            placeholder="이름을 입력해주세요."
            class="border border-3 mt-3"
            style="width: 60%"
            v-model="inputName"
          />

          <input
            type="text"
            placeholder="email을 입력해주세요."
            class="border border-3 mt-3"
            style="width: 60%"
            v-model="inputEmail"
          />
          <button class="btn btn-primary my-3" style="width: 60%" @click="find">확인</button>
        </div>
        <!--전화번호로 찾기-->
        <div
          id="findPhone"
          v-if="findSource === 'phoneFind'"
          class="d-flex flex-column align-items-center"
        >
          <input
            type="text"
            placeholder="이름을 입력해주세요."
            class="border border-3 mt-3"
            style="width: 60%"
            v-model="inputName"
          />
          <input
            type="text"
            placeholder="전화번호를 입력해주세요."
            class="border border-3 mt-3"
            style="width: 60%"
            v-model="inputTel"
          />
          <button class="btn btn-primary my-3" style="width: 60%" @click="find">확인</button>
        </div>
      </div>
      <!--비밀번호 찾기-->
      <div id="findPwd" v-if="findWhat === 'findPwd'">
        <div class="d-flex justify-content-center my-2 find-font" style="font-size: 1.3rem">
          비밀번호를 찾을 방법을 선택해주세요.
        </div>
        <!--라디오 버튼 선택-->
        <div class="d-flex justify-content-center gap-4 my-2 pt-2">
          <!--이메일, 전화번호 중 선택-->
          <div>
            <input type="radio" id="emailFind" value="emailFind" v-model="findSource" />
            <label for="emailFind" class="find-font" style="font-size: 1.1rem">이메일로 찾기</label>
          </div>
          <div>
            <input type="radio" id="phoneFind" value="phoneFind" v-model="findSource" />
            <label for="phoneFind" class="find-font" style="font-size: 1.1rem"
              >전화번호로 찾기</label
            >
          </div>
        </div>
        <!--이메일로 찾기-->
        <div
          id="findEmail"
          v-if="findSource === 'emailFind'"
          class="d-flex flex-column align-items-center"
        >
          <input
            type="text"
            placeholder="ID를 입력해주세요."
            class="border border-3 mt-3"
            style="width: 60%"
            v-model="inputId"
          />

          <input
            type="text"
            placeholder="email을 입력해주세요."
            class="border border-3 mt-3"
            style="width: 60%"
            v-model="inputEmail"
          />
          <button class="btn btn-primary my-3" style="width: 60%" @click="find">확인</button>
        </div>
        <!--전화번호로 찾기-->
        <div
          id="findPhone"
          v-if="findSource === 'phoneFind'"
          class="d-flex flex-column align-items-center"
        >
          <input
            type="text"
            placeholder="ID를 입력해주세요."
            class="border border-3 mt-3"
            style="width: 60%"
            v-model="inputId"
          />
          <input
            type="text"
            placeholder="전화번호를 입력해주세요."
            class="border border-3 mt-3"
            style="width: 60%"
            v-model="inputTel"
          />
          <button class="btn btn-primary my-3" style="width: 60%" @click="find">확인</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/*화면 다 채우고 배경 연회색으로*/
.container-fluid {
  height: 92vh;
  background-color: #f2f2f2;
}

/*로그인 폼 배경색 연파랑으로*/
.custom-form {
  background-color: rgb(238, 244, 251);
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
.find-start {
  font-size: 2rem;
  font-family: 'ntsans-bold';
}

/*본문 폰트 설정*/
.find-font {
  font-family: 'nnsq_acb';
}
</style>

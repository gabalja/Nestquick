<script setup>
import { ref, inject, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from "../../stores/user"

import UserListItem from "./item/userListItem.vue"
import InquiryListItem from "./item/InquiryListItem.vue"
import InterestSaleItem from "./item/InterestSaleItem.vue"
import RecentSaleItem from "./item/RecentSaleItem.vue"


//초기 설정
const local = inject('local')
const router = useRouter()
const userStore = useUserStore()

const userDto = ref({
    userId: '',
    userName: '',
    emailId: '',
    emailDomain: '',
    phoneNumber: '',
    joinDate: '',
    permission: -1,
    img: ''
})

const showList = ref('')

const userCnt = ref(0)
const inquiryCnt = ref(0)

const today = new Date()
const oneday = 24 * 60 * 60 * 1000

function mvModify() {
    router.push({ name: 'userModify', params: { userId: userDto.value.userId } })
}

const withdraw = async (userId) => {
    if (prompt("정말 탈퇴를 희망하시면 \"탈퇴하겠습니다\"를 입력해주세요") === '탈퇴하겠습니다')
        await userStore.withdrawal(userId).then(alert("탈퇴가 완료되었습니다. \n 그동안 nestquick을 이용해주셔서 감사합니다."))
}

onMounted(() => {
    local.get('/user/myPage')
        .then(({ data }) => {
            userDto.value = data.userInfo
            showList.value = userDto.value.permission == 1 ? 'user' : 'interest'
        })
        .catch((error) => {
            alert("에러 발생 !!! " + error)
            router.push({ name: 'main' })
        })

    local.get('/user/myPage/userCnt')
        .then(({ data }) => {
            userCnt.value = data.userCnt
        }).catch((error) => {
            alert("에러 발생 !!! " + error)
        })

    local.get('/user/myPage/inquiryCnt')
        .then(({ data }) => {
            inquiryCnt.value = data.inquiryCnt
        }).catch((error) => {
            alert("에러 발생 !!! " + error)
        })


})
</script> 

<template>
    <div class="container-fluid ">
        <!--위쪽 부분-->
        <div class="d-flex justify-content-center align-items-start pb-3 pt-5" style="height: 300px;">
            <div class="d-flex justify-content-center col-10">
                <!--내 정보-->
                <div class="d-flex justify-content-center shadow-lg p-4" style="margin-right: 5rem">
                    <div class="d-flex mx-5 gap-5">
                        <!--사진 부분-->
                        <div class="d-flex align-items-center">
                            <div class="d-flex flex-column align-items-center">
                                <img src="@/assets/img/city1.jpg" class="img-fluid rounded-circle border border-5"
                                    style="width: 200px; height: 200px" />
                                <span class="mt-1">함께한 시간 : {{ Math.floor((today - new Date(userDto.joinDate))
                                    / oneday) + "일 " + Math.floor((today - new Date(userDto.joinDate))
                                        % oneday / (60 * 60 * 1000)) + "시간" }}</span>
                            </div>
                        </div>
                        <!--정보 보여주기 부분-->
                        <div>
                            <div class="d-flex my-3 align-items-center">
                                <h2>{{ userDto.userId }}</h2>
                                <p class="ms-2">{{ userDto.userName }}</p>
                            </div>
                            <div class="d-flex my-2 align-items-center">
                                <p>이메일&nbsp;:&nbsp;</p>
                                <p>{{ userDto.emailId + '@' + userDto.emailDomain }}</p>
                            </div>
                            <div class="d-flex my-2 align-items-center">
                                <p>전화번호&nbsp;:&nbsp;</p>
                                <p>{{ userDto.phoneNumber.slice(0, 3) + '-' + userDto.phoneNumber.slice(3, 7) + '-' +
                                    userDto.phoneNumber.slice(7, 11) }}</p>
                            </div>
                            <div class="my-3 d-flex align-items-center justify-content-center">
                                <button class="btn btn-primary" type="submit" @click="mvModify">내 정보 수정</button>
                                <button class="btn btn-danger ml-3" type="submit"
                                    @click="withdraw(userDto.userId)">탈퇴</button>
                            </div>
                        </div>
                    </div>
                </div>
                <template v-if="userDto.permission === 1">
                    <div class="d-flex justify-content-start align-items-center gap-5">
                        <div class="d-flex justify-content-center align-items-center rounded-3 estate-form"
                            style="position: relative">
                            <div style="position: absolute; top: -30px; left: -40px">
                                <img src="@/assets/img/people_icon.png" style="width: 100px; height: 100px" />
                            </div>
                            <div>
                                <h1 class="text-center text-info user-font">{{ userCnt }}명</h1>
                                <p class="text-center">회원 수</p>
                            </div>
                        </div>
                        <div class="d-flex justify-content-center align-items-center rounded-3 estate-form"
                            style="position: relative">
                            <div style="position: absolute; top: -30px; left: -30px">
                                <img src="@/assets/img/inquiry.png" style="width: 100px; height: 100px" />
                            </div>
                            <div>
                                <h1 class="text-center text-success user-font">{{ inquiryCnt }}건</h1>
                                <p class="text-center">새로운 문의</p>
                            </div>
                        </div>
                    </div>
                </template>
                <template v-else>
                    <div class="d-flex justify-content-start align-items-center gap-3">
                        <div class="d-flex justify-content-center align-items-center border border-dark rounded-3 people"
                            style="width: 150px; height: 150px">
                            <div>
                                <h1 class="text-center text-info">2건</h1> <!--바인딩 안됨-->
                                <p class="text-center">관심 아파트</p>
                            </div>
                        </div>
                        <div class="d-flex justify-content-center align-items-center border border-dark rounded-3 trade"
                            style="width: 150px; height: 150px">
                            <div>
                                <h1 class="text-center text-success">4건</h1> <!--바인딩 안됨-->
                                <p class="text-center">최근 본 아파트</p>
                            </div>
                        </div>
                    </div>
                </template>
            </div>
        </div>

        <!--아래쪽-->
        <div class="offset-1 mt-3 pt-5">
            <div class="row d-flex justify-content-start" v-if="userDto.permission === 1">
                <div class="col-2 btn-group mb-5 ml-5 justify-content-start" role="group"
                    aria-label="Basic radio toggle button group">
                    <input type="radio" class="btn-check btn-sm" name="btnradio" id="btnradio1" autocomplete="off"
                        @change="showList = 'user'" checked>
                    <label class="btn btn-outline-primary" for="btnradio1">회원 목록</label>

                    <input type="radio" class="btn-check btn-sm" name="btnradio" id="btnradio2" autocomplete="off"
                        @change="showList = 'inquiry'">
                    <label class="btn btn-outline-primary" for="btnradio2">문의 목록</label>
                </div>
            </div>
            <div class="row d-flex justify-content-start" v-else>
                <div class="col-2 btn-group mb-5 ml-5 justify-content-start" role="group"
                    aria-label="Basic radio toggle button group">
                    <input type="radio" class="btn-check" name="btnradio" id="btnradio1" autocomplete="off"
                        @change="showList = 'interest'" checked>
                    <label class="btn btn-outline-primary" for="btnradio1">관심 아파트</label>

                    <input type="radio" class="btn-check" name="btnradio" id="btnradio2" autocomplete="off"
                        @change="showList = 'recent'">
                    <label class="btn btn-outline-primary" for="btnradio2">최근 본 아파트</label>
                </div>
            </div>
            <div v-show="showList === 'user'" class="mb-5 ml-5">
                <UserListItem />
            </div>
            <div v-show="showList === 'inquiry'" class="mb-5 ">
                <InquiryListItem />
            </div>
            <div v-show="showList === 'interest'" class="mb-5 ml-5">
                <InterestSaleItem />
            </div>
            <div v-show="showList === 'recent'" class="mb-5 ml-5">
                <RecentSaleItem />
            </div>
        </div>
    </div>
</template>

<style scoped>
/*화면 다 채우고 배경 연회색으로*/
.container-fluid {
    height: 92vh;
    background-color: #f2f2f2;
    margin: 0;
    padding: 0;
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

/*사용자 이름 설정*/
.user-name {
    font-size: 2rem;
    font-family: 'ntsans-bold';
}

/*본문 폰트 설정*/
.user-font {
    font-family: 'nnsq_acb';
}

/*사진 버튼 디자인*/
.user-picture-btn {
    width: 150px;
    background: #3e4684;
    color: #ffffff;
    border: none;
    border-radius: 30px;
    font-weight: 600;
}

/*정보 수정 버튼 디자인*/
.modify-btn {
    width: 200px;
    background: #4b76c8;
    color: #ffffff;
    border: none;
    border-radius: 30px;
    font-weight: 600;
}

/*관심 매물, 최근 본 매물*/
.estate-form {
    width: 200px;
    height: 200px;
    border: 3px solid lightgray;
}

.card {
    max-width: 250px;
    margin: 0;
    padding: 0;
}

.card-text {
    margin: 0;
}

.card-img-top {
    max-width: 200px;
    height: 15rem;
    object-fit: cover;
}
</style>
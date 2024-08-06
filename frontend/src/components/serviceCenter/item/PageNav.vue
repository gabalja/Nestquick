<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const selectedTab = ref(localStorage.getItem("selectedTab"))

function question() {
    console.log("자주 묻는 질문으로 이동")
    selectedTab.value = "FAQ"
    localStorage.setItem('selectedTab', 'FAQ')
    router.push({ name: 'FAQ' })
}

function inquiry() {
    console.log("1:1 문의로 이동")
    selectedTab.value = "inquiry"
    localStorage.setItem('selectedTab', 'inquiry')
    router.push({ name: 'inquiry' })
}

function announcement() {
    console.log("공지사항으로 이동")
    selectedTab.value = "announcementList"
    localStorage.setItem('selectedTab', 'announcementList')
    router.push({ name: 'announcementList', params: { currentPage: 1 } })
}

watch(selectedTab, (newValue) => {
    console.log('localStorage value changed:', newValue)
})
</script>

<template>
    <div class="text-center mt-5 mb-3">
        <h1>고객센터</h1>
    </div>
    <div class="col-6 btn-group mb-5" role="group" aria-label="Basic radio toggle button group">
        <input type="radio" class="btn-check" name="btnradio" id="btnradio1" autocomplete="off" @click="question"
            :checked="selectedTab === 'FAQ'">
        <label class="btn btn-outline-primary" for="btnradio1">FAQ</label>

        <input type="radio" class="btn-check" name="btnradio" id="btnradio2" autocomplete="off" @click="inquiry"
            :checked="selectedTab === 'inquiry'">
        <label class="btn btn-outline-primary" for="btnradio2">1:1 문의</label>

        <input type="radio" class="btn-check" name="btnradio" id="btnradio3" autocomplete="off" @click="announcement"
            :checked="selectedTab === 'announcementList'">
        <label class="btn btn-outline-primary" for="btnradio3">공지사항</label>
    </div>
</template>

<style scoped></style>
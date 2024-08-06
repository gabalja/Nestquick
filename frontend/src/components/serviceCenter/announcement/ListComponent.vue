<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from "vue-router"
import { listAnnouncement } from "@/api/serviceCenter/announcement.js"
import { useUserStore } from "../../../stores/user"
import { storeToRefs } from 'pinia'

import SearchFilterComponent from "@/components/common/SearchFilterComponent.vue"
import PaginationComponent from "@/components/common/PaginationComponent.vue"

const userStore = useUserStore()
const { userId } = storeToRefs(userStore)

//초기설정
const router = useRouter()
const route = useRoute()

const { VITE_SIZE_PER_PAGE } = import.meta.env;
const currentPage = ref(parseInt(route.params.currentPage))
const totalPageCount = ref(0)

//리스트
const listParameterDto = ref({
    searchFilter: "",
    searchWord: "",
    currentPage: currentPage,
    sizePerPage: VITE_SIZE_PER_PAGE,
    start: 0
})

const announcementList = ref([])

const getAnnouncementList = () => {
    listAnnouncement(listParameterDto.value,
        ({ data }) => {
            announcementList.value = data.announcementList;
            currentPage.value = data.currentPage;
            totalPageCount.value = data.totalPageCount;
            router.push({ name: 'announcementList', params: { currentPage: currentPage.value } })
        },
        (error) => { alert("에러 발생 !!! " + error) }
    )
}

const movePage = (page) => {
    currentPage.value = page;
    listParameterDto.value.currentPage = page;
    listParameterDto.value.start = currentPage.value * VITE_SIZE_PER_PAGE - VITE_SIZE_PER_PAGE
    getAnnouncementList();
}

onMounted(() => {
    listParameterDto.value.currentPage = currentPage.value;
    listParameterDto.value.start = currentPage.value * VITE_SIZE_PER_PAGE - VITE_SIZE_PER_PAGE

    getAnnouncementList();
})

//글쓰기로 이동
const moveToRegist = () => {
    router.push({ name: "announcementRegist" })
}

//검색
const selectOption = ref([
    { text: "검색조건", value: "" },
    { text: "공지번호", value: "announcementNo" },
    { text: "제목", value: "title" },
    { text: "등록일", value: "registDate" }
])

const changeFilter = (filter) => {
    listParameterDto.value.searchFilter = filter;
}
</script>

<template>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="row align-self-center mb-2">
                    <div class="col-md-2 text-start">
                        <button type="button" v-if="userId === 'admin'" class="btn btn-outline-primary btn-sm"
                            @click="moveToRegist">
                            글쓰기
                        </button>
                    </div>
                    <div class="col-md-5 offset-5">
                        <form class="d-flex" @submit.prevent="onSubmit">
                            <div class="col-3">
                                <SearchFilterComponent :selectOption="selectOption" @onFilterSelect="changeFilter" />
                            </div>
                            <div class="input-group input-group-sm ms-1">
                                <input type="text" class="form-control" v-model="listParameterDto.searchWord"
                                    placeholder="검색어 입력" />
                                <button class="btn btn-dark" type="button" @keyup.enter="getAnnouncementList">검색</button>
                            </div>
                        </form>
                    </div>
                </div>
                <table class="table table-hover">
                    <thead class="table-light">
                        <tr class="text-center">
                            <th scope="col">No</th>
                            <th scope="col" class="col-7">제목</th>
                            <th scope=" col">조회수</th>
                            <th scope="col">공지일</th>
                        </tr>
                    </thead>
                    <tbody>
                        <template v-for="announcement in announcementList" :key="announcement.announcementNo">
                            <tr class="text-center">
                                <th>{{ announcement.announcementNo }}</th>
                                <td class="text-start col-7">
                                    <router-link :to="{
                                        name: 'announcementView',
                                        params: { announcementNo: announcement.announcementNo, currentPage: currentPage }
                                    }">
                                        {{ announcement.title }}
                                    </router-link>
                                </td>
                                <td>{{ announcement.hit }}</td>
                                <td>{{ announcement.registDate.substring(0, 10) }}</td>
                            </tr>
                        </template>
                    </tbody>
                </table>
            </div>
            <PaginationComponent :current-page="currentPage" :total-page-count="totalPageCount" @pageChange="movePage" />
        </div>
    </div>
</template>

<style scoped>
a {
    color: black;
    text-decoration: none;
}
</style>

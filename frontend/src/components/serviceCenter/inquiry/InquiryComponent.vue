<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { registInquiry } from '@/api/serviceCenter/inquiry'

import SearchFilterComponent from "../../common/SearchFilterComponent.vue";

const router = useRouter()

//임시
const loginUser = ref("ssafy")
const InquiryDto = ref({
    writerId: loginUser.value,
    title: "",
    contents: "",
    type: "",
})

const title = ref('')
const contents = ref('')
const selectOption = ref([
    { text: "선택해주세요", value: "" },
    { text: "회원관련", value: "user" },
    { text: "매물관련", value: "sale" },
    { text: "기타", value: "others" }
])

const changeFilter = (filter) => {
    InquiryDto.value.type = filter;
}

function regist() {
    InquiryDto.value.title = title.value
    InquiryDto.value.contents = contents.value

    console.log(InquiryDto.value)

    if (!InquiryDto.value.title) alert("제목이 입력되지 않았습니다.")
    else if (!InquiryDto.value.contents) alert("제목이 입력되지 않았습니다.")
    else (
        registInquiry(InquiryDto.value,
            () => {
                alert("작성해주신 문의내용이 등록되었습니다.")
                router.push({ name: 'inquiry' })
            },
            (error) => { console.log("에러 발생 !!! " + error) }
        )
    )
}
</script>

<template>
    <div>
        <div class="container d-flex justify-content-center align-items-center">
            <div class="row justify-content-center w-100 mb-5">
                <div
                    class="col-lg-10 row border border-black border-2 rounded-3 border-opacity-50 justify-content-center w-60">
                    <div class="pt-4 text-center">
                        <h3>문의사항 작성</h3>
                    </div>
                    <form @submit.prevent="onSubmit">
                        <div class="row mb-3 pt-4 justify-content-center">
                            <label for="title" class="col-sm-1 col-form-label">구분</label>
                            <div class="col-sm-9 mr-5 mt-1">
                                <SearchFilterComponent :selectOption="selectOption" @onFilterSelect="changeFilter" />
                            </div>
                        </div>
                        <div class="row mb-3 pt-4 justify-content-center">
                            <label for="title" class="col-sm-1 col-form-label">제목</label>
                            <div class="col-sm-9 mr-5">
                                <input type="text" class="form-control" id="title" name="title" placeholder="제목을 입력해주세요"
                                    v-model="title">
                            </div>
                        </div>
                        <div class="row mb-3 justify-content-center pt-2">
                            <label for="contents" class="col-sm-1 col-form-label">내용</label>
                            <div class="col-sm-9 mr-5">
                                <textarea class="form-control" name="contents" id="contents" placeholder="내용을 입력해주세요"
                                    rows="10" v-model="contents" />
                            </div>
                        </div>
                        <div class="col text-end mt-3 mb-4 mr-5">
                            <button type="submit" class="btn btn-primary mr-5" @click="regist">등록</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
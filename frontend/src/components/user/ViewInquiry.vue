<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { localAxios } from "../../util/http-commons";

const local = localAxios()
const route = useRoute()
const router = useRouter()
const { inquiryNo } = route.params

const inquiryDto = ref({})
const changeStatus = () => {
    local.put(`service/inquiry/${inquiryNo}`).then(({ data }) => {
        alert(data.message)
        router.push({ name: 'myPage' })
    })
}

const mvList = () => {
    router.push({ name: 'myPage' })
}

onMounted(() => {
    local.get(`service/inquiry/${inquiryNo}`).then(({ data }) => {
        inquiryDto.value = data
    })
})
</script>

<template>
    <div>
        <div class="container d-flex justify-content-center align-items-center">
            <div class="row justify-content-center w-100 mb-5">
                <div
                    class="col-lg-10 row border border-black border-2 rounded-3 border-opacity-50 justify-content-center w-60">
                    <div class="pt-4 text-center">
                        <h3>{{ inquiryDto.writerId }}님의 문의</h3>
                    </div>
                    <div class="text-end">
                        <p>문의일 : {{ inquiryDto.registDate }}( {{ inquiryDto.status }} )</p>
                    </div>
                    <div class="row mb-3 pt-4 justify-content-center">
                        <label for="title" class="col-sm-1 col-form-label">구분</label>
                        <div class="col-sm-9 mr-5">
                            <input type="text" class="form-control" id="title" name="title" :value="inquiryDto.type"
                                readonly>
                        </div>
                    </div>
                    <div class="row mb-3 pt-4 justify-content-center">
                        <label for="title" class="col-sm-1 col-form-label">제목</label>
                        <div class="col-sm-9 mr-5">
                            <input type="text" class="form-control" id="title" name="title" :value="inquiryDto.title"
                                readonly>
                        </div>
                    </div>
                    <div class="row mb-3 justify-content-center pt-2">
                        <label for="contents" class="col-sm-1 col-form-label">내용</label>
                        <div class="col-sm-9 mr-5">
                            <textarea class="form-control" name="contents" id="contents" :value="inquiryDto.contents"
                                readonly />
                        </div>
                    </div>
                    <div class="col text-end mt-3 mb-4 mr-5">
                        <button type="submit" class="btn btn-primary mr-5" @click="changeStatus">해결</button>
                        <button type="submit" class="btn btn-primary mr-5" @click="mvList">목록</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
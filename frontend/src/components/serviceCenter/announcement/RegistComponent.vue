<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router";
import { registAnnouncement } from "@/api/serviceCenter/announcement.js"

const router = useRouter();

const title = ref("")
const contents = ref("")

const announcementDto = ref({
    title: "",
    contents: ""
})

const uploadFileList = ref([])

const uploadFile = (event) => {
    uploadFileList.value = event.target.files
    console.log(uploadFileList.value.length)
}

function regist() {
    if (!title.value) alert("제목이 입력되지 않았습니다.")
    else if (!contents.value) alert("내용이 입력되지 않았습니다.")
    else {
        announcementDto.value.title = title.value
        announcementDto.value.contents = contents.value

        registAnnouncement(announcementDto.value, uploadFileList.value,
            () => {
                alert("공지사항이 등록되었습니다.")
                router.push({ name: 'announcementList', params: { currentPage: 1 } })
            },
            (error) => {
                alert("에러발생!!! " + error)
            }
        )
    }
}

function cancel() {
    let check = confirm("작성을 취소하고 목록으로 돌아가시겠습니까?\n작성내용은 저장되지 않습니다.")
    if (check) router.push({ name: "announcementList" })
}
</script>

<template>
    <div class="container d-flex justify-content-center align-items-center">
        <div class="row justify-content-center w-100">
            <div class="col-lg-10 row border border-black border-2 rounded-3 border-opacity-50 justify-content-center w-60">
                <div class="pt-4 text-center">
                    <h3>공지사항 작성</h3>
                </div>
                <form @submit.prevent="onSubmit">
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
                            <textarea class="form-control" name="contents" id="contents" placeholder="내용을 입력해주세요" rows="10"
                                v-model="contents" />
                        </div>
                    </div>
                    <div class="row mb-3 pt-4 justify-content-center">
                        <label for="title" class="col-sm-1 col-form-label">파일</label>
                        <div class="col-sm-9 mr-5">
                            <input type="file" class="form-control" id="uploadFile" name="uploadFile" multiple
                                @change="uploadFile">
                        </div>
                    </div>
                    <div class="col text-end mb-4">
                        <button type="submit" class="btn btn-primary mr-3" @click="regist">등록</button>
                        <button type="button" class="btn btn-primary mr-3" @click="cancel">취소</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<style scoped>
ul {
    list-style: none;
    padding: 5px;
}
</style>
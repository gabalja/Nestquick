<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { getAnnouncement, modifyAnnouncement } from '@/api/serviceCenter/announcement'

const route = useRoute()
const router = useRouter()

const { announcementNo } = route.params

const announcementDto = ref({
    announcementNo: announcementNo,
    title: "",
    contents: "",
    deleteFileList: [],
    newFileList: [],
    uploadFileList: []
})

const announcement = ref({})
const title = ref("")
const contents = ref("")
const uploadFileList = ref([])
const newFileList = ref([])
const deleteFileList = ref([])

onMounted(() => {
    getAnnouncement(announcementNo,
        //(response) => {announcement.value = response.data}
        ({ data }) => {
            announcement.value = data
            title.value = announcement.value.title
            contents.value = announcement.value.contents
            uploadFileList.value = announcement.value.uploadFileList
        },
        (error) => { alert("에러 발생 !!! " + error) }
    )
})

const addFile = (event) => {
    newFileList.value = event.target.files
    console.log(newFileList.value)
}

function deleteFile(saveFolder, originalName, saveName) {
    deleteFileList.value.push({ saveFolder: saveFolder, originalName: originalName, saveName: saveName })
    console.log(deleteFileList.value)
    uploadFileList.value = uploadFileList.value.filter((file) => file.saveName !== saveName)
}

function modify() {
    announcementDto.value.title = title.value
    announcementDto.value.contents = contents.value
    announcementDto.value.deleteFileList = deleteFileList.value
    announcementDto.value.uploadFileList = uploadFileList.value

    if (!announcementDto.value.title) alert("제목이 입력되지 않았습니다.")
    else if (!announcementDto.value.contents) alert("내용이 입력되지 않았습니다.")
    else {
        modifyAnnouncement(announcementDto.value, newFileList.value,
            () => { alert(announcementDto.value.announcementNo + "번 공지가 수정되었습니다."); router.push({ name: 'announcementList' }) },
            (error) => { alert("에러 발생 !!!" + error) }
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
                    <h3>{{ announcementNo }}번 공지 수정</h3>
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
                    <div class="row mb-3 justify-content-start mt-3">
                        <div class="w-70" v-show="uploadFileList.length > 0">
                            <label for="files" class="ml-3 pl-3">업로드된 파일</label>
                            <ul class="border rounded-3 border-3 mt-3 d-flex flex-wrap" style="list-style: none;"
                                name="files" id="files">
                                <li class="m-3" v-for="file in uploadFileList" :key="file.saveName">
                                    {{ file.originalName }}
                                    <button type="button" class="btn btn-danger btn-sm ml-3"
                                        @click="deleteFile(file.saveFolder, file.originalName, file.saveName)">
                                        삭제
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="row mb-3 justify-content-start">
                        <div class="w-70">
                            <label for="uploadFile" class="ml-3 pl-3">추가 업로드</label>
                            <div class="col-sm-9 mx-auto">
                                <input type="file" class="form-control" id="uploadFile" name="uploadFile" multiple
                                    @change="addFile">
                            </div>
                        </div>
                    </div>
                    <div class="col text-end mb-4 pt-3">
                        <button type="submit" class="btn btn-primary mr-3" @click="modify">수정</button>
                        <button type="button" class="btn btn-primary mr-3" @click="cancel">취소</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<style scoped>
.container {
    height: 88vh;
}
</style>
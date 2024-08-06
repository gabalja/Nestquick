<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAnnouncement, deleteAnnouncement } from '@/api/serviceCenter/announcement'
import { useUserStore } from "../../../stores/user"
import { storeToRefs } from 'pinia'

const userStore = useUserStore()
const { userId } = storeToRefs(userStore)

//초기 설정
const route = useRoute()
const router = useRouter()

//const announcementNo = ref(route.params.announcementNo)
const { announcementNo } = route.params

const announcement = ref({})
const uploadFileList = ref({})
const { currentPage } = route.params

onMounted(() => {
    getAnnouncement(announcementNo,
        //(response) => {announcement.value = response.data}
        ({ data }) => {
            announcement.value = data
            uploadFileList.value = announcement.value.uploadFileList
        },
        (error) => { alert("에러 발생 !!! " + error) }
    )
})

// function download(saveFolder, originalName, saveName) {
//     announcementFileInfoDto.value.saveFolder = saveFolder
//     announcementFileInfoDto.value.originalName = originalName
//     announcementFileInfoDto.value.saveName = saveName

//     downloadFile(announcementFileInfoDto.value,
//         (response) => {
//             // create file link in browser's memory
//             const href = URL.createObjectURL(new Blob([response.data], { type: response.headers['content-type'] }));

//             // create "a" HTML element with href to file & click
//             const link = document.createElement('a');
//             link.href = href;
//             link.setAttribute('download', announcementFileInfoDto.value.originalName); //or any other extension
//             document.body.appendChild(link);
//             link.click();

//             // clean up "a" element & remove ObjectURL
//             document.body.removeChild(link);
//             URL.revokeObjectURL(href);

//         },
//         (error) => { alert("에러 발생 !!! " + error) }
//     )
// }

function modify() {
    router.push({ name: 'announcementModify', params: { currentPage: currentPage } })
}

function remove() {
    let check = confirm("정말 삭제하시겠습니까?")
    if (check) {
        deleteAnnouncement(announcementNo,
            () => {
                alert(announcementNo + "번 공지가 삭제되었습니다.")
                router.push({ name: 'announcementList' })
            },
            (error) => { alert("에러 발생 !!!" + error) }
        )
    }
}

function list() {
    router.push({ name: 'announcementList', params: { currentPage: currentPage } })
}

</script>

<template>
    <div class="container d-flex justify-content-center">
        <div class="my-5 row justify-content-center w-100">
            <div class="col-lg-10 row border border-black border-2 rounded-3 border-opacity-50 justify-content-center w-60">
                <div class="pt-4 text-center">
                    <h3>{{ announcement.announcementNo }}번 공지</h3>
                </div>
                <div class="text-end">
                    <p>공지일 : {{ announcement.registDate }}</p>
                </div>
                <div class="row mb-3 justify-content-center">
                    <label for="title" class="col-sm-1 col-form-label">제목 :</label>
                    <div class="col-sm-9 mr-5">
                        <input type="text" class="form-control" id="title" readonly="readonly" :value="announcement.title">
                    </div>
                </div>
                <div class="row mb-3 justify-content-center pt-2">
                    <label for="contents" class="col-sm-1 col-form-label">내용 :</label>
                    <div class="col-sm-9 mr-5">
                        <textarea class="form-control" id="contents" readonly="readonly" :value="announcement.contents"
                            rows="15" />
                    </div>
                </div>
                <div class="row mb-3 justify-content-start" style="">
                    <div class="w-70" v-show="uploadFileList.length > 0">
                        <label class="ml-3 pl-3" for="files">첨부파일</label>
                        <ul class="border rounded-3 border-3 mt-3 d-flex flex-wrap" style="list-style: none;" id="files">
                            <li class="m-3" v-for="file in uploadFileList" :key="file.saveName">
                                {{ file.originalName }}
                                <a
                                    :href="`http://localhost/nestquick/service/announcement/download/${file.saveFolder}/${file.originalName}/${file.saveName}`">[다운로드]</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="col text-end mb-4 align-text-end">
                    <button type="button" class="btn btn-primary mr-3" @click="modify" v-if="userId === 'admin'">수정</button>
                    <button type="button" class="btn btn-primary mr-3" @click="remove" v-if="userId === 'admin'">삭제</button>
                    <button type="button" class="btn btn-primary mr-3" @click="list">목록</button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
ul {
    list-style: none;
}

table {
    padding-left: 100px;
}
</style>
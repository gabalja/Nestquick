<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'

const local = inject('local')
const inquiryList = ref([])
const router = useRouter()

onMounted(() => {
    local.get('/user/myPage/inquiryList').then(({ data }) => { inquiryList.value = data.inquiryList; console.log(data) }).catch((error) => { alert("에러 발생 !!! " + error) })
})

const getStatus = (status) => {
    if (status === '처리중') return 'processing'
    else if (status === '해결') return 'solved'
    else return ''
}

const navigateToInquiryView = (inquiryNo) => {
    console.log("이동!!!!")
    router.push({
        name: 'inquiryView',
        params: { inquiryNo }
    });
}

</script>

<template>
    <div class="container">
        <div class="row justify-content-start">
            <div class="col-lg-10">
                <table class="table table-hover">
                    <thead class="table-light">
                        <tr class="text-center">
                            <th scope="col">작성자</th>
                            <th scope="col" class="col-7">제목</th>
                            <th scope=" col">구분</th>
                            <th scope="col">문의일</th>
                            <th scope="col">상태</th>
                        </tr>
                    </thead>
                    <tbody>
                        <template v-for="inquiry in inquiryList" :key="inquiry.inquiryNo">
                            <tr class="text-center">
                                <th>{{ inquiry.writerId }}</th>
                                <td class="text-start col-7">
                                    <span @click="navigateToInquiryView(inquiry.inquiryNo)">{{ inquiry.title }}</span>
                                </td>
                                <td>{{ inquiry.type }}</td>
                                <td>{{ inquiry.registDate.substring(0, 10) }}</td>
                                <td :class="getStatus(inquiry.status)">{{ inquiry.status }}</td>
                            </tr>
                        </template>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<style scoped>
.solved {
    color: green;
}

.processing {
    color: red;
}

a {
    color: black;
    text-decoration: none;
}
</style>
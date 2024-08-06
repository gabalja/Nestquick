<script setup>
import { computed } from 'vue';

const props = defineProps({ currentPage: Number, totalPageCount: Number })
const emit = defineEmits(['pageChange']);

const navigationSize = parseInt(import.meta.env.VITE_NAVIGATION_SIZE);

const startPage = computed(() => {
    return parseInt((props.currentPage - 1) / navigationSize) * navigationSize + 1;
});

const endPage = computed(() => {
    let lastPage = parseInt((props.currentPage - 1) / navigationSize) * navigationSize + navigationSize;
    return props.totalPageCount < lastPage ? props.totalPageCount : lastPage;
});

const endRange = computed(() => {
    return parseInt((props.totalPageCount - 1) / navigationSize) * navigationSize < props.currentPage;
});

function range(start, end) {
    const list = [];
    for (let i = start; i <= end; i++) list.push(i);

    return list;
}

function onPageChange(page) {
    emit("pageChange", page);
}

</script>

<template>
    <div class="row pb-5 ">
        <ul class="pagination justify-content-center">
            <li class="page-item">
                <a class="page-link" @click="onPageChange(1)">맨앞</a>
            </li>
            <li class="page-item">
                <a class="page-link" @click="onPageChange(startPage == 1 ? 1 : startPage - 1)">이전</a>
            </li>
            <template v-for="page in range(startPage, endPage)" :key="page">
                <li :class="currentPage === page ? 'page-item active' : 'page-item'">
                    <a class="page-link" @click="onPageChange(page)">{{ page }}</a>
                </li>
            </template>
            <li class="page-item">
                <a class="page-link" @click="onPageChange(endRange ? totalPageCount : endPage + 1)">다음</a>
            </li>
            <li class="page-item">
                <a class="page-link" @click="onPageChange(totalPageCount)">맨끝</a>
            </li>
        </ul>
    </div>
</template>

<style scoped>
a {
    cursor: pointer;
}
</style>
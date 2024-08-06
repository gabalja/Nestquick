<script setup>
import { ref, onMounted } from 'vue'
import { getQuestion } from '@/api/serviceCenter/faq'

const FAQ = ref({})

onMounted(() => {
    getQuestion(
        ({ data }) => { FAQ.value = data; console.log(FAQ.value) },
        (error) => { console.log("에러 발생!!! " + error) }
    )
})

</script>

<template>
    <div class="container col-lg-6">
        <div class="accordion" id="accordionExample">
            <div class="accordion-item" v-for="question in FAQ" :key="question.questionNo">
                <h2 class="accordion-header">
                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                        :data-bs-target="'#' + question.questionNo" aria-expanded="true" aria-controls="collapseOne">
                        Q. {{ question.title }}
                    </button>
                </h2>
                <div :id="question.questionNo" class="accordion-collapse collapse">
                    <div class="accordion-body">
                        A. {{ question.contents }}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.accordion-item .accordion-button {
    background-color: transparent;
    color: black;
}


/* 포커스 효과 제거 */
.accordion-button:focus {
    outline: none;
    box-shadow: none;
}


.accordion-button:hover {
    background-color: #CCE5FF
}
</style>
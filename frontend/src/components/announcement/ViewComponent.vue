<script setup>
//관리자에게만 수정, 삭제 버튼 보이게(임시)
const isAdmin = (true)
</script>

<template>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <h2 class="my-3 py-3 shadow-sm bg-light text-center">
                    <mark class="sky">공지사항</mark>
                </h2>
            </div>
            <div class="col-lg-10">
                <div class="row my-2">
                    <h2 class="text-secondary px-5">{{ announcement.title }}</h2>
                </div>
                <div class="row">
                    <div class="col-md-8">
                        <div class="clearfix align-content-center">
                            <p>
                                <span class="text-secondary fw-light"> {{ announcement.registDate }} 조회수 :
                                    {{ announcement.hit }} </span>
                            </p>
                        </div>
                    </div>
                    <div class="divider mb-3"></div>
                    <div class="text-secondary">
                        {{ announcement.contents }}
                    </div>
                    <div class="divider mt-3 mb-3"></div>
                    <div class="d-flex justify-content-end">
                        <button type="button" id="btn-list" class="btn btn-outline-primary mb-3">
                            글목록
                        </button>
                        <!--관리자면 보이는 버튼, Modify-->
                        <button v-if="isAdmin" type="button" id="btn-mv-modify" class="btn btn-outline-success mb-3 ms-1">
                            글수정
                        </button>
                        <!--관리자면 보이는 버튼, 정말 삭제하시겠습니까? 알림, delete 요청-->
                        <button v-if="isAdmin" type="button" id="btn-delete" class="btn btn-outline-danger mb-3 ms-1">
                            글삭제
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
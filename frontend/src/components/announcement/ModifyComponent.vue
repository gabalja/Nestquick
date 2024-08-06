<script setup>

</script>

<!--axios로 받아온 announcement 정보를 우선 뿌려줌-->
<template>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <h2 class="my-3 py-3 shadow-sm bg-light text-center">
                    <mark class="sky">공지사항 수정</mark>
                </h2>
            </div>
            <div class="col-lg-10">
                <form id="form-modify" method="POST" action="">
                    <div class="mb-3">
                        <!--수정하고 게시일 최신화?? 수정일로 바꿔서??-->
                        <label for="registDate" class="form-label">게시일 : </label>
                        <input type="text" class="form-control" id="registDate" value="announcement.registDate" readonly />
                    </div>
                    <div class="mb-3">
                        <label for="title" class="form-label">제목 : </label>
                        <input type="text" class="form-control" id="title" value="announcement.title" />
                    </div>
                    <div class="mb-3">
                        <label for="contents" class="form-label">내용 : </label>
                        <textarea class="form-control" id="contents" rows="7">{{ announcement.contents }}</textarea>
                    </div>
                    <div class="col-auto text-center">
                        <!--modify 요청, 수정되었습니다 알림 띄우고, View-->
                        <button type="button" id="btn-modify" class="btn btn-outline-primary mb-3">
                            글수정
                        </button>
                        <!--작성한 내용이 사라집니다 알림 띄우고, List-->
                        <button type="button" id="bnt-list" class="btn btn-outline-danger mb-3">
                            목록
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
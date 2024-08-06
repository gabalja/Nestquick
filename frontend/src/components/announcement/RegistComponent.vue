<script setup>

</script>

<template>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <h2 class="my-3 py-3 shadow-sm bg-light text-center">
                    <mark class="sky">공지사항 작성</mark>
                </h2>
            </div>
            <div class="col-lg-10">
                <form id="form-register" method="POST" action="">
                    <div class="mb-3">
                        <label for="subject" class="form-label">제목 : </label>
                        <input type="text" class="form-control" id="subject" name="subject" placeholder="제목..." />
                    </div>
                    <div class="mb-3">
                        <label for="content" class="form-label">내용 : </label>
                        <textarea class="form-control" id="content" name="content" rows="7"></textarea>
                    </div>
                    <div class="col-auto text-center">
                        <!--regist 요청 전송, 작성이 완료되었습니다 알림, List-->
                        <button type="button" id="btn-register" class="btn btn-outline-primary mb-3">
                            글작성
                        </button>
                        <!--작성 내용이 사라집니다 알림, list -->
                        <button type="reset" class="btn btn-outline-danger mb-3">취소</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
<script setup>
//관리자에게만 글쓰기 버튼 보이게(임시)
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
                <div class="row align-self-center mb-2">
                    <!--관리자에게만 글쓰기 버튼 보이게(임시)-->
                    <div class="col-md-2 text-start" v-if="isAdmin">
                        <!--공지사항 Regist-->
                        <button type="button" id="btn-mv-register" class="btn btn-outline-primary btn-sm">
                            글쓰기
                        </button>
                    </div>
                    <div class="col-md-5 offset-5">
                        <form class="d-flex">
                            <select id="key" class="form-select form-select-sm ms-5 me-1 w-50" aria-label="검색조건">
                                <option selected>검색조건</option>
                                <option value="title">제목</option>
                                <option value="registDate">게시일</option>
                            </select>
                            <div class="input-group input-group-sm">
                                <input type="text" class="form-control" placeholder="검색어..." />
                                <!--검색 결과만 보이게-->
                                <button class="btn btn-dark" type="button">검색</button>
                            </div>
                        </form>
                    </div>
                </div>
                <table class="table table-hover">
                    <thead>
                        <tr class="text-center">
                            <th scope="col">공지번호</th>
                            <th scope="col">제목</th>
                            <th scope="col">조회수</th>
                            <th scope="col">게시일</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!--axios로 받아온 공지사항 JSON에서 받아서 뿌려준다-->
                        <tr class="text-center" v-for="announcement in announcements" :key="announcement.articleNo">
                            <th scope="row">{{ announcement.articleNo }}</th>
                            <td class="text-start">
                                <!--제목 누르면 View -->
                                <a href="#" class="announcement-title link-dark" data-no="announcement.articleNo"
                                    style="text-decoration: none">
                                    {{ announcement.title }}
                                </a>
                            </td>
                            <td>{{ announcement.hit }}</td>
                            <td>{{ announcement.registDate }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
<p align="center">
    <img alt="로고" src="https://github.com/gabalja/Nestquick/blob/main/assets/nestquick_logo.png" width="50%" height="50%"/>
</p>

---
## 📇 목차
- [프로젝트 소개](#-프로젝트-소개)
- [팀원 소개](#-팀원-소개)
- [Role](#-role)
- [개발 일정](#-개발-일정)
- [기술 스택](#-기술-스택)
- [주요 기능 및 역할](#-주요-기능-및-역할)
- [폴더 구조 및 아키텍쳐 설계](#-폴더-구조-및-아키텍쳐-설계)
---

## 🍀 프로젝트 소개
**부동산 매물 실시간 가격 조회 및 주변 분석을 통해 가장 적합한 매물을 찾아주는 서비스**
#### 컨셉
- Nestquick은 Nest(둥지) + quick(빠르다)의 합성어
- 고객이 원하는 집을 빠르게 찾아준다는 의미 내포
#### 강점
- 빠르고 간단하게 사용자가 원하는 정보 제시
- 간편한 매물 상세정보 조회
  
## 💁 팀원 소개
| <img alt="이지원" src="https://github.com/yesFlash.png" width="230" height="100%"/> | <img alt="이경배" src="https://github.com/rglley.png" width="230" height="100%" /> | <img alt="손의성" src="https://github.com/gabalja.png" width="230" height="100%"/> |
|:---------------------------------------------------------:|:-----------------------------------------------------------------:|:-------------------------------------------------------------:|
| [이지원](https://github.com/yesFlash) | [이경배](https://github.com/rglley) | [손의성](https://github.com/gabalja) |                            

## 👨‍👩‍👧‍👦 Role
**이지원**
- Team Leader
- Kakao Map API를 활용한 지도 서비스
- Chart.js를 사용한 거래가 그래프 제작
  
**이경배**
- Back Leader
- JWT를 사용한 회원관리 기능 구축
- 고객 게시판 구현
- Swagger 셋업
  
**손의성**
- Front Leader
- Vue 기반 전반적 화면 설계
- 마이페이지 제작
- 최종 발표

## 📅 개발 일정
<p align="center">
    <img alt="일정" src="https://github.com/gabalja/Nestquick/blob/main/assets/dev_schedule.png" width="100%" height="100%"/>
</p>

## 🛠️ 기술 스택

#### Front End
![Vue.js](https://img.shields.io/badge/vuejs-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D)
![Vuetify](https://img.shields.io/badge/Vuetify-1867C0?style=for-the-badge&logo=vuetify&logoColor=AEDDFF)
![Bootstrap](https://img.shields.io/badge/bootstrap-%238511FA.svg?style=for-the-badge&logo=bootstrap&logoColor=white)
![Chart.js](https://img.shields.io/badge/chart.js-F5788D.svg?style=for-the-badge&logo=chart.js&logoColor=white)

#### Back End
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">

#### Database
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">

#### Version Control
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)

# 타켓 후보 => 일단 특정 대상 없이 Common하게

- [ ]  반려동물 : 공원, 동물병원
- [ ]  사회초년생
- [ ]  부모님을 위한 집 : 병원
- [ ]  힐링 : 공원
- [ ]  뷰, 숲세권
- [ ]  문화 활동 : 전시, 공연, 액티비티, 지역 문화 행사 정보, 매물 점수

# 기능 후보

- [x]  사용자 구분 : 관리자, 부동산업자, 일반 고객
- [x]  공지사항 (작성자 제한)
- [x]  1:1 문의하기
- [ ]  매물 즐겨찾기
    - [x]  찜한 매물 모아보기
- [x]  최근 본 매물
- [x]  핫한 매물 (마커 색상 구분)
- [x]  매물 SNS 공유
- [ ]  소셜 로그인
- [ ]  회원 가입 인증 (문자, 메일)
- [ ]  매물 메모 (댓글 느낌)
- [x]  매물 후기
- [x]  매물 등록
    - [x]  매물 등록 시 관리자 1차 필터링
- [ ]  매물 추천 : 거리, 찜한 매물(지역) 기준
- [x]  선택한 매물 주변 인프라
- [x]  지도화면에서 보고싶은 정보 필터링
- [x]  검색 필터링 : 전/월세, 가격
- [x]  매매/원룸/빌라
- [ ]  선택한 지역 인프라 점수 (OOO은 어떤 동네일까요?)
- [ ]  지역 커뮤니티
    - [ ]  실시간 채팅

### 관통 문서 아이디어

- [x]  관련 뉴스
- [x]  관심 지역 정보 관리
- [ ]  주변 탐방 정보 수집
- [ ]  주변 업종 정보
- [ ]  주변 환경 정보

### 오픈 API (행정동 기준)

- [ ]  구성원 연령대
- [ ]  CCTV 설치 현황
- [ ]  1인 세대 수
- [ ]  인구 증감 (직방에 있는 기능)

### 서비스

- [ ]  리뷰등록, 활동점수에 따른 포인트 → 지역 상점에서 사용


# 참고 사이트 리스트

- [ ]  직방
- [ ]  다방
- [ ]  KB 부동산

다방 : 구역 그리기

KB : 유툽, 이미지 검색 바로가기 버튼

- [ ]  공지사항 제목 선택시 제목 아래에 내용 영역 보여주기 (페이징 부담 감소)

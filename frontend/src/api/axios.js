import { ref } from "vue"
import axios from "axios"

// 요청 경로 설정
// backend의 설정이 
// server.servlet.context - path=/board
// server.port=80 이므로 맞춰서 적어줌
const SERVER_URL = ref("http://localhost")
const PORT = ref(":80")
const VERSION = ref("/board")

export const instance = axios.create({  // axios 객체 생성
    baseURL : SERVER_URL.value + PORT.value + VERSION.value
})

// 세션 스토리지에 저장되어 있는 userStore의 Authorization를 'Authorization'라는 이름으로 헤더에 넣어줌
// 로그인 전 사용자 기준 sessionStorage.getItem('userStore')가 null이다
// 삼항 연산자로 값 결정
// sessionStorage.getItem('userStore')는 문자열 상태이므로 JSON.parse해서 값을 가져오자
//instance.defaults.headers.common['Authorization'] = sessionStorage.getItem('userStore') == null ? '' : JSON.parse(sessionStorage.getItem('userStore'))['Authorization']

// axios 공식 문서 보면 있음
// 이렇게 설정하는거다? 해야한다?
instance.defaults.headers.post['Content-Type'] = 'application/json'

instance.interceptors.request.use((config) => {
    return config
}), (error) => {
    return Promise.reject(error)
    }

instance.interceptors.response.use((response) => {
    return response
}), (error) => {
    return Promise.reject(error)
}
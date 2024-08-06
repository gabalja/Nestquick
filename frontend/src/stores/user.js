import { ref, inject } from 'vue'
import { defineStore, storeToRefs } from 'pinia'
import { useSaleStore } from '@/stores/sale'
import { useDealStore } from '@/stores/deal'

// Pinia Composing Store
// https://pinia.vuejs.org/cookbook/composing-stores.html
export const useUserStore = defineStore(
  'userStore',
  () => {
    const saleStore = useSaleStore()
    const { bookmarks } = storeToRefs(saleStore)
    const dealStore = useDealStore()
    const { recentAptCode } = storeToRefs(dealStore)

    const isLogin = ref(false)
    const userId = ref('')
    const accessToken = ref('')
    const local = inject('local')
    const menuList = ref([
      { name: '로그인', routeName: 'userLogin' },
      { name: '회원가입', routeName: 'userSignup' },
      { name: '마이페이지', routeName: 'myPage' },
      { name: '로그아웃', routeName: 'logout' }
    ])

    const userLogin = async (userDto) => {
      await local
        .post('/user/login', userDto)
        .then(({ data }) => {
          accessToken.value = data.accessToken
          local.defaults.headers.common['Authorization'] = accessToken.value
          isLogin.value = true
          userId.value = userDto.userId

          local
            .get('/deal/listBookmark', { params: { user_id: userId.value } })
            .then(({ data }) => {
              bookmarks.value = data
            })
            .catch((error) => alert('에러 발생 !!! ' + error))

          alert(data.message)
        })
        .catch((error) => alert('에러 발생 !!! ' + error))
    }

    const userLogout = async () => {
      await local
        .delete('/user/logout')
        .then(({ data }) => {
          local.defaults.headers.common['Authorization'] = ''
          isLogin.value = false
          alert(data.message)
          localStorage.setItem('login', '')
          userId.value = ''
          // 로그아웃할 때 북마크 목록 지우기
          bookmarks.value = []
          // 최근 본 매물 목록도 초기화
          recentAptCode.value = []
        })
        .catch((error) => {
          alert('에러 발생 !!! ' + error)
        })
    }

    // 회원 정보를 수정하면 axios header에 저장된 accessToken을 삭제한다.
    const userModify = async (userInfo) => {
      await local.put(`/user/modify`, userInfo).then((response) => {
        local.defaults.headers.common['Authorization'] = ''
        isLogin.value = false
        alert(response.data.message)
      })
    }

    const pwdModify = async (pwdInfo) => {
      await local.put('/user/modifyPwd', pwdInfo).then((response) => {
        local.defaults.headers.common['Authorization'] = ''
        isLogin.value = false
        alert(response.data.message)
      })
    }

    // // 회원 탈퇴 시 axios header에 저장된 accessToken을 삭제한다.
    const withdrawal = async (userId) => {
      await local.delete(`/user/withdrawal/${userId}`).then(() => {
        local.defaults.headers.common['Authorization'] = ''
        alert(`회원정보(${userId})가 삭제되었습니다.`)
      })
    }

    return {
      isLogin,
      menuList,
      accessToken,
      userLogin,
      userId,
      userLogout,
      userModify,
      pwdModify,
      withdrawal
    }
  },
  {
    persist: {
      storage: sessionStorage
    }
  }
)

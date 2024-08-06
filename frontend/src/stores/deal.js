import { ref} from 'vue'
import { defineStore } from 'pinia'

export const useDealStore = defineStore('dealStore', () => {
    
    // 최근 본 아파트
    const recentAptCode = ref([])

    return{recentAptCode}
},
{
  persist: {
    storage: sessionStorage
  }
})

// 최근 본 아파트! 사용할 페이지에서 아래 내용 작성하고 
// recentAptCode를 ref 변수 사용법으로 쓰면 됩니다
// import { storeToRefs } from 'pinia'
// import { useDealStore } from '@/stores/deal'
// const dealStore = useDealStore()
// const { recentAptCode } = storeToRefs(dealStore)
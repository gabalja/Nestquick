import { ref, inject } from 'vue'
import { defineStore } from "pinia"

// pinia를 통한 상태 관리
// https://mine-it-record.tistory.com/641
export const useSaleStore = defineStore('saleStore', () => {
  
  const axios = inject('axios')

  async function regist(title, content) {
    
    await axios.post('/sale/regist', { title, content })
      .then((response) => {
        console.log(response)
      })

  }

  return {
    regist,
  }
})
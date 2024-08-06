import { localAxios } from '@/util/http-commons'

const local = localAxios()

const controller = '/service/FAQ'

function getQuestion(success, fail) {
    local.get(`${controller}`).then(success).catch(fail)
}
  
export {
    getQuestion
  }
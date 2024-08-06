import { localAxios } from '@/util/http-commons'

const local = localAxios()

const controller = '/user'

function idcheck(userId, success, fail) {
  local.get(`${controller}/${userId}`).then(success).catch(fail)
}

function signup(userDto, success, fail) {
  local.post(`${controller}/signUp`, userDto).then(success).catch(fail)
}

export {
  idcheck,
  signup,
}

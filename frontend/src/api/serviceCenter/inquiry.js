import { localAxios } from '@/util/http-commons'

const local = localAxios()

const controller = '/service/inquiry'

function registInquiry(inquiryDto, success, fail) {
  const formData = new FormData()

  formData.append(
    'inquiryDto',
    new Blob([JSON.stringify(inquiryDto)], { type: 'application/json' })
  )
  local
    .post(`${controller}`, formData, { headers: { 'Content-Type': `multipart/form-data` } })
    .then(success)
    .catch(fail)
}

export { registInquiry }

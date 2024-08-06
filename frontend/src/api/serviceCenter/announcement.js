import { localAxios } from '@/util/http-commons'

const local = localAxios()

const controller = '/service/announcement'

function listAnnouncement(listParameterDto, success, fail) {
  local.get(`${controller}`, { params: listParameterDto }).then(success).catch(fail)
}

function getAnnouncement(announcementNo, success, fail) {
  local.get(`${controller}/${announcementNo}`).then(success).catch(fail)
}

function registAnnouncement(announcementDto, uploadFileList, success, fail) {
  const formData = new FormData()
  for (let fileIdx = 0; fileIdx < uploadFileList.length; fileIdx++) {
    formData.append('uploadFile', uploadFileList[fileIdx])
  }
  formData.append(
    'announcementDto',
    new Blob([JSON.stringify(announcementDto)], { type: 'application/json' })
  )
  local
    .post(`${controller}`, formData, { headers: { 'Content-Type': `multipart/form-data` } })
    .then(success)
    .catch(fail)
}

function modifyAnnouncement(announcementDto, newFileList, success, fail) {
  const formData = new FormData()
  for (let fileIdx = 0; fileIdx < newFileList.length; fileIdx++) {
    formData.append('newFile', newFileList[fileIdx])
  }
  formData.append(
    'announcementDto',
    new Blob([JSON.stringify(announcementDto)], { type: 'application/json' })
  )
  local
    .put(`${controller}`, formData, { headers: { 'Content-Type': `multipart/form-data` } })
    .then(success)
    .catch(fail)
}

function deleteAnnouncement(announcementNo, success, fail) {
  local.delete(`${controller}/${announcementNo}`).then(success).catch(fail)
}

export {
  listAnnouncement,
  getAnnouncement,
  registAnnouncement,
  modifyAnnouncement,
  deleteAnnouncement,
}

import { localAxios } from "@/util/http-commons"

const local = localAxios()

function regist(data, success, fail) {

  const formdata = new FormData()
  formdata.append('imagefile', data.imageFile[0])
  // json 타입의 파일처럼 넘겨준다
  formdata.append('other', new Blob([JSON.stringify(data)], { type: "application/json" }))

  // 객체 형태이기 때문에 stringify해서 보내주자
  local.post('/sale/regist', formdata, {
    headers: {
      "Content-Type": `multipart/form-data`,
    }
  })
    .then(success)
  .catch(fail)

}

function detail(sale_no, success, fail) {
  local.get(`/sale/view/${sale_no}`).then(success).catch(fail)
}

// param으로 아파트 코드가 넘어옴
function list(param, success, fail) {
  local.get("/sale/list", { params: param }).then(success).catch(fail);
}
export {
  regist,
  detail,
  list
}
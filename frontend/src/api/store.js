import { localAxios } from "@/util/http-commons";

const mapStore = localAxios();

function shoplist(param, success, fail) {
    mapStore.get("/store/list", { params: param }).then(success).catch(fail);
}

export {
    shoplist
 };
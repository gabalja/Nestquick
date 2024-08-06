import { localAxios } from "@/util/http-commons";

const aptDeal = localAxios();

function listAptDeal(param, success, fail) {
    aptDeal.get("", { params: param }).then(success).catch(fail);
}

function requestAPIAptList(param, success, fail) {
    aptDeal.get("/deal/APIaptdeal", { params: param }).then(success).catch(fail);
}

function aptListFromDB(param, success, fail) {
    aptDeal.get("/deal/DBaptlist", { params: param }).then(success).catch(fail);
}

function aptDealListFromDB(param, success, fail) {
    aptDeal.get("/deal/DBaptDeal", { params: param }).then(success).catch(fail);
}

function addBookmark(param, success, fail) {
    aptDeal.get("/deal/addBookmark", { params: param }).then(success).catch(fail);
}

function deleteBookmark(param, success, fail) {
    aptDeal.get("/deal/deleteBookmark", { params: param }).then(success).catch(fail);
}

function listBookmark(param, success, fail) {
    aptDeal.get("/deal/listBookmark", { params: param }).then(success).catch(fail);
}

function popularApt(param, success, fail) {
    aptDeal.get("/deal/popularApt", { params: param }).then(success).catch(fail);
}

function aptinfo(param, success, fail) {
    aptDeal.get("/deal/aptinfo/" + param).then(success).catch(fail);
}

export {
    listAptDeal, requestAPIAptList, aptListFromDB, aptDealListFromDB,
    addBookmark, deleteBookmark, listBookmark, popularApt, aptinfo, 
 };

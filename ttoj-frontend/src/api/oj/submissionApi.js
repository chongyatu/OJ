import request from "@/utils/request";

export function getSubmissionByCondition(condition){
    return request.post('/oj-api/oj/submission/getSubmissionByCondition', condition)
}

export default {
    getSubmissionByCondition
}

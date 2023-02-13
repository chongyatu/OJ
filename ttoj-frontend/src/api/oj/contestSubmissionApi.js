import request from '@/utils/request'

export function getContestSubmissionByContestId(condition){
    return request.post(`/oj-api/oj/contest-submission`, condition)
}

export default {
    getContestSubmissionByContestId
}

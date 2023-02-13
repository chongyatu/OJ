import request from '@/utils/request'

export function getProblemsByContestId(contestId){
    return request.get(`/oj-api/oj/contest-problem/${contestId}/problems`)
}

export function getProblemByContestIdAndDisplayId(contestId, problemDisplayId){
    return request.get(`/oj-api/oj/contest-problem/${contestId}/problems/${problemDisplayId}`)
}
export default {
    getProblemsByContestId
}

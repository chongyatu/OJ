import request from '@/utils/request'

export function getContestByContestId(contestId){
    return request.get(`/oj-api/oj/contest/${contestId}`)
}

export function getContestsByCondition(condition) {
    return request.post('/oj-api/oj/contest/getContestsByCondition', condition)
}

export function getRecentContest(days){
    return request.get(`/oj-api/oj/contest/recent/${days}`)
}

export function getContestRankingsByCondition(condition){
    return request.post(`/oj-api/oj/contest/rankings`, condition)
}

export default {
    getContestByContestId,
    getContestsByCondition,
    getRecentContest,
    getContestRankingsByCondition,
}

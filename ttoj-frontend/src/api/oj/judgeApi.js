import request from '@/utils/request'

export function archiveJudge(params){
    return request.post('/oj-api/oj/judge/archive', params)
}

export function contestSubmit(params){
    return request.post('/oj-api/oj/judge/contestSubmit', params)
}

export function testJudge(params){
    return request.post('/oj-api/oj/judge/test', params)
}

export default {
    archiveJudge,
    contestSubmit,
    testJudge
}

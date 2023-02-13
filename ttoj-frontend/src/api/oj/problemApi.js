import request from '@/utils/request'

export function getProblemsByCondition(condition) {
    return request.post('/oj-api/oj/problem/getProblemsByCondition', condition)
}

export function getProblemById(id){
    return request.get('/oj-api/oj/problem/' + id)
}

export default {
    getProblemsByCondition,
    getProblemById,
}

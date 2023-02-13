import request from '@/utils/request'

export function getCurrentUser(){
    return request.get('/oj-api/oj/user/currentUser')
}

export function getRankListByCondition(condition){
    return request.post(`/oj-api/oj/user/rank`, condition)
}

export default {
    getCurrentUser,
    getRankListByCondition
}


import request from '@/utils/request'

export function getContestByContestId(contestId) {
    return request.get(`/oj-api/oj/manage/contest/${contestId}`)
}

export function getContestsByCondition(condition) {
    return request.post('/oj-api/oj/manage/contest/getContestsByCondition', condition)
}

export function changeContestVisibility(contestId, visible) {
    return request.post(`/oj-api/oj/manage/contest/changeContestVisibility/${contestId}/${visible}`)
}

export function saveContest(contest) {
    return request.post('/oj-api/oj/manage/contest/save', contest)
}

export function updateContest(contest) {
    return request.post('/oj-api/oj/manage/contest/update', contest)
}

export function deleteContestById(contestId) {
    return request.post(`/oj-api/oj/manage/contest/delete`, contestId)
}

export function getContestAuthorUsersBySearch(params) {
    return request.post(`/oj-api/oj/manage/contest/getContestAuthorUsersBySearch`, params)
}

export function updateContestAuthors(contestAuthorsUpdateParams) {
    console.log(contestAuthorsUpdateParams);
    return request.post(`/oj-api/oj/manage/contest/updateContestAuthors`, contestAuthorsUpdateParams)
}


export default {
    getContestByContestId,
    getContestsByCondition,
    changeContestVisibility,
    saveContest,
    deleteContestById,
    getContestAuthorUsersBySearch,
    updateContestAuthors
}

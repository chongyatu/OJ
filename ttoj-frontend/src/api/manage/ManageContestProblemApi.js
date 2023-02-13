import request from '@/utils/request'

export function getContestProblemByCondition(condition){
    console.log(condition);
    return request.post(`/oj-api/oj/manage/contestProblem/getContestProblemsByCondition`, condition)
}

export function deleteContestProblemByProblemId(deleteParams){
    return request.post(`/oj-api/oj/manage/contestProblem/deleteContestProblem`, deleteParams)
}

/**
 *  @PostMapping("getProblemsFromArchiveByCondition")
 public R getProblemsFromArchiveByCondition(@RequestBody ManageContestProblemQueryParams manageContestProblemQueryParams){

 */
export function getProblemsFromArchiveByCondition(condition){
    return request.post(`/oj-api/oj/manage/contestProblem/getProblemsFromArchiveByCondition`, condition)
}

/*
  @PostMapping("addProblem")
    public R addContestProblemById(@RequestBody  ManageContestProblemAddParams manageContestProblemAddParams){

 */
export function addContestProblemById(addPrams){
    return request.post(`/oj-api/oj/manage/contestProblem/addContestProblem`, addPrams)
}


export function updateContestProblem(updateParams){
    return request.post(`/oj-api/oj/manage/contestProblem/updateContestProblem`, updateParams)
}

export default {
    getContestProblemByCondition,
    deleteContestProblemByProblemId,
    getProblemsFromArchiveByCondition,
    addContestProblemById,
    updateContestProblem
}

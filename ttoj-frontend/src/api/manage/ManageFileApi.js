import request from '@/utils/request'
import {downloadFile} from "@/utils/util";

// @GetMapping("testcase/{problemId}")
// downloadTestCase
export function downloadTestCase(problemId){
    downloadFile(`/oj-api/oj/manage/file/testcase/${problemId}`)
}

export default {
    downloadTestCase
}

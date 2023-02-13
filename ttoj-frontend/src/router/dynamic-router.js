import ManageProblemList from "@/views/Manage/ManageProblem/ManageProblemList";
import ManageProblemEditor from "@/views/Manage/ManageProblem/ManageProblemEditor";
import ManageContestList from "@/views/Manage/ManageContest/ManageContestList";
import ManageContestEditor from "@/views/Manage/ManageContest/ManageContestEditor";
import ManageUserList from "@/views/Manage/ManageUser/ManageUserList";
import ManageContestProblemList from "@/views/Manage/ManageContest/ManageContestProblemList";

export default [
    {
      path: 'users',
      meta: {
          name: 'ManageUser',
          requiredAuth: true
      },
      component: ManageUserList
    },
    {
        path: 'problems',
        meta: {
            name: 'ManageProblem',
            requiredAuth: true
        },
        component: ManageProblemList
    },
    {
        path: 'problems/create',
        meta:{
            name: 'ManageProblem',
            requiredAuth: true,
            scrollTop: 0,
        },
        component: ManageProblemEditor
    },
    {
        path: 'problems/:problemId',
        meta:{
            name: 'ManageProblem',
            requiredAuth: true
        },
        component: ManageProblemEditor
    },
    {
        path: 'contests',
        meta:{
            name: 'ManageContest',
            requiredAuth: true
        },
        component: ManageContestList
    },
    {
        path: 'contests/create',
        meta:{
            name: 'ManageContest',
            requiredAuth: true
        },
        component: ManageContestEditor
    },
    {
        path: 'contests/:contestId',
        meta:{
            name: 'ManageContest',
            requiredAuth: true
        },
        component: ManageContestEditor
    },
    {
        path: 'contests/:contestId/problems',
        meta:{
            name: 'ManageContest',
            requiredAuth: true
        },
        component: ManageContestProblemList
    },
]



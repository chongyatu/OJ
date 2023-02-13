import Layout from "@/views/Layout";

export default [
    {
        path: '/',
        meta:{
          title: 'TTOJ'
        },
        redirect: 'problems',
        component: Layout,
        children: [
            {
                path: 'problems',
                meta:{
                    title: '题库'
                },
                component: () => import('@/views/Problem/ProblemList/ProblemList'),
            },
        ]
    },
    {
        path: '/problems/:problemId',
        redirect: '',
        component: Layout,
        meta: {
            title: '题库'
        },
        children: [
            {
                path: '',
                redirect: '',
                component: () => import('@/views/Problem/ProblemDetail/ProblemDetail'),
                children: [
                    {
                        path: '',
                        name: 'ProblemDescription',
                        meta: {

                        },
                        component: () => import('@/views/Problem/ProblemDetail/ProblemDescription')
                    },
                ]
            },
        ]
    },
    {
        path: '/contest',
        redirect: '',
        component: Layout,
        meta:{
            title: '比赛'
        },
        children: [
            {
                path: '',
                component: () => import('@/views/Contest/ContestList/ContestList'),
            },
        ]
    },
    {
        path: '/contest/:contestId',
        redirect: '',
        component: Layout,
        meta: {
            title: '比赛'
        },
        children: [
            {
                path: '',
                redirect: 'contestOverview',
                component: () => import('@/views/Contest/ContestDetail/ContestDetail'),
                children: [
                    {
                        path: 'contestOverview',
                        name: 'contestOverview',
                        meta: {
                            title: '比赛说明',

                        },
                        component: () => import('@/views/Contest/ContestDetail/ContestOverview/ContestOverview'),
                    },
                    {
                        path: 'contestProblems',
                        name: 'contestProblems',
                        meta: {
                            title: '题目',
                        },
                        component: () => import('@/views/Contest/ContestDetail/ContestProblems/ContestProblems'),
                    },
                    {
                        path: 'contestRankings',
                        name: 'contestRankings',
                        meta: {
                            title: '排名',

                        },
                        component: () => import('@/views/Contest/ContestDetail/ContestRankings/ContestRankings'),
                    },
                    {
                        path: 'contestMy',
                        name: 'contestMy',
                        meta: {
                        },
                        component: () => import('@/views/Contest/ContestDetail/ContestMy/ContestMy'),
                    },
                    {
                        path: ':problemDisplayId',
                        name: 'contestProblem',
                        meta: {
                        },
                        component: () => import('@/views/Problem/ProblemDetail/ProblemDetail'),
                        redirect: '',
                        children: [
                            {
                                path: '',
                                meta: {

                                },
                                component: () => import('@/views/Problem/ProblemDetail/ProblemDescription')
                            },
                        ]
                    }
                ]
            }
        ]
    },
    {
        path: '/manage',
        redirect: '',
        component: Layout,
        meta: {
            title: '管理'
        },
        children: [
            {
                path: '',
                redirect: '',
                component: () => import('@/views/Manage/Manage'),
                name: 'dynamicRoutesParent',
                children: []
            },

        ]
    },
]


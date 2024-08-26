import Layout from "@/views/Layout";

export default [
    {
        path: '/',
        redirect: 'problems',
        component: Layout,
        children: [
            {
                path: 'problems',
                component: () => import('@/views/Problem/ProblemList/ProblemList'),
            },
        ]
    },
    {
        path: '/problems/:problemId',
        redirect: '',
        component: Layout,
        children: [
            {
                path: '',
                redirect: '',
                component: () => import('@/views/Problem/ProblemDetail/ProblemDetail'),
                children: [
                    {
                        path: '',
                        name: 'ProblemDescription',
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
        children: [
            {
                path: '',
                redirect: 'contestOverview',
                component: () => import('@/views/Contest/ContestDetail/ContestDetail'),
                children: [
                    {
                        path: 'contestOverview',
                        name: 'contestOverview',
                        component: () => import('@/views/Contest/ContestDetail/ContestOverview/ContestOverview'),
                    },
                    {
                        path: 'contestProblems',
                        name: 'contestProblems',
                        component: () => import('@/views/Contest/ContestDetail/ContestProblems/ContestProblems'),
                    },
                    {
                        path: 'contestRankings',
                        name: 'contestRankings',
                        component: () => import('@/views/Contest/ContestDetail/ContestRankings/ContestRankings'),
                    },
                    {
                        path: 'contestMy',
                        name: 'contestMy',
                        component: () => import('@/views/Contest/ContestDetail/ContestMy/ContestMy'),
                    },
                    {
                        path: ':problemDisplayId',
                        name: 'contestProblem',
                        component: () => import('@/views/Problem/ProblemDetail/ProblemDetail'),
                        redirect: '',
                        children: [
                            {
                                path: '',
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


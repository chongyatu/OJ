export default {
    /**
     * @description 配置显示在浏览器标签的title
     */
    title: 'TTOJ',
    /**
     * @description token在Cookie中存储的天数，默认1天
     */
    cookieExpires: 1,
    /**
     * @description 是否使用国际化，默认为false
     *              如果不使用，则需要在路由中给需要在菜单中展示的路由设置meta: {title: 'xxx'}
     *              用来在菜单中显示文字
     */
    useI18n: false,
    /**
     * @description api请求基础路径
     */
    baseUrl: "http://localhost:9991/",
    /**
     * @description 需要加载的插件
     */
    plugin: {

    },
    /*
    支持的语言
     */
    supportLanguages: [
        {value: 'c', title: 'C'},
        {value: 'cpp', title: 'C++'},
        {value: 'java', title: 'Java'},
        {value: 'python2', title: 'Python2'},
        {value: 'python3', title: 'Python3'},
        {value: 'javascript', title: 'Javascript'},
        {value: 'go', title: 'Golang'}
        ],
    /*
    判题结果
     */
    judgeResult:{
        'Accepted':{title: '答案正确', explain: '答案正确:恭喜！您提交的程序通过了所有的测试用例',color: '#32CA99'},
        'WrongAnswer':{title: '答案错误', explain: '答案错误:您的程序没有通过所有的测试用例',color: '#EA0E07'},
        'TimeLimitExceeded':{title: '运行超时', explain: '您的程序未能在规定时间内运行结束',color: '#EA0E07'},
        'RealTimeLimitExceeded':{title: '输出时间超限', explain: '您的程序未能在规定时间内运行结束',color: '#EA0E07'},
        'MemoryLimitExceeded':{title: '内存超限', explain: '您的程序使用内存过多',color: '#EA0E07'},
        'RuntimeError':{title: '运行错误', explain: '您的程序发生段错误',color: '#EA0E07'},
        'SystemError':{title: '内部错误', explain: '',color: '#EA0E07'},
        'CompileError':{title: '编译错误', explain: '',color: '#EA0E07'}
    },
    /*
    比赛界面固定标签页
     */
    commonTagName: [
        'contestOverview',
        'contestRankings',
        'contestMy',
        'contestSubmissions',
        'contestProblems'
    ],
    /*
    特殊角色
     */
    roles: ['题目创作者', '比赛举办方', '管理员'],
    /*
    需要控制宽度的路由
     */
    /*
    最近比赛的天数
     */
    recentContestDays: 3,

    /*
    各种提示信息
     */
    msg:{
        timeout: '请求超时'
    },
    /**
     * 难度的颜色
     */
    level:[
        '入门',
        '简单',
        '中等',
        '困难',
        '极难',
    ],
    levelColor:{
        '入门': '#03a89e',
        '简单': '#2db55d',
        '中等': '#e1b800',
        '困难': '#ef4743',
        '极难': '#cc3333',
    },
    /**
     * tooltip提示信息
     */
    hint:{
        // 比赛出题人
        contestAuthor: '将某个用户设为比赛出题人后,该用户自动拥有题目创作者身份\n' +
                       '同时可以管理该比赛,在比赛中的提交也不会被普通用户看到,也不进入排名'
    },
    /**
     * 提示信息颜色
     */
    msgColor:{
        error: 'f56c6c',
        success: '#e1f3d8',
        warn: '#e6a23c',
        info: '#909399'
    }
}

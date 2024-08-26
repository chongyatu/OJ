import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'

Vue.config.productionTip = false

/*
全局样式
*/
import '@/css/style.css'
import '@/css/view.css';
import '@/css/normalize.css';

/*
element ui
 */
import ELEMENT from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'element-ui/lib/theme-chalk/display.css';

Vue.use(ELEMENT)　　//注意大写

/*
markdown 编辑器
 */
import MavonEditor from 'mavon-editor' // components
import 'mavon-editor/dist/css/index.css' // styles
Vue.use(MavonEditor)

/*
codemirror
 */
import VueCodemirror from 'vue-codemirror'

// require active-line.js
import 'codemirror/addon/selection/active-line.js'
//theme
import 'codemirror/theme/idea.css'
// mode
import 'codemirror/mode/clike/clike'
import 'codemirror/mode/python/python'
// edit
import 'codemirror/addon/edit/matchbrackets.js';
import 'codemirror/addon/edit/closebrackets.js';
// base style
import 'codemirror/lib/codemirror.css'

Vue.use(VueCodemirror)

import config from "@/config"

Vue.prototype.$config = config


import axios from "axios"
import VueAxios from "vue-axios"

Vue.use(VueAxios, axios)

Vue.prototype.notify = (success, message) => {
    if(success){
        ELEMENT.Notification.success({
            title: '请求成功',
            message,
            duration: 2000
        });
    }else{
        ELEMENT.Notification.error({
            title: '请求失败',
            message,
            duration: 3000
        });
    }
}

Vue.prototype.errorNotify = (message) => {
    ELEMENT.Notification.error({
        title: '失败',
        message,
        duration: 3000
    });
}

Vue.prototype.successNotify = (message) => {
    ELEMENT.Notification.success({
        title: '成功',
        message,
        duration: 1000
    });
}

Vue.prototype.warnNotify = (message) => {
    ELEMENT.Notification.warning({
        title: '警告',
        message,
        duration: 2000
    });
}

Vue.prototype.infoNotify = (message) => {
    ELEMENT.Notification.info({
        title: '消息',
        message,
        duration: 1000
    });
}

new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
})

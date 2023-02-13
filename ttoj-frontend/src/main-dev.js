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
import Codemirror from 'codemirror'

// require active-line.js
import 'codemirror/addon/selection/active-line.js'
// styleSelectedText
import 'codemirror/addon/selection/mark-selection.js'
// hint
import 'codemirror/addon/hint/anyword-hint.js';
import 'codemirror/addon/hint/show-hint.js'
import 'codemirror/addon/hint/show-hint.css';
// import 'codemirror/addon/hint/javascript-hint.js'

// highlightSelectionMatches
import 'codemirror/addon/scroll/annotatescrollbar.js';
import 'codemirror/addon/search/matchesonscrollbar.js';
import 'codemirror/addon/dialog/dialog.js';
import 'codemirror/addon/dialog/dialog.css';
import 'codemirror/addon/search/searchcursor.js';
import 'codemirror/addon/search/search.js';
import 'codemirror/addon/search/match-highlighter.js';

//theme
import 'codemirror/theme/idea.css'
// mode
import 'codemirror/mode/javascript/javascript'
import 'codemirror/mode/clike/clike'
import 'codemirror/mode/go/go'
import 'codemirror/mode/python/python'
import 'codemirror/mode/clike/clike.js'
import 'codemirror/addon/comment/comment.js'
// keyMap
import 'codemirror/keymap/sublime.js'
// foldGutter
import 'codemirror/addon/fold/foldgutter.css';
import 'codemirror/addon/fold/foldgutter.js';
import 'codemirror/addon/fold/brace-fold.js';
import 'codemirror/addon/fold/indent-fold.js';
import 'codemirror/addon/fold/comment-fold.js'
import 'codemirror/addon/fold/foldcode.js'
// edit
import 'codemirror/addon/edit/matchbrackets.js';
import 'codemirror/addon/edit/matchtags.js';
import 'codemirror/addon/edit/closetag.js';
import 'codemirror/addon/edit/closebrackets.js';


//  base style
import 'codemirror/lib/codemirror.css'

Vue.use(VueCodemirror, /* {
  options: { theme: 'base16-dark', ... },
  events: ['scroll', ...]
} */)

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

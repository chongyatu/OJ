import Vue from "vue"
import routes from './static-router'
import VueRouter from "vue-router";

Vue.use(VueRouter)

// ?
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
    routes,
    mode: 'history',
    scrollBehavior (to, from, savedPosition) {
        console.log(to, from);
        return savedPosition || { x: 0, y: 0 }
    },
})

// 加载动画
router.beforeEach((to, from, next) => {
    next()
});

router.afterEach(route => {
});

export default router

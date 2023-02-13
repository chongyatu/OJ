import types from '@/store/types'
import storage from "@/utils/storage";
import Vue from 'vue'

export default {
    [types.Set_LoginUser](state, loginUser){
        Vue.set(state,'user', loginUser.user)
        Vue.set(state,'permissions', loginUser.permissions)
        Vue.set(state,'manageRouter', loginUser.manageRouters)
    },
    [types.Clear_LoginUser](state){
        Vue.set(state,'user', null)
        Vue.set(state,'permissions', null)
        Vue.set(state,'manageRouter', null)
        storage.remove("token")
    },
}

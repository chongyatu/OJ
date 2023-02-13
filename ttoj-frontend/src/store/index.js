import Vue from "vue";
import Vuex from "vuex"
import mutations from "./mutations";
import actions from "./actions";
import getters from "./getters";
import contest from "@/store/modules/contest";

Vue.use(Vuex)

const state = {
    user:{},
}

export default new Vuex.Store({
    state,
    getters,
    mutations,
    actions,
    modules:{
        contest
    }
})


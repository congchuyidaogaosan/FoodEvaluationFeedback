import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    username: localStorage.getItem('username') || ''
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    SET_USERNAME(state, username) {
      state.username = username
      localStorage.setItem('username', username)
    },
    CLEAR_USER(state) {
      state.token = ''
      state.username = ''
      localStorage.removeItem('token')
      localStorage.removeItem('username')
    }
  },
  actions: {
    login({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        // 这里调用登录API
        commit('SET_TOKEN', userInfo.token)
        commit('SET_USERNAME', userInfo.username)
        resolve()
      })
    },
    logout({ commit }) {
      return new Promise((resolve) => {
        commit('CLEAR_USER')
        resolve()
      })
    }
  },
  getters: {
    isLoggedIn: state => !!state.token
  }
}) 
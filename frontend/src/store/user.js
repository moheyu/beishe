import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: null
  }),
  getters: {
    isAuthenticated: (state) => !!state.token
  },
  actions: {
    setToken(newToken) {
      this.token = newToken
    },
    setUserInfo(info) {
      this.userInfo = info
    },
    logout() {
      this.token = ''
      this.userInfo = null
    }
  },
  persist: {
    key: 'travel-user',
    storage: localStorage,
    paths: ['token', 'userInfo']
  }
})

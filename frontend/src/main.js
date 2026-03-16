import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store' // 导入 store 实例
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'normalize.css/normalize.css'

// 注册 ElementUI
Vue.use(ElementUI)
Vue.config.productionTip = false

// 挂载 Vuex 到 Vue 实例
new Vue({
  router,
  store, // 替代原有的 pinia
  render: h => h(App)
}).$mount('#app')
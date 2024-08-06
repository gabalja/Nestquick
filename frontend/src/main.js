import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedState from 'pinia-plugin-persistedstate'

// 뷰티파이 기본 세팅
// https://vuetifyjs.com/en/getting-started/installation/#using-nuxt-3
// Using Laravel Mix
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

const vuetify = createVuetify({
  components,
  directives
})

//부트스트랩 세팅
import 'bootstrap/dist/css/bootstrap.min.css'
import "bootstrap"

// axios 객체 사용
import { instance } from "./api/axios"  // axios.js에서 export 해놨기 때문에 instance 적을 수 있다

import App from './App.vue'
import router from './router'

const app = createApp(App)

// axios instance를 axios를 적으면 사용할 수 있도록 함
app.provide('axios', instance)  // axios를 적으면 instance(axios)가 제공된다~

app.use(createPinia().use(piniaPluginPersistedState))
app.use(router)
app.use(vuetify)

app.mount('#app')

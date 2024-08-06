import { createRouter, createWebHistory } from 'vue-router'
import MainView from '../views/MainView.vue'
import SaleView from '../views/SaleView.vue'
import SaleRegistComponent from '../components/sale/RegistComponent.vue'
import SaleMapComponent from '../components/sale/MapComponent.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',  // 경로
      name: 'main', // 이름
      component: MainView // 컴포넌트
    },{
      path: '/sale',
      name: 'sale',
      component: SaleView,
      children: [
        {
          path: 'regist',
          name: 'saleRegist', // 이름
          component: SaleRegistComponent // 컴포넌트
        },{
          path: 'map',
          name: 'saleMap', // 이름
          component: SaleMapComponent // 컴포넌트
        }
      ]
    },
  ]
})

export default router

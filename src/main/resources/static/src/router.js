import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      component: () => import('@/views/dashboard/Index'),
      children: [
        // Dashboard
        {
          name: '과제',
          path: '',
          component: () => import('@/views/dashboard/Dashboard'),
        },
        // Brand
        {
          name: '브랜드 관리',
          path: '/brands',
          component: () => import('@/views/dashboard/pages/Brands'),
        },
        // Product
        {
          name: '상품 관리',
          path: '/Products',
          component: () => import('@/views/dashboard/pages/Products'),
        }
      ],
    },
  ],
})

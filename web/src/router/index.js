import { createRouter, createWebHistory } from 'vue-router'
import store from "@/store";
import {notification} from "ant-design-vue";

const routes = [
  {
    path: '/login',
    component: () => import('../views/login.vue')
  }, {
    path: '/',
    component: () => import('../views/main.vue'),
    meta: {
      loginRequire: true
    },
    children: [{
      path: 'welcome',
      component: () => import('../views/main/welcome.vue'),
    }, {
      path: 'passenger',
      component: () => import('../views/main/passenger.vue'),
    }, {
      path: 'ticket',
      component: () => import('../views/main/ticket.vue'),
    }, {
      path: 'order',
      component: () => import('../views/main/order.vue'),
    }, {
      path: 'my-ticket',
      component: () => import('../views/main/my-ticket.vue'),
    }]
  }, {
    path: '',
    redirect: '/welcome'
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(function (item) {
    console.log(item, "Need to validate or not: ", item.meta.loginRequire || false);
    return item.meta.loginRequire;
  })) {
    const _member = store.state.member;
    console.log("Validation starts: ", _member);
    if (!_member.token) {
      console.log("Did not login or login expires");
      notification.error({ description: "Did not login or login expires" });
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router

import { createRouter, createWebHistory } from 'vue-router'
import store from "@/store";
import {notification} from "ant-design-vue";

const routes = [
  {
    path: '/login',
    component: () => import('../views/login.vue')
  },
  {
    path: '/',
    component: () => import('../views/main.vue'),
    meta: {
      loginRequire: true
    },
  }
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

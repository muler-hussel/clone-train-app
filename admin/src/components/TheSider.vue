<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        :open-keys="['batch', 'base', 'business', 'member']"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined /> &nbsp; Welcome
        </router-link>
      </a-menu-item>
      <a-sub-menu key="batch">
        <template #title>
          <span>
            <UnorderedListOutlined />
            Schedule Job
          </span>
        </template>
        <a-menu-item key="/batch/job">
          <router-link to="/batch/job">
            <MenuUnfoldOutlined /> &nbsp; Assignment
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="member">
        <template #title>
          <span>
            <UnorderedListOutlined />
            Membership Management
          </span>
        </template>
        <a-menu-item key="/member/ticket">
          <router-link to="/member/ticket">
            <MenuUnfoldOutlined /> Member Ticket
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="base">
        <template #title>
          <span>
            <UnorderedListOutlined />
            Basic Data
          </span>
        </template>
        <a-menu-item key="/base/station">
          <router-link to="/base/station">
            <user-outlined /> &nbsp; Station
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train">
          <router-link to="/base/train">
            <user-outlined /> &nbsp; Train
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-station">
          <router-link to="/base/train-station">
            <user-outlined /> &nbsp; Train Station
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-carriage">
          <router-link to="/base/train-carriage">
            <user-outlined /> &nbsp; Train Carriage
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-seat">
          <router-link to="/base/train-seat">
            <user-outlined /> &nbsp; Train Seat
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="business">
        <template #title>
          <span>
            <UnorderedListOutlined />
            Daily Data
          </span>
        </template>
        <a-menu-item key="/business/daily-train-ticket">
          <router-link to="/business/daily-train-ticket">
            <user-outlined /> &nbsp; Ticket Stock
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/sk-token">
          <router-link to="/business/sk-token">
            <user-outlined /> &nbsp; Tokens
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/confirm-order">
          <router-link to="/business/confirm-order">
            <user-outlined /> &nbsp; Order
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train">
          <router-link to="/business/daily-train">
            <user-outlined /> &nbsp; Daily Train
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-station">
          <router-link to="/business/daily-train-station">
            <user-outlined /> &nbsp; Daily Train Station
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-carriage">
          <router-link to="/business/daily-train-carriage">
            <user-outlined /> &nbsp; Daily Train Carriage
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-seat">
          <router-link to="/business/daily-train-seat">
            <user-outlined /> &nbsp; Daily Train Seat
          </router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import router from "@/router";

export default defineComponent({
  name: "the-sider-view",

  setup(){
    const selectedKeys = ref([]);
    watch(() => router.currentRoute.value.path, (newValue) => {
      console.log('watch', newValue);
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
    }, {immediate: true});
    return {
      selectedKeys,
    };
  },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>

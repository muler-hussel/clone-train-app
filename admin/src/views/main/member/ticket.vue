<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">Refresh</a-button>
      
    </a-space>
  </p>
  <a-table :dataSource="tickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
      </template>
      <template v-else-if="column.dataIndex === 'seatCol'">
        <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatCol && item.type === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";

export default defineComponent({
  name: "ticket-view",
  setup() {
    const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const visible = ref(false);
    let ticket = ref({
      id: undefined,
      memberId: undefined,
      passengerId: undefined,
      passengerName: undefined,
      trainDate: undefined,
      trainCode: undefined,
      carriageIndex: undefined,
      seatRow: undefined,
      seatCol: undefined,
      departure: undefined,
      departureTime: undefined,
      destination: undefined,
      arrivalTime: undefined,
      seatType: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const tickets = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = [
    {
      title: 'MemberId',
      dataIndex: 'memberId',
      key: 'memberId',
    },
    {
      title: 'PassengerId',
      dataIndex: 'passengerId',
      key: 'passengerId',
    },
    {
      title: 'PassengerName',
      dataIndex: 'passengerName',
      key: 'passengerName',
    },
    {
      title: 'TrainDate',
      dataIndex: 'trainDate',
      key: 'trainDate',
    },
    {
      title: 'TrainCode',
      dataIndex: 'trainCode',
      key: 'trainCode',
    },
    {
      title: 'CarriageIndex',
      dataIndex: 'carriageIndex',
      key: 'carriageIndex',
    },
    {
      title: 'SeatRow',
      dataIndex: 'seatRow',
      key: 'seatRow',
    },
    {
      title: 'SeatCol',
      dataIndex: 'seatCol',
      key: 'seatCol',
    },
    {
      title: 'Departure',
      dataIndex: 'departure',
      key: 'departure',
    },
    {
      title: 'DepartureTime',
      dataIndex: 'departureTime',
      key: 'departureTime',
    },
    {
      title: 'Destination',
      dataIndex: 'destination',
      key: 'destination',
    },
    {
      title: 'ArrivalTime',
      dataIndex: 'arrivalTime',
      key: 'arrivalTime',
    },
    {
      title: 'SeatType',
      dataIndex: 'seatType',
      key: 'seatType',
    },
    ];


    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/member/admin/ticket/query-list", {
        params: {
          page: param.page,
          size: param.size
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          tickets.value = data.content.list;
          // 设置分页控件的值
          pagination.value.current = param.page;
          pagination.value.total = data.content.total;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleTableChange = (page) => {
      // console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
      pagination.value.pageSize = page.pageSize;
      handleQuery({
        page: page.current,
        size: page.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      ticket,
      visible,
      tickets,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      SEAT_COL_ARRAY,
      SEAT_TYPE_ARRAY
    };
  },
});
</script>

<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" value-format="YYYY-MM-DD" placeholder="Select date" :disabled-date="disabledDate" />
      <station-select-view v-model="params.start" width="200px"></station-select-view>
      <station-select-view v-model="params.end" width="200px"></station-select-view>
      <a-button type="primary" @click="handleQuery()">Search</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainTickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-button type="primary" @click="toOrder(record)" :disabled="isExpire(record)">{{isExpire(record) ? "Out Of Date" : "Order"}}</a-button>
          <a-button type="primary" @click="showStation(record)">All Stations</a-button>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'station'">
        {{record.start}}<br/>
        {{record.end}}
      </template>
      <template v-else-if="column.dataIndex === 'time'">
        {{record.startTime}}<br/>
        {{record.endTime}}
      </template>
      <template v-else-if="column.dataIndex === 'duration'">
        {{calDuration(record.startTime, record.endTime)}}<br/>
        <div v-if="record.startTime.replaceAll(':', '') >= record.endTime.replaceAll(':', '')">
          Next-day Arrival
        </div>
        <div v-else>
          Same-day Arrival
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'fc'">
        <div v-if="record.fc >= 0">
          {{record.fc}}<br/>
          {{record.fcPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'sc'">
        <div v-if="record.sc >= 0">
          {{record.sc}}<br/>
          {{record.scPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'ss'">
        <div v-if="record.ss >= 0">
          {{record.ss}}<br/>
          {{record.ssPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'hs'">
        <div v-if="record.hs >= 0">
          {{record.hs}}<br/>
          {{record.hsPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
    </template>
  </a-table>
<!--途经车站-->
  <a-modal style="top: 30px" v-model:visible="visible" :title="null" :footer="null" closable="false">
    <a-table :data-source="stations" :pagination="false">
      <a-table-column key="index" title="index" data-index="index" />
      <a-table-column key="name" title="name" data-index="name" />
      <a-table-column key="arriveTime" title="arrive time" data-index="inTime">
        <template #default="{ record }">
          {{record.index === 0 ? '-' : record.inTime}}
        </template>
      </a-table-column>
      <a-table-column key="departTime" title="depart time" data-index="outTime">
        <template #default="{ record }">
          {{record.index === (stations.length - 1) ? '-' : record.outTime}}
        </template>
      </a-table-column>
      <a-table-column key="stopTime" title="stop time" data-index="stopTime">
        <template #default="{ record }">
          {{record.index === (stations.length - 1) || record.index === 0 ? '-' : record.stopTime}}
        </template>
      </a-table-column>
    </a-table>

  </a-modal>
</template>

<script>
import {defineComponent, ref, onMounted, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import StationSelectView from "@/components/StationSelect.vue";
import dayjs from "dayjs";
import router from "@/router";

export default defineComponent({
  name: "ticket-view",
  components: {StationSelectView},
  setup() {
    const visible = ref(false);
    let dailyTrainTicket = ref({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      startIndex: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      endIndex: undefined,
      fc: undefined,
      fcPrice: undefined,
      sc: undefined,
      scPrice: undefined,
      ss: undefined,
      ssPrice: undefined,
      hs: undefined,
      hsPrice: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrainTickets = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({});
    const columns = [
    {
      title: 'TrainCode',
      dataIndex: 'trainCode',
      key: 'trainCode',
    },
    {
      title: 'Station',
      dataIndex: 'station',
    },
    {
      title: 'Time',
      dataIndex: 'time',
    },
    {
      title: 'Duration',
      dataIndex: 'duration',
    },
    {
      title: 'First Class',
      dataIndex: 'fc',
      key: 'fc',
    },
    {
      title: 'Second Class',
      dataIndex: 'sc',
      key: 'sc',
    },
    {
      title: 'Soft Sleeping',
      dataIndex: 'ss',
      key: 'ss',
    },
    {
      title: 'Hard Sleeping',
      dataIndex: 'hs',
      key: 'hs',
    },
    {
      title: 'Operation',
      dataIndex: 'operation',
    },
    ];


    const handleQuery = (param) => {
      if (Tool.isEmpty(params.value.date)) {
        notification.error({ description: "Please select date."});
        return;
      }
      if (Tool.isEmpty(params.value.start)) {
        notification.error({ description: "Please select departure station."});
        return;
      }
      if (Tool.isEmpty(params.value.end)) {
        notification.error({ description: "Please select destination."});
        return;
      }
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }

      SessionStorage.set(SESSION_TICKET_PARAMS, params.value);
      loading.value = true;
      axios.get("/business/daily-train-ticket/query-list", {
        params: {
          page: param.page,
          size: param.size,
          trainCode: params.value.trainCode,
          date: params.value.date,
          start: params.value.start,
          end: params.value.end
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          dailyTrainTickets.value = data.content.list;
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

    const calDuration = (startTime, endTime) => {
      let diff = dayjs(endTime, 'HH:mm:ss').diff(dayjs(startTime, 'HH:mm:ss'), 'seconds');
      return dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
    };

    const toOrder = (record) => {
      dailyTrainTicket.value = Tool.copy(record);
      SessionStorage.set(SESSION_ORDER, dailyTrainTicket.value);
      router.push("/order");
    }

    const stations = ref([]);
    const showStation = (record) => {
      visible.value = true;
      axios.get("/business/daily-train-station/query-by-train-code", {
        params: {
          date: record.date,
          trainCode: record.trainCode
        }
      }).then((response) => {
        let data = response.data;
        if (data.success) {
          stations.value = data.content;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const disabledDate = current => {
      return current && (current <= dayjs().add(-1, 'day') || current > dayjs().add(14, 'day'));
    };

    onMounted(() => {
      params.value = SessionStorage.get(SESSION_TICKET_PARAMS) || {};
      if (Tool.isNotEmpty(params.value)) {
        handleQuery({
          page: 1,
          size: pagination.value.pageSize
        });
      }
    });

    const isExpire = (record) => {
      //标准时间 2000/01/01 00：00：00
      let startDateTimeString = record.date.replace(/-/g, "/") + " " + record.startTime;
      let startDateTime = new Date(startDateTimeString);

      let now = new Date();
      return now.valueOf() >= startDateTime.valueOf();
    }

    return {
      dailyTrainTicket,
      visible,
      dailyTrainTickets,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      params,
      calDuration,
      toOrder,
      showStation,
      stations,
      disabledDate,
      isExpire
    };
  },
});
</script>

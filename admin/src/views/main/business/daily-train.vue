<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="Select date" />
      <train-select-view v-model="params.code" width="200px"></train-select-view>
      <a-button type="primary" @click="handleQuery()">Search</a-button>
      <a-button type="primary" @click="onAdd">New</a-button>
      <a-button type="primary" @click="onClickGenDaily">Generate</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrains"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="Data is unrecoverable, make sure to delete"
              @confirm="onDelete(record)">
            <a style="color: red">Delete</a>
          </a-popconfirm>
          <a @click="onEdit(record)">Edit</a>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'type'">
        <span v-for="item in TRAIN_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.type">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="DailyTrain" @ok="handleOk">
    <a-form :model="dailyTrain" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="Date">
        <a-date-picker v-model:value="dailyTrain.date" valueFormat="YYYY-MM-DD" placeholder="Select date" />
      </a-form-item>
      <a-form-item label="Code">
        <train-select-view v-model="dailyTrain.code" @change="onChangeCode"></train-select-view>
      </a-form-item>
      <a-form-item label="Type">
        <a-select v-model:value="dailyTrain.type">
          <a-select-option v-for="item in TRAIN_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="Departure">
        <a-input v-model:value="dailyTrain.start" />
      </a-form-item>
      <a-form-item label="DeparturePinyin">
        <a-input v-model:value="dailyTrain.startPinyin" />
      </a-form-item>
      <a-form-item label="Departureime">
        <a-time-picker v-model:value="dailyTrain.startTime" valueFormat="HH:mm:ss" placeholder="Select time" />
      </a-form-item>
      <a-form-item label="Arrival">
        <a-input v-model:value="dailyTrain.end" />
      </a-form-item>
      <a-form-item label="ArrivalPinyin">
        <a-input v-model:value="dailyTrain.endPinyin" />
      </a-form-item>
      <a-form-item label="ArrivalTime">
        <a-time-picker v-model:value="dailyTrain.endTime" valueFormat="HH:mm:ss" placeholder="Select time" />
      </a-form-item>
    </a-form>
  </a-modal>
  <a-modal v-model:visible="genDailyVisible" title="Generate train" @ok="handleGenDailyOk" :confirm-loading="genDailyLoading">
    <a-form :model="genDaily" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
      <a-form-item label="Date">
        <a-date-picker v-model:value="genDaily.date" placeholder="Select date" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectView from "@/components/TrainSelect.vue";
import dayjs from "dayjs";

export default defineComponent({
  name: "daily-train-view",
  components: {TrainSelectView},
  setup() {
    const TRAIN_TYPE_ARRAY = window.TRAIN_TYPE_ARRAY;
    const visible = ref(false);
    let dailyTrain = ref({
      id: undefined,
      date: undefined,
      code: undefined,
      type: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrains = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      code: null
    })
    const genDaily = ref({
      date: null
    });
    const genDailyVisible = ref(false);
    const genDailyLoading = ref(false);
    const columns = [
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: 'Code',
      dataIndex: 'code',
      key: 'code',
    },
    {
      title: 'Type',
      dataIndex: 'type',
      key: 'type',
    },
    {
      title: 'Departure',
      dataIndex: 'start',
      key: 'start',
    },
    {
      title: 'DeparturePinyin',
      dataIndex: 'startPinyin',
      key: 'startPinyin',
    },
    {
      title: 'DepartureTime',
      dataIndex: 'startTime',
      key: 'startTime',
    },
    {
      title: 'Arrival',
      dataIndex: 'end',
      key: 'end',
    },
    {
      title: 'ArrivalPinyin',
      dataIndex: 'endPinyin',
      key: 'endPinyin',
    },
    {
      title: 'ArrivalTime',
      dataIndex: 'endTime',
      key: 'endTime',
    },
    {
      title: 'Operation',
      dataIndex: 'operation'
    }
    ];

    const onAdd = () => {
      dailyTrain.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      dailyTrain.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/daily-train/delete/" + record.id).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({description: "Delete successfully！"});
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleOk = () => {
      axios.post("/business/admin/daily-train/save", dailyTrain.value).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({description: "Save successfully"});
          visible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/business/admin/daily-train/query-list", {
        params: {
          page: param.page,
          size: param.size,
          code: params.value.code,
          date: params.value.date,
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          dailyTrains.value = data.content.list;
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

    const onChangeCode = (train) => {
      console.log("log: ", train);
      let t = Tool.copy(train);
      delete t.id;
      dailyTrain.value = Object.assign(dailyTrain.value, t);
    };

    const onClickGenDaily = () => {
      genDailyVisible.value = true;
    }

    const handleGenDailyOk = () => {
      let date = dayjs(genDaily.value.date).format("YYYY-MM-DD");
      genDailyLoading.value = true;
      axios.get("/business/admin/daily-train/gen-daily/" + date).then((response) => {
        genDailyLoading.value = false;
        let data = response.data;
        if (data.success) {
          notification.success({ description: "Generate successfully" });
          genDailyVisible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      TRAIN_TYPE_ARRAY,
      dailyTrain,
      visible,
      dailyTrains,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      onChangeCode,
      params,
      onClickGenDaily,
      genDaily,
      genDailyVisible,
      handleGenDailyOk,
      genDailyLoading
    };
  },
});
</script>

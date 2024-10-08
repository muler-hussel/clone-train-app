<template>
  <p>
    <a-space>
      <train-select-view v-model="params.trainCode" width="200px"></train-select-view>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="Select date" />
      <a-button type="primary" @click="handleQuery()">Search</a-button>
      <a-button type="primary" @click="onAdd">New</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainStations"
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
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="DailyTrainStation" @ok="handleOk">
    <a-form :model="dailyTrainStation" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="Date">
        <a-date-picker v-model:value="dailyTrainStation.date" valueFormat="YYYY-MM-DD" placeholder="Select date" />
      </a-form-item>
      <a-form-item label="TrainCode">
        <train-select-view v-model:value="dailyTrainStation.trainCode"></train-select-view>
      </a-form-item>
      <a-form-item label="Index">
        <a-input v-model:value="dailyTrainStation.index" />
      </a-form-item>
      <a-form-item label="Station">
        <station-select-view v-model:value="dailyTrainStation.name"></station-select-view>
      </a-form-item>
      <a-form-item label="StationPinyin">
        <a-input v-model:value="dailyTrainStation.namePinyin" disabled/>
      </a-form-item>
      <a-form-item label="InTime">
        <a-time-picker v-model:value="dailyTrainStation.inTime" valueFormat="HH:mm:ss" placeholder="Select time" />
      </a-form-item>
      <a-form-item label="OutTime">
        <a-time-picker v-model:value="dailyTrainStation.outTime" valueFormat="HH:mm:ss" placeholder="Select time" />
      </a-form-item>
      <a-form-item label="StopTime">
        <a-time-picker v-model:value="dailyTrainStation.stopTime" valueFormat="HH:mm:ss" placeholder="Select time" disabled/>
      </a-form-item>
      <a-form-item label="Mileage">
        <a-input v-model:value="dailyTrainStation.mileage" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, ref, onMounted, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectView from "@/components/TrainSelect.vue";
import dayjs from "dayjs";
import {pinyin} from "pinyin-pro";
import StationSelectView from "@/components/StationSelect.vue";

export default defineComponent({
  name: "daily-train-station-view",
  components: {StationSelectView, TrainSelectView},
  setup() {
    const visible = ref(false);
    let dailyTrainStation = ref({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      index: undefined,
      name: undefined,
      namePinyin: undefined,
      inTime: undefined,
      outTime: undefined,
      stopTime: undefined,
      mileage: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrainStations = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      trainCode: null,
      date: null
    });

    const columns = [
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: 'TrainCode',
      dataIndex: 'trainCode',
      key: 'trainCode',
    },
    {
      title: 'Index',
      dataIndex: 'index',
      key: 'index',
    },
    {
      title: 'Station',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'StationPinyin',
      dataIndex: 'namePinyin',
      key: 'namePinyin',
    },
    {
      title: 'InTime',
      dataIndex: 'inTime',
      key: 'inTime',
    },
    {
      title: 'OutTime',
      dataIndex: 'outTime',
      key: 'outTime',
    },
    {
      title: 'StopTime',
      dataIndex: 'stopTime',
      key: 'stopTime',
    },
    {
      title: 'Mileage',
      dataIndex: 'mileage',
      key: 'mileage',
    },
    {
      title: 'Operation',
      dataIndex: 'operation'
    }
    ];

    watch(() => dailyTrainStation.value.name, () => {
      if (Tool.isNotEmpty(dailyTrainStation.value.name)) {
        dailyTrainStation.value.namePinyin = pinyin(dailyTrainStation.value.name, { toneType: 'none'} ).replaceAll(" ", "");
      } else {
        dailyTrainStation.value.namePinyin = "";
      }
    }, {immediate: true});

    watch(() => dailyTrainStation.value.inTime, () => {
      let diff = dayjs(dailyTrainStation.value.outTime, 'HH:mm:ss').diff(dayjs(dailyTrainStation.value.inTime, 'HH:mm:ss'), 'seconds');
      dailyTrainStation.value.stopTime = dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
    }, {immediate: true});

    watch(() => dailyTrainStation.value.outTime, () => {
      let diff = dayjs(dailyTrainStation.value.outTime, 'HH:mm:ss').diff(dayjs(dailyTrainStation.value.inTime, 'HH:mm:ss'), 'seconds');
      dailyTrainStation.value.stopTime = dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
    }, {immediate: true});

    const onAdd = () => {
      dailyTrainStation.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      dailyTrainStation.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/daily-train-station/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/daily-train-station/save", dailyTrainStation.value).then((response) => {
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
      axios.get("/business/admin/daily-train-station/query-list", {
        params: {
          page: param.page,
          size: param.size,
          trainCode: params.value.trainCode,
          date: params.value.date
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          dailyTrainStations.value = data.content.list;
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
      dailyTrainStation,
      visible,
      dailyTrainStations,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      params
    };
  },
});
</script>

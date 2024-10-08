<template>
  <p>
    <a-space>
      <train-select-view v-model="params.trainCode" width="200px"></train-select-view>
      <a-button type="primary" @click="handleQuery()">Search</a-button>
      <a-button type="primary" @click="onAdd">New</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainStations"
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
  <a-modal v-model:visible="visible" title="TrainStation" @ok="handleOk">
    <a-form :model="trainStation" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="TrainCode">
        <train-select-view v-model="trainStation.trainCode"></train-select-view>
      </a-form-item>
      <a-form-item label="Index">
        <a-input v-model:value="trainStation.index" />
      </a-form-item>
      <a-form-item label="Station">
        <station-select-view v-model:value="trainStation.name"></station-select-view>
      </a-form-item>
      <a-form-item label="StationPinyin">
        <a-input v-model:value="trainStation.namePinyin" disabled/>
      </a-form-item>
      <a-form-item label="InTime">
        <a-time-picker v-model:value="trainStation.inTime" valueFormat="HH:mm:ss" placeholder="Select time" />
      </a-form-item>
      <a-form-item label="OutTime">
        <a-time-picker v-model:value="trainStation.outTime" valueFormat="HH:mm:ss" placeholder="Select time" />
      </a-form-item>
      <a-form-item label="StopTime">
        <a-time-picker v-model:value="trainStation.stopTime" valueFormat="HH:mm:ss" placeholder="Select time" disabled/>
      </a-form-item>
      <a-form-item label="Mileage">
        <a-input v-model:value="trainStation.mileage" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, ref, onMounted, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import {pinyin} from "pinyin-pro";
import TrainSelectView from "@/components/TrainSelect"
import StationSelectView from "@/components/StationSelect";
import dayjs from 'dayjs';

export default defineComponent({
  name: "train-station-view",
  components: {StationSelectView, TrainSelectView},
  setup() {
    const visible = ref(false);
    let trainStation = ref({
      id: undefined,
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
    const trainStations = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      trainCode: null
    });

    const columns = [
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

    watch(() => trainStation.value.name, () => {
      if (Tool.isNotEmpty(trainStation.value.name)) {
        trainStation.value.namePinyin = pinyin(trainStation.value.name, { toneType: 'none'} ).replaceAll(" ", "");
      } else {
        trainStation.value.namePinyin = "";
      }
    }, {immediate: true});

    watch(() => trainStation.value.inTime, () => {
      let diff = dayjs(trainStation.value.outTime, 'HH:mm:ss')
          .diff(dayjs(trainStation.value.inTime, 'HH:mm:ss'), 'seconds');
      trainStation.value.stopTime = dayjs('00:00:00', 'HH:mm:ss')
          .second(diff)
          .format('HH:mm:ss');
    }, {immediate: true});

    watch(() => trainStation.value.outTime, () => {
      let diff = dayjs(trainStation.value.outTime, 'HH:mm:ss')
          .diff(dayjs(trainStation.value.inTime, 'HH:mm:ss'), 'seconds');
      trainStation.value.stopTime = dayjs('00:00:00', 'HH:mm:ss')
          .second(diff)
          .format('HH:mm:ss');
    }, {immediate: true});

    const onAdd = () => {
      trainStation.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      trainStation.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/train-station/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/train-station/save", trainStation.value).then((response) => {
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
      axios.get("/business/admin/train-station/query-list", {
        params: {
          page: param.page,
          size: param.size,
          trainCode: params.value.trainCode,
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          trainStations.value = data.content.list;
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
        size: page.pageSize,
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      trainStation,
      visible,
      trainStations,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      params,
    };
  },
});
</script>

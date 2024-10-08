<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">Refresh</a-button>
      <a-button type="primary" @click="onAdd">New</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trains"
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
          <a-popconfirm
            title="Delete seats already generated and generate new ones?"
            @confirm="genSeat(record)"
            ok-text="Yes" cancel-text="Cancel">
            <a>Generate</a>
          </a-popconfirm>
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
  <a-modal v-model:visible="visible" title="Train" @ok="handleOk">
    <a-form :model="train" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="Code">
        <a-input v-model:value="train.code" :disabled="!!train.id" />
      </a-form-item>
      <a-form-item label="Type">
        <a-select v-model:value="train.type">
          <a-select-option v-for="item in TRAIN_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="Departure">
        <station-select-view v-model:value="train.start"></station-select-view>
      </a-form-item>
      <a-form-item label="DeparturePinyin">
        <a-input v-model:value="train.startPinyin" disabled/>
      </a-form-item>
      <a-form-item label="DepartureTime">
        <a-time-picker v-model:value="train.startTime" valueFormat="HH:mm:ss" placeholder="Select time" />
      </a-form-item>
      <a-form-item label="Arrival">
        <station-select-view v-model:value="train.end"></station-select-view>
      </a-form-item>
      <a-form-item label="ArrivalPinyin">
        <a-input v-model:value="train.endPinyin" disabled/>
      </a-form-item>
      <a-form-item label="ArrivalTime">
        <a-time-picker v-model:value="train.endTime" valueFormat="HH:mm:ss" placeholder="Select time" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, ref, onMounted, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import {pinyin} from "pinyin-pro";
import StationSelectView from "@/components/StationSelect.vue";

export default defineComponent({
  name: "train-view",
  components: {StationSelectView},
  setup() {
    const TRAIN_TYPE_ARRAY = window.TRAIN_TYPE_ARRAY;
    const visible = ref(false);
    let train = ref({
      id: undefined,
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
    const trains = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = [
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

    watch(() => train.value.start, () => {
      if (Tool.isNotEmpty(train.value.start)) {
        train.value.startPinyin = pinyin(train.value.start, { toneType: 'none' }).replaceAll(" ", "");
      } else {
        train.value.startPinyin = "";
      }
    }, {immediate: true});

    watch(() => train.value.end, () => {
      if (Tool.isNotEmpty(train.value.end)) {
        train.value.endPinyin = pinyin(train.value.end, { toneType: 'none' }).replaceAll(" ", "");
      } else {
        train.value.endPinyin = "";
      }
    }, {immediate: true});

    const onAdd = () => {
      train.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      train.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/train/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/train/save", train.value).then((response) => {
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
      axios.get("/business/admin/train/query-list", {
        params: {
          page: param.page,
          size: param.size
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          trains.value = data.content.list;
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

    const genSeat = (record) => {
      loading.value = true;
      axios.get("/business/admin/train/gen-seat/" + record.code).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          notification.success({ description: "Generated successfully" });
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
      train,
      visible,
      trains,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      genSeat
    };
  },
});
</script>

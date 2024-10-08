<template>
  <p>
    <a-space>
      <train-select-view v-model="params.trainCode" width="200px"></train-select-view>
      <a-button type="primary" @click="handleQuery()">Search</a-button>
      <a-button type="primary" @click="onAdd">New</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainCarriages"
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
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="TrainCarriage" @ok="handleOk">
    <a-form :model="trainCarriage" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="TrainCode">
        <train-select-view v-model:value="trainCarriage.trainCode"></train-select-view>
      </a-form-item>
      <a-form-item label="Index">
        <a-input v-model:value="trainCarriage.index" />
      </a-form-item>
      <a-form-item label="SeatType">
        <a-select v-model:value="trainCarriage.seatType">
          <a-select-option v-for="item in SEAT_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
<!--      <a-form-item label="SeatCount">
        <a-input v-model:value="trainCarriage.seatCount" />
      </a-form-item>-->
      <a-form-item label="RowCount">
        <a-input v-model:value="trainCarriage.rowCount" />
      </a-form-item>
<!--      <a-form-item label="ColCount">
        <a-input v-model:value="trainCarriage.colCount" />
      </a-form-item>-->
    </a-form>
  </a-modal>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectView from "@/components/TrainSelect.vue";

export default defineComponent({
  name: "train-carriage-view",
  components: {TrainSelectView},
  setup() {
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const visible = ref(false);
    let trainCarriage = ref({
      id: undefined,
      trainCode: undefined,
      index: undefined,
      seatType: undefined,
      seatCount: undefined,
      rowCount: undefined,
      colCount: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const trainCarriages = ref([]);
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
      title: 'SeatType',
      dataIndex: 'seatType',
      key: 'seatType',
    },
    {
      title: 'SeatCount',
      dataIndex: 'seatCount',
      key: 'seatCount',
    },
    {
      title: 'RowCount',
      dataIndex: 'rowCount',
      key: 'rowCount',
    },
    {
      title: 'ColCount',
      dataIndex: 'colCount',
      key: 'colCount',
    },
    {
      title: 'Operation',
      dataIndex: 'operation'
    }
    ];

    const onAdd = () => {
      trainCarriage.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      trainCarriage.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/train-carriage/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/train-carriage/save", trainCarriage.value).then((response) => {
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
      axios.get("/business/admin/train-carriage/query-list", {
        params: {
          page: param.page,
          size: param.size,
          trainCode: params.value.trainCode
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          trainCarriages.value = data.content.list;
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
      SEAT_TYPE_ARRAY,
      trainCarriage,
      visible,
      trainCarriages,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      params,
      onAdd,
      handleOk,
      onEdit,
      onDelete
    };
  },
});
</script>

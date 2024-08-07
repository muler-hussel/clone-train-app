<template>
  <p>
    <a-space>
      <a-button type="primary" @click="onAdd">New Passenger</a-button>
      <a-button type="primary" @click="handleQuery()">Refresh</a-button>
    </a-space>
  </p>
  <a-table :dataSource="passengers"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="Data is unrecoverable, make sure to delete"
              @confirm="onDelete(record)" >
            <a style="color: red">Delete</a>
          </a-popconfirm>
          <a @click="onEdit(record)">Edit</a>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'type'">
        <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.type">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="Passenger" @ok="handleOk">
    <a-form
        :model="passenger"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
    >
      <a-form-item label="Name">
        <a-input v-model:value="passenger.name" />
      </a-form-item>
      <a-form-item label="IDCard">
        <a-input v-model:value="passenger.idCard" />
      </a-form-item>
      <a-form-item label="Type">
        <a-select v-model:value="passenger.type">
          <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code" value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, reactive, ref, onMounted} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  setup() {
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;

    const visible = ref(false);

    const passenger = ref({
      id: undefined,
      memberId: undefined,
      name: undefined,
      idCard: undefined,
      type: undefined,
      createTime: undefined,
      updateTime: undefined,
    });

    const passengers = ref([]);

    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 2,
    })

    let loading = ref(false);

    const columns = [{
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: 'IDCard',
        dataIndex: 'idCard',
        key: 'idCard',
      },
      {
        title: 'Type',
        dataIndex: 'type',
        key: 'type',
      },
      {
        title: 'Operation',
        dataIndex: 'operation',
      },
    ];

    const onAdd = () => {
      passenger.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      passenger.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/member/passenger/delete/" + record.id).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({ description: "Delete successfully" });
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const handleOk = () => {
      axios.post("/member/passenger/save", passenger.value).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({description: "Save successfully"});
          visible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          })
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
      axios.get("/member/passenger/query-list", {
        params: {
          page: param.page,
          size: param.size
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          passengers.value = data.content.list;
          pagination.value.current = param.page;
          pagination.value.total = data.content.total;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleTableChange = (pagination) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return{
      PASSENGER_TYPE_ARRAY,
      passenger,
      visible,
      pagination,
      onAdd,
      handleOk,
      passengers,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onEdit,
      onDelete,
    };
  },
});
</script>

<style>

</style>
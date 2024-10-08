<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">Refresh</a-button>
      <a-button type="primary" @click="onAdd">New</a-button>
    </a-space>
  </p>
  <a-table :dataSource="stations"
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
  <a-modal v-model:visible="visible" title="Station" @ok="handleOk">
    <a-form :model="station" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="Name">
        <a-input v-model:value="station.name" />
      </a-form-item>
      <a-form-item label="NamePinyin">
        <a-input v-model:value="station.namePinyin" disabled/>
      </a-form-item>
      <a-form-item label="NameInitial">
        <a-input v-model:value="station.nameInitial" disabled/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, ref, onMounted, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import {pinyin} from "pinyin-pro";

export default defineComponent({
  name: "station-view",
  setup() {
    const visible = ref(false);
    let station = ref({
      id: undefined,
      name: undefined,
      namePinyin: undefined,
      nameInitial: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const stations = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'NamePinyin',
      dataIndex: 'namePinyin',
      key: 'namePinyin',
    },
    {
      title: 'NameInitial',
      dataIndex: 'nameInitial',
      key: 'nameInitial',
    },
    {
      title: 'Operation',
      dataIndex: 'operation'
    }
    ];

    //拼音
    watch(() => station.value.name, () => {
      if (Tool.isNotEmpty(station.value.name)) {
        station.value.namePinyin = pinyin(station.value.name, {toneType: 'none'}).replaceAll(" ", "");
        station.value.nameInitial = pinyin(station.value.name, { pattern: 'first', toneType: 'none'}).replaceAll(" ", "");
      } else {
        station.value.namePinyin = "";
        station.value.nameInitial = "";
      }
    }, {immediate: true});

    const onAdd = () => {
      station.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      station.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/station/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/station/save", station.value).then((response) => {
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
      axios.get("/business/admin/station/query-list", {
        params: {
          page: param.page,
          size: param.size
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          stations.value = data.content.list;
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
      station,
      visible,
      stations,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete
    };
  },
});
</script>

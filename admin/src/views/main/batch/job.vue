<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">Refresh</a-button>
      <a-button type="primary" @click="handleAdd">New</a-button>
    </a-space>
  </p>
  <a-table :dataSource="jobs"
           :columns="columns"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="Run immediately?"
              @confirm="handleRun">
            <a-button type="primary" size="small">
              Operate
            </a-button>
          </a-popconfirm>
          <a-popconfirm
              title="Make sure to restart"
              @confirm="handleResume(record)">
            <a-button v-show="record.state === 'PAUSED' || record.state === 'ERROR'" type="primary" size="small">
              Restart
            </a-button>
          </a-popconfirm>
          <a-popconfirm
              title="Stop immediately?"
              @confirm="handlePause(record)">
            <a-button v-show="record.state === 'NORMAL' || record.state === 'BLOCKED'" type="primary" size="small">
              Pause
            </a-button>
          </a-popconfirm>
          <a-button type="primary" @click="handleEdit(record)" size="small">
            Edit
          </a-button>
          <a-popconfirm
              title="Data is unrecoverable, make sure to delete"
              @confirm="handleDelete(record)">
            <a-button type="danger" size="small">
              Delete
            </a-button>
          </a-popconfirm>
        </a-space>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="modalVisible"
           title="User"
           @ok="handleModalOk"
           :confirm-loading="modalLoading">
    <a-form :model="job" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="Name">
        <a-input v-model:value="job.name" />
      </a-form-item>
      <a-form-item label="Description">
        <a-input v-model:value="job.description" />
      </a-form-item>
      <a-form-item label="Group">
        <a-input v-model:value="job.group" :disabled="!!job.state" />
      </a-form-item>
      <a-form-item label="CronExpression">
        <a-input v-model:value="job.cronExpression" />
        <div class="ant-alert ant-alert-success">
          Operate every 5 seconds: 0/5 * * * * ?
          <br>
          Operate every 5 minutes: * 0/5 * * * ?
        </div>
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
  name: "batch-job-view",
  setup() {
    const jobs = ref();
    const loading = ref();
    const columns = [
    {
      title: 'Group',
      dataIndex: 'group',
    },
    {
      title: 'Name',
      dataIndex: 'name',
    },
    {
      title: 'Description',
      dataIndex: 'description',
    },
    {
      title: 'State',
      dataIndex: 'state',
    },
    {
      title: 'CronExpression',
      dataIndex: 'cronExpression',
    },
    {
      title: 'PreFireTime',
      dataIndex: 'preFireTime',
    },
    {
      title: 'NextFireTime',
      dataIndex: 'nextFireTime',
    },
    {
      title: 'Operation',
      dataIndex: 'operation'
    }
    ];

    const handleQuery = () => {
      loading.value = true;
      jobs.value = [];
      axios.get('/batch/admin/job/query').then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          jobs.value = data.content;
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const job = ref();
    job.value = {};
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      let url = "add";
      if (job.value.state) {
        url = "reschedule";
      }
      axios.post('/batch/admin/job/' + url, job.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.success) {
          modalVisible.value = false;
          notification.success({ description: "Save successfully" });
          handleQuery();
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const handleAdd = () => {
      modalVisible.value = true;
      job.value = {
        group: 'DEFAULT'
      };
    };

    const handleEdit = (record) => {
      modalVisible.value = true;
      job.value = Tool.copy(record);
    };

    const handleDelete = (record) => {
      axios.post('/batch/admin/job/delete', {
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({ description: "Delete successfully" });
          handleQuery();
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const handlePause = (record) => {
      axios.post('/batch/admin/job/pause', {
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({description: "Pause successfully"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleResume = (record) => {
      axios.post('/batch/admin/job/reschedule', record).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.success) {
          modalVisible.value = false;
          notification.success({description: "Resume successfully"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleRun = (record) => {
      axios.post('/batch/admin/job/run', record).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({description: "Run successfully"});
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const getEnumValue = (key, obj) => {
      return Tool.getEnumValue(key, obj);
    }

    onMounted(() => {
      console.log('Index mounted')
      handleQuery();
    });

    return {
      columns,
      jobs,
      handleQuery,
      loading,
      handleAdd,
      handleResume,
      handleEdit,
      handleDelete,
      handlePause,
      job,
      modalLoading,
      modalVisible,
      handleModalOk,
      handleRun,
      getEnumValue,
    };
  },
});
</script>

<template>
  <a-select
      v-model:value="trainCode"
      show-search allowClear
      :filter-option="filterTrainCodeOption"
      @change="onChange" placeholder="Select your trip"
      :style="'width: ' + localWidth"
  >
    <a-select-option v-for="item in trains" :key="item.code" :value="item.code" :lable="item.code + item.start + item.end">
      {{item.code}} | {{item.start}} — {{item.end}}
    </a-select-option>
  </a-select>
</template>
<script>

import {defineComponent, onMounted, ref, watch} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "train-select-view",
  props: ["modelValue", "width"],
  emits: ['update:modelValue', 'change'],
  setup(props, {emit}) {
    const trainCode = ref();
    const trains = ref([]);
    const localWidth = ref(props.width);

    if (Tool.isEmpty(props.width)) {
      localWidth.value = "100%";
    }

    watch(() => props.modelValue, () => {
      console.log("props.modelValue", props.modelValue);
      trainCode.value = props.modelValue;
    }, {immediate: true});

    /*加入前端缓存*/
    const queryAllTrain = () => {
      let list = SessionStorage.get(SESSION_ALL_TRAIN);
      if (Tool.isNotEmpty(list)) {
        console.log("queryAllTrain get cache");
        trains.value = list;
      } else {
        axios.get("business/admin/train/query-all").then((response) => {
          let data = response.data;
          if (data.success) {
            trains.value = data.content;
            console.log("queryAllTrain save cache");
            SessionStorage.set(SESSION_ALL_TRAIN, trains.value);
          } else {
            notification.error({description: data.message});
          }
        });
      }
    };

    const onChange = (value) => {
      emit('update:modelValue', value);
      let train = trains.value.filter(item => item.code === value)[0];
      if (Tool.isEmpty(train)) {
        train = {};
      }
      emit('change', train);
    };

    const filterTrainCodeOption = (input, option) => {
      console.log(input, option);
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    onMounted(() => {
      queryAllTrain();
    });

    return {
      trainCode,
      trains,
      localWidth,
      filterTrainCodeOption,
      onChange,
    }
  }
})



</script>
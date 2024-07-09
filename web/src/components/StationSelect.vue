<template>
  <a-select
      v-model:value="name"
      show-search allow-clear
      :filter-option="filterNameOption"
      @change="onChange" placeholder="Search for station"
      :style="'width: ' + localWidth"
  >
    <a-select-option v-for="item in stations" :key="item.name" :value="item.name" :lable="item.name + item.namePinyin + item.nameInitial">
      {{item.name}} ({{item.namePinyin}} / {{item.nameInitial}})
    </a-select-option>
  </a-select>
</template>
<script>

import {defineComponent, onMounted, ref, watch} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "station-select-view",
  props: ["modelValue", "width"],
  emit: ['update:modelValue', 'change'],
  setup(props, {emit}) {
    const name = ref();
    const stations = ref([]);
    const localWidth = ref(props.width);

    if (Tool.isEmpty(props.width)) {
      localWidth.value = "100%";
    }

    watch(() => props.modelValue, () => {
      console.log("props.modelValue", props.modelValue);
      name.value = props.modelValue;
    }, {immediate: true});

    const queryAllStation = () => {
      axios.get("business/station/query-all").then((response) => {
        let data = response.data;
        if (data.success) {
          stations.value = data.content;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const onChange = (value) => {
      emit('update:modelValue', value);
      let station = stations.value.filter(item => item.name === value)[0];
      if (Tool.isNotEmpty(station)) {
        station = {};
      }
      emit('change', station);
    };

    const filterNameOption = (input, option) => {
      console.log(input, option);
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    onMounted(() => {
      queryAllStation();
    });

    return {
      name,
      stations,
      localWidth,
      queryAllStation,
      filterNameOption,
      onChange,
    }
  }
})



</script>
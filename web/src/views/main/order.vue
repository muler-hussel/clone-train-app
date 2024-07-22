<template>
  <div class="order-train">
    <span class="order-train-main">{{dailyTrainTicket.date}}</span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.trainCode}}</span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.start}}</span>&nbsp;
    <span class="order-train-main">（{{dailyTrainTicket.startTime}}）</span>&nbsp;
    <span class="order-train-main"> — </span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.end}}</span>&nbsp;
    <span class="order-train-main">（{{dailyTrainTicket.endTime}}）</span>&nbsp;

    <div class="order-train-ticket">
      <span v-for="item in seatTypes" :key="item.type">
        <span>{{item.desc}}</span>:
        <span class="order-train-ticket-main">{{item.price}}￥  </span>&nbsp;
        <span class="order-train-ticket-main">{{item.count}}</span>&nbsp;tickets available&nbsp;&nbsp;
      </span>
    </div>
  </div>
  <a-divider></a-divider>
  <b>Check passengers: </b>&nbsp;
  <a-checkbox-group v-model:value="passengerChecks" :options="passengerOptions" />
  <div class="order-tickets">
    <a-row class="order-tickets-header" v-if="tickets.length > 0">
      <a-col :span="5">Passenger</a-col>
      <a-col :span="8">IdCard</a-col>
      <a-col :span="5">Ticket Type</a-col>
      <a-col :span="6">Seat Type</a-col>
    </a-row>
    <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
      <a-col :span="5">{{ticket.passengerName}}</a-col>
      <a-col :span="8">{{ticket.passengerIdCard}}</a-col>
      <a-col :span="5">
        <a-select v-model:value="ticket.passengerType" style="width: 100%">
          <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-col>
      <a-col :span="6">
        <a-select v-model:value="ticket.seatTypeCode" style="width: 100%">
          <a-select-option v-for="item in seatTypes" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-col>
    </a-row>
  </div>
  <div v-if="tickets.length > 0">
    <a-button type="primary" size="large" @click="finishCheckPassenger">Submit</a-button>
  </div>
  <a-modal v-model:visible="visible" title="Please check information below"
           style="top: 50px; width: 80%" @ok="showImageCodeModal">
    <div class="order-tickets">
      <a-row class="order-tickets-header" v-if="tickets.length > 0">
        <a-col :span="6">Passenger</a-col>
        <a-col :span="8">IdCard</a-col>
        <a-col :span="5">Ticket Type</a-col>
        <a-col :span="5">Seat Type</a-col>
      </a-row>
      <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
        <a-col :span="6">{{ticket.passengerName}}</a-col>
        <a-col :span="8">{{ticket.passengerIdCard}}</a-col>
        <a-col :span="5">
          <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
            <span v-if="item.code === ticket.passengerType">
              {{item.desc}}
            </span>
          </span>
        </a-col>
        <a-col :span="5">
          <span v-for="item in seatTypes" :key="item.code">
            <span v-if="item.code === ticket.seatTypeCode">
              {{item.desc}}
            </span>
          </span>
        </a-col>
      </a-row>
      <br/>
      <div v-if="chooseSeatType === 0" style="color: darkblue">
        You are not allowed to choose seats.
        <div> - You can choose seats only if you only purchased first class tickets second class tickets.</div>
        <div> - Remaining seats are not abundant enough for you to choose.</div>
      </div>
      <div v-else style="text-align: center">
        <a-checkbox v-for="item in SEAT_COL_ARRAY" :key="item.code" :value="item.code"
                    v-model:checked="chooseSeatObj[item.code + '1']">
          {{item.desc}}
        </a-checkbox>
        <div v-if="tickets.length > 1">
          <a-checkbox v-for="item in SEAT_COL_ARRAY" :key="item.code" :value="item.code"
                          v-model:checked="chooseSeatObj[item.code + '2']">
            {{item.desc}}
          </a-checkbox>
        </div>
        <div style="color: #999999">You can choose {{tickets.length}} seats.</div>
      </div>
      <br/>
<!--      Purchased tickets: {{tickets}}-->
<!--      Chosen seats: {{chooseSeatObj}}-->
    </div>
  </a-modal>

<!--验证码-->
  <a-modal v-model:visible="imageCodeModalVisible" :title="null" :footer="null" :closable="false"
           style="top: 50px; width: 400px">
    <p style="text-align: center; font-weight: bold; font-size: 19px">
      Validation
    </p>
    <p>
      <a-input v-model:value="imageCode" placeholder="Validation">
        <template #suffix>
          <img v-show="!!imageCodeSrc" :src="imageCodeSrc" alt="validation" v-on:click="loadImageCode()" />
        </template>
      </a-input>
    </p>
    <a-button type="primary" block @click="handleOk">Please type in validation</a-button>
  </a-modal>
  <a-modal v-model:visible="lineModalVisible" :title="null" :footer="null" :closable="false"
           style="top: 50px; width: 400px">
    <div class="book-line">
      <div v-show="confirmOrderLineCount < 0">
        <loading-outlined /> Order is under process...
      </div>
      <div v-show="confirmOrderLineCount >= 0">
        <loading-outlined /> There are {{confirmOrderLineCount}} users ahead of you.
      </div>
    </div>
  </a-modal>
</template>

<script>

import {computed, defineComponent, onMounted, ref, watch} from "vue";
import passenger from "./passenger.vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent ({
  name: "order-view",
  computed: {
    passenger() {
      return passenger
    }
  },
  setup() {
    const passengers = ref([]);
    const passengerOptions = ref([]);
    const passengerChecks = ref([]);
    const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
    console.log("Order: ", dailyTrainTicket);

    const SEAT_TYPE = window.SEAT_TYPE;
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;
    const seatTypes = [];
    const visible = ref(false);
    const lineModalVisible = ref(false);
    const confirmOrderId = ref();
    const confirmOrderLineCount = ref(-1);

    for (let KEY in SEAT_TYPE) {
      let key = KEY.toLowerCase();
      if (dailyTrainTicket[key] >= 0) {
        seatTypes.push({
          type: KEY,
          code: SEAT_TYPE[KEY]["code"],
          desc: SEAT_TYPE[KEY]["desc"],
          count: dailyTrainTicket[key],
          price: dailyTrainTicket[key + 'Price'],
        })
      }
    }

    const tickets = ref([]);

    watch(() => passengerChecks.value, (newVal, oldVal) => {
      console.log("Checking changed: ", newVal, oldVal)
      //每次变化时，清空购票列表，重新构造
      tickets.value = [];
      passengerChecks.value.forEach((item) => tickets.value.push({
        passengerId: item.id,
        passengerType: item.type,
        seatTypeCode: seatTypes[0].code,
        passengerName: item.name,
        passengerIdCard: item.idCard
      }))
    }, {immediate: true});

    const chooseSeatType = ref(0); //0:不支持选座 1：一等座 2：二等座
    //计算对应的ACEF列
    const SEAT_COL_ARRAY = computed(() => {
      return window.SEAT_COL_ARRAY.filter(item => item.type === chooseSeatType.value);
    });
    //选座
    const chooseSeatObj = ref({});
    watch(() => SEAT_COL_ARRAY.value, () => {
      chooseSeatObj.value = {};
      for (let i = 1; i <= 2; i++) {
        SEAT_COL_ARRAY.value.forEach((item) => {
          chooseSeatObj.value[item.code + i] = false;
        })
      }
      console.log("Initialize two rows unchecked: ", chooseSeatObj.value);
    }, {immediate: true});

    const handleQueryPassenger = () => {
      axios.get("/member/passenger/query-mine").then((response) => {
        let data = response.data;
        if (data.success) {
          passengers.value = data.content;
          passengers.value.forEach((item) => passengerOptions.value.push({
            label: item.name,
            value: item
          }))
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const finishCheckPassenger = () => {
      console.log("Tickets list: ", tickets.value);
      if (tickets.value.length > 5) {
        notification.error({ description: 'You can purchase at most 5 tickets.' });
        return;
      }

      // 校验余票是否充足，购票列表中的每个座位类型，都去车次座位余票信息中，看余票是否充足
      // 前端校验不一定准，但前端校验可以减轻后端很多压力
      // 注意：这段只是校验，必须copy出seatTypesTemp变量来扣减，用原始的seatTypes去扣减，会影响真实的库存
      let seatTypesTemp = Tool.copy(seatTypes);
      for (let i = 0; i < tickets.value.length; i++) {
        let ticket = tickets.value[i];
        for (let j = 0; j < seatTypesTemp.length; j++) {
          let seatType = seatTypesTemp[j];
          //同类型座位余票-1
          if (ticket.seatTypeCode === seatType.code) {
            seatType.count--;
            if (seatType.count < 0) {
              notification.error({ description: seatType.desc + ' sold out' });
              return;
            }
          }
        }
      }
      console.log("Front end validation proved")
      visible.value = true;

      // 判断是否支持选座，只有纯一等座和纯二等座支持选座
      // 先筛选出购票列表中的所有座位类型，比如四张表：[1, 1, 2, 2]
      let ticketSeatTypeCodes = [];
      for (let i = 0; i < tickets.value.length; i++) {
        let ticket = tickets.value[i];
        ticketSeatTypeCodes.push(ticket.seatTypeCode);
      }
      // 为购票列表中的所有座位类型去重：[1, 2]
      const ticketSeatTypeCodesSet = Array.from(new Set(ticketSeatTypeCodes));
      if (ticketSeatTypeCodesSet.length !== 1) {
        console.log("Chosen seat type is more than one, cannot choose seat.");
        chooseSeatType.value = 0;
      } else {
        if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.FC.code) {
          chooseSeatType.value = SEAT_TYPE.FC.code;
        } else if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.SC.code) {
          chooseSeatType.value = SEAT_TYPE.SC.code;
        } else {
          console.log("You can only choose first and second class seat.");
          chooseSeatType.value = 0;
        }

        //小于20张票
        if (chooseSeatType.value !== 0) {
          for (let i = 0; i < seatTypes.length; i++) {
            let seatType = seatTypes[i];
            if (ticketSeatTypeCodesSet[0] == seatType.code) {
              if (seatType.count < 20) {
                console.log("Remaining tickets is fewer than 20.")
                chooseSeatType.value = 0;
                break;
              }
            }
          }
        }
      }
    }

    const handleOk = () => {
      if (Tool.isEmpty(imageCode.value)) {
        notification.error({ description: 'Validation cannot be null' });
        return;
      }
      console.log("Chosen seats: ", chooseSeatObj.value);

      // 设置每张票的座位
      // 先清空购票列表的座位，有可能之前选了并设置座位了，但选座数不对被拦截了，又重新选一遍
      for (let i = 0; i < tickets.value.length; i++) {
        tickets.value[i].seat = null;
      }
      let i = -1; //不选座位
      // 要么不选座位，要么所选座位应该等于购票数，即i === (tickets.value.length - 1)
      for (let key in chooseSeatObj.value) {
        if (chooseSeatObj.value[key]) {
          i++;
          if (i > tickets.value.length - 1) {
            notification.error({ description: 'Seats chosen is more than tickets purchased' });
            return;
          }
          tickets.value[i].seat = key;
        }
      }
      if (i > -1 && i < (tickets.value.length - 1)) {
        notification.error({ description: 'Seats chosen is fewer than tickets purchased' });
        return;
      }
      console.log("Purchased: ", tickets.value);

      axios.post("/business/confirm-order/done", {
        dailyTrainTicketId: dailyTrainTicket.id,
        date: dailyTrainTicket.date,
        trainCode: dailyTrainTicket.trainCode,
        departure: dailyTrainTicket.start,
        destination: dailyTrainTicket.end,
        tickets: tickets.value,
        imageCodeToken: imageCodeToken.value,
        imageCode: imageCode.value,
      }).then((response) => {
        let data = response.data;
        if (data.success) {
          //notification.success({ description: "Order successfully" });
          visible.value = false;
          imageCodeModalVisible.value = false;
          lineModalVisible.value = true;
          confirmOrderId.value = data.content;
          queryLineCount();
        } else {
          notification.error({ description: data.message });
        }
      });
    }

    /*定时查询订单状态*/
    //确认订单后定时查询
    let queryLineCountInterval;

    //定时查询订单结果/排队情况
    const queryLineCount = () => {
      confirmOrderLineCount.value = -1;
      queryLineCountInterval = setInterval(function () {
        axios.get("/business/confirm-order/query-line-count/" + confirmOrderId.value).then((response) => {
          let data = response.data;
          if (data.success) {
            let result = data.content;
            switch (result) {
              case -1:
                notification.success({description: "Order successfully"});
                lineModalVisible.value = false;
                clearInterval(queryLineCountInterval);
                break;
              case -2:
                notification.error({description: "Fail to order"});
                lineModalVisible.value = false;
                clearInterval(queryLineCountInterval);
                break;
              case -3:
                notification.error({description: "No more tickets"});
                lineModalVisible.value = false;
                clearInterval(queryLineCountInterval);
                break;
              default:
                confirmOrderLineCount.value = result;
            }
          } else {
            notification.error({description: data.message});
          }
        });
      }, 500);
    };

    /*验证码*/
    const imageCodeModalVisible = ref();
    const imageCodeToken = ref();
    const imageCodeSrc = ref();
    const imageCode = ref();
    /*加载图形验证码*/
    const loadImageCode = () => {
      imageCodeToken.value = Tool.uuid(8);
      imageCodeSrc.value = process.env.VUE_APP_SERVER + '/business/kaptcha/image-code/' + imageCodeToken.value;
    };
    const showImageCodeModal = () => {
      loadImageCode();
      imageCodeModalVisible.value = true;
    }

    onMounted(() => {
      handleQueryPassenger();
    });

    return {
      dailyTrainTicket,
      seatTypes,
      handleQueryPassenger,
      passengers,
      passengerOptions,
      passengerChecks,
      tickets,
      PASSENGER_TYPE_ARRAY,
      finishCheckPassenger,
      visible,
      chooseSeatObj,
      chooseSeatType,
      SEAT_COL_ARRAY,
      handleOk,
      imageCodeSrc,
      imageCodeToken,
      imageCodeModalVisible,
      imageCode,
      showImageCodeModal,
      loadImageCode,
      lineModalVisible,
      confirmOrderId,
      confirmOrderLineCount
    };
  },
});
</script>

<style>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}
.order-train .order-train-ticket {
  margin-top: 15px;
}
.order-train .order-train-ticket .order-train-ticket-main {
  color: cornflowerblue;
  font-size: 18px;
}
.order-tickets{
  margin: 10px 0;
}
.order-tickets{
  padding: 5px 10px;
}
.order-tickets .order-tickets-header {
  background-color: cornflowerblue;
  border: solid 1px cornflowerblue;
  color: whitesmoke;
  font-size: 16px;
  padding: 10px 0;
  font-weight: bold;
  align-content: center;
}
.order-tickets .order-tickets-row {
  border: solid 1px lightblue;
  border-top: none;
  vertical-align: middle;
  line-height: 30px;
}
.order-tickets {
  margin: 5px 5px;
}
</style>
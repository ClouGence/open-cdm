<template>
  <div class="aksk-center">
    <p class="user-center-title">{{ $t('a-li-yun-fang-wen-quan-xian') }}</p>
    <div>
      <div class="container-wrapper border-radius-card" style="margin-bottom: 0">
        <Alert type="warning" show-icon>
          <p>
            {{ $t('a-li-yun-aksk-xu-yao-fu-yu-bi-yao-de-quan-xian') }}
            <a :href="`${store.state.docUrlPrefix}/reference/rds_mysql_ram_least_privilege`" target="_blank">
              {{ $t('ru-he-fu-quan') }}
            </a>
          </p>
          <!--          <p style="margin-top: 8px">{{ $t('a-li-yun-aksk-jin-bao-cun-2-xiao-shi-guo-qi-hou-zi-dong-shan-chu') }}</p>-->
        </Alert>
        <Form style="margin-top: 12px" label-position="right" :label-width="130" :model="form" :rules="validate" ref="aksk-form">
          <FormItem label="AccessKey ID" prop="aliyunAk">
            <Input v-model="form.aliyunAk" style="width: 280px" />
          </FormItem>
          <FormItem label="AccessKey Secret" prop="aliyunSk">
            <Input v-model="form.aliyunSk" type="password" password style="width: 280px" />
          </FormItem>
        </Form>
      </div>
      <div class="modal-footer" style="margin-top: 20px">
        <Button type="primary" @click="handleApplyStToken">
          {{ $t('shou-quan-fang-wen') }}
        </Button>
        <Button type="default" @click="handleCleanStToken">
          {{ $t('jie-chu-fang-wen-quan-xian') }}
        </Button>
      </div>
    </div>
    <!--    <p v-if="includesDM" class="user-center-title">{{ $t('shen-pi-liu-cheng-fang-wen-quan-xian') }}</p>-->
    <!--    <div v-if="includesDM">-->
    <!--      <div class="container-wrapper" style="margin-bottom: 0">-->
    <!--        <Alert type="warning" show-icon>-->
    <!--            {{ $t('dui-jieali-yun-ding-ding-shen-pi-liu-xu-chuang-jian-ding-ding-qi-ye-nei-bu-ying-yong-clouddm-hui-shi-yong-gai-ying-yong-de-appkey-he-appsecret-yong-yu-fa-qi-openapi-tiao-yong-lai-chuang-jian-shen-pi-shi-li-cha-xun-shen-pi-zhuang-tai-deng-gai-qi-ye-nei-bu-ying-yong-ju-you-te-ding-fang-wen-quan-xian-yong-yu-fa-qi-shen-pi-shi-li-fa-song-tong-zhi-deng-quan-xian-ju-ti-nei-rong-qing-cha-kan') }}-->
    <!--            <a href="https://doc.clouddm.clougence.com/docs/en/dingtalk_setting" target="_blank">{{ $t('dui-jie-ding-ding-shen-pi') }}</a>.-->
    <!--        </Alert>-->
    <!--        <Form style="margin-top: 12px" label-position="right" :label-width="130" :model="ticketForm"-->
    <!--              :rules="ticketFormValidate" ref="ticket-form">-->
    <!--          <FormItem label="AgentId" prop="agentId">-->
    <!--            <Input v-model="ticketForm.agentId" style="width: 280px"/>-->
    <!--          </FormItem>-->
    <!--          <FormItem label="AppKey" prop="appKey">-->
    <!--            <Input v-model="ticketForm.appKey" password style="width: 280px"/>-->
    <!--          </FormItem>-->
    <!--          <FormItem label="AppSecret" prop="appSecret">-->
    <!--            <Input v-model="ticketForm.appSecret" type="password" password style="width: 280px"/>-->
    <!--          </FormItem>-->
    <!--        </Form>-->
    <!--      </div>-->
    <!--      <div class="modal-footer" style="margin-top: 20px">-->
    <!--        <Button type="primary" @click="handleApplyTicketToken">{{-->
    <!--            $t('shou-quan-fang-wen')-->
    <!--          }}-->
    <!--        </Button>-->
    <!--      </div>-->
    <!--    </div>-->
  </div>
</template>
<script>
import store from '@/store';
import { mapGetters } from 'vuex';

export default {
  components: {},
  data() {
    return {
      store,
      form: {
        aliyunAk: '',
        aliyunSk: ''
      },
      validate: {
        aliyunAk: [
          {
            required: true,
            message: this.$t('a-li-yun-ak-bu-neng-wei-kong')
          }
        ],
        aliyunSk: [
          {
            required: true,
            message: this.$t('a-li-yun-sk-bu-neng-wei-kong')
          }
        ]
      },
      ticketForm: {
        agentId: '',
        appKey: '',
        appSecret: ''
      },
      ticketFormValidate: {
        agentId: [
          {
            required: true,
            message: this.$t('agentid-bu-neng-wei-kong')
          }
        ],
        appKey: [
          {
            required: true,
            message: this.$t('appkey-bu-neng-wei-kong')
          }
        ],
        appSecret: [
          {
            required: true,
            message: this.$t('appsecret-bu-neng-wei-kong')
          }
        ]
      }
    };
  },
  computed: {
    ...mapGetters(['includesDM'])
  },
  methods: {
    handleApplyStToken() {
      this.$refs['aksk-form'].validate((valid) => {
        if (valid) {
          this.$services
            .rdpUserUpdateAliyunAkSk({
              data: {
                ...this.form
              }
            })
            .then((res) => {
              if (res.success) {
                this.$Message.success(this.$t('shou-quan-cheng-gong'));
                this.aliyunAk = '';
                this.aliyunSk = '';
              }
            });
        }
      });
    },
    handleCleanStToken() {
      this.$services.rdpUserCleanAliyunAkSk().then((res) => {
        if (res.success) {
          this.$Message.success(this.$t('jie-chu-cheng-gong'));
          this.aliyunAk = '';
          this.aliyunSk = '';
        }
      });
    },
    handleApplyTicketToken() {
      this.$refs['ticket-form'].validate((valid) => {
        if (valid) {
          this.$services
            .dmTicketApproUpdateKey({
              data: {
                ...this.ticketForm,
                approvalType: 'DINGDING'
              }
            })
            .then((res) => {
              if (res.success) {
                this.$Message.success(this.$t('shou-quan-cheng-gong'));
                this.$bus.emit('getTemplateList');
              }
            });
        }
      });
    }
  }
};
</script>
<style lang="less">
.user-center-title {
  font-size: 16px;
  line-height: 16px;
  padding-left: 8px;
  color: rgba(0, 0, 0, 0.88);
  font-family: PingFangSC-Semibold, serif;
  font-weight: 500;
  border-left: 3px solid #535c70;
  margin-bottom: 6px;
}

.user-center-wrapper {
  margin-top: 16px;
  padding: 20px 40px;
  background-color: #ffffff;
  border: 1px solid #dadada;

  & > p {
    line-height: 36px;
    font-size: 16px;
    padding: 12px 0;
    border-bottom: 1px solid #dadada;

    .user-center-label {
      font-family: PingFangSC-Semibold, serif;
      font-weight: 500;
      display: inline-block;
      width: 104px;
    }
  }

  .ivu-table td,
  .ivu-table th {
    height: 40px;
  }
}

.user-center-wrapper-sp {
  background-color: #ececec;
  margin-bottom: 20px;

  .ivu-input {
    height: 40px;
    line-height: 40px;
  }

  .user-center-wrapper-sp-btn {
    background-color: #ffa30e;
    color: #ffffff;
    margin-top: 16px;
    background-image: none;
    border: none;
    width: 100%;
    height: 50px;
    line-height: 50px;
    font-size: 16px;
    font-family: PingFangSC-Semibold, serif;
    font-weight: 500;

    &:hover {
      background-color: #ffa30e !important;
      background-image: none;
    }
  }
}

/*.ivu-form-item:last-child{*/
/*    margin-bottom: 0;*/
/*}*/
.system-setting-title {
  font-family: PingFangSC-Semibold, serif;
  font-weight: 500;
  margin-bottom: 20px;
}

.aksk-center {
  position: relative;

  .ivu-tabs-nav .ivu-tabs-tab-active {
    color: #0bb9f8;
    font-family: PingFangSC-Semibold, serif;
  }

  .ivu-table th {
    background-color: #f5f5f5;
  }
}
</style>

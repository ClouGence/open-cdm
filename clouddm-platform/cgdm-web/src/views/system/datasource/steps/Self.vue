<template>
  <div class="step step-self">
    <a-form-model v-if="step === 'ds'" ref="selfFormRef" :model="form" :rules="formValidate" :labelCol="{ style: 'min-width: 100px' }">
      <a-form-model-item :label="$t('lian-jie-fang-shi')">
        <a-select style="width: 180px" v-model="form.connectType" @select="handleDsConnectionChange">
          <a-select-option v-for="option in dsConnectionOptions" :key="option.connectType" :value="option.connectType">
            {{ option.connectTypeI18nName }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="SID" v-if="dsConnectionOptionsObj[form.connectType] && dsConnectionOptionsObj[form.connectType].needOracleSid">
        <a-input v-model="form.sid" placeholder="SID" />
      </a-form-model-item>
      <a-form-model-item
        label="SERVICE"
        v-if="dsConnectionOptionsObj[form.connectType] && dsConnectionOptionsObj[form.connectType].needOracleService"
      >
        <a-input v-model="form.service" placeholder="SERVICE" />
      </a-form-model-item>
      <a-form-model-item :prop="hosts[0].type" :label="$t('wang-luo-di-zhi')">
        <div style="display: flex">
          <a-input v-model.trim="hosts[0].host" style="width: 462px" placeholder="IP">
            <template #addonBefore>
              <a-select v-model="hosts[0].type" style="width: 100px" @change="handleChangeHostType(1)" v-if="!isDesktop">
                <a-select-option value="privateHost">{{ $t('nei-wang') }}</a-select-option>
                <a-select-option value="publicHost">{{ $t('wai-wang') }}</a-select-option>
              </a-select>
            </template>
          </a-input>
          <a-input
            v-model.trim="hosts[0].port"
            :style="[{ width: '80px' }, { marginTop: isDesktop ? 'none' : '-1px' }, { borderLeft: 'none' }]"
            placeholder="PORT"
          />
          <a-checkbox v-model="hosts[0].default" style="margin: 0 12px" v-if="!isDesktop" @change="handleChangeDefaultHost(1, $event)">
            {{ $t('mo-ren-fang-wen-di-zhi') }}
          </a-checkbox>
          <div v-show="!hosts[1].show" style="cursor: pointer" @click="handleShowSecondHost" v-if="!isDesktop">
            <cc-iconfont :size="20" color="#51D300" name="add" />
          </div>
          <div v-show="hosts[1].show" @click="handleDeleteHost(0)">
            <cc-iconfont :size="20" color="red" name="delete" />
          </div>
        </div>
      </a-form-model-item>
      <a-form-model-item v-if="hosts[1].show" label=" ">
        <div v-if="hosts[1].show" style="display: flex">
          <a-input v-model.trim="hosts[1].host" style="width: 462px" placeholder="IP">
            <template #addonBefore>
              <a-select v-model="hosts[1].type" style="width: 100px" @change="handleChangeHostType(0)">
                <a-select-option value="privateHost">{{ $t('nei-wang') }}</a-select-option>
                <a-select-option value="publicHost">{{ $t('wai-wang') }}</a-select-option>
              </a-select>
            </template>
          </a-input>
          <a-input v-model.trim="hosts[1].port" style="width: 80px; margin-top: -1px; border-left: none" placeholder="PORT" />
          <a-checkbox v-model="hosts[1].default" style="margin: 0 12px" @change="handleChangeDefaultHost(0, $event)">
            {{ $t('mo-ren-fang-wen-di-zhi') }}
          </a-checkbox>
          <div v-show="hosts[1].show" @click="handleDeleteHost(0)">
            <cc-iconfont :size="20" color="red" name="delete" />
          </div>
        </div>
      </a-form-model-item>
      <a-form-model-item :label="$t('ren-zheng-fang-shi')">
        <a-select v-model="form.type" style="width: 160px">
          <a-select-option v-for="security in securityOptions" :key="security.securityType" :rowKey="security.securityType">
            {{ security.secrityTypeI18nName || security.securityType }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item
        v-if="form.type !== SECURITY_TYPE.ONLY_PASSWD && form.type !== SECURITY_TYPE.NONE"
        :label="$t('shu-ju-ku-zhang-hao')"
        prop="userName"
      >
        <a-input v-model.trim="form.userName" autocomplete="new-password" style="width: 462px" />
      </a-form-model-item>
      <a-form-model-item
        v-if="form.type !== SECURITY_TYPE.NONE && form.type !== SECURITY_TYPE.ONLY_USER"
        :label="$t('shu-ju-ku-mi-ma')"
        prop="password"
      >
        <cc-password-input v-model="form.password" style="width: 462px" />
      </a-form-model-item>
      <a-form-model-item :label="$t('shu-ju-ku-ming')" prop="instanceDesc" v-if="isDb2(stepData[0].dataSourceType)">
        <a-input v-model="form.database" style="width: 462px" />
      </a-form-model-item>
      <a-form-model-item :label="$t('shu-ju-yuan-ming-cheng')" prop="instanceDesc">
        <a-input v-model="form.instanceDesc" style="width: 462px" />
        <div class="name-tip">
          {{ $t('ming-cheng-bian-yu-ji-yi-fang-bian-shi-yong-shi-shi-bie-ru-jiao-yi-ku-yong-hu-ku-ce-shi-ku-deng') }}
        </div>
      </a-form-model-item>
      <a-form-model-item :label="$t('wu-li-wei-zhi')" prop="region" v-if="!isDesktop">
        <cc-region-select v-model="form.region" :env="stepData[0].deployEnvType" />
      </a-form-model-item>
      <a-form-model-item :label="$t('gong-dan')" v-if="!isDesktop">
        <a-switch v-model="form.ticket" @change="handleTicketChange" />
        <div v-if="form.ticket">
          <div class="ticket">
            {{ $t('shen-pi-fang-shi') }}
            <a-radio-group v-model="form.ticketType" @change="handleRefreshTemplate">
              <a-radio v-for="type of ticketTypes" :key="type.approvalType" :value="type.approvalType">
                {{ type.i18n }}
              </a-radio>
            </a-radio-group>
            <a-button :loading="refreshTemplateLoading" class="refresh-template-btn" @click="handleRefreshTemplate" size="small">
              {{ $t('shua-xin-shen-pi-mo-ban') }}
            </a-button>
            <div>
              <div>{{ $t('quan-xian-shen-qing') }}</div>
              <a-select
                v-model="form.authTicket"
                :placeholder="$t('qing-xuan-ze-shen-pi-liu-cheng')"
                :filter-option="filterOption"
                show-search
                allowClear
              >
                <a-select-option v-for="template in ticketTemplates" :value="template.templateIdentity" :key="template.templateIdentity">
                  {{ template.approTemplateName }}
                </a-select-option>
              </a-select>
            </div>
            <div>
              <div>{{ $t('shu-ju-bian-geng') }}</div>
              <a-select
                v-model="form.ticketArr"
                :placeholder="$t('qing-xuan-ze-shen-pi-liu-cheng')"
                mode="multiple"
                :filter-option="filterOption"
                show-search
              >
                <a-select-option v-for="template in ticketTemplates" :value="template.templateIdentity" :key="template.templateIdentity">
                  {{ template.approTemplateName }}
                </a-select-option>
              </a-select>
            </div>
          </div>
        </div>
      </a-form-model-item>
      <a-form-model-item label=" ">
        <div class="test-connection">
          <a-button @click="_testConnection()" style="width: 120px" :loading="testConnectionLoading">
            {{ $t('ce-shi-lian-jie-0') }}
          </a-button>
          <div class="error-msgContent">
            <cc-iconfont
              v-if="testConnectMsg"
              :color="testConnectMsg === $t('lian-jie-cheng-gong') ? '#52C41A' : '#FF1815'"
              :name="testConnectMsg === $t('lian-jie-cheng-gong') ? 'success' : 'error'"
            />
            {{ testConnectMsg }}
            <a-button type="link" v-if="tested && !(testConnectMsg === $t('lian-jie-cheng-gong'))" @click="showErrorMsgModal = true">
              {{ $t('xiang-qing') }}
            </a-button>
          </div>
        </div>
      </a-form-model-item>
      <a-form-model-item label=" ">
        <a-button v-if="!isDesktop" style="width: 120px; margin-right: 16px" @click="handleBack(1, form)">
          {{ $t('shang-yi-bu') }}
        </a-button>
        <a-button style="width: 120px" type="primary" @click="handleAddDs" :loading="addDsLoading">
          {{ $t('que-ren-tian-jia') }}
        </a-button>
      </a-form-model-item>
    </a-form-model>
    <a-modal :visible="showErrorMsgModal" :title="$t('lian-jie-shi-bai')" :mask-closable="false" @cancel="showErrorMsgModal = false">
      <div>
        {{ testConnectMsgDetail }}
      </div>
      <div class="footer">
        <a-button @click="showErrorMsgModal = false">{{ $t('guan-bi') }}</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
import * as Vue from 'vue';
import { mapGetters } from 'vuex';
import datasourceMixin from '@/mixins/datasourceMixin';
import { APPROVAL_BIZ_TYPE, SECURITY_TYPE } from '@/const';
import { isDb2 } from '@/const/dataSource';

export default {
  name: 'Self',
  props: {
    stepData: Array,
    handleBack: Function
  },
  computed: {
    ...mapGetters(['isDesktop'])
  },
  mixins: [datasourceMixin],
  data() {
    const validatePrivateHost = (rule, value, callback) => {
      this.hosts.forEach((host) => {
        if (host.type === 'privateHost' && !host.host && host.type === 'publicHost' && (!host.host || !host.port)) {
          return callback(new Error(this.$t('nei-wang-bu-neng-wei-kong')));
        }
      });
      callback();
    };
    const validatePublicHost = (rule, value, callback) => {
      this.hosts.forEach((host) => {
        if (host.type === 'publicHost' && !host.host && host.type === 'privateHost' && (!host.host || !host.port)) {
          return callback(new Error(this.$t('wai-wang-bu-neng-wei-kong')));
        }
      });
      callback();
    };
    return {
      addDsLoading: false,
      testConnectionLoading: false,
      SECURITY_TYPE,
      showErrorMsgModal: false,
      refreshTemplateLoading: false,
      step: 'ds',
      tested: false,
      hosts: [
        {
          type: 'privateHost',
          default: true,
          host: '',
          port: '',
          show: true
        },
        {
          type: 'publicHost',
          default: false,
          host: '',
          port: '',
          show: false
        }
      ],
      form: {
        connectType: '',
        sid: '',
        service: '',
        userName: '',
        password: '',
        instanceDesc: '',
        region: 'customer',
        order: false,
        ticket: false,
        ticketType: 'DINGDING',
        ticketArr: [],
        authTicket: null,
        type: '',
        database: ''
      },
      ticketTypes: [],
      securityOptions: [],
      formValidate: {
        privateHost: [
          {
            validator: validatePrivateHost,
            trigger: 'blur'
          }
        ],
        publicHost: [
          {
            validator: validatePublicHost,
            trigger: 'blur'
          }
        ],
        userName: [
          {
            required: true,
            message: this.$t('zhang-hao-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ],
        region: [
          {
            required: true,
            message: this.$t('di-qu-bu-neng-wei-kong')
          }
        ]
      },
      orderForm: {
        platform: '',
        type: [],
        process: ''
      },
      orderFormValidate: {},
      ticketTemplates: [],
      ticketTemplatesObj: {},
      dsConnectionOptions: [],
      dsConnectionOptionsObj: {}
    };
  },
  async created() {
    this.form = { ...this.form, ...this.stepData[1] };
    // this.getSecurityOption();
    this.getDsConnectionOptions();
    const data = await this.$services.dmTicketApproTicketType();
    if (data.code === '1') {
      this.ticketTypes = data.data;
    }
  },
  methods: {
    isDb2,
    handleDsConnectionChange() {
      this.getSecurityOption();
    },
    async getDsConnectionOptions() {
      const { dataSourceType, deployEnvType } = this.stepData[0];
      const res = await this.$services.dmConstantListDsConnectionOption({
        data: {
          dataSourceType,
          deployEnvType
        }
      });

      if (res.success && res.data.connectionOptions && res.data.connectionOptions.length) {
        const { connectionOptions } = res.data;
        const obj = {};
        this.dsConnectionOptions = connectionOptions;
        connectionOptions.forEach((option) => {
          obj[option.connectType] = option;
        });
        this.form.connectType = connectionOptions[0].connectType;
        this.dsConnectionOptionsObj = obj;
        await this.getSecurityOption();
      }
    },
    async getSecurityOption() {
      const { dataSourceType, deployEnvType } = this.stepData[0];
      const res = await this.$services.dmConstantListDsSecurityOption({
        data: {
          dataSourceType,
          deployEnvType,
          connectType: this.form.connectType
        }
      });

      if (res.success) {
        const obj = {};
        this.securityOptions = [...res.data.securityOptions];
        res.data.securityOptions.forEach((security) => {
          obj[security.securityType] = security;
        });
        this.securityOptionsObj = obj;
        this.form.type = this.securityOptions.length > 0 ? this.securityOptions[0].securityType : '';
      }
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    async handleTicketChange(checked) {
      if (checked) {
        const res = await this.$services.dmTicketApproListTemplates({
          data: { approvalType: this.form.ticketType }
        });

        if (res.success) {
          this.ticketTemplates = res.data;
          const ticketTemplatesObj = {};
          res.data.forEach((ticket) => {
            ticketTemplatesObj[ticket.templateIdentity] = ticket;
          });
          this.ticketTemplatesObj = ticketTemplatesObj;
        } else {
          // Vue.set(this.form, 'ticket', false);
        }
      }
    },
    handleDeleteHost(index) {
      if (index === 0) {
        const shiftHost = this.hosts.shift();
        this.hosts.push(shiftHost);
      }

      this.hosts[0].default = true;
      this.hosts[1] = {
        ...this.hosts[1],
        host: '',
        port: '',
        default: false,
        show: false
      };
    },
    handleShowSecondHost() {
      this.hosts.forEach((host) => {
        host.show = true;
      });

      this.hosts = [...this.hosts];
    },
    handleChangeHostType(index) {
      const compareIndex = 1 ^ index;
      this.hosts[index].type = this.hosts[compareIndex].type === 'privateHost' ? 'publicHost' : 'privateHost';
      this.hosts = [...this.hosts];
    },
    async _testConnection() {
      this.$refs.selfFormRef.validate(async (valid) => {
        if (valid) {
          this.tested = true;
          this.testConnectionLoading = true;
          try {
            await this.testConnection(this.generateData());
          } finally {
            this.testConnectionLoading = false;
          }
        }
      });
    },
    generateData() {
      const { bindClusterId, dataSourceType, deployEnvType, envId } = this.stepData[0];
      const { userName, password, instanceDesc, region, type, connectType, sid, service, database } = this.form;

      let privateHost = '';
      let publicHost = '';
      let defaultHost = '';
      this.hosts.forEach((host) => {
        if (host.type === 'privateHost') {
          if (host.host && host.port) {
            privateHost = `${host.host}:${host.port}`;
          }
        } else {
          if (host.host && host.port) {
            publicHost = `${host.host}:${host.port}`;
          }
        }

        if (host.default) {
          defaultHost = `${host.host}:${host.port}`;
        }
      });
      const templates = [];
      this.form.ticketArr.forEach((t) => {
        templates.push({
          approvalType: this.form.ticketType,
          templateIdentity: t,
          templateName: this.ticketTemplatesObj[t].approTemplateName,
          approvalBiz: APPROVAL_BIZ_TYPE.EXECUTE
        });
      });
      if (this.form.authTicket) {
        templates.push({
          approvalType: this.form.ticketType,
          templateIdentity: this.form.authTicket,
          templateName: this.ticketTemplatesObj[this.form.authTicket].approTemplateName,
          approvalBiz: APPROVAL_BIZ_TYPE.AUTH
        });
      }

      const dsApproTemplatesJson = JSON.stringify(templates);
      const data = {
        connectType,
        defaultHost,
        bindClusterId,
        dataSourceType,
        deployEnvType,
        dsPropsJson: JSON.stringify({
          database,
          sid,
          serviceName: service,
          userName,
          password
        }),
        privateHost,
        publicHost,
        region,
        instanceDesc,
        securityType: type,
        envId,
        dsApproTemplatesJson
      };
      return data;
    },
    async handleAddDs() {
      if (this.testConnectMsg === this.$t('lian-jie-cheng-gong')) {
        this.$refs.selfFormRef.validate(async (valid) => {
          if (valid) {
            const data = new FormData();
            data.append('addDsFOJson', JSON.stringify(this.generateData()));
            try {
              this.addDsLoading = true;
              const res = await this.$services.dmDataSourceAddDs({
                data,
                headers: {
                  'content-type': 'multipart/form-data'
                },
                msgContent: this.$t('tian-jia-shu-ju-ku-cheng-gong')
              });

              if (res.success) {
                await this.$router.push({ name: 'System_DataSource' });
              }
            } catch (e) {
              this.addDsLoading = false;
            }
          }
        });
      } else {
        this.testConnectMsg = this.$t('qing-xian-ce-shi-lian-jie');
      }
    },
    handleChangeDefaultHost(index, e) {
      this.hosts[index].default = !e.target.checked;
      this.hosts = [...this.hosts];
    },
    async handleRefreshTemplate() {
      this.refreshTemplateLoading = true;
      const res = await this.$services.dmTicketApproRefreshTemplates({
        data: {
          approvalType: this.form.ticketType
        }
      });

      if (res.success) {
        this.ticketTemplates = res.data;
        const ticketTemplatesObj = {};
        res.data.forEach((ticket) => {
          ticketTemplatesObj[ticket.templateIdentity] = ticket;
        });
        this.ticketTemplatesObj = ticketTemplatesObj;
        this.refreshTemplateLoading = false;
      }
    }
  },
  watch: {
    firstSelectedNetworkType(newValue, oldValue) {
      this.form.oldValue = '';
    },
    stepData: {
      handler() {
        this.getDsConnectionOptions();
      },
      deep: true,
      immediate: true
    }
  }
};
</script>

<style lang="less">
.step-self {
  .ticket {
    width: 462px;
    padding: 2px 12px 12px;
    background-color: #f4f4f4;

    .refresh-template-btn {
      float: right;
      margin-top: 4px;
    }
  }

  .ant-input-group-addon {
    border-radius: 0;
  }

  .ant-form-item {
    display: flex;
  }

  .ant-form-item-label {
    width: 80px;
  }

  margin-top: 20px;

  .name-tip {
    color: #888;
  }

  .datasource {
    text-align: left;

    .test-connection {
      display: flex;
      align-items: center;

      .error-msgContent {
        margin-left: 14px;
        height: 32px;
        line-height: 32px;
      }
    }

    & > div > div:first-child {
      margin-bottom: 20px;
    }
  }
}
</style>

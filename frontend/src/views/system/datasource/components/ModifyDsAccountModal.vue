<template>
  <a-modal :visible="visible" :mask-closable="false" :width="480" :title="$t('xiu-gai-shu-ju-ku-zhang-hao')" @cancel="handleCloseModal">
    <div class="edit-account-modal">
      <a-form-model ref="accountForm" :label-col="{ span: 4 }" :model="currentDs" :rules="accountRules" :wrapper-col="{ span: 17 }">
        <a-form-model-item :label="$t('lian-jie-fang-shi')">
          <a-select v-model="currentDs.connectType" @change="getSecurityOption" disabled style="width: 160px">
            <a-select-option v-for="connection in dsConnectionOptions" :key="connection.connectType" :rowKey="connection.connectType">
              {{ connection.connectTypeI18nName || connection.securityType }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item :label="$t('ren-zheng-fang-shi')">
          <a-select v-model="currentDs.securityType" @change="handleChangeSecurityType" disabled style="width: 160px">
            <a-select-option v-for="security in securityOptions" :key="security.securityType" :rowKey="security.securityType">
              {{ security.secrityTypeI18nName || security.securityType }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item v-if="currentDs.securityType === 'USER_PASSWD'" :label="$t('zhang-hao')" prop="userName">
          <a-input v-model="currentDs.userName" style="width: 350px" />
        </a-form-model-item>
        <a-form-model-item v-if="currentDs.securityType !== 'NONE'" :label="$t('mi-ma')" prop="password">
          <cc-password-input v-model="currentDs.password" style="width: 350px" />
        </a-form-model-item>
        <a-form-model-item label=" ">
          <a-button style="margin-right: 10px" @click="_testConnection">
            {{ $t('ce-shi-lian-jie') }}
          </a-button>
          <cc-iconfont
            v-if="testConnectMsg"
            :color="testConnectMsg === $t('lian-jie-cheng-gong') ? 'green' : 'red'"
            :name="testConnectMsg === $t('lian-jie-cheng-gong') ? 'success' : 'error'"
          />
          {{ testConnectMsg }}
        </a-form-model-item>
      </a-form-model>
      <div class="footer">
        <a-button type="primary" @click="handleUpdateDsAccount" :disabled="testConnectMsg !== $t('lian-jie-cheng-gong')">
          {{ $t('bao-cun') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('qu-xiao') }}</a-button>
      </div>
    </div>
  </a-modal>
</template>

<script>
import datasourceMixin from '@/mixins/datasourceMixin';

export default {
  name: 'ModifyDsAccountModal',
  props: {
    ds: Object,
    visible: Boolean,
    handleCloseModal: Function
  },
  mixins: [datasourceMixin],
  data() {
    return {
      testConnectMsg: '',
      currentDs: {},
      securityOptions: [],
      dsConnectionOptions: [],
      dsConnectionOptionsObj: {},
      accountRules: {
        userName: [
          {
            required: true,
            message: this.$t('zhang-hao-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: this.$t('mi-ma-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ]
      }
    };
  },
  methods: {
    _testConnection() {
      this.$refs.accountForm.validate(async (valid) => {
        if (valid) {
          const {
            bindClusterId,
            dataSourceType,
            userName,
            password,
            region,
            privateHost,
            publicHost,
            defaultHost,
            deployEnvType,
            securityType,
            connectType
          } = this.currentDs;
          const data = {
            bindClusterId,
            dataSourceType,
            region,
            privateHost,
            publicHost,
            defaultHost,
            deployEnvType,
            dsPropsJson: JSON.stringify({
              userName,
              password
            }),
            securityType,
            connectType
          };
          this.testConnection(data);
        } else {
          return false;
        }
      });
    },
    async handleUpdateDsAccount() {
      const { id, userName, password } = this.currentDs;
      const data = {
        dataSourceId: id,
        dsPropsJson: JSON.stringify({
          userName,
          password
        })
      };

      const res = await this.$services.dmDataSourceUpdateDsAccount({
        data,
        msg: this.$t('geng-xin-zhang-hao-cheng-gong')
      });

      if (res.success) {
        this.handleCloseModal();
      }
    },
    async getSecurityOption() {
      const { dataSourceType, deployEnvType } = this.ds;
      const res = await this.$services.dmConstantListDsSecurityOption({
        data: {
          dataSourceType,
          deployEnvType,
          connectType: this.currentDs.connectType
        }
      });

      if (res.success) {
        const obj = {};
        this.securityOptions = [...res.data.securityOptions];
        res.data.securityOptions.forEach((security) => {
          obj[security.securityType] = security;
        });
        this.securityOptionsObj = obj;
      }
    },
    async getConnectionOption() {
      const { dataSourceType, deployEnvType } = this.ds;
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
        this.dsConnectionOptionsObj = obj;
        await this.getSecurityOption();
      }
    },
    handleChangeSecurityType() {
      this.currentDs = { ...this.currentDs };
    }
  },
  created() {
    this.currentDs = this.ds;
    this.getConnectionOption();
  }
};
</script>

<style scoped></style>

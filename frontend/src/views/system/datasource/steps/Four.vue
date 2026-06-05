<template>
  <div class="step step-four">
    <a-table :columns="datasourceTableColumns" :data-source="datasourceList" :rowKey="(record) => record.instanceId" size="small">
      <template #name="record">{{ record.instanceDesc }}/{{ record.instanceId }}</template>
      <template #auth="record">
        <a-select :default-value="record.auth.type" size="small" style="width: 160px" @change="handleChangeAuthType(record, $event)">
          <a-select-option v-for="security in securityOptions" :key="security.securityType" :rowKey="security.securityType">
            {{ security.secrityTypeI18nName || security.securityType }}
          </a-select-option>
        </a-select>
      </template>
      <template #account="record">
        <div class="account">
          <div v-if="record.auth.type === SECURITY_TYPE.USER_PASSWD || record.auth.type === SECURITY_TYPE.ONLY_PASSWD">
            <a-input
              v-if="record.auth.type !== SECURITY_TYPE.ONLY_PASSWD"
              v-model="record.auth.data.userName"
              :placeholder="$t('qing-shu-ru-shu-ju-ku-zhang-hao')"
              size="small"
              style="width: 180px"
              autocomplete="new-password"
            />
            <cc-password-input v-model="record.auth.data.password" size="small" style="width: 180px" />
            <!--            <Input v-model="record.auth.data.password" :placeholder="$t('qing-shu-ru-shu-ju-ku-mi-ma')" style="width: 180px;"/>-->
            <a-checkbox
              :default-checked="record.auth.data.auto"
              @change="toggleAutoCreate(record, $event)"
              v-if="securityOptionsObj[record.auth.type] && securityOptionsObj[record.auth.type].hasAutoCreateAccountOption"
            >
              {{ $t('zi-dong-chuang-jian') }}
            </a-checkbox>
          </div>
          <div v-else-if="record.auth.type === SECURITY_TYPE.USER_PASSWD_WITH_TLS">
            <a-input v-model="record.auth.data.username" :placeholder="$t('qing-shu-ru-shu-ju-ku-zhang-hao')" style="width: 180px" />
            <a-input v-model="record.auth.data.password" :placeholder="$t('qing-shu-ru-shu-ju-ku-mi-ma')" style="width: 180px" />
            <label>
              {{ record.auth.data.tls ? record.auth.data.tls.name : $t('shang-chuan-tls-wen-jian') }}
              <input class="input-file" name="tls" type="file" @change="handleUpload(record, 'tls', $event)" />
            </label>
          </div>
          <!--          <div v-else-if="record.auth.type==='阿里云AccessKey'">-->
          <!--            <Input v-model="record.auth.data.ak" placeholder="请输入AccessKey ID" style="width: 180px;"/>-->
          <!--            <Input v-model="record.auth.data.sk" placeholder="请输入AccessKey Secret" style="width: 180px;"/>-->
          <!--          </div>-->
          <div v-else-if="record.auth.type === SECURITY_TYPE.KERBEROS">
            <label for="kerberos">
              {{ record.auth.data.kerberos ? record.auth.data.kerberos.name : $t('shang-chuan-kerberos-pei-zhi-wen-jian') }}
              <input id="kerberos" class="input-file" name="kerberos" type="file" @change="handleUpload(record, 'kerberos', $event)" />
            </label>
            <label for="keybat">
              {{ record.auth.data.keybat ? record.auth.data.keybat.name : $t('shang-chuan-keybat-wen-jian') }}
              <input id="keybat" class="input-file" name="keybat" type="file" @change="handleUpload(record, 'keybat', $event)" />
            </label>
          </div>
        </div>
      </template>
    </a-table>
    <div class="footer">
      <a-button @click="handleBack">{{ $t('shang-yi-bu') }}</a-button>
      <!--      <a-button type="primary" @click="handleSubmit">下一步: 确认添加</a-button>-->
      <a-button type="primary" @click="handleSubmit" v-if="!isDesktop">
        {{ $t('xia-yi-bu-gong-dan-shen-pi-pei-zhi') }}
      </a-button>
      <a-button type="primary" v-else @click="handleAddAllDs">
        {{ $t('xia-yi-bu-que-ren-tian-jia') }}
      </a-button>
    </div>
  </div>
</template>

<script>
import { SECURITY_TYPE } from '@/const';
import { mapGetters } from 'vuex';

export default {
  name: 'Four',
  props: {
    stepData: Array,
    handleBack: Function,
    handleSubmitData: Function,
    handleAddAllDs: Function
  },
  data() {
    return {
      SECURITY_TYPE,
      datasourceList: [{}],
      securityOptions: [],
      securityOptionsObj: {},
      selectOptions: [],
      datasourceTableColumns: [
        {
          title: this.$t('shi-li-ming-cheng-id'),
          scopedSlots: { customRender: 'name' }
        },
        {
          title: this.$t('shu-ju-ku-ren-zheng-fang-shi'),
          scopedSlots: { customRender: 'auth' },
          width: 300
        },
        {
          title: this.$t('shu-ju-ku-zhang-hao'),
          scopedSlots: { customRender: 'account' },
          width: 600
        }
      ]
    };
  },
  computed: {
    ...mapGetters(['isDesktop'])
  },
  methods: {
    async getSecurityOption() {
      const { dataSourceType, deployEnvType } = this.stepData[0];
      const res = await this.$services.dmConstantListDsSecurityOption({
        data: {
          dataSourceType,
          deployEnvType,
          connectType: 'DEFAULT'
        }
      });

      if (res.success) {
        const obj = {};
        this.securityOptions = [...res.data.securityOptions];
        res.data.securityOptions.forEach((security) => {
          obj[security.securityType] = security;
        });
        this.securityOptionsObj = obj;
        // eslint-disable-next-line prefer-destructuring
        const data = [];
        this.stepData[2].forEach((ds) => {
          ds.auth = {
            type: this.securityOptions.length > 0 ? this.securityOptions[0].securityType : '',
            data: {},
            auto: false
          };
          data.push(ds);
        });
        this.datasourceList = data;
      }
    },
    async checkAccount(ds) {
      const { deployEnvType, dataSourceType } = this.stepData[0];

      const { region, instanceId, auth } = ds;

      const { userName, password } = auth.data;

      const data = {
        dataSourceType,
        deployEnvType,
        instanceId,
        region,
        autoCreateAccount: auth.auto,
        dsPropsJson: JSON.stringify({
          userName,
          password
        })
      };

      const res = await this.$services.dmAliyunRdsCheckCloudDsAccount({ data });
      if (!res.success) {
        ds.accountError = true;
      }
      return res;
    },
    async handleSubmit() {
      const requestArr = [];
      this.datasourceList.forEach((ds) => {
        if (ds.auth.type === SECURITY_TYPE.USER_PASSWD) {
          ds.accountError = false;
          requestArr.push(this.checkAccount(ds));
        }
      });

      let canNext = true;
      const res = await Promise.all(requestArr);
      res.forEach((r) => {
        if (!r.success) {
          canNext = false;
        }
      });

      if (canNext) {
        this.handleSubmitData(3, this.datasourceList);
      }
    },
    handleUpload(record, type, e) {
      const { files } = e.target;
      this.datasourceList.forEach((ds) => {
        if (ds.instanceId === record.instanceId) {
          ds.auth.data[type] = files[0];
        }
      });

      this.datasourceList = [...this.datasourceList];
    },
    toggleAutoCreate(record, e) {
      const { datasourceList } = this;
      datasourceList.forEach((ds) => {
        if (ds.instanceId === record.instanceId) {
          ds.auth.auto = e.target.checked;
        }
      });
      this.datasourceList = [...datasourceList];
    },
    handleChangeAuthType(record, value) {
      const { datasourceList } = this;
      datasourceList.forEach((ds) => {
        if (ds.instanceId === record.instanceId) {
          ds.auth = {
            type: value,
            data: {
              userName: '',
              password: ''
            },
            auto: false
          };
        }
      });
      this.datasourceList = [...datasourceList];
    }
  },
  created() {
    this.getSecurityOption();
  }
};
</script>

<style lang="less" scoped>
.account {
  & > div {
    display: flex;
    align-items: center;

    & > * + * {
      margin-left: 10px;
    }

    .label-button {
      height: 24px;
      line-height: 24px;
      border: 1px solid #d9d9d9;
    }
  }

  .input-file {
    visibility: hidden;
  }
}
</style>

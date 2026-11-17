<template>
  <div class="content-wrapper add-datasource-page">
    <DataSourceTypeModal
      v-model="showAddDataSourceTypeModal"
      :initial-selection="{ dsType: addDataSourceForm.type, deployType: addDataSourceForm.deployType }"
      @select="handleConfirmAddDataSourceType"
    />
    <div class="add-datasource-wrapper">
      <div class="add-datasource-content">
        <DataSourceInfo
          :addDataSourceForm="addDataSourceForm"
          v-if="currentStep === 1"
          ref="dataSourceInfo"
          :current-step="currentStep"
          :show-query-config="shouldAutoEnableFeatures"
          :auto-enable-features="shouldAutoEnableFeatures"
          :driver-family-map="driverFamilyMap"
          :ds-id="editDataSourceId"
          :edit-mode="editMode"
          @driver-status-change="handleDriverStatusChange"
          @config-loaded="handleConfigLoaded"
        ></DataSourceInfo>
        <SuccessAdd v-if="currentStep > 2"></SuccessAdd>
      </div>
      <div v-if="currentStep === 1" class="add-dataSource-tools">
        <div class="add-dataSource-actions">
          <Button @click="handleTestConnection" :loading="testConnectionLoading" v-if="currentStep === 1">
            {{ $t('ce-shi-lian-jie') }}
          </Button>
          <Button
            type="primary"
            class="primary-action"
            @click="handleAddDataSource"
            :loading="addDatasourceLoading"
            :disabled="disableAddDataSource"
            v-if="currentStep === 1"
          >
            {{ editMode ? $t('bao-cun') : $t('xin-zeng-shu-ju-yuan') }}
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import DataSourceInfo from '@/components/function/addDataSource/DataSourceInfo';
import SuccessAdd from '@/components/function/addDataSource/SuccessAdd';
import { separatePort, isMySQL } from '@/utils';
import { isPostgreSQL } from '@/const/dataSource';
import { mapGetters, mapState } from 'vuex';
import { cloneDeep as deepClone } from '@/utils/lodash';
import DataSourceTypeModal from '@/components/function/addDataSource/DataSourceTypeModal';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import { formatHostPort } from '@/utils/networkAddress';
import DataSourceGroup from '../dataSourceGroup.json';

const EMPTY_DATA_SOURCE_FORM = {
  fetchType: 'MANUALLY_FILL',
  deployType: 'SELF_MAINTENANCE',
  dbName: '',
  noValidateDbName: '',
  driver: '',
  driverFamily: '',
  driverVersion: '',
  dsKvConfigs: [],
  hostList: [
    {
      type: 'public',
      display: true,
      host: '',
      port: '3306'
    },
    {
      type: 'public',
      display: false,
      host: '',
      port: '3306'
    }
  ],
  connectType: null,
  connectTypeValue: '',
  host: '',
  type: 'MySQL',
  envId: '',
  region: '',
  queryClusterId: '',
  queryHostType: 'PUBLIC',
  rdsList: [],
  aliyunAk: '',
  aliyunSk: '',
  instanceDesc: '',
  ifAkSK: 'true',
  port: '',
  publicHost: '',
  publicPort: '',
  hdfsSecurityType: 'NONE',
  account: '',
  password: '',
  hdfsPort: '8020',
  securityType: 'KERBEROS',
  hdfsDwDir: '/user/hive/warehouse',
  sid: '',
  service: '',
  accountRole: '',
  asSysDba: false,
  accessKey: '',
  secretKey: ''
};

export default {
  name: 'AddDataSource',
  components: {
    DataSourceTypeModal,
    DataSourceInfo,
    SuccessAdd
  },
  props: {
    isModal: {
      type: Boolean,
      default: false
    },
    handleSetTestDsMsg: Function,
    handleCloseAddDsModal: Function
  },
  data() {
    return {
      addDatasourceLoading: false,
      DataSourceGroup,
      currentStep: 0,
      addDataSourceForm: deepClone(EMPTY_DATA_SOURCE_FORM),
      driverReadyForAdd: true,
      driverRequiredForAdd: false,
      testConnectionLoading: false,
      showAddDataSourceTypeModal: false
    };
  },
  computed: {
    ...mapGetters(['isDesktop']),
    ...mapState(['globalDsSetting', 'dmGlobalSetting']),
    driverFamilyMap() {
      const dsSetting = this.dmGlobalSetting?.dsSettingDef || this.globalDsSetting || {};

      return Object.keys(dsSetting).reduce((result, dsType) => {
        result[dsType] = Array.isArray(dsSetting[dsType]?.driverFamilies) ? dsSetting[dsType].driverFamilies : [];
        return result;
      }, {});
    },
    shouldAutoEnableFeatures() {
      return !this.isDesktop && this.$route?.path === '/datasource/add';
    },
    editMode() {
      return this.$route.query.mode === 'edit' && !!this.editDataSourceId;
    },
    editDataSourceId() {
      const dsId = Number(this.$route.query.dsId);
      return Number.isFinite(dsId) && dsId > 0 ? dsId : null;
    },
    disableAddDataSource() {
      return this.driverRequiredForAdd && !this.driverReadyForAdd;
    }
  },
  beforeUnmount() {
    this.$bus.off(EVENT_BUS_NAME_LIST.SHOW_ADD_DATASOURCE_TYPE_MODAL, this.handleShowAddDataSourceTypeModal);
  },
  watch: {
    '$route.query': {
      handler() {
        this.syncStepFromRoute();
      }
    }
  },
  created() {
    this.syncStepFromRoute();
    this.$bus.on(EVENT_BUS_NAME_LIST.SHOW_ADD_DATASOURCE_TYPE_MODAL, this.handleShowAddDataSourceTypeModal);
  },
  methods: {
    handleShowAddDataSourceTypeModal() {
      if (this.editMode) {
        return;
      }
      this.showAddDataSourceTypeModal = true;
    },
    handleConfirmAddDataSourceType(type) {
      this.$router.push({
        path: '/datasource/add',
        query: {
          dsType: type.dsType,
          deployType: type.deployType
        }
      });
    },
    handleSetEmptyDatasourceForm() {
      this.currentStep = 1;
      this.addDataSourceForm = deepClone(EMPTY_DATA_SOURCE_FORM);
    },
    separatePort,
    syncPrimaryHostFields() {
      const visibleHost =
        this.addDataSourceForm.hostList.find((item) => item.display && item.type === 'public') || this.addDataSourceForm.hostList[0] || {};

      this.addDataSourceForm.publicHost = visibleHost.host || '';
      this.addDataSourceForm.publicPort = visibleHost.port || '';
      if (!this.addDataSourceForm.host) {
        this.addDataSourceForm.host = visibleHost.host || '';
      }
      if (!this.addDataSourceForm.port) {
        this.addDataSourceForm.port = visibleHost.port || '';
      }
      this.addDataSourceForm.resolvedHost = this.resolvePrimaryHost();
      this.addDataSourceForm.queryHostType = 'PUBLIC';
    },
    resolvePrimaryHost() {
      if (this.addDataSourceForm.resolvedHost) {
        return this.addDataSourceForm.resolvedHost;
      }
      const isSeparate = this.separatePort(this.addDataSourceForm.type) || this.addDataSourceForm.type === 'Db2Fori';
      if (isSeparate && this.addDataSourceForm.host && this.addDataSourceForm.port) {
        return formatHostPort(this.addDataSourceForm.address || this.addDataSourceForm.host, this.addDataSourceForm.port);
      }
      return this.addDataSourceForm.host || this.addDataSourceForm.publicHost || '';
    },
    handleDriverStatusChange(status) {
      this.driverRequiredForAdd = !!status?.required;
      this.driverReadyForAdd = !this.driverRequiredForAdd || !!status?.ready;
    },
    ensureDriverReadyForAdd() {
      const driverReady = this.$refs.dataSourceInfo?.isDriverReadyForSubmit?.() ?? this.driverReadyForAdd;
      if (this.driverRequiredForAdd && !driverReady) {
        this.$Message.error(this.$t('initialization.mysqlDriverDownloadRequired'));
        return false;
      }

      return true;
    },
    handleAddDataSource() {
      if (!this.ensureDriverReadyForAdd()) {
        return;
      }

      this.$refs.dataSourceInfo.validateAddDsForm((val) => {
        if (val) {
          this.$refs.dataSourceInfo?.syncAddDsUiFormToKvConfigs?.();
          this.syncPrimaryHostFields();
          this.handleAdd();
        }
      });
    },
    handleAdd() {
      const payload = this.$refs.dataSourceInfo?.buildDsSubmitPayload?.();
      this.addDatasourceLoading = true;
      const serviceName = this.editMode ? 'rdpDataSourceUpdateDs' : 'rdpDataSourceAddDs';
      this.$services[serviceName]({ data: payload })
        .then(async (res) => {
          this.addDatasourceLoading = false;
          if (res.success) {
            if (this.editMode) {
              this.$Message.success(this.$t('xiu-gai-cheng-gong'));
              this.$router.push({ path: '/datasource' });
            } else {
              this.currentStep = 4;
            }
            return;
          }
          this.$Message.error(res.msg || this.$t('shu-ju-yuan-tian-jia-shi-bai'));
        })
        .catch((error) => {
          this.addDatasourceLoading = false;
          this.$Message.error(error?.message || this.$t('shu-ju-yuan-tian-jia-shi-bai'));
        });
    },
    handleTestConnection() {
      if (!this.ensureDriverReadyForAdd()) {
        return;
      }
      this.$refs.dataSourceInfo.validateAddDsForm((valid) => {
        if (!valid) {
          return;
        }
        const payload = this.$refs.dataSourceInfo?.buildDsSubmitPayload?.();
        this.testConnectionLoading = true;
        this.$services
          .dmDataSourceConnectDs({ data: payload })
          .then((res) => {
            const result = res.data || {};
            const connectSuccess = res.success && result.success !== false;
            if (connectSuccess) {
              this.$Message.success(this.$t('ce-shi-lian-jie-cheng-gong'));
              return;
            }
            this.$Message.error(result.message || res.msg || this.$t('ce-shi-lian-jie-shi-bai'));
          })
          .catch((error) => {
            this.$Message.error(error?.message || this.$t('ce-shi-lian-jie-shi-bai'));
          })
          .finally(() => {
            this.testConnectionLoading = false;
          });
      });
    },
    handleAddPersonalDataSource(testDs = false) {
      this.$refs.dataSourceInfo.validateAddDsForm((val) => {
        if (val) {
          this.$refs.dataSourceInfo?.syncAddDsUiFormToKvConfigs?.();
          this.syncPrimaryHostFields();
          this.handleAddPersonal(testDs);
        }
      });
    },
    handleAddPersonal(testDs) {
      if (!this.addDataSourceForm.host && !this.addDataSourceForm.publicHost) {
        this.$Modal.warning({
          title: this.$t('tian-jia-shu-ju-yuan-ti-shi'),
          content: this.$t('qing-tian-xie-wan-zheng-qie-zheng-que-de-shu-ju-yuan-di-zhi')
        });
      } else {
        const { connectTypeValue, dsKvConfigs } = this.addDataSourceForm;
        const formData = new FormData();
        const isSeparate = this.separatePort(this.addDataSourceForm.type);
        const host = isSeparate
          ? this.addDataSourceForm.host && this.addDataSourceForm.port
            ? formatHostPort(this.addDataSourceForm.address || this.addDataSourceForm.host, this.addDataSourceForm.port)
            : ''
          : this.addDataSourceForm.host;
        const publicHost = isSeparate
          ? this.addDataSourceForm.publicHost && this.addDataSourceForm.publicPort
            ? formatHostPort(this.addDataSourceForm.publicHost, this.addDataSourceForm.publicPort)
            : ''
          : this.addDataSourceForm.publicHost;

        const kvConfigs = [];
        if (dsKvConfigs.length) {
          dsKvConfigs.forEach((config) => {
            const { configName, currentCount, defaultValue } = config;
            kvConfigs.push({
              configName,
              configValue: currentCount || defaultValue
            });
          });
        }
        const DataSourceAddData = {
          host: publicHost && this.addDataSourceForm.type === 'Oracle' ? `${publicHost}:${connectTypeValue}` : publicHost,
          privateHost: '',
          publicHost: publicHost && this.addDataSourceForm.type === 'Oracle' ? `${publicHost}:${connectTypeValue}` : publicHost,
          type: this.addDataSourceForm.type,
          connectType: this.addDataSourceForm.connectType,
          instanceDesc: this.addDataSourceForm.instanceDesc,
          hostType: 'PUBLIC',
          account:
            DataSourceGroup.oracle.indexOf(this.addDataSourceForm.type) > -1
              ? this.addDataSourceForm.asSysDba
                ? `${this.addDataSourceForm.account} as SYSDBA`
                : this.addDataSourceForm.account
              : this.addDataSourceForm.account,
          instanceId: this.addDataSourceForm.instanceId,
          password: this.addDataSourceForm.password,
          securityType: this.addDataSourceForm.securityType,
          dbName: this.addDataSourceForm.dbName || this.addDataSourceForm.noValidateDbName,
          dsKvConfigs: kvConfigs,
          // extraData: {
          //   hdfsIp: this.addDataSourceForm.hdfsIp,
          //   hdfsPort: this.addDataSourceForm.hdfsPort,
          //   hdfsDwDir: this.addDataSourceForm.hdfsDwDir,
          //   hdfsPrincipal: this.addDataSourceForm.hdfsPrincipal
          // },
          driver: this.addDataSourceForm.driver,
          envId: this.addDataSourceForm.envId
        };

        Object.keys(DataSourceAddData).forEach((item) => {
          if (typeof DataSourceAddData[item] === 'string') {
            DataSourceAddData[item] = DataSourceAddData[item].trim();
          }
        });

        formData.append('rdpConfig', JSON.stringify(DataSourceAddData));
        formData.append(
          'dmConfig',
          JSON.stringify({
            hostType: 'PUBLIC',
            driver: DataSourceAddData.driver
          })
        );
        if (testDs) {
          this.$services
            .dmDesktopDataSourceTestDs({
              data: formData
            })
            .then((res) => {
              if (res.success) {
                this.handleSetTestDsMsg(this.$t('ce-shi-lian-jie-cheng-gong'));
              } else {
                this.handleSetTestDsMsg(this.$t('ce-shi-lian-jie-shi-bai'));
              }
            });
        } else {
          this.$services
            .dmDesktopDataSourceAddDs({
              data: formData
            })
            .then((res) => {
              if (res.success) {
                this.handleCloseAddDsModal();
              }
            });
        }
      }
    },
    handleReset() {
      this.addDataSourceForm = {
        fetchType: 'MANUALLY_FILL',
        deployType: 'SELF_MAINTENANCE',
        host: '',
        publicHost: '',
        publicPort: '',
        type: 'MySQL',
        region: '',
        rdsList: [],
        aliyunAk: '',
        aliyunSk: '',
        instanceDesc: '',
        ifAkSK: 'true',
        port: '3306',
        hdfsSecurityType: 'NONE',
        account: '',
        hdfsPort: '8020',
        securityType: 'KERBEROS',
        hdfsDwDir: '/user/hive/warehouse'
      };
    },
    syncStepFromRoute() {
      if (this.isModal) {
        this.currentStep = 1;
        return;
      }
      if (this.editMode) {
        const dsType = this.$route.query.dsType;
        if (dsType) {
          this.addDataSourceForm.type = dsType;
        }
        this.currentStep = 1;
        return;
      }
      const dsType = this.$route.query.dsType;
      if (dsType) {
        const changedType = this.addDataSourceForm.type !== dsType;
        this.addDataSourceForm.type = dsType;
        this.addDataSourceForm.deployType = this.$route.query.deployType || 'SELF_MAINTENANCE';
        this.currentStep = 1;
        if (changedType && this.$refs.dataSourceInfo) {
          this.$refs.dataSourceInfo.handleDataSourceChange();
        }
      } else {
        this.$router.replace({ path: '/datasource' });
        return;
      }
      this.driverReadyForAdd = true;
      this.driverRequiredForAdd = false;
    },
    handleConfigLoaded(config) {
      if (!this.editMode || !config?.instanceId || this.$route.query.instanceId === config.instanceId) {
        return;
      }
      this.$router.replace({
        path: '/datasource/add',
        query: {
          ...this.$route.query,
          instanceId: config.instanceId
        }
      });
    }
  }
};
</script>
<style lang="less">
.add-datasource-page {
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  width: 100%;
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  position: relative;
  overflow-x: hidden;
  overflow-y: hidden;
  padding: 0 !important;
  background: #ffffff;
}

.add-datasource-wrapper {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  margin: 0;
  background: transparent;
  border: none;
  border-radius: 0;
  box-shadow: none;
  overflow: visible;

  .add-datasource-content {
    flex: 1 1 auto;
    display: flex;
    flex-direction: column;
    min-height: 0;
    margin-bottom: 0;
    padding: 0;
    overflow: hidden;
  }
}

.add-dataSource-tools {
  position: relative;
  flex: 0 0 auto;
  display: flex;
  align-items: center;
  min-height: 56px;
  justify-content: center;
  padding: 8px 24px 16px;
  background: transparent;
  border-top: none;
  box-shadow: none;

  .add-dataSource-actions {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
  }

  button {
    min-width: 108px;
    margin: 0;
    border-radius: 6px;
    font-weight: 500;
  }

  .primary-action {
    min-width: 148px;
    border-color: #0f9f55;
    background: #0f9f55;
  }
}

.desktop {
  padding: 0;

  .add-datasource-wrapper {
    padding: 0;
    border: none;

    .add-datasource-content {
      margin-bottom: 0;
      padding: 0;

      .add-datasource-step1 {
        padding: 0;
      }
    }
  }
}
</style>

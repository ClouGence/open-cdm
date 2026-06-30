<template>
  <div class="content-wrapper add-datasource-page">
    <div class="add-datasource-wrapper">
      <div class="add-datasource-content">
        <DataSourceInfo
          :addDataSourceForm="addDataSourceForm"
          v-if="currentStep === 0 || currentStep === 1"
          ref="dataSourceInfo"
          :current-step="currentStep"
          :show-query-config="shouldAutoEnableFeatures"
          :auto-enable-features="shouldAutoEnableFeatures"
          :driver-family-map="driverFamilyMap"
          :ds-id="editDataSourceId"
          :edit-mode="editMode"
          :set-security-setting="setSecuritySetting"
          @driver-status-change="handleDriverStatusChange"
          @config-loaded="handleConfigLoaded"
        ></DataSourceInfo>
        <SuccessAdd v-if="currentStep > 2"></SuccessAdd>
      </div>
      <div class="add-dataSource-tools">
        <div class="add-dataSource-actions">
          <Button type="primary" @click="handleStep('next')" v-if="currentStep === 0">
            {{ $t('xia-yi-bu') }}
          </Button>
          <Button
            type="primary"
            @click="handleAddDataSource"
            :loading="addDatasourceLoading"
            :disabled="disableAddDataSource"
            v-if="currentStep === 1"
          >
            {{ editMode ? $t('bao-cun') : $t('xin-zeng-shu-ju-yuan') }}
          </Button>
          <Button @click="handleTestConnection" :loading="testConnectionLoading" v-if="currentStep === 1">
            {{ $t('ce-shi-lian-jie') }}
          </Button>
        </div>
        <span v-if="testConnectionHasResult" class="test-connection-inline-msg" :class="testConnectionSuccess ? 'tc-success' : 'tc-fail'">
          <Icon :type="testConnectionSuccess ? 'ios-checkmark-circle' : 'ios-close-circle'" />
          {{ testConnectionMessage }}
        </span>
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
import DataSourceGroup from '../dataSourceGroup.json';
import store from '../../store/index';

const EMPTY_DATA_SOURCE_FORM = {
  fetchType: 'MANUALLY_FILL',
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
    DataSourceInfo,
    SuccessAdd
  },
  props: {
    handleSetTestDsMsg: Function,
    handleCloseAddDsModal: Function
  },
  data() {
    return {
      addDatasourceLoading: false,
      DataSourceGroup,
      errorMsg: '',
      store,
      currentStep: 0,
      clusters: [],
      addDataSourceForm: deepClone(EMPTY_DATA_SOURCE_FORM),
      securitySetting: [],
      driverReadyForAdd: true,
      driverRequiredForAdd: false,
      testConnectionLoading: false,
      testConnectionHasResult: false,
      testConnectionSuccess: false,
      testConnectionMessage: ''
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
    store.state.rdsData = [];
    store.state.addedRdsList = [];
    store.state.firstAddDataSource = true;
    store.state.selectedCluster = {};
    store.state.clusterList = [];
  },
  watch: {
    '$route.query.dsType': {
      handler() {
        this.syncStepFromRoute();
      }
    },
    '$route.query.dsId': {
      handler() {
        this.syncStepFromRoute();
      }
    }
  },
  mounted() {
    this.syncStepFromRoute();
  },
  methods: {
    handleSetEmptyDatasourceForm() {
      this.currentStep = 0;
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
        return `${this.addDataSourceForm.host}:${this.addDataSourceForm.port}`;
      }
      return this.addDataSourceForm.host || this.addDataSourceForm.publicHost || '';
    },
    setSecuritySetting(setting) {
      this.securitySetting = setting;
    },
    handleDriverStatusChange(status) {
      this.driverRequiredForAdd = !!status?.required;
      this.driverReadyForAdd = !this.driverRequiredForAdd || !!status?.ready;
    },
    setActionMessage(success, message) {
      this.testConnectionHasResult = true;
      this.testConnectionSuccess = !!success;
      this.testConnectionMessage = message || '';
    },
    clearActionMessage() {
      this.testConnectionHasResult = false;
      this.testConnectionSuccess = false;
      this.testConnectionMessage = '';
    },
    ensureDriverReadyForAdd() {
      const driverReady = this.$refs.dataSourceInfo?.isDriverReadyForSubmit?.() ?? this.driverReadyForAdd;
      if (this.driverRequiredForAdd && !driverReady) {
        this.setActionMessage(false, this.$t('initialization.mysqlDriverDownloadRequired'));
        return false;
      }

      return true;
    },
    handleStep(type) {
      if (type === 'pre') {
        this.currentStep--;
        this.clearActionMessage();
        // if (this.currentStep === 1) {
        //   this.addDataSourceForm.rdsList.map((item) => {
        //     if (item.clusters) {
        //       this.clusters[item.instanceId] = item.clusters;
        //     }
        //     return null;
        //   });
        // }
      } else if (this.currentStep === 0) {
        this.$refs.dataSourceInfo.validateSelectStep((valid) => {
          if (!valid) {
            return;
          }
          this.securitySetting = this.$refs.dataSourceInfo.securitySetting;
          this.currentStep++;
          this.clearActionMessage();
          this.updateAddDsRoute();
        });
      } else if (this.currentStep === 1) {
        if (this.isManual || this.addDataSourceForm.ifAkSK === 'false') {
          if (!this.addDataSourceForm.host) {
            this.$Modal.warning({
              title: this.$t('shu-ju-yuan-tian-jia-shi-bai'),
              content: this.$t('qing-tian-xie-shu-ju-yuan-xin-xi')
            });
          } else if (
            this.addDataSourceForm.type === 'Hive' &&
            (!this.addDataSourceForm.hdfsIp ||
              !this.addDataSourceForm.hdfsPort ||
              !this.addDataSourceForm.hdfsDwDir ||
              !this.addDataSourceForm.hdfsSecurityType ||
              (this.addDataSourceForm.securityType === 'NONE' && !this.addDataSourceForm.account) ||
              (this.addDataSourceForm.securityType === 'KERBEROS' && !this.addDataSourceForm.hdfsPrincipal))
          ) {
            this.$Modal.warning({
              title: this.$t('shu-ju-yuan-tian-jia-shi-bai'),
              content: this.$t('qing-tian-xie-wan-zheng-de-shu-ju-yuan-xin-xi')
            });
          } else {
            this.currentStep++;
          }
        } else if (this.addDataSourceForm.rdsList.length > 0) {
          const noClusterDataSource = [];

          this.addDataSourceForm.rdsList.map((item) => {
            if (!item.clusters || item.clusters.length < 1) {
              noClusterDataSource.push(item);
            }
            return null;
          });
          if (noClusterDataSource.length > 0) {
            this.$Modal.confirm({
              title: this.$t('shu-ju-yuan-tian-jia-ti-shi'),
              content: this.$t(
                'nin-dang-qian-yi-you-tian-jia-ji-qi-que-ren-dang-qian-suo-xuan-shu-ju-yuan-bu-dui-ci-tian-jia-bai-ming-dan-ru-bu-tian-jia-hou-xu-qing-zhi-shu-ju-yuan-guan-li-tian-jia'
              ),
              onOk: () => {
                this.currentStep++;
              }
            });
          } else {
            this.currentStep++;
          }
        } else {
          this.$Modal.warning({
            title: this.$t('shu-ju-yuan-tian-jia-shi-bai'),
            content: this.$t('qing-xuan-ze-zhi-shao-yi-ge-shu-ju-yuan')
          });
        }
      } else {
        this.currentStep++;
      }
    },
    handleAddDataSource() {
      if (!this.ensureDriverReadyForAdd()) {
        return;
      }

      this.$refs.dataSourceInfo.$refs.addLocalDs.validate((val) => {
        if (val) {
          this.clearActionMessage();
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
          this.setActionMessage(false, res.msg || this.$t('shu-ju-yuan-tian-jia-shi-bai'));
        })
        .catch((error) => {
          this.addDatasourceLoading = false;
          this.setActionMessage(false, error?.message || this.$t('shu-ju-yuan-tian-jia-shi-bai'));
        });
    },
    handleTestConnection() {
      if (!this.ensureDriverReadyForAdd()) {
        return;
      }
      this.$refs.dataSourceInfo.$refs.addLocalDs.validate((valid) => {
        if (!valid) {
          return;
        }
        const payload = this.$refs.dataSourceInfo?.buildDsSubmitPayload?.();
        this.testConnectionLoading = true;
        this.clearActionMessage();
        this.$services
          .dmDataSourceConnectDs({ data: payload })
          .then((res) => {
            const result = res.data || {};
            const connectSuccess = res.success && result.success !== false;
            this.setActionMessage(
              connectSuccess,
              connectSuccess ? this.$t('ce-shi-lian-jie-cheng-gong') : result.message || res.msg || this.$t('ce-shi-lian-jie-shi-bai')
            );
          })
          .catch((error) => {
            this.setActionMessage(false, error?.message || this.$t('ce-shi-lian-jie-shi-bai'));
          })
          .finally(() => {
            this.testConnectionLoading = false;
          });
      });
    },
    handleAddPersonalDataSource(testDs = false) {
      this.$refs.dataSourceInfo.$refs.addLocalDs.validate((val) => {
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
            ? `${this.addDataSourceForm.host}:${this.addDataSourceForm.port}`
            : ''
          : this.addDataSourceForm.host;
        const publicHost = isSeparate
          ? this.addDataSourceForm.publicHost && this.addDataSourceForm.publicPort
            ? `${this.addDataSourceForm.publicHost}:${this.addDataSourceForm.publicPort}`
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
    handleCancel() {
      this.currentStep = 0;
    },
    getSecurity(type) {
      let security = {};

      this.securitySetting.map((item) => {
        if (item.securityType === type) {
          security = item;
        }
        return null;
      });
      return security;
    },
    syncStepFromRoute() {
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
        this.addDataSourceForm.type = dsType;
        this.currentStep = 1;
        return;
      }
      this.currentStep = 0;
      this.testConnectionHasResult = false;
    },
    updateAddDsRoute() {
      const query = {
        dsType: this.addDataSourceForm.type
      };
      const currentQueryKeys = Object.keys(this.$route.query || {});
      if (this.$route.query.dsType === query.dsType && currentQueryKeys.length === 1) {
        return;
      }
      this.$router.replace({ path: '/datasource/add', query });
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
  flex: 1 1 auto;
  width: 100%;
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  position: relative;
  overflow-y: auto;
  padding-bottom: 76px;
  background: #f5f8fb;
}

.add-datasource-wrapper {
  display: flex;
  flex-direction: column;
  min-height: calc(100% - 40px);
  margin: 20px;
  background: #fff;
  border: 1px solid #e3eaf2;
  border-radius: 10px;
  box-shadow: 0 10px 28px rgba(31, 41, 55, 0.04);
  overflow: visible;

  .add-datasource-content {
    /*padding: 20px;*/
    flex: 1;
    min-height: 0;
    margin-bottom: 0;
    overflow: auto;
  }
}

.add-dataSource-tools {
  position: fixed;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 20;
  display: flex;
  height: 64px;
  align-items: center;
  justify-content: center;
  background: #fff;
  border-top: 1px solid #e5e7eb;
  box-shadow: 0 -10px 22px rgba(15, 23, 42, 0.06);

  .add-dataSource-actions {
    position: absolute;
    left: 50%;
    display: flex;
    align-items: center;
    transform: translateX(-50%);
  }

  button {
    margin: 0 8px;
  }

  .test-connection-inline-msg {
    position: absolute;
    right: 36px;
    font-size: 13px;
    display: inline-flex;
    align-items: center;
    gap: 4px;
    max-width: calc(50vw - 120px);
    overflow: hidden;
    line-height: 20px;
    text-overflow: ellipsis;
    white-space: nowrap;

    &.tc-success {
      color: #19be6b;
    }

    &.tc-fail {
      color: #ed4014;
    }
  }
}

.desktop {
  padding: 0;

  .add-datasource-wrapper {
    padding: 0;
    margin-top: 0;
    border: none;

    .add-datasource-content {
      margin-bottom: 0;

      .add-datasource-step1 {
        padding: 0;
      }
    }
  }
}
</style>

<template>
  <div class="content-wrapper add-datasource-page">
    <Breadcrumb>
      <BreadcrumbItem to="/datasource">{{ $t('shu-ju-yuan-guan-li') }}</BreadcrumbItem>
      <BreadcrumbItem>{{ $t('xin-zeng-shu-ju-yuan') }}</BreadcrumbItem>
    </Breadcrumb>
    <div class="add-datasource-wrapper">
      <div class="add-datasource-flowbar">
        <div class="add-datasource-flowbar-main">
          <span v-if="selectedDsStepActive" class="add-datasource-flowbar-icon">
            <DataSourceIcon :type="addDataSourceForm.type" size="34px" leftMargin="0" />
          </span>
          <span v-else class="add-datasource-flowbar-number">1</span>
          <span class="add-datasource-flowbar-title">{{ selectedDsStepTitle }}</span>
        </div>
        <div class="add-datasource-flowbar-steps">
          <span class="add-datasource-flowbar-step" :class="{ 'is-active': currentStep === 0, 'is-done': currentStep > 0 }">
            <span class="add-datasource-flowbar-step-index">1</span>
            <span>{{ $t('xuan-ze-shu-ju-yuan') }}</span>
          </span>
          <span class="add-datasource-flowbar-line"></span>
          <span class="add-datasource-flowbar-step" :class="{ 'is-active': currentStep === 1 }">
            <span class="add-datasource-flowbar-step-index">2</span>
            <span>{{ $t('xin-zeng-shu-ju-yuan') }}</span>
          </span>
        </div>
      </div>
      <div class="add-datasource-content">
        <DataSourceInfo
          :addDataSourceForm="addDataSourceForm"
          v-if="currentStep === 0 || currentStep === 1"
          ref="dataSourceInfo"
          :current-step="currentStep"
          :show-query-config="shouldAutoEnableFeatures"
          :auto-enable-features="shouldAutoEnableFeatures"
          :driver-family-map="driverFamilyMap"
          :set-security-setting="setSecuritySetting"
          @driver-status-change="handleDriverStatusChange"
        ></DataSourceInfo>
        <SuccessAdd v-if="currentStep > 2"></SuccessAdd>
      </div>
      <div class="add-dataSource-tools">
        <Button v-if="currentStep === 0" @click="handleReturn">
          {{ $t('fan-hui-shu-ju-yuan-guan-li') }}
        </Button>
        <Button type="primary" @click="handleStep('next')" v-if="currentStep === 0">
          {{ $t('xia-yi-bu') }}
        </Button>
        <Button v-if="currentStep === 1" @click="handleStep('pre')">
          {{ $t('shang-yi-bu') }}
        </Button>
        <Button @click="handleTestConnection" :loading="testConnectionLoading" v-if="currentStep === 1">
          {{ $t('ce-shi-lian-jie') }}
        </Button>
        <Button type="primary" @click="handleAddDataSource" :loading="addDatasourceLoading" :disabled="disableAddDataSource" v-if="currentStep === 1">
          {{ $t('xin-zeng-shu-ju-yuan') }}
        </Button>
      </div>
    </div>
  </div>
</template>
<script>
import DataSourceInfo from '@/components/function/addDataSource/DataSourceInfo';
import DataSourceIcon from '@/components/function/DataSourceIcon';
import SuccessAdd from '@/components/function/addDataSource/SuccessAdd';
import { separatePort, isMySQL } from '@/utils';
import { isPostgreSQL } from '@/const/dataSource';
import { mapGetters, mapState } from 'vuex';
import deepClone from 'lodash.clonedeep';
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
    DataSourceIcon,
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
      testConnectionLoading: false
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
    disableAddDataSource() {
      return this.driverRequiredForAdd && !this.driverReadyForAdd;
    },
    selectedDsStepActive() {
      return this.currentStep >= 1 && !!this.addDataSourceForm.type;
    },
    selectedDsStepTitle() {
      if (!this.selectedDsStepActive) {
        return this.$t('xuan-ze-shu-ju-yuan');
      }
      return `${this.$t('tian-jia')} ${this.selectedDsDisplayName}`;
    },
    selectedDsDisplayName() {
      const dsType = this.addDataSourceForm.type;
      if (!dsType) {
        return '';
      }
      const supportNames = this.dmGlobalSetting?.dsSupportNames || [];
      const groups = Array.isArray(supportNames) ? supportNames : [];
      for (const group of groups) {
        const items = Array.isArray(group) ? group : [group];
        for (const item of items) {
          if (typeof item === 'string' && item === dsType) {
            return item;
          }
          if (item && item.dsKey === dsType) {
            return item.displayName || item.dsKey;
          }
        }
      }
      return dsType;
    }
  },
  beforeUnmount() {
    store.state.rdsData = [];
    store.state.addedRdsList = [];
    store.state.firstAddDataSource = true;
    store.state.selectedCluster = {};
    store.state.clusterList = [];
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
    ensureDriverReadyForAdd() {
      const driverReady = this.$refs.dataSourceInfo?.isDriverReadyForSubmit?.() ?? this.driverReadyForAdd;
      if (this.driverRequiredForAdd && !driverReady) {
        this.$Message.warning(this.$t('initialization.mysqlDriverDownloadRequired'));
        return false;
      }

      return true;
    },
    handleStep(type) {
      if (type === 'pre') {
        this.currentStep--;
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
          this.$refs.dataSourceInfo?.syncAddDsUiFormToKvConfigs?.();
          this.syncPrimaryHostFields();
          this.handleAdd();
        }
      });
    },
    handleAdd() {
      const payload = this.$refs.dataSourceInfo?.buildDsSubmitPayload?.();
      this.addDatasourceLoading = true;
      this.$services.rdpDataSourceAdd({ data: payload }).then(async (res) => {
        this.addDatasourceLoading = false;
        if (res.success) {
          this.currentStep = 4;
        }
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
        this.$services
          .dmDataSourceConnectDs({ data: payload })
          .then((res) => {
            const result = res.data || {};
            if (res.success && result.success !== false) {
              this.$Message.success(this.$t('ce-shi-lian-jie-cheng-gong'));
            } else {
              this.$Message.error(result.message || res.msg || this.$t('ce-shi-lian-jie-shi-bai'));
            }
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
    handleReturn() {
      this.$router.push({ path: '/datasource' });
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
    }
  }
};
</script>
<style lang="less">
.add-datasource-page {
  position: relative;
  overflow: hidden;
}

.add-datasource-wrapper {
  flex: 1;
  display: flex;
  min-height: 0;
  flex-direction: column;
  background: var(--bg-card);
  margin-top: 16px;
  border: 1px solid var(--border-primary);
  overflow: hidden;

  .add-datasource-content {
    /*padding: 20px;*/
    flex: 1;
    min-height: 0;
    margin-bottom: 0;
    overflow: auto;
  }
}

.add-datasource-flowbar {
  display: flex;
  min-height: 64px;
  align-items: center;
  justify-content: space-between;
  gap: 32px;
  padding: 10px 28px;
  border-bottom: 1px solid var(--border-primary);
  background: var(--bg-card);
}

.add-datasource-flowbar-main {
  display: inline-flex;
  min-width: 180px;
  align-items: center;
  gap: 12px;
}

.add-datasource-flowbar-icon,
.add-datasource-flowbar-number {
  display: inline-flex;
  width: 48px;
  height: 48px;
  flex: 0 0 48px;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--bg-card);
  border: 1px solid var(--border-primary);
  box-shadow: 0 2px 8px rgba(15, 23, 42, 0.08);
  line-height: 1;
}

.add-datasource-flowbar-number {
  background: var(--primary-color);
  border-color: var(--primary-color);
  color: #fff;
  font-weight: 600;
}

.add-datasource-flowbar-title {
  color: var(--text-primary);
  font-size: 15px;
  font-weight: 600;
  white-space: nowrap;
}

.add-datasource-flowbar-steps {
  display: inline-flex;
  min-width: 0;
  flex: 0 0 auto;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
}

.add-datasource-flowbar-step {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: var(--text-tertiary);
  font-size: 13px;
  white-space: nowrap;
}

.add-datasource-flowbar-step-index {
  display: inline-flex;
  width: 22px;
  height: 22px;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  border: 1px solid var(--border-primary);
  color: var(--text-tertiary);
  font-size: 12px;
  line-height: 1;
}

.add-datasource-flowbar-step.is-active {
  color: var(--primary-color);
  font-weight: 600;
}

.add-datasource-flowbar-step.is-active .add-datasource-flowbar-step-index,
.add-datasource-flowbar-step.is-done .add-datasource-flowbar-step-index {
  background: var(--primary-color);
  border-color: var(--primary-color);
  color: #fff;
}

.add-datasource-flowbar-line {
  height: 1px;
  width: 64px;
  flex: 0 0 64px;
  background: var(--border-primary);
}

@media screen and (max-width: 900px) {
  .add-datasource-flowbar {
    align-items: flex-start;
    flex-direction: column;
    gap: 10px;
  }

  .add-datasource-flowbar-steps {
    width: 100%;
    justify-content: flex-start;
  }

  .add-datasource-flowbar-line {
    width: 48px;
    flex-basis: 48px;
  }
}

.add-dataSource-tools {
  /*margin-top: 20px;*/
  flex: 0 0 60px;
  text-align: center;
  background: var(--bg-card);
  width: 100%;
  line-height: 60px;
  height: 60px;
  border-top: 1px solid var(--border-primary);
  box-shadow: 0 -8px 18px -18px rgba(0, 0, 0, 0.35);

  button {
    margin: 0 8px;
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

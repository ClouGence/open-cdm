<template>
  <div class="content-wrapper">
    <Breadcrumb>
      <BreadcrumbItem to="/system/ccdatasource">{{ $t('shu-ju-yuan-guan-li') }}</BreadcrumbItem>
      <BreadcrumbItem>{{ $t('xin-zeng-shu-ju-yuan') }}</BreadcrumbItem>
    </Breadcrumb>
    <div class="add-datasource-wrapper">
      <Steps class="add-dataSource-step" :current="currentStep > 1 ? 1 : currentStep">
        <Step :title="$t('xuan-ze-shu-ju-yuan')"></Step>
        <Step :title="$t('xin-zeng-shu-ju-yuan')"></Step>
      </Steps>
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
    </div>
    <div>
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
        <Button type="primary" @click="handleAddDataSource" :loading="addDatasourceLoading" :disabled="disableAddDataSource" v-if="currentStep === 1">
          {{ $t('xin-zeng-shu-ju-yuan') }}
        </Button>
      </div>
    </div>
  </div>
</template>
<script>
import DataSourceInfo from '@/components/function/addDataSource/DataSourceInfo';
import SuccessAdd from '@/components/function/addDataSource/SuccessAdd';
import { isDb2, isHana, separatePort, isMySQL } from '@/utils';
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
  instanceType: 'SELF_MAINTENANCE',
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
  securityFile: '',
  caFile: '',
  clientSecurityFile: '',
  secretFile: '',
  secretFilePassword: '',
  clientTrustStorePassword: '',
  keystoreFile: '',
  tlsTrustStoreFile: '',
  tlsTrustStoreFilePassword: '',
  tlsKeystoreFile: '',
  tlsKeystoreFilePassword: '',
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
      driverRequiredForAdd: false
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
      return !this.isDesktop && this.$route?.path === '/system/ccdatasource/add';
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
      this.addDataSourceForm.host = visibleHost.host || '';
      this.addDataSourceForm.port = visibleHost.port || '';
      this.addDataSourceForm.queryHostType = 'PUBLIC';
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
          this.syncPrimaryHostFields();
          this.handleAdd();
        }
      });
    },
    handleAdd() {
      if (!this.addDataSourceForm.host && !this.addDataSourceForm.publicHost) {
        this.$Modal.warning({
          title: this.$t('tian-jia-shu-ju-yuan-ti-shi'),
          content: this.$t('qing-tian-xie-wan-zheng-qie-zheng-que-de-shu-ju-yuan-di-zhi')
        });
      } else {
        const { dsKvConfigs } = this.addDataSourceForm;
        let { connectTypeValue } = this.addDataSourceForm;
        if (connectTypeValue && typeof connectTypeValue === 'string') {
          connectTypeValue = connectTypeValue.trim();
        }
        const formData = new FormData();
        const isSeparate = this.separatePort(this.addDataSourceForm.type);
        let host = isSeparate
          ? this.addDataSourceForm.host && this.addDataSourceForm.port
            ? `${this.addDataSourceForm.host}:${this.addDataSourceForm.port}`
            : ''
          : this.addDataSourceForm.host;
        if (this.addDataSourceForm.type === 'Db2Fori') {
          host =
            this.addDataSourceForm.host && this.addDataSourceForm.port
              ? `${this.addDataSourceForm.host}:${this.addDataSourceForm.port}`
              : this.addDataSourceForm.host;
        }
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
          deployType: this.addDataSourceForm.instanceType,
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
          accessKey: this.addDataSourceForm.accessKey,
          secretKey: this.addDataSourceForm.secretKey,
          dbName: this.addDataSourceForm.dbName || this.addDataSourceForm.noValidateDbName,
          clientTrustStorePassword: this.addDataSourceForm.clientTrustStorePassword,
          dsKvConfigs: kvConfigs,
          // extraData: {
          //   hdfsIp: this.addDataSourceForm.hdfsIp,
          //   hdfsPort: this.addDataSourceForm.hdfsPort,
          //   hdfsDwDir: this.addDataSourceForm.hdfsDwDir,
          //   hdfsPrincipal: this.addDataSourceForm.hdfsPrincipal
          // },
          driver: this.addDataSourceForm.driver,
          envId: this.addDataSourceForm.envId,
          infoFetchType: 'MANUALLY_FILL',
          secretFilePassword: this.addDataSourceForm?.secretFilePassword || ''
        };
        Object.keys(DataSourceAddData).forEach((item) => {
          if (typeof DataSourceAddData[item] === 'string') {
            DataSourceAddData[item] = DataSourceAddData[item].trim();
          }
        });

        // 处理不同类型源端字段映射
        switch (this.addDataSourceForm.type) {
          // tls类型
          case 'MySQL':
          case 'Kafka':
          case 'Tunnel':
          case 'AutoMQ':
            DataSourceAddData.securityFilePassword = this.addDataSourceForm?.tlsTrustStoreFilePassword || '';
            DataSourceAddData.clientSecurityFilePassword = this.addDataSourceForm?.tlsKeystoreFilePassword || '';
            this.addDataSourceForm.securityFile = this.addDataSourceForm.tlsTrustStoreFile;
            this.addDataSourceForm.clientSecurityFile = this.addDataSourceForm.tlsKeystoreFile;
            break;
          // ca 证书
          case 'PostgreSQL':
            this.addDataSourceForm.secretFile = this.addDataSourceForm.clientSecretFile;
            break;
          default:
            break;
        }

        formData.append('secretFile', this.addDataSourceForm?.secretFile || '');
        formData.append('securityFile', this.addDataSourceForm?.securityFile || '');
        formData.append('clientSecurityFile', this.addDataSourceForm?.clientSecurityFile || '');
        formData.append('DataSourceAddData', JSON.stringify(DataSourceAddData));

        this.addDatasourceLoading = true;
        this.$services.rdpDataSourceAdd({ data: formData }).then(async (res) => {
          this.addDatasourceLoading = false;
          if (res.success) {
            await this.enableDatasourceFeatures(res.data);
            this.currentStep = 4;
          }
        });
      }
    },
    async enableDatasourceFeatures(dataSourceId) {
      if (!this.shouldAutoEnableFeatures || !dataSourceId) {
        return;
      }

      if (!this.addDataSourceForm.queryClusterId) {
        this.$Message.warning(this.$t('shu-ju-cha-xun-wei-kai-qi'));
        return;
      }

      const queryRes = await this.$services.dmDataSourceEnableDsQuery({
        data: {
          dataSourceId,
          clusterId: this.addDataSourceForm.queryClusterId,
          hostType: this.addDataSourceForm.queryHostType
        }
      });

      if (!queryRes.success) {
        this.$Message.warning(this.$t('shu-ju-cha-xun-wei-kai-qi'));
        return;
      }

      const devopsRes = await this.$services.dmDataSourceEnableDsDevOps({
        data: {
          dataSourceId
        }
      });

      if (!devopsRes.success) {
        this.$Message.warning('CI/CD 未开启');
      }
    },
    handleAddPersonalDataSource(testDs = false) {
      this.$refs.dataSourceInfo.$refs.addLocalDs.validate((val) => {
        if (val) {
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
          deployType: this.addDataSourceForm.instanceType,
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
          dbName:
            isDb2(this.addDataSourceForm.type) || isHana(this.addDataSourceForm.type)
              ? this.addDataSourceForm.dbName
              : this.addDataSourceForm.noValidateDbName,
          clientTrustStorePassword: this.addDataSourceForm.clientTrustStorePassword,
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

        formData.append('secretFile', this.addDataSourceForm.secretFile);
        formData.append('securityFile', this.addDataSourceForm.securityFile);
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
      this.$router.push({ path: '/system/ccdatasource' });
    },
    handleReset() {
      this.addDataSourceForm = {
        fetchType: 'MANUALLY_FILL',
        host: '',
        publicHost: '',
        publicPort: '',
        type: 'MySQL',
        region: '',
        instanceType: 'SELF_MAINTENANCE',
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
        securityFile: '',
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
.add-datasource-wrapper {
  background: var(--bg-card);
  margin-top: 16px;
  border: 1px solid var(--border-primary);

  .add-datasource-content {
    /*padding: 20px;*/
    margin-bottom: 60px;
  }
}

.add-dataSource-step {
  padding: 30px 24px;
  border-bottom: 1px solid var(--border-primary);

  .ivu-steps-item,
  .ivu-steps-main {
    min-width: 0;
  }
}

@media screen and (min-width: 992px) {
  .add-dataSource-step {
    padding-left: 120px;
    padding-right: 120px;
  }
}

@media screen and (min-width: 1440px) {
  .add-dataSource-step {
    padding-left: 380px;
    padding-right: 380px;
  }
}

.add-dataSource-tools {
  /*margin-top: 20px;*/
  position: absolute;
  bottom: 0;
  left: 0;
  text-align: center;
  background: var(--bg-card);
  width: 100%;
  line-height: 60px;
  height: 60px;
  z-index: 99;
  box-shadow: 0 2px 23px 0 rgba(197, 197, 197, 0.5);

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

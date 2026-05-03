<template>
  <FormItem :label="$t('qu-dong')" key="driverSelection" v-if="currentDriverFamilies.length">
    <div class="driver-selection-row">
      <Select v-model="innerDriverFamily" style="width: 180px" @on-change="handleDriverFamilyChange">
        <Option v-for="family in currentDriverFamilies" :key="family.name" :value="family.name">
          {{ family.name }}
        </Option>
      </Select>
      <Select v-model="innerDriverVersion" style="width: 180px" @on-change="handleDriverVersionChange">
        <Option v-for="version in currentDriverVersions" :key="version" :value="version">
          {{ version }}
        </Option>
      </Select>
    </div>
    <DriverStatusLine
      :selected-driver-key="selectedDriverKey"
      :driver-family="innerDriverFamily"
      :driver-version="innerDriverVersion"
      :driver-status="driverStatus"
      @check="handleCheckDriverStatus"
      @download="handleDownloadDriver"
    />
  </FormItem>
</template>

<script>
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import DriverStatusLine from './DriverStatusLine';

const createInitialDriverStatus = () => ({
  checking: false,
  available: false,
  totalFileCount: 0,
  completedFileCount: 0,
  currentFilePercent: 0,
  status: 'IDLE',
  retryAction: 'CHECK',
  message: '',
  resourceCoordinate: '',
  currentFileName: ''
});

export default {
  name: 'DriverSelectionField',
  components: {
    DriverStatusLine
  },
  props: {
    dataSourceType: {
      type: String,
      default: ''
    },
    driverFamilyMap: {
      type: Object,
      default: () => ({})
    },
    queryClusterId: {
      type: [Number, String],
      default: null
    },
    currentQueryCluster: {
      type: Object,
      default: () => ({})
    },
    currentStep: {
      type: Number,
      default: 0
    },
    driverFamily: {
      type: String,
      default: ''
    },
    driverVersion: {
      type: String,
      default: ''
    },
    driverValue: {
      type: String,
      default: ''
    },
    driverReady: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:driverFamily', 'update:driverVersion', 'update:driverValue', 'update:driverReady'],
  data() {
    return {
      innerDriverFamily: this.driverFamily || '',
      innerDriverVersion: this.driverVersion || '',
      driverStatus: createInitialDriverStatus(),
      driverStatusRequestKey: '',
      driverStatusTimeoutId: null
    };
  },
  computed: {
    currentDriverFamilies() {
      return this.driverFamilyMap[this.dataSourceType] || [];
    },
    currentDriverVersions() {
      const family = this.currentDriverFamilies.find((item) => item.name === this.innerDriverFamily);
      return Array.isArray(family?.versions) ? family.versions : [];
    },
    selectedDriverKey() {
      return this.innerDriverFamily && this.innerDriverVersion ? `${this.innerDriverFamily}::${this.innerDriverVersion}` : '';
    },
    selectedDriverStatusKey() {
      if (!this.selectedDriverKey) {
        return '';
      }

      const clusterId = this.normalizeDriverClusterId(this.queryClusterId);
      return `${this.selectedDriverKey}::${clusterId || 'ALL'}`;
    },
    isDriverPrepared() {
      return this.driverStatus.status === 'AVAILABLE' && !!this.driverStatus.available;
    }
  },
  watch: {
    driverFamily(value) {
      if (value !== this.innerDriverFamily) {
        this.innerDriverFamily = value || '';
      }
    },
    driverVersion(value) {
      if (value !== this.innerDriverVersion) {
        this.innerDriverVersion = value || '';
      }
    },
    currentDriverFamilies: {
      handler() {
        this.applyDriverFamilySelection();
      },
      immediate: true
    },
    dataSourceType() {
      this.applyDriverFamilySelection(true);
    },
    selectedDriverStatusKey() {
      if (!this.selectedDriverStatusKey) {
        this.resetDriverStatus();
        return;
      }

      if (this.currentStep === 1) {
        this.refreshDriverStatus();
      }
    },
    currentStep(step) {
      if (step === 1) {
        this.refreshDriverStatus();
      }
    },
    isDriverPrepared: {
      handler(value) {
        this.$emit('update:driverReady', value);
      },
      immediate: true
    }
  },
  created() {
    this.$bus.on(EVENT_BUS_NAME_LIST.WS_RES_DRIVER_DOWNLOAD_EVENT, this.handleDriverDownloadEvent);
  },
  beforeUnmount() {
    this.clearDriverStatusCheckTimeout();
    this.$bus.off(EVENT_BUS_NAME_LIST.WS_RES_DRIVER_DOWNLOAD_EVENT, this.handleDriverDownloadEvent);
  },
  methods: {
    normalizeDriverClusterId(clusterId) {
      const normalized = Number(clusterId);
      return Number.isFinite(normalized) && normalized > 0 ? normalized : null;
    },
    currentClusterHasRunningWorkers() {
      return Number(this.currentQueryCluster?.runningCount) > 0;
    },
    ensureDriverClusterHasRunningWorkers() {
      if (this.currentClusterHasRunningWorkers()) {
        return true;
      }

      this.$Message.warning(this.$t('gai-ji-qun-wu-cun-huo-ji-qi'));
      return false;
    },
    getDriverClusterId() {
      const clusterId = this.normalizeDriverClusterId(this.queryClusterId);
      return clusterId || undefined;
    },
    syncDriverOutputs() {
      const driverValue =
        this.innerDriverFamily && this.innerDriverVersion ? JSON.stringify([this.innerDriverFamily, `/${this.innerDriverVersion}`]) : '';
      this.$emit('update:driverFamily', this.innerDriverFamily || '');
      this.$emit('update:driverVersion', this.innerDriverVersion || '');
      this.$emit('update:driverValue', driverValue);
    },
    applyDriverFamilySelection(forceReset = false) {
      const families = this.currentDriverFamilies;

      if (!families.length) {
        this.innerDriverFamily = '';
        this.innerDriverVersion = '';
        this.syncDriverOutputs();
        this.resetDriverStatus();
        return;
      }

      let currentFamily = families.find((item) => item.name === this.innerDriverFamily);
      if (!currentFamily || forceReset) {
        currentFamily = families[0];
        this.innerDriverFamily = currentFamily?.name || '';
      }

      const versions = Array.isArray(currentFamily?.versions) ? currentFamily.versions : [];
      if (!versions.length) {
        this.innerDriverVersion = '';
        this.syncDriverOutputs();
        this.resetDriverStatus();
        return;
      }

      if (forceReset || !versions.includes(this.innerDriverVersion)) {
        this.innerDriverVersion = versions[0];
      }

      this.syncDriverOutputs();
    },
    handleDriverFamilyChange(familyName) {
      const family = this.currentDriverFamilies.find((item) => item.name === familyName);
      const versions = Array.isArray(family?.versions) ? family.versions : [];
      this.innerDriverFamily = familyName || '';
      this.innerDriverVersion = versions.length ? versions[0] : '';
      this.syncDriverOutputs();
    },
    handleDriverVersionChange(version) {
      this.innerDriverVersion = version || '';
      this.syncDriverOutputs();
    },
    resetDriverStatus() {
      this.clearDriverStatusCheckTimeout();
      this.driverStatusRequestKey = '';
      this.driverStatus = createInitialDriverStatus();
    },
    clearDriverStatusCheckTimeout() {
      if (this.driverStatusTimeoutId) {
        clearTimeout(this.driverStatusTimeoutId);
        this.driverStatusTimeoutId = null;
      }
    },
    scheduleDriverStatusCheckTimeout(requestKey) {
      this.clearDriverStatusCheckTimeout();
      this.driverStatusTimeoutId = setTimeout(() => {
        if (this.driverStatusRequestKey !== requestKey || this.driverStatus.status !== 'CHECKING') {
          return;
        }

        this.driverStatusRequestKey = '';
        this.driverStatus = {
          ...this.driverStatus,
          checking: false,
          available: false,
          status: 'UNKNOWN',
          retryAction: 'CHECK',
          message: ''
        };
      }, 15000);
    },
    setDriverErrorStatus(message, retryAction = 'CHECK') {
      this.clearDriverStatusCheckTimeout();
      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: false,
        status: 'ERROR',
        retryAction,
        message: message || ''
      };
    },
    async refreshDriverStatus() {
      const driverKey = this.selectedDriverStatusKey;
      if (!driverKey) {
        this.resetDriverStatus();
        return;
      }

      const requestKey = `${driverKey}::${Date.now()}`;
      this.driverStatusRequestKey = requestKey;
      this.driverStatus = {
        ...this.driverStatus,
        checking: true,
        available: false,
        status: 'CHECKING',
        retryAction: 'CHECK',
        message: '',
        resourceCoordinate: '',
        currentFileName: '',
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0
      };
      this.scheduleDriverStatusCheckTimeout(requestKey);

      try {
        const res = await this.$services.rdpDataSourceCheckDriverStatus({
          data: {
            clusterId: this.getDriverClusterId(),
            driverFamily: this.innerDriverFamily,
            driverVersion: this.innerDriverVersion
          }
        });

        if (this.driverStatusRequestKey !== requestKey || this.selectedDriverStatusKey !== driverKey) {
          return;
        }

        this.clearDriverStatusCheckTimeout();

        if (res.success) {
          const available = !!res.data?.available;
          this.driverStatus = {
            ...this.driverStatus,
            checking: false,
            available,
            status: available ? 'AVAILABLE' : 'UNAVAILABLE',
            retryAction: available ? 'CHECK' : 'DOWNLOAD',
            message: ''
          };
          return;
        }

        this.setDriverErrorStatus(res.msg || '', 'CHECK');
      } catch (error) {
        if (this.driverStatusRequestKey !== requestKey || this.selectedDriverStatusKey !== driverKey) {
          return;
        }

        this.setDriverErrorStatus(error?.message || '', 'CHECK');
      }
    },
    handleCheckDriverStatus() {
      if (!this.ensureDriverClusterHasRunningWorkers()) {
        return;
      }

      this.refreshDriverStatus();
    },
    async handleDownloadDriver() {
      if (!this.innerDriverFamily || !this.innerDriverVersion) {
        return;
      }

      if (!this.ensureDriverClusterHasRunningWorkers()) {
        return;
      }

      this.clearDriverStatusCheckTimeout();
      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: false,
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0,
        status: 'DOWNLOADING',
        retryAction: 'DOWNLOAD',
        message: '',
        resourceCoordinate: '',
        currentFileName: ''
      };

      try {
        const res = await this.$services.rdpDataSourceDownloadDriver({
          data: {
            clusterId: this.getDriverClusterId(),
            driverFamily: this.innerDriverFamily,
            driverVersion: this.innerDriverVersion
          }
        });

        if (!res.success) {
          this.setDriverErrorStatus(res.msg || this.$t('xia-zai-shi-bai'), 'DOWNLOAD');
          this.$Message.error(res.msg || this.$t('xia-zai-shi-bai'));
        }
      } catch (error) {
        this.setDriverErrorStatus(error?.message || this.$t('xia-zai-shi-bai'), 'DOWNLOAD');
        this.$Message.error(error?.message || this.$t('xia-zai-shi-bai'));
      }
    },
    handleDriverDownloadEvent(payload) {
      const event = payload?.object || payload;
      if (!event) {
        return;
      }

      const isCurrentDriver =
        event.driverFamily === this.innerDriverFamily &&
        event.driverVersion === this.innerDriverVersion &&
        this.normalizeDriverClusterId(event.clusterId) === this.normalizeDriverClusterId(this.queryClusterId);
      if (!isCurrentDriver) {
        return;
      }

      this.clearDriverStatusCheckTimeout();

      if (event.status === 'COMPLETED') {
        this.driverStatus = {
          ...this.driverStatus,
          checking: false,
          available: !!event.available,
          totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
          completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
          currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
          status: 'DOWNLOADING',
          retryAction: 'DOWNLOAD',
          message: event.message || '',
          resourceCoordinate: event.resourceCoordinate || this.driverStatus.resourceCoordinate,
          currentFileName: event.currentFileName || this.driverStatus.currentFileName
        };
        this.refreshDriverStatus();
        return;
      }

      if (event.status === 'FAILED') {
        this.setDriverErrorStatus(event.message || this.$t('xia-zai-shi-bai'), 'DOWNLOAD');
        this.driverStatus = {
          ...this.driverStatus,
          totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
          completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
          currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
          resourceCoordinate: event.resourceCoordinate || this.driverStatus.resourceCoordinate,
          currentFileName: event.currentFileName || this.driverStatus.currentFileName
        };
        this.$Message.error(event.message || this.$t('xia-zai-shi-bai'));
        return;
      }

      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: !!event.available,
        totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
        completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
        currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
        status: event.status || 'DOWNLOADING',
        retryAction: 'DOWNLOAD',
        message: event.message || '',
        resourceCoordinate: event.resourceCoordinate || this.driverStatus.resourceCoordinate,
        currentFileName: event.currentFileName || this.driverStatus.currentFileName
      };
    }
  }
};
</script>

<style lang="less" scoped>
.driver-selection-row {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}
</style>

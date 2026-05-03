import { mapState } from 'vuex';

const datasourceMixin = {
  data() {
    return {
      testConnectMsg: '',
      testConnectMsgDetail: ''
    };
  },
  computed: {
    ...mapState(['globalDsSetting'])
  },
  methods: {
    async testConnection(data) {
      const res = await this.$services.dmDataSourceConnectDs({ data });
      const connectResult = res?.data && typeof res.data === 'object' ? res.data : null;
      const connectSuccess =
        connectResult && Object.prototype.hasOwnProperty.call(connectResult, 'success') ? !!connectResult.success : !!res.success;

      this.testConnectMsg = connectSuccess ? this.$t('lian-jie-cheng-gong') : this.$t('lian-jie-shi-bai-qing-jian-cha-shu-ju-yuan-deng-ru-xin-xi');
      this.testConnectMsgDetail = connectSuccess ? '' : connectResult?.message || res.msg || '';
    },
    showDropdownItem(dataSourceType, key) {
      return (
        this.globalDsSetting[dataSourceType] && this.globalDsSetting[dataSourceType].features && this.globalDsSetting[dataSourceType].features[key]
      );
    }
  }
};

export default datasourceMixin;

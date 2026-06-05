import dayjs from 'dayjs';

const authMixin = {
  methods: {
    renderDurationTime(record) {
      let str = '';
      if (!record.endTime) {
        if (!record.startTime) {
          str = this.$t('yong-jiu');
        } else {
          str = `${dayjs(record.startTime).format('YYYY-MM-DD HH:mm:ss')} ~ ${this.$t('yong-jiu')}`;
        }
      } else {
        str = `${dayjs(record.startTime).format('YYYY-MM-DD HH:mm:ss')} ~ ${dayjs(record.endTime).format('YYYY-MM-DD HH:mm:ss')}`;
      }
      return str;
    }
  }
};

export default authMixin;

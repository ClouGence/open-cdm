import i18n from '@/i18n';

const scmColumns = [
  {
    title: i18n.global.t('ti-gong-zhe'),
    slot: 'provider',
    width: 120
  },
  {
    title: i18n.global.t('zhan-shi-ming-cheng'),
    key: 'display',
    minWidth: 200
  },
  {
    title: i18n.global.t('fu-wu-di-zhi'),
    key: 'serviceUrl',
    minWidth: 600
  },
  {
    title: i18n.global.t('cao-zuo'),
    slot: 'action',
    fixed: 'right',
    width: 120
  }
];

export { scmColumns };

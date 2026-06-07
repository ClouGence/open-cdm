// import './iview.config';
import antdConfig from './antd.config';

import { createApp } from 'vue';
import VueCountdown from '@chenfengyuan/vue-countdown';
import CCIconfont from '@/components/widgets/CCIconfont';
import CCLabel from '@/components/widgets/CCLabel';
import CCRegionSelect from '@/components/widgets/CCRegionSelect';
import CCClusterTypeSelect from '@/components/widgets/CCClusterTypeSelect';
import CCStatus from '@/components/widgets/CCStatus';
import CCDataSourceIcon from '@/components/widgets/CCDataSourceIcon';
import CCPasswordInput from '@/components/widgets/CCPasswordInput';
import CCSchemaTreeSelect from '@/components/widgets/CCSchemaTreeSelect';
import CCTableTreeSelect from '@/components/widgets/CCTableTreeSelect';
import CCFilterSelect from '@/components/widgets/CCFilterSelect';
import CCSvgIcon from '@/components/widgets/CCSvgIcon';
import DataSourceIcon from '@/components/function/DataSourceIcon';
import CCPagination from './widgets/CCPagination';
export default {
  install(app) {
    app.use(antdConfig);
    app.component(VueCountdown.name, VueCountdown);

    app.component('CcIconfont', CCIconfont);
    app.component('CcLabel', CCLabel);
    app.component('CcRegionSelect', CCRegionSelect);
    app.component('CcClusterTypeSelect', CCClusterTypeSelect);
    app.component('CcStatus', CCStatus);
    app.component('CcDataSourceIcon', CCDataSourceIcon);
    app.component('CcPasswordInput', CCPasswordInput);
    app.component('CcSchemaTreeSelect', CCSchemaTreeSelect);
    app.component('CcTableTreeSelect', CCTableTreeSelect);
    app.component('CcFilterSelect', CCFilterSelect);
    app.component('CcPagination', CCPagination);
    app.component('CcSvgIcon', CCSvgIcon);
    app.component('DataSourceIcon', DataSourceIcon);
  }
};

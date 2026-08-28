import antdConfig from './antd.config';

import CCLabel from '@/components/widgets/CCLabel';
import CCStatus from '@/components/widgets/CCStatus';
import CCDataSourceIcon from '@/components/widgets/CCDataSourceIcon';
import CCSvgIcon from '@/components/widgets/CCSvgIcon';
import CCPagination from './widgets/CCPagination';
export default {
  install(app) {
    app.use(antdConfig);

    app.component('CcLabel', CCLabel);
    app.component('CcStatus', CCStatus);
    app.component('CcDataSourceIcon', CCDataSourceIcon);
    app.component('CcPagination', CCPagination);
    app.component('CcSvgIcon', CCSvgIcon);
  }
};

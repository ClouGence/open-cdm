import clickOutHide from '@/directives/clickOutHide';
// import { permission } from '@/directives/permission';

export default {
  install(app) {
    app.directive('click-out-hide', clickOutHide);
    // app.directive('permission', permission);}
  }
};

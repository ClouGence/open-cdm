import clickOutHide from '@/directives/clickOutHide';

export default {
  install(app) {
    app.directive('click-out-hide', clickOutHide);
  }
};

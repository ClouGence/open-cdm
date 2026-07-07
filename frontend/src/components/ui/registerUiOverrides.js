/* eslint-disable vue/no-reserved-component-names -- override ViewUI with Ant Design adapters */
import DmTable from '@/components/ui/DmTable.vue';
import DmButton from '@/components/ui/DmButton.vue';
import DmInput from '@/components/ui/DmInput.vue';
import DmPage from '@/components/ui/DmPage.vue';

export default {
  install(app) {
    app.component('Table', DmTable);
    app.component('Button', DmButton);
    app.component('Input', DmInput);
    app.component('Page', DmPage);
  }
};

import { createApp } from 'vue';
import ViewUIPlus from 'view-ui-plus';
import eventBus from '@/utils/eventBus';
import checkES5Support from './utils/isEs5Supported';
import vResize from '@theshy/v-resize';
import 'vue-loading-overlay/dist/css/index.css';
import PdButton from '@/components/ui/pdButton';
import CustomIcon from '@/components/function/CustomIcon';
import DataSourceIcon from '@/components/function/DataSourceIcon';
import '@imengyu/vue3-context-menu/lib/vue3-context-menu.css';
import ContextMenu from '@imengyu/vue3-context-menu';
import CommonMixin from '@/components/function/mixin/commonMixin';
import CCModal from '@/components/ui/CCModal';
import CCPasswordInput from '@/components/widgets/CCPasswordInput';
import CCIconfont from '@/components/widgets/CCIconfont';
import registerUiOverrides from '@/components/ui/registerUiOverrides';
import App from './App';
import router from './router';
import store from './store';
import services from './services/http';
import './services';
import '@/utils/errorQueueModal';
import components from '@/components';
import directives from '@/directives';
import '@/filters';
import '@/assets/iconfont/iconfont';
import './styles/global.less';
import './styles/reset.css';
import './styles/iconfont.css';
import 'view-ui-plus/dist/styles/viewuiplus.css';
import './styles/iconfont';
import './styles/app.less';
import './iconfontJs';
import './styles/iconfontCss.css';
import '@/assets/iconfont-v2/iconfont.css';
import '@/assets/iconfont-v2';
import 'tailwindcss/tailwind.css';
import i18n, { bootstrapGoogleTranslate } from './i18n';
import 'ant-design-vue/dist/reset.css';
import '@wsfe/vue-tree/style.css';
import '@wsfe/vue-tree/src/styles/index.less';
import 'vue-sonner/style.css';
import Toast from '@/utils/toast';
import { LocaleProvider } from 'ant-design-vue';
import * as filters from '@/filters';
import { supportsCloudCanalBuild } from '@/utils/product';

// Include Theme Styles
import './styles/themes/theme.less';

if (supportsCloudCanalBuild) {
  import('./styles/cloudCanal.less');
}

// Determines whether the browser supports vue3
checkES5Support();

// Create instance of Vue application
const app = createApp(App);

app.mixin(CommonMixin);

// Use plugins
app.use(i18n);
app.use(ViewUIPlus, {
  capture: false,
  modal: {
    maskClosable: false
  },
  i18n
});

app.use(registerUiOverrides);
app.use(router);
app.use(store);
app.use(vResize);
app.use(LocaleProvider);
app.use(components);
app.use(directives);
app.use(ContextMenu);

// Register global components
app.component('PdButton', PdButton);
app.component('CustomIcon', CustomIcon);
app.component('CCModal', CCModal);
app.component('DataSourceIcon', DataSourceIcon);
app.component('CcPasswordInput', CCPasswordInput);
app.component('CcIconfont', CCIconfont);

app.config.globalProperties.$bus = eventBus;
app.config.globalProperties.$services = services;
app.config.globalProperties.$i18n = i18n;
app.config.globalProperties.$filters = filters;

app.config.globalProperties.$Message = Toast;
app.config.globalProperties.$message = Toast;

// Initialize the theme system
store.dispatch('initTheme');

// Mount Application
app.mount('#app');

// Start Google Translate translation when non-basic language
bootstrapGoogleTranslate();

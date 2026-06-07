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
import { LocaleProvider } from 'ant-design-vue';
import * as filters from '@/filters';
import { supportsCloudCanalBuild } from '@/utils/product';

// 引入主题样式
import './styles/themes/theme.less';

if (supportsCloudCanalBuild) {
  import('./styles/cloudCanal.less');
}

// 判断浏览器是否支持vue3
checkES5Support();

// 创建 Vue 应用实例
const app = createApp(App);

app.mixin(CommonMixin);

// 使用插件
app.use(i18n);
app.use(ViewUIPlus, {
  capture: false,
  modal: {
    maskClosable: false
  },
  i18n
});
app.use(router);
app.use(store);
app.use(vResize);
app.use(LocaleProvider);
app.use(components);
app.use(directives);
app.use(ContextMenu);

// 注册全局组件
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

// 初始化主题系统
store.dispatch('initTheme');

// 挂载应用
app.mount('#app');

// 非基础语言时，启动 Google Translate 翻译
bootstrapGoogleTranslate();

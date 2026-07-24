import '@/utils/dayjsSetup';
import { createApp } from 'vue';
import {
  Alert,
  Breadcrumb,
  BreadcrumbItem,
  Button,
  ButtonGroup,
  Card,
  Checkbox,
  CheckboxGroup,
  Circle,
  Col,
  Collapse,
  DatePicker,
  Divider,
  Drawer,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  Form,
  FormItem,
  Icon,
  Input,
  InputNumber,
  Menu,
  MenuItem,
  Message,
  Modal,
  Option,
  OptionGroup,
  Page,
  Panel,
  Poptip,
  Progress,
  Radio,
  RadioGroup,
  Row,
  Select,
  Space,
  Spin,
  Step,
  Steps,
  Switch,
  Table,
  TabPane,
  Tabs,
  Tag,
  Tooltip,
  Tree,
  Upload
} from 'view-ui-plus';
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
import i18n from './i18n';
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
// Register view-ui-plus components globally
const iviewComponents = {
  Alert,
  Breadcrumb,
  BreadcrumbItem,
  Button,
  ButtonGroup,
  Card,
  Checkbox,
  CheckboxGroup,
  DatePicker,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  Form,
  FormItem,
  Icon,
  Input,
  InputNumber,
  Menu,
  MenuItem,
  Modal,
  Option,
  OptionGroup,
  Page,
  Poptip,
  Radio,
  RadioGroup,
  Select,
  Table,
  TabPane,
  Tabs,
  Tooltip,
  Tree,
  Divider,
  Drawer,
  'i-switch': Switch,
  'i-button': Button,
  'i-input': Input,
  'i-alert': Alert,
  'i-form': Form,
  'i-form-item': FormItem,
  'i-checkbox': Checkbox,
  Row,
  Col,
  Steps,
  Step,
  Collapse,
  Panel,
  'i-circle': Circle,
  Progress,
  Spin,
  Tag,
  Space,
  Upload
};
Object.keys(iviewComponents).forEach((key) => {
  app.component(key, iviewComponents[key]);
});
// Extend Modal.confirm to honor a className option by tagging the freshly
// mounted modal wrap. View UI Plus does not pass className through to the
// modal wrap on its own, but we want destructive confirms (deletes) to be
// able to opt their OK button into the error color via styles/modal.less.
const originalModalConfirm = Modal.confirm.bind(Modal);
Modal.confirm = function patchedConfirm(props = {}) {
  const result = originalModalConfirm(props);
  if (props.className) {
    requestAnimationFrame(() => {
      const wraps = document.body.querySelectorAll('.ivu-modal-wrap');
      const latest = wraps[wraps.length - 1];
      if (latest) latest.classList.add(props.className);
    });
  }
  return result;
};
app.config.globalProperties.$Modal = Modal;
app.config.globalProperties.$Message = Message;
app.config.globalProperties.$Spin = Spin;

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

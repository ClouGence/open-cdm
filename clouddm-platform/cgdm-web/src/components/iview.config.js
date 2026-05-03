import {
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
  Menu,
  MenuItem,
  Message,
  Modal,
  Option,
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
  Switch,
  Row,
  Col,
  Steps,
  Step,
  Collapse,
  Panel,
  TimePicker,
  Circle,
  Progress,
  Spin,
  Tag,
  Space
} from 'view-ui-plus';
import * as Vue from 'vue';

const components = {
  Menu,
  Button,
  MenuItem,
  Table,
  Select,
  Option,
  Input,
  Icon,
  Modal,
  Message,
  Tabs,
  TabPane,
  Form,
  FormItem,
  Alert,
  Breadcrumb,
  BreadcrumbItem,
  Page,
  Tooltip,
  Poptip,
  Tree,
  DropdownItem,
  RadioGroup,
  Radio,
  DatePicker,
  Card,
  ButtonGroup,
  Dropdown,
  DropdownMenu,
  CheckboxGroup,
  Checkbox,
  Divider,
  TimePicker,
  Tag,
  'i-switch': Switch,
  'i-button': Button,
  'i-input': Input,
  'i-alert': Alert,
  'i-form': Form,
  'i-form-item': FormItem,
  'i-checkbox': Checkbox,
  Row,
  Steps,
  Step,
  Collapse,
  Panel,
  'i-circle': Circle,
  Progress,
  Spin,
  Col,
  Space
};

import { createApp } from 'vue';
import App from '../App.vue';

const app = createApp(App);

// 全局注册组件
Object.keys(components).forEach((key) => {
  app.component(key, components[key]);
});

// 全局挂载方法或属性
app.config.globalProperties.$Modal = Modal;
app.config.globalProperties.$Message = Message;
app.config.globalProperties.$Spin = Spin;

app.mount('#app');

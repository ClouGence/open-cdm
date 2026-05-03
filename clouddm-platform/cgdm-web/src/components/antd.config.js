import { createApp } from 'vue';
import 'ant-design-vue/dist/reset.css';

// 仅按需引入你要用的组件
import {
  Alert,
  Breadcrumb,
  Button,
  Checkbox,
  DatePicker,
  Dropdown,
  Form,
  Input,
  Menu,
  Modal,
  Popconfirm,
  Popover,
  Radio,
  Select,
  Switch,
  Table,
  Tabs,
  Tooltip,
  Tree,
  Pagination,
  Upload,
  Spin,
  ConfigProvider,
  Collapse,
  Tag,
  TimePicker,
  Progress,
  Drawer,
  message,
  Skeleton,
  Empty
} from 'ant-design-vue';

export default {
  install(app) {
    app.use(Alert);
    app.use(Breadcrumb);
    app.use(Button);
    app.use(Checkbox);
    app.use(DatePicker);
    app.use(Dropdown);
    app.use(Form);
    app.use(Input);
    app.use(Menu);
    app.use(Modal);
    app.use(Popconfirm);
    app.use(Popover);
    app.use(Radio);
    app.use(Select);
    app.use(Switch);
    app.use(Table);
    app.use(Tabs);
    app.use(Tooltip);
    app.use(Tree);
    app.use(Pagination);
    app.use(Upload);
    app.use(Spin);
    app.use(ConfigProvider);
    app.use(Collapse);
    app.use(Tag);
    app.use(TimePicker);
    app.use(Progress);
    app.use(Drawer);
    app.use(Skeleton);
    app.use(Empty);

    app.config.globalProperties.$message = message;
  }
};

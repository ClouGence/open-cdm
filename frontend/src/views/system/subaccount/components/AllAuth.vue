<template>
  <div>
    <a-tabs type="card" v-model="active" size="small">
      <a-tab-pane key="instance" :tab="`实例权限(${instanceList.length})`" />
      <a-tab-pane key="library" :tab="`库权限(${libraryList.length})`" />
      <a-tab-pane key="schema" :tab="`schema权限(${schemaList.length})`" />
      <a-tab-pane key="table" :tab="`表权限(${tableList.length})`" />
    </a-tabs>
    <a-table :columns="columns" size="small" :data-source="tabData" :rowKey="(record) => record.path" bordered>
      <template #auth-header>
        {{ computedAuthHeader }}
      </template>
      <template #name="record">{{ record.dsEnv }}/{{ record.instanceId }}{{ record.path }}</template>
      <template v-for="auth in dsAuthKind" #[`${auth.kindValue}-title`]>
        <div :key="auth.kindValue">
          {{ auth.kindNameI18n }}
        </div>
      </template>
      <template v-for="kind in dsAuthKind" #[kind.kindValue]="record">
        <div :key="kind.kindValue">
          <cc-iconfont name="check" v-if="record.dsAuthKinds.includes(kind.kindValue)" />
        </div>
      </template>
      <template #duration="record">
        {{ renderDurationTime(record) }}
      </template>
    </a-table>
  </div>
</template>

<script>
import authMixin from '../../../../mixins/authMixin';

export default {
  name: 'AllAuth',
  mixins: [authMixin],
  props: {
    subAccount: Object,
    authData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      active: 'instance',
      dsAuthKind: [],
      instanceList: [],
      libraryList: [],
      schemaList: [],
      tableList: []
    };
  },
  computed: {
    computedAuthHeader() {
      switch (this.active) {
        case 'instance':
          return '实例';
        case 'library':
          return '库';
        case 'schema':
          return 'schema';
        case 'table':
          return '表';
        default:
          return '表';
      }
    },
    columns() {
      // const { text } = this.currentQuery;
      const dsColumns = [
        {
          customRender: 'name',
          title: 'auth-header',
          key: 'name'
          // filteredValue: [text] || null,
          // onFilter: (value, record) => record.key.includes(value)
        }
      ];
      this.dsAuthKind.forEach((auth) => {
        dsColumns.push({
          title: `${auth.kindValue}-title`,
          customRender: auth.kindValue,
          align: 'center'
        });
      });

      dsColumns.push({
        title: '生效时间',
        width: 290,
        customRender: 'duration'
      });

      return dsColumns;
    },
    tabData() {
      let data = [{}];
      switch (this.active) {
        case 'instance':
          data = this.instanceList;
          break;
        case 'library':
          data = this.libraryList;
          break;
        case 'schema':
          data = this.schemaList;
          break;
        case 'table':
          data = this.tableList;
          break;
        default:
          break;
      }

      return data;
    }
  },
  methods: {
    async getDsAuthKind() {
      const res = await this.$services.rdpDataAuthListAllDsAuthKind();
      if (res.success) {
        this.dsAuthKind = res.data;
      }
    },
    generateAuthData(data) {
      const instanceList = [];
      const libraryList = [];
      const schemaList = [];
      const tableList = [];

      data.forEach((auth) => {
        if (auth.path && auth.path !== '/' && auth.path[auth.path.length - 1] === '/') {
          auth.path = auth.path.substr(0, auth.path.length - 1);
        }
      });

      data.forEach((auth) => {
        if (auth.diffuse) {
          const keys = auth.path.split('/');
          if (keys.length === 2) {
            if (keys[1] === '') {
              instanceList.push(auth);
            } else {
              libraryList.push(auth);
            }
          } else {
            schemaList.push(auth);
          }
        } else {
          tableList.push(auth);
        }
      });

      this.instanceList = instanceList;
      this.libraryList = libraryList;
      this.schemaList = schemaList;
      this.tableList = tableList;
    },
    async listUserAllDsAuth() {
      if (this.authData.length) {
        this.generateAuthData(this.authData);
      } else {
        const res = await this.$services.rdpDataAuthListUserAllDsAuth({
          data: { ownerUid: this.subAccount.uid }
        });

        if (res.success) {
          this.generateAuthData(res.data);
        }
      }
    }
  },
  created() {
    this.listUserAllDsAuth();
    this.getDsAuthKind();
  }
};
</script>

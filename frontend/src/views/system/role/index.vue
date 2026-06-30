<template>
  <div class="role">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option border-radius-card">
            <div class="left">
              <Input
                v-model.trim="search.roleName"
                :placeholder="$t('qing-shu-ru-jiao-se-ming-cheng')"
                style="width: 280px; margin-right: 10px"
                @on-keydown="handleEnterSearch"
                @on-clear="handleSearchRole"
                clearable
              />
              <Button :loading="loading" type="primary" ghost @click="handleSearchRole">
                {{ $t('cha-xun') }}
              </Button>
            </div>
            <div class="right">
              <Button v-if="myAuth.includes('RDP_ROLE_MANAGE')" type="primary" @click="handleClickAddBtn" icon="md-add">
                {{ $t('chuang-jian-jiao-se') }}
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table :columns="roleColumns" :data="showRoleList" size="small" :loading="loading" border stripe>
              <template #roleName="{ row }">
                <div class="role-name-container">
                  {{ row.aliasName || row.roleName }}
                  <span v-if="row.innerTag" class="inner-tag">{{ $t('nei-zhi') }}</span>
                </div>
              </template>
              <template #action="{ row }">
                <Button v-if="myAuth.includes('RDP_ROLE_MANAGE')" type="text" size="small" @click="handleEditRole('view', row)">
                  {{ $t('cha-kan') }}
                </Button>
                <Button v-if="myAuth.includes('RDP_ROLE_MANAGE') && !row.innerTag" type="text" size="small" @click="handleEditRole('edit', row)">
                  {{ $t('bian-ji') }}
                </Button>
                <Poptip
                  confirm
                  transfer
                  v-if="myAuth.includes('RDP_ROLE_MANAGE') && !row.innerTag"
                  :cancel-text="$t('qu-xiao')"
                  :ok-text="$t('que-ding')"
                  :title="$t('que-ding-shan-chu-gai-jiao-se-ma')"
                  @on-ok="handleDeleteRole(row)"
                >
                  <Button type="text" size="small">{{ $t('shan-chu') }}</Button>
                </Poptip>
              </template>
            </Table>
          </div>
        </div>
      </div>
      <div class="footer">
        <Page
          :total="total"
          show-total
          show-elevator
          @on-change="handlePageChange"
          show-sizer
          :page-size="pageSize"
          @on-page-size-change="handlePageSizeChange"
          :model-value="pageNum"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  name: 'Role',
  computed: {
    ...mapState(['userInfo', 'myAuth'])
  },
  data() {
    return {
      loading: false,
      search: {
        roleName: ''
      },
      roleColumns: [
        {
          title: this.$t('jiao-se-ming-cheng'),
          slot: 'roleName'
        },
        {
          title: this.$t('cao-zuo'),
          width: 290,
          slot: 'action'
        }
      ],
      total: 0,
      pageSize: 20,
      pageNum: 1,
      roleList: [],
      showRoleList: [],
      selectOptions: []
    };
  },
  methods: {
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.setTableShowData();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.handlePageChange(1);
    },
    setTableShowData(type) {
      if (type) {
        this.pageNum = 1;
      }
      const { pageNum, pageSize } = this;
      const filteredRoleList = this.getFilteredRoleList();
      this.total = filteredRoleList.length;
      this.showRoleList = filteredRoleList.slice((pageNum - 1) * pageSize, pageNum * pageSize);
    },
    getFilteredRoleList() {
      const keyword = (this.search.roleName || '').trim().toLowerCase();
      if (!keyword) {
        return this.roleList;
      }
      return this.roleList.filter((role) => {
        const aliasName = `${role.aliasName || ''}`.toLowerCase();
        const roleName = `${role.roleName || ''}`.toLowerCase();
        return aliasName.includes(keyword) || roleName.includes(keyword);
      });
    },
    handleSearchRole() {
      this.setTableShowData('init');
    },
    handleEnterSearch(event) {
      if (event.key === 'Enter' || event.keyCode === 13) {
        this.handleSearchRole();
      }
    },
    handleEditRole(type, record) {
      this.$router.push(`/manager/role/${record.roleId}/${type}`);
    },
    handleClickAddBtn() {
      this.$router.push('/manager/role/create');
    },
    async handleDeleteRole(role) {
      const data = { roleId: role.roleId };
      const res = await this.$services.rdpRoleDeleteRole({
        data,
        msg: this.$t('shan-chu-jiao-se-cheng-gong')
      });
      if (res.success) {
        await this.getRoleList();
      }
    },
    async getRoleList(searchType) {
      this.loading = true;
      const roleListRes = await this.$services.rdpRoleListRole();
      this.loading = false;
      if (roleListRes.success) {
        this.roleList = roleListRes.data || [];
        this.setTableShowData(searchType);
      }
    }
  },
  mounted() {
    this.getRoleList();
  }
};
</script>

<style lang="less" scoped>
.role {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.role-name-container {
  display: flex;
  align-items: center;
}

.inner-tag {
  background-color: #eaaa45;
  color: white;
  padding: 1px 4px;
  border-radius: 3px;
  font-size: 10px;
  margin-left: 6px;
  white-space: nowrap;
}
</style>

<template>
  <div class="role">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('jiao-se-guan-li') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="option border-radius-card">
            <div class="left"></div>
            <div class="right">
              <Button
                v-if="myAuth.includes('RDP_ROLE_MANAGE')"
                ghost
                type="primary"
                @click="handleClickAddBtn"
                icon="md-add"
                style="margin-right: 10px"
              >
                {{ $t('chuang-jian-jiao-se') }}
              </Button>
              <Button class="refresh" @click="getRoleList('init')" :loading="loading">
                <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
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
    <CCModal
      v-model="showAddNewRoleModal"
      :title="isEditing ? (roleModalType === 'edit' ? $t('bian-ji-jue-se') : $t('cha-kan-jue-se')) : $t('chuang-jian-jue-se')"
      :width="548"
      :mask-closable="false"
      @on-cancel="hideAddNewRoleModal"
    >
      <div class="new-role-modal">
        <Form ref="addNewRoleForm" :model="newRole" :rules="rules">
          <FormItem :label="$t('jiao-se-ming-cheng')" prop="roleName">
            <Input :disabled="isEditing" v-model="newRole.roleName" style="width: 420px" />
          </FormItem>
        </Form>
        <div class="auth">
          <div class="title">{{ $t('jiao-se-quan-xian') }}</div>
          <div class="content border-radius-card">
            <a-tree
              v-model:checkedKeys="checkedKeys"
              :tree-data="treeData"
              checkable
              :disabled="roleModalType === 'view'"
              :replace-fields="replaceFields"
              v-model:expandedKeys="expandedKeys"
            ></a-tree>
          </div>
        </div>
      </div>
      <template #footer>
        <div>
          <Button @click="hideAddNewRoleModal">{{ $t('guan-bi') }}</Button>
          <Button type="primary" @click="handleAddRole" :loading="loading" v-if="roleModalType !== 'view'">
            {{ isEditing ? $t('xiu-gai') : $t('chuang-jian') }}
          </Button>
        </div>
      </template>
    </CCModal>
  </div>
</template>

<script>
import cloneDeep from 'lodash.clonedeep';
import { mapState } from 'vuex';

export default {
  name: 'Role',
  computed: {
    ...mapState(['userInfo', 'myAuth'])
  },
  data() {
    return {
      loading: false,
      roleModalType: '',
      replaceFields: {
        title: 'i18nName'
      },
      isEditing: false,
      mustCheckedKeys: [],
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
      rules: {
        roleName: [
          {
            required: true,
            trigger: 'blur',
            message: this.$t('jiao-se-ming-cheng-bu-neng-wei-kong')
          }
        ]
      },
      checkedKeys: [],
      expandedKeys: [],
      newRole: {
        roleName: ''
      },
      treeData: [],
      showAddNewRoleModal: false,
      total: 0,
      pageSize: 20,
      pageNum: 1,
      roleList: [],
      showRoleList: [],
      selectOptions: [],
      checkedAll: false,
      allAuthKeys: [],
      categoryKeys: []
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
      this.showRoleList = this.roleList.slice((pageNum - 1) * pageSize, pageNum * pageSize);
    },
    handleEditRole(type, record) {
      this.isEditing = true;
      this.roleModalType = type;
      this.showAddNewRoleModal = true;
      this.newRole = cloneDeep(record);
      this.newRole.roleName = this.newRole.aliasName;
      this.checkedKeys = [...record.roleLabels];
      this.getAllAuthLabel(true);
      this.$nextTick(() => {
        this.$refs.addNewRoleForm.resetFields();
      });
    },
    handleAddRole() {
      this.loading = true;
      this.$refs.addNewRoleForm.validate(async (valid) => {
        if (valid) {
          if (!this.checkedKeys.length) {
            this.$Message.error(this.$t('qing-xuan-ze-quan-xian'));
            return;
          }
          const authLabelList = this.checkedKeys.filter((key) => !this.categoryKeys.includes(key) && key !== 'ALL');
          const data = {
            roleName: this.newRole.roleName,
            authLabelList
          };

          let res;
          if (!this.isEditing) {
            res = await this.$services.rdpRoleCreateRole({
              data,
              msg: this.$t('tian-jia-jiao-se-cheng-gong')
            });
          } else {
            data.roleId = this.newRole.roleId;
            res = await this.$services.rdpRoleUpdateRole({
              data,
              msg: this.$t('xiu-gai-jiao-se-cheng-gong')
            });
          }

          if (res.success) {
            await this.getRoleList();
            this.hideAddNewRoleModal();
          }
        }
      });
      this.loading = false;
    },
    hideAddNewRoleModal() {
      this.newRole = { roleName: '' };
      this.checkedAll = false;
      this.isEditing = false;
      this.checkedKeys = [];
      this.expandedKeys = [];
      this.mustCheckedKeys = [];
      this.showAddNewRoleModal = false;
    },
    async getAllAuthLabel(edit = false) {
      const subAccountRes = await this.$services.rdpRoleListRoleAuthLabelTree();
      const temp = {};
      const allAuthKeys = [];
      const categoryKeys = [];
      const tempTreeData = [];
      const mustCheckedKeys = [];
      const treeData = [
        {
          children: [],
          i18nName: this.$t('quan-bu'),
          key: 'ALL',
          mustSelectAndReadOnly: false
        }
      ];
      if (subAccountRes.success) {
        const formatData = (node) => {
          allAuthKeys.push(node.key);
          if (node.category) {
            categoryKeys.push(node.key);
          }
          node.title = node.i18nName;
          if (node.mustSelectAndReadOnly) {
            mustCheckedKeys.push(node.key);
          }

          if (node.children && node.children.length) {
            this.expandedKeys.push(node.key);
            node.children.forEach((child) => {
              formatData(child);
            });
          }
        };
        const { data } = subAccountRes;
        treeData[0].children = data;
        formatData(treeData[0]);

        this.treeData = treeData;
        this.allAuthKeys = allAuthKeys;
        this.categoryKeys = categoryKeys;
        this.mustCheckedKeys = [...mustCheckedKeys];
        this.checkedKeys = [...mustCheckedKeys];
        if (edit) {
          const res = await this.$services.rdpRoleFetchRole({
            data: {
              roleId: this.newRole.roleId
            }
          });
          if (res.success) {
            this.checkedKeys = res.data.roleLabels;
          }
        }
      }
    },
    handleClickAddBtn() {
      this.showAddNewRoleModal = true;
      this.roleModalType = 'edit';
      this.getAllAuthLabel();
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
        this.roleList = roleListRes.data;
        this.total = roleListRes.data.length;
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

.new-role-modal {
  section {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .auth {
    .title {
      margin-bottom: 10px;
    }

    .all {
      height: 30px;
      width: 100%;
      background: #f3f3f3;
      line-height: 30px;
      padding-left: 10px;
      border: 1px solid rgba(204, 204, 204, 1);
      border-bottom: none;
    }

    .content {
      width: 500px;
      height: 270px;
      border: 1px solid rgba(218, 218, 218, 1);
      overflow: scroll;
    }
  }
}

.rule-default-tag {
  display: flex;
  align-items: center;
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

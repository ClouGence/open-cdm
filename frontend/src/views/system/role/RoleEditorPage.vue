<template>
  <div class="role-editor-page">
    <Spin v-if="loading" fix />
    <div class="role-editor-layout">
      <section class="role-editor-section role-editor-card">
        <div class="role-basic-section">
          <div class="role-basic-header">
            <div class="role-editor-section-title">{{ $t('ji-chu-xin-xi') }}</div>
            <div class="role-editor-actions">
              <Button @click="goBack">{{ isView ? $t('fan-hui') : $t('qu-xiao') }}</Button>
              <Button v-if="!isView && !roleForm.innerTag" type="primary" :loading="submitLoading" @click="handleSubmit">
                {{ isCreate ? $t('chuang-jian') : $t('bao-cun') }}
              </Button>
            </div>
          </div>
          <Form ref="roleForm" :model="roleForm" :rules="formRules" :label-width="104" class="role-basic-form">
            <FormItem :label="$t('jiao-se-ming-cheng')" prop="roleName">
              <Input :disabled="!isCreate" v-model="roleForm.roleName" :placeholder="$t('qing-shu-ru-jiao-se-ming-cheng')" />
            </FormItem>
          </Form>
        </div>

        <div class="role-auth-section">
          <div class="role-editor-section-title">{{ $t('quan-xian-pei-zhi') }}</div>
          <div class="role-auth-toolbar">
            <Input
              v-model.trim="authSearchText"
              class="role-auth-search"
              icon="ios-search"
              :placeholder="$t('sou-suo-quan-xian-ming-cheng')"
              clearable
              @on-clear="handleAuthFilterChange"
              @on-change="handleAuthFilterChange"
            />
            <Button type="text" class="role-auth-tool-btn" @click="handleExpandAllAuth">
              <Icon type="ios-arrow-down" />
              {{ $t('zhan-kai-quan-bu') }}
            </Button>
            <Button type="text" class="role-auth-tool-btn" @click="handleCollapseAllAuth">
              <Icon type="ios-arrow-up" />
              {{ $t('shou-qi-quan-bu') }}
            </Button>
            <div class="role-auth-switch">
              <span>{{ $t('zhi-kan-yi-xuan') }}</span>
              <i-switch v-model="onlyShowSelected" true-color="#18b566" @on-change="handleAuthFilterChange" />
            </div>
            <Button type="text" class="role-auth-tool-btn" :disabled="!canEditAuth" @click="handleSelectAllAuth">
              <Icon type="ios-checkmark-circle-outline" />
              {{ $t('quan-xuan') }}
            </Button>
            <Button type="text" class="role-auth-tool-btn" :disabled="!canEditAuth" @click="handleClearAuth">
              <Icon type="ios-trash-outline" />
              {{ $t('qing-kong') }}
            </Button>
          </div>
          <div class="role-auth-tree-container">
            <a-tree
              v-if="displayTreeData.length"
              :checked-keys="checkedKeys"
              :tree-data="displayTreeData"
              checkable
              :selectable="false"
              :disabled="!canEditAuth"
              :replace-fields="replaceFields"
              v-model:expandedKeys="expandedKeys"
              @check="handleAuthCheckedChange"
            ></a-tree>
            <div v-else class="role-auth-empty">{{ $t('zan-wu-shu-ju') }}</div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RoleEditorPage',
  data() {
    return {
      loading: false,
      submitLoading: false,
      replaceFields: {
        title: 'i18nName'
      },
      roleForm: {
        roleId: '',
        roleName: '',
        innerTag: false
      },
      checkedKeys: [],
      expandedKeys: [],
      authFilterExpandedKeys: [],
      authFilterActive: false,
      authSearchText: '',
      onlyShowSelected: false,
      treeData: [],
      allAuthKeys: [],
      categoryKeys: [],
      mustCheckedKeys: [],
      authIncludeMap: {}
    };
  },
  computed: {
    isCreate() {
      return this.$route.name === 'Management_Role_Create';
    },
    isEdit() {
      return this.$route.name === 'Management_Role_Edit';
    },
    isView() {
      return this.$route.name === 'Management_Role_View';
    },
    roleId() {
      return this.$route.params.roleId;
    },
    canEditAuth() {
      return !this.isView && !this.roleForm.innerTag;
    },
    formRules() {
      return {
        roleName: [
          {
            required: true,
            trigger: 'blur',
            message: this.$t('jiao-se-ming-cheng-bu-neng-wei-kong')
          }
        ]
      };
    },
    displayTreeData() {
      return this.getDisplayTreeData();
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    async init() {
      this.loading = true;
      await this.getAllAuthLabel();
      if (!this.isCreate) {
        await this.fetchRoleDetail();
      }
      this.loading = false;
    },
    async fetchRoleDetail() {
      const res = await this.$services.rdpRoleFetchRole({
        data: {
          roleId: this.roleId
        }
      });
      if (!res.success || !res.data) {
        this.goBack();
        return;
      }
      this.roleForm = {
        roleId: res.data.roleId,
        roleName: res.data.aliasName || res.data.roleName,
        innerTag: res.data.innerTag
      };
      this.checkedKeys = Array.isArray(res.data.selectedRoleLabels)
        ? [...res.data.selectedRoleLabels]
        : this.compactAuthKeys(res.data.roleLabels || []);
    },
    async getAllAuthLabel() {
      const res = await this.$services.rdpRoleListRoleAuthLabelTree();
      const allAuthKeys = [];
      const categoryKeys = [];
      const mustCheckedKeys = [];
      const expandableKeys = [];
      const authIncludeMap = {};
      const treeData = [
        {
          children: [],
          i18nName: this.$t('quan-bu'),
          key: 'ALL',
          mustSelectAndReadOnly: false
        }
      ];

      if (!res.success) {
        return;
      }

      const formatData = (node) => {
        allAuthKeys.push(node.key);
        if (node.category) {
          categoryKeys.push(node.key);
        } else {
          authIncludeMap[node.key] = Array.isArray(node.include) ? node.include.filter(Boolean) : [];
        }
        node.title = node.i18nName;
        if (node.mustSelectAndReadOnly) {
          mustCheckedKeys.push(node.key);
        }
        if (node.children && node.children.length) {
          expandableKeys.push(node.key);
          node.children.forEach((child) => formatData(child));
        }
      };

      treeData[0].children = res.data || [];
      formatData(treeData[0]);
      this.treeData = treeData;
      this.allAuthKeys = allAuthKeys;
      this.categoryKeys = categoryKeys;
      this.mustCheckedKeys = [...mustCheckedKeys];
      this.authIncludeMap = authIncludeMap;
      this.expandedKeys = expandableKeys;
      this.checkedKeys = [...mustCheckedKeys];
    },
    getCascadeIncludeKeys(key, seen = new Set()) {
      if (!key || seen.has(key)) {
        return [];
      }
      seen.add(key);
      const includes = this.authIncludeMap[key] || [];
      return includes.reduce((result, includeKey) => {
        result.push(includeKey);
        result.push(...this.getCascadeIncludeKeys(includeKey, seen));
        return result;
      }, []);
    },
    compactAuthKeys(keys = []) {
      const sourceKeys = Array.from(new Set(keys || []));
      const sourceKeySet = new Set(sourceKeys);
      const includedKeySet = new Set();
      sourceKeys.forEach((key) => {
        this.getCascadeIncludeKeys(key).forEach((includeKey) => {
          if (sourceKeySet.has(includeKey) && !this.mustCheckedKeys.includes(includeKey)) {
            includedKeySet.add(includeKey);
          }
        });
      });
      return sourceKeys.filter((key) => !includedKeySet.has(key));
    },
    isAuthKey(key) {
      return key && key !== 'ALL' && !this.categoryKeys.includes(key);
    },
    normalizeCheckedAuthKeys(keys = []) {
      return Array.from(new Set([...(keys || []).filter((key) => this.isAuthKey(key)), ...this.mustCheckedKeys]));
    },
    getDisplayTreeData() {
      const keyword = (this.authSearchText || '').trim().toLowerCase();
      const checkedKeySet = new Set(this.checkedKeys || []);
      const filterNode = (node) => {
        const children = (node.children || []).map(filterNode).filter(Boolean);
        const nodeName = `${node.i18nName || node.title || ''}`.toLowerCase();
        const selfMatchSearch = !keyword || nodeName.includes(keyword);
        const selfMatchSelected = !this.onlyShowSelected || checkedKeySet.has(node.key);
        if ((selfMatchSearch && selfMatchSelected) || children.length) {
          return {
            ...node,
            children
          };
        }
        return null;
      };
      return (this.treeData || []).map(filterNode).filter(Boolean);
    },
    getExpandableKeys(data = this.treeData) {
      const keys = [];
      const collect = (nodes = []) => {
        nodes.forEach((node) => {
          if (node.children && node.children.length) {
            keys.push(node.key);
            collect(node.children);
          }
        });
      };
      collect(data);
      return keys;
    },
    getTreeAuthKeys(data = this.treeData) {
      const keys = [];
      const collect = (nodes = []) => {
        nodes.forEach((node) => {
          if (!node.category && node.key !== 'ALL') {
            keys.push(node.key);
          }
          if (node.children && node.children.length) {
            collect(node.children);
          }
        });
      };
      collect(data);
      return keys;
    },
    isAuthFilterActive() {
      return Boolean((this.authSearchText || '').trim() || this.onlyShowSelected);
    },
    handleAuthFilterChange() {
      this.$nextTick(() => {
        if (this.isAuthFilterActive()) {
          if (!this.authFilterActive) {
            this.authFilterExpandedKeys = [...this.expandedKeys];
          }
          this.authFilterActive = true;
          this.expandedKeys = this.getExpandableKeys(this.displayTreeData);
          return;
        }

        this.authFilterActive = false;
        const defaultExpandedKeys = this.getExpandableKeys(this.treeData);
        this.expandedKeys = this.authFilterExpandedKeys.length ? [...this.authFilterExpandedKeys] : defaultExpandedKeys;
        this.authFilterExpandedKeys = [];
      });
    },
    handleAuthCheckedChange(checkedKeys) {
      const emittedCheckedKeys = this.normalizeCheckedAuthKeys(Array.isArray(checkedKeys) ? checkedKeys : checkedKeys?.checked || []);
      if (!this.isAuthFilterActive()) {
        this.checkedKeys = emittedCheckedKeys;
      } else {
        const visibleAuthKeySet = new Set(this.getTreeAuthKeys(this.displayTreeData));
        const hiddenCheckedKeys = this.normalizeCheckedAuthKeys(this.checkedKeys).filter((key) => !visibleAuthKeySet.has(key));
        const visibleCheckedKeys = emittedCheckedKeys.filter((key) => visibleAuthKeySet.has(key));
        this.checkedKeys = this.normalizeCheckedAuthKeys([...hiddenCheckedKeys, ...visibleCheckedKeys]);
      }
      if (this.onlyShowSelected) {
        this.handleAuthFilterChange();
      }
    },
    handleExpandAllAuth() {
      this.expandedKeys = this.getExpandableKeys(this.displayTreeData.length ? this.displayTreeData : this.treeData);
    },
    handleCollapseAllAuth() {
      this.expandedKeys = [];
    },
    handleSelectAllAuth() {
      if (!this.canEditAuth) {
        return;
      }
      const sourceTree = this.isAuthFilterActive() ? this.displayTreeData : this.treeData;
      const sourceAuthKeys = this.getTreeAuthKeys(sourceTree);
      this.checkedKeys = this.isAuthFilterActive()
        ? this.normalizeCheckedAuthKeys([...this.checkedKeys, ...sourceAuthKeys])
        : this.normalizeCheckedAuthKeys(sourceAuthKeys);
      this.handleAuthFilterChange();
    },
    handleClearAuth() {
      if (!this.canEditAuth) {
        return;
      }
      this.checkedKeys = [...this.mustCheckedKeys];
      this.handleAuthFilterChange();
    },
    handleSubmit() {
      this.$refs.roleForm.validate(async (valid) => {
        if (!valid) {
          return;
        }
        if (!this.checkedKeys.length) {
          this.$Message.error(this.$t('qing-xuan-ze-quan-xian'));
          return;
        }

        const authLabelList = this.normalizeCheckedAuthKeys(this.checkedKeys);
        const data = {
          roleName: this.roleForm.roleName,
          authLabelList
        };

        this.submitLoading = true;
        let res;
        if (this.isCreate) {
          res = await this.$services.rdpRoleCreateRole({
            data,
            msg: this.$t('tian-jia-jiao-se-cheng-gong')
          });
        } else if (this.isEdit) {
          res = await this.$services.rdpRoleUpdateRole({
            data: {
              ...data,
              roleId: this.roleForm.roleId
            },
            msg: this.$t('xiu-gai-jiao-se-cheng-gong')
          });
        }
        this.submitLoading = false;

        if (res && res.success) {
          this.goBack();
        }
      });
    },
    goBack() {
      this.$router.push('/manager/role');
    }
  }
};
</script>

<style lang="less" scoped>
.role-editor-page {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.role-editor-layout {
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  min-height: 0;
}

.role-editor-section {
  background: #ffffff;
  border: 1px solid #e5e8ef;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(31, 35, 41, 0.08);
}

.role-editor-card {
  flex: 1 1 auto;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.role-editor-section-title {
  color: #17233d;
  font-size: 18px;
  font-weight: 600;
  line-height: 24px;
}

.role-basic-section {
  flex: 0 0 auto;
  padding-bottom: 20px;
  border-bottom: 1px solid #edf0f5;

  .role-basic-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 16px;
  }

  .role-editor-actions {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 12px;
    flex: 0 0 auto;
  }

  .role-basic-form {
    margin-top: 28px;

    :deep(.ivu-form-item) {
      margin-bottom: 24px;
    }

    :deep(.ivu-form-item-label) {
      color: #253044;
      font-size: 14px;
      font-weight: 600;
    }

    :deep(.ivu-form-item-content) {
      width: min(520px, calc(100% - 104px));
    }

    :deep(.ivu-input-wrapper),
    :deep(.ivu-input) {
      width: 100%;
    }
  }
}

.role-auth-section {
  flex: 1 1 auto;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding-top: 20px;
}

.role-auth-toolbar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 14px 22px;
  margin-top: 22px;
  margin-bottom: 18px;
  min-height: 34px;
}

.role-auth-search {
  flex: 0 0 280px;

  :deep(.ivu-input) {
    height: 36px;
    border-radius: 6px;
  }
}

.role-auth-tool-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  height: 32px;
  padding: 0 4px;
  color: #253044;
  font-weight: 500;

  :deep(.ivu-icon) {
    color: #253044;
    font-size: 16px;
  }

  &:hover {
    color: #18b566;

    :deep(.ivu-icon) {
      color: #18b566;
    }
  }
}

.role-auth-switch {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  color: #253044;
  font-weight: 500;
  white-space: nowrap;
}

.role-auth-tree-container {
  flex: 1 1 auto;
  min-height: 0;
  overflow: auto;
  padding: 18px 22px;
  border: 1px solid #e5e8ef;
  border-radius: 8px;
  background: #ffffff;
}

.role-auth-empty {
  height: 220px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #9ea7b4;
}

:deep(.ant-tree) {
  color: #253044;
  font-size: 15px;
  line-height: 28px;
}

:deep(.ant-tree li) {
  padding: 2px 0;
}

:deep(.ant-tree li .ant-tree-node-content-wrapper) {
  min-height: 28px;
  padding: 0 4px;
  border-radius: 4px;
}

:deep(.ant-tree-checkbox) {
  margin: 5px 8px 0 0;
}

:deep(.ant-tree-checkbox-inner) {
  width: 16px;
  height: 16px;
  border-radius: 4px;
}

:deep(.ant-tree-switcher) {
  width: 22px;
  height: 28px;
  line-height: 28px;
}

:deep(.ant-tree-switcher-noop .ant-tree-switcher-line-icon) {
  display: none;
}
</style>

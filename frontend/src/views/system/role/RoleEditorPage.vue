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
            <div class="role-auth-switch">
              <span>{{ $t('zhi-kan-yi-xuan') }}</span>
              <i-switch v-model="onlyShowSelected" true-color="#18b566" @on-change="handleAuthFilterChange" />
            </div>
          </div>
          <div class="role-auth-tree-container">
            <div v-if="displayTreeData.length" class="role-auth-tree-shell">
              <aside ref="authIndexScroll" class="role-auth-index">
                <div class="role-auth-index-list">
                  <button
                    v-for="item in authIndexItems"
                    :key="item.key"
                    type="button"
                    class="role-auth-index-item"
                    :class="{ active: activeAuthIndexKey === item.key }"
                    :data-auth-key="item.key"
                    @click="handleAuthIndexClick(item.key)"
                  >
                    {{ item.title }}
                  </button>
                </div>
              </aside>
              <div ref="authTreeScroll" class="role-auth-tree-scroll" @scroll.passive="handleAuthTreeScroll">
                <a-tree
                  class="role-auth-tree"
                  :checked-keys="checkedKeys"
                  :tree-data="displayTreeData"
                  checkable
                  :selectable="false"
                  :disabled="!canEditAuth"
                  :replace-fields="replaceFields"
                  v-model:expandedKeys="expandedKeys"
                  @check="handleAuthCheckedChange"
                >
                  <template #title="{ i18nName, title, key }">
                    <span class="role-auth-tree-node-title" :data-auth-key="key">{{ i18nName || title }}</span>
                  </template>
                </a-tree>
              </div>
            </div>
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
      activeAuthIndexKey: '',
      authTreeScrollRaf: null,
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
    },
    authIndexItems() {
      const rootChildren = this.displayTreeData[0]?.children || [];
      return rootChildren.map((node) => ({
        key: node.key,
        title: node.i18nName || node.title || node.key
      }));
    }
  },
  mounted() {
    this.init();
  },
  beforeUnmount() {
    if (this.authTreeScrollRaf) {
      window.cancelAnimationFrame(this.authTreeScrollRaf);
      this.authTreeScrollRaf = null;
    }
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
      this.activeAuthIndexKey = treeData[0].children[0]?.key || '';
      this.$nextTick(() => this.syncAuthIndexWithScroll());
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
    getAuthAncestorKeys(targetKey, data = this.treeData) {
      const findNode = (nodes = [], parents = []) => {
        for (const node of nodes) {
          if (node.key === targetKey) {
            return parents;
          }
          const childResult = findNode(node.children || [], [...parents, node.key]);
          if (childResult) {
            return childResult;
          }
        }
        return null;
      };
      return findNode(data) || [];
    },
    getAuthTreeTitleNode(key) {
      const scrollEl = this.$refs.authTreeScroll;
      return Array.from(scrollEl?.querySelectorAll?.('.role-auth-tree-node-title') || []).find((node) => node.dataset.authKey === key);
    },
    getAuthIndexNode(key) {
      const indexEl = this.$refs.authIndexScroll;
      return Array.from(indexEl?.querySelectorAll?.('.role-auth-index-item') || []).find((node) => node.dataset.authKey === key);
    },
    scrollAuthIndexIntoView(key) {
      const indexEl = this.$refs.authIndexScroll;
      const target = this.getAuthIndexNode(key);
      if (!indexEl || !target) {
        return;
      }
      const indexRect = indexEl.getBoundingClientRect();
      const targetRect = target.getBoundingClientRect();
      if (targetRect.top >= indexRect.top && targetRect.bottom <= indexRect.bottom) {
        return;
      }
      indexEl.scrollTo({
        top: indexEl.scrollTop + targetRect.top - indexRect.top - indexRect.height / 2 + targetRect.height / 2,
        behavior: 'smooth'
      });
    },
    setActiveAuthIndexKey(key) {
      if (!key) {
        return;
      }
      if (key !== this.activeAuthIndexKey) {
        this.activeAuthIndexKey = key;
      }
      this.scrollAuthIndexIntoView(key);
    },
    syncAuthIndexWithScroll() {
      const scrollEl = this.$refs.authTreeScroll;
      if (!scrollEl || !this.authIndexItems.length) {
        this.activeAuthIndexKey = '';
        return;
      }
      const scrollRect = scrollEl.getBoundingClientRect();
      const anchorTop = scrollRect.top + 16;
      let activeKey = this.authIndexItems[0].key;

      this.authIndexItems.forEach((item) => {
        const titleNode = this.getAuthTreeTitleNode(item.key);
        if (!titleNode) {
          return;
        }
        if (titleNode.getBoundingClientRect().top <= anchorTop) {
          activeKey = item.key;
        }
      });

      this.setActiveAuthIndexKey(activeKey);
    },
    handleAuthTreeScroll() {
      if (this.authTreeScrollRaf) {
        return;
      }
      this.authTreeScrollRaf = window.requestAnimationFrame(() => {
        this.authTreeScrollRaf = null;
        this.syncAuthIndexWithScroll();
      });
    },
    handleAuthIndexClick(key) {
      if (!key) {
        return;
      }
      this.setActiveAuthIndexKey(key);
      this.expandedKeys = Array.from(new Set([...this.expandedKeys, ...this.getAuthAncestorKeys(key), key]));
      this.$nextTick(() => {
        const scrollEl = this.$refs.authTreeScroll;
        const target = this.getAuthTreeTitleNode(key);
        if (!scrollEl || !target) {
          return;
        }
        const top = target.getBoundingClientRect().top - scrollEl.getBoundingClientRect().top + scrollEl.scrollTop - 12;
        scrollEl.scrollTo({
          top: Math.max(top, 0),
          behavior: 'smooth'
        });
        this.handleAuthTreeScroll();
      });
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
          this.$nextTick(() => this.syncAuthIndexWithScroll());
          return;
        }

        this.authFilterActive = false;
        const defaultExpandedKeys = this.getExpandableKeys(this.treeData);
        this.expandedKeys = this.authFilterExpandedKeys.length ? [...this.authFilterExpandedKeys] : defaultExpandedKeys;
        this.authFilterExpandedKeys = [];
        this.$nextTick(() => this.syncAuthIndexWithScroll());
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
  // no extra card — page is already inside app-main-card
}

.role-editor-card {
  flex: 1 1 auto;
  min-height: 0;
  display: flex;
  flex-direction: column;
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
  overflow: hidden;
  padding: 0;
  border: 1px solid #e5e8ef;
  border-radius: 8px;
  background: #ffffff;
}

.role-auth-tree-shell {
  display: grid;
  height: 100%;
  min-height: 0;
  grid-template-columns: 184px minmax(0, 1fr);
}

.role-auth-index {
  min-height: 0;
  overflow: auto;
  padding: 16px 12px;
  border-right: 1px solid #edf0f5;
  background: #ffffff;
}

.role-auth-index-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.role-auth-index-item {
  position: relative;
  display: block;
  width: 100%;
  min-width: 0;
  border: 0;
  border-radius: 6px;
  padding: 7px 8px 7px 14px;
  background: transparent;
  color: #4b5565;
  cursor: pointer;
  font-size: 13px;
  line-height: 18px;
  overflow: hidden;
  text-align: left;
  text-overflow: ellipsis;
  transition:
    background-color 0.16s ease,
    color 0.16s ease;
  white-space: nowrap;

  &:hover,
  &.active {
    background: #effbf5;
    color: #17233d;
  }

  &.active::before {
    position: absolute;
    top: 8px;
    bottom: 8px;
    left: 5px;
    width: 3px;
    border-radius: 999px;
    background: #21bf73;
    content: '';
  }
}

.role-auth-tree-scroll {
  min-width: 0;
  min-height: 0;
  overflow: auto;
  padding: 18px 22px 18px 34px;
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

:deep(.role-auth-tree .ant-tree-child-tree) {
  position: relative;
  margin-left: 11px;
  padding-left: 16px;
}

:deep(.role-auth-tree .ant-tree-child-tree::before) {
  position: absolute;
  top: 0;
  bottom: 14px;
  left: 0;
  width: 1px;
  background: #dfe6ef;
  content: '';
}

:deep(.role-auth-tree .ant-tree-child-tree > li) {
  position: relative;
}

:deep(.role-auth-tree .ant-tree-child-tree > li::before) {
  position: absolute;
  top: 16px;
  left: -16px;
  width: 13px;
  height: 1px;
  background: #dfe6ef;
  content: '';
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

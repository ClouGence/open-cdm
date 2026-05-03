<template>
  <div class="schema-tree-select">
    <div class="search">
      <a-input-search :placeholder="$t('sou-suo-shu-xing-jie-gou-zhong-zhan-kai-de-nei-rong')" size="small" @change="search" />
    </div>
    <div class="tree">
      <a-tree
        :tree-data="tree"
        @select="handleSelectNode"
        :expanded-keys.sync="expandedKeys"
        :loaded-keys="loadedKeys"
        :load-data="handleGetSchema"
        show-icon
      >
        <template #dataSourceType="record">
          <cc-data-source-icon :instanceType="record.deployEnvType" :size="16" :type="record.dataSourceType" color="#4DBAEE" />
        </template>
        <template #schemaType>
          <cc-iconfont name="shujuku" color="#999999" :size="12" />
        </template>
        <template #libraryType>
          <cc-iconfont name="schema" color="#999999" :size="12" />
        </template>
        <template #schemaMore>
          <cc-iconfont name="more" color="#999999" :size="12" />
        </template>
        <template #title="record">
          <div class="tree-title">
            <span :title="record.title" class="title" v-if="type === 'schema'">
              {{ record.title }}
            </span>
            <span
              :title="record.deep === 2 && record.disabled ? $t('wu-quan-xian') : record.title"
              class="title"
              v-else
              :style="`width: ${record.deep === 1 ? 295 : record.deep === 2 ? 175 : record.deep === 3 ? 170 : 136}px`"
            >
              {{ record.title }}
            </span>
            <a-button
              type="link"
              v-if="type === 'auth' && record.deep > 1"
              @click.stop="handleClickAuth(record)"
              :style="`color: ${selectedKeys.length && record.key === selectedKeys[0] ? '#fff' : '#0BB9F8'};margin-top:-3px;`"
            >
              <span v-if="record.deep === 2">{{ $t('shi-li-shou-quan') }}</span>
              <span v-else-if="record.deep === 3">{{ $t('ku-shou-quan') }}</span>
              <span v-else-if="record.deep === 4">{{ $t('schema-shou-quan') }}</span>
            </a-button>
          </div>
        </template>
      </a-tree>
    </div>
    <auth-modal
      :visible="showAuthModal"
      :ds="authData"
      :handle-close-modal="hideAuthModal"
      :ds-auth-kind="dsAuthKind"
      v-if="showAuthModal"
      :get-user-ds-auth="getUserDsAuth"
      :refresh-current-table="refreshCurrentTable"
      :is-ticket="isTicket"
    />
  </div>
</template>

<script>
import cloneDeep from 'lodash.clonedeep';
import { APPROVAL_BIZ_TYPE } from '@/const';
import AuthModal from '../modal/AuthModal';
// import * as Vue from 'vue';
// import { PG_GP } from '../../const';

const TAG = 'schema_tree_select';

export default {
  name: 'CCSchemaTreeSelect',
  components: { AuthModal },
  props: {
    getUserDsAuth: Function,
    handleDataProcessing: Function,
    auth: Boolean,
    type: {
      default: 'schema'
    },
    dsAuthKind: Array,
    isTicket: Boolean
  },
  data() {
    return {
      selectedEvent: null,
      authData: {},
      showAuthModal: false,
      pageSize: 20,
      preRequestData: {},
      searchStr: '',
      searchValue: '',
      tree: [],
      rawTree: [],
      treeMap: {},
      expandedKeys: [],
      loadedKeys: [],
      autoExpandParent: true,
      moreDataObj: {},
      searchMoreDataObj: {},
      selectedKeys: []
    };
  },
  methods: {
    hideAuthModal() {
      this.showAuthModal = false;
    },
    handleClickAuth(record) {
      const authData = cloneDeep(record);
      authData.authTitle = record.deep === 2 ? this.$t('shi-li-0') : record.deep === 3 ? this.$t('ku') : 'SCHEMA';
      this.authData = authData;
      this.showAuthModal = true;
    },
    // 对子节点进行搜索。
    searchEach(node, value) {
      const depth = this.getTreeDepth(node);
      const self = this;
      for (let i = 0; i < depth - 1; i++) {
        // 记录【删除不匹配搜索内容的叶子节点】操作的次数。
        // 如果这个变量记录的操作次数为0，表示树形结构中，所有的
        // 叶子节点(不包含只有根节点的情况)都匹配搜索内容。那么就没有必要再
        // 在循环体里面遍历树了.
        let spliceCounter = 0;

        // 遍历树形结构
        this.traverseTree(node, (n) => {
          if (self.isHasChildren(n)) {
            const children = n.children;
            const length = children.length;

            // 找到不匹配搜索内容的叶子节点并删除。为了避免要删除的元素在数组中的索引改变，从后向前循环,
            // 找到匹配的元素就删除。
            for (let j = length - 1; j >= 0; j--) {
              const e3 = children[j];
              if (!self.isHasChildren(e3) && e3.title.indexOf(value) <= -1) {
                children.splice(j, 1);
                spliceCounter++;
              }
            } // end for (let j = length - 1; j >= 0; j--)
          }
        }); // end this.traverseTree(node, n=>{

        // 所有的叶子节点都匹配搜索内容，没必要再执行循环体了。
        if (spliceCounter === 0) {
          break;
        }
      }
    },
    // 搜索框回车事件响应
    search(e) {
      const self = this;
      // 把树形结构还原成搜索以前的。
      const tree = cloneDeep(this.rawTree);
      console.log(e.target.value);
      if (tree && tree.length > 0) {
        tree.forEach((n) => {
          self.searchEach(n, e.target.value);
        });

        // 没有叶子节点的根节点也要清理掉
        const length = tree.length;
        for (let i = length - 1; i >= 0; i--) {
          const e2 = tree[i];
          if (!this.isHasChildren(e2) && e2.title.indexOf(e.target.value) <= -1) {
            tree.splice(i, 1);
          }
        }

        if (tree.length) {
          tree.forEach((root) => {
            if (root.children && root.children.length) {
              root.children.forEach((leaf) => {
                if (leaf.children && leaf.children.length) {
                  if (leaf.children.length > this.pageSize) {
                    if (!leaf.rawChildren) {
                      leaf.rawChildren = cloneDeep(leaf.children);
                      leaf.end = 0;
                    }
                    leaf.end++;
                    leaf.children = leaf.rawChildren.slice(0, leaf.end * this.pageSize);
                    if (leaf.children.length < leaf.rawChildren.length) {
                      leaf.children.push({
                        more: true,
                        deep: 3,
                        key: `ecneguolc_${leaf.key}`,
                        parentKey: leaf.key,
                        title: this.$t('jia-zai-geng-duo'),
                        scopedSlots: {
                          icon: 'schemaMore',
                          title: 'title'
                        },
                        isLeaf: true,
                        isRoot: false,
                        hasNextLevel: false
                      });
                    }
                  }

                  leaf.children.forEach((leaf2) => {
                    if (!leaf2.more && leaf2.children && leaf2.children.length && leaf2.children.length > this.pageSize) {
                      if (!leaf2.rawChildren) {
                        leaf2.rawChildren = cloneDeep(leaf2.children);
                        leaf2.end = 0;
                      }
                      leaf2.end++;
                      leaf2.children = leaf2.rawChildren.slice(0, leaf2.end * this.pageSize);
                      if (leaf2.children.length < leaf2.rawChildren.length) {
                        leaf2.children.push({
                          more: true,
                          deep: 4,
                          key: `ecneguolc_${leaf2.key}`,
                          parentKey: leaf2.key,
                          title: this.$t('jia-zai-geng-duo'),
                          scopedSlots: {
                            icon: 'schemaMore',
                            title: 'title'
                          },
                          isLeaf: true,
                          isRoot: false,
                          hasNextLevel: false
                        });
                      }
                    }
                  });
                }
              });
            }
          });
        }
        this.sliceSearchData(tree);
      }
    },
    // 判断树形结构中的一个节点是否具有孩子节点
    isHasChildren(node) {
      let flag = false;
      if (node.children && node.children.length > 0) {
        flag = true;
      }
      return flag;
    },
    // 通过传入根节点获得树的深度，是 calDepth 的调用者。
    getTreeDepth(node) {
      if (undefined === node || node === null) {
        return 0;
      }
      // 返回结果
      let r = 0;
      // 树中当前层节点的集合。
      let currentLevelNodes = [node];
      // 判断当前层是否有节点
      while (currentLevelNodes.length > 0) {
        // 当前层有节点，深度可以加一。
        r++;
        // 下一层节点的集合。
        let nextLevelNodes = [];
        // 找到树中所有的下一层节点，并把这些节点放到 nextLevelNodes 中。
        for (let i = 0; i < currentLevelNodes.length; i++) {
          const e = currentLevelNodes[i];
          if (this.isHasChildren(e)) {
            nextLevelNodes = nextLevelNodes.concat(e.children);
          }
        }
        // 令当前层节点集合的引用指向下一层节点的集合。
        currentLevelNodes = nextLevelNodes;
      }
      return r;
    },
    traverseTree(node, callback) {
      if (!node) {
        return;
      }
      const stack = [];
      stack.push(node);
      let tmpNode;
      while (stack.length > 0) {
        tmpNode = stack.pop();
        callback(tmpNode);
        if (tmpNode.children && tmpNode.children.length > 0) {
          for (let i = tmpNode.children.length - 1; i >= 0; i--) {
            stack.push(tmpNode.children[i]);
          }
        }
      }
    },
    handleGetSchema(treeNode) {
      this.loadedKeys.push(treeNode.dataRef.key);

      return new Promise((resolve) => {
        if (!treeNode.dataRef.isLeaf && !treeNode.dataRef.isRoot) {
          if (treeNode.dataRef.deep === 3) {
            this.getSchemaList(treeNode.dataRef, treeNode);
          } else {
            this.getLeafData(treeNode.dataRef, treeNode);
          }
          resolve();
        } else {
          resolve();
        }
      });
    },
    async getDsListForManage(data = {}) {
      const res = await this.$services.rdpDataSourceListByCondition({
        data: { ...data, useVisibility: true, loadApproInfo: this.isTicket }
      });
      const tree = {};
      const treeMap = {};
      if (res.success) {
        if (this.type !== 'auth') {
          res.data = res.data.filter((ds) => ds.dataSourceType !== 'Redis');
        }

        if (this.isTicket) {
          res.data = res.data.filter((ds) => ds.supportApproBiz && ds.supportApproBiz.includes(APPROVAL_BIZ_TYPE.AUTH));
        }

        res.data.forEach((ds) => {
          const { dsEnvName, dsEnvId, instanceDesc, instanceId, dataSourceType, supportApproBiz = [] } = ds;
          const key = `${dsEnvName}/${instanceId}`;
          const leaf1 = {
            title: dsEnvName,
            isRoot: true,
            deep: 1,
            scopedSlots: { title: 'title' },
            key: dsEnvName,
            checkable: false,
            isLeaf: false
          };

          const noAuth = !(supportApproBiz && supportApproBiz.includes('AUTH')) && this.isTicket;
          const leaf2 = {
            ...ds,
            isRoot: false,
            deep: 2,
            title: instanceDesc,
            key,
            disabled: noAuth,
            isLeaf: ['Redis'].includes(dataSourceType) || noAuth,
            scopedSlots: {
              icon: 'dataSourceType',
              title: 'title'
            }
            // disabled: !ds.supportDataHandle
          };
          treeMap[key] = leaf2;
          if (tree[dsEnvId]) {
            tree[dsEnvId].children.push(leaf2);
          } else {
            treeMap[leaf1.key] = leaf1;
            this.expandedKeys.push(leaf1.key);
            tree[dsEnvId] = {
              ...leaf1,
              children: [leaf2]
            };
          }
        });
      }
      this.tree = Object.values(tree);
      this.rawTree = cloneDeep(Object.values(tree));
      this.treeMap = cloneDeep(treeMap);
    },
    refreshCurrentTable() {
      if (this.selectedKeys.length && this.selectedEvent) {
        this.handleSelectNode(this.selectedKeys, this.selectedEvent);
      }
    },
    handleSelectNode(selectedKeys, e) {
      const treeNode = e.node.dataRef;
      let selectedEvent = null;
      this.selectedKeys = selectedKeys;
      if (treeNode.dataSourceType === 'Redis') {
        return;
      }

      if (treeNode && treeNode.more) {
        this.sliceData(treeNode.parentKey);
      } else if (treeNode && treeNode.isLeaf && !treeNode.more) {
        selectedEvent = e;
        this.getTables(treeNode);
      } else {
        const index = this.expandedKeys.indexOf(treeNode.key);

        if (index > -1) {
          this.expandedKeys.splice(index, 1);
        } else {
          this.expandedKeys.push(treeNode.key);
        }
        if (treeNode.deep > 1) {
          this.handleDataProcessing([], treeNode);
        }
      }
      this.selectedEvent = selectedEvent;
    },
    async getTables(schema) {
      const { id: dataSourceId, title: schemaName, parentData = '' } = schema;
      const res = await this.$services.dmDataSourceSchemaListTables({
        data: {
          dataSourceId,
          schemaName,
          parentData,
          useVisibility: true
        }
      });

      if (res.success) {
        this.handleDataProcessing(res.data, schema);
      }
    },
    async getLeafData(datasource, treeNode) {
      const cloneDs = cloneDeep(datasource);
      delete cloneDs.scopedSlots;
      treeNode.dataRef.loading = true;
      // this.tree = [...this.tree];

      const res = await this.$services.dmDataSourceSchemaListFirstLevel({
        data: {
          dataSourceId: datasource.id,
          useVisibility: true
        },
        page: 'schema_tree_select'
      });

      treeNode.dataRef.loading = false;
      delete cloneDs.loading;
      if (res.success && res.data.nameList && res.data.nameList.length) {
        treeNode.dataRef.startId = 0;
        const children = [];
        const { dsEnvName, instanceId } = datasource;
        const { hasNextLevel, nameList } = res.data;
        nameList.forEach((schema) => {
          const key = `${dsEnvName}/${instanceId}/${schema}`;
          const leaf = {
            ...cloneDs,
            deep: 3,
            scopedSlots: {
              icon: 'schemaType',
              title: 'title'
            },
            isRoot: false,
            title: schema,
            isLeaf: !hasNextLevel,
            key
          };
          this.treeMap[key] = leaf;
          children.push(leaf);
        });
        treeNode.dataRef.children = cloneDeep(children);
        this.tree = cloneDeep(this.tree);
        for (let i = 0; i < this.rawTree.length; i++) {
          const root = this.rawTree[i];
          for (let j = 0; j < root.children.length; j++) {
            const leaf = root.children[j];
            if (leaf.key === treeNode.dataRef.key) {
              leaf.children = cloneDeep(children);
            }
          }
        }
        this.rawTree = [...this.rawTree];
        // Vue.set(this.moreDataObj, treeNode.dataRef.key, children);
        // Vue.set(this.searchMoreDataObj, treeNode.dataRef.key, cloneDeep(children));
        this.sliceData(treeNode.dataRef.key);
      } else {
        this.preRequestData = {
          datasource,
          treeNode
        };
        this.expandedKeys = this.expandedKeys.filter((key) => key !== treeNode.dataRef.key);
        this.loadedKeys = this.loadedKeys.filter((key) => key !== treeNode.dataRef.key);
      }
    },
    async getSchemaList(datasource, treeNode) {
      const { title: parentData } = treeNode.dataRef;
      const cloneDs = cloneDeep(datasource);
      delete cloneDs.scopedSlots;
      const res = await this.$services.dmDataSourceSchemaListSchemas({
        data: {
          dataSourceId: datasource.id,
          parentData,
          useVisibility: true
        }
      });

      if (res.success && res.data.length) {
        treeNode.dataRef.startId = 0;
        const children = [];
        const { dsEnvName, instanceId } = datasource;
        res.data.forEach((schema) => {
          const key = `${dsEnvName}/${instanceId}/${parentData}/${schema.name}`;
          const leaf = {
            ...cloneDs,
            deep: 4,
            scopedSlots: {
              icon: 'libraryType',
              title: 'title'
            },
            parentData,
            isisRoot: false,
            title: schema.name,
            isLeaf: true,
            key
          };
          this.treeMap[key] = leaf;
          children.push(leaf);
          // treeNode.dataRef.children.push(leaf);
        });
        treeNode.dataRef.children = cloneDeep(children);
        this.tree = cloneDeep(this.tree);
        for (let i = 0; i < this.rawTree.length; i++) {
          const root = this.rawTree[i];
          for (let j = 0; j < root.children.length; j++) {
            const leaf = root.children[j];
            if (leaf.children) {
              for (let k = 0; k < leaf.children.length; k++) {
                const leaf2 = leaf.children[k];
                if (leaf2.key === treeNode.dataRef.key) {
                  leaf2.children = cloneDeep(children);
                }
              }
            }
          }
        }
        this.rawTree = [...this.rawTree];
        // Vue.set(this.moreDataObj, treeNode.dataRef.key, children);
        // Vue.set(this.searchMoreDataObj, treeNode.dataRef.key, cloneDeep(children));
        this.sliceData(treeNode.dataRef.key);
      } else {
        this.preRequestData = {
          datasource,
          treeNode
        };
      }
    },
    sliceData(key) {
      for (let i = 0; i < this.tree.length; i++) {
        const root = this.tree[i];
        if (root.children && root.children.length) {
          for (let j = 0; j < root.children.length; j++) {
            const leaf = root.children[j];
            if (leaf.children && leaf.children.length) {
              if (leaf.key === key) {
                if (leaf.children.length > this.pageSize) {
                  if (!leaf.rawChildren) {
                    leaf.rawChildren = cloneDeep(leaf.children);
                    leaf.end = 0;
                  }
                  leaf.end++;
                  leaf.children = leaf.rawChildren.slice(0, this.pageSize * leaf.end);
                  if (leaf.children.length < leaf.rawChildren.length) {
                    leaf.children.push({
                      more: true,
                      deep: 3,
                      key: `ecneguolc_${key}`,
                      parentKey: key,
                      title: this.$t('jia-zai-geng-duo'),
                      scopedSlots: {
                        icon: 'schemaMore',
                        title: 'title'
                      },
                      isLeaf: true,
                      isRoot: false,
                      hasNextLevel: false
                    });
                  }
                }
              } else {
                for (let k = 0; k < leaf.children.length; k++) {
                  const leaf2 = leaf.children[k];
                  if (leaf2.key === key && leaf2.children.length > this.pageSize) {
                    if (!leaf2.rawChildren) {
                      leaf2.rawChildren = cloneDeep(leaf2.children);
                      leaf2.end = 0;
                    }
                    leaf2.end++;
                    leaf2.children = leaf2.rawChildren.slice(0, this.pageSize * leaf2.end);
                    if (leaf2.children.length < leaf2.rawChildren.length) {
                      leaf2.children.push({
                        more: true,
                        deep: 4,
                        key: `ecneguolc_${key}`,
                        parentKey: key,
                        title: this.$t('jia-zai-geng-duo'),
                        scopedSlots: {
                          icon: 'schemaMore',
                          title: 'title'
                        },
                        isLeaf: true,
                        isRoot: false,
                        hasNextLevel: false
                      });
                    }
                  }
                }
              }
            }
          }
        }
      }

      this.tree = [...this.tree];
    },
    sliceSearchData(tree) {
      this.tree = [...tree];
    },
    handlePreRequest() {
      const { datasource, treeNode } = this.preRequestData;
      this.getLeafData(datasource, treeNode);
      this.expandedKeys.push(treeNode.dataRef.key);
    }
  },
  created() {
    this.getDsListForManage();
    this.$bus.on(`${TAG}_listfirstlevel_callback`, this.handlePreRequest);
  },
  unmounted() {
    this.$bus.off(`${TAG}_listfirstlevel_callback`);
  }
};
</script>

<style lang="less">
.schema-tree-select {
  height: calc(~'100vh - 188px');
  width: 320px;
  border: 1px solid rgba(199, 199, 199, 1);
  //overflow: scroll;
  position: relative;

  .ant-tree {
    color: #333333;

    li .ant-tree-node-content-wrapper {
      padding: 0;
    }

    .ant-tree-switcher {
      width: 22px;
      height: 22px;
    }

    .ant-select-switcher-icon,
    .anticon {
      font-size: 14px;
      color: #999999;
    }

    .ant-tree-node-content-wrapper {
      color: #333333;
    }

    .ant-tree-node-selected {
      color: #ffffff;

      .cc-iconfont {
        color: #ffffff;
      }

      background-color: #0bb9f8 !important;
    }
  }

  .search {
    position: absolute;
    height: 44px;
    width: 100%;
    line-height: 44px;
    padding: 0 10px;
    border-bottom: 1px solid #eaeaea;
    background: #fafafa;
  }

  .tree {
    height: calc(~'100vh - 232px');
    margin-top: 44px;
    overflow: scroll;
  }

  .tree-title {
    display: inline-block;

    .title {
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      float: left;
    }

    button {
      opacity: 0;
    }

    &:hover {
      button {
        opacity: 1;
      }
    }
  }
}
</style>

<script lang="jsx">
import ContextMenu from '@imengyu/vue3-context-menu';
import VTree from '@wsfe/vue-tree';
import { mapGetters } from 'vuex';
import copyMixin from '@/mixins/copyMixin';
import datasourceMixin from '@/mixins/datasourceMixin';
import { ACTION_TYPE, TAB_TYPE } from '@/const';
import browseMixin from '@/mixins/browseMixin';
import { NODE_TYPE, DS_RIGHT_CLICK_MENU_ITEM } from '@/utils';
import utilMixin from '@/mixins/utilMixin';
import { clearAllPending } from '@/services/http/cancelRequest';
import AddDataSource from '@/views/dataSource/AddDataSource';

export default {
  name: 'DataSourceTree',
  mixins: [copyMixin, datasourceMixin, browseMixin, utilMixin],
  components: {
    AddDataSource,
    VTree
  },
  props: {
    currentTab: Object,
    listLevels: Function,
    listLeaf: Function,
    detailLevels: Function,
    treeData: Array,
    setLoading: Function,
    getDataSourceList: Function,
    getNodeData: Function,
    getNodeByKey: Function,
    handleAddTab: Function,
    refreshTabSelectOptions: Function
  },
  watch: {
    treeData: {
      handler(newData) {
        // 延迟检查v-tree的实际数据状态
        this.$nextTick(() => {
          this.checkTreeDataAndToggle();
        });
      },
      immediate: true
    }
  },
  data() {
    const storedHide = this.getStoredHideState();
    return {
      testDsMsg: '',
      showAddDsModal: false,
      isInitialized: false, // 标记是否已经初始化完成
      advancedSetting: [
        {
          value: 'delimited',
          label: this.$t('shi-yong-xian-ding-fu-bao-guo-shu-ju-ku-dui-xiang-ming')
        },
        {
          value: 'usingExists',
          label: this.$t('shi-yong-if-exists-zi-ju')
        },
        {
          value: 'cascade',
          label: this.$t('shi-yong-cascade-zi-ju-jin-hang-qiang-zhi-shan-chu')
        },
        {
          value: 'restrict',
          label: this.$t('shi-yong-restrict-zi-ju-zai-xian-zhi-tiao-jian-xia-shan-chu')
        },
        {
          value: 'purge',
          label: this.$t('shi-yong-purge-zi-ju-jin-hang-zi-yuan-hui-shou')
        },
        {
          value: 'truncateUseDelete',
          label: this.$t('shi-yong-delete-yu-ju-ti-dai-truncate-yu-ju')
        }
      ],
      scrollY: 0,
      top: 0,
      saveScroll: true,
      menuModal: {
        options: {
          delimited: false,
          usingExists: false,
          cascade: false,
          restrict: false,
          purge: false,
          truncateUseDelete: false
        },
        collapseKey: '',
        actionData: {},
        show: false,
        title: '',
        content: '',
        name: '',
        preName: '',
        showNameInput: false,
        sql: '',
        permission: false
      },
      doActionLoading: false,
      showEditDsDescModal: false,
      showDeleteInstanceModal: false,
      dsDesc: '',
      actionType: '',
      genActionData: null,
      TAB_TYPE,
      expandedKeys: [],
      selectedNode: null,
      hide: storedHide,
      dataSourceWidth: 0,
      preDataSourceWidth: 250,
      searchKey: '',
      showTicketModal: false,
      rawSqlToSubmit: '',
      ticketData: {
        ticketTitle: '',
        description: ''
      },
      ticketRuleValidate: {
        ticketTitle: [
          {
            required: true,
            message: this.$t('biao-ti-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ],
        description: [
          {
            required: true,
            message: this.$t('xu-qiu-miao-shu-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ]
      }
    };
  },
  computed: {
    ...mapGetters(['isDesktop', 'getMenus', 'getBrowserMenus', 'isDark'])
  },
  mounted() {
    const dataSourceTreeList = $('.datasource-tree .ctree-tree__scroll-area');
    if (dataSourceTreeList && dataSourceTreeList.length) {
      dataSourceTreeList[0].addEventListener('scroll', this.handleSetScrollTop, true);
    }
  },
  beforeUnmount() {
    const dataSourceTreeList = $('.datasource-tree .ctree-tree__scroll-area');
    if (dataSourceTreeList && dataSourceTreeList.length) {
      dataSourceTreeList[0].removeEventListener('scroll', this.handleSetScrollTop, true);
    }
  },
  methods: {
    // 获取存储的隐藏状态
    getStoredHideState() {
      try {
        const stored = localStorage.getItem('clouddm_datasource_hide');
        return stored === 'true';
      } catch (e) {
        return false;
      }
    },
    // 保存隐藏状态
    saveHideState(hide) {
      try {
        localStorage.setItem('clouddm_datasource_hide', hide.toString());
      } catch (e) {
        console.warn('Failed to save hide state:', e);
      }
    },
    checkTreeDataAndToggle() {
      // 检查v-tree组件内部是否有数据
      if (this.$refs.tree) {
        const treeData = this.$refs.tree.getTreeData();
        const hasData = treeData && treeData.length > 0;

        console.log('v-tree data check:', hasData, treeData);

        // 获取用户保存的状态
        const storedHide = this.getStoredHideState();

        if (hasData) {
          // 如果有数据，根据用户保存的状态决定是否展开
          // 只有在用户没有手动收起时才自动展开
          if (!storedHide) {
            // 只有当当前状态与目标状态不一致时才更新，避免不必要的更新导致闪动
            if (this.hide !== false || this.dataSourceWidth !== 250) {
              this.hide = false;
              this.dataSourceWidth = 250;
            }
          } else {
            // 用户之前手动收起了，保持收起状态
            if (this.hide !== true || this.dataSourceWidth !== 0) {
              this.hide = true;
              this.dataSourceWidth = 0;
            }
          }
        } else {
          // 没有数据时，只有在初始化完成后才自动收起
          // 但如果用户之前手动展开了，保持展开状态
          if (this.isInitialized && !storedHide) {
            // 只有在状态确实需要改变时才更新
            if (this.hide !== true || this.dataSourceWidth !== 0) {
              this.hide = true;
              this.dataSourceWidth = 0;
            }
          }
        }

        // 标记为已初始化
        this.isInitialized = true;
      }
    },
    async submitTicket() {
      this.$refs.ticketContent.validate(async (valid) => {
        if (valid) {
          const { node } = this.currentTab;
          const dbLevels = this.browseGenLevelsData(node);
          if (this.actionType === DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEMA_DROP) {
            dbLevels.splice(dbLevels.length - 1, 1, '');
          }
          const data = {
            dbLevels,
            rawSql: this.rawSqlToSubmit,
            description: this.ticketData.description,
            ticketTitle: this.ticketData.ticketTitle,
            force: true
          };
          const res = await this.$services.dmTicketCreate({ data });
          if (res.success) {
            const path = `/ticket/${res.data?.ticketId}`;
            this.$Message.success({
              duration: 2.5,
              render: (h) => {
                return h('div', [
                  this.$t('ti-jiao-cheng-gong'),
                  ', ',
                  h(
                    'a',
                    {
                      style: {
                        position: 'relative',
                        top: '1px',
                        color: '#2d8cf0',
                        textDecoration: 'underline',
                        cursor: 'pointer'
                      },
                      on: {
                        click: () => {
                          this.$router.push(path);
                        }
                      }
                    },
                    this.$t('dian-ji-tiao-zhuan-zhi-gong-dan')
                  )
                ]);
              }
            });
            this.handleCloseModal();
            this.showTicketModal = false;
          } else {
            this.$Message.error(res.msgContent);
          }
        } else {
          return false;
        }
      });
    },
    handleSetTestDsMsg(msgContent) {
      this.testDsMsg = msgContent;
    },
    handleSetScrollTop(e) {
      this.scrollY = e.target.scrollTop;
    },
    handleEleScroll(top) {
      const eleList = $('.datasource-tree .ctree-tree__scroll-area');
      if (eleList && eleList.length) {
        eleList[0].scrollTo({ top });
      }
    },
    async handleDeleteInstance() {
      const res = await this.$services.dmBrowseActionsInstanceDelete({
        data: {
          levels: this.browseGenLevelsData(this.selectedNode)
        }
      });

      if (res.success) {
        this.$message.success(this.$t('shan-chu-shu-ju-yuan-cheng-gong'));
        this.$refs.tree.remove(this.selectedNode.key);
        this.handleCloseModal();
      }
    },
    handleRefreshTree() {
      this.scrollY = 0;
      this.expandedKeys = [];
      this.getDataSourceList();
      this.searchKey = '';
    },
    handleMenuNameChange(e) {
      if (e.target.value !== this.menuModal.name) {
        this.menuModal.sql = '';
        this.menuModal.permission = false;
        this.menuModal.danger = false;
        this.menuModal.actionData = {};
      }
      this.menuModal.name = e.target.value;
    },
    handleMenuOptionChange(key, e) {
      if (e.target.checked !== this.menuModal.options[key]) {
        this.menuModal.sql = '';
        this.menuModal.permission = false;
        this.menuModal.danger = false;
        this.menuModal.actionData = {};
      }
      this.menuModal.options[key] = e.target.checked;
    },
    async handleDoAction() {
      const callback = async () => {
        let currentNode = {};
        switch (this.actionType) {
          case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_INSTANCE_RENAME:
            break;
          case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_INSTANCE_DROP:
          case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CATALOG_DROP:
          case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEMA_DROP:
            this.$refs.tree.remove(this.selectedNode.key);
            this.refreshTabSelectOptions(this.selectedNode._parent.key);
            break;
          case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CATALOG_CREATE:
            if (this.selectedNode.nodeType === NODE_TYPE.CATALOG) {
              const parentNode = this.selectedNode._parent;
              currentNode = {
                ...this.selectedNode,
                [NODE_TYPE.CATALOG]: {
                  id: this.menuModal.name,
                  name: this.menuModal.name,
                  attr: {}
                },
                isNew: true,
                isLeaf: false,
                title: this.menuModal.name,
                popTip: `${parentNode.popTip}.${this.menuModal.name}`,
                key: `${parentNode.key}.\`${this.menuModal.name}\``,
                children: [],
                icon: NODE_TYPE.CATALOG,
                nodeType: NODE_TYPE.CATALOG,
                levels: this.selectedNode.levels
              };
              this.$refs.tree.insertAfter(currentNode, this.selectedNode.key);
              this.$refs.tree.setSelected(currentNode.key, true);
              // this.insertNode(parentNode.key, this.selectedNode.key, currentNode);
              // const refreshCache = true;
              // await this.listLevels(
              //   currentNode,
              //   {},
              //   () => {},
              //   () => {},
              //   refreshCache
              // );
            } else {
              if (this.selectedNode.children.length) {
                const parentNode = this.selectedNode;
                currentNode = {
                  ...this.selectedNode,
                  [NODE_TYPE.CATALOG]: {
                    id: this.menuModal.name,
                    name: this.menuModal.name,
                    attr: {}
                  },
                  isNew: true,
                  isLeaf: true,
                  title: this.menuModal.name,
                  popTip: `${parentNode.popTip}.${this.menuModal.name}`,
                  key: `${parentNode.key}.\`${this.menuModal.name}\``,
                  children: [],
                  icon: NODE_TYPE.CATALOG,
                  nodeType: NODE_TYPE.CATALOG,
                  levels: this.selectedNode.children[0].levels
                };
                this.$refs.tree.insertBefore(currentNode, this.selectedNode.children[0].key);
                this.$refs.tree.setSelected(currentNode.key, true);
                // this.insertNode(parentNode.key, null, currentNode);
              } else {
                await this.getNodeData(this.selectedNode, {
                  selected: `${this.selectedNode.key}.\`${this.menuModal.name}\``
                });
              }
            }
            this.menuModal.name = '';
            this.menuModal.preName = '';
            break;
          case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEMA_RENAME:
          case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CATALOG_RENAME:
            const node = this.getNodeByKey(this.treeData, this.selectedNode.key);
            node.title = this.menuModal.name;
            node[node.nodeType].id = this.menuModal.name;
            node[node.nodeType].name = this.menuModal.name;
            node.key = `${node.parentKey}.\`${this.menuModal.name}\``;
            node.popTip = `${node.parentPoptip}.\`${this.menuModal.name}\``;
            if (node.children && node.children.length) {
              node.children.forEach((child) => {
                child.parentKey = node.key;
                child.parentPoptip = node.popTip;
                child.key = `${node.key}.\`${child.title}\``;
                child.popTip = `${node.popTip}.\`${child.title}\``;
              });
            }
            this.handleSetExpandedKeys(node);
            await this.handleSetData(this.treeData);
            this.handleSetSelected(node.key);
            if (this.actionType === DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEMA_RENAME) {
              this.refreshTabSelectOptions(node.parentKey);
            }
            break;
          case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEMA_CREATE:
            if (this.selectedNode.nodeType === NODE_TYPE.SCHEMA) {
              console.log(this.selectedNode);
              const parentNode = this.selectedNode._parent;
              currentNode = {
                ...this.selectedNode,
                [NODE_TYPE.SCHEMA]: {
                  id: this.menuModal.name,
                  name: this.menuModal.name,
                  attr: {}
                },
                isNew: true,
                isLeaf: true,
                title: this.menuModal.name,
                popTip: `${parentNode.popTip}.${this.menuModal.name}`,
                key: `${parentNode.key}.\`${this.menuModal.name}\``,
                children: [],
                icon: NODE_TYPE.SCHEMA,
                nodeType: NODE_TYPE.SCHEMA,
                levels: this.selectedNode.levels
              };
              this.$refs.tree.insertAfter(currentNode, this.selectedNode.key);
              this.$refs.tree.setSelected(currentNode.key, true);
              this.refreshTabSelectOptions(this.selectedNode._parent.key);
            } else {
              if (this.selectedNode.children.length) {
                const parentNode = this.selectedNode;
                currentNode = {
                  ...this.selectedNode,
                  [NODE_TYPE.SCHEMA]: {
                    id: this.menuModal.name,
                    name: this.menuModal.name,
                    attr: {}
                  },
                  isNew: true,
                  isLeaf: true,
                  title: this.menuModal.name,
                  popTip: `${parentNode.popTip}.${this.menuModal.name}`,
                  key: `${parentNode.key}.\`${this.menuModal.name}\``,
                  children: [],
                  icon: NODE_TYPE.SCHEMA,
                  nodeType: NODE_TYPE.SCHEMA,
                  levels: this.selectedNode.children[0].levels
                };
                this.$refs.tree.insertBefore(currentNode, this.selectedNode.children[0].key);
                this.$refs.tree.setSelected(currentNode.key, true);
              } else {
                await this.getNodeData(this.selectedNode, {
                  selected: `${this.selectedNode.key}.\`${this.menuModal.name}\``
                });
              }
            }
            this.menuModal.name = '';
            this.menuModal.preName = '';
            break;
          default:
            break;
        }

        this.handleCloseModal();
        this.doActionLoading = false;
        this.$message.success(this.$t('cao-zuo-cheng-gong'));
      };
      const callbackFail = () => {
        this.doActionLoading = false;
      };
      this.doActionLoading = true;
      await this.browseDoAction(this.menuModal.actionData, callback, callbackFail);
    },
    handleDblClick(node) {
      if (node.isLeaf) {
        this.handleAddTab(TAB_TYPE.QUERY, node);
      }
    },
    handleSetExpandedKeys(node) {
      console.log('expand key', node.key);
      const { key } = node;
      if (this.isExpandedKey(node)) {
        this.expandedKeys = this.expandedKeys.filter((k) => k !== key);
      } else {
        this.expandedKeys.push(key);
      }
    },
    isExpandedKey(node) {
      return this.expandedKeys.includes(node.key);
    },
    handleCloseModal() {
      if (this.$refs.addDatasource) {
        this.testDsMsg = '';
        this.$refs.addDatasource.handleSetEmptyDatasourceForm();
      }
      this.menuModal = {
        options: {
          delimited: false,
          usingExists: false,
          cascade: false,
          restrict: false,
          purge: false,
          truncateUseDelete: false
        },
        actionData: {},
        show: false,
        title: '',
        content: '',
        name: '',
        preName: '',
        sql: '',
        permission: false
      };
      this.dsDesc = '';
      this.doActionLoading = false;
      this.showEditDsDescModal = false;
      this.showDeleteInstanceModal = false;
      this.showAddDsModal = false;
      clearAllPending();
    },
    async handleSetData(data, search = false) {
      this.top = this.scrollY;
      console.log('handleSetData', this.$refs);
      await this.$refs.tree.setData(data);
      setTimeout(() => {
        this.handleEleScroll(this.top);
        if (search) {
          this.handleSearch(false);
        }
        // 设置数据后检查树状态
        this.checkTreeDataAndToggle();
      });
    },
    async handleUpdateNode(key, node) {
      this.$refs.tree.updateNode(key, node);
    },
    async handleAppendList(key, children) {
      if (key && children) {
        children.forEach((child) => {
          this.$refs.tree.append(child, key);
        });
      }
    },
    handleSetSelected(key, selected = true) {
      this.$refs.tree.setSelected(key, selected);
    },
    handleGetNode(key) {
      return this.$refs.tree.getNode(key);
    },
    handleGetTreeData() {
      return this.$refs.tree.getTreeData();
    },
    renderNode(node) {
      const { title, icon, children, nodeType, INSTANCE } = node;
      return (
        <div class='node'>
          {icon && nodeType === 'INSTANCE' ? (
            <CustomIcon type={INSTANCE.attr.dsType} instanceType={INSTANCE.attr.dsDeployType} />
          ) : nodeType === 'ENV' ? (
            <cc-svg-icon name={icon} size={16} />
          ) : (
            <CustomIcon type={icon} />
          )}
          <div
            style={{
              marginLeft: '3px',
              marginRight: `${nodeType === 'INSTANCE' && !node.connected ? '20px' : '0'}`,
              color: `${node.isNew ? 'green' : this.isDark ? '#fff' : '#000'}`,
              fontWeight: `${node.isNew ? 'bold' : 'default'}`
            }}
            innerHTML={this.highlight(title, this.searchKey)}></div>
          {nodeType === 'INSTANCE' && !node.connected && (
            <Tooltip placement='right' content={node.connectedMsg} transfer style={{ position: 'absolute', right: '3px' }}>
              <cc-svg-icon
                name='ds-disconnect'
                size={12}
                style={{
                  color: 'red',
                  marginRight: '3px'
                }}
              />
            </Tooltip>
          )}
          {children && children.length > 0 && <div style='font-weight: bold;color: #bbb;'>[{children.length}]</div>}
        </div>
      );
    },
    handleNodeRightClick(node) {
      this.selectedNode = node;
      this.$refs.tree.setSelected(node.key, true);
    },
    async handleExpandLoadNode(node, resolve, reject) {
      await this.getNodeData(node, {}, resolve, reject);
    },
    async handleSearch(scroll = true) {
      await this.$refs.tree.filter(this.searchKey);
      if (scroll && this.treeData[0]) {
        this.$refs.tree.scrollTo(this.treeData[0].key);
      }
    },
    handleFocus() {
      if (!this.currentTab) {
        return;
      }

      if (!this.currentTab.node || !this.currentTab.node.key) {
        return;
      }

      // 尝试最多4次定位（数据源tree最大深度为4）
      this.handleScrollToWithRetry(this.currentTab.node.key, 4);
    },

    /**
     * 带重试机制的定位方法
     * @param {string} targetKey 目标节点的key
     * @param {number} maxRetries 最大重试次数
     */
    async handleScrollToWithRetry(targetKey, maxRetries) {
      const attemptLocation = async (attempt = 1) => {
        const success = await this.handleScrollToWithAutoExpand(targetKey, 3);

        if (success) {
          return true;
        }

        if (attempt === maxRetries) {
          return false;
        }

        await new Promise((resolve) => setTimeout(resolve, 100));
        return attemptLocation(attempt + 1);
      };

      await attemptLocation();
    },

    /**
     * 递归展开到目标节点并滚动到该位置
     * @param {string} targetKey 目标节点的key
     * @param {number} maxRetries 最大重试次数
     * @returns {boolean} 是否成功
     */
    async handleScrollToWithAutoExpand(targetKey, maxRetries) {
      const targetNode = this.$refs.tree.getNode(targetKey);
      if (targetNode && targetNode.visible) {
        this.$refs.tree.scrollTo(targetKey, 'center');
        this.handleSetSelected(targetKey);
        return true;
      }

      // 没找到节点，直接递归
      const pathKeys = this.extractPathKeys(targetKey);
      const expandSuccess = await this.expandPathToTarget(pathKeys, targetKey, maxRetries);

      if (!expandSuccess) {
        return false;
      }

      await this.$nextTick();
      const finalNode = this.$refs.tree.getNode(targetKey);
      if (finalNode && finalNode.visible) {
        this.$refs.tree.scrollTo(targetKey, 'center');
        this.handleSetSelected(targetKey);
        return true;
      } else {
        return false;
      }
    },

    /**
     * 从key中提取路径数组
     * @param {string} key 完整的节点key
     * @returns {Array} 路径数组
     */
    extractPathKeys(key) {
      const parts = key.split('.');
      const pathKeys = [];

      for (let i = 1; i <= parts.length; i++) {
        const partialKey = parts.slice(0, i).join('.');
        pathKeys.push(partialKey);
      }

      return pathKeys;
    },

    /**
     * 递归展开路径到目标节点
     * @param {Array} pathKeys 路径数组
     * @param {string} targetKey 目标key
     * @param {number} maxRetries 最大重试次数
     * @returns {boolean} 是否成功
     */
    async expandPathToTarget(pathKeys, targetKey, maxRetries) {
      const expandPromises = [];

      for (let i = 0; i < pathKeys.length - 1; i++) {
        const currentKey = pathKeys[i];
        const nextKey = pathKeys[i + 1];

        const currentNode = this.$refs.tree.getNode(currentKey);
        if (!currentNode) {
          return false;
        }

        if (!currentNode.expanded) {
          expandPromises.push(this.expandNodeAndWait(currentKey, maxRetries));
        }
      }

      // 等待所有展开操作完成
      if (expandPromises.length > 0) {
        const results = await Promise.all(expandPromises);
        // 检查是否有任何展开操作失败
        if (results.some((result) => !result)) {
          return false;
        }
      }

      // 等待DOM更新
      await this.$nextTick();
      return true;
    },

    /**
     * 展开节点并等待加载完成
     * @param {string} key 节点key
     * @param {number} maxRetries 最大重试次数
     * @returns {boolean} 是否成功
     */
    async expandNodeAndWait(key, maxRetries) {
      this.$refs.tree.setExpand(key, true);

      // 使用递归避免循环中的await
      const waitForNodeLoad = async (retryCount = 0) => {
        if (retryCount >= maxRetries) {
          return false;
        }

        await new Promise((resolve) => setTimeout(resolve, 200));

        const node = this.$refs.tree.getNode(key);
        if (node && node.expanded && node.children && node.children.length > 0) {
          return true;
        }

        // 递归调用，增加重试次数
        return waitForNodeLoad(retryCount + 1);
      };

      return waitForNodeLoad();
    },

    handleScrollTo(key) {
      this.$refs.tree.setExpand(key, true);
      this.$refs.tree.scrollTo(key, 'center');
      this.handleSetSelected(key);
    },
    handleSwitchHide() {
      if (this.hide) {
        this.dataSourceWidth = 250;
        this.hide = false;
        this.saveHideState(false);
      } else {
        this.dataSourceWidth = 0;
        this.hide = true;
        this.saveHideState(true);
      }
    },
    handleShowAddDsModal() {
      this.showAddDsModal = true;
    },
    async handleAddDs() {
      await this.$refs.addDatasource.handleAddPersonalDataSource();
    },
    async handleTestDs() {
      await this.$refs.addDatasource.handleAddPersonalDataSource(true);
    },
    handleCloseAddDsModal() {
      this.showAddDsModal = false;
    },
    //
    handleNodeClick(node) {
      this.selectedNode = node;
      this.$refs.tree.setSelected(node.key, true);
    },
    onContextmenu(event) {
      if (!this.selectedNode) {
        return;
      }
      const menuList = this.getBrowserMenus(
        this.selectedNode && this.selectedNode.nodeType === NODE_TYPE.ENV ? null : this.selectedNode.INSTANCE.attr.dsType,
        this.selectedNode.nodeType
      );
      if (menuList) {
        const items = [];
        menuList.forEach((menu, menuIndex) => {
          if (menu.menuId !== 'MENU_SEPARATOR') {
            items.push({
              label: menu.i18n,
              svgProps: {
                class: 'svg-icon'
              },
              svgIcon: `#icon-svg-${menu.menuId}`,
              divided: menuList[menuIndex + 1] && menuList[menuIndex + 1].menuId === 'MENU_SEPARATOR',
              onClick: () => this.handleRightClickMenu(menu.menuId)
            });
          }
        });

        if (items.length) {
          ContextMenu.showContextMenu({
            x: event.x,
            y: event.y,
            theme: 'flat',
            items,
            customClass: 'custom-class',
            zIndex: 3,
            minWidth: 100
          });
        }
      }
    },
    async handleRightClickMenu(actionType) {
      console.log('handleRightClickMenu', actionType);
      this.actionType = actionType;
      const data = {
        actionType,
        callback: null,
        other: {
          targetType: '',
          targetName: '',
          targetNewName: '',
          options: {}
        }
      };

      switch (actionType) {
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CONSOLE:
          this.handleAddTab(TAB_TYPE.QUERY, this.selectedNode, { force: true });
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_COPY_NAME:
          this.copyText(this.selectedNode.title);
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_COPY_JDBC:
          this.copyText(this.selectedNode.INSTANCE.attr.dsHost);
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PERMISSIONS:
          this.$router.push({ path: '/system/permission', query: { type: 'apply' } });
          break;
        default:
          break;
      }

      switch (actionType) {
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_REFRESH:
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_INSTANCE_REFRESH:
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CATALOG_REFRESH:
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEMA_REFRESH:
          const callback = async () => {
            if (this.selectedNode.isLeaf) {
              if (this.selectedNode.key === this.currentTab?.node?.key) {
                this.currentTab.expandedKeys = [];
                const refreshCache = true;
                await this.listLeaf(refreshCache);
              }
            } else {
              this.expandedKeys = this.expandedKeys.filter((key) => !key.includes(this.selectedNode.key));
              const refreshCache = true;
              await this.listLevels(this.selectedNode, {}, null, null, refreshCache);
            }
          };
          const refreshCache = true;
          await this.detailLevels(this.selectedNode, callback, refreshCache);
          break;
        default:
          break;
      }

      switch (actionType) {
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_INSTANCE_CREATE:
          await this.$router.push('/system/ccdatasource');
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_INSTANCE_RENAME:
          if (!this.dsDesc) {
            this.dsDesc = this.selectedNode.title;
            this.showEditDsDescModal = true;
          } else {
            const res = await this.$services.dmBrowseActionsInstanceRemark({
              data: {
                levels: this.browseGenLevelsData(this.selectedNode),
                remark: this.dsDesc
              }
            });

            if (res.success) {
              this.showEditDsDescModal = false;
              this.dsDesc = '';
              this.$message.success(this.$t('xiu-gai-shi-li-bei-zhu-cheng-gong'));
              const refreshCache = true;
              await this.detailLevels(this.selectedNode, () => {}, refreshCache);
            }
          }
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_INSTANCE_DROP:
          this.showDeleteInstanceModal = true;
          break;
        default:
          break;
      }

      switch (actionType) {
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CATALOG_CREATE:
          if (!this.menuModal.name) {
            this.menuModal.show = true;
            this.menuModal.showNameInput = true;
            this.menuModal.title = this.$t('xin-jian-shu-ju-ku');
            this.menuModal.content = this.selectedNode.nodeType === NODE_TYPE.CATALOG ? this.selectedNode._parent.popTip : this.selectedNode.popTip;
          } else {
            data.other.targetName = this.menuModal.name;
            data.other.targetType = NODE_TYPE.CATALOG;
            data.other.options = this.menuModal.options;
            data.callback = (permission, danger, sql, genActionData) => {
              this.menuModal.permission = permission;
              this.menuModal.danger = danger;
              this.menuModal.sql = sql;
              this.menuModal.actionData = genActionData;
            };
            await this.browseGenAction(
              data.actionType,
              this.browseGenLevelsData(
                this.selectedNode,
                this.selectedNode.nodeType === NODE_TYPE.CATALOG ? this.selectedNode._parent.levels : this.selectedNode.levels
              ),
              data.callback,
              data.other
            );
          }
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CATALOG_DROP:
          data.other.targetName = this.selectedNode.title;
          data.other.targetType = NODE_TYPE.CATALOG;
          data.other.options = this.menuModal.options;
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.show = true;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.sql = sql;
            this.menuModal.actionData = genActionData;
            this.menuModal.title = this.$t('que-ren');
            this.menuModal.name = this.selectedNode.title;
            this.menuModal.preName = this.selectedNode.title;
            this.menuModal.content = this.$t('que-ding-yao-shan-chu-thisselectednodepoptip-ma', [this.selectedNode.popTip]);
          };
          await this.browseGenAction(
            data.actionType,
            this.browseGenLevelsData(this.selectedNode, this.selectedNode._parent.levels),
            data.callback,
            data.other
          );
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CATALOG_REFRESH:
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CATALOG_RENAME:
          if (!this.menuModal.name) {
            this.menuModal.show = true;
            this.menuModal.name = this.selectedNode.title;
            this.menuModal.preName = this.selectedNode.title;
            this.menuModal.title = this.$t('zhong-ming-ming');
            this.menuModal.showNameInput = true;
            this.menuModal.content = this.$t('zhong-ming-ming-thisselectednodepoptip-wei', [this.selectedNode.popTip]);
          } else {
            data.other.targetName = this.selectedNode.title;
            data.other.targetNewName = this.menuModal.name;
            data.other.targetType = this.selectedNode.nodeType;
            data.other.options = this.menuModal.options;
            data.callback = (permission, danger, sql, genActionData) => {
              this.menuModal.permission = permission;
              this.menuModal.danger = danger;
              this.menuModal.sql = sql;
              this.menuModal.actionData = genActionData;
            };
            await this.browseGenAction(
              data.actionType,
              this.browseGenLevelsData(this.selectedNode, this.selectedNode._parent.levels),
              data.callback,
              data.other
            );
          }
          break;
        default:
          break;
      }

      switch (actionType) {
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEMA_CREATE:
          if (!this.menuModal.name) {
            this.menuModal.show = true;
            this.menuModal.showNameInput = true;
            this.menuModal.title = this.$t('xin-jian-schema');
            this.menuModal.content = this.selectedNode.nodeType !== NODE_TYPE.SCHEMA ? this.selectedNode.popTip : this.selectedNode._parent.popTip;
          } else {
            data.other.targetName = this.menuModal.name;
            data.other.targetType = NODE_TYPE.SCHEMA;
            data.other.options = this.menuModal.options;
            data.callback = (permission, danger, sql, genActionData) => {
              this.menuModal.permission = permission;
              this.menuModal.danger = danger;
              this.menuModal.sql = sql;
              this.menuModal.actionData = genActionData;
            };
            await this.browseGenAction(
              data.actionType,
              this.browseGenLevelsData(
                this.selectedNode,
                this.selectedNode.nodeType !== NODE_TYPE.SCHEMA ? this.selectedNode.levels : this.selectedNode._parent.levels
              ),
              data.callback,
              data.other
            );
          }
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_CREATE:
          this.handleAddTab(TAB_TYPE.STRUCT, this.selectedNode, {
            editorType: ACTION_TYPE.CREATE_TABLE
          });
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEMA_RENAME:
          if (!this.menuModal.name) {
            this.menuModal.show = true;
            this.menuModal.title = this.$t('zhong-ming-ming');
            this.menuModal.name = this.selectedNode.title;
            this.menuModal.preName = this.selectedNode.title;
            this.menuModal.showNameInput = true;
            this.menuModal.content = this.$t('zhong-ming-ming-thisselectednodepoptip-wei', [this.selectedNode.popTip]);
          } else {
            data.other.targetName = this.selectedNode.title;
            data.other.targetNewName = this.menuModal.name;
            data.other.targetType = this.selectedNode.nodeType;
            data.other.options = this.menuModal.options;
            data.callback = (permission, danger, sql, genActionData) => {
              this.menuModal.permission = permission;
              this.menuModal.danger = danger;
              this.menuModal.sql = sql;
              this.menuModal.actionData = genActionData;
            };
            await this.browseGenAction(
              data.actionType,
              this.browseGenLevelsData(this.selectedNode, this.selectedNode._parent.levels),
              data.callback,
              data.other
            );
          }
          break;
        case DS_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEMA_DROP:
          data.other.targetName = this.selectedNode.title;
          data.other.targetType = this.selectedNode.nodeType;
          data.other.options = this.menuModal.options;
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.show = true;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.sql = sql;
            this.menuModal.actionData = genActionData;
            this.menuModal.title = this.$t('que-ren');
            this.menuModal.name = this.selectedNode.title;
            this.menuModal.preName = this.selectedNode.title;
            this.menuModal.content = this.$t('que-ding-yao-shan-chu-thisselectednodepoptip-ma', [this.selectedNode.popTip]);
          };
          await this.browseGenAction(
            data.actionType,
            this.browseGenLevelsData(this.selectedNode, this.selectedNode._parent.levels),
            data.callback,
            data.other
          );
          break;
        default:
          break;
      }
    }
  }
};
</script>

<template>
  <div class="data-source-container" :style="`width: ${dataSourceWidth}px`">
    <div class="tree-resize" />
    <div class="data-source-filter" v-if="!hide">
      <!--      <Icon type="md-add" style="margin-right: 5px;" @click="handleShowAddDsModal" v-if="isDesktop"/>-->
      <Input v-model="searchKey" class="filter-input" icon="ios-search" @on-click="handleSearch" @on-enter="handleSearch" size="small" allow-clear />
      <cc-svg-icon :size="18" name="focus" @click.native="handleFocus" style="cursor: pointer" :color="`${isDark ? '#fff' : '#000'}`"></cc-svg-icon>
      <cc-svg-icon
        style="margin-left: 6px"
        name="refresh"
        @click.native="handleRefreshTree"
        :size="16"
        :color="`${isDark ? '#fff' : '#000'}`"
      ></cc-svg-icon>
    </div>
    <cc-iconfont @click.native="handleSwitchHide" :size="14" class="hide-icon" color="#999999" :name="hide ? 'zhankai' : 'shouqi'"></cc-iconfont>
    <div class="datasource-tree" @contextmenu.prevent.stop="onContextmenu">
      <v-tree
        emptyText=" "
        ref="tree"
        keyField="key"
        :load="handleExpandLoadNode"
        :render="renderNode"
        :expand-on-filter="false"
        @node-right-click="handleNodeRightClick"
        @node-dblclick="handleDblClick"
        :nodeIndent="10"
        :renderNodeAmount="200"
        @click="handleNodeClick"
      ></v-tree>
    </div>
    <CCModal :title="menuModal.title" v-model="menuModal.show" :mask-closable="false" :closable="false" :keyboard="false">
      <div style="margin-bottom: 5px; font-weight: bold">
        {{ menuModal.content }}
      </div>
      <a-collapse :bordered="false" size="small" style="margin: 5px 0" v-model="menuModal.collapseKey" :destroyInactivePanel="true">
        <a-collapse-panel :header="$t('gao-ji-pei-zhi')" key="options">
          <div v-for="setting in advancedSetting" :key="setting.value">
            <a-checkbox :value="menuModal.options[setting.value]" @change="handleMenuOptionChange(setting.value, $event)">
              {{ setting.label }}
            </a-checkbox>
          </div>
        </a-collapse-panel>
      </a-collapse>
      <div style="display: flex" v-if="menuModal.showNameInput">
        <a-input :value="menuModal.name" :placeholder="$t('qing-shu-ru-xin-de-ming-zi')" @change="handleMenuNameChange" allow-clear />
      </div>
      <div style="margin-top: 5px; font-weight: bold" v-if="menuModal.sql">{{ $t('sql-yu-ju') }}:</div>
      <div style="width: 100%; border: 1px solid #ccc; padding: 3px 10px" v-if="menuModal.sql">
        <pre>{{ menuModal.sql }}</pre>
      </div>
      <template #footer>
        <a-button type="primary" @click="handleRightClickMenu(actionType)" v-if="!menuModal.sql" :disabled="!menuModal.name">
          {{ $t('sheng-cheng-sql-yu-ju') }}
        </a-button>
        <a-button
          v-if="menuModal.permission && menuModal.sql"
          type="primary"
          :danger="menuModal.danger"
          @click="handleDoAction"
          :loading="doActionLoading"
        >
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button
          v-if="!menuModal.permission && menuModal.sql"
          @click="
            rawSqlToSubmit = menuModal.sql;
            ticketData.ticketTitle = `${$t('gong-dan')}${new Date().getTime()}`;
            ticketData.description = '';
            showTicketModal = true;
          "
        >
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal
      v-model="showEditDsDescModal"
      :mask-closable="false"
      :closable="false"
      :keyboard="false"
      :title="$t('xiu-gai-shi-li-bei-zhu')"
      v-if="showEditDsDescModal"
    >
      <div style="margin-bottom: 5px; font-weight: bold">
        {{ $t('xiu-gai-selectednodepoptip-de-bei-zhu-selectednodetitle-wei', [selectedNode.popTip, selectedNode.title]) }}
      </div>
      <a-input v-model="dsDesc" :placeholder="$t('qing-shu-ru-xin-de-bei-zhu')" allow-clear />
      <template #footer>
        <a-button @click="handleRightClickMenu(actionType)">{{ $t('xiu-gai') }}</a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal
      v-model="showDeleteInstanceModal"
      :mask-closable="false"
      :closable="false"
      :keyboard="false"
      :title="$t('shan-chu-shi-li')"
      v-if="showDeleteInstanceModal"
    >
      <div style="margin-bottom: 5px; font-weight: bold">{{ $t('que-ding-yao-shan-chu-selectednodepoptip-ma', [selectedNode.popTip]) }}?</div>
      <template #footer>
        <a-button @click="handleDeleteInstance(actionType)" type="danger">
          {{ $t('shan-chu') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="showAddDsModal" :width="1000" :title="$t('xin-zeng-shu-ju-yuan')">
      <div style="height: 500px; overflow: auto">
        <add-data-source
          :is-modal="true"
          ref="addDatasource"
          :handle-close-add-ds-modal="handleCloseAddDsModal"
          :handle-set-test-ds-msgContent="handleSetTestDsMsg"
          v-if="showAddDsModal"
        />
      </div>
      <template #footer>
        <div style="display: flex; justify-content: space-between">
          <div style="display: flex; align-items: center">
            <Button type="primary" @click="handleTestDs">{{ $t('ce-shi-lian-jie') }}</Button>
            <div style="display: flex; margin-left: 5px; align-items: center" v-if="testDsMsg">
              <Icon type="ios-checkmark-circle" v-if="testDsMsg === $t('ce-shi-lian-jie-cheng-gong')" size="20" color="green" />
              <Icon type="ios-close-circle" v-else size="20" color="red" />
              <div style="margin-left: 5px">{{ testDsMsg }}</div>
            </div>
          </div>
          <div>
            <Button type="primary" @click="handleAddDs">{{ $t('xin-zeng-shu-ju-yuan') }}</Button>
            <Button @click="handleCloseModal">{{ $t('guan-bi') }}</Button>
          </div>
        </div>
      </template>
    </CCModal>
    <CCModal v-model="showTicketModal" :title="$t('ti-jiao-gong-dan')" @on-cancel="showTicketModal = false">
      <a-form :model="ticketData" :rules="ticketRuleValidate" ref="ticketContent">
        <a-form-item :label="$t('biao-ti')" prop="ticketTitle">
          <Input v-model="ticketData.ticketTitle" />
        </a-form-item>
        <a-form-item :label="$t('xu-qiu-miao-shu')" prop="description">
          <Input type="textarea" v-model="ticketData.description" :rows="4" />
        </a-form-item>
      </a-form>
      <template #footer>
        <Button type="text" @click="showTicketModal = false">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="submitTicket">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
  </div>
</template>

<style scoped lang="less">
:deep(.ctree-tree-node__title) {
  padding-left: 0;
  margin-left: 0;
}

.data-source-container {
  background: #ffffff;
  height: 100%;
  float: left;
  display: flex;
  flex-direction: column;
  position: relative;
  border-right: 1px solid #ccc;

  .tree-resize {
    height: 100%;
    width: 6px;
    background: rgba(0, 0, 0, 0);
    //background: red;
    position: absolute;
    right: -3px;
    cursor: col-resize;
    z-index: 9;
  }

  .hide-icon {
    position: absolute;
    border-radius: 5px;
    right: -28px;
    z-index: 9;
    top: 7px;
    background: #fff;
    padding: 2px 5px;
    cursor: pointer;
    box-shadow: rgba(0, 0, 0, 0.35) 0px 1px 2px;
  }

  .datasource-tree {
    padding: 2px 0 0 4px;
    flex: 1;
    min-height: 0;

    :deep(.node) {
      display: flex;
      align-items: center;
    }
  }
}

:deep(.ant-collapse-header) {
  padding: 5px 10px 5px 14px !important;
}

:deep(.ant-collapse-item) {
  border: none;
}

:deep(.ant-collapse-content-box) {
  padding: 8px 10px;
}

:deep(.highlight) {
  background: orange !important;
}

[data-theme='dark'] {
  .data-source-container {
    background: var(--bg-primary);
  }
}
</style>

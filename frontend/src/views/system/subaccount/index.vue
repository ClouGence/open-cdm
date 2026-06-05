<template>
  <div class="sub-account">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('zi-zhang-hao-guan-li') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="option border-radius-card">
            <div class="left">
              <Select v-model="search.roleId" style="width: 120px; margin-right: 10px" clearable>
                <Option v-for="option in roleList" :key="option.value" :value="option.value">
                  {{ option.name }}
                </Option>
              </Select>
              <Input
                v-model="search.userNameOrSubAccountPrefix"
                :placeholder="$t('qing-shu-ru-xing-ming-zi-zhang-hao-uid-cha-xun')"
                style="width: 280px; margin-right: 10px"
                @on-keydown="handleEnterSearch"
                clearable
              />
              <Button :loading="subAccountListLoading" type="primary" @click="getSubAccountList('init')">
                {{ $t('cha-xun') }}
              </Button>
            </div>
            <div class="right">
              <Button @click="handleClickAddBtn" type="primary" ghost style="margin-right: 10px" icon="md-add">
                {{ $t('tian-jia-zi-zhang-hao') }}
              </Button>
              <Button @click="getSubAccountList('init')">
                <CustomIcon type="icon-v2-Refresh" v-if="!subAccountListLoading" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table
              :columns="subAccountColumns"
              :data="showSubAccountList"
              :locale="{ emptyText: $t('zan-wu-shu-ju') }"
              :loading="subAccountListLoading"
              size="small"
              border
              stripe
            >
              <template #status="{ row }">
                <div class="center">
                  <cc-status :type="row.disable ? 'error' : 'success'" />
                </div>
              </template>
              <template #username="{ row }">
                <span>{{ row.username }}</span>
              </template>
              <template #phone="{ row }">
                <div class="text">{{ row.phone }}</div>
              </template>
              <template #mail="{ row }">
                <div class="text">{{ row.email }}</div>
              </template>
              <template #roleName="{ row }">
                <div class="role-name-container">
                  <div class="role-name-text">
                    {{ row.roleName }}
                  </div>
                  <Tag class="rule-default-tag" v-if="row.innerRole" style="margin-left: 4px">
                    {{ $t('nei-0') }}
                  </Tag>
                </div>
              </template>
              <template #resources="{ row }">
                <i-switch
                  true-color="#52C41A"
                  :disabled="row.disable || !myAuth.includes('RDP_AUTH_MANAGE')"
                  v-model="row.resourceManage"
                  @on-change="handleEnableResourceChange(row, $event)"
                ></i-switch>
              </template>
              <template #subAccount="{ row }">
                <div class="py-4">
                  <div class="flex items-center">
                    <cc-status class="mr-2" :type="row.disable ? 'error' : 'success'" />
                    <div>
                      <div class="copy-account">
                        <Tooltip :content="$t('dian-ji-fu-zhi-zi-zhang-hao')" transfer placement="top">
                          <span
                            class="text font-semibold text-base w-max max-w-[160px] truncate"
                            @click="copyText(`${row.bindType == 'INTERNAL' ? row.subAccount : row.bindAccount}`, $t('fu-zhi-zhang-hao-cheng-gong'))"
                          >
                            {{ row.bindAccount }}
                          </span>
                        </Tooltip>
                        <Tooltip :content="$t('mfa-yi-kai-qi')" transfer placement="top" v-if="row.useMfa">
                          <Tag style="margin-left: 4px" color="primary" class="use-mfa">MFA</Tag>
                        </Tooltip>
                        <!--                        <Tooltip :content="$t('mfa-yi-guan-bi')" transfer placement="top" v-if="!row.useMfa">-->
                        <!--                          <Tag style="margin-left: 4px" color="default" class="unuse-mfa">MFA</Tag>-->
                        <!--                        </Tooltip>-->
                        <Tag v-if="row.bindType !== 'INTERNAL' && row.bindType" style="margin-left: 4px" color="primary" class="use-mfa">
                          {{ row.bindType }}
                        </Tag>
                      </div>
                    </div>
                  </div>
                  <div class="uid mt-2">
                    <span class="opacity-60">{{ $t('yong-hu-uid-0') }}{{ row.uid }}</span>
                    <cc-iconfont
                      :size="12"
                      name="copy"
                      class="copy"
                      @click.native="copyText(`${row.uid}`, $t('fu-zhi-uid-cheng-gong'))"
                      style="margin-left: 3px"
                    />
                  </div>
                </div>
              </template>
              <template #lastTryLoginTime="{ row }">
                <div>{{ row.lastTryLoginTime }}</div>
              </template>
              <template #action="{ row }">
                <div class="action">
                  <a
                    v-if="myAuth.includes('RDP_AUTH_READ')"
                    :style="{ color: row.disable || row.resourceManage ? '#ccc' : '' }"
                    @click="!(row.disable || row.resourceManage) && goAuthPage('edit', row)"
                    type="primary"
                  >
                    {{ $t('shou-quan') }}
                  </a>
                  <a
                    v-if="myAuth.includes('RDP_USER_MANAGE')"
                    :style="{ color: row.disable || row.resourceManage ? '#ccc' : '' }"
                    type="primary"
                    @click="handleTriggerStopAccount(row)"
                  >
                    {{ row.disable ? $t('qi-yong') : $t('ting-yong') }}
                  </a>
                  <Poptip
                    v-if="!forbidDelSubAccount && myAuth.includes('RDP_USER_MANAGE')"
                    :title="$t('que-ding-shan-chu-gai-zi-zhang-hao-ma')"
                    transfer
                    confirm
                    @on-ok="handleDeleteSubAccount(row)"
                  >
                    <a type="primary">{{ $t('shan-chu') }}</a>
                  </Poptip>
                  <Dropdown transfer v-if="myAuth.includes('RDP_USER_MANAGE') || myAuth.includes('RDP_AUTH_READ')">
                    <a href="javascript:void(0)">
                      {{ $t('geng-duo') }}
                      <Icon type="ios-arrow-down"></Icon>
                    </a>
                    <template #list>
                      <DropdownMenu>
                        <!-- 仅保留资源授权入口 -->
                        <!-- <DropdownItem v-if="myAuth.includes('RDP_AUTH_READ')">
                          <a @click="goAuthPage('view', row)">
                            {{ $t('yi-shou-quan-xian') }}
                          </a>
                        </DropdownItem> -->
                        <DropdownItem v-if="myAuth.includes('RDP_USER_MANAGE')" @click="handleShowModifyAccount(row)">
                          <a v-if="row.bindType === 'INTERNAL'">
                            {{ $t('xiu-gai-zhang-hao') }}
                          </a>
                          <Tooltip
                            :content="$t('bu-zhi-chi-ldap-yu-zhang-hao-he-mi-ma-de-xiu-gai-qing-lian-xi-ldap-yu-guan-li-yuan')"
                            v-else
                            transfer
                            placement="left"
                          >
                            <a style="color: #ccc">
                              {{ $t('xiu-gai-zhang-hao') }}
                            </a>
                          </Tooltip>
                        </DropdownItem>
                        <DropdownItem v-if="myAuth.includes('RDP_USER_MANAGE')">
                          <a v-if="row.bindType === 'INTERNAL'" @click="handleShowEditPasswordModal(row)">
                            {{ $t('chong-zhi-mi-ma') }}
                          </a>
                          <Tooltip
                            :content="$t('bu-zhi-chi-ldap-yu-zhang-hao-he-mi-ma-de-xiu-gai-qing-lian-xi-ldap-yu-guan-li-yuan')"
                            v-else
                            transfer
                            placement="left"
                          >
                            <a :disabled="row.bindType !== 'INTERNAL'" @click="handleShowEditPasswordModal(row)">
                              {{ $t('chong-zhi-mi-ma') }}
                            </a>
                          </Tooltip>
                        </DropdownItem>
                        <DropdownItem v-if="myAuth.includes('RDP_USER_MANAGE')">
                          <a :style="{ color: row.disable ? '#ccc' : '' }" @click="!row.disable && handleClickChangeRoleModal(row)">
                            {{ $t('xiu-gai-jiao-se') }}
                          </a>
                        </DropdownItem>
                      </DropdownMenu>
                    </template>
                  </Dropdown>
                </div>
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
          :model-value="pageNum"
          :page-size="pageSize"
          @on-page-size-change="handlePageSizeChange"
        />
      </div>
    </div>
    <CCModal v-model="showAddNewSubAccountModal" :mask-closable="false" :width="646" :title="$t('chuang-jian-zi-zhang-hao')">
      <Form ref="newAccountFormRef" :model="newAccountForm" :rules="newAccountRules" v-if="showAddNewSubAccountModal" :label-width="100">
        <FormItem :label="$t('xing-ming')" prop="userName">
          <Input v-model="newAccountForm.userName" />
        </FormItem>
        <FormItem :label="$t('zi-zhang-hao')" prop="subAccount">
          <Input v-model="newAccountForm.subAccount">
            <template #append>@{{ userInfo && userInfo.userDomain ? userInfo.userDomain : '' }}</template>
          </Input>
        </FormItem>
        <FormItem :label="$t('deng-lu-mi-ma')" prop="password">
          <div style="display: flex; width: 100%">
            <Input v-model="newAccountForm.password" :placeholder="passwordRule.tips" />
            <Button style="margin-left: 10px" @click="generateRandomPwd" type="primary" ghost>
              {{ $t('zi-dong-sheng-cheng') }}
            </Button>
          </div>
        </FormItem>
        <FormItem :label="$t('jiao-se')" prop="roleId">
          <div style="display: flex; width: 100%">
            <Select v-model="newAccountForm.roleId" :placeholder="$t('qing-xuan-ze')" style="margin-right: 10px">
              <Option v-for="role in roleList" :key="role.value" :value="role.value" :label="role.name">
                <div class="role-name-container">
                  <div class="role-name-text">
                    {{ role.name }}
                  </div>
                  <Tag class="rule-default-tag" color="default" v-if="role.innerTag" style="margin-left: 4px">
                    {{ $t('nei-0') }}
                  </Tag>
                </div>
              </Option>
            </Select>
            <Button type="text" to="/system/role">{{ $t('chuang-jian-jiao-se') }}</Button>
          </div>
        </FormItem>
        <h4 class="mb-4">{{ $t('tong-zhi-jie-shou-she-zhi') }}</h4>
        <FormItem :label="$t('shou-ji-hao')" prop="phone">
          <Input v-model="newAccountForm.phone" />
        </FormItem>
        <FormItem :label="$t('you-xiang')" prop="email">
          <Input v-model="newAccountForm.email" />
        </FormItem>
      </Form>
      <template #footer>
        <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="loading" @click="handleAddSubAccount">
          {{ $t('chuang-jian') }}
        </Button>
      </template>
    </CCModal>
    <CCModal v-model="showChangeRoleModal" :width="320" :title="$t('xiu-gai-jiao-se')" @on-cancel="handleCloseModal">
      <Select v-model="newAccountForm.roleId" style="width: 276px">
        <Option v-for="role in roleList" :label="role.name" :key="role.value" :value="role.value">
          <div class="role-name-container">
            <div class="role-name-text">
              {{ role.name }}
            </div>
            <Tag class="rule-default-tag" color="default" v-if="role.innerTag" style="margin-left: 4px">
              {{ $t('nei-0') }}
            </Tag>
          </div>
        </Option>
      </Select>
      <template #footer>
        <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="loading" @click="handleChangeUserRole">
          {{ $t('que-ding') }}
        </Button>
      </template>
    </CCModal>
    <CCModal v-model="showModifySubAccount" :width="560" :title="$t('xiu-gai-zhang-hao')" @on-cancel="handleCloseModal">
      <Form :model="newAccountForm" :label-width="100">
        <FormItem :label="$t('xing-ming')" prop="userName">
          <Input v-model="newAccountForm.userName" />
        </FormItem>
        <FormItem :label="$t('zi-zhang-hao')" prop="subAccount">
          <Input v-model="newAccountForm.accountName">
            <template #append>@{{ newAccountForm.userDomain }}</template>
          </Input>
        </FormItem>
      </Form>
      <template #footer>
        <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :disabled="!isEdited" :loading="loading" @click="handleModifySubAccount">
          {{ $t('que-ding') }}
        </Button>
      </template>
    </CCModal>
    <CCModal v-model="showEditPasswordModal" :title="$t('chong-zhi-de-mi-ma', [newAccountForm.username])" width="640px" :mask-closable="false">
      <div style="margin-bottom: 10px" v-if="editPasswordErrMsg">
        <Alert type="error">{{ editPasswordErrMsg }}</Alert>
      </div>
      <Form :label-width="160" ref="passwordConfirmForm">
        <FormItem :label="$t('cao-zuo-ren-mi-ma')">
          <Input type="password" password v-model="opUserPassword" :placeholder="$t('xu-yan-zheng-cao-zuo-ren-shen-fen')"></Input>
        </FormItem>
        <FormItem :label="$t('xin-mi-ma')">
          <Poptip trigger="focus" placement="right-start" transfer style="width: 100%">
            <div style="display: flex">
              <Input
                v-model="newPassword"
                type="password"
                password
                style="width: 298px"
                :placeholder="$t('qing-shu-ru-zi-zhang-hao-de-xin-mi-ma')"
              ></Input>
              <Button style="margin-left: 10px" @click="generateRandomPwd" type="primary" ghost>
                {{ $t('zi-dong-sheng-cheng') }}
              </Button>
            </div>
            <template #content>
              <p>{{ passwordRule.tips }}</p>
            </template>
          </Poptip>
        </FormItem>
      </Form>
      <template #footer>
        <Button @click="handleCloseModal">{{ $t('guan-bi') }}</Button>
        <Button type="primary" :loading="loading" @click="handleConfirmEditPassword">
          {{ $t('que-ding') }}
        </Button>
      </template>
    </CCModal>
    <CCModal :title="$t('ti-shi')" v-model="showEnableResourceModal">
      <div>
        {{ $t('que-ding-yao-shou-quan-quan-bu-zi-yuan-gei-selectedsubaccountusername', [selectedSubaccount.username]) }}
      </div>
      <template #footer>
        <Button @click="handleCloseEnableResourceModal">{{ $t('guan-bi') }}</Button>
        <Button type="primary" :loading="loading" @click="handleEnableResource">
          {{ $t('que-ding') }}
        </Button>
      </template>
    </CCModal>
    <CCModal :title="$t('ti-shi')" v-model="showDisableResourceModal">
      <div>
        {{ $t('que-ding-yao-qu-xiao-selectedsubaccountusername-de-quan-bu-shou-quan', [selectedSubaccount.username]) }}
      </div>
      <template #footer>
        <Button @click="handleCloseDisableResourceModal">{{ $t('guan-bi') }}</Button>
        <Button type="primary" :loading="loading" @click="handleDisableResource">
          {{ $t('que-ding') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import { generateData } from '@/utils';
import copyMixin from '@/mixins/copyMixin';
import enterOpPwdMixin from '@/mixins/modal/enterOpPwdMixin';
import { encryptMixin } from '@/mixins/encryptMixin';
import RandExp from 'randexp';
import deepClone from 'lodash.clonedeep';

const EMPTY_SUB_ACCOUNT = {
  userName: '',
  subAccount: '',
  password: '',
  roleId: undefined,
  phone: '',
  email: ''
};

// 数据和方法带2的都是数据库管理权限相关
export default {
  name: 'SubAccount',
  mixins: [copyMixin, enterOpPwdMixin, encryptMixin],
  computed: {
    ...mapState(['userInfo', 'globalSetting', 'myCatLog', 'myAuth']),
    ...mapGetters(['includesDM']),
    isEdited() {
      // 如果有空值，视为未编辑
      if (!this.newAccountForm.userName || !this.newAccountForm.accountName) {
        return false;
      }
      return this.originAccount.userName !== this.newAccountForm.userName || this.originAccount.accountName !== this.newAccountForm.accountName;
    }
  },
  data() {
    return {
      showAddBtn: false,
      originAccount: {
        userName: '',
        accountName: ''
      },
      showEnableResourceModal: false,
      showDisableResourceModal: false,
      newAccountForm: {
        userName: '',
        accountName: '',
        ...deepClone(EMPTY_SUB_ACCOUNT)
      },
      newAccountRules: {},
      search: {
        roleId: '',
        userNameOrSubAccountPrefix: ''
      },
      subAccountColumns: [
        // {
        //   title: ' ',
        //   slot: 'status',
        //   width: 45
        // },
        {
          title: this.$t('xing-ming'),
          slot: 'username',
          width: 150
        },
        {
          title: this.$t('zhang-hao'),
          slot: 'subAccount',
          minWidth: 320
        },
        {
          title: this.$t('shou-ji-hao'),
          slot: 'phone',
          minWidth: 150
        },
        {
          title: this.$t('you-xiang'),
          slot: 'mail',
          minWidth: 200
        },
        {
          title: this.$t('jiao-se'),
          slot: 'roleName',
          minWidth: 200
        },
        {
          title: this.$t('quan-bu-zi-yuan-quan-xian'),
          slot: 'resources',
          width: 115
        },
        {
          title: this.$t('shang-ci-deng-lu-shi-jian'),
          slot: 'lastTryLoginTime',
          width: 180
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action',
          fixed: 'right',
          width: 330
        }
      ],
      subAccountListLoading: false,
      showEditPasswordModal: false,
      opUserPassword: '',
      newPassword: '',
      editPasswordErrMsg: '',
      showChangeRoleModal: false,
      showModifySubAccount: false,
      showAddNewSubAccountModal: false,
      showManageRoleModal: false,
      selectedSubaccount: {
        name: '',
        account: '',
        role: '',
        status: '',
        createTime: ''
      },
      total: 0,
      pageSize: 20,
      pageNum: 1,
      subAccountList: [],
      showSubAccountList: [],
      roleList: [],
      forbidDelSubAccount: false,
      passwordRule: {
        expr: '',
        tips: ''
      },
      loading: false
    };
  },
  async mounted() {
    await this.getConfigValueList();
  },
  methods: {
    async getConfigValueList() {
      const res = await this.$services.rdpUserConfigGetUserSpecifiedConfs({
        data: {
          configNames: ['subAccountAuthType', 'forbidDelSubAccount']
        }
      });

      if (res.success && res.data) {
        if (res.data.forbidDelSubAccount && res.data.forbidDelSubAccount.configValue) {
          this.forbidDelSubAccount = JSON.parse(res.data.forbidDelSubAccount.configValue);
        }
      }
    },
    handleEnterSearch(e) {
      if (e.code === 'Enter') {
        e.preventDefault();
        this.getSubAccountList();
      }
    },
    async handleEnableResource() {
      const res = await this.$services.rdpUserManagerUpdateResourceManage({
        data: {
          targetUid: this.selectedSubaccount.uid,
          resourceManage: true
        }
      });

      if (res.success) {
        this.showEnableResourceModal = false;
        this.$Message.success(this.$t('shou-quan-cheng-gong'));
      }
      this.getSubAccountList();
    },
    handleCloseEnableResourceModal() {
      this.showEnableResourceModal = false;
      this.selectedSubaccount.resourceManage = false;
    },
    async handleDisableResource() {
      const res = await this.$services.rdpUserManagerUpdateResourceManage({
        data: {
          targetUid: this.selectedSubaccount.uid,
          resourceManage: false
        }
      });

      if (res.success) {
        this.showDisableResourceModal = false;
        this.$Message.success(this.$t('jie-chu-shou-quan-cheng-gong'));
        this.getSubAccountList();
      }
    },
    async handleCloseDisableResourceModal() {
      this.showDisableResourceModal = false;
      this.selectedSubaccount.resourceManage = true;
    },
    async handleEnableResourceChange(subAccount, e) {
      this.selectedSubaccount = subAccount;
      if (e) {
        this.showEnableResourceModal = true;
      } else {
        this.showDisableResourceModal = true;
      }
    },
    async handleModifySubAccount() {
      this.loading = true;
      const { uid, userDomain } = this.newAccountForm;
      const data = { targetUid: uid };
      // 只传递被修改的字段
      if (this.originAccount.userName !== this.newAccountForm.userName) {
        data.userName = this.newAccountForm.userName;
      }
      if (this.originAccount.accountName !== this.newAccountForm.accountName) {
        data.subAccount = `${this.newAccountForm.accountName}@${userDomain}`;
      }

      const res = await this.$services.rdpUserManagerUpdateSubAccount({
        data
      });

      if (res.success) {
        this.$Message.success(this.$t('xiu-gai-zhang-hao-cheng-gong'));
        await this.getSubAccountList();
        this.handleCloseModal();
      }
      this.loading = false;
    },
    async handleChangeUserRole() {
      this.loading = true;
      const { parentId, uid, roleId } = this.newAccountForm;
      const data = {
        parentId,
        subAccountUid: uid,
        roleId
      };

      const res = await this.$services.rdpUserManagerUpdateUserRole({
        data
      });

      if (res.success) {
        this.$Message.success(this.$t('geng-xin-jiao-se-cheng-gong'));
        await this.getSubAccountList();
        this.handleCloseModal();
      }
      this.loading = false;
    },
    generateRandomPwd() {
      const regex = new RegExp(this.passwordRule.expr);
      const randexp = new RandExp(regex);
      const password = randexp.gen();
      if (regex.test(password)) {
        this.newAccountForm.password = password;
        this.newPassword = password;
        if (this.$refs.newAccountFormRef && this.showAddNewSubAccountModal) {
          this.$refs.newAccountFormRef.validateField('password');
        }
      } else {
        this.generateRandomPwd();
      }
    },
    async handleAddSubAccount() {
      this.loading = true;
      this.$refs.newAccountFormRef.validate(async (valid) => {
        if (valid) {
          const res = await this.$services.rdpUserManagerAddSubAccount({
            data: {
              ...this.newAccountForm,
              subAccount: `${this.newAccountForm.subAccount}@${this.userInfo && this.userInfo.userDomain ? this.userInfo.userDomain : ''}`,
              password: this.passwordEncrypt(this.newAccountForm.password)
            }
          });

          if (res.success) {
            this.$Message.success(this.$t('chuang-jian-zi-zhang-hao-cheng-gong'));
            await this.getSubAccountList();
            this.handleCloseModal();
          }
        }
        this.loading = false;
      });
    },
    goAuthPage(type, record) {
      this.$router.push({
        path: `/system/sub_account/authdm/${record.uid}`,
        query: {
          name: record.subAccount,
          type: type
        }
      });
    },
    async handleDeleteSubAccount(subAccount) {
      const res = await this.$services.rdpUserManagerDeleteSubAccount({
        data: { subAccount: subAccount.subAccount },
        msg: this.$t('shan-chu-zi-zhang-hao-cheng-gong')
      });

      if (res.success) {
        await this.getSubAccountList();
      }
    },
    handleShowModifyAccount(row) {
      this.originAccount = {
        userName: row.username || '',
        accountName: row.subAccount ? row.subAccount.split('@')[0] : ''
      };
      this.newAccountForm = {
        ...deepClone(row),
        userName: row.username || '',
        accountName: row.subAccount ? row.subAccount.split('@')[0] : ''
      };
      this.showModifySubAccount = true;
    },
    handleShowEditPasswordModal(row) {
      this.newAccountForm = deepClone(row);
      this.showEditPasswordModal = true;
    },
    async handleClickChangeRoleModal(row) {
      await this.getRoleList();
      this.newAccountForm = deepClone(row);
      this.showChangeRoleModal = true;
    },
    handleCloseModal() {
      this.newAccountForm = deepClone(EMPTY_SUB_ACCOUNT);
      this.showAddNewSubAccountModal = false;
      this.showChangeRoleModal = false;
      this.showModifySubAccount = false;
      this.showEditPasswordModal = false;
      this.opUserPassword = '';
      this.newPassword = '';
      this.editPasswordErrMsg = '';
      this.showEnableResourceModal = false;
      this.showDisableResourceModal = false;
    },
    async handleConfirmEditPassword() {
      this.loading = true;
      const regExp = new RegExp(this.passwordRule.expr);
      if (regExp.test(this.newPassword)) {
        const res = await this.$services.rdpUserResetSubAccountPwd({
          data: {
            operatorPwd: this.passwordEncrypt(this.opUserPassword),
            newPassword: this.passwordEncrypt(this.newPassword),
            subAccountUid: this.newAccountForm.uid
          },
          modal: false
        });
        if (res.success) {
          this.$Message.success(this.$t('zi-zhang-hao-mi-ma-chong-zhi-cheng-gong'));
          this.handleCloseModal();
        } else {
          this.editPasswordErrMsg = res.msg;
        }
      } else {
        console.log('reset error');
        this.editPasswordErrMsg = this.passwordRule.tips;
      }
      this.loading = false;
    },
    handlePageChange(pageNum) {
      console.log(pageNum);
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
      this.showSubAccountList = this.subAccountList.slice((pageNum - 1) * pageSize, pageNum * pageSize);
    },
    async getSubAccountList(searchType) {
      this.subAccountListLoading = true;
      const res = await this.$services.rdpUserManagerListSubAccounts({
        data: {
          ...this.search,
          roleId: this.search.roleId || 0
        }
      });
      this.subAccountListLoading = false;
      if (res.success) {
        this.subAccountList = generateData(res.data);
        this.total = this.subAccountList.length;
        this.setTableShowData(searchType);
      }
    },
    async getRoleList() {
      const roleListRes = await this.$services.rdpUserListRules();
      if (roleListRes.success) {
        const roleList = [];
        roleListRes.data.forEach((role) => {
          roleList.push({
            innerTag: role.innerTag,
            name: role.aliasName,
            value: role.roleId
          });
        });
        this.roleList = roleList;
      }
    },
    async handleClickAddBtn() {
      const res = await this.$services.rdpUserManagerCtrlAddSubAccount();
      if (!res.success) {
        return;
      }
      await this.getRoleList();
      this.showAddNewSubAccountModal = true;
    },
    async handleTriggerStopAccount(row) {
      const { disable, uid } = row;
      const data = {
        disable: !disable,
        uid
      };
      const res = await this.$services.rdpUserManagerStopSubAccount({
        data,
        msg: disable ? this.$t('qi-yong-cheng-gong') : this.$t('ting-yong-cheng-gong')
      });
      if (res.success) {
        await this.getSubAccountList();
        // Vue.set(this.accountList[index], 'disable', !disable);
      }
    }
  },
  async created() {
    await this.getRoleList();
    await this.getSubAccountList();
    this.$services.rdpUserGetSubAccountPwdValidateExpr().then((res) => {
      if (res.success) {
        this.passwordRule.expr = res.data.expr;
        this.passwordRule.tips = res.data.tips;
        const validateSubAccount = async (checkType, checkContent, callback, msg) => {
          const res2 = await this.$services.rdpUserManagerCheckSubAccountDuplicate({
            data: {
              checkType,
              checkContent
            },
            modal: false
          });
          if (res2.success) {
            callback();
          } else {
            callback(new Error(this.$t('msg-zhong-fu', [msg])));
          }
        };
        const validateSubAccountName = (rule, value, callback) => {
          validateSubAccount('SUB_ACCOUNT', value, callback, this.$t('zi-zhang-hao'));
        };
        const validateSubAccountPhone = (rule, value, callback) => {
          validateSubAccount('PHONE', value, callback, this.$t('shou-ji-hao'));
        };
        const validateSubAccountEmail = (rule, value, callback) => {
          validateSubAccount('EMAIL', value, callback, this.$t('you-xiang'));
        };
        this.newAccountRules = {
          userName: [
            {
              required: true,
              trigger: 'blur',
              message: this.$t('xing-ming-bu-neng-wei-kong')
            }
          ],
          subAccount: [
            {
              required: true,
              trigger: 'blur',
              message: this.$t('zi-zhang-hao-bu-neng-wei-kong')
            },
            {
              validator: validateSubAccountName,
              trigger: 'blur'
            }
          ],
          password: [
            {
              required: true,
              trigger: 'blur',
              message: this.$t('mi-ma-bu-neng-wei-kong')
            },
            {
              pattern: new RegExp(this.passwordRule.expr),
              message: this.passwordRule.tips
            }
          ],
          roleId: [
            {
              required: true,
              trigger: 'blur',
              message: this.$t('jiao-se-bu-neng-wei-kong'),
              transform: (value) => String(value)
            }
          ],
          phone: [
            {
              required: true,
              trigger: 'blur',
              message: this.$t('shou-ji-hao-bu-neng-wei-kong')
            },
            {
              validator: validateSubAccountPhone,
              trigger: 'blur'
            }
          ],
          email: [
            {
              required: true,
              trigger: 'blur',
              message: this.$t('you-xiang-bu-neng-wei-kong')
            },
            {
              validator: validateSubAccountEmail,
              trigger: 'blur'
            }
          ]
        };
      }
    });
  }
};
</script>

<style lang="less" scoped>
.sub-account {
  display: flex;
  flex-direction: column;
  height: 100%;

  .uid {
    display: flex;
    cursor: pointer;

    .copy {
      display: none;
    }

    &:hover {
      .copy {
        display: block;
      }
    }
  }

  .copy-account {
    display: flex;
    align-items: center;
    cursor: pointer;

    .square {
      width: 15px;
      height: 12px;
    }

    i {
      display: none;
    }

    &:hover {
      i {
        display: block;
      }

      .square {
        display: none;
      }
    }
  }

  .action {
    //button {
    //  margin-right: 12px;
    //}
    //.ivu-dropdown {
    //  padding: 0 7px;
    //}
    a {
      margin-right: 16px;
    }
  }

  .actions {
    font-size: 12px;
  }
}

.manage-role-modal {
  display: flex;

  .left {
    .title {
      margin-bottom: 10px;

      span {
        color: #888;

        &:first-child {
          color: rgba(0, 0, 0, 0.88);
          font-weight: bold;
          margin-right: 10px;
        }
      }
    }

    .role-table {
      display: flex;
      flex-direction: column;
      height: 400px;
      border: 1px solid rgba(234, 234, 234, 1);
    }
  }

  .new-role {
    flex: 1;
    padding: 20px;
  }
}

.new-subaccount-modal {
  .ivu-input-wrapper {
    width: 420px;
  }

  .title {
    font-weight: bold;
    font-size: 14px;
    margin-bottom: 18px;
  }
}

.rule-default-tag {
  display: flex;
  align-items: center;
}

.role-name-container {
  display: flex;
  align-items: center;

  .role-name-text {
    flex: 1;
    max-width: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.unuse-mfa,
.use-mfa {
  height: 20px !important;
  line-height: 20px !important;
  padding: 0 4px !important;
  white-space: nowrap;
}

.unuse-mfa {
  :deep(.ivu-tag-text) {
    color: #ccc !important;
  }
}
</style>

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
              <template #accountStatus="{ row }">
                <Tooltip :content="accountStatus(row).label" transfer placement="top">
                  <span class="account-status-dot" :class="`is-${accountStatus(row).type}`"></span>
                </Tooltip>
              </template>
              <template #username="{ row }">
                <span v-if="row.username">{{ row.username }}</span>
                <span v-else class="empty-text">{{ emptyText() }}</span>
              </template>
              <template #account="{ row }">
                <div class="account-display">
                  <span v-if="displayAccount(row)" class="account-display-text">{{ displayAccount(row) }}</span>
                  <span v-else class="empty-text">{{ emptyText() }}</span>
                  <Tooltip v-if="showProviderIcon(row)" :content="row.bindType" transfer placement="top">
                    <CustomIcon
                      v-if="providerIconResource(row.bindType)"
                      :resource="providerIconResource(row.bindType)"
                      :alt="providerName(row.bindType)"
                      size="18px"
                      leftMargin="6px"
                      class="provider-icon"
                    />
                    <span v-else class="provider-fallback">{{ row.bindType }}</span>
                  </Tooltip>
                </div>
              </template>
              <template #phone="{ row }">
                <div v-if="row.phone" class="text">{{ row.phone }}</div>
                <div v-else class="empty-text">{{ emptyText() }}</div>
              </template>
              <template #mail="{ row }">
                <div v-if="row.email" class="text">{{ row.email }}</div>
                <div v-else class="empty-text">{{ emptyText() }}</div>
              </template>
              <template #roleName="{ row }">
                <div class="role-name-container">
                  <div class="role-name-text">
                    {{ row.roleName }}
                  </div>
                </div>
              </template>
              <template #action="{ row }">
                <div class="action">
                  <a
                    v-if="myAuth.includes('RDP_AUTH_READ')"
                    :style="{ color: row.disable ? '#ccc' : '' }"
                    @click="!row.disable && goAuthPage('edit', row)"
                    type="primary"
                  >
                    {{ $t('shou-quan') }}
                  </a>
                  <a v-if="myAuth.includes('RDP_USER_MANAGE')" @click="handleShowModifyAccount(row)">
                    {{ $t('xiu-gai') }}
                  </a>
                  <a v-if="myAuth.includes('RDP_USER_MANAGE')" @click="handleShowDeleteConfirm(row)">
                    {{ $t('shan-chu') }}
                  </a>
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
    <CCModal v-model="accountFormVisible" :mask-closable="false" :width="680" :title="accountFormTitle" @on-cancel="handleCloseModal">
      <Form
        ref="newAccountFormRef"
        class="account-form"
        :model="newAccountForm"
        :rules="newAccountRules"
        v-if="accountFormVisible"
        :label-width="100"
      >
        <div class="form-section-title">{{ $t('ping-zheng') }}</div>
        <div class="credential-account-row">
          <FormItem
            :label="isExternalAccount ? $t('bang-ding-zhang-hao') : $t('deng-lu-zhang-hao')"
            :prop="isExternalAccount ? '' : 'account'"
            :required="isExternalAccount"
            class="credential-account-field"
          >
            <div class="form-field-with-success">
              <Input
                v-if="!isExternalAccount"
                v-model="newAccountForm.account"
                @input="clearDuplicateCheck('account')"
                @on-blur="handleDuplicateBlur('account')"
              />
              <Input v-else v-model="newAccountForm.bindAccount" :placeholder="noneText()" disabled />
              <span class="field-success" v-if="!isExternalAccount && duplicateValid.account"></span>
            </div>
          </FormItem>
          <FormItem :label="$t('jiao-se')" prop="roleId" class="credential-role-field" :label-width="52">
            <Select v-model="newAccountForm.roleId" :placeholder="$t('qing-xuan-ze')">
              <Option v-for="role in roleList" :key="role.value" :value="role.value" :label="role.name">
                <div class="role-name-container">
                  <div class="role-name-text">
                    {{ role.name }}
                  </div>
                </div>
              </Option>
            </Select>
          </FormItem>
        </div>
        <div v-if="accountFormMode === 'edit'" class="credential-source-row">
          <FormItem :label="$t('lai-yuan')" class="credential-source-field">
            <div class="provider-info">
              <CustomIcon
                v-if="isExternalAccount && providerIconResource(newAccountForm.bindType)"
                :resource="providerIconResource(newAccountForm.bindType)"
                :alt="providerName(newAccountForm.bindType)"
                size="18px"
                class="provider-icon"
              />
              <span>{{ isExternalAccount ? providerName(newAccountForm.bindType) : $t('ben-di-zhang-hao') }}</span>
            </div>
          </FormItem>
          <FormItem :label="$t('yun-xu-shi-yong-ben-di-zhang-hao-deng-lu')" class="credential-allow-local-field" :label-width="190">
            <i-switch v-model="newAccountForm.allowLocal" :disabled="!isExternalAccount" true-color="#52C41A" />
          </FormItem>
        </div>
        <FormItem v-if="isExternalAccount && showLocalCredential" :label="$t('ben-di-zhang-hao')" prop="account">
          <div class="form-field-with-success">
            <Input v-model="newAccountForm.account" @input="clearDuplicateCheck('account')" @on-blur="handleDuplicateBlur('account')" />
            <span class="field-success" v-if="duplicateValid.account"></span>
          </div>
        </FormItem>
        <FormItem v-if="showLocalCredential" :label="$t('deng-lu-mi-ma')" prop="password">
          <Poptip trigger="focus" placement="right-start" transfer style="width: 100%">
            <div style="display: flex; width: 100%">
              <Input v-model="newAccountForm.password" type="password" password :placeholder="passwordRule.tips" />
              <Button style="margin-left: 10px" @click="generateRandomPwd" type="primary" ghost>
                {{ $t('zi-dong-sheng-cheng') }}
              </Button>
            </div>
            <template #content>
              <p>{{ passwordRule.tips }}</p>
            </template>
          </Poptip>
        </FormItem>
        <FormItem v-if="showLocalCredential" :label="$t('que-ren-mi-ma')" prop="confirmPassword">
          <Input v-model="newAccountForm.confirmPassword" type="password" password />
        </FormItem>

        <div class="form-section-title">{{ $t('yong-hu-xin-xi') }}</div>
        <FormItem :label="$t('yong-hu-ming')" prop="userName">
          <Input v-model="newAccountForm.userName" />
        </FormItem>
        <FormItem :label="$t('shou-ji')" prop="phone">
          <div class="form-field-with-success">
            <Input v-model="newAccountForm.phone" @input="clearDuplicateCheck('phone')" @on-blur="handleDuplicateBlur('phone')" />
            <span class="field-success" v-if="duplicateValid.phone"></span>
          </div>
        </FormItem>
        <FormItem :label="$t('you-xiang')" prop="email">
          <div class="form-field-with-success">
            <Input v-model="newAccountForm.email" @input="clearDuplicateCheck('email')" @on-blur="handleDuplicateBlur('email')" />
            <span class="field-success" v-if="duplicateValid.email"></span>
          </div>
        </FormItem>

        <div class="form-section-title">{{ $t('zhuang-tai') }}</div>
        <FormItem :label="$t('qi-yong-jin-yong')">
          <i-switch v-model="newAccountForm.enabled" true-color="#52C41A" />
        </FormItem>
        <FormItem v-if="accountFormMode === 'edit'" :label="$t('suo-ding-deng-lu')">
          <span class="lock-state-text">{{ newAccountForm.loginLocked ? $t('yi-suo-ding') : $t('wei-suo-ding') }}</span>
        </FormItem>
        <FormItem v-if="accountFormMode === 'edit'" :label="$t('shang-ci-deng-lu-shi-jian')">
          <span class="readonly-status-text">{{ readonlyText(newAccountForm.lastLoginTime) }}</span>
        </FormItem>
        <FormItem v-if="accountFormMode === 'edit'" :label="$t('duo-yin-zi-ren-zheng')">
          <span class="readonly-status-text">{{ newAccountForm.useMfa ? $t('yi-qi-yong') : $t('wei-qi-yong') }}</span>
        </FormItem>
        <FormItem v-if="accountFormMode === 'edit' && newAccountForm.loginLocked" :label="$t('suo-ding-shi-jian')">
          <div class="lock-time-row">
            <span class="readonly-status-text">{{ readonlyText(newAccountForm.lockedAt) }}</span>
            <Button style="margin-left: 10px" type="primary" ghost @click="unlockAccount">{{ $t('jie-suo') }}</Button>
          </div>
        </FormItem>
      </Form>
      <template #footer>
        <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="loading" @click="handleSubmitAccountForm">
          {{ accountFormMode === 'create' ? $t('chuang-jian') : $t('que-ding') }}
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
import deepClone from 'lodash.clonedeep';

const PASSWORD_PLACEHOLDER = '******';
const DEFAULT_PASSWORD_MIN_LENGTH = 8;
const PASSWORD_UPPER = 'ABCDEFGHJKLMNPQRSTUVWXYZ';
const PASSWORD_LOWER = 'abcdefghijkmnopqrstuvwxyz';
const PASSWORD_DIGIT = '23456789';
const PASSWORD_ALL = `${PASSWORD_UPPER}${PASSWORD_LOWER}${PASSWORD_DIGIT}`;

const EMPTY_SUB_ACCOUNT = {
  userName: '',
  account: '',
  password: '',
  confirmPassword: '',
  roleId: undefined,
  phone: '',
  email: '',
  enabled: true,
  loginLocked: false,
  lockedAt: '',
  lastLoginTime: '',
  useMfa: false,
  bindType: 'INTERNAL',
  bindAccount: '',
  allowLocal: true
};

// 数据和方法带2的都是数据库管理权限相关
export default {
  name: 'SubAccount',
  mixins: [copyMixin, enterOpPwdMixin, encryptMixin],
  computed: {
    ...mapState(['userInfo', 'globalSetting', 'myCatLog', 'myAuth']),
    ...mapGetters(['includesDM']),
    accountFormTitle() {
      return this.accountFormMode === 'create' ? this.$t('chuang-jian-zi-zhang-hao') : this.$t('xiu-gai');
    },
    isExternalAccount() {
      return this.accountFormMode === 'edit' && this.newAccountForm.bindType && this.newAccountForm.bindType !== 'INTERNAL';
    },
    showLocalCredential() {
      return !this.isExternalAccount || this.newAccountForm.allowLocal;
    }
  },
  data() {
    return {
      showAddBtn: false,
      originAccount: {},
      newAccountForm: {
        userName: '',
        ...deepClone(EMPTY_SUB_ACCOUNT)
      },
      newAccountRules: {},
      search: {
        roleId: '',
        userNameOrSubAccountPrefix: ''
      },
      subAccountColumns: [
        {
          title: this.$t('zhuang-tai'),
          slot: 'accountStatus',
          width: 70
        },
        {
          title: this.$t('yong-hu-ming'),
          slot: 'username',
          width: 150
        },
        {
          title: this.$t('zhang-hao'),
          slot: 'account',
          width: 260
        },
        {
          title: this.$t('shou-ji'),
          slot: 'phone',
          width: 150
        },
        {
          title: this.$t('you-xiang'),
          slot: 'mail',
          width: 260
        },
        {
          title: this.$t('jiao-se'),
          slot: 'roleName',
          minWidth: 120
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action',
          fixed: 'right',
          width: 190
        }
      ],
      subAccountListLoading: false,
      accountFormVisible: false,
      accountFormMode: 'create',
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
      passwordRule: {
        strongPolicy: false,
        minLength: DEFAULT_PASSWORD_MIN_LENGTH,
        tips: ''
      },
      duplicateValid: {
        account: false,
        phone: false,
        email: false
      },
      loading: false
    };
  },
  methods: {
    accountStatus(row) {
      if (row.disable) {
        return { label: this.$t('jin-yong'), type: 'disabled' };
      }
      if (row.loginLocked) {
        return { label: this.$t('yi-suo-ding'), type: 'locked' };
      }
      return { label: this.$t('zheng-chang'), type: 'normal' };
    },
    displayAccount(row) {
      if (this.showProviderIcon(row)) {
        return row.bindAccount || row.account || '';
      }
      return row.account || '';
    },
    emptyText() {
      return '(空)';
    },
    noneText() {
      return '(无)';
    },
    readonlyText(value) {
      return value || this.noneText();
    },
    providerIconResource(bindType) {
      const providerMap = {
        AD: 'AD',
        DINGTALK: 'DingTalk',
        FEISHU: 'Feishu',
        GITEE: 'Gitee',
        GITHUB: 'Github',
        LDAP: 'LDAP',
        OIDC: 'OIDC',
        WECHAT: 'Wechat'
      };
      const providerName = providerMap[String(bindType || '').toUpperCase()];
      return providerName ? `webside/${providerName}@login-icon` : '';
    },
    providerName(bindType) {
      return bindType || this.emptyText();
    },
    showProviderIcon(row) {
      return row.bindType && row.bindType !== 'INTERNAL';
    },
    handleEnterSearch(e) {
      if (e.code === 'Enter') {
        e.preventDefault();
        this.getSubAccountList();
      }
    },
    clearDuplicateCheck(field) {
      if (this.duplicateValid[field] !== undefined) {
        this.duplicateValid[field] = false;
      }
    },
    resetDuplicateValid() {
      this.duplicateValid = {
        account: false,
        phone: false,
        email: false
      };
    },
    handleDuplicateBlur(field) {
      if (this.$refs.newAccountFormRef) {
        this.$refs.newAccountFormRef.validateField(field);
      }
    },
    unlockAccount() {
      this.newAccountForm.loginLocked = false;
      this.newAccountForm.lockedAt = '';
    },
    buildAccountPayload() {
      return {
        userName: this.newAccountForm.userName,
        account: this.newAccountForm.account,
        password: this.isExternalAccount ? '' : this.passwordEncrypt(this.newAccountForm.password),
        roleId: this.newAccountForm.roleId,
        phone: this.newAccountForm.phone,
        email: this.newAccountForm.email,
        disable: !this.newAccountForm.enabled,
        loginLocked: this.newAccountForm.loginLocked,
        allowLocal: true
      };
    },
    buildUpdateAccountPayload() {
      const payload = {
        targetUid: this.newAccountForm.uid
      };
      const changed = (field, value) => {
        if (this.originAccount[field] !== value) {
          payload[field] = value;
        }
      };
      changed('userName', this.newAccountForm.userName);
      changed('roleId', this.newAccountForm.roleId);
      changed('phone', this.newAccountForm.phone);
      changed('email', this.newAccountForm.email);
      if (this.originAccount.disable !== !this.newAccountForm.enabled) {
        payload.disable = !this.newAccountForm.enabled;
      }
      if (this.originAccount.loginLocked !== this.newAccountForm.loginLocked) {
        payload.loginLocked = this.newAccountForm.loginLocked;
      }
      if (this.isExternalAccount && this.originAccount.allowLocal !== this.newAccountForm.allowLocal) {
        payload.allowLocal = this.newAccountForm.allowLocal;
      }
      if (this.showLocalCredential && this.originAccount.account !== this.newAccountForm.account) {
        payload.account = this.newAccountForm.account;
      }
      if (this.showLocalCredential && this.newAccountForm.password !== PASSWORD_PLACEHOLDER) {
        payload.password = this.passwordEncrypt(this.newAccountForm.password);
      }
      return payload;
    },
    async handleSubmitAccountForm() {
      this.loading = true;
      this.$refs.newAccountFormRef.validate(async (valid) => {
        if (valid) {
          const payload = this.buildAccountPayload();
          const request =
            this.accountFormMode === 'create'
              ? this.$services.rdpUserManagerAddSubAccount({
                  data: payload
                })
              : this.$services.rdpUserManagerUpdateSubAccount({
                  data: this.buildUpdateAccountPayload()
                });
          const res = await request;
          if (res.success) {
            this.$Message.success(
              this.accountFormMode === 'create' ? this.$t('chuang-jian-zi-zhang-hao-cheng-gong') : this.$t('xiu-gai-zhang-hao-cheng-gong')
            );
            await this.getSubAccountList();
            this.handleCloseModal();
          }
        }
        this.loading = false;
      });
    },
    generateRandomPwd() {
      const length = Math.max(this.passwordRule.minLength || DEFAULT_PASSWORD_MIN_LENGTH, this.passwordRule.strongPolicy ? 3 : 1);
      const chars = [];
      if (this.passwordRule.strongPolicy) {
        chars.push(this.randomPasswordChar(PASSWORD_UPPER), this.randomPasswordChar(PASSWORD_LOWER), this.randomPasswordChar(PASSWORD_DIGIT));
      }
      while (chars.length < length) {
        chars.push(this.randomPasswordChar(PASSWORD_ALL));
      }
      const password = chars.sort(() => Math.random() - 0.5).join('');
      this.newAccountForm.password = password;
      this.newAccountForm.confirmPassword = password;
      if (this.$refs.newAccountFormRef && this.accountFormVisible) {
        this.$refs.newAccountFormRef.validateField('password');
        this.$refs.newAccountFormRef.validateField('confirmPassword');
      }
    },
    randomPasswordChar(chars) {
      return chars[Math.floor(Math.random() * chars.length)];
    },
    isPasswordValid(value) {
      if (!value || value.length < (this.passwordRule.minLength || DEFAULT_PASSWORD_MIN_LENGTH)) {
        return false;
      }
      if (!this.passwordRule.strongPolicy) {
        return true;
      }
      return /[A-Z]/.test(value) && /[a-z]/.test(value) && /\d/.test(value);
    },
    goAuthPage(type, record) {
      this.$router.push({
        path: `/system/account/authdm/${record.uid}`,
        query: {
          name: this.displayAccount(record),
          type: type
        }
      });
    },
    handleShowDeleteConfirm(subAccount) {
      this.$Modal.confirm({
        title: this.$t('que-ding-shan-chu-gai-zi-zhang-hao-ma'),
        okText: this.$t('que-ding'),
        cancelText: this.$t('qu-xiao'),
        onOk: () => {
          this.handleDeleteSubAccount(subAccount);
        }
      });
    },
    async handleDeleteSubAccount(subAccount) {
      const res = await this.$services.rdpUserManagerDeleteSubAccount({
        data: { account: subAccount.account },
        msg: this.$t('shan-chu-zi-zhang-hao-cheng-gong')
      });

      if (res.success) {
        await this.getSubAccountList();
      }
    },
    handleShowModifyAccount(row) {
      this.accountFormMode = 'edit';
      this.originAccount = deepClone(row);
      this.newAccountForm = {
        ...deepClone(EMPTY_SUB_ACCOUNT),
        uid: row.uid,
        userName: row.username || '',
        account: row.account || '',
        password: row.bindType === 'INTERNAL' || row.allowLocal ? PASSWORD_PLACEHOLDER : '',
        confirmPassword: row.bindType === 'INTERNAL' || row.allowLocal ? PASSWORD_PLACEHOLDER : '',
        roleId: row.roleId,
        phone: row.phone || '',
        email: row.email || '',
        enabled: !row.disable,
        loginLocked: !!row.loginLocked,
        lockedAt: row.loginLocked ? row.lastTryLoginTime : '',
        lastLoginTime: row.lastTryLoginTime || '',
        useMfa: !!row.useMfa,
        bindType: row.bindType || 'INTERNAL',
        bindAccount: row.bindAccount || row.account || '',
        allowLocal: row.bindType === 'INTERNAL' || !!row.allowLocal
      };
      this.originAccount = {
        ...this.originAccount,
        userName: row.username || '',
        roleId: row.roleId,
        phone: row.phone || '',
        email: row.email || '',
        account: row.account || '',
        allowLocal: row.bindType === 'INTERNAL' || !!row.allowLocal,
        disable: !!row.disable,
        loginLocked: !!row.loginLocked
      };
      this.resetDuplicateValid();
      this.accountFormVisible = true;
    },
    handleCloseModal() {
      this.newAccountForm = deepClone(EMPTY_SUB_ACCOUNT);
      this.originAccount = {};
      this.accountFormMode = 'create';
      this.resetDuplicateValid();
      this.accountFormVisible = false;
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
      this.accountFormMode = 'create';
      this.originAccount = {};
      this.newAccountForm = deepClone(EMPTY_SUB_ACCOUNT);
      this.resetDuplicateValid();
      this.accountFormVisible = true;
    }
  },
  async created() {
    await this.getRoleList();
    await this.getSubAccountList();
    this.$services.rdpUserGetSubAccountPwdPolicy().then((res) => {
      if (res.success) {
        this.passwordRule.strongPolicy = !!res.data.strongPolicy;
        this.passwordRule.minLength = res.data.minLength || DEFAULT_PASSWORD_MIN_LENGTH;
        this.passwordRule.tips = res.data.tips;
        const validateSubAccount = async (checkType, checkContent, callback, msg) => {
          const fieldMap = {
            SUB_ACCOUNT: 'account',
            PHONE: 'phone',
            EMAIL: 'email'
          };
          const field = fieldMap[checkType];
          this.clearDuplicateCheck(field);
          if (!checkContent) {
            callback();
            return;
          }
          const res2 = await this.$services.rdpUserManagerCheckSubAccountDuplicate({
            data: {
              checkType,
              checkContent,
              targetUid: this.accountFormMode === 'edit' ? this.newAccountForm.uid : ''
            },
            modal: false
          });
          if (res2.success) {
            if (field) {
              this.duplicateValid[field] = true;
            }
            callback();
          } else {
            callback(new Error(this.$t('msg-zhong-fu', [msg])));
          }
        };
        const validateSubAccountName = (rule, value, callback) => {
          validateSubAccount('SUB_ACCOUNT', value, callback, this.$t('zi-zhang-hao'));
        };
        const validateSubAccountPhone = (rule, value, callback) => {
          validateSubAccount('PHONE', value, callback, this.$t('shou-ji'));
        };
        const validateSubAccountEmail = (rule, value, callback) => {
          validateSubAccount('EMAIL', value, callback, this.$t('you-xiang'));
        };
        const validateConfirmPassword = (rule, value, callback) => {
          if (this.accountFormMode === 'edit' && this.newAccountForm.password === PASSWORD_PLACEHOLDER) {
            callback();
            return;
          }
          if (!value) {
            callback(new Error(this.$t('que-ren-mi-ma-bu-neng-wei-kong')));
          } else if (value !== this.newAccountForm.password) {
            callback(new Error(this.$t('liang-ci-shu-ru-de-mi-ma-bu-yi-zhi')));
          } else {
            callback();
          }
        };
        const validatePassword = (rule, value, callback) => {
          if (this.accountFormMode === 'edit' && value === PASSWORD_PLACEHOLDER) {
            callback();
            return;
          }
          if (!value) {
            callback(new Error(this.$t('mi-ma-bu-neng-wei-kong')));
          } else if (!this.isPasswordValid(value)) {
            callback(new Error(this.passwordRule.tips));
          } else {
            callback();
          }
        };
        this.newAccountRules = {
          account: [
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
              validator: validatePassword,
              trigger: 'blur'
            }
          ],
          confirmPassword: [
            {
              validator: validateConfirmPassword,
              trigger: 'blur'
            }
          ],
          roleId: [
            {
              required: true,
              trigger: 'change',
              message: this.$t('jiao-se-bu-neng-wei-kong'),
              transform: (value) => String(value)
            }
          ],
          phone: [
            {
              validator: validateSubAccountPhone,
              trigger: 'blur'
            }
          ],
          email: [
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

  .account-display {
    display: flex;
    align-items: center;
    min-width: 0;
  }

  .account-display-text {
    display: inline-block;
    max-width: 190px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .empty-text {
    color: #a8a8a8;
  }

  .account-status-dot {
    display: inline-block;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    vertical-align: middle;

    &.is-normal {
      background: #19be6b;
    }

    &.is-locked {
      background: #ff9900;
    }

    &.is-disabled {
      background: #ed4014;
    }
  }

  .provider-icon {
    flex: 0 0 auto;
  }

  .provider-info {
    display: flex;
    align-items: center;
    gap: 6px;
    min-height: 32px;
  }

  .provider-fallback {
    display: inline-flex;
    align-items: center;
    margin-left: 6px;
    color: #a8a8a8;
    font-size: 12px;
    line-height: 18px;
  }

  .action {
    white-space: nowrap;

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

.account-form {
  :deep(.ivu-form-item) {
    margin-bottom: 12px;
  }
}

.form-section-title {
  margin: 9px 0 6px;
  padding-left: 8px;
  border-left: 3px solid #2d8cf0;
  color: #17233d;
  font-weight: 600;
  line-height: 16px;

  &:first-child {
    margin-top: 0;
  }
}

.credential-account-row {
  display: flex;
  gap: 12px;
  width: 100%;
}

.credential-account-field {
  flex: 1 1 0;
}

.credential-role-field {
  flex: 0 0 25%;
}

.credential-source-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  width: 100%;
}

.credential-source-field {
  flex: 1 1 0;
}

.credential-allow-local-field {
  flex: 0 0 44%;
}

.form-field-with-success {
  position: relative;
  width: 100%;

  :deep(.ivu-input) {
    padding-right: 30px;
  }
}

.field-success {
  position: absolute;
  top: 50%;
  right: 10px;
  width: 18px;
  height: 18px;
  transform: translateY(-50%);
  pointer-events: none;
}

.field-success::after {
  content: '';
  position: absolute;
  top: 2px;
  left: 6px;
  width: 6px;
  height: 11px;
  border-right: 2px solid #22c55e;
  border-bottom: 2px solid #22c55e;
  transform: rotate(45deg);
}

.lock-state-text,
.readonly-status-text {
  color: #808695;
}

.lock-time-row {
  display: flex;
  align-items: center;
  width: 100%;
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

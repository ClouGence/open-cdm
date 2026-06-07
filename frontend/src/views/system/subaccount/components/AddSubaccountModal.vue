<template>
  <Modal v-model="visible" :mask-closable="false" :width="646" :title="$t('chuang-jian-zi-zhang-hao')" @on-cancel="handleClose">
    <Form ref="newAccountFormRef" :model="newAccountForm" :rules="newAccountRules" :label-width="80">
      <FormItem :label="$t('xing-ming')" prop="userName">
        <Input v-model="newAccountForm.userName" />
      </FormItem>
      <FormItem :label="$t('zi-zhang-hao')" prop="account">
        <Input v-model="newAccountForm.account" />
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
              {{ role.name }}
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
      <template #footer>
        <Button @click="handleClose">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleAddSubAccount">{{ $t('chuang-jian') }}</Button>
      </template>
    </Form>
  </Modal>
</template>

<script>
import { mapState } from 'vuex';
import * as Vue from 'vue';
import { encryptMixin } from '@/mixins/encryptMixin';

const DEFAULT_PASSWORD_MIN_LENGTH = 8;
const PASSWORD_UPPER = 'ABCDEFGHJKLMNPQRSTUVWXYZ';
const PASSWORD_LOWER = 'abcdefghijkmnopqrstuvwxyz';
const PASSWORD_DIGIT = '23456789';
const PASSWORD_ALL = `${PASSWORD_UPPER}${PASSWORD_LOWER}${PASSWORD_DIGIT}`;

export default {
  name: 'AddSubAccountModal',
  mixins: [encryptMixin],
  props: {
    visible: Boolean,
    roleList: Array,
    handleClose: Function,
    getSubaccountList: Function,
    passwordRule: Object
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.userInfo
    })
  },
  data() {
    const validateSubaccount = async (checkType, checkContent, callback, msg) => {
      const res = await this.$services.rdpUserManagerCheckSubAccountDuplicate({
        data: {
          checkType,
          checkContent
        },
        modal: false
      });
      if (res.success) {
        callback();
      } else {
        callback(new Error(this.$t('msg-zhong-fu', [msg])));
      }
    };
    const validateSubaccountName = (rule, value, callback) => {
      validateSubaccount('SUB_ACCOUNT', value, callback, this.$t('zi-zhang-hao'));
    };
    const validateSubaccountPhone = (rule, value, callback) => {
      validateSubaccount('PHONE', value, callback, this.$t('shou-ji-hao'));
    };
    const validateSubaccountEmail = (rule, value, callback) => {
      validateSubaccount('EMAIL', value, callback, this.$t('you-xiang'));
    };
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error(this.$t('mi-ma-bu-neng-wei-kong')));
      } else if (!this.isPasswordValid(value)) {
        callback(new Error(this.passwordRule.tips));
      } else {
        callback();
      }
    };
    return {
      newAccountForm: {
        userName: '',
        account: '',
        password: '',
        roleId: undefined,
        phone: '',
        email: ''
      },
      newAccountRules: {
        userName: [
          {
            required: true,
            trigger: 'blur',
            message: this.$t('xing-ming-bu-neng-wei-kong')
          }
        ],
        account: [
          {
            required: true,
            trigger: 'blur',
            message: this.$t('zi-zhang-hao-bu-neng-wei-kong')
          },
          {
            validator: validateSubaccountName,
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
            validator: validatePassword,
            trigger: 'blur'
          }
        ],
        roleId: [
          {
            required: true,
            trigger: 'blur',
            message: this.$t('jiao-se-bu-neng-wei-kong')
          }
        ],
        phone: [
          {
            required: true,
            trigger: 'blur',
            message: this.$t('shou-ji-hao-bu-neng-wei-kong')
          },
          {
            validator: validateSubaccountPhone,
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
            validator: validateSubaccountEmail,
            trigger: 'blur'
          }
        ]
      }
    };
  },
  methods: {
    generateRandomPwd() {
      const length = Math.max(this.passwordRule.minLength || DEFAULT_PASSWORD_MIN_LENGTH, this.passwordRule.strongPolicy ? 3 : 1);
      const chars = [];
      if (this.passwordRule.strongPolicy) {
        chars.push(this.randomPasswordChar(PASSWORD_UPPER), this.randomPasswordChar(PASSWORD_LOWER), this.randomPasswordChar(PASSWORD_DIGIT));
      }
      while (chars.length < length) {
        chars.push(this.randomPasswordChar(PASSWORD_ALL));
      }
      this.newAccountForm.password = chars.sort(() => Math.random() - 0.5).join('');
      this.$refs.newAccountFormRef.validateField('password');
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
    async handleAddSubAccount() {
      this.$refs.newAccountFormRef.validate(async (valid) => {
        if (valid) {
          const res = await this.$services.rdpUserManagerAddSubAccount({
            data: {
              ...this.newAccountForm,
              password: this.passwordEncrypt(this.newAccountForm.password)
            }
          });

          if (res.success) {
            this.$Message.success(this.$t('chuang-jian-zi-zhang-hao-cheng-gong'));
            await this.getSubaccountList({});
            this.handleClose();
          }
        }
      });
    }
  }
};
</script>

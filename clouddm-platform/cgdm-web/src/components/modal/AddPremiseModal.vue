<template>
  <CCModal :title="$t('tian-jia-yong-hu')" :value="visible" @on-cancel="handleClose">
    <div class="home-signin-container">
      <Alert type="error" show-icon v-if="errormsg">{{ errormsg }}</Alert>
      <div class="home-signin-content">
        <Form ref="registerForm" :model="login" label-position="right" :label-width="100" :rules="ruleValidate">
          <FormItem :label="$t('shou-ji-hao-ma')" prop="phone">
            <Input style="width: 380px" type="text" v-model="login.phone" :placeholder="$t('qing-shu-ru-shou-ji-hao')" />
          </FormItem>
          <FormItem :label="$t('you-xiang')" prop="email">
            <Input style="width: 380px" type="text" v-model="login.email" :placeholder="$t('qing-shu-ru-you-xiang')" autocomplete="new-password" />
          </FormItem>
          <FormItem :label="$t('mi-ma')" prop="password">
            <Poptip trigger="focus" placement="right-start">
              <Input
                style="width: 380px"
                type="password"
                v-model="login.password"
                password
                :placeholder="$t('qing-she-zhi-mi-ma')"
                @on-change="handleCheckPassword"
                autocomplete="new-password"
              />
              <template #content>
                <ul class="password-check">
                  <li>
                    <Icon :type="pwLength ? 'ios-checkmark-circle-outline' : 'ios-close-circle-outline'" />
                    <span>{{ $t('chang-du-wei-832-ge-zi-fu') }}</span>
                  </li>
                  <li>
                    <Icon :type="pwContain ? 'ios-checkmark-circle-outline' : 'ios-close-circle-outline'" />
                    <span>{{ $t('mi-ma-bu-neng-bao-han-shou-ji-hao') }}</span>
                  </li>
                  <li>
                    <Icon :type="pwFormat ? 'ios-checkmark-circle-outline' : 'ios-close-circle-outline'" />
                    <span>{{ $t('bi-xu-bao-han-ying-wen-he-shu-zi') }}</span>
                  </li>
                </ul>
              </template>
            </Poptip>
          </FormItem>
          <FormItem :label="$t('que-ren-mi-ma')" prop="passwordAgain">
            <Poptip trigger="focus" placement="right-start">
              <Input
                style="width: 380px"
                type="password"
                v-model="login.passwordAgain"
                password
                :placeholder="$t('qing-zai-ci-shu-ru-mi-ma')"
                @on-change="handleCheckPasswordAgain"
              />
              <template #content>
                <div v-if="!pwConfirm" style="color: #ed4014; font-size: 12px; line-height: 20px">
                  {{ $t('qing-zai-ci-shu-ru-xiang-tong-de-mi-ma') }}
                </div>
                <div v-if="pwConfirm" style="color: #19be6b; font-size: 12px">
                  <Icon type="md-checkmark" style="margin-right: 5px" />
                  {{ $t('qing-zai-ci-shu-ru-xiang-tong-de-mi-ma') }}
                </div>
              </template>
            </Poptip>
          </FormItem>
          <FormItem :label="$t('xing-ming')" prop="username">
            <Input style="width: 380px" type="text" v-model="login.username" :placeholder="$t('qing-shu-ru-xing-ming')" />
          </FormItem>
          <FormItem :label="$t('jiao-se')" prop="role">
            <Select style="width: 380px" type="text" v-model="login.role" :placeholder="$t('qing-shu-ru-xing-ming')">
              <Option v-for="role in roleList" :value="role" :key="role">{{ role }}</Option>
            </Select>
          </FormItem>
        </Form>
      </div>
    </div>
    <template #footer>
      <Button type="primary" @click="handleSigninConfirm">{{ $t('bao-cun') }}</Button>
      <Button @click="handleClose">{{ $t('qu-xiao') }}</Button>
    </template>
  </CCModal>
</template>

<script>
export default {
  name: 'AddPremiseModal',
  props: {
    visible: Boolean,
    handleClose: Function
  },
  data() {
    return {
      errormsg: '',
      roleList: ['SYSTEM', 'ADMIN_READONLY'],
      login: {
        email: '',
        password: '',
        phone: '',
        username: '',
        passwordAgain: '',
        role: '',
        noModal: true
      },
      ruleValidate: {
        password: [
          {
            required: true,
            message: this.$t('mi-ma-bu-neng-wei-kong'),
            trigger: 'blur'
          },
          {
            min: 8,
            message: this.$t('mi-ma-chang-du-wei-832'),
            trigger: 'blur'
          },
          {
            max: 32,
            message: this.$t('mi-ma-chang-du-wei-832'),
            trigger: 'blur'
          },
          {
            validator: (rule, value, callback) => {
              if (value === '') {
                return callback(new Error(this.$t('qing-zai-ci-shu-ru-ni-de-mi-ma')));
              }
              if (!this.pwContain || !this.pwFormat) {
                return callback(new Error(this.$t('ge-shi-you-wu')));
              }
              return callback();
            },
            trigger: 'blur'
          }
        ],
        passwordAgain: [
          {
            required: true,
            message: this.$t('que-ren-mi-ma-bu-neng-wei-kong'),
            trigger: 'blur'
          },
          {
            validator: (rule, value, callback) => {
              if (value !== this.login.password) {
                return callback(new Error(this.$t('mi-ma-shu-ru-you-wu')));
              }
              return callback();
            },
            trigger: 'blur'
          }
        ],
        email: [
          { required: true, message: this.$t('you-xiang-bu-neng-wei-kong'), trigger: 'blur' },
          { type: 'email', message: this.$t('bu-zheng-que-de-you-xiang-ge-shi'), trigger: 'blur' }
        ],
        phone: [{ required: true, message: this.$t('shou-ji-hao-bu-neng-wei-kong'), trigger: 'blur' }],
        username: [{ required: true, message: this.$t('yong-hu-ming-bu-neng-wei-kong'), trigger: 'blur' }],
        role: [{ required: true, message: this.$t('jiao-se-bu-neng-wei-kong'), trigger: 'blur' }]
      },
      loading: false,
      pwLength: false,
      pwContain: false,
      pwFormat: false,
      pwConfirm: false
    };
  },
  methods: {
    handleCheckPassword() {
      this.pwLength = this.login.password.length >= 8 && this.login.password.length <= 32;
      this.pwContain = this.login.password.indexOf(this.login.phone) === -1;

      const pattern = /(?=.*[0-9])(?=.*[a-zA-Z])/;

      this.pwFormat = pattern.test(this.login.password);
    },
    handleCheckPasswordAgain() {
      this.pwConfirm = Boolean(this.login.password && this.login.password === this.login.passwordAgain);
    },
    handleSigninConfirm() {
      this.$refs.registerForm.validate((val) => {
        if (val) {
          this.loading = true;
          this.$services.ccUserAddUserForPremise({ data: this.login, modal: false }).then((res) => {
            this.loading = false;
            if (res.success) {
              this.$Message.success(this.$t('tian-jia-yong-hu-cheng-gong'));
              this.errormsg = '';
              this.handleClose();
            } else {
              this.errormsg = res.msg;
            }
          });
        }
        // 表示校验成功或失败
      });
    }
  }
};
</script>

<style scoped lang="less">
.home-signin-container {
  background: #ffffff;
  //width: 1000px;
  margin: 0 auto;
  position: relative;

  .ivu-form-item {
    margin-bottom: 24px;

    .ivu-form-item-content {
      overflow: visible;
      white-space: nowrap;
    }
  }

  .home-signin-hasAccount {
    position: absolute;
    right: -200px;
    top: 6px;
  }

  .home-signin-content {
    margin: 0 auto;
    width: 500px;
    position: relative;

    .welcome-title {
      font-size: 20px;
      text-align: center;
      margin-bottom: 36px;
    }

    .ivu-alert {
      position: absolute;
      top: 28px;
      width: 480px;
    }
  }
}
</style>

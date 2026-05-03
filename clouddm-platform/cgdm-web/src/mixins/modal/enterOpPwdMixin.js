import { Modal } from 'ant-design-vue';

const enterOpPwdMixin = {
  data() {
    return {
      enterOpPwdModalForm: {
        opPwd: ''
      }
    };
  },
  methods: {
    showEnterOpPwdModal() {
      const verifyOpPwd = async () => {
        const res = await this.$services.rdpUserOpPasswdVerify({
          data: {
            opPassword: this.enterOpPwdModalForm.opPwd,
            msg: this.$t('cao-zuo-mi-ma-yan-zheng-cheng-gong')
          }
        });
        if (res.success) {
          this.enterOpPwdModalForm = { opPwd: '' };
          modal.destroy();
        }
      };
      const modal = Modal.confirm({
        title: this.$t('qing-shu-ru-cao-zuo-mi-ma'),
        class: 'enter-op-pwd-modal',
        width: 414,
        content: () => (
          <div class='enter-op-pwd-modal'>
            <a-alert style='margin-bottom:20px' message={this.$t('cao-zuo-mi-ma-yi-shi-xiao-qing-chong-xin-shu-ru')} type='warning' show-icon />
            <div class='pwd'>
              <div class='label'>{this.$t('cao-zuo-mi-ma')}</div>
              <cc-password-input v-model={this.enterOpPwdModalForm.opPwd} handleEnter={verifyOpPwd} />
            </div>
            <a-button class='reset' onClick={() => this.$bus.emit('setOpPasswordModal', true)} type='link'>
              {this.$t('wang-ji-mi-ma')}
            </a-button>
            <div class='footer'>
              <a-button type='primary' onClick={verifyOpPwd}>
                {this.$t('que-ding')}
              </a-button>
              <a-button onClick={() => modal.destroy()}>{this.$t('qu-xiao')}</a-button>
            </div>
          </div>
        )
      });
    }
  }
};

export default enterOpPwdMixin;

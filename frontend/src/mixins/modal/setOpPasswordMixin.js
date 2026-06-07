import { Modal } from 'ant-design-vue';
import { encryptMixin } from '@/mixins/encryptMixin';

const EMPTY_OP_CONFIG = {
  opPassword: ''
};
const setOpPasswordMixin = {
  data() {
    return {
      opEyeVisible: false,
      opModalConfig: {
        labelCol: { span: 5 },
        wrapperCol: { span: 19 }
      },
      opForm: { ...EMPTY_OP_CONFIG },
      opModalErrMsg: {
        opPassword: ''
      },
      opErrorText: {
        opPassword: this.$t('cao-zuo-mi-ma')
      }
    };
  },
  mixins: [encryptMixin],
  methods: {
    handleBlurOpModalValidate(type) {
      if (!this.opForm[type]) {
        this.opModalErrMsg[type] = this.$t('thisoperrortexttype-bu-neng-wei-kong', [this.opErrorText[type]]);
      } else {
        this.opModalErrMsg[type] = '';
      }
    },
    handleChangeOpEye() {
      this.opEyeVisible = !this.opEyeVisible;
    },
    setOpPasswordModal(edit = false) {
      return new Promise((resolve, reject) => {
        const handleResetOpPwd = async () => {
          const res = await this.$services.rdpUserResetOpPasswd({
            data: {
              ...this.opForm,
              opPassword: this.opForm.opPassword
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('geng-xin-cao-zuo-mi-ma-cheng-gong'));
            hideModal();
            resolve();
          }
          reject();
        };
        const hideModal = () => {
          this.opForm = { ...EMPTY_OP_CONFIG };
          this.opModalErrMsg = { ...EMPTY_OP_CONFIG };
          modal.destroy();
        };
        const modal = Modal.warning({
          title: edit ? this.$t('xiu-gai-cao-zuo-mi-ma') : this.$t('qing-xian-she-zhi-nin-de-cao-zuo-mi-ma'),
          class: 'set-op-password-modal',
          width: 414,
          content: () => (
            <div style={{ marginLeft: 0 }}>
              {edit ? null : (
                <a-alert
                  type='warning'
                  show-icon
                  message={this.$t(
                    'cao-zuo-mi-ma-yong-yu-shu-ju-ku-cha-xun-bian-geng-ji-bu-fen-min-gan-gong-neng-cao-zuo-she-zhi-cao-zuo-mi-ma-hou-cai-neng-kai-shi-zuo-ye'
                  )}
                  style={{ marginBottom: '20px' }}
                />
              )}
              <a-form-model label-col={this.opModalConfig.labelCol} wrapper-col={this.opModalConfig.wrapperCol}>
                <a-form-model-item label={this.$t('cao-zuo-mi-ma')}>
                  <a-input
                    v-model={this.opForm.opPassword}
                    type={this.opEyeVisible ? 'text' : 'password'}
                    placeholder={this.$t('mi-ma')}
                    autocomplete='new-password'
                    onBlur={() => this.handleBlurOpModalValidate('opPassword')}
                    onPressEnter={handleResetOpPwd}>
                    {{
                      suffix: () => (
                        <a-icon type={this.opEyeVisible ? 'eye' : 'eye-invisible'} size='large' theme='filled' onclick={this.handleChangeOpEye} />
                      )
                    }}
                  </a-input>
                  <div className='error-msgContent'>{this.opModalErrMsg.opPassword}</div>
                </a-form-model-item>
              </a-form-model>
              <div class='footer'>
                <a-button type='primary' onClick={handleResetOpPwd}>
                  {this.$t('bao-cun')}
                </a-button>
                <a-button onClick={hideModal}>{this.$t('qu-xiao')}</a-button>
              </div>
            </div>
          )
        });
      });
    }
  }
};

export default setOpPasswordMixin;

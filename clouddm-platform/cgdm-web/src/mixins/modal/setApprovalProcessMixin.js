import { Modal } from 'ant-design-vue';

const setApprovalProcessMixin = {
  data() {
    return {
      aksk: {
        ak: '',
        sk: '',
        agentId: ''
      },
      akskModalErrMsg: {
        ak: '',
        sk: '',
        agentId: ''
      },
      modal: null
    };
  },
  methods: {
    handleBlurApprovalProcessModalValidate(type) {
      if (!this.aksk[type]) {
        this.akskModalErrMsg[type] = this.$t('type-bu-neng-wei-kong', [type]);
      } else {
        this.akskModalErrMsg[type] = '';
      }
    },
    async updateKey() {
      const data = {
        appKey: this.aksk.ak,
        secretKey: this.aksk.sk,
        approvalType: 'DINGDING',
        agentId: this.aksk.agentId
      };
      const res = await this.$services.dmTicketApproUpdateKey({ data });
      if (res.success) {
        this.$bus.emit('getTemplateList');
      }
    },
    handleUpdateKey() {
      if (!this.aksk.agentId) {
        this.akskModalErrMsg.agentId = this.$t('ak-bu-neng-wei-kong');
      }
      if (!this.aksk.ak) {
        this.akskModalErrMsg.ak = this.$t('ak-bu-neng-wei-kong');
      }
      if (!this.aksk.sk) {
        this.akskModalErrMsg.sk = this.$t('sk-bu-neng-wei-kong');
      }
      if (this.akskModalErrMsg.ak || this.akskModalErrMsg.sk || this.akskModalErrMsg.agentId) {
        return false;
      }
      this.updateKey();
      this.modal.destroy();
      this.emptyAkSkModalData();
    },
    emptyAkSkModalData() {
      this.aksk = {
        ak: '',
        sk: '',
        agentId: ''
      };
      this.akskModalErrMsg = {
        ak: '',
        sk: '',
        agentId: ''
      };
    },
    setApprovalProcessModal() {
      this.modal = Modal.info({
        title: this.$t('shen-pi-liu-cheng-fang-wen-quan-xian'),
        width: 620,
        loading: true,
        class: 'dingding-aprrove-modal',
        closable: true,
        centered: true,
        content: () => (
          <div>
            <a-alert
              type='warning'
              show-icon
              style={{ marginBottom: '20px' }}
              v-slots={{
                message: () => (
                  <p>
                    {this.$t(
                      'dui-jieali-yun-ding-ding-shen-pi-liu-xu-chuang-jian-ding-ding-qi-ye-nei-bu-ying-yong-clouddm-hui-shi-yong-gai-ying-yong-de-appkey-he-appsecret-yong-yu-fa-qi-openapi-tiao-yong-lai-chuang-jian-shen-pi-shi-li-cha-xun-shen-pi-zhuang-tai-deng-gai-qi-ye-nei-bu-ying-yong-ju-you-te-ding-fang-wen-quan-xian-yong-yu-fa-qi-shen-pi-shi-li-fa-song-tong-zhi-deng-quan-xian-ju-ti-nei-rong-qing-cha-kan'
                    )}
                    <a href='https://doc.clouddm.clougence.com/docs/en/dingtalk_setting' target='_blank'>
                      {this.$t('dui-jie-ding-ding-shen-pi')}
                    </a>
                    .
                  </p>
                )
              }}></a-alert>
            <a-form data={this.aksk} label-col={{ span: 4 }} wrapper-col={{ span: 20 }}>
              <a-form-item label='AgentId'>
                <a-input v-model={this.aksk.agentId} onBlur={() => this.handleBlurApprovalProcessModalValidate('agentId')} />
                <div className='error-msgContent'>{this.akskModalErrMsg.agentId}</div>
              </a-form-item>
              <a-form-item label='AppKey'>
                <a-input v-model={this.aksk.ak} onBlur={() => this.handleBlurApprovalProcessModalValidate('ak')} />
                <div className='error-msgContent'>{this.akskModalErrMsg.ak}</div>
              </a-form-item>
              <a-form-item label='AppSecret'>
                <a-input v-model={this.aksk.sk} onBlur={() => this.handleBlurApprovalProcessModalValidate('sk')} />
                <div className='error-msgContent'>{this.akskModalErrMsg.sk}</div>
              </a-form-item>
            </a-form>
            <div class='footer'>
              <a-button type='primary' onclick={() => this.handleUpdateKey()}>
                {this.$t('shou-quan-fang-wen')}
              </a-button>
              <a-button onClick={() => hideModal()}>{this.$t('qu-xiao')}</a-button>
            </div>
          </div>
        )
      });
      const hideModal = () => {
        this.modal.destroy();
        this.emptyAkSkModalData();
      };
    }
  }
};

export default setApprovalProcessMixin;

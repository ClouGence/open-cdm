<template>
  <div>
    <h3 style="margin-bottom: 10px">
      {{ $t('yi-xia-qing-kuang-hui-dao-zhi-xu-ke-zheng-wu-xiao') }}
    </h3>
    <div>
      <p>
        {{ $t('1-cong-wei-jin-hang-guo-xu-ke-zheng-shen-qing-he-ji-huo') }}
      </p>
      <p>
        {{ $t('2-rong-liang-chao-guo-xu-ke-zheng-xian-ding-deedu-shan-chu-duo-yu-de-ren-wu-hou-ke-zi-hang-zhong-xin-ji-huo') }}
      </p>
      <p>
        {{
          $t(
            '3-xu-ke-zheng-chao-guo-shi-chang-she-qu-ban-ke-yi-zai-guan-wang-zhong-xin-shen-qing-she-qu-ban-xu-ke-zheng-ji-huo-yan-chang-shi-yong-shi-jian'
          )
        }}
      </p>
    </div>
    <h3 style="margin: 10px 0">{{ $t('xu-ke-zheng-shen-qing-bu-zhou') }}</h3>
    <div>
      <p>
        {{ $t('di-yi-bu-she-qu-ban-qing-zhi-jie-dian-ji') }}
        <a :href="store.state.docUrlPrefix" target="_blank">
          {{ $t('shen-qing-she-qu-ban-xu-ke-zheng') }}
        </a>
        {{ $t('shang-ye-ban-qing-lian-xi-cloudcanal-gong-zuo-ren-yuan-huo-qu-xu-ke-zheng') }}
      </p>
      <div>
        {{ $t('di-er-bu-fu-zhi-xia-mian-de-shen-qing-ma-jiang-gai-shen-qing-ma-tian-ru-dui-ying-de-di-fang') }}
        <Card dis-hover class="apply-code-container">
          <a class="copy-btn" @click="handleCopyApplyCode">{{ $t('fu-zhi') }}</a>
          {{ applyCode }}
        </Card>
      </div>
      <p>
        {{
          $t(
            'di-san-bu-jiang-guan-wang-shou-quan-ye-mian-sheng-cheng-de-xu-ke-zheng-tian-xie-dao-xia-mian-shu-ru-kuang-dian-ji-ji-huo-wan-cheng-ji-huo'
          )
        }}
      </p>
      <!--      <p>-->
      <!--        第七步：如需CloudCanal工作人员更新许可证，可点击 <a @click="handleCopyAuthCode">复制</a> 获取<strong>当前授权码</strong>，将当前授权码、申请码和MAC地址提供给CloudCanal工作人员-->
      <!--      </p>-->
      <p style="margin: 10px 0 20px 0; font-weight: bold">
        {{ $t('tips-ju-ti-gui-ze-qing-cha-kan') }}
        <a :href="`${store.state.docUrlPrefix}/license/license_use`">
          {{ $t('xu-ke-zheng-gui-ze') }}
        </a>
      </p>
    </div>
    <Form label-position="right" :label-width="100">
      <FormItem :label="$t('xu-ke-zheng')">
        <Input type="textarea" :rows="4" v-model="licenseData.authCode" :placeholder="$t('qing-shu-ru-xu-ke-zheng')"></Input>
      </FormItem>
    </Form>
    <div class="modal-footer" style="margin-top: 20px">
      <Button type="primary" @click="handleActiveLicense">{{ $t('ji-huo') }}</Button>
      <Button @click="handleCancel">{{ $t('qu-xiao') }}</Button>
    </div>
  </div>
</template>

<script>
import { handleCopy } from '@/utils/clipboard';
import store from '@/store';

export default {
  props: {
    handleCancel: Function,
    applyCode: String,
    macAddress: String,
    handleActiveLicense: Function,
    licenseData: Object,
    cueAuthCode: String
  },
  data() {
    return {
      store
    };
  },
  methods: {
    handleCopyApplyCode() {
      handleCopy(this.applyCode);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    handleCopyMacAddress() {
      handleCopy(this.macAddress);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    handleCopyAuthCode() {
      handleCopy(this.cueAuthCode);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    }
  }
};
</script>
<style lang="less">
.apply-code-container {
  word-break: break-all;
  margin: 10px 0;
  position: relative;
  padding-top: 16px;
  .copy-btn {
    position: absolute;
    right: 20px;
    top: 10px;
  }
}
</style>

<template>
  <a-modal v-model="showModifyIpModal" :mask-closable="false" :width="402" :title="$t('xiu-gai-wang-luo-di-zhi')">
    <div v-if="showModifyIpModal" class="edit-ip-modal">
      <div class="address">
        <div class="title">{{ $t('nei-wang-di-zhi') }}</div>
        <div class="ip">
          <a-input v-model="selectedDatasource.privateHost" :placeholder="$t('nei-wang-ip-huo-yu-ming')" style="width: 280px" />
        </div>
      </div>
      <div class="address">
        <div class="title">{{ $t('wai-wang-di-zhi') }}</div>
        <div class="ip">
          <a-input v-model="selectedDatasource.publicHost" :placeholder="$t('wai-wang-ip-huo-yu-ming')" style="width: 280px" />
        </div>
      </div>
      <div class="address">
        <div class="title">{{ $t('mo-ren-fang-wen-di-zhi') }}</div>
        <div class="ip">
          <a-radio-group v-model="selectedDatasource.defaultHostType">
            <a-radio value="private">
              {{ $t('nei-wang') }}
            </a-radio>
            <a-radio value="public">
              {{ $t('wai-wang') }}
            </a-radio>
          </a-radio-group>
        </div>
      </div>
      <div class="footer">
        <a-button type="primary" @click="handleChangeHost">{{ $t('bao-cun') }}</a-button>
        <a-button @click="showModifyIpModal = false">{{ $t('qu-xiao') }}</a-button>
      </div>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'ModifyIpModal',
  props: {
    visible: Boolean,
    ds: Object,
    handleCloseModal: Function
  },
  data() {
    return {
      currentDs: {},
      whiteList: []
    };
  },
  methods: {
    async queryWhiteList(dataSourceId) {
      const res = await this.$services.dmAliyunRdsQueryWhiteList({ data: { dataSourceId } });
      if (res.success) {
        this.selectedDsWhitelist = res.data;
      }
    }
  },
  watch: {
    ds(newVal) {
      this.currentDs = newVal;
    }
  }
};
</script>

<style scoped></style>

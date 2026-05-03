<template>
  <div class="steps">
    <div :style="{ background: step > 1 ? '#A6E0F5' : '#DDD' }" class="one">
      {{ $t('xuan-ze-shu-ju-yuan') }}
      <div :style="{ borderLeftColor: step > 1 ? '#A6E0F5' : '#DDD' }" class="right" />
    </div>
    <div :style="{ background: step > 2 ? '#A6E0F5' : '#DDD' }" class="two">
      <div class="left" />
      {{ $t('tian-jia-bai-ming-dan') }}
      <div :style="{ borderLeftColor: step > 2 ? '#A6E0F5' : '#DDD' }" class="right" />
    </div>
    <div :style="{ background: step > 3 ? '#A6E0F5' : '#DDD' }" class="three">
      <div class="left" />
      {{ $t('tian-jia-shu-ju-ku-zhang-hao') }}
      <div :style="{ borderLeftColor: step > 3 ? '#A6E0F5' : '#DDD' }" class="right" v-if="!isDesktop" />
    </div>
    <div :style="{ background: step > 4 ? '#A6E0F5' : '#DDD' }" class="four" v-if="!isDesktop">
      <div class="left" />
      {{ $t('gong-dan-she-zhi') }}
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'Steps',
  props: {
    step: Number
  },
  computed: {
    ...mapGetters(['isDesktop'])
  }
};
</script>

<style lang="less" scoped>
.steps {
  width: 100%;
  display: flex;
  margin-bottom: 10px;
  color: rgba(0, 0, 0, 0.88);

  .one {
    z-index: 4;
  }

  .two {
    z-index: 3;
  }

  .three {
    z-index: 2;
  }

  .four {
    z-index: 1;
  }

  & > div + div {
    margin-left: 10px;
  }

  & > div {
    position: relative;
    flex: 1;
    height: 30px;
    line-height: 30px;
    text-align: center;
    background: #ddd;

    .left {
      position: absolute;
      top: 0;
      left: 0;
      width: 0;
      height: 0;
      border-color: transparent transparent transparent white;
      border-style: solid;
      border-width: 15px;
    }

    .right {
      position: absolute;
      top: 0;
      right: -30px;
      width: 0;
      height: 0;
      border-color: transparent transparent transparent #ddd;
      border-style: solid;
      border-width: 15px;
    }
  }
}
</style>

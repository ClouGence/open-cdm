<template>
  <div class="app-brand-logo" :class="{ 'app-brand-logo--compact': compact }">
    <img :src="logoClouddm" class="app-brand-logo__img" alt="CloudDM" />
  </div>
</template>

<script>
import logoClouddm from '@/assets/logo-clouddm.svg';

export default {
  name: 'AppBrandLogo',
  props: {
    compact: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      logoClouddm
    };
  }
};
</script>

<style lang="less" scoped>
.app-brand-logo {
  display: flex;
  align-items: center;
  min-width: 0;
  color: #222222;

  &__img {
    height: 22px;
    width: auto;
    max-width: 130px;
    display: block;
    object-fit: contain;
    object-position: left center;
  }

  &--compact {
    color: #222222;

    .app-brand-logo__img {
      height: 18px;
      max-width: 110px;
    }
  }
}

[data-theme='dark'] .app-brand-logo {
  color: #fafafa;

  .app-brand-logo__img {
    filter: brightness(0) invert(1);
  }
}
</style>

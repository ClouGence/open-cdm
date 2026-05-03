<template>
  <div class="empty-wrap">
    <div v-if="loading">
      <CCLoading :icon="loadingLogo" />
    </div>
    <CustomIcon v-else :type="icon" rightMargin size="26" />
    <a class="link-text" @click="jumpTo" v-if="link">{{ content }}</a>
    <span v-else class="normal-text">{{ content }}</span>
  </div>
</template>

<script>
import CCLoading from '@/components/widgets/CCLoading';

export default {
  props: {
    content: {
      type: String,
      default: '-'
    },
    icon: {
      type: String,
      default: 'icon-v2-Success2'
    },
    loading: {
      type: Boolean,
      default: false
    },
    loadingLogo: {
      type: String,
      default: 'icon-v2-logo-DM'
    },
    link: {
      type: String,
      default: ''
    }
  },
  components: {
    CCLoading
  },
  data() {
    return {
      test: ''
    };
  },
  methods: {
    isExternalLink(path) {
      return /^(https?:|mailto:|tel:)/.test(path);
    },
    jumpTo() {
      console.warn(this.isExternalLink(this.link));
      // eslint-disable-next-line no-unused-expressions
      this.isExternalLink(this.link) ? window.open(this.link, 'blank') : this.$router.push(this.link);
    }
  }
};
</script>

<style>
.empty-wrap {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.link-text {
  color: #6aa1ed;
  text-decoration: underline;
}

.normal-text {
  color: #aaa;
  font-size: 18px;
}
</style>

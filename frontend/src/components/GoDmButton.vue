<template>
  <Button size="small" @click.stop="handleGoDmConsole">{{ $t('shu-ju-cha-xun') }}</Button>
</template>
<script>
import { mapGetters } from 'vuex';

export default {
  name: 'GoDMButton',
  computed: {
    ...mapGetters(['includesDM'])
  },
  methods: {
    handleGoDmConsole() {
      if (this.includesDM) {
        window.open(this.$router.resolve({ path: '/sql' }).href, '_blank', 'noopener');
      } else {
        this.$Modal.warning({
          title: this.$t('cao-zuo-ti-shi'),
          content: this.$t('wei-an-zhuang-dm-ti-shi')
        });
      }
    }
  }
};
</script>

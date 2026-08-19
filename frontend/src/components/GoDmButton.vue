<template>
  <Button size="small" @click.stop="handleGoDmConsole">{{ $t('shu-ju-cha-xun') }}</Button>
</template>
<script>
export default {
  name: 'GoDMButton',
  methods: {
    handleGoDmConsole() {
      this.$router.push({ path: '/sql' });
    }
  }
};
</script>

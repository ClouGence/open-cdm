<template>
  <CustomIcon class="loading-icon" :type="icon" :size="size" rightMargin />
</template>

<script>
export default {
  props: {
    icon: {
      type: String,
      default: 'icon-v2-logo-DM'
    },
    size: {
      type: String,
      default: '80px'
    }
  }
};
</script>

<style scoped>
.loading {
  background-color: transparent;
}

.loading-icon {
  animation: blurFade 2.5s ease-in-out infinite;
}

@keyframes blurFade {
  0% {
    opacity: 0.4;
    filter: blur(2px);
    transform: scale(0.95);
  }
  50% {
    opacity: 1;
    filter: blur(0);
    transform: scale(1);
  }
  100% {
    opacity: 0.4;
    filter: blur(2px);
    transform: scale(0.95);
  }
}
</style>

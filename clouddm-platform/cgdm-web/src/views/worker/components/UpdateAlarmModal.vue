<template>
  <Modal :value="visible"></Modal>
</template>

<script>
export default {
  name: 'UpdateAlarmModal',
  props: {
    visible: Boolean,
    worker: Object
  }
};
</script>

<style scoped></style>

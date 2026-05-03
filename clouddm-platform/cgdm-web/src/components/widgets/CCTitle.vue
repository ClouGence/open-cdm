<script>
export default {
  name: 'CCTitle',
  props: {
    content: {
      type: String,
      default: ''
    }
  }
};
</script>

<template>
  <Breadcrumb class="bread-crumb">
    <BreadcrumbItem>{{ content }}</BreadcrumbItem>
  </Breadcrumb>
</template>

<style scoped lang="less">
.bread-crumb {
  margin-bottom: 10px;
}
</style>

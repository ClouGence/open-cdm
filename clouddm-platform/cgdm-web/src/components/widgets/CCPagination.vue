<template>
  <div class="cc-pagination">
    <div class="item" v-if="page <= 1">
      <a-icon class="disabled-icon" type="left" key="disable-left" />
    </div>
    <div class="item" v-else>
      <a-icon type="left" @click="handlePageChange('left')" key="left" />
    </div>
    <div class="item">
      {{ page }}
    </div>
    <div class="item">
      <a-icon v-if="hasNext" type="right" @click="handlePageChange('right')" key="right" />
      <a-icon class="disabled-icon" v-if="!hasNext" type="right" key="disable-right" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'CCPagination',
  props: {
    size: {
      type: String,
      default: 'default'
    },
    page: Number,
    hasNext: Boolean
  },
  computed: {
    iconSize() {
      let size;
      switch (this.size) {
        case 'small':
          size = 12;
          break;
        case 'default':
          size = 14;
          break;
        case 'large':
          size = 16;
          break;
        default:
          break;
      }

      return size;
    }
  },
  methods: {
    handlePageChange(type) {
      this.$emit('onChange', type);
    }
  }
};
</script>

<style scoped lang="less">
.cc-pagination {
  margin: 5px;
  display: flex;

  .item {
    padding: 5px;
  }
  .disabled-icon {
    cursor: not-allowed;
    color: rgba(0, 0, 0, 0.25);
  }
}
</style>

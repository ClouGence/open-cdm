<template>
    <div>
        <p class="markdown-html panel-text-content" v-html="content">
        </p>
    </div>
</template>

<script>

import Remarkable from 'remarkable';

export default {
  mounted() {
    this.$bus?.on?.('render', (range, data) => {
      this.render(range, data);
    });
  },

  unmounted() {
  },

  props: {
    panel: {},
    time: {}
  },

  data() {
    return {
      content: '',
      remarkable: new Remarkable()
    };
  },

  methods: {

    render(range, data) {
      this.content = this.remarkable.render(this.panel.content);
    }
  }

};
</script>

<style lang="less">
</style>

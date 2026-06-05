<template>
    <div class="singlestat-panel">
    </div>
</template>

<script>
import $ from 'jquery';
import { SingleStatCtrl } from '../../panel/singlestat/module';
import Scope from '../../directives/scope';
import SinglestatDirective from '../../directives/singlestat';

export default {
  mounted() {
    this.ctrlScope = new Scope();
    this.ctrl = new SingleStatCtrl(this.ctrlScope, {
      panel: this.panel
    });

    this.singlestatScope = new Scope();
    this.singlestatScope.ctrl = this.ctrl;
    SinglestatDirective(this.singlestatScope, $(this.$el));

    this.$bus?.on?.('render', (range, data) => {
      this.render(range, data);
    });
  },

  unmounted() {
    this.ctrlScope.$emit('$destroy');
    this.singlestatScope.$emit('$destroy');
  },

  props: {
    panel: {},
    time: {},
    emitter: {}
  },

  data() {
    return {
    };
  },

  methods: {

    render(range, data) {
      this.ctrl.range = range;
      this.ctrl.events.emit('data-received', data);
    }
  }

};
</script>

<style lang="less">
</style>

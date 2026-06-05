<template>
  <div />
</template>
<script>
import { mapState } from 'vuex';

export default {
  name: 'DmWaterMark',
  props: {
    inputText: {
      type: String,
      default: 'clouddm'
    },
    inputAllowDele: {
      type: Boolean,
      default: false
    },
    inputDestroy: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      maskDiv: {},
      parentNode: {}
    };
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.userInfo
    })
  },
  mounted() {
    this.$nextTick(() => {
      this.init();
      if (!this.inputAllowDele) {
        this.Monitor();
      }
    });
  },
  methods: {
    init() {
      const canvas = document.createElement('canvas');
      canvas.id = 'canvas';
      canvas.width = '300';
      canvas.height = '300';
      this.maskDiv = document.createElement('div');
      const ctx = canvas.getContext('2d');
      ctx.font = 'normal 200 18px Microsoft Yahei';
      ctx.fillStyle = 'rgb(235, 235, 235)';
      ctx.rotate((15 * Math.PI) / 180);
      ctx.fillText(this.inputText, 90, 60);
      const src = canvas.toDataURL('image/png');
      this.maskDiv.style.position = 'fixed';
      this.maskDiv.style.zIndex = '9999';
      this.maskDiv.id = '_waterMark';
      this.maskDiv.style.top = '100px';
      this.maskDiv.style.left = '0';
      this.maskDiv.style.width = '100%';
      this.maskDiv.style.height = '100%';
      this.maskDiv.style.pointerEvents = 'none';
      this.maskDiv.style.backgroundImage = `URL(${src})`;
      this.parentNode = document.querySelector('.home');
      this.parentNode.appendChild(this.maskDiv);
    },
    Monitor() {
      const options = {
        childList: true,
        attributes: true,
        characterData: true,
        subtree: true,
        attributeOldValue: true,
        characterDataOldValue: true
      };
      const observer = new MutationObserver(this.callback);
      observer.observe(this.parentNode, options);
    },
    callback(mutations) {
      if (mutations[0].target.id === '_waterMark') {
        this.removeMaskDiv();
      }
      if (mutations[0].attributeName === 'id') {
        this.removeMaskDiv();
        this.init();
      }
      if (mutations[0].removedNodes[0] && mutations[0].removedNodes[0].id === '_waterMark') {
        if (JSON.stringify(this.userInfo) !== '{}') {
          this.init();
        }
      }
    },
    removeMaskDiv() {
      this.parentNode.removeChild(this.maskDiv);
    },
    createMaskDiv() {
      this.init();
    }
  },
  watch: {
    inputText() {
      this.$nextTick(() => {
        this.removeMaskDiv();
      });
    }
  },
  destroy() {
    if (this.inputDestroy) {
      this.removeMaskDiv();
    }
  }
};
</script>

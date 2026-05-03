<template>
  <div class="cc-modal-warp">
    <Modal class="cc-modal" width="600" ref="ModalRef" :value="visible" @on-cancel="handleDestroy">
      <template #header>
        <div class="header-wrap">
          <CustomIcon :type="iconMapping[type]" size="20px" color="#ff6e0d" rightMargin="10px" />
          <div class="cc-modal-title">{{ title }}</div>
        </div>
      </template>
      <div class="cc-modal-content">
        {{ formatContent }}
      </div>
      <template #footer>
        <Button type="primary" class="footer-btn" @click="handleDestroy">
          {{ i18n.global.t('que-ding') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>

<script>
import i18n from '@/i18n';

const iconMapping = {
  warn: 'icon-v2-WarnColorful',
  success: 'icon-v2-SuccessColorful',
  error: 'icon-v2-ErrorColorful'
};

export default {
  name: 'CCModalInstance',
  props: {
    type: {
      type: String,
      default: 'error'
    },
    title: {
      type: String,
      default: ''
    },
    content: {
      type: String,
      default: ''
    },
    close: {
      type: Function,
      default: () => {}
    },
    onOk: {
      type: Function,
      default: null
    }
  },

  computed: {
    i18n() {
      return i18n;
    },
    iconMapping() {
      return iconMapping;
    },
    formatContent() {
      try {
        return this.removeQuotes(JSON.parse(this.content).join(''));
      } catch (err) {
        return this.removeQuotes(this.content);
      }
    }
  },

  data() {
    return {
      visible: true
    };
  },

  methods: {
    handleDestroy() {
      try {
        if (this.onOk) {
          this.onOk();
        }
        this.visible = false;
        if (!this.visible) {
          this.close();
        }
      } catch (err) {
        console.error(err);
      }
    },
    removeQuotes(str) {
      return str.replace(/"/g, '');
    }
  }
};
</script>
<style scoped lang="less">
.cc-modal-content {
  word-wrap: break-word;
}

.header-wrap {
  display: flex;
  align-items: center;
  .cc-modal-title {
    color: #17233d;
    font-size: 16px;
    font-weight: 500;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.footer-btn {
  width: 120px;
}
.cc-modal {
  :deep(.ivu-modal-header) {
    background: none;
    box-shadow: rgba(0, 0, 0, 0.05) 0px 0px 0px 0.5px;
  }
  :deep(.ivu-modal-content) {
    border-radius: 3px;
  }
  :deep(.ivu-modal-footer) {
    display: flex;
    justify-content: center;
    border: none;
    padding: 12px 18px 18px 18px;
  }
}
</style>

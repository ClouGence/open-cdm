<template>
  <a-modal
    v-bind="attrs"
    :open="visibleProxy"
    @update:open="handleUpdateOpen"
    @ok="onOk"
    @cancel="onCancel"
    :maskClosable="maskClosable"
    :afterClose="onAfterClose"
    :ok-text="$t('que-ren')"
    :cancel-text="$t('qu-xiao')"
    :footer="isHideFooter ? null : undefined"
  >
    <template #modalRender="{ originVNode }">
      <div :style="modalStyle" ref="modalTransformRef">
        <component :is="originVNode" />
      </div>
    </template>
    <template v-if="$slots.title" #title>
      <slot name="title" />
    </template>
    <template v-if="visibleProxy && $slots.default">
      <slot />
    </template>
    <template v-if="isInfoType" #footer>
      <div class="info-footer">
        <a-button @click="onConfirm" type="primary">{{ $t('que-ren') }}</a-button>
      </div>
    </template>
    <template v-else-if="$slots.footer" #footer>
      <slot name="footer" />
    </template>

    <div ref="dragHandleRef" class="modal-drag-handle" v-if="draggable"></div>
  </a-modal>
</template>
<script setup>
import { ref, watch, useAttrs, nextTick, computed, useSlots } from 'vue';
import { useDraggable } from '@vueuse/core';

const slots = useSlots();

const props = defineProps({
  modelValue: { type: Boolean, required: true },
  draggable: { type: Boolean, default: true },
  maskClosable: { type: Boolean, default: false },
  footerHide: { type: Boolean, default: false },
  type: { type: String, default: '' }
});

const emit = defineEmits(['update:modelValue', 'on-ok', 'on-cancel', 'on-confirm']);
const attrs = useAttrs();

const visibleProxy = ref(props.modelValue);
const dragHandleRef = ref(null);
const modalTransformRef = ref(null);

const isDragging = ref(false);
const modalPosition = ref({ x: 0, y: 0 });
let dragPointerStart = { x: 0, y: 0 };
let modalPositionStart = { x: 0, y: 0 };
let modalRectStart = null;

const modalStyle = computed(() => ({
  transform: `translate(${modalPosition.value.x}px, ${modalPosition.value.y}px)`,
  transition: isDragging.value ? 'none' : 'transform 0.2s ease'
}));

const isHideFooter = computed(() => {
  return props.footerHide && !slots?.footer;
});

const isInfoType = computed(() => {
  return props.type === 'info';
});

watch(
  () => props.modelValue,
  (val) => {
    visibleProxy.value = val;
    if (val && props.draggable) {
      nextTick(() => {
        resetModalPosition();
      });
    }
  }
);

function getClientXY(e) {
  const point = e?.touches?.[0] || e?.changedTouches?.[0] || e;
  return {
    x: point?.clientX ?? 0,
    y: point?.clientY ?? 0
  };
}

const { isDragging: dragState } = useDraggable(dragHandleRef, {
  axis: 'both',
  initialValue: { x: 0, y: 0 },
  onMove(_position, event) {
    if (!props.draggable) return;
    const handleEl = dragHandleRef.value;
    if (!handleEl) return;
    const modalEl = handleEl.closest && handleEl.closest('.ant-modal');
    if (!modalEl || !modalRectStart) return;

    const { x: cx, y: cy } = getClientXY(event);
    const dxRaw = cx - dragPointerStart.x;
    const dyRaw = cy - dragPointerStart.y;

    // 以起始矩形为基准预测移动后的矩形
    const candidateLeft = modalRectStart.left + dxRaw;
    const candidateRight = modalRectStart.right + dxRaw;
    const candidateTop = modalRectStart.top + dyRaw;
    const candidateBottom = modalRectStart.bottom + dyRaw;

    const viewportWidth = window.innerWidth;
    const viewportHeight = window.innerHeight;

    let dx = dxRaw;
    let dy = dyRaw;

    if (candidateLeft < 0) dx += -candidateLeft;
    if (candidateRight > viewportWidth) dx -= candidateRight - viewportWidth;
    if (candidateTop < 0) dy += -candidateTop;
    if (candidateBottom > viewportHeight) dy -= candidateBottom - viewportHeight;

    modalPosition.value = {
      x: modalPositionStart.x + dx,
      y: modalPositionStart.y + dy
    };
  },
  onStart(_position, event) {
    isDragging.value = true;
    const { x: cx, y: cy } = getClientXY(event);
    dragPointerStart = { x: cx, y: cy };
    modalPositionStart = { x: modalPosition.value.x, y: modalPosition.value.y };
    const handleEl = dragHandleRef.value;
    const modalEl = handleEl && handleEl.closest ? handleEl.closest('.ant-modal') : null;
    const transformEl = modalTransformRef.value;
    modalRectStart = transformEl || modalEl ? (transformEl || modalEl).getBoundingClientRect() : null;
  },
  onEnd() {
    isDragging.value = false;
  }
});

function resetModalPosition() {
  modalPosition.value = { x: 0, y: 0 };
}

function handleUpdateOpen(val) {
  visibleProxy.value = val;
  emit('update:modelValue', val);
}

function onOk() {
  emit('on-ok');
  // 默认弹窗，兼容一下，确认按钮关闭弹窗
  if (!isInfoType.value && !slots?.footer) {
    handleUpdateOpen(false);
  }
}

function onCancel() {
  emit('on-cancel');
}

function onConfirm() {
  emit('on-confirm');
  handleUpdateOpen(false);
}

function onAfterClose() {
  // 关闭动画结束后再重置位置，确保关闭动画从拖拽后位置开始
  resetModalPosition();
}
</script>

<style scoped lang="less">
.cc-modal-warp {
  width: 1000px !important;
  background: pink;
}
.modal-drag-handle {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 50px;
  cursor: move;
  z-index: 1000;
  background: transparent;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
}

.modal-drag-handle:hover {
  background: rgba(0, 0, 0, 0.02) !important;
}

/* 确保modal可以拖拽 */
:deep(.ant-modal) {
  transition: transform 0.2s ease;
}

:deep(.ant-modal-content) {
  position: relative;
}

.info-footer {
  text-align: right;
}
</style>

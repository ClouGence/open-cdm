<template>
  <Sonner
    class="toaster group"
    position="bottom-right"
    :expand="false"
    close-button
    close-button-position="top-right"
    :close-button-aria-label="closeAriaLabel"
    :toast-options="{
      classes: {
        toast:
          'group toast group-[.toaster]:bg-white group-[.toaster]:text-[#171717] group-[.toaster]:border-[#dfdfdf] group-[.toaster]:shadow-lg group-[.toaster]:rounded-lg group-[.toaster]:pr-10',
        description: 'group-[.toast]:text-[#707070]',
        actionButton: 'group-[.toast]:bg-[#181d26] group-[.toast]:text-white',
        cancelButton: 'group-[.toast]:bg-[#f5f5f5] group-[.toast]:text-[#707070]',
        closeButton: 'sonner-close-button'
      }
    }"
    v-bind="$attrs"
  >
    <template #success-icon>
      <svg class="size-5" viewBox="0 0 20 20" fill="#3ecf8e">
        <path
          fill-rule="evenodd"
          d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 10-1.06 1.061l2.5 2.5a.75.75 0 001.137-.089l4-5.5z"
          clip-rule="evenodd"
        />
      </svg>
    </template>
    <template #error-icon>
      <svg class="size-5" viewBox="0 0 20 20" fill="#ef4444">
        <path
          fill-rule="evenodd"
          d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.28 7.22a.75.75 0 00-1.06 1.06L8.94 10l-1.72 1.72a.75.75 0 101.06 1.06L10 11.06l1.72 1.72a.75.75 0 101.06-1.06L11.06 10l1.72-1.72a.75.75 0 00-1.06-1.06L10 8.94 8.28 7.22z"
          clip-rule="evenodd"
        />
      </svg>
    </template>
    <template #warning-icon>
      <svg class="size-5" viewBox="0 0 20 20" fill="#f59e0b">
        <path
          fill-rule="evenodd"
          d="M8.485 2.495c.673-1.167 2.357-1.167 3.03 0l6.28 10.875c.673 1.167-.17 2.625-1.516 2.625H3.72c-1.347 0-2.189-1.458-1.515-2.625L8.485 2.495zM10 6a.75.75 0 01.75.75v3.5a.75.75 0 01-1.5 0v-3.5A.75.75 0 0110 6zm0 9a1 1 0 100-2 1 1 0 000 2z"
          clip-rule="evenodd"
        />
      </svg>
    </template>
    <template #info-icon>
      <svg class="size-5" viewBox="0 0 20 20" fill="#3b82f6">
        <path
          fill-rule="evenodd"
          d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a.75.75 0 000 1.5h.253a.25.25 0 01.244.304l-.459 2.066A1.75 1.75 0 0010.747 15H11a.75.75 0 000-1.5h-.253a.25.25 0 01-.244-.304l.459-2.066A1.75 1.75 0 009.253 9H9z"
          clip-rule="evenodd"
        />
      </svg>
    </template>
    <template #loading-icon>
      <svg class="size-5 animate-spin" viewBox="0 0 24 24" fill="none">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="#171717" stroke-width="4" />
        <path class="opacity-75" fill="#3ecf8e" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
      </svg>
    </template>
    <template #close-icon>
      <svg class="size-3.5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
        <path
          d="M6.28 5.22a.75.75 0 00-1.06 1.06L8.94 10l-3.72 3.72a.75.75 0 101.06 1.06L10 11.06l3.72 3.72a.75.75 0 101.06-1.06L11.06 10l3.72-3.72a.75.75 0 00-1.06-1.06L10 8.94 6.28 5.22z"
        />
      </svg>
    </template>
  </Sonner>
</template>

<script setup>
import { computed } from 'vue';
import { Toaster as Sonner } from 'vue-sonner';
import i18n from '@/i18n';

const closeAriaLabel = computed(() => i18n.global.t('guan-bi'));
</script>

<style scoped>
:deep([data-sonner-toast][data-styled='true'] [data-close-button].sonner-close-button) {
  right: 8px;
  top: 8px;
  left: unset;
  bottom: unset;
  width: 20px;
  height: 20px;
  padding: 0;
  border: none;
  border-radius: 4px;
  background: transparent;
  color: #9a9a9a;
  transform: none;
  box-shadow: none;
}

:deep([data-sonner-toast][data-styled='true'] [data-close-button].sonner-close-button:hover) {
  background: #f5f5f5;
  color: #707070;
}

:deep([data-sonner-toast][data-styled='true'] [data-close-button].sonner-close-button:focus-visible) {
  box-shadow: 0 0 0 2px rgba(24, 29, 38, 0.12);
}
</style>

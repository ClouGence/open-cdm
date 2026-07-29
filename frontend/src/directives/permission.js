import appLogger from '@/utils/logger';
import store from '@/store';

// only dm desktop
export const permission = {
  mounted(el, binding) {
    const { value } = binding;
    if (value) {
      const isDesktop = store.getters.isDesktop;
      appLogger.debug(isDesktop);
      let hidden = false;
      if (isDesktop) {
        hidden = true;
      }
      if (el.parentNode && hidden) {
        el.parentNode.removeChild(el);
      }
    } else {
      throw new Error('need permissions');
    }
  }
};

import store from '@/store';

// only dm desktop
export const permission = {
  mounted(el, binding) {
    const { value } = binding;
    if (value) {
      const isDesktop = store.getters.isDesktop;
      console.log(isDesktop);
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

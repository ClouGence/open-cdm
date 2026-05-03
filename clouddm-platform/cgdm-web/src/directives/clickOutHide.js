const clickOutHide = {
  beforeMount(el, binding) {
    el.__vueClickOutside__ = (e) => {
      if (!el.contains(e.target)) {
        binding.value(e);
      }
    };
    document.addEventListener('click', el.__vueClickOutside__);
  },
  unmounted(el) {
    document.removeEventListener('click', el.__vueClickOutside__);
    delete el.__vueClickOutside__;
  }
};

export default clickOutHide;

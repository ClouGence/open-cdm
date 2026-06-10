import { createVNode, render } from 'vue';
import CCModalInstance from '@/components/ui/CCModalInstance';

const CCMI = {
  confirm(config) {
    // Create a DOM element
    const div = document.createElement('div');
    document.body.appendChild(div);

    // Create VNode
    const vnode = createVNode(CCModalInstance, {
      ...config,
      close: () => {
        // Examples of destruction of components
        render(null, div);
        div.remove();
      }
    });

    // Mount Component
    render(vnode, div);
  }
};

export default CCMI;

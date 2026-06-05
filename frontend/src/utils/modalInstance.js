import { createVNode, render } from 'vue';
import CCModalInstance from '@/components/ui/CCModalInstance';

const CCMI = {
  confirm(config) {
    // 创建一个 DOM 元素
    const div = document.createElement('div');
    document.body.appendChild(div);

    // 创建 VNode
    const vnode = createVNode(CCModalInstance, {
      ...config,
      close: () => {
        // 销毁组件实例
        render(null, div);
        div.remove();
      }
    });

    // 挂载组件
    render(vnode, div);
  }
};

export default CCMI;

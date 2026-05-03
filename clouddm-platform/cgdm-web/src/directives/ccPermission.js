import store from '@/store';
import { permissionList } from '@/const/permission';
import { EDITIONS } from '@/const';

// no use now
export const permission = {
  inserted(el, binding) {
    const { value } = binding;
    if (value) {
      let inBlackList = false;
      const edition = store.state.userInfo.ccGlobalSetting.productAuthType;
      const blackUris = store.getters && Object.keys(store.getters.blackUri);
      if (permissionList.blackUris[value]) {
        const blackUri = permissionList.blackUris[value];
        blackUri.forEach((uri) => {
          if (blackUris.includes(uri)) {
            inBlackList = true;
          }
        });
      }
      if (permissionList.common.includes(value)) {
        inBlackList = true;
      }

      if (inBlackList && el.parentNode) {
        if (edition === EDITIONS.COMMUNITY_VERSION) {
          el.parentNode.removeChild(el);
        } else if (edition === EDITIONS.EXPERIENCE_VERSION) {
          // const newEl = document.createElement('span');
          // newEl.innerText = '商业版';
          // newEl.style.position = 'absolute';
          // el.appendChild(newEl);
        }
      }
    } else {
      throw new Error('need permissions');
    }
  }
};

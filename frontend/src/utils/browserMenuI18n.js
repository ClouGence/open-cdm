import i18n from '@/i18n';

export function getBrowserMenuI18nKey(menuId) {
  if (!menuId) {
    return '';
  }
  return `ui-menu-title-${menuId.toLowerCase().replace(/_/g, '-')}`;
}

export function resolveBrowserMenuLabel(menu) {
  if (!menu) {
    return '';
  }
  const key = getBrowserMenuI18nKey(menu.menuId);
  if (key && i18n.global.te(key)) {
    return i18n.global.t(key);
  }
  return menu.i18n || menu.menuId || '';
}

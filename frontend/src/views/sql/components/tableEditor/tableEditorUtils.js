import dayjs from 'dayjs';
import { cloneDeep as deepClone } from '@/utils/lodash';

let itemSequence = 0;

function nextItemKey(panelKey) {
  itemSequence += 1;
  return `${panelKey}-${dayjs().valueOf()}-${itemSequence}`;
}

function selectedOption(fieldSchema, value) {
  if (!Array.isArray(fieldSchema.options)) {
    return null;
  }
  return fieldSchema.options.find((option) => option.value === value) || null;
}

function initializeSelectedOption(target, fieldSchema) {
  const option = selectedOption(fieldSchema, target[fieldSchema.field]);
  if (!option || !Array.isArray(option.children)) {
    return;
  }
  initializeFields(target, option.children);
}

export function initializeFields(target, fields = []) {
  fields.forEach((fieldSchema) => {
    if (!(fieldSchema.field in target)) {
      target[fieldSchema.field] = deepClone(fieldSchema.defaultVal);
    }

    if (fieldSchema.type === 'Options' || fieldSchema.type === 'Radios') {
      initializeSelectedOption(target, fieldSchema);
    }

    if (fieldSchema.type === 'Fold' && Array.isArray(fieldSchema.children)) {
      initializeFields(target, fieldSchema.children);
    }
  });
  return target;
}

export function createEditorItem(panelKey, panelSchema) {
  const item = {
    key: nextItemKey(panelKey),
    schema: panelKey,
    isAdd: true
  };
  initializeFields(item, panelSchema?.children);
  return item;
}

export function normalizeEditorItem(panelKey, source) {
  const item = {};
  Object.keys(source || {}).forEach((field) => {
    const value = source[field];
    if (value === 'false') {
      item[field] = false;
      return;
    }
    if (value === 'true') {
      item[field] = true;
      return;
    }
    if (Array.isArray(value)) {
      item[field] = value.map((child) => ({
        ...deepClone(child),
        key: child.key || nextItemKey(`${panelKey}-${field}`)
      }));
      return;
    }
    item[field] = value;
  });
  item.key = item.key || nextItemKey(panelKey);
  item.schema = panelKey;
  item.isAdd = false;
  return item;
}

function hydrateRenderableField(fieldSchema, target) {
  if (!(fieldSchema.field in target)) {
    target[fieldSchema.field] = deepClone(fieldSchema.defaultVal);
  }

  if (fieldSchema.type === 'Options' || fieldSchema.type === 'Radios') {
    const option = selectedOption(fieldSchema, target[fieldSchema.field]);
    fieldSchema.children = option?.children ? deepClone(option.children) : [];
    initializeFields(target, fieldSchema.children);
    fieldSchema.children.forEach((child) => hydrateRenderableField(child, target));
    return;
  }

  if (Array.isArray(fieldSchema.children)) {
    fieldSchema.children.forEach((child) => hydrateRenderableField(child, target));
  }
}

export function buildRenderableFields(panelSchema, target) {
  const fields = deepClone(panelSchema?.children || []);
  fields.forEach((fieldSchema) => hydrateRenderableField(fieldSchema, target));
  return fields.filter((fieldSchema) => !fieldSchema.hide);
}

function findFieldInList(fields, fieldName) {
  for (const fieldSchema of fields || []) {
    if (fieldSchema.field === fieldName) {
      return fieldSchema;
    }

    const childMatch = findFieldInList(fieldSchema.children, fieldName);
    if (childMatch) {
      return childMatch;
    }

    for (const option of fieldSchema.options || []) {
      const optionMatch = findFieldInList(option.children, fieldName);
      if (optionMatch) {
        return optionMatch;
      }
    }
  }
  return null;
}

export function findFieldSchema(panelSchema, fieldName) {
  return findFieldInList(panelSchema?.children, fieldName);
}

export function isEmptyEditorValue(value) {
  if (value === null || value === undefined) {
    return true;
  }
  if (typeof value === 'string') {
    return value.trim() === '';
  }
  if (Array.isArray(value)) {
    return value.length === 0;
  }
  return false;
}

function collectRequiredErrors(fields, target, errors) {
  fields.forEach((fieldSchema) => {
    if (fieldSchema.require && !fieldSchema.hide && isEmptyEditorValue(target[fieldSchema.field])) {
      errors.push({
        field: fieldSchema.field,
        label: fieldSchema.titleI18N
      });
    }

    if (fieldSchema.type === 'Options' || fieldSchema.type === 'Radios') {
      const option = selectedOption(fieldSchema, target[fieldSchema.field]);
      if (option?.children) {
        collectRequiredErrors(option.children, target, errors);
      }
    }

    if (fieldSchema.type === 'Fold' && fieldSchema.children) {
      collectRequiredErrors(fieldSchema.children, target, errors);
    }
  });
}

export function validatePanelItem(panelSchema, target) {
  const errors = [];
  collectRequiredErrors(panelSchema?.children || [], target, errors);
  return errors;
}

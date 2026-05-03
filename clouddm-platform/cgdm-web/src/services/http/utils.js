export const checkStatus = (res) => {
  const { status } = res;

  if (status === 200) {
    const contentType = res.headers && res.headers['content-type'];
    if (contentType === 'application/vnd.ms-excel;charset=utf-8' || contentType === 'application/octet-stream') {
      return res;
    }

    // 新增如果是bold类型，把header也塞进response
    const isBlobResponse = res.request && res.request.responseType === 'blob';
    const isBlobData = typeof Blob !== 'undefined' && res.data instanceof Blob;
    const isOctetStream = contentType && contentType.indexOf('application/octet-stream') !== -1;

    if (isBlobResponse || isBlobData || isOctetStream) {
      try {
        if (res.data && typeof res.data === 'object') {
          Object.defineProperty(res.data, 'headers', {
            value: res.headers || {},
            enumerable: false,
            configurable: true
          });
        }
      } catch (e) {
        console.error(e);
      }
      return res.data;
    }
    return res.data;
  }

  const msg = res.data.message || res.data || '';

  return {
    status,
    msg
  };
};

// 从原本的递归写法，到栈迭代
export const trimObj = (obj) => {
  if (obj === null || typeof obj !== 'object') {
    return typeof obj === 'string' ? obj.trim() : obj;
  }

  const seen = new WeakMap();
  const root = Array.isArray(obj) ? [] : {};
  const stack = [{ source: obj, target: root }];
  seen.set(obj, root);

  while (stack.length > 0) {
    const { source, target } = stack.pop();

    if (Array.isArray(source)) {
      for (let i = 0; i < source.length; i++) {
        const value = source[i];
        if (value !== null && typeof value === 'object') {
          if (seen.has(value)) {
            target[i] = seen.get(value);
          } else {
            const newValue = Array.isArray(value) ? [] : {};
            seen.set(value, newValue);
            target[i] = newValue;
            stack.push({ source: value, target: newValue });
          }
        } else if (typeof value === 'string') {
          target[i] = value.trim();
        } else {
          target[i] = value;
        }
      }
    } else {
      for (const key in source) {
        if (Object.prototype.hasOwnProperty.call(source, key)) {
          const value = source[key];
          const trimmedKey = key.trim();
          if (value !== null && typeof value === 'object') {
            if (seen.has(value)) {
              target[trimmedKey] = seen.get(value);
            } else {
              const newValue = Array.isArray(value) ? [] : {};
              seen.set(value, newValue);
              target[trimmedKey] = newValue;
              stack.push({ source: value, target: newValue });
            }
          } else if (typeof value === 'string') {
            target[trimmedKey] = value.trim();
          } else {
            target[trimmedKey] = value;
          }
        }
      }
    }
  }

  return root;
};

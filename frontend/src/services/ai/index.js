import i18n from '@/i18n';
import request from '@/services/http/request';

export class AiRequestError extends Error {
  constructor(code, data = null, msg = '') {
    super(msg || code);
    this.code = code;
    this.data = data;
    this.msg = msg;
  }
}

export function aiErrorMessage(error) {
  if (typeof error?.msg === 'string' && error.msg.trim()) return error.msg;
  const key = `ai.errors.${error?.code}`;
  if (i18n.global.te(key)) return i18n.global.t(key);
  return i18n.global.t('ai.errors.INTERNAL_ERROR');
}

// Preserve identifiers and prompt whitespace instead of applying the default recursive trim.
export async function aiRequest(path, data = {}, { signal } = {}) {
  try {
    const payload = await request({
      url: `/api/entry/ai/${path}`,
      data,
      signal,
      transformRequest: [(value) => JSON.stringify(value)],
      noStatus: true,
      throwError: true
    });
    if (signal?.aborted) throw payload;
    if (payload.code !== '1') throw new AiRequestError(payload.code, payload.data, payload.msg);
    return payload.data;
  } catch (error) {
    if (error instanceof AiRequestError || signal?.aborted) throw error;
    if (typeof error.data?.code === 'string') throw new AiRequestError(error.data.code, error.data.data, error.data.msg);
    if (error.status === 406) throw new AiRequestError('FORBIDDEN', null, error.msg);
    if (error.status) throw new AiRequestError(`HTTP_${error.status}`, null, error.msg);
    if (error.code === 'ECONNABORTED' || error.code === 'ETIMEDOUT') throw new AiRequestError('REQUEST_TIMEOUT');
    throw new AiRequestError('NETWORK_ERROR');
  }
}

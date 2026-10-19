import { AiRequestError } from './index';
import { fetchRequest } from '@/services/http/request';

const EVENT_TYPES = new Set(['started', 'snapshot', 'phase', 'toolStarted', 'toolCompleted', 'textDelta', 'result', 'ERROR', 'done']);
const TERMINAL_STATES = new Set(['COMPLETED', 'NEEDS_INPUT', 'FAILED', 'CANCELLED', 'EXPIRED']);

// Parsing is separate from transport so split UTF-8, CRLF and terminal-loss cases are reproducible.
export async function consumeAiEvents(body, { sessionId, requestId, onEvent, onActivity = () => {} }) {
  const reader = body.getReader();
  const decoder = new TextDecoder('utf-8', { fatal: true });
  let pending = '';
  let skipLf = false;
  let frame = { event: '', id: '', data: [] };
  let sequence = 0;
  let turnId = null;
  let complete = false;

  function line(value) {
    if (value !== '') {
      if (value.startsWith(':')) return;
      const colon = value.indexOf(':');
      let field = value;
      let content = '';
      if (colon >= 0) {
        field = value.slice(0, colon);
        content = value.slice(colon + 1);
      }
      if (content.startsWith(' ')) content = content.slice(1);
      if (field === 'data') frame.data.push(content);
      if (field === 'event') frame.event = content;
      if (field === 'id') frame.id = content;
      return;
    }
    const current = frame;
    frame = { event: '', id: '', data: [] };
    if (!current.data.length) return;
    let event;
    try {
      event = JSON.parse(current.data.join('\n'));
    } catch {
      throw new AiRequestError('STREAM_PROTOCOL');
    }
    // The global handler can emit an unwrapped ERROR before admission or after response commit.
    if (current.event === 'ERROR' && typeof event.code === 'string') {
      throw new AiRequestError(event.code, null, event.error || event.msg);
    }
    if (
      event.sessionId !== sessionId ||
      event.requestId !== requestId ||
      typeof event.turnId !== 'string' ||
      !Number.isSafeInteger(event.seq) ||
      event.seq < 1 ||
      !EVENT_TYPES.has(event.type) ||
      event.type !== current.event ||
      current.id !== `${event.turnId}:${event.seq}` ||
      !event.data ||
      typeof event.data !== 'object'
    ) {
      throw new AiRequestError('STREAM_PROTOCOL');
    }
    if (turnId !== null && event.turnId !== turnId) throw new AiRequestError('STREAM_PROTOCOL');
    turnId = event.turnId;
    if (event.seq <= sequence) return;
    if (event.seq !== sequence + 1 || (sequence === 0 && event.type !== 'started')) throw new AiRequestError('STREAM_PROTOCOL');
    if (event.type === 'done' && !TERMINAL_STATES.has(event.data.status)) throw new AiRequestError('STREAM_PROTOCOL');
    sequence = event.seq;
    onEvent(event);
    if (event.type === 'done') complete = true;
  }

  try {
    while (!complete) {
      const chunk = await reader.read();
      if (!chunk.done) onActivity();
      let text;
      try {
        text = decoder.decode(chunk.value, { stream: !chunk.done });
      } catch {
        throw new AiRequestError('STREAM_PROTOCOL');
      }
      for (const character of text) {
        if (skipLf) {
          skipLf = false;
          if (character === '\n') continue;
        }
        if (character === '\n' || character === '\r') {
          line(pending);
          pending = '';
          skipLf = character === '\r';
          if (complete) break;
        } else {
          pending += character;
        }
      }
      if (chunk.done) break;
    }
    // A partial frame at EOF is never delivered. The caller must query the persisted turn when done is absent.
    return { complete, turnId };
  } finally {
    try {
      await reader.cancel();
    } finally {
      reader.releaseLock();
    }
  }
}

async function readAiStream(path, data, { onEvent, signal, admissionTimeout = 60000, idleTimeout = 40000 }) {
  const controller = new AbortController();
  const abort = () => controller.abort();
  if (signal?.aborted) abort();
  signal?.addEventListener('abort', abort, { once: true });
  let admitted = false;
  const admissionTimer = setTimeout(abort, admissionTimeout);
  let idleTimer;
  const activity = () => {
    if (!admitted) return;
    clearTimeout(idleTimer);
    idleTimer = setTimeout(abort, idleTimeout);
  };
  try {
    const response = await fetchRequest(`/api/entry/ai/${path}`, { data, signal: controller.signal, accept: 'text/event-stream' });
    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
      const payload = await response.json().catch(() => null);
      if (!payload) throw new AiRequestError('STREAM_PROTOCOL');
      throw new AiRequestError(payload.code, payload.data, payload.msg);
    }
    if (!contentType.includes('text/event-stream') || !response.body) throw new AiRequestError('STREAM_PROTOCOL');
    const result = await consumeAiEvents(response.body, {
      sessionId: data.sessionId,
      requestId: data.requestId,
      onActivity: activity,
      onEvent: (event) => {
        if (event.type === 'started') {
          admitted = true;
          clearTimeout(admissionTimer);
          activity();
        }
        onEvent(event);
      }
    });
    if (!admitted) throw new AiRequestError('STREAM_PROTOCOL');
    return result;
  } catch (error) {
    if (error instanceof AiRequestError || signal?.aborted) throw error;
    if (typeof error.data?.code === 'string') throw new AiRequestError(error.data.code, error.data.data, error.data.msg);
    if (error.status === 406) throw new AiRequestError('FORBIDDEN', null, error.msg);
    if (error.status) throw new AiRequestError(`HTTP_${error.status}`, null, error.msg);
    if (controller.signal.aborted) throw new AiRequestError('REQUEST_TIMEOUT');
    throw new AiRequestError('NETWORK_ERROR');
  } finally {
    clearTimeout(admissionTimer);
    clearTimeout(idleTimer);
    signal?.removeEventListener('abort', abort);
  }
}

export function streamAiTurn(data, options) {
  return readAiStream(
    'turn/streamTurn',
    { ...data, timezone: Intl.DateTimeFormat().resolvedOptions().timeZone, timezoneOffsetMin: new Date().getTimezoneOffset() },
    options
  );
}

export function resumeAiTurn(data, options) {
  return readAiStream('turn/resumeTurn', data, options);
}

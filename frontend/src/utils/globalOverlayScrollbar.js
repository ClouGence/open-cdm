const MIN_THUMB_SIZE = 32;
const TRACK_HIT_SIZE = 16;

const SKIP_SELECTOR = [
  '.monaco-editor',
  '.monaco-scrollable-element',
  '.luckysheet',
  '.app-page-tabs',
  '.table-editor-sidebar',
  '.cc-global-sb-root',
  '[data-native-scrollbar]'
].join(',');

const OVERLAY_LAYER_SELECTOR = [
  '.ant-modal-wrap',
  '.ant-modal-root',
  '.ivu-modal-wrap',
  '.ant-drawer',
  '.ant-popover',
  '.ant-dropdown',
  '.ant-select-dropdown',
  '.ant-picker-dropdown'
].join(',');

let installed = false;

function shouldSkipElement(el) {
  if (!el || el.nodeType !== 1) return true;
  const tag = el.tagName;
  if (tag === 'TEXTAREA' || tag === 'SELECT' || tag === 'INPUT' || tag === 'OPTION' || tag === 'IFRAME' || tag === 'CANVAS') {
    return true;
  }
  if (el.closest && el.closest(SKIP_SELECTOR)) {
    return true;
  }
  return false;
}

function getScrollableAxes(el) {
  if (shouldSkipElement(el)) return { v: false, h: false };
  const isDoc = el === document.documentElement || el === document.body;
  const clientH = isDoc ? window.innerHeight : el.clientHeight;
  const clientW = isDoc ? window.innerWidth : el.clientWidth;
  if (clientH < 24 || clientW < 24) return { v: false, h: false };

  const scrollH = el.scrollHeight;
  const scrollW = el.scrollWidth;
  if (scrollH - clientH <= 2 && scrollW - clientW <= 2) {
    return { v: false, h: false };
  }

  const style = window.getComputedStyle(el);
  if (style.display === 'none' || style.visibility === 'hidden' || style.opacity === '0') {
    return { v: false, h: false };
  }

  const oy = style.overflowY;
  const ox = style.overflowX;
  const canV = scrollH - clientH > 2 && (oy === 'auto' || oy === 'scroll' || oy === 'overlay' || (isDoc && oy === 'visible'));
  const canH = scrollW - clientW > 2 && (ox === 'auto' || ox === 'scroll' || ox === 'overlay' || (isDoc && ox === 'visible'));
  return { v: canV, h: canH };
}

function getClippedRect(el) {
  const isDoc = el === document.documentElement || el === document.body;
  let left = 0;
  let top = 0;
  let right = window.innerWidth;
  let bottom = window.innerHeight;

  if (!isDoc) {
    const r = el.getBoundingClientRect();
    if (r.width <= 0 || r.height <= 0) return null;
    left = Math.max(left, r.left);
    top = Math.max(top, r.top);
    right = Math.min(right, r.right);
    bottom = Math.min(bottom, r.bottom);

    let parent = el.parentElement;
    while (parent && parent !== document.body && parent !== document.documentElement) {
      const ps = window.getComputedStyle(parent);
      if (ps.display === 'none' || ps.visibility === 'hidden') return null;
      const ov = ps.overflow + ' ' + ps.overflowX + ' ' + ps.overflowY;
      if (ov.includes('hidden') || ov.includes('auto') || ov.includes('scroll') || ov.includes('clip')) {
        const pr = parent.getBoundingClientRect();
        left = Math.max(left, pr.left);
        top = Math.max(top, pr.top);
        right = Math.min(right, pr.right);
        bottom = Math.min(bottom, pr.bottom);
      }
      parent = parent.parentElement;
    }
  }

  if (right - left < 24 || bottom - top < 24) return null;
  return { left, top, right, bottom, width: right - left, height: bottom - top };
}

function computeLayerZIndex(el) {
  const overlayAncestor = el.closest ? el.closest(OVERLAY_LAYER_SELECTOR) : null;
  if (!overlayAncestor) return 90;
  let maxZ = 1050;
  let node = overlayAncestor;
  while (node && node !== document.body) {
    const z = parseInt(window.getComputedStyle(node).zIndex, 10);
    if (Number.isFinite(z) && z >= maxZ) {
      maxZ = z + 5;
    }
    node = node.parentElement;
  }
  return maxZ;
}

function resolveTopOffset(el) {
  const attr = el.getAttribute && el.getAttribute('data-overlay-top-offset');
  if (attr) {
    const num = Number(attr);
    if (Number.isFinite(num) && num >= 0) return num;
  }
  return 0;
}

export function installGlobalOverlayScrollbars() {
  if (installed || typeof window === 'undefined' || typeof document === 'undefined') {
    return;
  }
  installed = true;

  const root = document.createElement('div');
  root.className = 'cc-global-sb-root';
  root.setAttribute('aria-hidden', 'true');
  document.body.appendChild(root);

  const entries = new Map();
  let syncRaf = null;
  let scanTimer = null;
  let activeDrag = null;

  function createTrackDom(axis) {
    const track = document.createElement('div');
    track.className = `cc-gsb-track cc-gsb-track-${axis}`;
    const thumb = document.createElement('div');
    thumb.className = `cc-gsb-thumb cc-gsb-thumb-${axis}`;
    track.appendChild(thumb);
    return { track, thumb };
  }

  function removeEntry(el) {
    const entry = entries.get(el);
    if (!entry) return;
    if (entry.vTrack && entry.vTrack.parentNode) entry.vTrack.parentNode.removeChild(entry.vTrack);
    if (entry.hTrack && entry.hTrack.parentNode) entry.hTrack.parentNode.removeChild(entry.hTrack);
    if (activeDrag && activeDrag.el === el) {
      stopDrag();
    }
    entries.delete(el);
  }

  function ensureEntry(el) {
    let entry = entries.get(el);
    if (entry) return entry;

    const v = createTrackDom('v');
    const h = createTrackDom('h');

    entry = {
      el,
      vTrack: v.track,
      vThumb: v.thumb,
      hTrack: h.track,
      hThumb: h.thumb,
      vThumbSize: 0,
      hThumbSize: 0,
      vHover: false,
      hHover: false
    };

    const bindTrackEvents = (axis, track, thumb) => {
      track.addEventListener('mouseenter', () => {
        if (axis === 'v') entry.vHover = true;
        else entry.hHover = true;
        track.classList.add('is-active');
      });
      track.addEventListener('mouseleave', () => {
        if (axis === 'v') entry.vHover = false;
        else entry.hHover = false;
        if (!activeDrag || activeDrag.el !== el || activeDrag.axis !== axis) {
          track.classList.remove('is-active');
        }
      });
      track.addEventListener('mousedown', (event) => {
        if (event.button !== 0) return;
        event.preventDefault();
        event.stopPropagation();
        const rect = track.getBoundingClientRect();
        const isDoc = el === document.documentElement || el === document.body;
        if (axis === 'v') {
          const clickPos = event.clientY - rect.top - entry.vThumbSize / 2;
          const maxTravel = Math.max(rect.height - entry.vThumbSize, 1);
          const ratio = Math.min(1, Math.max(0, clickPos / maxTravel));
          const maxScroll = Math.max(el.scrollHeight - (isDoc ? window.innerHeight : el.clientHeight), 0);
          el.scrollTop = ratio * maxScroll;
        } else {
          const clickPos = event.clientX - rect.left - entry.hThumbSize / 2;
          const maxTravel = Math.max(rect.width - entry.hThumbSize, 1);
          const ratio = Math.min(1, Math.max(0, clickPos / maxTravel));
          const maxScroll = Math.max(el.scrollWidth - (isDoc ? window.innerWidth : el.clientWidth), 0);
          el.scrollLeft = ratio * maxScroll;
        }
        updateEntry(entry);
      });
      thumb.addEventListener('mousedown', (event) => {
        if (event.button !== 0) return;
        event.preventDefault();
        event.stopPropagation();
        const isDoc = el === document.documentElement || el === document.body;
        const trackLen = axis === 'v' ? track.clientHeight : track.clientWidth;
        const thumbLen = axis === 'v' ? entry.vThumbSize : entry.hThumbSize;
        activeDrag = {
          el,
          entry,
          axis,
          track,
          startClient: axis === 'v' ? event.clientY : event.clientX,
          startScroll: axis === 'v' ? el.scrollTop : el.scrollLeft,
          trackSpan: Math.max(trackLen - thumbLen, 1),
          maxScroll:
            axis === 'v'
              ? Math.max(el.scrollHeight - (isDoc ? window.innerHeight : el.clientHeight), 0)
              : Math.max(el.scrollWidth - (isDoc ? window.innerWidth : el.clientWidth), 0)
        };
        track.classList.add('is-active', 'is-dragging');
        document.body.style.userSelect = 'none';
        window.addEventListener('mousemove', handleWindowMouseMove);
        window.addEventListener('mouseup', stopDrag);
      });
    };

    bindTrackEvents('v', v.track, v.thumb);
    bindTrackEvents('h', h.track, h.thumb);

    root.appendChild(v.track);
    root.appendChild(h.track);
    entries.set(el, entry);
    return entry;
  }

  function handleWindowMouseMove(event) {
    if (!activeDrag) return;
    const { el, entry, axis, startClient, startScroll, trackSpan, maxScroll } = activeDrag;
    const currentClient = axis === 'v' ? event.clientY : event.clientX;
    const deltaPx = currentClient - startClient;
    const deltaScroll = (deltaPx / trackSpan) * maxScroll;
    const nextScroll = Math.min(maxScroll, Math.max(0, startScroll + deltaScroll));
    if (axis === 'v') {
      el.scrollTop = nextScroll;
    } else {
      el.scrollLeft = nextScroll;
    }
    updateEntry(entry);
  }

  function stopDrag() {
    if (!activeDrag) return;
    const { entry, axis, track } = activeDrag;
    track.classList.remove('is-dragging');
    const stillHover = axis === 'v' ? entry.vHover : entry.hHover;
    if (!stillHover) {
      track.classList.remove('is-active');
    }
    activeDrag = null;
    document.body.style.userSelect = '';
    window.removeEventListener('mousemove', handleWindowMouseMove);
    window.removeEventListener('mouseup', stopDrag);
  }

  function updateEntry(entry) {
    const el = entry.el;
    if (!document.contains(el)) {
      removeEntry(el);
      return;
    }

    const axes = getScrollableAxes(el);
    if (!axes.v && !axes.h) {
      removeEntry(el);
      return;
    }

    const rect = getClippedRect(el);
    if (!rect) {
      entry.vTrack.style.display = 'none';
      entry.hTrack.style.display = 'none';
      return;
    }

    const zIndex = String(computeLayerZIndex(el));
    const isDoc = el === document.documentElement || el === document.body;
    const clientH = isDoc ? window.innerHeight : el.clientHeight;
    const clientW = isDoc ? window.innerWidth : el.clientWidth;
    const topOffset = resolveTopOffset(el);
    const cornerPad = axes.v && axes.h ? 14 : 4;

    if (axes.v) {
      const trackTop = rect.top + topOffset + 4;
      const trackHeight = Math.max(rect.bottom - trackTop - cornerPad, 24);
      const thumbLen = Math.min(trackHeight, Math.max(MIN_THUMB_SIZE, (clientH / el.scrollHeight) * trackHeight));
      const maxScroll = Math.max(el.scrollHeight - clientH, 1);
      const maxTravel = Math.max(trackHeight - thumbLen, 0);
      const ratio = Math.min(1, Math.max(0, el.scrollTop / maxScroll));

      entry.vThumbSize = thumbLen;
      entry.vTrack.style.display = 'block';
      entry.vTrack.style.zIndex = zIndex;
      entry.vTrack.style.top = `${trackTop}px`;
      entry.vTrack.style.left = `${rect.right - TRACK_HIT_SIZE}px`;
      entry.vTrack.style.height = `${trackHeight}px`;
      entry.vThumb.style.height = `${thumbLen}px`;
      entry.vThumb.style.transform = `translateY(${ratio * maxTravel}px)`;
    } else {
      entry.vTrack.style.display = 'none';
    }

    if (axes.h) {
      const trackLeft = rect.left + 4;
      const trackWidth = Math.max(rect.right - trackLeft - cornerPad, 24);
      const thumbLen = Math.min(trackWidth, Math.max(MIN_THUMB_SIZE, (clientW / el.scrollWidth) * trackWidth));
      const maxScroll = Math.max(el.scrollWidth - clientW, 1);
      const maxTravel = Math.max(trackWidth - thumbLen, 0);
      const ratio = Math.min(1, Math.max(0, el.scrollLeft / maxScroll));

      entry.hThumbSize = thumbLen;
      entry.hTrack.style.display = 'block';
      entry.hTrack.style.zIndex = zIndex;
      entry.hTrack.style.left = `${trackLeft}px`;
      entry.hTrack.style.top = `${rect.bottom - TRACK_HIT_SIZE}px`;
      entry.hTrack.style.width = `${trackWidth}px`;
      entry.hThumb.style.width = `${thumbLen}px`;
      entry.hThumb.style.transform = `translateX(${ratio * maxTravel}px)`;
    } else {
      entry.hTrack.style.display = 'none';
    }
  }

  function syncAll() {
    syncRaf = null;
    entries.forEach((entry) => updateEntry(entry));
  }

  function scheduleSync() {
    if (syncRaf !== null) return;
    syncRaf = requestAnimationFrame(syncAll);
  }

  function scanScrollables() {
    scanTimer = null;
    const seen = new Set();

    const docEl = document.scrollingElement || document.documentElement;
    if (docEl) {
      const docAxes = getScrollableAxes(docEl);
      if (docAxes.v || docAxes.h) {
        seen.add(docEl);
        updateEntry(ensureEntry(docEl));
      }
    }

    const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT);
    let node = walker.nextNode();
    while (node) {
      if (node === root) {
        node = walker.nextSibling();
        continue;
      }
      if (!shouldSkipElement(node)) {
        const hasOverflow = node.scrollHeight - node.clientHeight > 2 || node.scrollWidth - node.clientWidth > 2;
        if (hasOverflow) {
          const axes = getScrollableAxes(node);
          if (axes.v || axes.h) {
            seen.add(node);
            updateEntry(ensureEntry(node));
          }
        }
      }
      node = walker.nextNode();
    }

    Array.from(entries.keys()).forEach((el) => {
      if (!seen.has(el)) {
        removeEntry(el);
      }
    });
  }

  function scheduleScan(delay = 80) {
    if (scanTimer !== null) {
      clearTimeout(scanTimer);
    }
    scanTimer = setTimeout(scanScrollables, delay);
  }

  // Capture-phase scroll listener: instant 60fps sync on ANY scrolling element
  document.addEventListener(
    'scroll',
    (event) => {
      const target = event.target === document ? document.scrollingElement || document.documentElement : event.target;
      if (!target || target.nodeType !== 1 || shouldSkipElement(target)) return;
      if (entries.has(target)) {
        updateEntry(entries.get(target));
        // Nested scroll containers may shift positions of descendant scrollbars
        scheduleSync();
      } else {
        const axes = getScrollableAxes(target);
        if (axes.v || axes.h) {
          updateEntry(ensureEntry(target));
        }
      }
    },
    { capture: true, passive: true }
  );

  window.addEventListener('resize', () => scheduleScan(60), { passive: true });
  window.addEventListener('hashchange', () => scheduleScan(120), { passive: true });

  if (typeof MutationObserver !== 'undefined') {
    const mo = new MutationObserver((mutations) => {
      for (let i = 0; i < mutations.length; i++) {
        const m = mutations[i];
        if (m.target === root || (m.target.closest && m.target.closest('.cc-global-sb-root'))) {
          continue;
        }
        scheduleScan(100);
        return;
      }
    });
    mo.observe(document.body, {
      childList: true,
      subtree: true,
      attributes: true,
      attributeFilter: ['class', 'style']
    });
  }

  scheduleScan(150);
}

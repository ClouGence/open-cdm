// Match ReAkh's image export width and canvas height boundary.
const EXPORT_MAX_HEIGHT = 16000;
const EXPORT_TRUNCATION_RESERVE = 96;

function enforceExportHeight(root, truncatedText) {
  if (root.scrollHeight <= EXPORT_MAX_HEIGHT) {
    return;
  }

  const rows = Array.from(root.querySelectorAll('[data-share-row]'));
  const heightToRemove = root.scrollHeight - EXPORT_MAX_HEIGHT + EXPORT_TRUNCATION_RESERVE;
  let removedHeight = 0;
  while (rows.length && removedHeight < heightToRemove) {
    const row = rows.pop();
    removedHeight += row.getBoundingClientRect().height;
    row.remove();
  }

  const notice = document.createElement('div');
  notice.dataset.shareExportTruncated = '';
  notice.textContent = truncatedText;
  notice.style.cssText =
    'margin-top:12px;padding:16px 12px 0;border-top:1px solid rgba(148,163,184,0.18);color:#6b7280;font-size:13px;line-height:1.5;text-align:center;';
  root.insertBefore(notice, root.querySelector('.ai-share-links'));

  // Recheck after adding the notice, including the retained branding and QR codes.
  while (root.scrollHeight > EXPORT_MAX_HEIGHT && rows.length) {
    rows.pop().remove();
  }
}

export async function renderAiShareImage(source, selectedIds, type, truncatedText) {
  const { snapdom } = await import('@zumer/snapdom');
  const wrapper = document.createElement('div');
  const width = window.innerWidth < 768 ? 720 : 900;
  wrapper.style.cssText = `position:fixed;left:-99999px;top:0;width:${width}px;pointer-events:none;`;
  const clone = source.cloneNode(true);
  clone.style.width = `${width}px`;
  clone.querySelectorAll('[data-share-row]').forEach((row) => {
    if (!selectedIds.includes(row.dataset.shareRow)) row.remove();
    else {
      row.removeAttribute('tabindex');
      row.querySelector('[data-share-content]').style.opacity = '1';
    }
  });
  clone.querySelectorAll('[data-share-control]').forEach((control) => control.remove());
  clone.querySelectorAll('.ai-share-row').forEach((row) => {
    row.style.paddingLeft = '0';
  });
  wrapper.appendChild(clone);
  document.body.appendChild(wrapper);
  try {
    await document.fonts.ready;
    await Promise.all(Array.from(clone.querySelectorAll('img'), (image) => image.decode()));
    // SVG image references must be embedded before the snapshot is rendered to a canvas.
    await Promise.all(
      Array.from(clone.querySelectorAll('svg image'), async (image) => {
        const response = await fetch(image.href.baseVal);
        if (!response.ok) throw new Error(`Share icon load failed: ${response.status}`);
        const blob = await response.blob();
        const dataUrl = await new Promise((resolve, reject) => {
          const reader = new FileReader();
          reader.onload = () => resolve(reader.result);
          reader.onerror = () => reject(reader.error);
          reader.readAsDataURL(blob);
        });
        const decoded = new Image();
        decoded.src = dataUrl;
        await decoded.decode();
        image.setAttribute('href', dataUrl);
        image.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', dataUrl);
      })
    );
    enforceExportHeight(clone, truncatedText);
    const snapshot = await snapdom(clone, { backgroundColor: '#ffffff', scale: 1, dpr: 1 });
    return await snapshot.toBlob({ type, quality: 0.86 });
  } finally {
    wrapper.remove();
  }
}

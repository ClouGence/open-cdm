import * as monaco from 'monaco-editor';
import { BrowserClipboardService } from 'monaco-editor/esm/vs/platform/clipboard/browser/clipboardService';
import { SyncDescriptor } from 'monaco-editor/esm/vs/platform/instantiation/common/descriptors';
import { StandaloneServices } from 'monaco-editor/esm/vs/editor/standalone/browser/standaloneServices';

class CompatibleClipboardService extends BrowserClipboardService {
  installWebKitWriteTextWorkaround() {
    if (typeof navigator.clipboard?.write !== 'function' || typeof ClipboardItem === 'undefined') return;
    super.installWebKitWriteTextWorkaround();
  }
}

// Install before the first editable, read-only or diff editor initializes the shared services.
StandaloneServices.initialize({ clipboardService: new SyncDescriptor(CompatibleClipboardService) });

// Monaco shares one theme across editable, read-only, and diff editors.
const applyTheme = () => {
  monaco.editor.setTheme(document.documentElement.getAttribute('data-theme') === 'dark' ? 'vs-dark' : 'vs');
};
applyTheme();
new MutationObserver(applyTheme).observe(document.documentElement, { attributes: true, attributeFilter: ['data-theme'] });

export { monaco };

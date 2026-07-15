const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('cgdmAPI', {
  onStatus: (callback) => ipcRenderer.on('status', (_event, message) => callback(message)),
});

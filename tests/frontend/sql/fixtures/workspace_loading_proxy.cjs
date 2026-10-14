const http = require('http');
const fs = require('fs');
const proxy = require('../../../../frontend/node_modules/http-proxy').createProxyServer({
  target: 'http://127.0.0.1:8222',
  ws: true
});
const config = process.argv[2];
if (!config) throw new Error('Provide a loading-fixture mode JSON file');
const port = Number(process.argv[3] || 6089);
const paths = [
  '/api/entry/browse/listLevels',
  '/api/entry/query/fetchDsStatusConf',
  '/api/entry/browse/listLeaf',
  '/api/entry/asynctask/dockTask/listDockTask'
];
proxy.on('error', (_, req, res) => {
  if (res && res.writeHead && !res.headersSent) res.writeHead(502);
  if (res && res.end) res.end();
});
const server = http.createServer((req, res) => {
  let mode = {};
  try {
    mode = JSON.parse(fs.readFileSync(config, 'utf8'));
  } catch {}
  const family = paths.find((p) => req.url.split('?')[0] === p);
  const delay = (family && mode.delays && mode.delays[family]) || 0;
  const timer = setTimeout(() => {
    if (res.destroyed) return;
    if (mode.failRoot && family === paths[0]) {
      res.writeHead(503);
      res.end();
      return;
    }
    proxy.web(req, res);
  }, delay);
  res.on('close', () => clearTimeout(timer));
});
server.on('upgrade', (req, socket, head) => proxy.ws(req, socket, head));
server.listen(port, '127.0.0.1', () => process.stdout.write(`Local loading fixture ready on ${port}\n`));

export function parseHostPort(value) {
  const address = String(value || '');
  if (address.includes('://') || address.includes(',') || address.includes(';')) {
    return { host: address, port: '' };
  }

  if (address.startsWith('[')) {
    const match = address.match(/^\[([^\]]+)\](?::([^:]+))?$/);
    if (match) {
      return { host: match[1], port: match[2] || '' };
    }
  }

  // A bare IPv6 address has no port delimiter; its final segment is part of the host.
  const match = address.match(/^([^:]+):([^:]+)$/);
  if (match) {
    return { host: match[1], port: match[2] };
  }
  return { host: address, port: '' };
}

export function formatHostPort(host, port) {
  if (!host || !port) {
    return host || '';
  }

  const address = String(host);
  if (address.includes('://') || address.includes(',') || address.includes(';')) {
    return address;
  }

  let hostname = address;
  if (address.startsWith('[') && address.endsWith(']')) {
    hostname = address.slice(1, -1);
  }

  if (hostname.includes(':')) {
    return `[${hostname}]:${port}`;
  }
  return `${hostname}:${port}`;
}

// Formatting changes whitespace only; literals, quoted names and comments are opaque.
export function formatHanaSql(sql) {
  const tokenPattern = /\s+|--[^\r\n]*|\/\*[\s\S]*?\*\/|\/\*|'(?:''|[^'])*'|"(?:""|[^"])*"|[\p{L}\p{N}_$#]+|[^]/gu;
  const tokens = sql.match(tokenPattern) || [];
  let result = '';
  let indent = 0;
  let newline = false;
  let whitespace = '';
  for (const token of tokens) {
    if (/^\s+$/.test(token)) {
      whitespace = token;
      continue;
    }
    if (token === "'" || token === '"' || token === '/*') {
      return null;
    }
    const word = token.toUpperCase();
    if (word === 'END') {
      indent = Math.max(0, indent - 1);
      newline = true;
    }
    if (result && (newline || whitespace.includes('\n'))) {
      result += '\n' + '    '.repeat(indent);
    } else if (result && whitespace) {
      result += ' ';
    }
    result += token;
    whitespace = '';
    newline = token === ';' || token.startsWith('--');
    if (word === 'BEGIN' || word === 'CASE') {
      indent++;
      newline = true;
    }
  }
  if (!result) {
    return sql;
  }
  // Verify the opaque token sequence, including comment boundaries, before replacing text.
  const significant = (text) => (text.match(tokenPattern) || []).filter((token) => !/^\s+$/.test(token));
  if (JSON.stringify(significant(sql)) !== JSON.stringify(significant(result))) {
    return null;
  }
  return (sql.match(/^\s*/) || [''])[0] + result + whitespace;
}

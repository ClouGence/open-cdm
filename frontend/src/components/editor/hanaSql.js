// Keep literals, delimited names and comments opaque. This lexer follows HanaLexer.g4.
function tokens(sql) {
  const pattern =
    /\s+|--[^\r\n]*|\/\*|N?'(?:''|[^'])*'|"(?:""|[^"])*"|(?:\d+\.\d+(?:[Ee][+-]?\d+)?|\d+[Ee][+-]?\d+|\d+)|[\p{L}_$#][\p{L}\p{N}_$#]*|\|\||:=|<>|!=|<=|>=|[^]/guy;
  const result = [];
  let gap = '';
  while (pattern.lastIndex < sql.length) {
    const start = pattern.lastIndex;
    const match = pattern.exec(sql);
    if (!match) return null;
    let text = match[0];
    if (/^\s+$/.test(text)) {
      gap = text;
      continue;
    }
    if (text === '/*') {
      let depth = 1;
      const comments = /\/\*|\*\//g;
      comments.lastIndex = pattern.lastIndex;
      while (depth) {
        const delimiter = comments.exec(sql);
        if (!delimiter) return null;
        depth += delimiter[0] === '/*' ? 1 : -1;
      }
      text = sql.slice(start, comments.lastIndex);
      pattern.lastIndex = comments.lastIndex;
    }
    if (text === "'" || text === '"') return null;
    result.push({ text, gap, word: text.toUpperCase(), comment: text.startsWith('--') || text.startsWith('/*') });
    gap = '';
  }
  return result;
}

const CLAUSES = new Set(['SELECT', 'FROM', 'WHERE', 'HAVING', 'LIMIT', 'OFFSET', 'UNION', 'INTERSECT', 'EXCEPT', 'VALUES']);
const OPERATORS = new Set(['=', '<>', '!=', '<=', '>=', '<', '>', '+', '-', '*', '/', '||', ':=']);
const BRANCHES = new Set(['ELSE', 'ELSEIF']);
const CONTROLS = new Set(['IF', 'WHILE', 'FOR', 'LOOP']);

export function formatHanaSql(sql) {
  const input = tokens(sql);
  if (!input) return null;
  const output = [];
  const nextWords = new Array(input.length);
  let nextWord;
  for (let i = input.length - 1; i >= 0; i--) {
    nextWords[i] = nextWord;
    if (!input[i].comment) nextWord = input[i].word;
  }
  const blocks = [];
  const parens = [];
  let indent = 0;
  let lineStart = true;
  let statementStart = true;
  let control = null;
  let clause = '';
  let closeSuffix = null;
  let createStatement = false;
  let blockHeader = false;
  const newline = () => {
    if (!lineStart && output.length) output.push('\n');
    lineStart = true;
  };
  for (let i = 0; i < input.length; i++) {
    const token = input[i];
    const { text, word } = token;
    const previous = input[i - 1];
    const next = nextWords[i];
    const top = blocks.at(-1);
    let open = null;
    let breakAfter = false;
    let continuation = false;
    if (!token.comment) {
      if (statementStart && word === 'CREATE') createStatement = true;
      if (blockHeader && ['SEQUENTIAL', 'PARALLEL', 'EXECUTION', 'AUTONOMOUS', 'TRANSACTION'].includes(word)) {
        breakAfter = ['EXECUTION', 'TRANSACTION'].includes(word);
        blockHeader = !breakAfter;
      } else if (word === 'END') {
        if (!top) return null;
        indent = top.indent;
        blocks.pop();
        newline();
        closeSuffix = ['BEGIN', 'CASE'].includes(top.kind) ? null : top.kind;
      } else if (closeSuffix) {
        if (word !== closeSuffix) return null;
        closeSuffix = null;
      } else if (BRANCHES.has(word) && top?.kind === 'IF') {
        indent = top.indent;
        newline();
        if (word === 'ELSE') {
          breakAfter = true;
          continuation = true;
        } else {
          control = 'ELSEIF';
        }
      } else if (word === 'BEGIN' || word === 'CASE') {
        if (word === 'BEGIN') newline();
        open = word;
        blockHeader = word === 'BEGIN' && ['SEQUENTIAL', 'PARALLEL', 'AUTONOMOUS'].includes(next);
        breakAfter = !blockHeader;
      } else if (word === 'WHEN' && (top?.kind === 'CASE' || ['MATCHED', 'NOT'].includes(next))) {
        newline();
      } else if (BRANCHES.has(word) && top?.kind === 'CASE') {
        newline();
      } else if (statementStart && CONTROLS.has(word)) {
        control = word;
        newline();
        if (word === 'LOOP') {
          open = word;
          control = null;
          breakAfter = true;
        }
      } else if (
        (word === 'THEN' && top?.kind !== 'CASE' && ['IF', 'ELSEIF'].includes(control)) ||
        (word === 'DO' && ['WHILE', 'FOR'].includes(control))
      ) {
        if (control === 'ELSEIF') continuation = true;
        else open = control;
        control = null;
        breakAfter = true;
      } else if (word === '(') {
        const multiline = next === 'SELECT' || next === 'WITH' || (clause === 'TABLE' && !parens.length);
        parens.push({ indent, clause, multiline });
        if (multiline) breakAfter = true;
      } else if (word === ')') {
        const parent = parens.pop();
        if (!parent) return null;
        indent = parent.indent;
        clause = parent.clause;
        if (parent.multiline) newline();
      } else if (word === ';') {
        if (control || closeSuffix) return null;
        breakAfter = true;
        clause = '';
        createStatement = false;
      } else if (word === ',' && (parens.at(-1)?.multiline || (!parens.length && ['SELECT', 'SET'].includes(clause)))) {
        breakAfter = true;
      } else if (CLAUSES.has(word) || (['GROUP', 'ORDER'].includes(word) && next === 'BY') || (word === 'SET' && !statementStart)) {
        // Function arguments (e.g. EXTRACT(... FROM ...)) stay together.
        if (!parens.length || parens.at(-1).multiline) {
          clause = word;
          newline();
        }
      } else if (word === 'TABLE' && createStatement) {
        clause = 'TABLE';
      }
    }

    if (previous?.comment && /[\r\n]/.test(token.gap)) newline();
    if (lineStart) {
      let lineIndent = indent;
      if (previous?.text === ',' && !parens.length && ['SELECT', 'SET'].includes(clause)) lineIndent++;
      output.push('    '.repeat(lineIndent));
    } else if (token.gap) {
      // A newline between adjacent SQL string literals can be significant.
      if (previous?.text.endsWith("'") && text.startsWith("'") && /[\r\n]/.test(token.gap)) output.push('\n');
      else output.push(' ');
    } else if (previous?.text === ',' || OPERATORS.has(text) || OPERATORS.has(previous?.text)) {
      output.push(' ');
    }
    output.push(text);
    lineStart = false;
    if (open) {
      blocks.push({ kind: open, indent });
      indent++;
    } else if (continuation || (word === '(' && parens.at(-1)?.multiline)) {
      indent++;
    }
    if (!token.comment) statementStart = breakAfter && word !== ',' && word !== '(';
    if (breakAfter || text.startsWith('--')) newline();
  }
  if (blocks.length || parens.length || control || closeSuffix) return null;
  if (output.at(-1) === '\n') output.pop();
  const formatted = output.join('');
  const actual = tokens(formatted);
  if (!actual || JSON.stringify(input.map((t) => t.text)) !== JSON.stringify(actual.map((t) => t.text))) return null;
  return formatted;
}

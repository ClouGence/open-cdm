const operators = {
  '+': true,
  '-': true,
  '*': true,
  '/': true,
  '%': true,
  '==': true,
  '!=': true,
  '>': true,
  '>=': true,
  '<': true,
  '<=': true,
  '&&': true,
  '||': true,
  '!': true
};

class Lexer {
  constructor(text) {
    this.text = text;
    this.index = 0;
    this.tokens = [];

    this.lex();
  }

  lex() {
    let ch;
    let ch2;

    while (this.index < this.text.length) {
      ch = this.text.charAt(this.index);

      if (isNumber(ch)) {
        this.readNumber();
      } else if (isAlpha(ch)) {
        this.tokens.push({
          text: ch,
          identifier: true
        });
        this.index++;
      } else if (ch == '(' || ch == ')') {
        this.tokens.push({
          text: ch
        });
        this.index++;
      } else if (isWhitespace(ch)) {
        this.index++;
      } else {
        ch2 = ch + this.peek(1);

        if (!operators[ch] && !operators[ch2]) {
          return;
        }

        if (operators[ch2]) {
          this.tokens.push({
            text: ch2
          });
          this.index += 2;
        } else {
          this.tokens.push({
            text: ch
          });
          this.index++;
        }
      }
    }
  }

  readNumber() {
    let number = '';
    let ch;

    while (this.index < this.text.length) {
      ch = this.text.charAt(this.index);
      if (ch === '.' || isNumber(ch)) {
        number += ch;
      } else {
        break;
      }

      this.index++;
    }

    this.tokens.push({
      text: number,
      constant: true
    });
  }

  peek(o) {
    const i = this.index + o;

    if (i < this.text.length) {
      return this.text.charAt(i);
    }

    return '';
  }
}

function isNumber(ch) {
  return ch >= '0' && ch <= '9' && typeof ch === 'string';
}

function isWhitespace(ch) {
  return ch === ' ' || ch === '\r' || ch === '\t'
    || ch === '\n' || ch === '\v' || ch === '\u00A0';
}

function isAlpha(ch) {
  return ch >= 'a' && ch <= 'z';
}

export default Lexer;

import Lexer from './lexer';

class Ast {
  constructor(text) {
    const lexer = new Lexer(text);

    this.tokens = lexer.tokens;
    this.index = 0;
  }

  peek(...s) {
    if (this.index >= this.tokens.length) {
      return;
    }

    const token = this.tokens[this.index];

    if (!s.length) {
      return token;
    }

    let t;

    s.forEach((a) => {
      if (token.text == a) {
        t = token;
      }
    });

    return t;
  }

  expect(...s) {
    const t = this.peek(...s);

    if (!t) {
      return t;
    }

    this.index++;
    return t;
  }

  expressionStatement() {
    return {
      type: 'ExpressionStatement',
      expression: this.expression()
    };
  }

  expression() {
    return this.logicalOR();
  }

  logicalOR() {
    let left = this.logicalAND();

    while (true) {
      const t = this.expect('||');

      if (!t) {
        break;
      }

      left = {
        type: 'LogicalExpression',
        operator: t.text,
        left,
        right: this.logicalAND()
      };
    }

    return left;
  }

  logicalAND() {
    let left = this.equality();

    while (true) {
      const t = this.expect('&&');

      if (!t) {
        break;
      }

      left = {
        type: 'LogicalExpression',
        operator: t.text,
        left,
        right: this.equality()
      };
    }

    return left;
  }

  equality() {
    let left = this.relational();

    while (true) {
      const t = this.expect('==', '!=');

      if (!t) {
        break;
      }

      left = {
        type: 'BinaryExpression',
        operator: t.text,
        left,
        right: this.relational()
      };
    }

    return left;
  }

  relational() {
    let left = this.additive();

    while (true) {
      const t = this.expect('<', '<=', '>', '>=');

      if (!t) {
        break;
      }

      left = {
        type: 'BinaryExpression',
        operator: t.text,
        left,
        right: this.additive()
      };
    }

    return left;
  }

  additive() {
    let left = this.multiplicative();

    while (true) {
      const t = this.expect('+', '-');

      if (!t) {
        break;
      }

      left = {
        type: 'BinaryExpression',
        operator: t.text,
        left,
        right: this.multiplicative()
      };
    }

    return left;
  }

  multiplicative() {
    let left = this.unary();

    while (true) {
      const t = this.expect('*', '/', '%');

      if (!t) {
        break;
      }

      left = {
        type: 'BinaryExpression',
        operator: t.text,
        left,
        right: this.unary()
      };
    }

    return left;
  }

  unary() {
    const t = this.expect('+', '-', '!');

    if (t) {
      return {
        type: 'UnaryExpression',
        operator: t.text,
        argument: this.unary()
      };
    }

    return this.primary();
  }

  primary() {
    const t = this.expect('(');

    if (t) {
      const e = this.expression();

      if (!e) {
        return;
      }

      const end = this.expect(')');

      if (!end) {
        throw new Error('expect a )');
      }

      return e;
    }

    const p = this.peek();

    if (!p) {
      throw new Error('not a primary expression');
    }

    if (p.identifier) {
      return this.identifier();
    }

    if (p.constant) {
      return this.constant();
    }

    throw new Error('not a primary expression');
  }

  identifier() {
    return {
      type: 'Identifier',
      name: this.expect().text
    };
  }

  constant() {
    return {
      type: 'Constant',
      value: new Number(this.expect().text)
    };
  }
}

export default Ast;

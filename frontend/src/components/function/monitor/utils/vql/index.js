import Ast from './ast';

export function calculate(text, data) {
  const ast = new Ast(text);
  const statement = ast.expressionStatement();

  return walkNumber(statement.expression, data);
}

export function isLogic(text) {
  if (text.indexOf('>') !== -1) {
    return true;
  }

  if (text.indexOf('<') !== -1) {
    return true;
  }

  if (text.indexOf('==') !== -1) {
    return true;
  }

  if (text.indexOf('!=') !== -1) {
    return true;
  }

  if (text.indexOf('&&') !== -1) {
    return true;
  }

  if (text.indexOf('||') !== -1) {
    return true;
  }

  return false;
}

export function is(text, config) {
  const ast = new Ast(text);

  const statement = ast.expressionStatement();

  config.result = walkBoolean(statement.expression, config);
}

export function format(text, mapping) {
  const ast = new Ast(text);
  const statement = ast.expressionStatement();

  return getText(statement.expression, mapping);
}

function getText(expression, mapping = {}) {
  let left; let
    right;

  switch (expression.type) {
    case 'LogicalExpression':
      left = getText(expression.left, mapping);
      right = getText(expression.right, mapping);

      return `${left} ${expression.operator} ${right}`;
    case 'BinaryExpression':
      left = getText(expression.left, mapping);
      right = getText(expression.right, mapping);

      return `${left} ${expression.operator} ${right}`;
    case 'UnaryExpression':
      const value = getText(expression.argument, mapping);

      return expression.operator + value;
    case 'Identifier':
      if (typeof mapping === 'object' && mapping[expression.name]) {
        return mapping[expression.name];
      }

      return expression.name;
    case 'Constant':
      return expression.value.toString();
  }
}

function walkNumber(expression, data) {
  switch (expression.type) {
    case 'BinaryExpression':
      const left = walkNumber(expression.left, data);

      const right = walkNumber(expression.right, data);

      switch (expression.operator) {
        case '+':
          return left + right;
        case '-':
          return left - right;
        case '-':
          return left - right;
        case '*':
          return left * right;
        case '/':
          if (right === 0) {
            return 0;
          }
          return left / right;
        case '%':
          return left % right;
      }
    case 'UnaryExpression':
      const value = walkNumber(expression.argument, data);

      if (expression.operator == '-') {
        return -value;
      }

      return value;
    case 'Identifier':
      return data[expression.name];
    case 'Constant':
      return expression.value;
  }

  return 0;
}

function walkBoolean(expression, config) {
  let left; let
    right;

  switch (expression.type) {
    case 'LogicalExpression':
      left = walkBoolean(expression.left, config);
      right = walkBoolean(expression.right, config);

      if (expression.operator == '&&') {
        return left && right;
      }

      if (expression.operator == '||') {
        return left || right;
      }
    case 'BinaryExpression':
      left = walkNumber(expression.left, config.data);
      right = walkNumber(expression.right, config.data);

      config.results = config.results || [];
      config.results.push({
        value: left,
        metric: getText(expression.left, config.mapping),
        expression: expression.operator,
        threshold: right
      });

      if (expression.operator == '>') {
        return left > right;
      }

      if (expression.operator == '<') {
        return left < right;
      }

      if (expression.operator == '>=') {
        return left >= right;
      }

      if (expression.operator == '<=') {
        return left <= right;
      }

      if (expression.operator == '==') {
        return left == right;
      }

      if (expression.operator == '!=') {
        return left != right;
      }
    case 'UnaryExpression':
      if (expression.operator == '!') {
        const value = walkBoolean(expression.argument, config);

        return !value;
      }
  }

  return false;
}

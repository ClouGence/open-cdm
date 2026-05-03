// Define a grammar called Hello
lexer grammar TypeProcessorDSLLexer;

/* skip spaces */
WS      : [ \t\n\r\f]+          -> skip ; // skip spaces, (空格\水平制表符\换行\回车\换页)
COMMENT1: '//' (~[\n\r])* EOL?  -> skip ;
COMMENT2: '#' (~[\n\r])* EOL?  -> skip ;
COMMENT3: '/*' .*? '*/';
EOL     : [\n\r\f];

/* key words */
DEFINE  : 'define';
ALIAS   : 'alias';
FOLLOW  : 'follow';
THROW   : 'throw';
TRUE    : 'true';
FALSE   : 'false';
NULL    : 'null';

/* assist words 连接符在某些特定场景下使用 */
SET     : '=';      // 设置
APPEND  : '+=';     // 追加设置
SEM     : ';';      // ;

ALL     : '*';      // ALL
COLON   : ':';      // Object 类型中使用
COMMA   : ',';      // 参数\分割项
LBT     : '(';      // 工具函数
RBT     : ')';      // 工具函数
LSBT    : '[';      // 集合 or 类型定义
RSBT    : ']';      // 集合 or 类型定义
ENV     : '${';     // ENV
OCBR    : '{';      // Map
CCBR    : '}';      // Map

/* 字符串 */
STRING          : '"' (~["\r\n] | '""' | TRANS)* '"'
                | '\'' (~['\r\n] | '\'\'' | TRANS)* '\''
                | '`' (~['\r\n] | '``' | TRANS)* '`'
                ;
fragment TRANS  : '\\' (['"\\/bfnrt] | UNICODE);
fragment UNICODE: 'u' HEX HEX HEX HEX;
fragment HEX    : [0-9a-fA-F];

/* 数字 */
HEX_NUM         : '0' [xX] [0-9a-fA-F]+;                // 十六进制：0x12345
OCT_NUM         : '0' [oO] [0-7]+;                      // 八 进 制：0o1234567
BIT_NUM         : '0' [bB] [01]+;                       // 二 进 制：0b01010101100
SIZE            : [1-9][0-9]* (B | KB | MB) ;           // 大小
INTEGER_NUM     : '-'? [0-9]+;                          // 十进制数：-0000234 or 123
DECIMAL_NUM     : '-'? (([0-9]* '.' [0-9]+) | [1-9]+)   // 浮点数
                  ([eE] [+-]? [1-9][0-9]*)?;            // 科学计数法
fragment B      : 'b';
fragment KB     : 'k';
fragment MB     : 'm';

/* 标识符 */
IDENTIFIER      : ([_a-zA-Z] [_0-9a-zA-Z]*);

/* TYPE */
TYPE            : ((IDENTIFIER '.')+)? IDENTIFIER;
// Define a grammar called Hello
parser grammar TypeProcessorDSLParser;
options { tokenVocab = TypeProcessorDSLLexer; }
/* ----------------------------------------------------------------------------------- 语句 & 命令 */

/* 入口 */
rootInstSet : defineInst? typeSetInst* EOF;

defineInst  : DEFINE idStr (ALIAS LSBT defineAlias RSBT)? (OCBR defineConf* CCBR)?;
defineAlias : idStr+;
defineConf  : idStr SET anyValue SEM?;

typeSetInst : (typeInst SEM?)+;
typeInst    : colTypeName (colTypeConf+ | (FOLLOW flowName) | (THROW STRING))?;
colTypeName : LSBT (ALL | idStr) RSBT;
colTypeConf : idStr (SET | APPEND) anyValue;
flowName    : colTypeName; // to alias name

/*---------------------------------------------------------------------------*/
anyValue    : extValue | baseValue | listValue | objectValue | envValue | funcCall;

/* 函数 */
funcCall    : IDENTIFIER LBT ( anyValue (COMMA anyValue)* )? RBT;
/* 基本类型 */
baseValue   : STRING                                                     #stringValue    // 字符串
            | NULL                                                       #nullValue      // 空值
            | (TRUE | FALSE)                                             #booleanValue   // boolean 类型
            | (DECIMAL_NUM | INTEGER_NUM | HEX_NUM | OCT_NUM | BIT_NUM)  #numberValue    // 数字类型
            | TYPE                                                       #typeValue      // 类型
            ;
/* 扩展类型 */
extValue    : SIZE | IDENTIFIER;
/* 环境变量 */
envValue    : ENV idStr CCBR;
/* 列表结构 */
listValue   : LSBT (anyValue+ (COMMA anyValue)*)? RSBT;
/* 对象结构 */
objectValue : OCBR (objectItem+ ( COMMA objectItem)*)? CCBR;
objectItem  : idStr COLON anyValue;
/* ID\类型\字符串 */
idStr       : STRING | TYPE | IDENTIFIER;

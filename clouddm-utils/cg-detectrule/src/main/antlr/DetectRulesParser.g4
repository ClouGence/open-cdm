// Define a grammar called Hello
parser grammar DetectRulesParser;

options {
    tokenVocab = DetectRulesLexer;
    superClass = DetectRulesBasicParser;
    //exportMacro = PARSERS_PUBLIC_TYPE;
}

@parser::header {
import java.util.List;
import com.clougence.detectrule.parser.DetectRulesFeature;
import static com.clougence.detectrule.parser.DetectRulesFeature.*;
}

@parser::members {
    public DetectRulesParser(TokenStream input, List<DetectRulesFeature> features) {
        this(input);
        super.initFeatures(features);
    }
}

root            : defineStatement* multiStatement? EOF;

comment         : COMMENT1 | COMMENT3;

multiStatement  : singleStatement+;

singleStatement : ifStatement
                | returnStatement
                | throwStatement
                | assertStatement
                | assignStatement
                ;

/* @define */
defineStatement : WELL DEFINE PRI_STR (AS defineType)? defineDefault? defineEnum? defineHINT?;
defineType      : INTEGER | FLOAT | DECIMAL | BOOL | STRING | DATE | TIME | DATETIME;
defineDefault   : DEFAULT PRI_STR;
defineEnum      : ENUM LSBT (PRI_STR (COMMA PRI_STR)*)? RSBT;
defineHINT      : HINT PRI_STR;

/* if ... then ... */
ifStatement     : conditionThen elseifThen* elseThen? END;
conditionThen   : IF expression THEN multiStatement?;
elseifThen      : ELSEIF expression THEN multiStatement?;
elseThen        : ELSE multiStatement?;

/* return ... */
returnStatement : RETURN expression?;

/* throw ... */
throwStatement  : THROW expression;

/* assert ... */
assertStatement : ASSERT expression assertExpr?;
assertExpr      : COMMA expression;

/* abc = ... */
assignStatement : IDENTIFIER ASSIGN expression;

// ----------------------------------------------------------------------------
expression      : anyPrimary                                    #atomExpr
                | paramExpr                                     #accessParamExpr
                | funOrDetectExpr                               #funcOrDetectExpr
                | LBT expression RBT                            #privilegeExpr
                | <assoc=right> AO_INCR expression              #unaryBeforeIncreExpr
                | <assoc=right> AO_DECR expression              #unaryBeforeDecrExpr
                | expression AO_INCR                            #unaryAfterIncreExpr
                | expression AO_DECR                            #unaryAfterDecrExpr
                | <assoc=right> (LO_NOT | BO_NOT) expression    #unaryNotPlusExpr
                | <assoc=right> unarySymbol expression          #unaryExpr
                | expression multiplicativeSymbol expression    #multiplicativeExpr
                | expression additiveSymbol expression          #additiveExpr
                | expression bitwiseSymbol expression           #bitwiseExpr
                | expression shiftSymbol expression             #shiftExpr
                | expression compareSymbol expression           #compareExpr
                | expression NOT? matchSymbol expression        #matchExpr
                | expression LO_AND expression                  #logicAndExpr
                | expression LO_OR expression                   #logicOrExpr
                | expression QUE expression COLON expression    #ternaryExpr
                ;

unarySymbol         : AO_PLUS | AO_MINUS | NOT;
multiplicativeSymbol: AO_MUL | AO_DIV | AO_MOD;
additiveSymbol      : AO_PLUS | AO_MINUS;
bitwiseSymbol       : BO_AND | BO_OR | BO_XOR;
shiftSymbol         : BO_RSHIFT2 | BO_RSHIFT | BO_LSHIFT;
compareSymbol       : CO_GE | CO_GT | CO_LE | CO_LT | CO_EQ | CO_IEQ | CO_NE;
matchSymbol         : IN | MATCHES;

paramExpr       : OREP (identify | PRI_STR) CCBR;

funOrDetectExpr : detectPart funcPart?;
detectPart      : REF identifyExpr;
funcPart        : LBT funcArgPart? RBT ((DOT identifyExpr) | subExpr)?;
funcArgPart     : expression (COMMA expression)*;


identifyExpr    : identifyPart subExpr?;
subExpr         : LSBT expression RSBT (subExpr | DOT identifyExpr)?;

identifyPart    : identify (DOT identify)*;
identify        : IDENTIFIER | PRI_IDENTIFIER | allIdentifierKeywords;

// --------------------------------------------------------------------------------------
anyPrimary      : funOrDetectExpr | primitiveValue | collectionValue | castExpr | identifyExpr;

primitiveValue  : (TRUE | FALSE)        #booleanValue
                | signSymbol? numValue  #numberValue
                | PRI_XOB_NUM           #xobValue
                | PRI_STR               #stringValue
                | dtValue               #datetimeValue
                | NULL                  #nullVaue
                ;
signSymbol      : AO_PLUS | AO_MINUS;

numValue        : (PRI_FLOAT | PRI_DECIMAL | (PRI_INTEGER DOT)) #floatNumValuePart
                | PRI_INTEGER                                   #intNumValuePart
                ;
dtValue         : PRI_DATETIME_FMT PRI_STR                      #fmtDateTimeValuePart
                | PRI_DATETIME                                  #dateTimeValueValuePart
                ;

collectionValue : listValue | objectValue;
listValue       : LSBT (anyPrimary (COMMA anyPrimary)*)? RSBT;

objectValue     : OCBR (objectItem (COMMA objectItem)*)? CCBR;
objectItem      : PRI_STR COLON anyPrimary;

castExpr        : CAST LBT expression AS dataType RBT
                ;

/* data Type */
dataType        : (INTEGER | FLOAT | DECIMAL) numFmtValue?  #castNum
                | (BOOL | STRING | DATE | TIME | DATETIME)  #castOther
                | PRI_DATETIME_FMT                          #castDtFmt
                ;

numFmtValue     : LBT PRI_INTEGER (COMMA numFmtRounding)? RBT;
numFmtRounding  : UP | DOWN | CEILING | FLOOR | HALF_UP | HALF_DOWN | HALF_EVEN | UNNECESSARY;

allIdentifierKeywords: DEFINE | DEFAULT | ENUM | HINT | CAST | CEILING | DOWN | FLOOR | HALF_EVEN | HALF_DOWN | HALF_UP | UP | UNNECESSARY |
                       BOOL | DATETIME | DATE | DECIMAL | FLOAT | INTEGER | STRING | TIME | INSTANCEOF | TYPEOF
                       LABEL | GOTO | SWITCH | CASE | FOR | CONTINUE | BREAK | VAR | DEF | FUNC;

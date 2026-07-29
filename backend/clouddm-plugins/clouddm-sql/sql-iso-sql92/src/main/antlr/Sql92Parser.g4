/*
 * Auto-generated from BNF grammar by bnf2antlr.py.
 * Do not edit by hand; regenerate from the .bnf source.
 */
parser grammar Sql92Parser;

options { tokenVocab=Sql92Lexer; }

root
    : sqlScript EOF
    ;

sqlScript
    : (sqlStatement SEMI?)*
    ;

sqlStatement
    : directSqlStatement
    | sqlProcedureStatement
    ;

// <exact numeric literal>
exactNumericLiteral
    : NUMBER (DOT (NUMBER)?)?
    | DOT NUMBER
    ;

// <approximate numeric literal>
approximateNumericLiteral
    : mantissa T__0 exponent
    ;

// <mantissa>
mantissa
    : exactNumericLiteral
    ;

// <exponent>
exponent
    : signedInteger
    ;

// <signed integer>
signedInteger
    : (sign)? NUMBER
    ;

// <sign>
sign
    : T__1
    | T__2
    ;

// <comment character>
commentCharacter
    : nonquoteCharacter
    | T__3
    ;

// <character set specification>
characterSetSpecification
    : standardCharacterRepertoireName
    | implementation_definedCharacterRepertoireName
    | user_definedCharacterRepertoireName
    | standardUniversalCharacterForm_of_useName
    | implementation_definedUniversalCharacterForm_of_useName
    ;

// <standard character repertoire name>
standardCharacterRepertoireName
    : IDENTIFIER
    ;

// <actual identifier>
actualIdentifier
    : IDENTIFIER
    | IDENTIFIER
    ;

// <unqualified schema name>
unqualifiedSchemaName
    : IDENTIFIER
    ;

// <implementation-defined character repertoire name>
implementation_definedCharacterRepertoireName
    : IDENTIFIER
    ;

// <user-defined character repertoire name>
user_definedCharacterRepertoireName
    : IDENTIFIER
    ;

// <standard universal character form-of-use name>
standardUniversalCharacterForm_of_useName
    : IDENTIFIER
    ;

// <implementation-defined universal character form-of-use name>
implementation_definedUniversalCharacterForm_of_useName
    : IDENTIFIER
    ;

// <date string>
dateString
    : T__3 dateValue T__3
    ;

// <date value>
dateValue
    : yearsValue T__2 monthsValue T__2 daysValue
    ;

// <years value>
yearsValue
    : datetimeValue
    ;

// <datetime value>
datetimeValue
    : NUMBER
    ;

// <months value>
monthsValue
    : datetimeValue
    ;

// <days value>
daysValue
    : datetimeValue
    ;

// <time string>
timeString
    : T__3 timeValue (timeZoneInterval)? T__3
    ;

// <time value>
timeValue
    : hoursValue T__4 minutesValue T__4 secondsValue
    ;

// <hours value>
hoursValue
    : datetimeValue
    ;

// <minutes value>
minutesValue
    : datetimeValue
    ;

// <seconds value>
secondsValue
    : secondsIntegerValue (DOT (secondsFraction)?)?
    ;

// <seconds integer value>
secondsIntegerValue
    : NUMBER
    ;

// <seconds fraction>
secondsFraction
    : NUMBER
    ;

// <time zone interval>
timeZoneInterval
    : sign hoursValue T__4 minutesValue
    ;

// <timestamp string>
timestampString
    : T__3 dateValue timeValue (timeZoneInterval)? T__3
    ;

// <interval string>
intervalString
    : T__3 (year_monthLiteral | day_timeLiteral) T__3
    ;

// <year-month literal>
year_monthLiteral
    : yearsValue
    | (yearsValue T__2)? monthsValue
    ;

// <day-time literal>
day_timeLiteral
    : day_timeInterval
    | IDENTIFIER
    ;

// <day-time interval>
day_timeInterval
    : daysValue (hoursValue (T__4 minutesValue (T__4 secondsValue)?)?)?
    ;

// <time-interval>
time_interval
    : hoursValue (T__4 minutesValue (T__4 secondsValue)?)?
    | minutesValue (T__4 secondsValue)?
    | secondsValue
    ;

// <not equals operator>
notEqualsOperator
    : IDENTIFIER
    ;

// <greater than or equals operator>
greaterThanOrEqualsOperator
    : IDENTIFIER
    ;

// <less than or equals operator>
lessThanOrEqualsOperator
    : IDENTIFIER
    ;

// <double period>
doublePeriod
    : IDENTIFIER
    ;

// <module>
module
    : IDENTIFIER languageClause moduleAuthorizationClause (temporaryTableDeclaration)? moduleContents
    ;

// <module character set specification>
moduleCharacterSetSpecification
    : T__5 T__6 characterSetSpecification
    ;

// <language clause>
languageClause
    : T__7 languageName
    ;

// <language name>
languageName
    : T__8
    | T__9
    | T__10
    | T__11
    | T__12
    | T__13
    | T__14
    ;

// <module authorization clause>
moduleAuthorizationClause
    : T__15 schemaName
    | T__16 moduleAuthorizationIdentifier
    | T__15 schemaName T__16 moduleAuthorizationIdentifier
    ;

// <module authorization identifier>
moduleAuthorizationIdentifier
    : IDENTIFIER
    ;

// <temporary table declaration>
temporaryTableDeclaration
    : T__17 T__18 T__19 T__20 qualifiedLocalTableName tableElementList (T__21 T__22 (T__23 | T__24) T__25)?
    ;

// <qualified local table name>
qualifiedLocalTableName
    : T__26 DOT tableName
    ;

// <table element list>
tableElementList
    : LEFT_PAREN tableElement (COMMA tableElement)* RIGHT_PAREN
    ;

// <table element>
tableElement
    : columnDefinition
    | tableConstraintDefinition
    ;

// <column definition>
columnDefinition
    : columnName (dataType | domainName) (defaultClause)? (columnConstraintDefinition)? (collateClause)?
    ;

// <data type>
dataType
    : characterStringType (T__27 T__28 characterSetSpecification)?
    | nationalCharacterStringType
    | bitStringType
    | numericType
    | datetimeType
    | intervalType
    ;

// <character string type>
characterStringType
    : T__27 (LEFT_PAREN length RIGHT_PAREN)?
    | T__29 (LEFT_PAREN length RIGHT_PAREN)?
    | T__27 T__30 (LEFT_PAREN length RIGHT_PAREN)?
    | T__29 T__30 (LEFT_PAREN length RIGHT_PAREN)?
    | T__31 (LEFT_PAREN length RIGHT_PAREN)?
    ;

// <length>
length
    : NUMBER
    ;

// <national character string type>
nationalCharacterStringType
    : T__32 T__27 (LEFT_PAREN length RIGHT_PAREN)?
    | T__32 T__29 (LEFT_PAREN length RIGHT_PAREN)?
    | T__33 (LEFT_PAREN length RIGHT_PAREN)?
    | T__32 T__27 T__30 (LEFT_PAREN length RIGHT_PAREN)?
    | T__32 T__29 T__30 (LEFT_PAREN length RIGHT_PAREN)?
    | T__33 T__30 (LEFT_PAREN length RIGHT_PAREN)?
    ;

// <numeric type>
numericType
    : exactNumericType
    | approximateNumericType
    ;

// <exact numeric type>
exactNumericType
    : T__34 (LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN)?
    | T__35 (LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN)?
    | T__36 (LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN)?
    | T__37
    | T__38
    | T__39
    ;

// <precision>
precision
    : NUMBER
    ;

// <scale>
scale
    : NUMBER
    ;

// <approximate numeric type>
approximateNumericType
    : T__40 (LEFT_PAREN precision RIGHT_PAREN)?
    | T__41
    | T__42 T__43
    ;

// <datetime type>
datetimeType
    : T__44
    | T__45 (LEFT_PAREN timePrecision RIGHT_PAREN)? (T__46 T__45 T__47)?
    | T__48 (LEFT_PAREN timestampPrecision RIGHT_PAREN)? (T__46 T__45 T__47)?
    ;

// <time precision>
timePrecision
    : timeFractionalSecondsPrecision
    ;

// <time fractional seconds precision>
timeFractionalSecondsPrecision
    : NUMBER
    ;

// <timestamp precision>
timestampPrecision
    : timeFractionalSecondsPrecision
    ;

// <interval type>
intervalType
    : T__49 intervalQualifier
    ;

// <interval qualifier>
intervalQualifier
    : startField T__50 endField
    | singleDatetimeField
    ;

// <start field>
startField
    : non_secondDatetimeField (LEFT_PAREN intervalLeadingFieldPrecision RIGHT_PAREN)?
    ;

// <non-second datetime field>
non_secondDatetimeField
    : T__51
    | T__52
    | T__53
    | T__54
    | T__55
    ;

// <interval leading field precision>
intervalLeadingFieldPrecision
    : NUMBER
    ;

// <end field>
endField
    : non_secondDatetimeField
    | T__56 (LEFT_PAREN intervalFractionalSecondsPrecision RIGHT_PAREN)?
    ;

// <interval fractional seconds precision>
intervalFractionalSecondsPrecision
    : NUMBER
    ;

// <single datetime field>
singleDatetimeField
    : non_secondDatetimeField (LEFT_PAREN intervalLeadingFieldPrecision RIGHT_PAREN)?
    | T__56 (LEFT_PAREN intervalLeadingFieldPrecision (COMMA intervalFractionalSecondsPrecision)? RIGHT_PAREN)?
    ;

// <qualified name>
qualifiedName
    : (schemaName DOT)? IDENTIFIER
    ;

// <default clause>
defaultClause
    : T__57 defaultOption
    ;

// <default option>
defaultOption
    : literal
    | datetimeValueFunction
    | T__58
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__59
    ;

// <literal>
literal
    : signedNumericLiteral
    | generalLiteral
    ;

// <signed numeric literal>
signedNumericLiteral
    : (sign)? NUMBER
    ;

// <general literal>
generalLiteral
    : STRING
    | STRING
    | STRING
    | STRING
    | STRING
    | STRING
    ;

// <datetime value function>
datetimeValueFunction
    : currentDateValueFunction
    | currentTimeValueFunction
    | currentTimestampValueFunction
    ;

// <current date value function>
currentDateValueFunction
    : IDENTIFIER
    ;

// <current time value function>
currentTimeValueFunction
    : IDENTIFIER (LEFT_PAREN timePrecision RIGHT_PAREN)?
    ;

// <current timestamp value function>
currentTimestampValueFunction
    : IDENTIFIER (LEFT_PAREN timestampPrecision RIGHT_PAREN)?
    ;

// <column constraint definition>
columnConstraintDefinition
    : (constraintNameDefinition)? columnConstraint (constraintAttributes)?
    ;

// <constraint name definition>
constraintNameDefinition
    : T__60 constraintName
    ;

// <column constraint>
columnConstraint
    : T__61 T__59
    | uniqueSpecification
    | referencesSpecification
    | checkConstraintDefinition
    ;

// <unique specification>
uniqueSpecification
    : T__62
    | T__63 T__64
    ;

// <references specification>
referencesSpecification
    : T__65 referencedTableAndColumns (T__66 matchType)? (referentialTriggeredAction)?
    ;

// <referenced table and columns>
referencedTableAndColumns
    : tableName (LEFT_PAREN referenceColumnList RIGHT_PAREN)?
    ;

// <reference column list>
referenceColumnList
    : columnNameList
    ;

// <match type>
matchType
    : T__67
    | T__68
    ;

// <referential triggered action>
referentialTriggeredAction
    : updateRule (deleteRule)?
    | deleteRule (updateRule)?
    ;

// <update rule>
updateRule
    : T__21 T__69 referentialAction
    ;

// <referential action>
referentialAction
    : T__70
    | T__28 T__59
    | T__28 T__57
    | T__71 T__72
    ;

// <delete rule>
deleteRule
    : T__21 T__24 referentialAction
    ;

// <check constraint definition>
checkConstraintDefinition
    : T__73 LEFT_PAREN searchCondition RIGHT_PAREN
    ;

// <search condition>
searchCondition
    : booleanTerm
    | searchCondition T__74 booleanTerm
    ;

// <boolean term>
booleanTerm
    : booleanFactor
    | booleanTerm T__75 booleanFactor
    ;

// <boolean factor>
booleanFactor
    : (T__61)? booleanTest
    ;

// <boolean test>
booleanTest
    : booleanPrimary (T__76 (T__61)? truthValue)?
    ;

// <boolean primary>
booleanPrimary
    : predicate
    | LEFT_PAREN searchCondition RIGHT_PAREN
    ;

// <predicate>
predicate
    : comparisonPredicate
    | betweenPredicate
    | inPredicate
    | likePredicate
    | nullPredicate
    | quantifiedComparisonPredicate
    | existsPredicate
    | matchPredicate
    | overlapsPredicate
    ;

// <comparison predicate>
comparisonPredicate
    : rowValueConstructor compOp rowValueConstructor
    ;

// <row value constructor>
rowValueConstructor
    : rowValueConstructorElement
    | LEFT_PAREN rowValueConstructorList RIGHT_PAREN
    | rowSubquery
    ;

// <row value constructor element>
rowValueConstructorElement
    : valueExpression
    | nullSpecification
    | defaultSpecification
    ;

// <value expression>
valueExpression
    : numericValueExpression
    | stringValueExpression
    | datetimeValueExpression
    | intervalValueExpression
    ;

// <numeric value expression>
numericValueExpression
    : term
    | numericValueExpression T__1 term
    | numericValueExpression T__2 term
    ;

// <term>
term
    : factor
    | term T__77 factor
    | term T__78 factor
    ;

// <factor>
factor
    : (sign)? numericPrimary
    ;

// <numeric primary>
numericPrimary
    : valueExpressionPrimary
    | numericValueFunction
    ;

// <value expression primary>
valueExpressionPrimary
    : unsignedValueSpecification
    | columnReference
    | setFunctionSpecification
    | scalarSubquery
    | caseExpression
    | LEFT_PAREN valueExpression RIGHT_PAREN
    | castSpecification
    ;

// <unsigned value specification>
unsignedValueSpecification
    : unsignedLiteral
    | generalValueSpecification
    ;

// <unsigned literal>
unsignedLiteral
    : NUMBER
    | generalLiteral
    ;

// <general value specification>
generalValueSpecification
    : parameterSpecification
    | dynamicParameterSpecification
    | variableSpecification
    | T__58
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__79
    ;

// <parameter specification>
parameterSpecification
    : IDENTIFIER (indicatorParameter)?
    ;

// <indicator parameter>
indicatorParameter
    : (T__80)? IDENTIFIER
    ;

// <dynamic parameter specification>
dynamicParameterSpecification
    : T__81
    ;

// <variable specification>
variableSpecification
    : embeddedVariableName (indicatorVariable)?
    ;

// <embedded variable name>
embeddedVariableName
    : T__4 hostIdentifier
    ;

// <host identifier>
hostIdentifier
    : adaHostIdentifier
    | cHostIdentifier
    | cobolHostIdentifier
    | fortranHostIdentifier
    | mUMPSHostIdentifier
    | pascalHostIdentifier
    | pLIHostIdentifier
    ;

// <indicator variable>
indicatorVariable
    : (T__80)? embeddedVariableName
    ;

// <column reference>
columnReference
    : (qualifier DOT)? columnName
    ;

// <qualifier>
qualifier
    : tableName
    | correlationName
    ;

// <set function specification>
setFunctionSpecification
    : T__82 LEFT_PAREN T__77 RIGHT_PAREN
    | generalSetFunction
    ;

// <general set function>
generalSetFunction
    : setFunctionType LEFT_PAREN (setQuantifier)? valueExpression RIGHT_PAREN
    ;

// <set function type>
setFunctionType
    : T__83
    | T__84
    | T__85
    | T__86
    | T__82
    ;

// <set quantifier>
setQuantifier
    : T__87
    | T__88
    ;

// <scalar subquery>
scalarSubquery
    : subquery
    ;

// <subquery>
subquery
    : LEFT_PAREN non_joinQueryExpression RIGHT_PAREN
    ;

// Merged rules: non-join query term, query term
non_joinQueryTerm
    : non_joinQueryPrimary
    | non_joinQueryTerm T__89 (T__88)? (correspondingSpec)? queryPrimary
    ;

// <non-join query primary>
non_joinQueryPrimary
    : simpleTable
    | LEFT_PAREN non_joinQueryExpression RIGHT_PAREN
    ;

// <simple table>
simpleTable
    : querySpecification
    | tableValueConstructor
    | explicitTable
    ;

// <query specification>
querySpecification
    : T__90 (setQuantifier)? selectList tableExpression
    ;

// <select list>
selectList
    : T__77
    | selectSublist (COMMA selectSublist)*
    ;

// <select sublist>
selectSublist
    : derivedColumn
    | qualifier DOT T__77
    ;

// <derived column>
derivedColumn
    : valueExpression (asClause)?
    ;

// <as clause>
asClause
    : (T__91)? columnName
    ;

// <table expression>
tableExpression
    : fromClause (whereClause)? (groupByClause)? (havingClause)?
    ;

// <from clause>
fromClause
    : T__92 crossJoin (COMMA crossJoin)*
    ;

// <correlation specification>
correlationSpecification
    : (T__91)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?
    ;

// <derived column list>
derivedColumnList
    : columnNameList
    ;

// <derived table>
derivedTable
    : tableSubquery
    ;

// <table subquery>
tableSubquery
    : subquery
    ;

// Merged rules: table reference, joined table, cross join, qualified join
crossJoin
    : tableName (correlationSpecification)?
    | derivedTable correlationSpecification
    | crossJoin T__93 T__94 crossJoin
    | crossJoin (T__95)? (joinType)? T__94 crossJoin (joinSpecification)?
    ;

// <join type>
joinType
    : T__96
    | outerJoinType (T__97)?
    | T__98
    ;

// <outer join type>
outerJoinType
    : T__99
    | T__100
    | T__67
    ;

// <join specification>
joinSpecification
    : joinCondition
    | namedColumnsJoin
    ;

// <join condition>
joinCondition
    : T__21 searchCondition
    ;

// <named columns join>
namedColumnsJoin
    : T__101 LEFT_PAREN joinColumnList RIGHT_PAREN
    ;

// <join column list>
joinColumnList
    : columnNameList
    ;

// <where clause>
whereClause
    : T__102 searchCondition
    ;

// <group by clause>
groupByClause
    : T__103 T__104 groupingColumnReferenceList
    ;

// <grouping column reference list>
groupingColumnReferenceList
    : groupingColumnReference (COMMA groupingColumnReference)*
    ;

// <grouping column reference>
groupingColumnReference
    : columnReference (collateClause)?
    ;

// <collate clause>
collateClause
    : T__105 IDENTIFIER
    ;

// <having clause>
havingClause
    : T__106 searchCondition
    ;

// <table value constructor>
tableValueConstructor
    : T__107 tableValueConstructorList
    ;

// <table value constructor list>
tableValueConstructorList
    : rowValueConstructor (COMMA rowValueConstructor)*
    ;

// <explicit table>
explicitTable
    : T__20 tableName
    ;

// <corresponding spec>
correspondingSpec
    : T__108 (T__104 LEFT_PAREN correspondingColumnList RIGHT_PAREN)?
    ;

// <corresponding column list>
correspondingColumnList
    : columnNameList
    ;

// <query primary>
queryPrimary
    : non_joinQueryPrimary
    | crossJoin
    ;

// <case expression>
caseExpression
    : caseAbbreviation
    | caseSpecification
    ;

// <case abbreviation>
caseAbbreviation
    : T__109 LEFT_PAREN valueExpression COMMA valueExpression RIGHT_PAREN
    | T__110 LEFT_PAREN valueExpression (COMMA valueExpression)* RIGHT_PAREN
    ;

// <case specification>
caseSpecification
    : simpleCase
    | searchedCase
    ;

// <simple case>
simpleCase
    : T__111 caseOperand simpleWhenClause (elseClause)? T__112
    ;

// <case operand>
caseOperand
    : valueExpression
    ;

// <simple when clause>
simpleWhenClause
    : T__113 whenOperand T__114 result
    ;

// <when operand>
whenOperand
    : valueExpression
    ;

// <result>
result
    : resultExpression
    | T__59
    ;

// <result expression>
resultExpression
    : valueExpression
    ;

// <else clause>
elseClause
    : T__115 result
    ;

// <searched case>
searchedCase
    : T__111 searchedWhenClause (elseClause)? T__112
    ;

// <searched when clause>
searchedWhenClause
    : T__113 searchCondition T__114 result
    ;

// <cast specification>
castSpecification
    : T__116 LEFT_PAREN castOperand T__91 castTarget RIGHT_PAREN
    ;

// <cast operand>
castOperand
    : valueExpression
    | T__59
    ;

// <cast target>
castTarget
    : domainName
    | dataType
    ;

// <numeric value function>
numericValueFunction
    : positionExpression
    | extractExpression
    | lengthExpression
    ;

// <position expression>
positionExpression
    : T__117 LEFT_PAREN characterValueExpression T__118 characterValueExpression RIGHT_PAREN
    ;

// Merged rules: character value expression, concatenation
characterValueExpression
    : characterFactor
    | characterValueExpression concatenationOperator characterFactor
    ;

// <character factor>
characterFactor
    : characterPrimary (collateClause)?
    ;

// <character primary>
characterPrimary
    : valueExpressionPrimary
    | stringValueFunction
    ;

// <string value function>
stringValueFunction
    : characterValueFunction
    | bitValueFunction
    ;

// <character value function>
characterValueFunction
    : characterSubstringFunction
    | fold
    | form_of_useConversion
    | characterTranslation
    | trimFunction
    ;

// <character substring function>
characterSubstringFunction
    : T__119 LEFT_PAREN characterValueExpression T__92 startPosition (T__120 stringLength)? RIGHT_PAREN
    ;

// <start position>
startPosition
    : numericValueExpression
    ;

// <string length>
stringLength
    : numericValueExpression
    ;

// <fold>
fold
    : (T__121 | T__122) LEFT_PAREN characterValueExpression RIGHT_PAREN
    ;

// <form-of-use conversion>
form_of_useConversion
    : T__123 LEFT_PAREN characterValueExpression T__101 form_of_useConversionName RIGHT_PAREN
    ;

// <form-of-use conversion name>
form_of_useConversionName
    : qualifiedName
    ;

// <character translation>
characterTranslation
    : T__124 LEFT_PAREN characterValueExpression T__101 IDENTIFIER RIGHT_PAREN
    ;

// <trim function>
trimFunction
    : T__125 LEFT_PAREN trimOperands RIGHT_PAREN
    ;

// <trim operands>
trimOperands
    : ((trimSpecification)? (trimCharacter)? T__92)? trimSource
    ;

// <trim specification>
trimSpecification
    : T__126
    | T__127
    | T__128
    ;

// <trim character>
trimCharacter
    : characterValueExpression
    ;

// <trim source>
trimSource
    : characterValueExpression
    ;

// <extract expression>
extractExpression
    : T__129 LEFT_PAREN extractField T__92 extractSource RIGHT_PAREN
    ;

// <extract field>
extractField
    : datetimeField
    | timeZoneField
    ;

// <datetime field>
datetimeField
    : non_secondDatetimeField
    | T__56
    ;

// <time zone field>
timeZoneField
    : IDENTIFIER
    | IDENTIFIER
    ;

// <extract source>
extractSource
    : datetimeValueExpression
    | intervalValueExpression
    ;

// <datetime value expression>
datetimeValueExpression
    : datetimeTerm
    | intervalValueExpression T__1 datetimeTerm
    | datetimeValueExpression T__1 intervalTerm
    | datetimeValueExpression T__2 intervalTerm
    ;

// Merged rules: interval term, interval term 2
intervalTerm
    : intervalFactor
    | intervalTerm T__77 factor
    | intervalTerm T__78 factor
    | term T__77 intervalFactor
    ;

// <interval factor>
intervalFactor
    : (sign)? intervalPrimary
    ;

// <interval primary>
intervalPrimary
    : valueExpressionPrimary (intervalQualifier)?
    ;

// Merged rules: interval value expression, interval value expression 1
intervalValueExpression
    : intervalValueExpression T__1 intervalTerm1
    | intervalValueExpression T__2 intervalTerm1
    | LEFT_PAREN datetimeValueExpression T__2 datetimeTerm RIGHT_PAREN intervalQualifier
    ;

// <interval term 1>
intervalTerm1
    : intervalTerm
    ;

// <datetime term>
datetimeTerm
    : datetimeFactor
    ;

// <datetime factor>
datetimeFactor
    : datetimePrimary (timeZone)?
    ;

// <datetime primary>
datetimePrimary
    : valueExpressionPrimary
    | datetimeValueFunction
    ;

// <time zone>
timeZone
    : T__130 timeZoneSpecifier
    ;

// <time zone specifier>
timeZoneSpecifier
    : T__18
    | T__45 T__47 intervalValueExpression
    ;

// <length expression>
lengthExpression
    : charLengthExpression
    | octetLengthExpression
    | bitLengthExpression
    ;

// <char length expression>
charLengthExpression
    : (IDENTIFIER | IDENTIFIER) LEFT_PAREN stringValueExpression RIGHT_PAREN
    ;

// <string value expression>
stringValueExpression
    : characterValueExpression
    | bitValueExpression
    ;

// <octet length expression>
octetLengthExpression
    : IDENTIFIER LEFT_PAREN stringValueExpression RIGHT_PAREN
    ;

// <null specification>
nullSpecification
    : T__59
    ;

// <default specification>
defaultSpecification
    : T__57
    ;

// <row value constructor list>
rowValueConstructorList
    : rowValueConstructorElement (COMMA rowValueConstructorElement)*
    ;

// <row subquery>
rowSubquery
    : subquery
    ;

// <comp op>
compOp
    : T__131
    | notEqualsOperator
    | T__132
    | T__133
    | lessThanOrEqualsOperator
    | greaterThanOrEqualsOperator
    ;

// <between predicate>
betweenPredicate
    : rowValueConstructor (T__61)? T__134 rowValueConstructor T__75 rowValueConstructor
    ;

// <in predicate>
inPredicate
    : rowValueConstructor (T__61)? T__118 inPredicateValue
    ;

// <in predicate value>
inPredicateValue
    : tableSubquery
    | LEFT_PAREN inValueList RIGHT_PAREN
    ;

// <in value list>
inValueList
    : valueExpression (COMMA valueExpression)*
    ;

// <like predicate>
likePredicate
    : matchValue (T__61)? T__135 pattern (T__136 escapeCharacter)?
    ;

// <match value>
matchValue
    : characterValueExpression
    ;

// <pattern>
pattern
    : characterValueExpression
    ;

// <escape character>
escapeCharacter
    : characterValueExpression
    ;

// <null predicate>
nullPredicate
    : rowValueConstructor T__76 (T__61)? T__59
    ;

// <quantified comparison predicate>
quantifiedComparisonPredicate
    : rowValueConstructor compOp quantifier tableSubquery
    ;

// <quantifier>
quantifier
    : all
    | some
    ;

// <all>
all
    : T__88
    ;

// <some>
some
    : T__137
    | T__138
    ;

// <exists predicate>
existsPredicate
    : T__139 tableSubquery
    ;

// <unique predicate>
uniquePredicate
    : T__62 tableSubquery
    ;

// <match predicate>
matchPredicate
    : rowValueConstructor T__66 (T__62)? (T__68 | T__67)? tableSubquery
    ;

// <overlaps predicate>
overlapsPredicate
    : rowValueConstructor1 T__140 rowValueConstructor2
    ;

// <row value constructor 1>
rowValueConstructor1
    : rowValueConstructor
    ;

// <row value constructor 2>
rowValueConstructor2
    : rowValueConstructor
    ;

// <truth value>
truthValue
    : T__141
    | T__142
    | T__143
    ;

// <constraint attributes>
constraintAttributes
    : constraintCheckTime ((T__61)? T__144)?
    | (T__61)? T__144 (constraintCheckTime)?
    ;

// <constraint check time>
constraintCheckTime
    : T__145 T__146
    | T__145 T__147
    ;

// <table constraint definition>
tableConstraintDefinition
    : (constraintNameDefinition)? tableConstraint (constraintCheckTime)?
    ;

// <table constraint>
tableConstraint
    : uniqueConstraintDefinition
    | referentialConstraintDefinition
    | checkConstraintDefinition
    ;

// <unique constraint definition>
uniqueConstraintDefinition
    : uniqueSpecification LEFT_PAREN uniqueColumnList RIGHT_PAREN
    ;

// <unique column list>
uniqueColumnList
    : columnNameList
    ;

// <referential constraint definition>
referentialConstraintDefinition
    : T__148 T__64 LEFT_PAREN referencingColumns RIGHT_PAREN referencesSpecification
    ;

// <referencing columns>
referencingColumns
    : referenceColumnList
    ;

// <module contents>
moduleContents
    : declareCursor
    | dynamicDeclareCursor
    | procedure
    ;

// <declare cursor>
declareCursor
    : T__17 IDENTIFIER (T__149)? (T__150)? T__151 T__120 cursorSpecification
    ;

// <cursor specification>
cursorSpecification
    : non_joinQueryExpression (orderByClause)? (updatabilityClause)?
    ;

// <order by clause>
orderByClause
    : T__152 T__104 sortSpecificationList
    ;

// <sort specification list>
sortSpecificationList
    : sortSpecification (COMMA sortSpecification)*
    ;

// <sort specification>
sortSpecification
    : sortKey (collateClause)? (orderingSpecification)?
    ;

// <sort key>
sortKey
    : columnName
    | NUMBER
    ;

// <ordering specification>
orderingSpecification
    : T__153
    | T__154
    ;

// <updatability clause>
updatabilityClause
    : T__120 (T__155 T__156 | T__69 (T__157 columnNameList)?)
    ;

// <dynamic declare cursor>
dynamicDeclareCursor
    : T__17 IDENTIFIER (T__149)? (T__150)? T__151 T__120 IDENTIFIER
    ;

// <procedure>
procedure
    : T__158 procedureName parameterDeclarationList SEMI sqlProcedureStatement SEMI
    ;

// <procedure name>
procedureName
    : IDENTIFIER
    ;

// <parameter declaration list>
parameterDeclarationList
    : LEFT_PAREN parameterDeclaration (COMMA parameterDeclaration)* RIGHT_PAREN
    ;

// <parameter declaration>
parameterDeclaration
    : IDENTIFIER dataType
    | statusParameter
    ;

// <status parameter>
statusParameter
    : T__159
    | T__160
    ;

// <SQL procedure statement>
sqlProcedureStatement
    : sqlSchemaStatement
    | sqlDataStatement
    | sqlTransactionStatement
    | sqlConnectionStatement
    | sqlSessionStatement
    | sqlDynamicStatement
    | sqlDiagnosticsStatement
    ;

// <SQL schema statement>
sqlSchemaStatement
    : sqlSchemaDefinitionStatement
    | sqlSchemaManipulationStatement
    ;

// <SQL schema definition statement>
sqlSchemaDefinitionStatement
    : schemaDefinition
    | tableDefinition
    | viewDefinition
    | grantStatement
    | domainDefinition
    | characterSetDefinition
    | collationDefinition
    | translationDefinition
    | assertionDefinition
    ;

// <schema definition>
schemaDefinition
    : T__161 T__15 schemaName (schemaCharacterSetSpecification)? (schemaElement)?
    ;

// <schema authorization identifier>
schemaAuthorizationIdentifier
    : IDENTIFIER
    ;

// <schema character set specification>
schemaCharacterSetSpecification
    : T__57 T__27 T__28 characterSetSpecification
    ;

// <schema element>
schemaElement
    : domainDefinition
    | tableDefinition
    | viewDefinition
    | grantStatement
    | assertionDefinition
    | characterSetDefinition
    | collationDefinition
    | translationDefinition
    ;

// <domain definition>
domainDefinition
    : T__161 T__162 domainName (T__91)? dataType (defaultClause)? (domainConstraint)? (collateClause)?
    ;

// <domain constraint>
domainConstraint
    : (constraintNameDefinition)? checkConstraintDefinition (constraintAttributes)?
    ;

// <table definition>
tableDefinition
    : T__161 ((T__163 | T__18) T__19)? T__20 tableName tableElementList (T__21 T__22 (T__24 | T__23) T__25)?
    ;

// <view definition>
viewDefinition
    : T__161 T__164 tableName (LEFT_PAREN viewColumnList RIGHT_PAREN)? T__91 non_joinQueryExpression (T__46 (levelsClause)? T__73 T__165)?
    ;

// <view column list>
viewColumnList
    : columnNameList
    ;

// <levels clause>
levelsClause
    : T__166
    | T__18
    ;

// <grant statement>
grantStatement
    : T__167 privileges T__21 objectName T__50 grantee (COMMA grantee)* (T__46 T__167 T__165)?
    ;

// <privileges>
privileges
    : T__88 T__168
    | actionList
    ;

// <action list>
actionList
    : action (COMMA action)*
    ;

// <action>
action
    : T__90
    | T__24
    | T__169 (LEFT_PAREN privilegeColumnList RIGHT_PAREN)?
    | T__69 (LEFT_PAREN privilegeColumnList RIGHT_PAREN)?
    | T__65 (LEFT_PAREN privilegeColumnList RIGHT_PAREN)?
    | T__170
    ;

// <privilege column list>
privilegeColumnList
    : columnNameList
    ;

// <object name>
objectName
    : (T__20)? tableName
    | T__162 domainName
    | T__171 IDENTIFIER
    | T__27 T__28 IDENTIFIER
    | T__172 IDENTIFIER
    ;

// <grantee>
grantee
    : T__173
    | IDENTIFIER
    ;

// <assertion definition>
assertionDefinition
    : T__161 T__174 constraintName assertionCheck (constraintAttributes)?
    ;

// <assertion check>
assertionCheck
    : T__73 LEFT_PAREN searchCondition RIGHT_PAREN
    ;

// <character set definition>
characterSetDefinition
    : T__161 T__27 T__28 IDENTIFIER (T__91)? characterSetSource (collateClause | limitedCollationDefinition)?
    ;

// <character set source>
characterSetSource
    : T__175 existingCharacterSetName
    ;

// <existing character set name>
existingCharacterSetName
    : standardCharacterRepertoireName
    | implementation_definedCharacterRepertoireName
    | schemaCharacterSetName
    ;

// <schema character set name>
schemaCharacterSetName
    : IDENTIFIER
    ;

// <limited collation definition>
limitedCollationDefinition
    : T__171 T__92 collationSource
    ;

// <collation source>
collationSource
    : collatingSequenceDefinition
    | translationCollation
    ;

// <collating sequence definition>
collatingSequenceDefinition
    : externalCollation
    | schemaCollationName
    | T__154 LEFT_PAREN IDENTIFIER RIGHT_PAREN
    | T__57
    ;

// <external collation>
externalCollation
    : T__176 LEFT_PAREN T__3 externalCollationName T__3 RIGHT_PAREN
    ;

// <external collation name>
externalCollationName
    : standardCollationName
    | implementation_definedCollationName
    ;

// <standard collation name>
standardCollationName
    : IDENTIFIER
    ;

// <implementation-defined collation name>
implementation_definedCollationName
    : IDENTIFIER
    ;

// <schema collation name>
schemaCollationName
    : IDENTIFIER
    ;

// <translation collation>
translationCollation
    : T__172 IDENTIFIER (T__114 T__171 IDENTIFIER)?
    ;

// <collation definition>
collationDefinition
    : T__161 T__171 IDENTIFIER T__120 characterSetSpecification T__92 collationSource (padAttribute)?
    ;

// <pad attribute>
padAttribute
    : T__71 T__177
    | T__177 T__178
    ;

// <translation definition>
translationDefinition
    : T__161 T__172 IDENTIFIER T__120 sourceCharacterSetSpecification T__50 targetCharacterSetSpecification T__92 translationSource
    ;

// <source character set specification>
sourceCharacterSetSpecification
    : characterSetSpecification
    ;

// <target character set specification>
targetCharacterSetSpecification
    : characterSetSpecification
    ;

// <translation source>
translationSource
    : translationSpecification
    ;

// <translation specification>
translationSpecification
    : externalTranslation
    | T__179
    | schemaTranslationName
    ;

// <external translation>
externalTranslation
    : T__176 LEFT_PAREN T__3 externalTranslationName T__3 RIGHT_PAREN
    ;

// <external translation name>
externalTranslationName
    : standardTranslationName
    | implementation_definedTranslationName
    ;

// <standard translation name>
standardTranslationName
    : IDENTIFIER
    ;

// <implementation-defined translation name>
implementation_definedTranslationName
    : IDENTIFIER
    ;

// <schema translation name>
schemaTranslationName
    : IDENTIFIER
    ;

// <SQL schema manipulation statement>
sqlSchemaManipulationStatement
    : dropSchemaStatement
    | alterTableStatement
    | dropTableStatement
    | dropViewStatement
    | revokeStatement
    | alterDomainStatement
    | dropDomainStatement
    | dropCharacterSetStatement
    | dropCollationStatement
    | dropTranslationStatement
    | dropAssertionStatement
    ;

// <drop schema statement>
dropSchemaStatement
    : T__180 T__15 schemaName (dropBehaviour)?
    ;

// <drop behaviour>
dropBehaviour
    : T__70
    | T__181
    ;

// <alter table statement>
alterTableStatement
    : T__182 T__20 tableName alterTableAction
    ;

// <alter table action>
alterTableAction
    : addColumnDefinition
    | alterColumnDefinition
    | dropColumnDefinition
    | addTableConstraintDefinition
    | dropTableConstraintDefinition
    ;

// <add column definition>
addColumnDefinition
    : T__183 (T__184)? columnDefinition
    ;

// <alter column definition>
alterColumnDefinition
    : T__182 (T__184)? columnName alterColumnAction
    ;

// <alter column action>
alterColumnAction
    : setColumnDefaultClause
    | dropColumnDefaultClause
    ;

// <set column default clause>
setColumnDefaultClause
    : T__28 defaultClause
    ;

// <drop column default clause>
dropColumnDefaultClause
    : T__180 T__57
    ;

// <drop column definition>
dropColumnDefinition
    : T__180 (T__184)? columnName dropBehaviour
    ;

// <add table constraint definition>
addTableConstraintDefinition
    : T__183 tableConstraintDefinition
    ;

// <drop table constraint definition>
dropTableConstraintDefinition
    : T__180 T__60 constraintName dropBehaviour
    ;

// <drop table statement>
dropTableStatement
    : T__180 T__20 tableName dropBehaviour
    ;

// <drop view statement>
dropViewStatement
    : T__180 T__164 tableName dropBehaviour
    ;

// <revoke statement>
revokeStatement
    : T__185 (T__167 T__165 T__120)? privileges T__21 objectName T__92 grantee (COMMA grantee)* dropBehaviour
    ;

// <alter domain statement>
alterDomainStatement
    : T__182 T__162 domainName alterDomainAction
    ;

// <alter domain action>
alterDomainAction
    : setDomainDefaultClause
    | dropDomainDefaultClause
    | addDomainConstraintDefinition
    | dropDomainConstraintDefinition
    ;

// <set domain default clause>
setDomainDefaultClause
    : T__28 defaultClause
    ;

// <drop domain default clause>
dropDomainDefaultClause
    : T__180 T__57
    ;

// <add domain constraint definition>
addDomainConstraintDefinition
    : T__183 domainConstraint
    ;

// <drop domain constraint definition>
dropDomainConstraintDefinition
    : T__180 T__60 constraintName
    ;

// <drop domain statement>
dropDomainStatement
    : T__180 T__162 domainName dropBehaviour
    ;

// <drop character set statement>
dropCharacterSetStatement
    : T__180 T__27 T__28 IDENTIFIER
    ;

// <drop collation statement>
dropCollationStatement
    : T__180 T__171 IDENTIFIER
    ;

// <drop translation statement>
dropTranslationStatement
    : T__180 T__172 IDENTIFIER
    ;

// <drop assertion statement>
dropAssertionStatement
    : T__180 T__174 constraintName
    ;

// <SQL data statement>
sqlDataStatement
    : openStatement
    | fetchStatement
    | closeStatement
    | selectStatement_SingleRow
    | sqlDataChangeStatement
    ;

// <open statement>
openStatement
    : T__186 IDENTIFIER
    ;

// <fetch statement>
fetchStatement
    : T__187 ((fetchOrientation)? T__92)? IDENTIFIER T__188 fetchTargetList
    ;

// <fetch orientation>
fetchOrientation
    : T__189
    | T__190
    | T__191
    | T__192
    | (T__193 | T__194) simpleValueSpecification
    ;

// <simple value specification>
simpleValueSpecification
    : IDENTIFIER
    | embeddedVariableName
    | literal
    ;

// <fetch target list>
fetchTargetList
    : targetSpecification (COMMA targetSpecification)*
    ;

// <target specification>
targetSpecification
    : parameterSpecification
    | variableSpecification
    ;

// <close statement>
closeStatement
    : T__195 IDENTIFIER
    ;

// <select statement: single row>
selectStatement_SingleRow
    : T__90 (setQuantifier)? selectList T__188 selectTargetList tableExpression
    ;

// <select target list>
selectTargetList
    : targetSpecification (COMMA targetSpecification)*
    ;

// <SQL data change statement>
sqlDataChangeStatement
    : deleteStatement_Positioned
    | deleteStatement_Searched
    | insertStatement
    | updateStatement_Positioned
    | updateStatement_Searched
    ;

// <delete statement: positioned>
deleteStatement_Positioned
    : T__24 T__92 tableName T__102 T__196 T__157 IDENTIFIER
    ;

// <delete statement: searched>
deleteStatement_Searched
    : T__24 T__92 tableName (T__102 searchCondition)?
    ;

// <insert statement>
insertStatement
    : T__169 T__188 tableName insertColumnsAndSource
    ;

// <insert columns and source>
insertColumnsAndSource
    : (LEFT_PAREN insertColumnList RIGHT_PAREN)? non_joinQueryExpression
    | T__57 T__107
    ;

// <insert column list>
insertColumnList
    : columnNameList
    ;

// <update statement: positioned>
updateStatement_Positioned
    : T__69 tableName T__28 setClauseList T__102 T__196 T__157 IDENTIFIER
    ;

// <set clause list>
setClauseList
    : setClause (COMMA setClause)*
    ;

// <set clause>
setClause
    : objectColumn T__131 updateSource
    ;

// <object column>
objectColumn
    : columnName
    ;

// <update source>
updateSource
    : valueExpression
    | nullSpecification
    | T__57
    ;

// <update statement: searched>
updateStatement_Searched
    : T__69 tableName T__28 setClauseList (T__102 searchCondition)?
    ;

// <SQL transaction statement>
sqlTransactionStatement
    : setTransactionStatement
    | setConstraintsModeStatement
    | commitStatement
    | rollbackStatement
    ;

// <set transaction statement>
setTransactionStatement
    : T__28 T__197 transactionMode (COMMA transactionMode)*
    ;

// <transaction mode>
transactionMode
    : isolationLevel
    | transactionAccessMode
    | diagnosticsSize
    ;

// <isolation level>
isolationLevel
    : T__198 T__199 levelOfIsolation
    ;

// <level of isolation>
levelOfIsolation
    : T__155 T__200
    | T__155 T__201
    | T__202 T__155
    | T__203
    ;

// <transaction access mode>
transactionAccessMode
    : T__155 T__156
    | T__155 T__204
    ;

// <diagnostics size>
diagnosticsSize
    : T__205 T__206 numberOfConditions
    ;

// <number of conditions>
numberOfConditions
    : simpleValueSpecification
    ;

// <set constraints mode statement>
setConstraintsModeStatement
    : T__28 T__207 constraintNameList (T__146 | T__147)
    ;

// <commit statement>
commitStatement
    : T__22 (T__208)?
    ;

// <rollback statement>
rollbackStatement
    : T__209 (T__208)?
    ;

// <SQL connection statement>
sqlConnectionStatement
    : connectStatement
    | setConnectionStatement
    | disconnectStatement
    ;

// <connect statement>
connectStatement
    : T__210 T__50 connectionTarget
    ;

// <connection target>
connectionTarget
    : sQL_serverName (T__91 IDENTIFIER)? (T__58 userName)?
    | T__57
    ;

// <SQL-server name>
sQL_serverName
    : simpleValueSpecification
    ;

// <user name>
userName
    : simpleValueSpecification
    ;

// <set connection statement>
setConnectionStatement
    : T__28 T__211 connectionObject
    ;

// <connection object>
connectionObject
    : T__57
    | IDENTIFIER
    ;

// <disconnect statement>
disconnectStatement
    : T__212 disconnectObject
    ;

// <disconnect object>
disconnectObject
    : connectionObject
    | T__88
    | T__196
    ;

// <SQL session statement>
sqlSessionStatement
    : setCatalogStatement
    | setSchemaStatement
    | setNamesStatement
    | setSessionAuthorizationIdentifierStatement
    | setLocalTimeZoneStatement
    ;

// <set catalog statement>
setCatalogStatement
    : T__28 T__213 valueSpecification
    ;

// <value specification>
valueSpecification
    : literal
    | generalValueSpecification
    ;

// <set schema statement>
setSchemaStatement
    : T__28 T__15 valueSpecification
    ;

// <set names statement>
setNamesStatement
    : T__28 T__5 valueSpecification
    ;

// <set session authorization identifier statement>
setSessionAuthorizationIdentifierStatement
    : T__28 T__214 T__16 valueSpecification
    ;

// <set local time zone statement>
setLocalTimeZoneStatement
    : T__28 T__45 T__47 setTimeZoneValue
    ;

// <set time zone value>
setTimeZoneValue
    : intervalValueExpression
    | T__18
    ;

// <SQL dynamic statement>
sqlDynamicStatement
    : systemDescriptorStatement
    | prepareStatement
    | deallocatePreparedStatement
    | describeStatement
    | executeStatement
    | executeImmediateStatement
    | sqlDynamicDataStatement
    ;

// <system descriptor statement>
systemDescriptorStatement
    : allocateDescriptorStatement
    | deallocateDescriptorStatement
    | getDescriptorStatement
    | setDescriptorStatement
    ;

// <allocate descriptor statement>
allocateDescriptorStatement
    : T__215 T__216 IDENTIFIER (T__46 T__84 occurrences)?
    ;

// <scope option>
scopeOption
    : T__163
    | T__18
    ;

// <occurrences>
occurrences
    : simpleValueSpecification
    ;

// <deallocate descriptor statement>
deallocateDescriptorStatement
    : T__217 T__216 IDENTIFIER
    ;

// <set descriptor statement>
setDescriptorStatement
    : T__28 T__216 IDENTIFIER setDescriptorInformation
    ;

// <set descriptor information>
setDescriptorInformation
    : setCount
    | T__79 itemNumber setItemInformation (COMMA setItemInformation)*
    ;

// <set count>
setCount
    : T__82 T__131 simpleValueSpecification1
    ;

// <simple value specification 1>
simpleValueSpecification1
    : simpleValueSpecification
    ;

// <item number>
itemNumber
    : simpleValueSpecification
    ;

// <set item information>
setItemInformation
    : descriptorItemName T__131 simpleValueSpecification2
    ;

// <descriptor item name>
descriptorItemName
    : T__218
    | T__219
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__43
    | T__220
    | IDENTIFIER
    | IDENTIFIER
    | T__221
    | T__80
    | T__222
    | T__223
    | T__224
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <simple value specification 2>
simpleValueSpecification2
    : simpleValueSpecification
    ;

// <get descriptor statement>
getDescriptorStatement
    : T__175 T__216 IDENTIFIER getDescriptorInformation
    ;

// <get descriptor information>
getDescriptorInformation
    : getCount
    | T__79 itemNumber getItemInformation (COMMA getItemInformation)*
    ;

// <get count>
getCount
    : simpleTargetSpecification1 T__131 T__82
    ;

// <simple target specification 1>
simpleTargetSpecification1
    : simpleTargetSpecification
    ;

// <simple target specification>
simpleTargetSpecification
    : IDENTIFIER
    | embeddedVariableName
    ;

// <get item information>
getItemInformation
    : simpleTargetSpecification2 T__131 descriptorItemName
    ;

// <simple target specification 2>
simpleTargetSpecification2
    : simpleTargetSpecification
    ;

// <prepare statement>
prepareStatement
    : T__225 sqlStatementName T__92 sqlStatementVariable
    ;

// <SQL statement name>
sqlStatementName
    : IDENTIFIER
    | IDENTIFIER
    ;

// <SQL statement variable>
sqlStatementVariable
    : simpleValueSpecification
    ;

// <deallocate prepared statement>
deallocatePreparedStatement
    : T__217 T__225 sqlStatementName
    ;

// <describe statement>
describeStatement
    : describeInputStatement
    | describeOutputStatement
    ;

// <describe input statement>
describeInputStatement
    : T__226 T__227 sqlStatementName usingDescriptor
    ;

// <using descriptor>
usingDescriptor
    : (T__101 | T__188) T__228 T__216 IDENTIFIER
    ;

// <describe output statement>
describeOutputStatement
    : T__226 (T__229)? sqlStatementName usingDescriptor
    ;

// <execute statement>
executeStatement
    : T__230 sqlStatementName (resultUsingClause)? (parameterUsingClause)?
    ;

// <result using clause>
resultUsingClause
    : usingClause
    ;

// <using clause>
usingClause
    : usingArguments
    | usingDescriptor
    ;

// <using arguments>
usingArguments
    : (T__101 | T__188) argument (COMMA argument)*
    ;

// <argument>
argument
    : targetSpecification
    ;

// <parameter using clause>
parameterUsingClause
    : usingClause
    ;

// <execute immediate statement>
executeImmediateStatement
    : T__230 T__147 sqlStatementVariable
    ;

// <SQL dynamic data statement>
sqlDynamicDataStatement
    : allocateCursorStatement
    | dynamicOpenStatement
    | dynamicCloseStatement
    | dynamicFetchStatement
    | dynamicDeleteStatement_Positioned
    | dynamicUpdateStatement_Positioned
    ;

// <allocate cursor statement>
allocateCursorStatement
    : T__215 IDENTIFIER (T__149)? (T__150)? T__151 T__120 IDENTIFIER
    ;

// <dynamic open statement>
dynamicOpenStatement
    : T__186 dynamicCursorName (usingClause)?
    ;

// <dynamic cursor name>
dynamicCursorName
    : IDENTIFIER
    | IDENTIFIER
    ;

// <dynamic close statement>
dynamicCloseStatement
    : T__195 dynamicCursorName
    ;

// <dynamic fetch statement>
dynamicFetchStatement
    : T__187 ((fetchOrientation)? T__92)? dynamicCursorName
    ;

// <dynamic delete statement: positioned>
dynamicDeleteStatement_Positioned
    : T__24 T__92 tableName T__102 T__196 T__157 dynamicCursorName
    ;

// <dynamic update statement: positioned>
dynamicUpdateStatement_Positioned
    : T__69 tableName T__28 setClause (COMMA setClause)* T__102 T__196 T__157 dynamicCursorName
    ;

// <SQL diagnostics statement>
sqlDiagnosticsStatement
    : getDiagnosticsStatement
    ;

// <get diagnostics statement>
getDiagnosticsStatement
    : T__175 T__205 sqlDiagnosticsInformation
    ;

// <sql diagnostics information>
sqlDiagnosticsInformation
    : statementInformation
    | conditionInformation
    ;

// <statement information>
statementInformation
    : statementInformationItem (COMMA statementInformationItem)*
    ;

// <statement information item>
statementInformationItem
    : simpleTargetSpecification T__131 statementInformationItemName
    ;

// <statement information item name>
statementInformationItemName
    : T__231
    | T__232
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <condition information>
conditionInformation
    : T__233 conditionNumber conditionInformationItem (COMMA conditionInformationItem)*
    ;

// <condition number>
conditionNumber
    : simpleValueSpecification
    ;

// <condition information item>
conditionInformationItem
    : simpleTargetSpecification T__131 conditionInformationItemName
    ;

// <condition information item name>
conditionInformationItemName
    : IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <embedded SQL host program>
embeddedSqlHostProgram
    : embeddedSqlAdaProgram
    | embeddedSqlCProgram
    | embeddedSqlCobolProgram
    | embeddedSqlFortranProgram
    | embeddedSqlMUMPSProgram
    | embeddedSqlPascalProgram
    | embeddedSqlPLIProgram
    ;

// <embedded SQL declare section>
embeddedSqlDeclareSection
    : embeddedSqlBeginDeclare (embeddedCharacterSetDeclaration)? (hostVariableDefinition)? embeddedSqlEndDeclare
    | embeddedSqlMUMPSDeclare
    ;

// <embedded SQL begin declare>
embeddedSqlBeginDeclare
    : sqlPrefix T__234 T__17 T__235 (sqlTerminator)?
    ;

// <SQL prefix>
sqlPrefix
    : T__236 T__228
    | T__237 T__228 LEFT_PAREN
    ;

// <SQL terminator>
sqlTerminator
    : IDENTIFIER
    | SEMI
    | RIGHT_PAREN
    ;

// <embedded character set declaration>
embeddedCharacterSetDeclaration
    : T__228 T__5 T__6 characterSetSpecification
    ;

// <host variable definition>
hostVariableDefinition
    : adaVariableDefinition
    | cVariableDefinition
    | cobolVariableDefinition
    | fortranVariableDefinition
    | mUMPSVariableDefinition
    | pascalVariableDefinition
    | pLIVariableDefinition
    ;

// <Ada variable definition>
adaVariableDefinition
    : adaHostIdentifier (COMMA adaHostIdentifier)* T__4 adaTypeSpecification (adaInitialValue)?
    ;

// <Ada type specification>
adaTypeSpecification
    : adaQualifiedTypeSpecification
    | adaUnqualifiedTypeSpecification
    ;

// <Ada qualified type specification>
adaQualifiedTypeSpecification
    : IDENTIFIER (T__27 T__28 (T__76)? characterSetSpecification)? LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | IDENTIFIER LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <Ada unqualified type specification>
adaUnqualifiedTypeSpecification
    : T__29 LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | T__238 LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | T__39
    | T__38
    | T__41
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <Ada initial value>
adaInitialValue
    : adaAssignmentOperator STRING
    ;

// <Ada assignment operator>
adaAssignmentOperator
    : T__4 T__131
    ;

// <C variable definition>
cVariableDefinition
    : (cStorageClass)? (cClassModifier)? cVariableSpecification SEMI
    ;

// <C storage class>
cStorageClass
    : IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <C class modifier>
cClassModifier
    : IDENTIFIER
    | IDENTIFIER
    ;

// <C variable specification>
cVariableSpecification
    : cNumericVariable
    | cCharacterVariable
    | cDerivedVariable
    ;

// <C numeric variable>
cNumericVariable
    : (IDENTIFIER | IDENTIFIER | IDENTIFIER | IDENTIFIER) cHostIdentifier (cInitialValue)? (COMMA cHostIdentifier (cInitialValue)?)*
    ;

// <C initial value>
cInitialValue
    : T__131 STRING
    ;

// <C character variable>
cCharacterVariable
    : IDENTIFIER (T__27 T__28 (T__76)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? (COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*
    ;

// <C array specification>
cArraySpecification
    : T__239 length T__240
    ;

// <C derived variable>
cDerivedVariable
    : cVARCHARVariable
    | cBitVariable
    ;

// <C VARCHAR variable>
cVARCHARVariable
    : T__31 (T__27 T__28 (T__76)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? (COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*
    ;

// <C bit variable>
cBitVariable
    : T__238 cHostIdentifier cArraySpecification (cInitialValue)? (COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*
    ;

// <Cobol variable definition>
cobolVariableDefinition
    : IDENTIFIER
    ;

// <Fortran variable definition>
fortranVariableDefinition
    : IDENTIFIER
    ;

// <MUMPS variable definition>
mUMPSVariableDefinition
    : IDENTIFIER
    ;

// <Pascal variable definition>
pascalVariableDefinition
    : IDENTIFIER
    ;

// <PL/I variable definition>
pLIVariableDefinition
    : IDENTIFIER
    ;

// <embedded SQL end declare>
embeddedSqlEndDeclare
    : sqlPrefix T__112 T__17 T__235 (sqlTerminator)?
    ;

// <embedded SQL MUMPS declare>
embeddedSqlMUMPSDeclare
    : sqlPrefix T__234 T__17 T__235 (embeddedCharacterSetDeclaration)? (hostVariableDefinition)? T__112 T__17 T__235 sqlTerminator
    ;

// <embedded SQL statement>
embeddedSqlStatement
    : sqlPrefix statementOrDeclaration (sqlTerminator)?
    ;

// <statement or declaration>
statementOrDeclaration
    : declareCursor
    | dynamicDeclareCursor
    | temporaryTableDeclaration
    | embeddedExceptionDeclaration
    | sqlProcedureStatement
    ;

// <embedded exception declaration>
embeddedExceptionDeclaration
    : T__241 condition conditionAction
    ;

// <condition>
condition
    : T__242
    | T__61 T__243
    ;

// <condition action>
conditionAction
    : T__244
    | goTo
    ;

// <go to>
goTo
    : (T__245 | T__246 T__50) gotoTarget
    ;

// <goto target>
gotoTarget
    : hostLabelIdentifier
    | NUMBER
    | hostPLILabelVariable
    ;

// <preparable statement>
preparableStatement
    : preparableSqlDataStatement
    | preparableSqlSchemaStatement
    | preparableSqlTransactionStatement
    | preparableSqlSessionStatement
    | preparableSqlImplementation_definedStatement
    ;

// <preparable SQL data statement>
preparableSqlDataStatement
    : deleteStatement_Searched
    | dynamicSingleRowSelectStatement
    | insertStatement
    | dynamicSelectStatement
    | updateStatement_Searched
    | preparableDynamicDeleteStatement_Positioned
    | preparableDynamicUpdateStatement_Positioned
    ;

// <dynamic single row select statement>
dynamicSingleRowSelectStatement
    : querySpecification
    ;

// <dynamic select statement>
dynamicSelectStatement
    : cursorSpecification
    ;

// <preparable dynamic delete statement: positioned>
preparableDynamicDeleteStatement_Positioned
    : T__24 (T__92 tableName)? T__102 T__196 T__157 IDENTIFIER
    ;

// <preparable dynamic update statement: positioned>
preparableDynamicUpdateStatement_Positioned
    : T__69 (tableName)? T__28 setClause T__102 T__196 T__157 IDENTIFIER
    ;

// <preparable SQL schema statement>
preparableSqlSchemaStatement
    : sqlSchemaStatement
    ;

// <preparable SQL transaction statement>
preparableSqlTransactionStatement
    : sqlTransactionStatement
    ;

// <preparable SQL session statement>
preparableSqlSessionStatement
    : sqlSessionStatement
    ;

// <direct SQL statement>
directSqlStatement
    : directSqlDataStatement
    | sqlSchemaStatement
    | sqlTransactionStatement
    | sqlConnectionStatement
    | sqlSessionStatement
    | directImplementation_definedStatement
    ;

// <direct SQL data statement>
directSqlDataStatement
    : deleteStatement_Searched
    | directSelectStatement_MultipleRows
    | insertStatement
    | updateStatement_Searched
    | temporaryTableDeclaration
    ;

// <direct select statement: multiple rows>
directSelectStatement_MultipleRows
    : non_joinQueryExpression (orderByClause)?
    ;

// <SQL object identifier>
sqlObjectIdentifier
    : sqlProvenance sqlVariant
    ;

// <SQL provenance>
sqlProvenance
    : arc1 arc2 arc3
    ;

// <arc1>
arc1
    : IDENTIFIER
    | NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <arc2>
arc2
    : IDENTIFIER
    | NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <arc3>
arc3
    : NUMBER
    ;

// <SQL variant>
sqlVariant
    : sqlEdition sqlConformance
    ;

// <SQL edition>
sqlEdition
    : r1987
    | r1989
    | r1992
    ;

// <1987>
r1987
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <1989>
r1989
    : r1989Base r1989Package
    ;

// <1989 base>
r1989Base
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <1989 package>
r1989Package
    : integrityNo
    | integrityYes
    ;

// <integrity no>
integrityNo
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <integrity yes>
integrityYes
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <1992>
r1992
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <SQL conformance>
sqlConformance
    : low
    | intermediate
    | high
    ;

// <low>
low
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <intermediate>
intermediate
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <high>
high
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// Name-type placeholder (from lexical BNF rule)
tableName
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
columnName
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
schemaName
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
catalogName
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
domainName
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
constraintName
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
correlationName
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
triggerName
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
constraintNameList
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
columnNameList
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

// Auto-generated placeholder for undefined rule
hexStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
asterisk
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
concatenationOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
rightBracket
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
ampersand
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
hostLabelIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
connectionName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
space
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlFortranProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
extendedCursorName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
equalsOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
questionMark
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
regularIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlAdaProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
queryExpression
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
schemaNameClause
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
plusSign
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
hostPLILabelVariable
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
solidus
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
nonquoteCharacter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
cursorName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlPascalProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
datetimeLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
intervalLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
unsignedNumericLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
identifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
comma
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlCobolProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
lessThanOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitStringType
    : IDENTIFIER
    ;

non_joinQueryExpression
    : non_joinQueryTerm
    ;

// Auto-generated placeholder for undefined rule
minusSign
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
semicolon
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
authorizationIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
characterStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
mUMPSHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
unsignedInteger
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlMUMPSProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
leftBracket
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
nationalCharacterStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
descriptorName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
colon
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
greaterThanOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
statementName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
collationName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
directImplementation_definedStatement
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
delimitedIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
extendedStatementName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
characterRepresentation
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
qualifiedIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
cHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitValueFunction
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
quote
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
translationName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
pascalHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
localTableName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlCProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
characterSetName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
period
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlPLIProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
rightParen
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitValueExpression
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
preparableSqlImplementation_definedStatement
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
fortranHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
moduleNameClause
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
parameterName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitLengthExpression
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
adaHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
cobolHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
pLIHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
leftParen
    : IDENTIFIER
    ;

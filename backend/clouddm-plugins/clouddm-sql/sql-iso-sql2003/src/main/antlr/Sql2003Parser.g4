/*
 * Auto-generated from BNF grammar by bnf2antlr.py.
 * Do not edit by hand; regenerate from the .bnf source.
 */
parser grammar Sql2003Parser;

options { tokenVocab=Sql2003Lexer; }

root
    : sqlScript? EOF
    ;

sqlScript
    : (sqlStatement SEMI?)*
    ;

sqlStatement
    : directSqlStatement
    | sqlProcedureStatement
    ;

// <left bracket or trigraph>
leftBracketOrTrigraph
    : T__0
    | leftBracketTrigraph
    ;

// <right bracket or trigraph>
rightBracketOrTrigraph
    : T__1
    | rightBracketTrigraph
    ;

// <left bracket trigraph>
leftBracketTrigraph
    : IDENTIFIER
    ;

// <right bracket trigraph>
rightBracketTrigraph
    : IDENTIFIER
    ;

// <large object length token>
largeObjectLengthToken
    : NUMBER multiplier
    ;

// <multiplier>
multiplier
    : T__2
    | T__3
    | T__4
    ;

// <Unicode delimited identifier>
unicodeDelimitedIdentifier
    : T__5 T__6 T__7 unicodeDelimiterBody T__7 unicodeEscapeSpecifier
    ;

// <Unicode escape specifier>
unicodeEscapeSpecifier
    : (T__8 T__9 unicodeEscapeCharacter T__9)?
    ;

// <Unicode delimiter body>
unicodeDelimiterBody
    : unicodeIdentifierPart
    ;

// <Unicode identifier part>
unicodeIdentifierPart
    : IDENTIFIER
    | unicodeEscapeValue
    ;

// <Unicode escape value>
unicodeEscapeValue
    : unicode4DigitEscapeValue
    | unicode6DigitEscapeValue
    | unicodeCharacterEscapeValue
    ;

// <Unicode 4 digit escape value>
unicode4DigitEscapeValue
    : unicodeEscapeCharacter NUMBER NUMBER NUMBER NUMBER
    ;

// <Unicode 6 digit escape value>
unicode6DigitEscapeValue
    : unicodeEscapeCharacter T__10 NUMBER NUMBER NUMBER NUMBER NUMBER NUMBER
    ;

// <Unicode character escape value>
unicodeCharacterEscapeValue
    : unicodeEscapeCharacter unicodeEscapeCharacter
    ;

// <not equals operator>
notEqualsOperator
    : T__11 T__12
    ;

// <greater than or equals operator>
greaterThanOrEqualsOperator
    : T__12 T__13
    ;

// <less than or equals operator>
lessThanOrEqualsOperator
    : T__11 T__13
    ;

// <concatenation operator>
concatenationOperator
    : T__14 T__14
    ;

// <right arrow>
rightArrow
    : T__15 T__12
    ;

// <double colon>
doubleColon
    : T__16 T__16
    ;

// <double period>
doublePeriod
    : DOT DOT
    ;

// <simple comment>
simpleComment
    : simpleCommentIntroducer (commentCharacter)?
    ;

// <simple comment introducer>
simpleCommentIntroducer
    : T__15 T__15 (T__15)?
    ;

// <bracketed comment>
bracketedComment
    : bracketedCommentIntroducer bracketedCommentContents bracketedCommentTerminator
    ;

// <bracketed comment introducer>
bracketedCommentIntroducer
    : IDENTIFIER T__17
    ;

// <bracketed comment terminator>
bracketedCommentTerminator
    : T__17 IDENTIFIER
    ;

// <bracketed comment contents>
bracketedCommentContents
    : ((commentCharacter)*)?
    ;

// <comment character>
commentCharacter
    : nonquoteCharacter
    | T__9
    ;

// <literal>
literal
    : signedNumericLiteral
    | generalLiteral
    ;

// <unsigned literal>
unsignedLiteral
    : NUMBER
    | generalLiteral
    ;

// <general literal>
generalLiteral
    : STRING
    | STRING
    | unicodeCharacterStringLiteral
    | binaryStringLiteral
    | STRING
    | STRING
    | booleanLiteral
    ;

// <Unicode character string literal>
unicodeCharacterStringLiteral
    : (characterSetSpecification)? T__5 T__6 T__9 (unicodeRepresentation)? T__9 ((T__9 (unicodeRepresentation)? T__9)*)? (T__18 escapeCharacter)?
    ;

// <Unicode representation>
unicodeRepresentation
    : STRING
    | unicodeEscapeValue
    ;

// <binary string literal>
binaryStringLiteral
    : T__19 T__9 ((NUMBER NUMBER)*)? T__9 ((T__9 ((NUMBER NUMBER)*)? T__9)*)? (T__18 escapeCharacter)?
    ;

// <signed numeric literal>
signedNumericLiteral
    : (sign)? NUMBER
    ;

// <exact numeric literal>
exactNumericLiteral
    : NUMBER (DOT (NUMBER)?)?
    | DOT NUMBER
    ;

// <sign>
sign
    : T__10
    | T__15
    ;

// <approximate numeric literal>
approximateNumericLiteral
    : mantissa T__20 exponent
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

// <date string>
dateString
    : T__9 unquotedDateString T__9
    ;

// <time string>
timeString
    : T__9 unquotedTimeString T__9
    ;

// <timestamp string>
timestampString
    : T__9 unquotedTimestampString T__9
    ;

// <time zone interval>
timeZoneInterval
    : sign hoursValue T__16 minutesValue
    ;

// <date value>
dateValue
    : yearsValue T__15 monthsValue T__15 daysValue
    ;

// <time value>
timeValue
    : hoursValue T__16 minutesValue T__16 secondsValue
    ;

// <interval string>
intervalString
    : T__9 unquotedIntervalString T__9
    ;

// <unquoted date string>
unquotedDateString
    : dateValue
    ;

// <unquoted time string>
unquotedTimeString
    : timeValue (timeZoneInterval)?
    ;

// <unquoted timestamp string>
unquotedTimestampString
    : unquotedDateString unquotedTimeString
    ;

// <unquoted interval string>
unquotedIntervalString
    : (sign)? (year_monthLiteral | day_timeLiteral)
    ;

// <year-month literal>
year_monthLiteral
    : yearsValue
    | (yearsValue T__15)? monthsValue
    ;

// <day-time literal>
day_timeLiteral
    : day_timeInterval
    | timeInterval
    ;

// <day-time interval>
day_timeInterval
    : daysValue (hoursValue (T__16 minutesValue (T__16 secondsValue)?)?)?
    ;

// <time interval>
timeInterval
    : hoursValue (T__16 minutesValue (T__16 secondsValue)?)?
    | minutesValue (T__16 secondsValue)?
    | secondsValue
    ;

// <years value>
yearsValue
    : datetimeValue
    ;

// <months value>
monthsValue
    : datetimeValue
    ;

// <days value>
daysValue
    : datetimeValue
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

// <datetime value>
datetimeValue
    : NUMBER
    ;

// <boolean literal>
booleanLiteral
    : T__21
    | T__22
    | T__23
    ;

// <actual identifier>
actualIdentifier
    : IDENTIFIER
    | IDENTIFIER
    ;

// <schema qualified name>
schemaQualifiedName
    : (schemaName DOT)? IDENTIFIER
    ;

// <local or schema qualified name>
localOrSchemaQualifiedName
    : (localOrSchemaQualifier DOT)? IDENTIFIER
    ;

// <local or schema qualifier>
localOrSchemaQualifier
    : schemaName
    | T__24
    ;

// <SQL-client module name>
sQL_clientModuleName
    : IDENTIFIER
    ;

// <procedure name>
procedureName
    : IDENTIFIER
    ;

// <schema qualified routine name>
schemaQualifiedRoutineName
    : schemaQualifiedName
    ;

// <local qualified name>
localQualifiedName
    : (localQualifier DOT)? IDENTIFIER
    ;

// <local qualifier>
localQualifier
    : T__24
    ;

// <host parameter name>
hostParameterName
    : T__16 IDENTIFIER
    ;

// <SQL parameter name>
sqlParameterName
    : IDENTIFIER
    ;

// <external routine name>
externalRoutineName
    : IDENTIFIER
    | STRING
    ;

// <transliteration name>
transliterationName
    : schemaQualifiedName
    ;

// <transcoding name>
transcodingName
    : schemaQualifiedName
    ;

// <schema-resolved user-defined type name>
schema_resolvedUser_definedTypeName
    : IDENTIFIER
    ;

// <schema qualified type name>
schemaQualifiedTypeName
    : (schemaName DOT)? IDENTIFIER
    ;

// <sequence generator name>
sequenceGeneratorName
    : schemaQualifiedName
    ;

// <user identifier>
userIdentifier
    : IDENTIFIER
    ;

// <SQL-server name>
sQL_serverName
    : simpleValueSpecification
    ;

// <connection user name>
connectionUserName
    : simpleValueSpecification
    ;

// <SQL statement name>
sqlStatementName
    : IDENTIFIER
    | IDENTIFIER
    ;

// <dynamic cursor name>
dynamicCursorName
    : IDENTIFIER
    | IDENTIFIER
    ;

// <scope option>
scopeOption
    : T__25
    | T__26
    ;

// <window name>
windowName
    : IDENTIFIER
    ;

// <predefined type>
predefinedType
    : characterStringType (T__27 T__28 characterSetSpecification)? (collateClause)?
    | nationalCharacterStringType (collateClause)?
    | binaryLargeObjectStringType
    | numericType
    | booleanType
    | datetimeType
    | intervalType
    ;

// <character string type>
characterStringType
    : T__27 (LEFT_PAREN length RIGHT_PAREN)?
    | T__29 (LEFT_PAREN length RIGHT_PAREN)?
    | T__27 T__30 LEFT_PAREN length RIGHT_PAREN
    | T__29 T__30 LEFT_PAREN length RIGHT_PAREN
    | T__31 LEFT_PAREN length RIGHT_PAREN
    | T__27 T__32 T__33 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    | T__29 T__32 T__33 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    | T__34 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    ;

// <national character string type>
nationalCharacterStringType
    : T__35 T__27 (LEFT_PAREN length RIGHT_PAREN)?
    | T__35 T__29 (LEFT_PAREN length RIGHT_PAREN)?
    | T__36 (LEFT_PAREN length RIGHT_PAREN)?
    | T__35 T__27 T__30 LEFT_PAREN length RIGHT_PAREN
    | T__35 T__29 T__30 LEFT_PAREN length RIGHT_PAREN
    | T__36 T__30 LEFT_PAREN length RIGHT_PAREN
    | T__35 T__27 T__32 T__33 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    | T__36 T__32 T__33 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    | T__37 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    ;

// <binary large object string type>
binaryLargeObjectStringType
    : T__38 T__32 T__33 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    | T__39 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    ;

// <numeric type>
numericType
    : exactNumericType
    | approximateNumericType
    ;

// <exact numeric type>
exactNumericType
    : T__40 (LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN)?
    | T__41 (LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN)?
    | T__42 (LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN)?
    | T__43
    | T__44
    | T__45
    | T__46
    ;

// <approximate numeric type>
approximateNumericType
    : T__47 (LEFT_PAREN precision RIGHT_PAREN)?
    | T__48
    | T__49 T__50
    ;

// <length>
length
    : NUMBER
    ;

// <large object length>
largeObjectLength
    : NUMBER (multiplier)? (charLengthUnits)?
    | largeObjectLengthToken (charLengthUnits)?
    ;

// <char length units>
charLengthUnits
    : T__51
    | IDENTIFIER
    | T__52
    ;

// <precision>
precision
    : NUMBER
    ;

// <scale>
scale
    : NUMBER
    ;

// <boolean type>
booleanType
    : T__53
    ;

// <datetime type>
datetimeType
    : T__54
    | T__55 (LEFT_PAREN timePrecision RIGHT_PAREN)? (withOrWithoutTimeZone)?
    | T__56 (LEFT_PAREN timestampPrecision RIGHT_PAREN)? (withOrWithoutTimeZone)?
    ;

// <with or without time zone>
withOrWithoutTimeZone
    : T__57 T__55 T__58
    | T__59 T__55 T__58
    ;

// <time precision>
timePrecision
    : timeFractionalSecondsPrecision
    ;

// <timestamp precision>
timestampPrecision
    : timeFractionalSecondsPrecision
    ;

// <time fractional seconds precision>
timeFractionalSecondsPrecision
    : NUMBER
    ;

// <interval type>
intervalType
    : T__60 intervalQualifier
    ;

// <row type>
rowType
    : T__61 rowTypeBody
    ;

// <row type body>
rowTypeBody
    : LEFT_PAREN fieldDefinition ((COMMA fieldDefinition)*)? RIGHT_PAREN
    ;

// <reference type>
referenceType
    : T__62 LEFT_PAREN referencedType RIGHT_PAREN (scopeClause)?
    ;

// <scope clause>
scopeClause
    : T__63 tableName
    ;

// <referenced type>
referencedType
    : path_resolvedUser_definedTypeName
    ;

// <path-resolved user-defined type name>
path_resolvedUser_definedTypeName
    : IDENTIFIER
    ;

// Merged rules: data type, collection type, array type, multiset type
arrayType
    : predefinedType
    | rowType
    | path_resolvedUser_definedTypeName
    | referenceType
    | arrayType T__64 (leftBracketOrTrigraph NUMBER rightBracketOrTrigraph)?
    | arrayType T__65
    ;

// <field definition>
fieldDefinition
    : IDENTIFIER arrayType (referenceScopeCheck)?
    ;

// <parenthesized value expression>
parenthesizedValueExpression
    : LEFT_PAREN valueExpression RIGHT_PAREN
    ;

// <value specification>
valueSpecification
    : literal
    | generalValueSpecification
    ;

// <unsigned value specification>
unsignedValueSpecification
    : unsignedLiteral
    | generalValueSpecification
    ;

// <general value specification>
generalValueSpecification
    : hostParameterSpecification
    | sqlParameterReference
    | dynamicParameterSpecification
    | embeddedVariableSpecification
    | currentCollationSpecification
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER path_resolvedUser_definedTypeName
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__66
    | T__67
    ;

// <simple value specification>
simpleValueSpecification
    : literal
    | hostParameterName
    | sqlParameterReference
    | embeddedVariableName
    ;

// <target specification>
targetSpecification
    : hostParameterSpecification
    | sqlParameterReference
    | columnReference
    | targetArrayElementSpecification
    | dynamicParameterSpecification
    | embeddedVariableSpecification
    ;

// <simple target specification>
simpleTargetSpecification
    : hostParameterSpecification
    | sqlParameterReference
    | columnReference
    | embeddedVariableName
    ;

// <host parameter specification>
hostParameterSpecification
    : hostParameterName (indicatorParameter)?
    ;

// <dynamic parameter specification>
dynamicParameterSpecification
    : T__68
    ;

// <embedded variable specification>
embeddedVariableSpecification
    : embeddedVariableName (indicatorVariable)?
    ;

// <indicator variable>
indicatorVariable
    : (T__69)? embeddedVariableName
    ;

// <indicator parameter>
indicatorParameter
    : (T__69)? hostParameterName
    ;

// <target array element specification>
targetArrayElementSpecification
    : targetArrayReference leftBracketOrTrigraph simpleValueSpecification rightBracketOrTrigraph
    ;

// <target array reference>
targetArrayReference
    : sqlParameterReference
    | columnReference
    ;

// <current collation specification>
currentCollationSpecification
    : IDENTIFIER LEFT_PAREN stringValueExpression RIGHT_PAREN
    ;

// <contextually typed value specification>
contextuallyTypedValueSpecification
    : implicitlyTypedValueSpecification
    | defaultSpecification
    ;

// <implicitly typed value specification>
implicitlyTypedValueSpecification
    : nullSpecification
    | emptySpecification
    ;

// <null specification>
nullSpecification
    : T__70
    ;

// <empty specification>
emptySpecification
    : T__64 leftBracketOrTrigraph rightBracketOrTrigraph
    | T__65 leftBracketOrTrigraph rightBracketOrTrigraph
    ;

// <default specification>
defaultSpecification
    : T__71
    ;

// <identifier chain>
identifierChain
    : IDENTIFIER ((DOT IDENTIFIER)*)?
    ;

// <basic identifier chain>
basicIdentifierChain
    : identifierChain
    ;

// <column reference>
columnReference
    : basicIdentifierChain
    | T__24 DOT IDENTIFIER DOT columnName
    ;

// <SQL parameter reference>
sqlParameterReference
    : basicIdentifierChain
    ;

// <set function specification>
setFunctionSpecification
    : aggregateFunction
    | groupingOperation
    ;

// <grouping operation>
groupingOperation
    : T__72 LEFT_PAREN columnReference ((COMMA columnReference)*)? RIGHT_PAREN
    ;

// <window function>
windowFunction
    : windowFunctionType T__73 windowNameOrSpecification
    ;

// <window function type>
windowFunctionType
    : rankFunctionType LEFT_PAREN RIGHT_PAREN
    | IDENTIFIER LEFT_PAREN RIGHT_PAREN
    | aggregateFunction
    ;

// <rank function type>
rankFunctionType
    : T__74
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <window name or specification>
windowNameOrSpecification
    : windowName
    | in_lineWindowSpecification
    ;

// <in-line window specification>
in_lineWindowSpecification
    : windowSpecification
    ;

// <case expression>
caseExpression
    : caseAbbreviation
    | caseSpecification
    ;

// <case abbreviation>
caseAbbreviation
    : T__75 LEFT_PAREN valueExpression COMMA valueExpression RIGHT_PAREN
    | T__76 LEFT_PAREN valueExpression (COMMA valueExpression)* RIGHT_PAREN
    ;

// <case specification>
caseSpecification
    : simpleCase
    | searchedCase
    ;

// <simple case>
simpleCase
    : T__77 caseOperand simpleWhenClause (elseClause)? T__78
    ;

// <searched case>
searchedCase
    : T__77 searchedWhenClause (elseClause)? T__78
    ;

// <simple when clause>
simpleWhenClause
    : T__79 whenOperand T__80 result
    ;

// <searched when clause>
searchedWhenClause
    : T__79 searchCondition T__80 result
    ;

// <else clause>
elseClause
    : T__81 result
    ;

// <case operand>
caseOperand
    : rowValuePredicand
    | overlapsPredicate
    ;

// <when operand>
whenOperand
    : rowValuePredicand
    | comparisonPredicatePart2
    | betweenPredicatePart2
    | inPredicatePart2
    | characterLikePredicatePart2
    | octetLikePredicatePart2
    | similarPredicatePart2
    | nullPredicatePart2
    | quantifiedComparisonPredicatePart2
    | matchPredicatePart2
    | overlapsPredicatePart2
    | distinctPredicatePart2
    | memberPredicatePart2
    | submultisetPredicatePart2
    | setPredicatePart2
    | typePredicatePart2
    ;

// <result>
result
    : resultExpression
    | T__70
    ;

// <result expression>
resultExpression
    : valueExpression
    ;

// <cast specification>
castSpecification
    : T__82 LEFT_PAREN castOperand T__83 castTarget RIGHT_PAREN
    ;

// <cast operand>
castOperand
    : valueExpression
    | implicitlyTypedValueSpecification
    ;

// <cast target>
castTarget
    : domainName
    | arrayType
    ;

// <next value expression>
nextValueExpression
    : T__84 T__67 T__85 sequenceGeneratorName
    ;

// <subtype treatment>
subtypeTreatment
    : T__86 LEFT_PAREN subtypeOperand T__83 targetSubtype RIGHT_PAREN
    ;

// <subtype operand>
subtypeOperand
    : valueExpression
    ;

// <target subtype>
targetSubtype
    : path_resolvedUser_definedTypeName
    | referenceType
    ;

// <generalized invocation>
generalizedInvocation
    : LEFT_PAREN arrayConcatenation T__83 arrayType RIGHT_PAREN DOT IDENTIFIER (sqlArgumentList)?
    ;

// <method selection>
methodSelection
    : routineInvocation
    ;

// <constructor method selection>
constructorMethodSelection
    : routineInvocation
    ;

// <static method invocation>
staticMethodInvocation
    : path_resolvedUser_definedTypeName doubleColon IDENTIFIER (sqlArgumentList)?
    ;

// <static method selection>
staticMethodSelection
    : routineInvocation
    ;

// <new specification>
newSpecification
    : T__87 routineInvocation
    ;

// <new invocation>
newInvocation
    : arrayConcatenation
    | routineInvocation
    ;

// <dereference operator>
dereferenceOperator
    : rightArrow
    ;

// <dereference operation>
dereferenceOperation
    : referenceValueExpression dereferenceOperator IDENTIFIER
    ;

// <method reference>
methodReference
    : arrayConcatenation dereferenceOperator IDENTIFIER sqlArgumentList
    ;

// <reference resolution>
referenceResolution
    : T__88 LEFT_PAREN referenceValueExpression RIGHT_PAREN
    ;

// <multiset element reference>
multisetElementReference
    : T__89 LEFT_PAREN multisetValueExpression RIGHT_PAREN
    ;

// <value expression>
valueExpression
    : commonValueExpression
    | booleanValueExpression
    | rowValueExpression
    ;

// <common value expression>
commonValueExpression
    : numericValueExpression
    | stringValueExpression
    | datetimeValueExpression
    | intervalValueExpression
    | user_definedTypeValueExpression
    | referenceValueExpression
    | collectionValueExpression
    ;

// <user-defined type value expression>
user_definedTypeValueExpression
    : arrayConcatenation
    ;

// <reference value expression>
referenceValueExpression
    : arrayConcatenation
    ;

// <collection value expression>
collectionValueExpression
    : arrayConcatenation
    | multisetValueExpression
    ;

// <collection value constructor>
collectionValueConstructor
    : arrayValueConstructor
    | multisetValueConstructor
    ;

// <numeric value expression>
numericValueExpression
    : term
    | numericValueExpression T__10 term
    | numericValueExpression T__15 term
    ;

// <term>
term
    : factor
    | term T__17 factor
    | term T__90 factor
    ;

// <factor>
factor
    : (sign)? numericPrimary
    ;

// <numeric primary>
numericPrimary
    : arrayConcatenation
    | numericValueFunction
    ;

// <numeric value function>
numericValueFunction
    : positionExpression
    | extractExpression
    | lengthExpression
    | cardinalityExpression
    | absoluteValueExpression
    | modulusExpression
    | naturalLogarithm
    | exponentialFunction
    | powerFunction
    | squareRoot
    | floorFunction
    | ceilingFunction
    | widthBucketFunction
    ;

// <position expression>
positionExpression
    : stringPositionExpression
    | blobPositionExpression
    ;

// <string position expression>
stringPositionExpression
    : T__91 LEFT_PAREN stringValueExpression T__92 stringValueExpression (T__93 charLengthUnits)? RIGHT_PAREN
    ;

// <blob position expression>
blobPositionExpression
    : T__91 LEFT_PAREN blobConcatenation T__92 blobConcatenation RIGHT_PAREN
    ;

// <length expression>
lengthExpression
    : charLengthExpression
    | octetLengthExpression
    ;

// <char length expression>
charLengthExpression
    : (IDENTIFIER | IDENTIFIER) LEFT_PAREN stringValueExpression (T__93 charLengthUnits)? RIGHT_PAREN
    ;

// <octet length expression>
octetLengthExpression
    : IDENTIFIER LEFT_PAREN stringValueExpression RIGHT_PAREN
    ;

// <extract expression>
extractExpression
    : T__94 LEFT_PAREN extractField T__95 extractSource RIGHT_PAREN
    ;

// <extract field>
extractField
    : primaryDatetimeField
    | timeZoneField
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

// <cardinality expression>
cardinalityExpression
    : T__96 LEFT_PAREN collectionValueExpression RIGHT_PAREN
    ;

// <absolute value expression>
absoluteValueExpression
    : T__97 LEFT_PAREN numericValueExpression RIGHT_PAREN
    ;

// <modulus expression>
modulusExpression
    : T__98 LEFT_PAREN IDENTIFIER COMMA IDENTIFIER RIGHT_PAREN
    ;

// <natural logarithm>
naturalLogarithm
    : T__99 LEFT_PAREN numericValueExpression RIGHT_PAREN
    ;

// <exponential function>
exponentialFunction
    : T__100 LEFT_PAREN numericValueExpression RIGHT_PAREN
    ;

// <power function>
powerFunction
    : T__101 LEFT_PAREN numericValueExpressionBase COMMA numericValueExpressionExponent RIGHT_PAREN
    ;

// <numeric value expression base>
numericValueExpressionBase
    : numericValueExpression
    ;

// <numeric value expression exponent>
numericValueExpressionExponent
    : numericValueExpression
    ;

// <square root>
squareRoot
    : T__102 LEFT_PAREN numericValueExpression RIGHT_PAREN
    ;

// <floor function>
floorFunction
    : T__103 LEFT_PAREN numericValueExpression RIGHT_PAREN
    ;

// <ceiling function>
ceilingFunction
    : (T__104 | T__105) LEFT_PAREN numericValueExpression RIGHT_PAREN
    ;

// <width bucket function>
widthBucketFunction
    : IDENTIFIER LEFT_PAREN widthBucketOperand COMMA widthBucketBound1 COMMA widthBucketBound2 COMMA widthBucketCount RIGHT_PAREN
    ;

// <width bucket operand>
widthBucketOperand
    : numericValueExpression
    ;

// <width bucket bound 1>
widthBucketBound1
    : numericValueExpression
    ;

// <width bucket bound 2>
widthBucketBound2
    : numericValueExpression
    ;

// <width bucket count>
widthBucketCount
    : numericValueExpression
    ;

// <string value expression>
stringValueExpression
    : characterValueExpression
    | blobConcatenation
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
    : arrayConcatenation
    | stringValueFunction
    ;

// <blob factor>
blobFactor
    : blobPrimary
    ;

// <blob primary>
blobPrimary
    : arrayConcatenation
    | stringValueFunction
    ;

// Merged rules: blob value expression, blob concatenation
blobConcatenation
    : blobFactor
    | blobConcatenation concatenationOperator blobFactor
    ;

// <string value function>
stringValueFunction
    : characterValueFunction
    | blobValueFunction
    ;

// <character value function>
characterValueFunction
    : characterSubstringFunction
    | regularExpressionSubstringFunction
    | fold
    | transcoding
    | characterTransliteration
    | trimFunction
    | characterOverlayFunction
    | normalizeFunction
    | specificTypeMethod
    ;

// <character substring function>
characterSubstringFunction
    : T__106 LEFT_PAREN characterValueExpression T__95 startPosition (T__85 stringLength)? (T__93 charLengthUnits)? RIGHT_PAREN
    ;

// <regular expression substring function>
regularExpressionSubstringFunction
    : T__106 LEFT_PAREN characterValueExpression T__107 characterValueExpression T__18 escapeCharacter RIGHT_PAREN
    ;

// <fold>
fold
    : (T__108 | T__109) LEFT_PAREN characterValueExpression RIGHT_PAREN
    ;

// <transcoding>
transcoding
    : T__110 LEFT_PAREN characterValueExpression T__93 transcodingName RIGHT_PAREN
    ;

// <character transliteration>
characterTransliteration
    : T__111 LEFT_PAREN characterValueExpression T__93 transliterationName RIGHT_PAREN
    ;

// <trim function>
trimFunction
    : T__112 LEFT_PAREN trimOperands RIGHT_PAREN
    ;

// <trim operands>
trimOperands
    : ((trimSpecification)? (trimCharacter)? T__95)? trimSource
    ;

// <trim source>
trimSource
    : characterValueExpression
    ;

// <trim specification>
trimSpecification
    : T__113
    | T__114
    | T__115
    ;

// <trim character>
trimCharacter
    : characterValueExpression
    ;

// <character overlay function>
characterOverlayFunction
    : T__116 LEFT_PAREN characterValueExpression T__117 characterValueExpression T__95 startPosition (T__85 stringLength)? (T__93 charLengthUnits)? RIGHT_PAREN
    ;

// <normalize function>
normalizeFunction
    : T__118 LEFT_PAREN characterValueExpression RIGHT_PAREN
    ;

// <specific type method>
specificTypeMethod
    : user_definedTypeValueExpression DOT T__119
    ;

// <blob value function>
blobValueFunction
    : blobSubstringFunction
    | blobTrimFunction
    | blobOverlayFunction
    ;

// <blob substring function>
blobSubstringFunction
    : T__106 LEFT_PAREN blobConcatenation T__95 startPosition (T__85 stringLength)? RIGHT_PAREN
    ;

// <blob trim function>
blobTrimFunction
    : T__112 LEFT_PAREN blobTrimOperands RIGHT_PAREN
    ;

// <blob trim operands>
blobTrimOperands
    : ((trimSpecification)? (trimOctet)? T__95)? blobTrimSource
    ;

// <blob trim source>
blobTrimSource
    : blobConcatenation
    ;

// <trim octet>
trimOctet
    : blobConcatenation
    ;

// <blob overlay function>
blobOverlayFunction
    : T__116 LEFT_PAREN blobConcatenation T__117 blobConcatenation T__95 startPosition (T__85 stringLength)? RIGHT_PAREN
    ;

// <start position>
startPosition
    : numericValueExpression
    ;

// <string length>
stringLength
    : numericValueExpression
    ;

// <datetime value expression>
datetimeValueExpression
    : datetimeTerm
    | intervalValueExpression T__10 datetimeTerm
    | datetimeValueExpression T__10 intervalTerm
    | datetimeValueExpression T__15 intervalTerm
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
    : arrayConcatenation
    | datetimeValueFunction
    ;

// <time zone>
timeZone
    : T__120 timeZoneSpecifier
    ;

// <time zone specifier>
timeZoneSpecifier
    : T__26
    | T__55 T__58 intervalPrimary
    ;

// <datetime value function>
datetimeValueFunction
    : currentDateValueFunction
    | currentTimeValueFunction
    | currentTimestampValueFunction
    | currentLocalTimeValueFunction
    | currentLocalTimestampValueFunction
    ;

// <current date value function>
currentDateValueFunction
    : IDENTIFIER
    ;

// <current time value function>
currentTimeValueFunction
    : IDENTIFIER (LEFT_PAREN timePrecision RIGHT_PAREN)?
    ;

// <current local time value function>
currentLocalTimeValueFunction
    : T__121 (LEFT_PAREN timePrecision RIGHT_PAREN)?
    ;

// <current timestamp value function>
currentTimestampValueFunction
    : IDENTIFIER (LEFT_PAREN timestampPrecision RIGHT_PAREN)?
    ;

// <current local timestamp value function>
currentLocalTimestampValueFunction
    : T__122 (LEFT_PAREN timestampPrecision RIGHT_PAREN)?
    ;

// Merged rules: interval value expression, interval value expression 1
intervalValueExpression
    : intervalValueExpression T__10 intervalTerm1
    | intervalValueExpression T__15 intervalTerm1
    | LEFT_PAREN datetimeValueExpression T__15 datetimeTerm RIGHT_PAREN intervalQualifier
    ;

// Merged rules: interval term, interval term 2
intervalTerm
    : intervalFactor
    | intervalTerm T__17 factor
    | intervalTerm T__90 factor
    | term T__17 intervalFactor
    ;

// <interval factor>
intervalFactor
    : (sign)? intervalPrimary
    ;

// <interval primary>
intervalPrimary
    : arrayConcatenation (intervalQualifier)?
    | intervalValueFunction
    ;

// <interval term 1>
intervalTerm1
    : intervalTerm
    ;

// <interval value function>
intervalValueFunction
    : intervalAbsoluteValueFunction
    ;

// <interval absolute value function>
intervalAbsoluteValueFunction
    : T__97 LEFT_PAREN intervalValueExpression RIGHT_PAREN
    ;

// <boolean value expression>
booleanValueExpression
    : booleanTerm
    | booleanValueExpression T__123 booleanTerm
    ;

// <boolean term>
booleanTerm
    : booleanFactor
    | booleanTerm T__124 booleanFactor
    ;

// <boolean factor>
booleanFactor
    : (T__125)? booleanTest
    ;

// <boolean test>
booleanTest
    : booleanPrimary (T__126 (T__125)? truthValue)?
    ;

// <truth value>
truthValue
    : T__21
    | T__22
    | T__23
    ;

// <boolean primary>
booleanPrimary
    : predicate
    | booleanPredicand
    ;

// <boolean predicand>
booleanPredicand
    : parenthesizedBooleanValueExpression
    | arrayConcatenation
    ;

// <parenthesized boolean value expression>
parenthesizedBooleanValueExpression
    : LEFT_PAREN booleanValueExpression RIGHT_PAREN
    ;

// Merged rules: value expression primary, nonparenthesized value expression primary, field reference, method invocation, direct invocation, attribute or method reference, array element reference, array value expression, array concatenation, array value expression 1, array factor
arrayConcatenation
    : parenthesizedValueExpression
    | unsignedValueSpecification
    | columnReference
    | setFunctionSpecification
    | windowFunction
    | scalarSubquery
    | caseExpression
    | castSpecification
    | subtypeTreatment
    | staticMethodInvocation
    | newSpecification
    | referenceResolution
    | collectionValueConstructor
    | multisetElementReference
    | routineInvocation
    | nextValueExpression
    | generalizedInvocation
    ;

// <array value constructor>
arrayValueConstructor
    : arrayValueConstructorByEnumeration
    | arrayValueConstructorByQuery
    ;

// <array value constructor by enumeration>
arrayValueConstructorByEnumeration
    : T__64 leftBracketOrTrigraph arrayElementList rightBracketOrTrigraph
    ;

// <array element list>
arrayElementList
    : arrayElement ((COMMA arrayElement)*)?
    ;

// <array element>
arrayElement
    : valueExpression
    ;

// <array value constructor by query>
arrayValueConstructorByQuery
    : T__64 LEFT_PAREN queryExpression (orderByClause)? RIGHT_PAREN
    ;

// <multiset value expression>
multisetValueExpression
    : multisetTerm
    | multisetValueExpression T__65 T__127 (T__128 | T__129)? multisetTerm
    | multisetValueExpression T__65 T__130 (T__128 | T__129)? multisetTerm
    ;

// <multiset term>
multisetTerm
    : multisetPrimary
    | multisetTerm T__65 T__131 (T__128 | T__129)? multisetPrimary
    ;

// <multiset primary>
multisetPrimary
    : multisetValueFunction
    | arrayConcatenation
    ;

// <multiset value function>
multisetValueFunction
    : multisetSetFunction
    ;

// <multiset set function>
multisetSetFunction
    : T__28 LEFT_PAREN multisetValueExpression RIGHT_PAREN
    ;

// <multiset value constructor>
multisetValueConstructor
    : multisetValueConstructorByEnumeration
    | multisetValueConstructorByQuery
    | tableValueConstructorByQuery
    ;

// <multiset value constructor by enumeration>
multisetValueConstructorByEnumeration
    : T__65 leftBracketOrTrigraph multisetElementList rightBracketOrTrigraph
    ;

// <multiset element list>
multisetElementList
    : multisetElement ((COMMA multisetElement))?
    ;

// <multiset element>
multisetElement
    : valueExpression
    ;

// <multiset value constructor by query>
multisetValueConstructorByQuery
    : T__65 LEFT_PAREN queryExpression RIGHT_PAREN
    ;

// <table value constructor by query>
tableValueConstructorByQuery
    : T__132 LEFT_PAREN queryExpression RIGHT_PAREN
    ;

// <row value constructor>
rowValueConstructor
    : commonValueExpression
    | booleanValueExpression
    | explicitRowValueConstructor
    ;

// <explicit row value constructor>
explicitRowValueConstructor
    : LEFT_PAREN rowValueConstructorElement COMMA rowValueConstructorElementList RIGHT_PAREN
    | T__61 LEFT_PAREN rowValueConstructorElementList RIGHT_PAREN
    | rowSubquery
    ;

// <row value constructor element list>
rowValueConstructorElementList
    : rowValueConstructorElement ((COMMA rowValueConstructorElement)*)?
    ;

// <row value constructor element>
rowValueConstructorElement
    : valueExpression
    ;

// <contextually typed row value constructor>
contextuallyTypedRowValueConstructor
    : commonValueExpression
    | booleanValueExpression
    | contextuallyTypedValueSpecification
    | LEFT_PAREN contextuallyTypedRowValueConstructorElement COMMA contextuallyTypedRowValueConstructorElementList RIGHT_PAREN
    | T__61 LEFT_PAREN contextuallyTypedRowValueConstructorElementList RIGHT_PAREN
    ;

// <contextually typed row value constructor element list>
contextuallyTypedRowValueConstructorElementList
    : contextuallyTypedRowValueConstructorElement ((COMMA contextuallyTypedRowValueConstructorElement)*)?
    ;

// <contextually typed row value constructor element>
contextuallyTypedRowValueConstructorElement
    : valueExpression
    | contextuallyTypedValueSpecification
    ;

// <row value constructor predicand>
rowValueConstructorPredicand
    : commonValueExpression
    | booleanPredicand
    | explicitRowValueConstructor
    ;

// <row value expression>
rowValueExpression
    : rowValueSpecialCase
    | explicitRowValueConstructor
    ;

// <table row value expression>
tableRowValueExpression
    : rowValueSpecialCase
    | rowValueConstructor
    ;

// <contextually typed row value expression>
contextuallyTypedRowValueExpression
    : rowValueSpecialCase
    | contextuallyTypedRowValueConstructor
    ;

// <row value predicand>
rowValuePredicand
    : rowValueSpecialCase
    | rowValueConstructorPredicand
    ;

// <row value special case>
rowValueSpecialCase
    : arrayConcatenation
    ;

// <table value constructor>
tableValueConstructor
    : T__133 rowValueExpressionList
    ;

// <row value expression list>
rowValueExpressionList
    : tableRowValueExpression ((COMMA tableRowValueExpression)*)?
    ;

// <contextually typed table value constructor>
contextuallyTypedTableValueConstructor
    : T__133 contextuallyTypedRowValueExpressionList
    ;

// <contextually typed row value expression list>
contextuallyTypedRowValueExpressionList
    : contextuallyTypedRowValueExpression ((COMMA contextuallyTypedRowValueExpression)*)?
    ;

// <table expression>
tableExpression
    : fromClause (whereClause)? (groupByClause)? (havingClause)? (windowClause)?
    ;

// <from clause>
fromClause
    : T__95 tableReferenceList
    ;

// <table reference list>
tableReferenceList
    : crossJoin ((COMMA crossJoin)*)?
    ;

// <sample clause>
sampleClause
    : T__134 sampleMethod LEFT_PAREN samplePercentage RIGHT_PAREN (repeatableClause)?
    ;

// <sample method>
sampleMethod
    : T__135
    | T__136
    ;

// <repeatable clause>
repeatableClause
    : T__137 LEFT_PAREN repeatArgument RIGHT_PAREN
    ;

// <sample percentage>
samplePercentage
    : numericValueExpression
    ;

// <repeat argument>
repeatArgument
    : numericValueExpression
    ;

// <only spec>
onlySpec
    : T__138 LEFT_PAREN tableOrQueryName RIGHT_PAREN
    ;

// <lateral derived table>
lateralDerivedTable
    : T__139 tableSubquery
    ;

// <collection derived table>
collectionDerivedTable
    : T__140 LEFT_PAREN collectionValueExpression RIGHT_PAREN (T__57 T__141)?
    ;

// <table function derived table>
tableFunctionDerivedTable
    : T__132 LEFT_PAREN collectionValueExpression RIGHT_PAREN
    ;

// <derived table>
derivedTable
    : tableSubquery
    ;

// <table or query name>
tableOrQueryName
    : tableName
    ;

// <derived column list>
derivedColumnList
    : columnNameList
    ;

// Merged rules: table reference, table primary or joined table, table primary, joined table, cross join, qualified join, natural join, union join
crossJoin
    : tableOrQueryName ((T__83)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?)?
    | derivedTable (T__83)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?
    | lateralDerivedTable (T__83)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?
    | collectionDerivedTable (T__83)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?
    | tableFunctionDerivedTable (T__83)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?
    | onlySpec ((T__83)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?)?
    ;

// <join specification>
joinSpecification
    : joinCondition
    | namedColumnsJoin
    ;

// <join condition>
joinCondition
    : T__142 searchCondition
    ;

// <named columns join>
namedColumnsJoin
    : T__93 LEFT_PAREN joinColumnList RIGHT_PAREN
    ;

// <join type>
joinType
    : T__143
    | outerJoinType (T__144)?
    ;

// <outer join type>
outerJoinType
    : T__145
    | T__146
    | T__147
    ;

// <join column list>
joinColumnList
    : columnNameList
    ;

// <where clause>
whereClause
    : T__148 searchCondition
    ;

// <group by clause>
groupByClause
    : T__149 T__150 (setQuantifier)? groupingElementList
    ;

// <grouping element list>
groupingElementList
    : groupingElement ((COMMA groupingElement)*)?
    ;

// <grouping element>
groupingElement
    : ordinaryGroupingSet
    | rollupList
    | cubeList
    | groupingSetsSpecification
    | emptyGroupingSet
    ;

// <ordinary grouping set>
ordinaryGroupingSet
    : groupingColumnReference
    | LEFT_PAREN groupingColumnReferenceList RIGHT_PAREN
    ;

// <grouping column reference>
groupingColumnReference
    : columnReference (collateClause)?
    ;

// <grouping column reference list>
groupingColumnReferenceList
    : groupingColumnReference ((COMMA groupingColumnReference)*)?
    ;

// <rollup list>
rollupList
    : T__151 LEFT_PAREN ordinaryGroupingSetList RIGHT_PAREN
    ;

// <ordinary grouping set list>
ordinaryGroupingSetList
    : ordinaryGroupingSet ((COMMA ordinaryGroupingSet)*)?
    ;

// <cube list>
cubeList
    : T__152 LEFT_PAREN ordinaryGroupingSetList RIGHT_PAREN
    ;

// <grouping sets specification>
groupingSetsSpecification
    : T__72 T__153 LEFT_PAREN groupingSetList RIGHT_PAREN
    ;

// <grouping set list>
groupingSetList
    : groupingSet ((COMMA groupingSet)*)?
    ;

// <grouping set>
groupingSet
    : ordinaryGroupingSet
    | rollupList
    | cubeList
    | groupingSetsSpecification
    | emptyGroupingSet
    ;

// <empty grouping set>
emptyGroupingSet
    : LEFT_PAREN RIGHT_PAREN
    ;

// <having clause>
havingClause
    : T__154 searchCondition
    ;

// <window clause>
windowClause
    : T__155 windowDefinitionList
    ;

// <window definition list>
windowDefinitionList
    : windowDefinition ((COMMA windowDefinition)*)?
    ;

// <window definition>
windowDefinition
    : newWindowName T__83 windowSpecification
    ;

// <new window name>
newWindowName
    : windowName
    ;

// <window specification>
windowSpecification
    : LEFT_PAREN windowSpecificationDetails RIGHT_PAREN
    ;

// <window specification details>
windowSpecificationDetails
    : (existingWindowName)? (windowPartitionClause)? (windowOrderClause)? (windowFrameClause)?
    ;

// <existing window name>
existingWindowName
    : windowName
    ;

// <window partition clause>
windowPartitionClause
    : T__156 T__150 windowPartitionColumnReferenceList
    ;

// <window partition column reference list>
windowPartitionColumnReferenceList
    : windowPartitionColumnReference ((COMMA windowPartitionColumnReference)*)?
    ;

// <window partition column reference>
windowPartitionColumnReference
    : columnReference (collateClause)?
    ;

// <window order clause>
windowOrderClause
    : T__157 T__150 sortSpecificationList
    ;

// <window frame clause>
windowFrameClause
    : windowFrameUnits windowFrameExtent (windowFrameExclusion)?
    ;

// <window frame units>
windowFrameUnits
    : T__158
    | T__159
    ;

// <window frame extent>
windowFrameExtent
    : windowFrameStart
    | windowFrameBetween
    ;

// <window frame start>
windowFrameStart
    : T__160 T__161
    | windowFramePreceding
    | T__162 T__61
    ;

// <window frame preceding>
windowFramePreceding
    : unsignedValueSpecification T__161
    ;

// <window frame between>
windowFrameBetween
    : T__163 windowFrameBound1 T__124 windowFrameBound2
    ;

// <window frame bound 1>
windowFrameBound1
    : windowFrameBound
    ;

// <window frame bound 2>
windowFrameBound2
    : windowFrameBound
    ;

// <window frame bound>
windowFrameBound
    : windowFrameStart
    | T__160 T__164
    | windowFrameFollowing
    ;

// <window frame following>
windowFrameFollowing
    : unsignedValueSpecification T__164
    ;

// <window frame exclusion>
windowFrameExclusion
    : T__165 T__162 T__61
    | T__165 T__149
    | T__165 T__166
    | T__165 T__167 T__168
    ;

// <query specification>
querySpecification
    : T__169 (setQuantifier)? selectList tableExpression
    ;

// <select list>
selectList
    : T__17
    | selectSublist ((COMMA selectSublist)*)?
    ;

// <select sublist>
selectSublist
    : derivedColumn
    | qualifiedAsterisk
    ;

// <qualified asterisk>
qualifiedAsterisk
    : asteriskedIdentifierChain DOT T__17
    | allFieldsReference
    ;

// <asterisked identifier chain>
asteriskedIdentifierChain
    : asteriskedIdentifier ((DOT asteriskedIdentifier)*)?
    ;

// <asterisked identifier>
asteriskedIdentifier
    : IDENTIFIER
    ;

// <derived column>
derivedColumn
    : valueExpression (asClause)?
    ;

// <as clause>
asClause
    : (T__83)? columnName
    ;

// <all fields reference>
allFieldsReference
    : arrayConcatenation DOT T__17 (T__83 LEFT_PAREN allFieldsColumnNameList RIGHT_PAREN)?
    ;

// <all fields column name list>
allFieldsColumnNameList
    : columnNameList
    ;

// <query expression>
queryExpression
    : (withClause)? non_joinQueryExpression
    ;

// <with clause>
withClause
    : T__57 (T__170)? withList
    ;

// <with list>
withList
    : withListElement ((COMMA withListElement)*)?
    ;

// <with list element>
withListElement
    : IDENTIFIER (LEFT_PAREN withColumnList RIGHT_PAREN)? T__83 LEFT_PAREN queryExpression RIGHT_PAREN (searchOrCycleClause)?
    ;

// <with column list>
withColumnList
    : columnNameList
    ;

// Merged rules: query term, non-join query term
non_joinQueryTerm
    : non_joinQueryPrimary
    | non_joinQueryTerm T__131 (T__128 | T__129)? (correspondingSpec)? queryPrimary
    ;

// <query primary>
queryPrimary
    : non_joinQueryPrimary
    | crossJoin
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

// <explicit table>
explicitTable
    : T__132 tableOrQueryName
    ;

// <corresponding spec>
correspondingSpec
    : T__171 (T__150 LEFT_PAREN correspondingColumnList RIGHT_PAREN)?
    ;

// <corresponding column list>
correspondingColumnList
    : columnNameList
    ;

// <search or cycle clause>
searchOrCycleClause
    : searchClause
    | cycleClause
    | searchClause cycleClause
    ;

// <search clause>
searchClause
    : T__172 recursiveSearchOrder T__28 sequenceColumn
    ;

// <recursive search order>
recursiveSearchOrder
    : T__173 T__174 T__150 sortSpecificationList
    | T__175 T__174 T__150 sortSpecificationList
    ;

// <sequence column>
sequenceColumn
    : columnName
    ;

// <cycle clause>
cycleClause
    : T__176 cycleColumnList T__28 cycleMarkColumn T__177 cycleMarkValue T__71 non_cycleMarkValue T__93 pathColumn
    ;

// <cycle column list>
cycleColumnList
    : cycleColumn ((COMMA cycleColumn)*)?
    ;

// <cycle column>
cycleColumn
    : columnName
    ;

// <cycle mark column>
cycleMarkColumn
    : columnName
    ;

// <path column>
pathColumn
    : columnName
    ;

// <cycle mark value>
cycleMarkValue
    : valueExpression
    ;

// <non-cycle mark value>
non_cycleMarkValue
    : valueExpression
    ;

// <scalar subquery>
scalarSubquery
    : subquery
    ;

// <row subquery>
rowSubquery
    : subquery
    ;

// <table subquery>
tableSubquery
    : subquery
    ;

// <subquery>
subquery
    : LEFT_PAREN queryExpression RIGHT_PAREN
    ;

// <predicate>
predicate
    : comparisonPredicate
    | betweenPredicate
    | inPredicate
    | likePredicate
    | similarPredicate
    | nullPredicate
    | quantifiedComparisonPredicate
    | existsPredicate
    | uniquePredicate
    | normalizedPredicate
    | matchPredicate
    | overlapsPredicate
    | distinctPredicate
    | memberPredicate
    | submultisetPredicate
    | setPredicate
    | typePredicate
    ;

// <comparison predicate>
comparisonPredicate
    : rowValuePredicand comparisonPredicatePart2
    ;

// <comparison predicate part 2>
comparisonPredicatePart2
    : compOp rowValuePredicand
    ;

// <comp op>
compOp
    : T__13
    | notEqualsOperator
    | T__11
    | T__12
    | lessThanOrEqualsOperator
    | greaterThanOrEqualsOperator
    ;

// <between predicate>
betweenPredicate
    : rowValuePredicand betweenPredicatePart2
    ;

// <between predicate part 2>
betweenPredicatePart2
    : (T__125)? T__163 (T__178 | T__179)? rowValuePredicand T__124 rowValuePredicand
    ;

// <in predicate>
inPredicate
    : rowValuePredicand inPredicatePart2
    ;

// <in predicate part 2>
inPredicatePart2
    : (T__125)? T__92 inPredicateValue
    ;

// <in predicate value>
inPredicateValue
    : tableSubquery
    | LEFT_PAREN inValueList RIGHT_PAREN
    ;

// <in value list>
inValueList
    : rowValueExpression ((COMMA rowValueExpression)*)?
    ;

// <like predicate>
likePredicate
    : characterLikePredicate
    | octetLikePredicate
    ;

// <character like predicate>
characterLikePredicate
    : rowValuePredicand characterLikePredicatePart2
    ;

// <character like predicate part 2>
characterLikePredicatePart2
    : (T__125)? T__180 characterPattern (T__18 escapeCharacter)?
    ;

// <character pattern>
characterPattern
    : characterValueExpression
    ;

// <escape character>
escapeCharacter
    : characterValueExpression
    ;

// <octet like predicate>
octetLikePredicate
    : rowValuePredicand octetLikePredicatePart2
    ;

// <octet like predicate part 2>
octetLikePredicatePart2
    : (T__125)? T__180 octetPattern (T__18 escapeOctet)?
    ;

// <octet pattern>
octetPattern
    : blobConcatenation
    ;

// <escape octet>
escapeOctet
    : blobConcatenation
    ;

// <similar predicate>
similarPredicate
    : rowValuePredicand similarPredicatePart2
    ;

// <similar predicate part 2>
similarPredicatePart2
    : (T__125)? T__107 T__177 similarPattern (T__18 escapeCharacter)?
    ;

// <similar pattern>
similarPattern
    : characterValueExpression
    ;

// <regular expression>
regularExpression
    : regularTerm
    | regularExpression T__14 regularTerm
    ;

// <regular term>
regularTerm
    : regularFactor
    | regularTerm regularFactor
    ;

// <regular factor>
regularFactor
    : regularPrimary
    | regularPrimary T__17
    | regularPrimary T__10
    | regularPrimary T__68
    | regularPrimary repeatFactor
    ;

// <repeat factor>
repeatFactor
    : T__181 lowValue (upperLimit)? T__182
    ;

// <upper limit>
upperLimit
    : COMMA (highValue)?
    ;

// <low value>
lowValue
    : NUMBER
    ;

// <high value>
highValue
    : NUMBER
    ;

// <regular primary>
regularPrimary
    : characterSpecifier
    | T__183
    | regularCharacterSet
    | LEFT_PAREN regularExpression RIGHT_PAREN
    ;

// <character specifier>
characterSpecifier
    : non_escapedCharacter
    | escapedCharacter
    ;

// <regular character set>
regularCharacterSet
    : T__184
    | T__0 characterEnumeration T__1
    | T__0 T__185 characterEnumeration T__1
    | T__0 characterEnumerationInclude T__185 characterEnumerationExclude T__1
    ;

// <character enumeration include>
characterEnumerationInclude
    : characterEnumeration
    ;

// <character enumeration exclude>
characterEnumerationExclude
    : characterEnumeration
    ;

// <character enumeration>
characterEnumeration
    : characterSpecifier
    | characterSpecifier T__15 characterSpecifier
    | T__0 T__16 regularCharacterSetIdentifier T__16 T__1
    ;

// <regular character set identifier>
regularCharacterSetIdentifier
    : IDENTIFIER
    ;

// <null predicate>
nullPredicate
    : rowValuePredicand nullPredicatePart2
    ;

// <null predicate part 2>
nullPredicatePart2
    : T__126 (T__125)? T__70
    ;

// <quantified comparison predicate>
quantifiedComparisonPredicate
    : rowValuePredicand quantifiedComparisonPredicatePart2
    ;

// <quantified comparison predicate part 2>
quantifiedComparisonPredicatePart2
    : compOp quantifier tableSubquery
    ;

// <quantifier>
quantifier
    : all
    | some
    ;

// <all>
all
    : T__128
    ;

// <some>
some
    : T__186
    | T__187
    ;

// <exists predicate>
existsPredicate
    : T__188 tableSubquery
    ;

// <unique predicate>
uniquePredicate
    : T__189 tableSubquery
    ;

// <normalized predicate>
normalizedPredicate
    : stringValueExpression T__126 (T__125)? T__190
    ;

// <match predicate>
matchPredicate
    : rowValuePredicand matchPredicatePart2
    ;

// <match predicate part 2>
matchPredicatePart2
    : T__191 (T__189)? (T__192 | T__193 | T__147)? tableSubquery
    ;

// <overlaps predicate>
overlapsPredicate
    : overlapsPredicatePart1 overlapsPredicatePart2
    ;

// <overlaps predicate part 1>
overlapsPredicatePart1
    : rowValuePredicand1
    ;

// <overlaps predicate part 2>
overlapsPredicatePart2
    : T__194 rowValuePredicand2
    ;

// <row value predicand 1>
rowValuePredicand1
    : rowValuePredicand
    ;

// <row value predicand 2>
rowValuePredicand2
    : rowValuePredicand
    ;

// <distinct predicate>
distinctPredicate
    : rowValuePredicand3 distinctPredicatePart2
    ;

// <distinct predicate part 2>
distinctPredicatePart2
    : T__126 T__129 T__95 rowValuePredicand4
    ;

// <row value predicand 3>
rowValuePredicand3
    : rowValuePredicand
    ;

// <row value predicand 4>
rowValuePredicand4
    : rowValuePredicand
    ;

// <member predicate>
memberPredicate
    : rowValuePredicand memberPredicatePart2
    ;

// <member predicate part 2>
memberPredicatePart2
    : (T__125)? T__195 (T__196)? multisetValueExpression
    ;

// <submultiset predicate>
submultisetPredicate
    : rowValuePredicand submultisetPredicatePart2
    ;

// <submultiset predicate part 2>
submultisetPredicatePart2
    : (T__125)? T__197 (T__196)? multisetValueExpression
    ;

// <set predicate>
setPredicate
    : rowValuePredicand setPredicatePart2
    ;

// <set predicate part 2>
setPredicatePart2
    : T__126 (T__125)? T__198 T__28
    ;

// <type predicate>
typePredicate
    : rowValuePredicand typePredicatePart2
    ;

// <type predicate part 2>
typePredicatePart2
    : T__126 (T__125)? T__196 LEFT_PAREN typeList RIGHT_PAREN
    ;

// <type list>
typeList
    : user_definedTypeSpecification ((COMMA user_definedTypeSpecification)*)?
    ;

// <user-defined type specification>
user_definedTypeSpecification
    : inclusiveUser_definedTypeSpecification
    | exclusiveUser_definedTypeSpecification
    ;

// <inclusive user-defined type specification>
inclusiveUser_definedTypeSpecification
    : path_resolvedUser_definedTypeName
    ;

// <exclusive user-defined type specification>
exclusiveUser_definedTypeSpecification
    : T__138 path_resolvedUser_definedTypeName
    ;

// <search condition>
searchCondition
    : booleanValueExpression
    ;

// <interval qualifier>
intervalQualifier
    : startField T__177 endField
    | singleDatetimeField
    ;

// <start field>
startField
    : non_secondPrimaryDatetimeField (LEFT_PAREN intervalLeadingFieldPrecision RIGHT_PAREN)?
    ;

// <end field>
endField
    : non_secondPrimaryDatetimeField
    | T__199 (LEFT_PAREN intervalFractionalSecondsPrecision RIGHT_PAREN)?
    ;

// <single datetime field>
singleDatetimeField
    : non_secondPrimaryDatetimeField (LEFT_PAREN intervalLeadingFieldPrecision RIGHT_PAREN)?
    | T__199 (LEFT_PAREN intervalLeadingFieldPrecision (COMMA intervalFractionalSecondsPrecision)? RIGHT_PAREN)?
    ;

// <primary datetime field>
primaryDatetimeField
    : non_secondPrimaryDatetimeField
    | T__199
    ;

// <non-second primary datetime field>
non_secondPrimaryDatetimeField
    : T__200
    | T__201
    | T__202
    | T__203
    | T__204
    ;

// <interval fractional seconds precision>
intervalFractionalSecondsPrecision
    : NUMBER
    ;

// <interval leading field precision>
intervalLeadingFieldPrecision
    : NUMBER
    ;

// <language clause>
languageClause
    : T__205 languageName
    ;

// <language name>
languageName
    : T__206
    | T__207
    | T__208
    | T__209
    | T__210
    | T__211
    | T__212
    | T__213
    ;

// <path specification>
pathSpecification
    : T__214 schemaNameList
    ;

// <schema name list>
schemaNameList
    : schemaName ((COMMA schemaName)*)?
    ;

// <routine invocation>
routineInvocation
    : IDENTIFIER sqlArgumentList
    ;

// <SQL argument list>
sqlArgumentList
    : LEFT_PAREN (sqlArgument ((COMMA sqlArgument)*)?)? RIGHT_PAREN
    ;

// <SQL argument>
sqlArgument
    : valueExpression
    | generalizedExpression
    | targetSpecification
    ;

// <generalized expression>
generalizedExpression
    : valueExpression T__83 path_resolvedUser_definedTypeName
    ;

// <character set specification>
characterSetSpecification
    : standardCharacterSetName
    | implementation_definedCharacterSetName
    | user_definedCharacterSetName
    ;

// <standard character set name>
standardCharacterSetName
    : IDENTIFIER
    ;

// <implementation-defined character set name>
implementation_definedCharacterSetName
    : IDENTIFIER
    ;

// <user-defined character set name>
user_definedCharacterSetName
    : IDENTIFIER
    ;

// <specific routine designator>
specificRoutineDesignator
    : T__215 routineType IDENTIFIER
    | routineType memberName (T__85 schema_resolvedUser_definedTypeName)?
    ;

// <routine type>
routineType
    : T__216
    | T__217
    | T__218
    | (T__219 | T__220 | T__221)? T__222
    ;

// <member name>
memberName
    : memberNameAlternatives (dataTypeList)?
    ;

// <member name alternatives>
memberNameAlternatives
    : schemaQualifiedRoutineName
    | IDENTIFIER
    ;

// <data type list>
dataTypeList
    : LEFT_PAREN (arrayType ((COMMA arrayType)*)?)? RIGHT_PAREN
    ;

// <collate clause>
collateClause
    : T__223 IDENTIFIER
    ;

// <constraint name definition>
constraintNameDefinition
    : T__224 constraintName
    ;

// <constraint characteristics>
constraintCharacteristics
    : constraintCheckTime ((T__125)? T__225)?
    | (T__125)? T__225 (constraintCheckTime)?
    ;

// <constraint check time>
constraintCheckTime
    : T__226 T__227
    | T__226 T__228
    ;

// <aggregate function>
aggregateFunction
    : T__229 LEFT_PAREN T__17 RIGHT_PAREN (filterClause)?
    | generalSetFunction (filterClause)?
    | binarySetFunction (filterClause)?
    | orderedSetFunction (filterClause)?
    ;

// <general set function>
generalSetFunction
    : setFunctionType LEFT_PAREN (setQuantifier)? valueExpression RIGHT_PAREN
    ;

// <set function type>
setFunctionType
    : computationalOperation
    ;

// <computational operation>
computationalOperation
    : T__230
    | T__231
    | T__232
    | T__233
    | T__234
    | T__187
    | T__186
    | T__229
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__235
    | T__236
    | T__237
    ;

// <set quantifier>
setQuantifier
    : T__129
    | T__128
    ;

// <filter clause>
filterClause
    : T__238 LEFT_PAREN T__148 searchCondition RIGHT_PAREN
    ;

// <binary set function>
binarySetFunction
    : binarySetFunctionType LEFT_PAREN dependentVariableExpression COMMA independentVariableExpression RIGHT_PAREN
    ;

// <binary set function type>
binarySetFunctionType
    : IDENTIFIER
    | IDENTIFIER
    | T__239
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

// <dependent variable expression>
dependentVariableExpression
    : numericValueExpression
    ;

// <independent variable expression>
independentVariableExpression
    : numericValueExpression
    ;

// <ordered set function>
orderedSetFunction
    : hypotheticalSetFunction
    | inverseDistributionFunction
    ;

// <hypothetical set function>
hypotheticalSetFunction
    : rankFunctionType LEFT_PAREN hypotheticalSetFunctionValueExpressionList RIGHT_PAREN withinGroupSpecification
    ;

// <within group specification>
withinGroupSpecification
    : T__240 T__149 LEFT_PAREN T__157 T__150 sortSpecificationList RIGHT_PAREN
    ;

// <hypothetical set function value expression list>
hypotheticalSetFunctionValueExpressionList
    : valueExpression ((COMMA valueExpression)*)?
    ;

// <inverse distribution function>
inverseDistributionFunction
    : inverseDistributionFunctionType LEFT_PAREN inverseDistributionFunctionArgument RIGHT_PAREN withinGroupSpecification
    ;

// <inverse distribution function argument>
inverseDistributionFunctionArgument
    : numericValueExpression
    ;

// <inverse distribution function type>
inverseDistributionFunctionType
    : IDENTIFIER
    | IDENTIFIER
    ;

// <sort specification list>
sortSpecificationList
    : sortSpecification ((COMMA sortSpecification)*)?
    ;

// <sort specification>
sortSpecification
    : sortKey (orderingSpecification)? (nullOrdering)?
    ;

// <sort key>
sortKey
    : valueExpression
    ;

// <ordering specification>
orderingSpecification
    : T__241
    | T__242
    ;

// <null ordering>
nullOrdering
    : T__243 T__174
    | T__243 T__244
    ;

// <schema definition>
schemaDefinition
    : T__245 T__246 schemaName (schemaCharacterSetOrPath)? (schemaElement)?
    ;

// <schema character set or path>
schemaCharacterSetOrPath
    : schemaCharacterSetSpecification
    | schemaPathSpecification
    | schemaCharacterSetSpecification schemaPathSpecification
    | schemaPathSpecification schemaCharacterSetSpecification
    ;

// <schema authorization identifier>
schemaAuthorizationIdentifier
    : IDENTIFIER
    ;

// <schema character set specification>
schemaCharacterSetSpecification
    : T__71 T__27 T__28 characterSetSpecification
    ;

// <schema path specification>
schemaPathSpecification
    : pathSpecification
    ;

// <schema element>
schemaElement
    : tableDefinition
    | viewDefinition
    | domainDefinition
    | characterSetDefinition
    | collationDefinition
    | transliterationDefinition
    | assertionDefinition
    | triggerDefinition
    | user_definedTypeDefinition
    | user_definedCastDefinition
    | user_definedOrderingDefinition
    | transformDefinition
    | schemaRoutine
    | sequenceGeneratorDefinition
    | grantStatement
    | roleDefinition
    ;

// <drop schema statement>
dropSchemaStatement
    : T__247 T__246 schemaName (dropBehavior)?
    ;

// <drop behavior>
dropBehavior
    : T__248
    | T__249
    ;

// <table definition>
tableDefinition
    : T__245 (tableScope)? T__132 tableName tableContentsSource (T__142 T__250 tableCommitAction T__158)?
    ;

// <table contents source>
tableContentsSource
    : tableElementList
    | T__196 path_resolvedUser_definedTypeName (subtableClause)? (tableElementList)?
    | asSubqueryClause
    ;

// <table scope>
tableScope
    : globalOrLocal T__251
    ;

// <global or local>
globalOrLocal
    : T__25
    | T__26
    ;

// <table commit action>
tableCommitAction
    : T__252
    | T__253
    ;

// <table element list>
tableElementList
    : LEFT_PAREN tableElement ((COMMA tableElement)*)? RIGHT_PAREN
    ;

// <table element>
tableElement
    : columnDefinition
    | tableConstraintDefinition
    | likeClause
    | self_referencingColumnSpecification
    | columnOptions
    ;

// <self-referencing column specification>
self_referencingColumnSpecification
    : T__62 T__126 self_referencingColumnName referenceGeneration
    ;

// <reference generation>
referenceGeneration
    : T__136 T__254
    | T__66 T__254
    | T__255
    ;

// <self-referencing column name>
self_referencingColumnName
    : columnName
    ;

// <column options>
columnOptions
    : columnName T__57 T__256 columnOptionList
    ;

// <column option list>
columnOptionList
    : (scopeClause)? (defaultClause)? (columnConstraintDefinition)?
    ;

// <subtable clause>
subtableClause
    : T__257 supertableClause
    ;

// <supertable clause>
supertableClause
    : supertableName
    ;

// <supertable name>
supertableName
    : tableName
    ;

// <like clause>
likeClause
    : T__180 tableName (likeOptions)?
    ;

// <like options>
likeOptions
    : identityOption
    | columnDefaultOption
    ;

// <identity option>
identityOption
    : T__258 T__259
    | T__260 T__259
    ;

// <column default option>
columnDefaultOption
    : T__258 T__261
    | T__260 T__261
    ;

// <as subquery clause>
asSubqueryClause
    : (LEFT_PAREN columnNameList RIGHT_PAREN)? T__83 subquery withOrWithoutData
    ;

// <with or without data>
withOrWithoutData
    : T__57 T__167 T__262
    | T__57 T__262
    ;

// <column definition>
columnDefinition
    : columnName (arrayType | domainName)? (referenceScopeCheck)? (defaultClause | identityColumnSpecification | generationClause)? (columnConstraintDefinition)? (collateClause)?
    ;

// <column constraint definition>
columnConstraintDefinition
    : (constraintNameDefinition)? columnConstraint (constraintCharacteristics)?
    ;

// <column constraint>
columnConstraint
    : T__125 T__70
    | uniqueSpecification
    | referencesSpecification
    | checkConstraintDefinition
    ;

// <reference scope check>
referenceScopeCheck
    : T__263 T__264 (T__125)? T__265 (T__142 T__253 referenceScopeCheckAction)?
    ;

// <reference scope check action>
referenceScopeCheckAction
    : referentialAction
    ;

// <identity column specification>
identityColumnSpecification
    : T__254 (T__266 | T__150 T__71) T__83 T__259 (LEFT_PAREN commonSequenceGeneratorOptions RIGHT_PAREN)?
    ;

// <generation clause>
generationClause
    : generationRule T__83 generationExpression
    ;

// <generation rule>
generationRule
    : T__254 T__266
    ;

// <generation expression>
generationExpression
    : LEFT_PAREN valueExpression RIGHT_PAREN
    ;

// <default clause>
defaultClause
    : T__71 defaultOption
    ;

// <default option>
defaultOption
    : literal
    | datetimeValueFunction
    | T__66
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | implicitlyTypedValueSpecification
    ;

// <table constraint definition>
tableConstraintDefinition
    : (constraintNameDefinition)? tableConstraint (constraintCharacteristics)?
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
    | T__189 IDENTIFIER T__67 IDENTIFIER
    ;

// <unique specification>
uniqueSpecification
    : T__189
    | T__267 T__268
    ;

// <unique column list>
uniqueColumnList
    : columnNameList
    ;

// <referential constraint definition>
referentialConstraintDefinition
    : T__269 T__268 LEFT_PAREN referencingColumns RIGHT_PAREN referencesSpecification
    ;

// <references specification>
referencesSpecification
    : T__263 referencedTableAndColumns (T__191 matchType)? (referentialTriggeredAction)?
    ;

// <match type>
matchType
    : T__147
    | T__193
    | T__192
    ;

// <referencing columns>
referencingColumns
    : referenceColumnList
    ;

// <referenced table and columns>
referencedTableAndColumns
    : tableName (LEFT_PAREN referenceColumnList RIGHT_PAREN)?
    ;

// <reference column list>
referenceColumnList
    : columnNameList
    ;

// <referential triggered action>
referentialTriggeredAction
    : updateRule (deleteRule)?
    | deleteRule (updateRule)?
    ;

// <update rule>
updateRule
    : T__142 T__270 referentialAction
    ;

// <delete rule>
deleteRule
    : T__142 T__253 referentialAction
    ;

// <referential action>
referentialAction
    : T__248
    | T__28 T__70
    | T__28 T__71
    | T__249
    | T__167 T__271
    ;

// <check constraint definition>
checkConstraintDefinition
    : T__272 LEFT_PAREN searchCondition RIGHT_PAREN
    ;

// <alter table statement>
alterTableStatement
    : T__273 T__132 tableName alterTableAction
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
    : T__274 (T__275)? columnDefinition
    ;

// <alter column definition>
alterColumnDefinition
    : T__273 (T__275)? columnName alterColumnAction
    ;

// <alter column action>
alterColumnAction
    : setColumnDefaultClause
    | dropColumnDefaultClause
    | addColumnScopeClause
    | dropColumnScopeClause
    | alterIdentityColumnSpecification
    ;

// <set column default clause>
setColumnDefaultClause
    : T__28 defaultClause
    ;

// <drop column default clause>
dropColumnDefaultClause
    : T__247 T__71
    ;

// <add column scope clause>
addColumnScopeClause
    : T__274 scopeClause
    ;

// <drop column scope clause>
dropColumnScopeClause
    : T__247 T__63 dropBehavior
    ;

// <alter identity column specification>
alterIdentityColumnSpecification
    : alterIdentityColumnOption
    ;

// <alter identity column option>
alterIdentityColumnOption
    : alterSequenceGeneratorRestartOption
    | T__28 basicSequenceGeneratorOption
    ;

// <drop column definition>
dropColumnDefinition
    : T__247 (T__275)? columnName dropBehavior
    ;

// <add table constraint definition>
addTableConstraintDefinition
    : T__274 tableConstraintDefinition
    ;

// <drop table constraint definition>
dropTableConstraintDefinition
    : T__247 T__224 constraintName dropBehavior
    ;

// <drop table statement>
dropTableStatement
    : T__247 T__132 tableName dropBehavior
    ;

// <view definition>
viewDefinition
    : T__245 (T__170)? T__276 tableName viewSpecification T__83 queryExpression (T__57 (levelsClause)? T__272 T__277)?
    ;

// <view specification>
viewSpecification
    : regularViewSpecification
    | referenceableViewSpecification
    ;

// <regular view specification>
regularViewSpecification
    : (LEFT_PAREN viewColumnList RIGHT_PAREN)?
    ;

// <referenceable view specification>
referenceableViewSpecification
    : T__196 path_resolvedUser_definedTypeName (subviewClause)? (viewElementList)?
    ;

// <subview clause>
subviewClause
    : T__257 tableName
    ;

// <view element list>
viewElementList
    : LEFT_PAREN viewElement ((COMMA viewElement)*)? RIGHT_PAREN
    ;

// <view element>
viewElement
    : self_referencingColumnSpecification
    | viewColumnOption
    ;

// <view column option>
viewColumnOption
    : columnName T__57 T__256 scopeClause
    ;

// <levels clause>
levelsClause
    : T__278
    | T__26
    ;

// <view column list>
viewColumnList
    : columnNameList
    ;

// <drop view statement>
dropViewStatement
    : T__247 T__276 tableName dropBehavior
    ;

// <domain definition>
domainDefinition
    : T__245 T__279 domainName (T__83)? arrayType (defaultClause)? (domainConstraint)? (collateClause)?
    ;

// <domain constraint>
domainConstraint
    : (constraintNameDefinition)? checkConstraintDefinition (constraintCharacteristics)?
    ;

// <alter domain statement>
alterDomainStatement
    : T__273 T__279 domainName alterDomainAction
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
    : T__247 T__71
    ;

// <add domain constraint definition>
addDomainConstraintDefinition
    : T__274 domainConstraint
    ;

// <drop domain constraint definition>
dropDomainConstraintDefinition
    : T__247 T__224 constraintName
    ;

// <drop domain statement>
dropDomainStatement
    : T__247 T__279 domainName dropBehavior
    ;

// <character set definition>
characterSetDefinition
    : T__245 T__27 T__28 IDENTIFIER (T__83)? characterSetSource (collateClause)?
    ;

// <character set source>
characterSetSource
    : T__280 characterSetSpecification
    ;

// <drop character set statement>
dropCharacterSetStatement
    : T__247 T__27 T__28 IDENTIFIER
    ;

// <collation definition>
collationDefinition
    : T__245 T__281 IDENTIFIER T__85 characterSetSpecification T__95 existingCollationName (padCharacteristic)?
    ;

// <existing collation name>
existingCollationName
    : IDENTIFIER
    ;

// <pad characteristic>
padCharacteristic
    : T__167 T__282
    | T__282 T__283
    ;

// <drop collation statement>
dropCollationStatement
    : T__247 T__281 IDENTIFIER dropBehavior
    ;

// <transliteration definition>
transliterationDefinition
    : T__245 T__284 transliterationName T__85 sourceCharacterSetSpecification T__177 targetCharacterSetSpecification T__95 transliterationSource
    ;

// <source character set specification>
sourceCharacterSetSpecification
    : characterSetSpecification
    ;

// <target character set specification>
targetCharacterSetSpecification
    : characterSetSpecification
    ;

// <transliteration source>
transliterationSource
    : existingTransliterationName
    | transliterationRoutine
    ;

// <existing transliteration name>
existingTransliterationName
    : transliterationName
    ;

// <transliteration routine>
transliterationRoutine
    : specificRoutineDesignator
    ;

// <drop transliteration statement>
dropTransliterationStatement
    : T__247 T__284 transliterationName
    ;

// <assertion definition>
assertionDefinition
    : T__245 T__285 constraintName T__272 LEFT_PAREN searchCondition RIGHT_PAREN (constraintCharacteristics)?
    ;

// <drop assertion statement>
dropAssertionStatement
    : T__247 T__285 constraintName
    ;

// <trigger definition>
triggerDefinition
    : T__245 T__286 triggerName triggerActionTime triggerEvent T__142 tableName (T__287 oldOrNewValuesAliasList)? triggeredAction
    ;

// <trigger action time>
triggerActionTime
    : T__288
    | T__289
    ;

// <trigger event>
triggerEvent
    : T__290
    | T__253
    | T__270 (T__196 triggerColumnList)?
    ;

// <trigger column list>
triggerColumnList
    : columnNameList
    ;

// <triggered action>
triggeredAction
    : (T__85 T__291 (T__61 | T__292))? (T__79 LEFT_PAREN searchCondition RIGHT_PAREN)? triggeredSqlStatement
    ;

// <triggered SQL statement>
triggeredSqlStatement
    : sqlProcedureStatement
    | T__293 T__294 (sqlProcedureStatement SEMI)* T__78
    ;

// <old or new values alias list>
oldOrNewValuesAliasList
    : oldOrNewValuesAlias
    ;

// <old or new values alias>
oldOrNewValuesAlias
    : T__295 (T__61)? (T__83)? oldValuesCorrelationName
    | T__87 (T__61)? (T__83)? newValuesCorrelationName
    | T__295 T__132 (T__83)? oldValuesTableAlias
    | T__87 T__132 (T__83)? newValuesTableAlias
    ;

// <old values table alias>
oldValuesTableAlias
    : IDENTIFIER
    ;

// <new values table alias>
newValuesTableAlias
    : IDENTIFIER
    ;

// <old values correlation name>
oldValuesCorrelationName
    : correlationName
    ;

// <new values correlation name>
newValuesCorrelationName
    : correlationName
    ;

// <drop trigger statement>
dropTriggerStatement
    : T__247 T__286 triggerName
    ;

// <user-defined type definition>
user_definedTypeDefinition
    : T__245 T__296 user_definedTypeBody
    ;

// <user-defined type body>
user_definedTypeBody
    : schema_resolvedUser_definedTypeName (subtypeClause)? (T__83 representation)? (user_definedTypeOptionList)? (methodSpecificationList)?
    ;

// <user-defined type option list>
user_definedTypeOptionList
    : user_definedTypeOption (user_definedTypeOption)?
    ;

// <user-defined type option>
user_definedTypeOption
    : instantiableClause
    | finality
    | referenceTypeSpecification
    | refCastOption
    | castOption
    ;

// <subtype clause>
subtypeClause
    : T__257 supertypeName
    ;

// <supertype name>
supertypeName
    : path_resolvedUser_definedTypeName
    ;

// <representation>
representation
    : predefinedType
    | memberList
    ;

// <member list>
memberList
    : LEFT_PAREN member ((COMMA member)*)? RIGHT_PAREN
    ;

// <member>
member
    : attributeDefinition
    ;

// <instantiable clause>
instantiableClause
    : T__297
    | T__125 T__297
    ;

// <finality>
finality
    : T__298
    | T__125 T__298
    ;

// <reference type specification>
referenceTypeSpecification
    : user_definedRepresentation
    | derivedRepresentation
    | system_generatedRepresentation
    ;

// <user-defined representation>
user_definedRepresentation
    : T__62 T__93 predefinedType
    ;

// <derived representation>
derivedRepresentation
    : T__62 T__95 listOfAttributes
    ;

// <system-generated representation>
system_generatedRepresentation
    : T__62 T__126 T__136 T__254
    ;

// <ref cast option>
refCastOption
    : (castToRef)? (castToType)?
    ;

// <cast to ref>
castToRef
    : T__82 LEFT_PAREN T__299 T__83 T__62 RIGHT_PAREN T__57 castToRefIdentifier
    ;

// <cast to ref identifier>
castToRefIdentifier
    : IDENTIFIER
    ;

// <cast to type>
castToType
    : T__82 LEFT_PAREN T__62 T__83 T__299 RIGHT_PAREN T__57 castToTypeIdentifier
    ;

// <cast to type identifier>
castToTypeIdentifier
    : IDENTIFIER
    ;

// <list of attributes>
listOfAttributes
    : LEFT_PAREN IDENTIFIER ((COMMA IDENTIFIER)*)? RIGHT_PAREN
    ;

// <cast option>
castOption
    : (castToDistinct)? (castToSource)?
    ;

// <cast to distinct>
castToDistinct
    : T__82 LEFT_PAREN T__299 T__83 T__129 RIGHT_PAREN T__57 castToDistinctIdentifier
    ;

// <cast to distinct identifier>
castToDistinctIdentifier
    : IDENTIFIER
    ;

// <cast to source>
castToSource
    : T__82 LEFT_PAREN T__129 T__83 T__299 RIGHT_PAREN T__57 castToSourceIdentifier
    ;

// <cast to source identifier>
castToSourceIdentifier
    : IDENTIFIER
    ;

// <method specification list>
methodSpecificationList
    : methodSpecification ((COMMA methodSpecification)*)?
    ;

// <method specification>
methodSpecification
    : originalMethodSpecification
    | overridingMethodSpecification
    ;

// <original method specification>
originalMethodSpecification
    : partialMethodSpecification (T__300 T__83 T__301)? (T__300 T__83 T__302)? (methodCharacteristics)?
    ;

// <overriding method specification>
overridingMethodSpecification
    : T__303 partialMethodSpecification
    ;

// <partial method specification>
partialMethodSpecification
    : (T__219 | T__220 | T__221)? T__222 IDENTIFIER sqlParameterDeclarationList returnsClause (T__215 specificMethodName)?
    ;

// <specific method name>
specificMethodName
    : (schemaName DOT)? IDENTIFIER
    ;

// <method characteristics>
methodCharacteristics
    : methodCharacteristic
    ;

// <method characteristic>
methodCharacteristic
    : languageClause
    | parameterStyleClause
    | deterministicCharacteristic
    | sQL_dataAccessIndication
    | null_callClause
    ;

// <attribute definition>
attributeDefinition
    : IDENTIFIER arrayType (referenceScopeCheck)? (attributeDefault)? (collateClause)?
    ;

// <attribute default>
attributeDefault
    : defaultClause
    ;

// <alter type statement>
alterTypeStatement
    : T__273 T__296 schema_resolvedUser_definedTypeName alterTypeAction
    ;

// <alter type action>
alterTypeAction
    : addAttributeDefinition
    | dropAttributeDefinition
    | addOriginalMethodSpecification
    | addOverridingMethodSpecification
    | dropMethodSpecification
    ;

// <add attribute definition>
addAttributeDefinition
    : T__274 T__304 attributeDefinition
    ;

// <drop attribute definition>
dropAttributeDefinition
    : T__247 T__304 IDENTIFIER T__249
    ;

// <add original method specification>
addOriginalMethodSpecification
    : T__274 originalMethodSpecification
    ;

// <add overriding method specification>
addOverridingMethodSpecification
    : T__274 overridingMethodSpecification
    ;

// <drop method specification>
dropMethodSpecification
    : T__247 specificMethodSpecificationDesignator T__249
    ;

// <specific method specification designator>
specificMethodSpecificationDesignator
    : (T__219 | T__220 | T__221)? T__222 IDENTIFIER dataTypeList
    ;

// <drop data type statement>
dropDataTypeStatement
    : T__247 T__296 schema_resolvedUser_definedTypeName dropBehavior
    ;

// <SQL-invoked routine>
sQL_invokedRoutine
    : schemaRoutine
    ;

// <schema routine>
schemaRoutine
    : schemaProcedure
    | schemaFunction
    ;

// <schema procedure>
schemaProcedure
    : T__245 sQL_invokedProcedure
    ;

// <schema function>
schemaFunction
    : T__245 sQL_invokedFunction
    ;

// <SQL-invoked procedure>
sQL_invokedProcedure
    : T__218 schemaQualifiedRoutineName sqlParameterDeclarationList routineCharacteristics routineBody
    ;

// <SQL-invoked function>
sQL_invokedFunction
    : (functionSpecification | methodSpecificationDesignator) routineBody
    ;

// <SQL parameter declaration list>
sqlParameterDeclarationList
    : LEFT_PAREN (sqlParameterDeclaration ((COMMA sqlParameterDeclaration)*)?)? RIGHT_PAREN
    ;

// <SQL parameter declaration>
sqlParameterDeclaration
    : (parameterMode)? (sqlParameterName)? parameterType (T__301)?
    ;

// <parameter mode>
parameterMode
    : T__92
    | T__305
    | T__306
    ;

// <parameter type>
parameterType
    : arrayType (locatorIndication)?
    ;

// <locator indication>
locatorIndication
    : T__83 T__302
    ;

// <function specification>
functionSpecification
    : T__217 schemaQualifiedRoutineName sqlParameterDeclarationList returnsClause routineCharacteristics (dispatchClause)?
    ;

// <method specification designator>
methodSpecificationDesignator
    : T__215 T__222 specificMethodName
    | (T__219 | T__220 | T__221)? T__222 IDENTIFIER sqlParameterDeclarationList (returnsClause)? T__85 schema_resolvedUser_definedTypeName
    ;

// <routine characteristics>
routineCharacteristics
    : (routineCharacteristic)?
    ;

// <routine characteristic>
routineCharacteristic
    : languageClause
    | parameterStyleClause
    | T__215 IDENTIFIER
    | deterministicCharacteristic
    | sQL_dataAccessIndication
    | null_callClause
    | dynamicResultSetsCharacteristic
    | savepointLevelIndication
    ;

// <savepoint level indication>
savepointLevelIndication
    : T__87 T__307 T__308
    | T__295 T__307 T__308
    ;

// <dynamic result sets characteristic>
dynamicResultSetsCharacteristic
    : T__309 T__301 T__153 maximumDynamicResultSets
    ;

// <parameter style clause>
parameterStyleClause
    : T__310 T__311 parameterStyle
    ;

// <dispatch clause>
dispatchClause
    : T__220 T__312
    ;

// <returns clause>
returnsClause
    : T__313 returnsType
    ;

// <returns type>
returnsType
    : returnsDataType (resultCast)?
    | returnsTableType
    ;

// <returns table type>
returnsTableType
    : T__132 tableFunctionColumnList
    ;

// <table function column list>
tableFunctionColumnList
    : LEFT_PAREN tableFunctionColumnListElement ((COMMA tableFunctionColumnListElement)*)? RIGHT_PAREN
    ;

// <table function column list element>
tableFunctionColumnListElement
    : columnName arrayType
    ;

// <result cast>
resultCast
    : T__82 T__95 resultCastFromType
    ;

// <result cast from type>
resultCastFromType
    : arrayType (locatorIndication)?
    ;

// <returns data type>
returnsDataType
    : arrayType (locatorIndication)?
    ;

// <routine body>
routineBody
    : sqlRoutineSpec
    | externalBodyReference
    ;

// <SQL routine spec>
sqlRoutineSpec
    : (rightsClause)? sqlRoutineBody
    ;

// <rights clause>
rightsClause
    : T__213 T__314 T__315
    | T__213 T__314 T__316
    ;

// <SQL routine body>
sqlRoutineBody
    : sqlProcedureStatement
    ;

// <external body reference>
externalBodyReference
    : T__317 (T__318 externalRoutineName)? (parameterStyleClause)? (transformGroupSpecification)? (externalSecurityClause)?
    ;

// <external security clause>
externalSecurityClause
    : T__317 T__314 T__316
    | T__317 T__314 T__315
    | T__317 T__314 T__319 T__320
    ;

// <parameter style>
parameterStyle
    : T__213
    | T__321
    ;

// <deterministic characteristic>
deterministicCharacteristic
    : T__322
    | T__125 T__322
    ;

// <SQL-data access indication>
sQL_dataAccessIndication
    : T__167 T__213
    | T__323 T__213
    | T__324 T__213 T__262
    | T__325 T__213 T__262
    ;

// <null-call clause>
null_callClause
    : T__313 T__70 T__142 T__70 T__326
    | T__327 T__142 T__70 T__326
    ;

// <maximum dynamic result sets>
maximumDynamicResultSets
    : NUMBER
    ;

// <transform group specification>
transformGroupSpecification
    : T__328 T__149 (singleGroupSpecification | multipleGroupSpecification)
    ;

// <single group specification>
singleGroupSpecification
    : groupName
    ;

// <multiple group specification>
multipleGroupSpecification
    : groupSpecification ((COMMA groupSpecification)*)?
    ;

// <group specification>
groupSpecification
    : groupName T__85 T__296 path_resolvedUser_definedTypeName
    ;

// <alter routine statement>
alterRoutineStatement
    : T__273 specificRoutineDesignator alterRoutineCharacteristics alterRoutineBehavior
    ;

// <alter routine characteristics>
alterRoutineCharacteristics
    : alterRoutineCharacteristic
    ;

// <alter routine characteristic>
alterRoutineCharacteristic
    : languageClause
    | parameterStyleClause
    | sQL_dataAccessIndication
    | null_callClause
    | dynamicResultSetsCharacteristic
    | T__318 externalRoutineName
    ;

// <alter routine behavior>
alterRoutineBehavior
    : T__249
    ;

// <drop routine statement>
dropRoutineStatement
    : T__247 specificRoutineDesignator dropBehavior
    ;

// <user-defined cast definition>
user_definedCastDefinition
    : T__245 T__82 LEFT_PAREN sourceDataType T__83 targetDataType RIGHT_PAREN T__57 castFunction (T__83 T__329)?
    ;

// <cast function>
castFunction
    : specificRoutineDesignator
    ;

// <source data type>
sourceDataType
    : arrayType
    ;

// <target data type>
targetDataType
    : arrayType
    ;

// <drop user-defined cast statement>
dropUser_definedCastStatement
    : T__247 T__82 LEFT_PAREN sourceDataType T__83 targetDataType RIGHT_PAREN dropBehavior
    ;

// <user-defined ordering definition>
user_definedOrderingDefinition
    : T__245 T__330 T__85 schema_resolvedUser_definedTypeName orderingForm
    ;

// <ordering form>
orderingForm
    : equalsOrderingForm
    | fullOrderingForm
    ;

// <equals ordering form>
equalsOrderingForm
    : T__331 T__138 T__150 orderingCategory
    ;

// <full ordering form>
fullOrderingForm
    : T__157 T__147 T__150 orderingCategory
    ;

// <ordering category>
orderingCategory
    : relativeCategory
    | mapCategory
    | stateCategory
    ;

// <relative category>
relativeCategory
    : T__332 T__57 relativeFunctionSpecification
    ;

// <map category>
mapCategory
    : T__333 T__57 mapFunctionSpecification
    ;

// <state category>
stateCategory
    : T__334 (IDENTIFIER)?
    ;

// <relative function specification>
relativeFunctionSpecification
    : specificRoutineDesignator
    ;

// <map function specification>
mapFunctionSpecification
    : specificRoutineDesignator
    ;

// <drop user-defined ordering statement>
dropUser_definedOrderingStatement
    : T__247 T__330 T__85 schema_resolvedUser_definedTypeName dropBehavior
    ;

// <transform definition>
transformDefinition
    : T__245 (T__328 | T__335) T__85 schema_resolvedUser_definedTypeName transformGroup
    ;

// <transform group>
transformGroup
    : groupName LEFT_PAREN transformElementList RIGHT_PAREN
    ;

// <group name>
groupName
    : IDENTIFIER
    ;

// <transform element list>
transformElementList
    : transformElement (COMMA transformElement)?
    ;

// <transform element>
transformElement
    : toSql
    | fromSql
    ;

// <to sql>
toSql
    : T__177 T__213 T__57 toSqlFunction
    ;

// <from sql>
fromSql
    : T__95 T__213 T__57 fromSqlFunction
    ;

// <to sql function>
toSqlFunction
    : specificRoutineDesignator
    ;

// <from sql function>
fromSqlFunction
    : specificRoutineDesignator
    ;

// <alter transform statement>
alterTransformStatement
    : T__273 (T__328 | T__335) T__85 schema_resolvedUser_definedTypeName alterGroup
    ;

// <alter group>
alterGroup
    : groupName LEFT_PAREN alterTransformActionList RIGHT_PAREN
    ;

// <alter transform action list>
alterTransformActionList
    : alterTransformAction ((COMMA alterTransformAction)*)?
    ;

// <alter transform action>
alterTransformAction
    : addTransformElementList
    | dropTransformElementList
    ;

// <add transform element list>
addTransformElementList
    : T__274 LEFT_PAREN transformElementList RIGHT_PAREN
    ;

// <drop transform element list>
dropTransformElementList
    : T__247 LEFT_PAREN transformKind (COMMA transformKind)? dropBehavior RIGHT_PAREN
    ;

// <transform kind>
transformKind
    : T__177 T__213
    | T__95 T__213
    ;

// <drop transform statement>
dropTransformStatement
    : T__247 (T__328 | T__335) transformsToBeDropped T__85 schema_resolvedUser_definedTypeName dropBehavior
    ;

// <transforms to be dropped>
transformsToBeDropped
    : T__128
    | transformGroupElement
    ;

// <transform group element>
transformGroupElement
    : groupName
    ;

// <sequence generator definition>
sequenceGeneratorDefinition
    : T__245 T__336 sequenceGeneratorName (sequenceGeneratorOptions)?
    ;

// <sequence generator options>
sequenceGeneratorOptions
    : sequenceGeneratorOption
    ;

// <sequence generator option>
sequenceGeneratorOption
    : sequenceGeneratorDataTypeOption
    | commonSequenceGeneratorOptions
    ;

// <common sequence generator options>
commonSequenceGeneratorOptions
    : commonSequenceGeneratorOption
    ;

// <common sequence generator option>
commonSequenceGeneratorOption
    : sequenceGeneratorStartWithOption
    | basicSequenceGeneratorOption
    ;

// <basic sequence generator option>
basicSequenceGeneratorOption
    : sequenceGeneratorIncrementByOption
    | sequenceGeneratorMaxvalueOption
    | sequenceGeneratorMinvalueOption
    | sequenceGeneratorCycleOption
    ;

// <sequence generator data type option>
sequenceGeneratorDataTypeOption
    : T__83 arrayType
    ;

// <sequence generator start with option>
sequenceGeneratorStartWithOption
    : T__337 T__57 sequenceGeneratorStartValue
    ;

// <sequence generator start value>
sequenceGeneratorStartValue
    : signedNumericLiteral
    ;

// <sequence generator increment by option>
sequenceGeneratorIncrementByOption
    : T__338 T__150 sequenceGeneratorIncrement
    ;

// <sequence generator increment>
sequenceGeneratorIncrement
    : signedNumericLiteral
    ;

// <sequence generator maxvalue option>
sequenceGeneratorMaxvalueOption
    : T__339 sequenceGeneratorMaxValue
    | T__167 T__339
    ;

// <sequence generator max value>
sequenceGeneratorMaxValue
    : signedNumericLiteral
    ;

// <sequence generator minvalue option>
sequenceGeneratorMinvalueOption
    : T__340 sequenceGeneratorMinValue
    | T__167 T__340
    ;

// <sequence generator min value>
sequenceGeneratorMinValue
    : signedNumericLiteral
    ;

// <sequence generator cycle option>
sequenceGeneratorCycleOption
    : T__176
    | T__167 T__176
    ;

// <alter sequence generator statement>
alterSequenceGeneratorStatement
    : T__273 T__336 sequenceGeneratorName alterSequenceGeneratorOptions
    ;

// <alter sequence generator options>
alterSequenceGeneratorOptions
    : alterSequenceGeneratorOption
    ;

// <alter sequence generator option>
alterSequenceGeneratorOption
    : alterSequenceGeneratorRestartOption
    | basicSequenceGeneratorOption
    ;

// <alter sequence generator restart option>
alterSequenceGeneratorRestartOption
    : T__341 T__57 sequenceGeneratorRestartValue
    ;

// <sequence generator restart value>
sequenceGeneratorRestartValue
    : signedNumericLiteral
    ;

// <drop sequence generator statement>
dropSequenceGeneratorStatement
    : T__247 T__336 sequenceGeneratorName dropBehavior
    ;

// <grant statement>
grantStatement
    : grantPrivilegeStatement
    | grantRoleStatement
    ;

// <grant privilege statement>
grantPrivilegeStatement
    : T__342 privileges T__177 grantee ((COMMA grantee)*)? (T__57 T__343 T__277)? (T__57 T__342 T__277)? (T__344 T__150 grantor)?
    ;

// <privileges>
privileges
    : objectPrivileges T__142 objectName
    ;

// <object name>
objectName
    : (T__132)? tableName
    | T__279 domainName
    | T__281 IDENTIFIER
    | T__27 T__28 IDENTIFIER
    | T__284 transliterationName
    | T__296 schema_resolvedUser_definedTypeName
    | T__336 sequenceGeneratorName
    | specificRoutineDesignator
    ;

// <object privileges>
objectPrivileges
    : T__128 T__345
    | action ((COMMA action)*)?
    ;

// <action>
action
    : T__169
    | T__169 LEFT_PAREN privilegeColumnList RIGHT_PAREN
    | T__169 LEFT_PAREN privilegeMethodList RIGHT_PAREN
    | T__253
    | T__290 (LEFT_PAREN privilegeColumnList RIGHT_PAREN)?
    | T__270 (LEFT_PAREN privilegeColumnList RIGHT_PAREN)?
    | T__263 (LEFT_PAREN privilegeColumnList RIGHT_PAREN)?
    | T__346
    | T__286
    | T__257
    | T__347
    ;

// <privilege method list>
privilegeMethodList
    : specificRoutineDesignator ((COMMA specificRoutineDesignator)*)?
    ;

// <privilege column list>
privilegeColumnList
    : columnNameList
    ;

// <grantee>
grantee
    : T__348
    | IDENTIFIER
    ;

// <grantor>
grantor
    : IDENTIFIER
    | IDENTIFIER
    ;

// <role definition>
roleDefinition
    : T__245 T__349 IDENTIFIER (T__57 T__350 grantor)?
    ;

// <grant role statement>
grantRoleStatement
    : T__342 roleGranted ((COMMA roleGranted)*)? T__177 grantee ((COMMA grantee)*)? (T__57 T__350 T__277)? (T__344 T__150 grantor)?
    ;

// <role granted>
roleGranted
    : IDENTIFIER
    ;

// <drop role statement>
dropRoleStatement
    : T__247 T__349 IDENTIFIER
    ;

// <revoke statement>
revokeStatement
    : revokePrivilegeStatement
    | revokeRoleStatement
    ;

// <revoke privilege statement>
revokePrivilegeStatement
    : T__351 (revokeOptionExtension)? privileges T__95 grantee ((COMMA grantee)*)? (T__344 T__150 grantor)? dropBehavior
    ;

// <revoke option extension>
revokeOptionExtension
    : T__342 T__277 T__85
    | T__343 T__277 T__85
    ;

// <revoke role statement>
revokeRoleStatement
    : T__351 (T__350 T__277 T__85)? roleRevoked ((COMMA roleRevoked)*)? T__95 grantee ((COMMA grantee)*)? (T__344 T__150 grantor)? dropBehavior
    ;

// <role revoked>
roleRevoked
    : IDENTIFIER
    ;

// <SQL-client module definition>
sQL_clientModuleDefinition
    : IDENTIFIER languageClause moduleAuthorizationClause (modulePathSpecification)? (moduleTransformGroupSpecification)? (moduleCollation)? (temporaryTableDeclaration)? moduleContents
    ;

// <module authorization clause>
moduleAuthorizationClause
    : T__246 schemaName
    | T__352 moduleAuthorizationIdentifier (T__85 T__220 (T__138 | T__124 T__309))?
    | T__246 schemaName T__352 moduleAuthorizationIdentifier (T__85 T__220 (T__138 | T__124 T__309))?
    ;

// <module authorization identifier>
moduleAuthorizationIdentifier
    : IDENTIFIER
    ;

// <module path specification>
modulePathSpecification
    : pathSpecification
    ;

// <module transform group specification>
moduleTransformGroupSpecification
    : transformGroupSpecification
    ;

// <module collation>
moduleCollation
    : moduleCollationSpecification
    ;

// <module collation specification>
moduleCollationSpecification
    : T__281 IDENTIFIER (T__85 characterSetSpecificationList)?
    ;

// <character set specification list>
characterSetSpecificationList
    : characterSetSpecification ((COMMA characterSetSpecification)*)?
    ;

// <module contents>
moduleContents
    : declareCursor
    | dynamicDeclareCursor
    | externally_invokedProcedure
    ;

// <module character set specification>
moduleCharacterSetSpecification
    : T__353 T__264 characterSetSpecification
    ;

// <externally-invoked procedure>
externally_invokedProcedure
    : T__218 procedureName hostParameterDeclarationList SEMI sqlProcedureStatement SEMI
    ;

// <host parameter declaration list>
hostParameterDeclarationList
    : LEFT_PAREN hostParameterDeclaration ((COMMA hostParameterDeclaration)*)? RIGHT_PAREN
    ;

// <host parameter declaration>
hostParameterDeclaration
    : hostParameterName hostParameterDataType
    | statusParameter
    ;

// <host parameter data type>
hostParameterDataType
    : arrayType (locatorIndication)?
    ;

// <status parameter>
statusParameter
    : T__354
    ;

// <SQL procedure statement>
sqlProcedureStatement
    : sqlExecutableStatement
    ;

// <SQL executable statement>
sqlExecutableStatement
    : sqlSchemaStatement
    | sqlDataStatement
    | sqlControlStatement
    | sqlTransactionStatement
    | sqlConnectionStatement
    | sqlSessionStatement
    | sqlDiagnosticsStatement
    | sqlDynamicStatement
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
    | sQL_invokedRoutine
    | grantStatement
    | roleDefinition
    | domainDefinition
    | characterSetDefinition
    | collationDefinition
    | transliterationDefinition
    | assertionDefinition
    | triggerDefinition
    | user_definedTypeDefinition
    | user_definedCastDefinition
    | user_definedOrderingDefinition
    | transformDefinition
    | sequenceGeneratorDefinition
    ;

// <SQL schema manipulation statement>
sqlSchemaManipulationStatement
    : dropSchemaStatement
    | alterTableStatement
    | dropTableStatement
    | dropViewStatement
    | alterRoutineStatement
    | dropRoutineStatement
    | dropUser_definedCastStatement
    | revokeStatement
    | dropRoleStatement
    | alterDomainStatement
    | dropDomainStatement
    | dropCharacterSetStatement
    | dropCollationStatement
    | dropTransliterationStatement
    | dropAssertionStatement
    | dropTriggerStatement
    | alterTypeStatement
    | dropDataTypeStatement
    | dropUser_definedOrderingStatement
    | alterTransformStatement
    | dropTransformStatement
    | alterSequenceGeneratorStatement
    | dropSequenceGeneratorStatement
    ;

// <SQL data statement>
sqlDataStatement
    : openStatement
    | fetchStatement
    | closeStatement
    | selectStatement_SingleRow
    | freeLocatorStatement
    | holdLocatorStatement
    | sqlDataChangeStatement
    ;

// <SQL data change statement>
sqlDataChangeStatement
    : deleteStatement_Positioned
    | deleteStatement_Searched
    | insertStatement
    | updateStatement_Positioned
    | updateStatement_Searched
    | mergeStatement
    ;

// <SQL control statement>
sqlControlStatement
    : callStatement
    | returnStatement
    ;

// <SQL transaction statement>
sqlTransactionStatement
    : startTransactionStatement
    | setTransactionStatement
    | setConstraintsModeStatement
    | savepointStatement
    | releaseSavepointStatement
    | commitStatement
    | rollbackStatement
    ;

// <SQL connection statement>
sqlConnectionStatement
    : connectStatement
    | setConnectionStatement
    | disconnectStatement
    ;

// <SQL session statement>
sqlSessionStatement
    : setSessionUserIdentifierStatement
    | setRoleStatement
    | setLocalTimeZoneStatement
    | setSessionCharacteristicsStatement
    | setCatalogStatement
    | setSchemaStatement
    | setNamesStatement
    | setPathStatement
    | setTransformGroupStatement
    | setSessionCollationStatement
    ;

// <SQL diagnostics statement>
sqlDiagnosticsStatement
    : getDiagnosticsStatement
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

// <SQL dynamic data statement>
sqlDynamicDataStatement
    : allocateCursorStatement
    | dynamicOpenStatement
    | dynamicFetchStatement
    | dynamicCloseStatement
    | dynamicDeleteStatement_Positioned
    | dynamicUpdateStatement_Positioned
    ;

// <system descriptor statement>
systemDescriptorStatement
    : allocateDescriptorStatement
    | deallocateDescriptorStatement
    | setDescriptorStatement
    | getDescriptorStatement
    ;

// <declare cursor>
declareCursor
    : T__355 IDENTIFIER (cursorSensitivity)? (cursorScrollability)? T__356 (cursorHoldability)? (cursorReturnability)? T__85 cursorSpecification
    ;

// <cursor sensitivity>
cursorSensitivity
    : T__357
    | T__358
    | T__359
    ;

// <cursor scrollability>
cursorScrollability
    : T__360
    | T__167 T__360
    ;

// <cursor holdability>
cursorHoldability
    : T__57 T__361
    | T__59 T__361
    ;

// <cursor returnability>
cursorReturnability
    : T__57 T__362
    | T__59 T__362
    ;

// <cursor specification>
cursorSpecification
    : queryExpression (orderByClause)? (updatabilityClause)?
    ;

// <updatability clause>
updatabilityClause
    : T__85 (T__363 T__138 | T__270 (T__196 columnNameList)?)
    ;

// <order by clause>
orderByClause
    : T__157 T__150 sortSpecificationList
    ;

// <open statement>
openStatement
    : T__364 IDENTIFIER
    ;

// <fetch statement>
fetchStatement
    : T__365 ((fetchOrientation)? T__95)? IDENTIFIER T__366 fetchTargetList
    ;

// <fetch orientation>
fetchOrientation
    : T__84
    | T__367
    | T__174
    | T__244
    | (T__368 | T__332) simpleValueSpecification
    ;

// <fetch target list>
fetchTargetList
    : targetSpecification ((COMMA targetSpecification)*)?
    ;

// <close statement>
closeStatement
    : T__369 IDENTIFIER
    ;

// <select statement: single row>
selectStatement_SingleRow
    : T__169 (setQuantifier)? selectList T__366 selectTargetList tableExpression
    ;

// <select target list>
selectTargetList
    : targetSpecification ((COMMA targetSpecification)*)?
    ;

// <delete statement: positioned>
deleteStatement_Positioned
    : T__253 T__95 targetTable T__148 T__162 T__196 IDENTIFIER
    ;

// <target table>
targetTable
    : tableName
    | T__138 LEFT_PAREN tableName RIGHT_PAREN
    ;

// <delete statement: searched>
deleteStatement_Searched
    : T__253 T__95 targetTable (T__148 searchCondition)?
    ;

// <insert statement>
insertStatement
    : T__290 T__366 insertionTarget insertColumnsAndSource
    ;

// <insertion target>
insertionTarget
    : tableName
    ;

// <insert columns and source>
insertColumnsAndSource
    : fromSubquery
    | fromConstructor
    | fromDefault
    ;

// <from subquery>
fromSubquery
    : (LEFT_PAREN insertColumnList RIGHT_PAREN)? (overrideClause)? queryExpression
    ;

// <from constructor>
fromConstructor
    : (LEFT_PAREN insertColumnList RIGHT_PAREN)? (overrideClause)? contextuallyTypedTableValueConstructor
    ;

// <override clause>
overrideClause
    : T__303 T__66 T__67
    | T__303 T__136 T__67
    ;

// <from default>
fromDefault
    : T__71 T__133
    ;

// <insert column list>
insertColumnList
    : columnNameList
    ;

// <merge statement>
mergeStatement
    : T__370 T__366 targetTable ((T__83)? mergeCorrelationName)? T__93 crossJoin T__142 searchCondition mergeOperationSpecification
    ;

// <merge correlation name>
mergeCorrelationName
    : correlationName
    ;

// <merge operation specification>
mergeOperationSpecification
    : mergeWhenClause
    ;

// <merge when clause>
mergeWhenClause
    : mergeWhenMatchedClause
    | mergeWhenNotMatchedClause
    ;

// <merge when matched clause>
mergeWhenMatchedClause
    : T__79 T__371 T__80 mergeUpdateSpecification
    ;

// <merge when not matched clause>
mergeWhenNotMatchedClause
    : T__79 T__125 T__371 T__80 mergeInsertSpecification
    ;

// <merge update specification>
mergeUpdateSpecification
    : T__270 T__28 setClauseList
    ;

// <merge insert specification>
mergeInsertSpecification
    : T__290 (LEFT_PAREN insertColumnList RIGHT_PAREN)? (overrideClause)? T__133 mergeInsertValueList
    ;

// <merge insert value list>
mergeInsertValueList
    : LEFT_PAREN mergeInsertValueElement ((COMMA mergeInsertValueElement)*)? RIGHT_PAREN
    ;

// <merge insert value element>
mergeInsertValueElement
    : valueExpression
    | contextuallyTypedValueSpecification
    ;

// <update statement: positioned>
updateStatement_Positioned
    : T__270 targetTable T__28 setClauseList T__148 T__162 T__196 IDENTIFIER
    ;

// <update statement: searched>
updateStatement_Searched
    : T__270 targetTable T__28 setClauseList (T__148 searchCondition)?
    ;

// <set clause list>
setClauseList
    : setClause ((COMMA setClause)*)?
    ;

// <set clause>
setClause
    : multipleColumnAssignment
    | setTarget T__13 updateSource
    ;

// <set target>
setTarget
    : updateTarget
    | mutatedSetClause
    ;

// <multiple column assignment>
multipleColumnAssignment
    : setTargetList T__13 assignedRow
    ;

// <set target list>
setTargetList
    : LEFT_PAREN setTarget ((COMMA setTarget)*)? RIGHT_PAREN
    ;

// <assigned row>
assignedRow
    : contextuallyTypedRowValueExpression
    ;

// <update target>
updateTarget
    : objectColumn
    | objectColumn leftBracketOrTrigraph simpleValueSpecification rightBracketOrTrigraph
    ;

// <object column>
objectColumn
    : columnName
    ;

// Merged rules: mutated set clause, mutated target
mutatedSetClause
    : mutatedSetClause DOT IDENTIFIER
    | objectColumn
    ;

// <update source>
updateSource
    : valueExpression
    | contextuallyTypedValueSpecification
    ;

// <temporary table declaration>
temporaryTableDeclaration
    : T__355 T__26 T__251 T__132 tableName tableElementList (T__142 T__250 tableCommitAction T__158)?
    ;

// <free locator statement>
freeLocatorStatement
    : T__372 T__302 locatorReference ((COMMA locatorReference)*)?
    ;

// <locator reference>
locatorReference
    : hostParameterName
    | embeddedVariableName
    ;

// <hold locator statement>
holdLocatorStatement
    : T__361 T__302 locatorReference ((COMMA locatorReference)*)?
    ;

// <call statement>
callStatement
    : T__373 routineInvocation
    ;

// <return statement>
returnStatement
    : T__362 returnValue
    ;

// <return value>
returnValue
    : valueExpression
    | T__70
    ;

// <start transaction statement>
startTransactionStatement
    : T__337 T__374 (transactionMode ((COMMA transactionMode)*)?)?
    ;

// <transaction mode>
transactionMode
    : isolationLevel
    | transactionAccessMode
    | diagnosticsSize
    ;

// <transaction access mode>
transactionAccessMode
    : T__363 T__138
    | T__363 T__375
    ;

// <isolation level>
isolationLevel
    : T__376 T__308 levelOfIsolation
    ;

// <level of isolation>
levelOfIsolation
    : T__363 T__377
    | T__363 T__378
    | T__137 T__363
    | T__379
    ;

// <diagnostics size>
diagnosticsSize
    : T__380 T__381 numberOfConditions
    ;

// <number of conditions>
numberOfConditions
    : simpleValueSpecification
    ;

// <set transaction statement>
setTransactionStatement
    : T__28 (T__26)? transactionCharacteristics
    ;

// <transaction characteristics>
transactionCharacteristics
    : T__374 transactionMode ((COMMA transactionMode)*)?
    ;

// <set constraints mode statement>
setConstraintsModeStatement
    : T__28 T__382 constraintNameList (T__227 | T__228)
    ;

// <savepoint statement>
savepointStatement
    : T__307 savepointSpecifier
    ;

// <savepoint specifier>
savepointSpecifier
    : IDENTIFIER
    ;

// <release savepoint statement>
releaseSavepointStatement
    : T__383 T__307 savepointSpecifier
    ;

// <commit statement>
commitStatement
    : T__250 (T__384)? (T__124 (T__167)? T__385)?
    ;

// <rollback statement>
rollbackStatement
    : T__386 (T__384)? (T__124 (T__167)? T__385)? (savepointClause)?
    ;

// <savepoint clause>
savepointClause
    : T__177 T__307 savepointSpecifier
    ;

// <connect statement>
connectStatement
    : T__387 T__177 connectionTarget
    ;

// <connection target>
connectionTarget
    : sQL_serverName (T__83 IDENTIFIER)? (T__66 connectionUserName)?
    | T__71
    ;

// <set connection statement>
setConnectionStatement
    : T__28 T__388 connectionObject
    ;

// <connection object>
connectionObject
    : T__71
    | IDENTIFIER
    ;

// <disconnect statement>
disconnectStatement
    : T__389 disconnectObject
    ;

// <disconnect object>
disconnectObject
    : connectionObject
    | T__128
    | T__162
    ;

// <set session characteristics statement>
setSessionCharacteristicsStatement
    : T__28 T__390 T__391 T__83 sessionCharacteristicList
    ;

// <session characteristic list>
sessionCharacteristicList
    : sessionCharacteristic ((COMMA sessionCharacteristic)*)?
    ;

// <session characteristic>
sessionCharacteristic
    : transactionCharacteristics
    ;

// <set session user identifier statement>
setSessionUserIdentifierStatement
    : T__28 T__390 T__352 valueSpecification
    ;

// <set role statement>
setRoleStatement
    : T__28 T__349 roleSpecification
    ;

// <role specification>
roleSpecification
    : valueSpecification
    | T__392
    ;

// <set local time zone statement>
setLocalTimeZoneStatement
    : T__28 T__55 T__58 setTimeZoneValue
    ;

// <set time zone value>
setTimeZoneValue
    : intervalValueExpression
    | T__26
    ;

// <set catalog statement>
setCatalogStatement
    : T__28 catalogNameCharacteristic
    ;

// <catalog name characteristic>
catalogNameCharacteristic
    : T__393 valueSpecification
    ;

// <set schema statement>
setSchemaStatement
    : T__28 schemaNameCharacteristic
    ;

// <schema name characteristic>
schemaNameCharacteristic
    : T__246 valueSpecification
    ;

// <set names statement>
setNamesStatement
    : T__28 characterSetNameCharacteristic
    ;

// <character set name characteristic>
characterSetNameCharacteristic
    : T__353 valueSpecification
    ;

// <set path statement>
setPathStatement
    : T__28 sQL_pathCharacteristic
    ;

// <SQL-path characteristic>
sQL_pathCharacteristic
    : T__214 valueSpecification
    ;

// <set transform group statement>
setTransformGroupStatement
    : T__28 transformGroupCharacteristic
    ;

// <transform group characteristic>
transformGroupCharacteristic
    : T__71 T__328 T__149 valueSpecification
    | T__328 T__149 T__85 T__296 path_resolvedUser_definedTypeName valueSpecification
    ;

// <set session collation statement>
setSessionCollationStatement
    : T__28 T__281 collationSpecification (T__85 characterSetSpecificationList)?
    | T__28 T__167 T__281 (T__85 characterSetSpecificationList)?
    ;

// <collation specification>
collationSpecification
    : valueSpecification
    ;

// <allocate descriptor statement>
allocateDescriptorStatement
    : T__394 (T__213)? T__395 IDENTIFIER (T__57 T__231 occurrences)?
    ;

// <occurrences>
occurrences
    : simpleValueSpecification
    ;

// <deallocate descriptor statement>
deallocateDescriptorStatement
    : T__396 (T__213)? T__395 IDENTIFIER
    ;

// <get descriptor statement>
getDescriptorStatement
    : T__280 (T__213)? T__395 IDENTIFIER getDescriptorInformation
    ;

// <get descriptor information>
getDescriptorInformation
    : getHeaderInformation ((COMMA getHeaderInformation)*)?
    | T__67 itemNumber getItemInformation ((COMMA getItemInformation)*)?
    ;

// <get header information>
getHeaderInformation
    : simpleTargetSpecification1 T__13 headerItemName
    ;

// <header item name>
headerItemName
    : T__229
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <get item information>
getItemInformation
    : simpleTargetSpecification2 T__13 descriptorItemName
    ;

// <item number>
itemNumber
    : simpleValueSpecification
    ;

// <simple target specification 1>
simpleTargetSpecification1
    : simpleTargetSpecification
    ;

// <simple target specification 2>
simpleTargetSpecification2
    : simpleTargetSpecification
    ;

// <descriptor item name>
descriptorItemName
    : T__96
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__262
    | IDENTIFIER
    | IDENTIFIER
    | T__397
    | T__69
    | IDENTIFIER
    | T__398
    | T__308
    | T__318
    | T__399
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__50
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__400
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__296
    | T__401
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <set descriptor statement>
setDescriptorStatement
    : T__28 (T__213)? T__395 IDENTIFIER setDescriptorInformation
    ;

// <set descriptor information>
setDescriptorInformation
    : setHeaderInformation ((COMMA setHeaderInformation)*)?
    | T__67 itemNumber setItemInformation ((COMMA setItemInformation)*)?
    ;

// <set header information>
setHeaderInformation
    : headerItemName T__13 simpleValueSpecification1
    ;

// <set item information>
setItemInformation
    : descriptorItemName T__13 simpleValueSpecification2
    ;

// <simple value specification 1>
simpleValueSpecification1
    : simpleValueSpecification
    ;

// <simple value specification 2>
simpleValueSpecification2
    : simpleValueSpecification
    ;

// <prepare statement>
prepareStatement
    : T__402 sqlStatementName (attributesSpecification)? T__95 sqlStatementVariable
    ;

// <attributes specification>
attributesSpecification
    : T__403 attributesVariable
    ;

// <attributes variable>
attributesVariable
    : simpleValueSpecification
    ;

// <SQL statement variable>
sqlStatementVariable
    : simpleValueSpecification
    ;

// <preparable statement>
preparableStatement
    : preparableSqlDataStatement
    | preparableSqlSchemaStatement
    | preparableSqlTransactionStatement
    | preparableSqlControlStatement
    | preparableSqlSessionStatement
    | preparableImplementation_definedStatement
    ;

// <preparable SQL data statement>
preparableSqlDataStatement
    : deleteStatement_Searched
    | dynamicSingleRowSelectStatement
    | insertStatement
    | dynamicSelectStatement
    | updateStatement_Searched
    | mergeStatement
    | preparableDynamicDeleteStatement_Positioned
    | preparableDynamicUpdateStatement_Positioned
    ;

// <preparable SQL schema statement>
preparableSqlSchemaStatement
    : sqlSchemaStatement
    ;

// <preparable SQL transaction statement>
preparableSqlTransactionStatement
    : sqlTransactionStatement
    ;

// <preparable SQL control statement>
preparableSqlControlStatement
    : sqlControlStatement
    ;

// <preparable SQL session statement>
preparableSqlSessionStatement
    : sqlSessionStatement
    ;

// <dynamic select statement>
dynamicSelectStatement
    : cursorSpecification
    ;

// <cursor attributes>
cursorAttributes
    : cursorAttribute
    ;

// <cursor attribute>
cursorAttribute
    : cursorSensitivity
    | cursorScrollability
    | cursorHoldability
    | cursorReturnability
    ;

// <deallocate prepared statement>
deallocatePreparedStatement
    : T__396 T__402 sqlStatementName
    ;

// <describe statement>
describeStatement
    : describeInputStatement
    | describeOutputStatement
    ;

// <describe input statement>
describeInputStatement
    : T__404 T__326 sqlStatementName usingDescriptor (nestingOption)?
    ;

// <describe output statement>
describeOutputStatement
    : T__404 (T__405)? describedObject usingDescriptor (nestingOption)?
    ;

// <nesting option>
nestingOption
    : T__57 T__406
    | T__59 T__406
    ;

// <using descriptor>
usingDescriptor
    : T__93 (T__213)? T__395 IDENTIFIER
    ;

// <described object>
describedObject
    : sqlStatementName
    | T__356 IDENTIFIER T__407
    ;

// <input using clause>
inputUsingClause
    : usingArguments
    | usingInputDescriptor
    ;

// <using arguments>
usingArguments
    : T__93 usingArgument ((COMMA usingArgument)*)?
    ;

// <using argument>
usingArgument
    : generalValueSpecification
    ;

// <using input descriptor>
usingInputDescriptor
    : usingDescriptor
    ;

// <output using clause>
outputUsingClause
    : intoArguments
    | intoDescriptor
    ;

// <into arguments>
intoArguments
    : T__366 intoArgument ((COMMA intoArgument)*)?
    ;

// <into argument>
intoArgument
    : targetSpecification
    ;

// <into descriptor>
intoDescriptor
    : T__366 (T__213)? T__395 IDENTIFIER
    ;

// <execute statement>
executeStatement
    : T__347 sqlStatementName (resultUsingClause)? (parameterUsingClause)?
    ;

// <result using clause>
resultUsingClause
    : outputUsingClause
    ;

// <parameter using clause>
parameterUsingClause
    : inputUsingClause
    ;

// <execute immediate statement>
executeImmediateStatement
    : T__347 T__228 sqlStatementVariable
    ;

// <dynamic declare cursor>
dynamicDeclareCursor
    : T__355 IDENTIFIER (cursorSensitivity)? (cursorScrollability)? T__356 (cursorHoldability)? (cursorReturnability)? T__85 IDENTIFIER
    ;

// <allocate cursor statement>
allocateCursorStatement
    : T__394 IDENTIFIER cursorIntent
    ;

// <cursor intent>
cursorIntent
    : statementCursor
    | resultSetCursor
    ;

// <statement cursor>
statementCursor
    : (cursorSensitivity)? (cursorScrollability)? T__356 (cursorHoldability)? (cursorReturnability)? T__85 IDENTIFIER
    ;

// <result set cursor>
resultSetCursor
    : T__85 T__218 specificRoutineDesignator
    ;

// <dynamic open statement>
dynamicOpenStatement
    : T__364 dynamicCursorName (inputUsingClause)?
    ;

// <dynamic fetch statement>
dynamicFetchStatement
    : T__365 ((fetchOrientation)? T__95)? dynamicCursorName outputUsingClause
    ;

// <dynamic single row select statement>
dynamicSingleRowSelectStatement
    : querySpecification
    ;

// <dynamic close statement>
dynamicCloseStatement
    : T__369 dynamicCursorName
    ;

// <dynamic delete statement: positioned>
dynamicDeleteStatement_Positioned
    : T__253 T__95 targetTable T__148 T__162 T__196 dynamicCursorName
    ;

// <dynamic update statement: positioned>
dynamicUpdateStatement_Positioned
    : T__270 targetTable T__28 setClauseList T__148 T__162 T__196 dynamicCursorName
    ;

// <preparable dynamic delete statement: positioned>
preparableDynamicDeleteStatement_Positioned
    : T__253 (T__95 targetTable)? T__148 T__162 T__196 (scopeOption)? IDENTIFIER
    ;

// <preparable dynamic update statement: positioned>
preparableDynamicUpdateStatement_Positioned
    : T__270 (targetTable)? T__28 setClauseList T__148 T__162 T__196 (scopeOption)? IDENTIFIER
    ;

// <embedded SQL host program>
embeddedSqlHostProgram
    : embeddedSqlAdaProgram
    | embeddedSqlCProgram
    | embeddedSqlCOBOLProgram
    | embeddedSqlFortranProgram
    | embeddedSqlMUMPSProgram
    | embeddedSqlPascalProgram
    | embeddedSqlPLIProgram
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
    | embeddedAuthorizationDeclaration
    | embeddedPathSpecification
    | embeddedTransformGroupSpecification
    | embeddedCollationSpecification
    | embeddedExceptionDeclaration
    | IDENTIFIER
    | sqlProcedureStatement
    ;

// <SQL prefix>
sqlPrefix
    : T__408 T__213
    | T__6 T__213 LEFT_PAREN
    ;

// <SQL terminator>
sqlTerminator
    : IDENTIFIER
    | SEMI
    | RIGHT_PAREN
    ;

// <embedded authorization declaration>
embeddedAuthorizationDeclaration
    : T__355 embeddedAuthorizationClause
    ;

// <embedded authorization clause>
embeddedAuthorizationClause
    : T__246 schemaName
    | T__352 embeddedAuthorizationIdentifier (T__85 T__220 (T__138 | T__124 T__309))?
    | T__246 schemaName T__352 embeddedAuthorizationIdentifier (T__85 T__220 (T__138 | T__124 T__309))?
    ;

// <embedded authorization identifier>
embeddedAuthorizationIdentifier
    : moduleAuthorizationIdentifier
    ;

// <embedded path specification>
embeddedPathSpecification
    : pathSpecification
    ;

// <embedded transform group specification>
embeddedTransformGroupSpecification
    : transformGroupSpecification
    ;

// <embedded collation specification>
embeddedCollationSpecification
    : moduleCollation
    ;

// <embedded SQL declare section>
embeddedSqlDeclareSection
    : embeddedSqlBeginDeclare (embeddedCharacterSetDeclaration)? (hostVariableDefinition)? embeddedSqlEndDeclare
    | embeddedSqlMUMPSDeclare
    ;

// <embedded character set declaration>
embeddedCharacterSetDeclaration
    : T__213 T__353 T__264 characterSetSpecification
    ;

// <embedded SQL begin declare>
embeddedSqlBeginDeclare
    : sqlPrefix T__293 T__355 T__409 (sqlTerminator)?
    ;

// <embedded SQL end declare>
embeddedSqlEndDeclare
    : sqlPrefix T__78 T__355 T__409 (sqlTerminator)?
    ;

// <embedded SQL MUMPS declare>
embeddedSqlMUMPSDeclare
    : sqlPrefix T__293 T__355 T__409 (embeddedCharacterSetDeclaration)? (hostVariableDefinition)? T__78 T__355 T__409 sqlTerminator
    ;

// <host variable definition>
hostVariableDefinition
    : adaVariableDefinition
    | cVariableDefinition
    | cOBOLVariableDefinition
    | fortranVariableDefinition
    | mUMPSVariableDefinition
    | pascalVariableDefinition
    | pLIVariableDefinition
    ;

// <embedded variable name>
embeddedVariableName
    : T__16 hostIdentifier
    ;

// <host identifier>
hostIdentifier
    : adaHostIdentifier
    | cHostIdentifier
    | cOBOLHostIdentifier
    | fortranHostIdentifier
    | mUMPSHostIdentifier
    | pascalHostIdentifier
    | pLIHostIdentifier
    ;

// <embedded exception declaration>
embeddedExceptionDeclaration
    : T__410 condition conditionAction
    ;

// <condition>
condition
    : sqlCondition
    ;

// <SQL condition>
sqlCondition
    : majorCategory
    | T__354 IDENTIFIER sQLSTATEClassValue (IDENTIFIER sQLSTATESubclassValue)? IDENTIFIER
    | T__224 constraintName
    ;

// <major category>
majorCategory
    : T__411
    | T__412
    | T__125 T__413
    ;

// <SQLSTATE class value>
sQLSTATEClassValue
    : sQLSTATEChar sQLSTATEChar
    ;

// <SQLSTATE subclass value>
sQLSTATESubclassValue
    : sQLSTATEChar sQLSTATEChar sQLSTATEChar
    ;

// <SQLSTATE char>
sQLSTATEChar
    : 
    | NUMBER
    ;

// <condition action>
conditionAction
    : T__414
    | goTo
    ;

// <go to>
goTo
    : (T__415 | T__416 T__177) gotoTarget
    ;

// <goto target>
gotoTarget
    : hostLabelIdentifier
    | NUMBER
    | hostPLILabelVariable
    ;

// <Ada variable definition>
adaVariableDefinition
    : adaHostIdentifier ((COMMA adaHostIdentifier)*)? T__16 adaTypeSpecification (adaInitialValue)?
    ;

// <Ada initial value>
adaInitialValue
    : adaAssignmentOperator STRING
    ;

// <Ada assignment operator>
adaAssignmentOperator
    : T__16 T__13
    ;

// <Ada type specification>
adaTypeSpecification
    : adaQualifiedTypeSpecification
    | adaUnqualifiedTypeSpecification
    | adaDerivedTypeSpecification
    ;

// <Ada qualified type specification>
adaQualifiedTypeSpecification
    : IDENTIFIER DOT T__29 (T__27 T__28 (T__126)? characterSetSpecification)? LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | IDENTIFIER DOT T__43
    | IDENTIFIER DOT T__45
    | IDENTIFIER DOT T__46
    | IDENTIFIER DOT T__48
    | IDENTIFIER DOT IDENTIFIER
    | IDENTIFIER DOT T__53
    | IDENTIFIER DOT IDENTIFIER
    | IDENTIFIER DOT IDENTIFIER
    ;

// <Ada unqualified type specification>
adaUnqualifiedTypeSpecification
    : T__29 LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | T__43
    | T__45
    | T__46
    | T__48
    | IDENTIFIER
    | T__53
    | IDENTIFIER
    | IDENTIFIER
    ;

// <Ada derived type specification>
adaDerivedTypeSpecification
    : adaCLOBVariable
    | adaCLOBLocatorVariable
    | adaBLOBVariable
    | adaBLOBLocatorVariable
    | adaUser_definedTypeVariable
    | adaUser_definedTypeLocatorVariable
    | adaREFVariable
    | adaArrayLocatorVariable
    | adaMultisetLocatorVariable
    ;

// <Ada CLOB variable>
adaCLOBVariable
    : T__213 T__296 T__126 T__34 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)?
    ;

// <Ada CLOB locator variable>
adaCLOBLocatorVariable
    : T__213 T__296 T__126 T__34 T__83 T__302
    ;

// <Ada BLOB variable>
adaBLOBVariable
    : T__213 T__296 T__126 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <Ada BLOB locator variable>
adaBLOBLocatorVariable
    : T__213 T__296 T__126 T__39 T__83 T__302
    ;

// <Ada user-defined type variable>
adaUser_definedTypeVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 predefinedType
    ;

// <Ada user-defined type locator variable>
adaUser_definedTypeLocatorVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 T__302
    ;

// <Ada REF variable>
adaREFVariable
    : T__213 T__296 T__126 referenceType
    ;

// <Ada array locator variable>
adaArrayLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <Ada multiset locator variable>
adaMultisetLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <C variable definition>
cVariableDefinition
    : (cStorageClass)? (cClassModifier)? cVariableSpecification SEMI
    ;

// <C variable specification>
cVariableSpecification
    : cNumericVariable
    | cCharacterVariable
    | cDerivedVariable
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

// <C numeric variable>
cNumericVariable
    : (IDENTIFIER IDENTIFIER | IDENTIFIER | IDENTIFIER | IDENTIFIER | IDENTIFIER) cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C character variable>
cCharacterVariable
    : cCharacterType (T__27 T__28 (T__126)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? ((COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*)?
    ;

// <C character type>
cCharacterType
    : IDENTIFIER
    | IDENTIFIER IDENTIFIER
    | IDENTIFIER IDENTIFIER
    ;

// <C array specification>
cArraySpecification
    : T__0 length T__1
    ;

// <C derived variable>
cDerivedVariable
    : cVARCHARVariable
    | cNCHARVariable
    | cNCHARVARYINGVariable
    | cCLOBVariable
    | cNCLOBVariable
    | cBLOBVariable
    | cUser_definedTypeVariable
    | cCLOBLocatorVariable
    | cBLOBLocatorVariable
    | cArrayLocatorVariable
    | cMultisetLocatorVariable
    | cUser_definedTypeLocatorVariable
    | cREFVariable
    ;

// <C VARCHAR variable>
cVARCHARVariable
    : T__31 (T__27 T__28 (T__126)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? ((COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*)?
    ;

// <C NCHAR variable>
cNCHARVariable
    : T__36 (T__27 T__28 (T__126)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? ((COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*)?
    ;

// <C NCHAR VARYING variable>
cNCHARVARYINGVariable
    : T__36 T__30 (T__27 T__28 (T__126)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? ((COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*)?
    ;

// <C CLOB variable>
cCLOBVariable
    : T__213 T__296 T__126 T__34 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)? cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C NCLOB variable>
cNCLOBVariable
    : T__213 T__296 T__126 T__37 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)? cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C user-defined type variable>
cUser_definedTypeVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 predefinedType cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C BLOB variable>
cBLOBVariable
    : T__213 T__296 T__126 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C CLOB locator variable>
cCLOBLocatorVariable
    : T__213 T__296 T__126 T__34 T__83 T__302 cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C BLOB locator variable>
cBLOBLocatorVariable
    : T__213 T__296 T__126 T__39 T__83 T__302 cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C array locator variable>
cArrayLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302 cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C multiset locator variable>
cMultisetLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302 cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C user-defined type locator variable>
cUser_definedTypeLocatorVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 T__302 cHostIdentifier (cInitialValue)? ((COMMA cHostIdentifier (cInitialValue)?)*)?
    ;

// <C REF variable>
cREFVariable
    : T__213 T__296 T__126 referenceType
    ;

// <C initial value>
cInitialValue
    : T__13 STRING
    ;

// <COBOL variable definition>
cOBOLVariableDefinition
    : (NUMBER | NUMBER) cOBOLHostIdentifier cOBOLTypeSpecification (STRING)? DOT
    ;

// <COBOL type specification>
cOBOLTypeSpecification
    : cOBOLCharacterType
    | cOBOLNationalCharacterType
    | cOBOLNumericType
    | cOBOLIntegerType
    | cOBOLDerivedTypeSpecification
    ;

// <COBOL derived type specification>
cOBOLDerivedTypeSpecification
    : cOBOLCLOBVariable
    | cOBOLNCLOBVariable
    | cOBOLBLOBVariable
    | cOBOLUser_definedTypeVariable
    | cOBOLCLOBLocatorVariable
    | cOBOLBLOBLocatorVariable
    | cOBOLArrayLocatorVariable
    | cOBOLMultisetLocatorVariable
    | cOBOLUser_definedTypeLocatorVariable
    | cOBOLREFVariable
    ;

// <COBOL character type>
cOBOLCharacterType
    : (T__27 T__28 (T__126)? characterSetSpecification)? (T__417 | T__418) (T__126)? (T__19 (LEFT_PAREN length RIGHT_PAREN)?)*
    ;

// <COBOL national character type>
cOBOLNationalCharacterType
    : (T__27 T__28 (T__126)? characterSetSpecification)? (T__417 | T__418) (T__126)? (T__419 (LEFT_PAREN length RIGHT_PAREN)?)*
    ;

// <COBOL CLOB variable>
cOBOLCLOBVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 T__34 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)?
    ;

// <COBOL NCLOB variable>
cOBOLNCLOBVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 T__37 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)?
    ;

// <COBOL BLOB variable>
cOBOLBLOBVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <COBOL user-defined type variable>
cOBOLUser_definedTypeVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 predefinedType
    ;

// <COBOL CLOB locator variable>
cOBOLCLOBLocatorVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 T__34 T__83 T__302
    ;

// <COBOL BLOB locator variable>
cOBOLBLOBLocatorVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 T__39 T__83 T__302
    ;

// <COBOL array locator variable>
cOBOLArrayLocatorVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <COBOL multiset locator variable>
cOBOLMultisetLocatorVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <COBOL user-defined type locator variable>
cOBOLUser_definedTypeLocatorVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 T__302
    ;

// <COBOL REF variable>
cOBOLREFVariable
    : (T__346 (T__126)?)? T__213 T__296 T__126 referenceType
    ;

// <COBOL numeric type>
cOBOLNumericType
    : (T__417 | T__418) (T__126)? T__420 cOBOLNinesSpecification (T__346 (T__126)?)? T__421 T__422 T__113 T__423
    ;

// <COBOL nines specification>
cOBOLNinesSpecification
    : cOBOLNines (T__424 (cOBOLNines)?)?
    | T__424 cOBOLNines
    ;

// <COBOL integer type>
cOBOLIntegerType
    : cOBOLBinaryInteger
    ;

// <COBOL binary integer>
cOBOLBinaryInteger
    : (T__417 | T__418) (T__126)? T__420 cOBOLNines (T__346 (T__126)?)? T__38
    ;

// <COBOL nines>
cOBOLNines
    : (NUMBER (LEFT_PAREN length RIGHT_PAREN)?)*
    ;

// <Fortran variable definition>
fortranVariableDefinition
    : fortranTypeSpecification fortranHostIdentifier ((COMMA fortranHostIdentifier)*)?
    ;

// <Fortran type specification>
fortranTypeSpecification
    : T__27 (T__17 length)? (T__27 T__28 (T__126)? characterSetSpecification)?
    | T__27 T__425 IDENTIFIER IDENTIFIER (T__17 length)? (T__27 T__28 (T__126)? characterSetSpecification)?
    | T__44
    | T__48
    | T__49 T__50
    | T__426
    | fortranDerivedTypeSpecification
    ;

// <Fortran derived type specification>
fortranDerivedTypeSpecification
    : fortranCLOBVariable
    | fortranBLOBVariable
    | fortranUser_definedTypeVariable
    | fortranCLOBLocatorVariable
    | fortranBLOBLocatorVariable
    | fortranUser_definedTypeLocatorVariable
    | fortranArrayLocatorVariable
    | fortranMultisetLocatorVariable
    | fortranREFVariable
    ;

// <Fortran CLOB variable>
fortranCLOBVariable
    : T__213 T__296 T__126 T__34 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)?
    ;

// <Fortran BLOB variable>
fortranBLOBVariable
    : T__213 T__296 T__126 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <Fortran user-defined type variable>
fortranUser_definedTypeVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 predefinedType
    ;

// <Fortran CLOB locator variable>
fortranCLOBLocatorVariable
    : T__213 T__296 T__126 T__34 T__83 T__302
    ;

// <Fortran BLOB locator variable>
fortranBLOBLocatorVariable
    : T__213 T__296 T__126 T__39 T__83 T__302
    ;

// <Fortran user-defined type locator variable>
fortranUser_definedTypeLocatorVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 T__302
    ;

// <Fortran array locator variable>
fortranArrayLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <Fortran multiset locator variable>
fortranMultisetLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <Fortran REF variable>
fortranREFVariable
    : T__213 T__296 T__126 referenceType
    ;

// <MUMPS variable definition>
mUMPSVariableDefinition
    : mUMPSNumericVariable SEMI
    | mUMPSCharacterVariable SEMI
    | mUMPSDerivedTypeSpecification SEMI
    ;

// <MUMPS character variable>
mUMPSCharacterVariable
    : T__31 mUMPSHostIdentifier mUMPSLengthSpecification ((COMMA mUMPSHostIdentifier mUMPSLengthSpecification)*)?
    ;

// <MUMPS length specification>
mUMPSLengthSpecification
    : LEFT_PAREN length RIGHT_PAREN
    ;

// <MUMPS numeric variable>
mUMPSNumericVariable
    : mUMPSTypeSpecification mUMPSHostIdentifier ((COMMA mUMPSHostIdentifier)*)?
    ;

// <MUMPS type specification>
mUMPSTypeSpecification
    : T__45
    | T__42 (LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN)?
    | T__48
    ;

// <MUMPS derived type specification>
mUMPSDerivedTypeSpecification
    : mUMPSCLOBVariable
    | mUMPSBLOBVariable
    | mUMPSUser_definedTypeVariable
    | mUMPSCLOBLocatorVariable
    | mUMPSBLOBLocatorVariable
    | mUMPSUser_definedTypeLocatorVariable
    | mUMPSArrayLocatorVariable
    | mUMPSMultisetLocatorVariable
    | mUMPSREFVariable
    ;

// <MUMPS CLOB variable>
mUMPSCLOBVariable
    : T__213 T__296 T__126 T__34 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)?
    ;

// <MUMPS BLOB variable>
mUMPSBLOBVariable
    : T__213 T__296 T__126 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <MUMPS user-defined type variable>
mUMPSUser_definedTypeVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 predefinedType
    ;

// <MUMPS CLOB locator variable>
mUMPSCLOBLocatorVariable
    : T__213 T__296 T__126 T__34 T__83 T__302
    ;

// <MUMPS BLOB locator variable>
mUMPSBLOBLocatorVariable
    : T__213 T__296 T__126 T__39 T__83 T__302
    ;

// <MUMPS user-defined type locator variable>
mUMPSUser_definedTypeLocatorVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 T__302
    ;

// <MUMPS array locator variable>
mUMPSArrayLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <MUMPS multiset locator variable>
mUMPSMultisetLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <MUMPS REF variable>
mUMPSREFVariable
    : T__213 T__296 T__126 referenceType
    ;

// <Pascal variable definition>
pascalVariableDefinition
    : pascalHostIdentifier ((COMMA pascalHostIdentifier)*)? T__16 pascalTypeSpecification SEMI
    ;

// <Pascal type specification>
pascalTypeSpecification
    : T__427 T__64 T__0 NUMBER doublePeriod length T__1 T__196 T__29 (T__27 T__28 (T__126)? characterSetSpecification)?
    | T__44
    | T__48
    | T__29 (T__27 T__28 (T__126)? characterSetSpecification)?
    | T__53
    | pascalDerivedTypeSpecification
    ;

// <Pascal derived type specification>
pascalDerivedTypeSpecification
    : pascalCLOBVariable
    | pascalBLOBVariable
    | pascalUser_definedTypeVariable
    | pascalCLOBLocatorVariable
    | pascalBLOBLocatorVariable
    | pascalUser_definedTypeLocatorVariable
    | pascalArrayLocatorVariable
    | pascalMultisetLocatorVariable
    | pascalREFVariable
    ;

// <Pascal CLOB variable>
pascalCLOBVariable
    : T__213 T__296 T__126 T__34 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)?
    ;

// <Pascal BLOB variable>
pascalBLOBVariable
    : T__213 T__296 T__126 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <Pascal CLOB locator variable>
pascalCLOBLocatorVariable
    : T__213 T__296 T__126 T__34 T__83 T__302
    ;

// <Pascal user-defined type variable>
pascalUser_definedTypeVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 predefinedType
    ;

// <Pascal BLOB locator variable>
pascalBLOBLocatorVariable
    : T__213 T__296 T__126 T__39 T__83 T__302
    ;

// <Pascal user-defined type locator variable>
pascalUser_definedTypeLocatorVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 T__302
    ;

// <Pascal array locator variable>
pascalArrayLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <Pascal multiset locator variable>
pascalMultisetLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <Pascal REF variable>
pascalREFVariable
    : T__213 T__296 T__126 referenceType
    ;

// <PL/I variable definition>
pLIVariableDefinition
    : (T__428 | T__355) (pLIHostIdentifier | LEFT_PAREN pLIHostIdentifier ((COMMA pLIHostIdentifier)*)? RIGHT_PAREN) pLITypeSpecification (STRING)? SEMI
    ;

// <PL/I type specification>
pLITypeSpecification
    : (T__29 | T__27) (T__30)? LEFT_PAREN length RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)?
    | pLITypeFixedDecimal LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN
    | pLITypeFixedBinary (LEFT_PAREN precision RIGHT_PAREN)?
    | pLITypeFloatBinary LEFT_PAREN precision RIGHT_PAREN
    | pLIDerivedTypeSpecification
    ;

// <PL/I derived type specification>
pLIDerivedTypeSpecification
    : pLICLOBVariable
    | pLIBLOBVariable
    | pLIUser_definedTypeVariable
    | pLICLOBLocatorVariable
    | pLIBLOBLocatorVariable
    | pLIUser_definedTypeLocatorVariable
    | pLIArrayLocatorVariable
    | pLIMultisetLocatorVariable
    | pLIREFVariable
    ;

// <PL/I CLOB variable>
pLICLOBVariable
    : T__213 T__296 T__126 T__34 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__27 T__28 (T__126)? characterSetSpecification)?
    ;

// <PL/I BLOB variable>
pLIBLOBVariable
    : T__213 T__296 T__126 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <PL/I user-defined type variable>
pLIUser_definedTypeVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 predefinedType
    ;

// <PL/I CLOB locator variable>
pLICLOBLocatorVariable
    : T__213 T__296 T__126 T__34 T__83 T__302
    ;

// <PL/I BLOB locator variable>
pLIBLOBLocatorVariable
    : T__213 T__296 T__126 T__39 T__83 T__302
    ;

// <PL/I user-defined type locator variable>
pLIUser_definedTypeLocatorVariable
    : T__213 T__296 T__126 path_resolvedUser_definedTypeName T__83 T__302
    ;

// <PL/I array locator variable>
pLIArrayLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <PL/I multiset locator variable>
pLIMultisetLocatorVariable
    : T__213 T__296 T__126 arrayType T__83 T__302
    ;

// <PL/I REF variable>
pLIREFVariable
    : T__213 T__296 T__126 referenceType
    ;

// <PL/I type fixed decimal>
pLITypeFixedDecimal
    : (T__42 | T__41) T__429
    | T__429 (T__42 | T__41)
    ;

// <PL/I type fixed binary>
pLITypeFixedBinary
    : (T__430 | T__38) T__429
    | T__429 (T__430 | T__38)
    ;

// <PL/I type float binary>
pLITypeFloatBinary
    : (T__430 | T__38) T__47
    | T__47 (T__430 | T__38)
    ;

// <direct SQL statement>
directSqlStatement
    : directlyExecutableStatement
    ;

// <directly executable statement>
directlyExecutableStatement
    : directSqlDataStatement
    | explainPlanStatement
    | sqlSchemaStatement
    | sqlTransactionStatement
    | sqlConnectionStatement
    | sqlSessionStatement
    | directImplementation_definedStatement
    ;

explainPlanStatement
    : EXPLAIN PLAN (T__28 STATEMENT_NAME T__13 STRING)? T__85 directSqlDataStatement
    ;

// <direct SQL data statement>
directSqlDataStatement
    : deleteStatement_Searched
    | directSelectStatement_MultipleRows
    | insertStatement
    | updateStatement_Searched
    | mergeStatement
    | temporaryTableDeclaration
    ;

// <direct select statement: multiple rows>
directSelectStatement_MultipleRows
    : cursorSpecification
    ;

// <get diagnostics statement>
getDiagnosticsStatement
    : T__280 T__380 sqlDiagnosticsInformation
    ;

// <SQL diagnostics information>
sqlDiagnosticsInformation
    : statementInformation
    | conditionInformation
    ;

// <statement information>
statementInformation
    : statementInformationItem ((COMMA statementInformationItem)*)?
    ;

// <statement information item>
statementInformationItem
    : simpleTargetSpecification T__13 statementInformationItemName
    ;

// <statement information item name>
statementInformationItemName
    : T__431
    | T__432
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    ;

// <condition information>
conditionInformation
    : (T__433 | T__434) conditionNumber conditionInformationItem ((COMMA conditionInformationItem)*)?
    ;

// <condition information item>
conditionInformationItem
    : simpleTargetSpecification T__13 conditionInformationItemName
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

// <condition number>
conditionNumber
    : simpleValueSpecification
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
    : IDENTIFIER ( T__435 IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
constraintName
    : IDENTIFIER ( T__435 IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
correlationName
    : IDENTIFIER ( T__435 IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
triggerName
    : IDENTIFIER ( T__435 IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
constraintNameList
    : IDENTIFIER ( T__435 IDENTIFIER )*
    ;

// Name-type placeholder (from lexical BNF rule)
columnNameList
    : IDENTIFIER ( T__435 IDENTIFIER )*
    ;

// Auto-generated placeholder for undefined rule
leftBracket
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
delimitedIdentifierPart
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
collationName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlFortranProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
descriptorName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
questionMark
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
doubleQuote
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlCProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
escapedCharacter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
preparableImplementation_definedStatement
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
cHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
mUMPSHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
intervalLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
queryName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
equalsOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
extendedStatementName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
connectionName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
verticalBar
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
statementName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
hostLabelIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
period
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
specificName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
nonquoteCharacter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
directImplementation_definedStatement
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlMUMPSProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
simpleLatinUpperCaseLetter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
extendedCursorName
    : IDENTIFIER
    ;

non_joinQueryExpression
    : non_joinQueryTerm
    ;

// Auto-generated placeholder for undefined rule
cOBOLHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
cursorName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
characterStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
pascalHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
user_definedTypeName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
queryExpressionBody
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
regularIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
fieldName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
adaHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
underscore
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
rightBracket
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
roleName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
digit
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
semicolon
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
characterRepresentation
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
hexit
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
circumflex
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
comma
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
non_escapedCharacter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
rightParen
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlAdaProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlPascalProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
lessThanOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
leftParen
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
schemaNameClause
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
quote
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlPLIProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
newline
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
attributeName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
percent
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
savepointName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
rightBrace
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
asterisk
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
datetimeLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
hostPLILabelVariable
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
routineName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
methodName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
colon
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
plusSign
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
characterSetName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
solidus
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
unicodeEscapeCharacter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlCOBOLProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
minusSign
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
pLIHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
qualifiedIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
nationalCharacterStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
ampersand
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
moduleNameClause
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
delimitedIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
space
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
authorizationIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
unsignedNumericLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
greaterThanOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
fortranHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
identifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
leftBrace
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
unsignedInteger
    : IDENTIFIER
    ;

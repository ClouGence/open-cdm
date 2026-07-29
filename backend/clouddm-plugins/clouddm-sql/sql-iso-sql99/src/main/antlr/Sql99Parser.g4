/*
 * Auto-generated from BNF grammar by bnf2antlr.py.
 * Do not edit by hand; regenerate from the .bnf source.
 */
parser grammar Sql99Parser;

options { tokenVocab=Sql99Lexer; }

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
    | r1999
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

// <1999>
r1999
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <SQL conformance>
sqlConformance
    : level parts packages
    ;

// <level>
level
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

// <parts>
parts
    : IDENTIFIER IDENTIFIER IDENTIFIER IDENTIFIER IDENTIFIER IDENTIFIER IDENTIFIER IDENTIFIER
    ;

// <packages>
packages
    : packagePKGi
    ;

// <Part (n)>
partn
    : partnNo
    | partnYes
    ;

// <Part (n) no>
partnNo
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Package PKG(i)>
packagePKGi
    : IDENTIFIER
    | IDENTIFIER
    ;

// <Part 3 yes>
part3Yes
    : part3Conformance
    ;

// <Part 3 conformance>
part3Conformance
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 4 yes>
part4Yes
    : part4Conformance part4Module
    ;

// <Part 4 conformance>
part4Conformance
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 4 module>
part4Module
    : part4ModuleYes
    | part4ModuleNo
    ;

// <Part 4 module yes>
part4ModuleYes
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 4 module no>
part4ModuleNo
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 yes>
part5Yes
    : part5Conformance part5Direct part5Embedded
    ;

// <Part 5 conformance>
part5Conformance
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 direct>
part5Direct
    : part5DirectYes
    | part5DirectNo
    ;

// <Part 5 direct yes>
part5DirectYes
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 direct no>
part5DirectNo
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 embedded>
part5Embedded
    : part5EmbeddedNo
    | part5EmbeddedLanguages
    ;

// <Part 5 embedded no>
part5EmbeddedNo
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 embedded languages>
part5EmbeddedLanguages
    : part5EmbeddedAda
    | part5EmbeddedC
    | part5EmbeddedCOBOL
    | part5EmbeddedFortran
    | part5EmbeddedMUMPS
    | part5EmbeddedPascal
    | part5EmbeddedPLI
    ;

// <Part 5 embedded Ada>
part5EmbeddedAda
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 embedded C>
part5EmbeddedC
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 embedded COBOL>
part5EmbeddedCOBOL
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 embedded Fortran>
part5EmbeddedFortran
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 embedded MUMPS>
part5EmbeddedMUMPS
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 embedded Pascal>
part5EmbeddedPascal
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <Part 5 embedded PL/I>
part5EmbeddedPLI
    : NUMBER
    | IDENTIFIER LEFT_PAREN NUMBER RIGHT_PAREN
    ;

// <SQL-client module definition>
sQL_clientModuleDefinition
    : IDENTIFIER languageClause moduleAuthorizationClause (modulePathSpecification)? (moduleTransformGroupSpecification)? (temporaryTableDeclaration)? moduleContents
    ;

// <SQL-client module name>
sQL_clientModuleName
    : IDENTIFIER
    ;

// <actual identifier>
actualIdentifier
    : IDENTIFIER
    | IDENTIFIER
    ;

// <module character set specification>
moduleCharacterSetSpecification
    : T__0 T__1 characterSetSpecification
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

// <unqualified schema name>
unqualifiedSchemaName
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

// <language clause>
languageClause
    : T__2 languageName
    ;

// <language name>
languageName
    : T__3
    | T__4
    | T__5
    | T__6
    | T__7
    | T__8
    | T__9
    | T__10
    ;

// <module authorization identifier>
moduleAuthorizationIdentifier
    : IDENTIFIER
    ;

// <user identifier>
userIdentifier
    : IDENTIFIER
    ;

// <module path specification>
modulePathSpecification
    : pathSpecification
    ;

// <path specification>
pathSpecification
    : T__11 schemaNameList
    ;

// <schema name list>
schemaNameList
    : schemaName (COMMA schemaName)*
    ;

// <module transform group specification>
moduleTransformGroupSpecification
    : transformGroupSpecification
    ;

// <transform group specification>
transformGroupSpecification
    : T__12 T__13 (singleGroupSpecification | multipleGroupSpecification)
    ;

// <single group specification>
singleGroupSpecification
    : groupName
    ;

// <group name>
groupName
    : IDENTIFIER
    ;

// <multiple group specification>
multipleGroupSpecification
    : groupSpecification (COMMA groupSpecification)*
    ;

// <group specification>
groupSpecification
    : groupName T__14 T__15 user_definedType
    ;

// <user-defined type>
user_definedType
    : IDENTIFIER
    ;

// <schema qualified type name>
schemaQualifiedTypeName
    : (schemaName DOT)? IDENTIFIER
    ;

// <temporary table declaration>
temporaryTableDeclaration
    : T__16 T__17 T__18 T__19 tableName tableElementList (T__20 T__21 tableCommitAction T__22)?
    ;

// <local or schema qualified name>
localOrSchemaQualifiedName
    : (localOrSchemaQualifier DOT)? IDENTIFIER
    ;

// <local or schema qualifier>
localOrSchemaQualifier
    : schemaName
    | T__23
    ;

// <table element list>
tableElementList
    : LEFT_PAREN tableElement (COMMA tableElement)* RIGHT_PAREN
    ;

// <table element>
tableElement
    : columnDefinition
    | tableConstraintDefinition
    | likeClause
    | self_referencingColumnSpecification
    | columnOptions
    ;

// <column definition>
columnDefinition
    : columnName (collectionType | domainName) (referenceScopeCheck)? (defaultClause)? (columnConstraintDefinition)? (collateClause)?
    ;

// <predefined type>
predefinedType
    : characterStringType (T__24 T__25 characterSetSpecification)?
    | nationalCharacterStringType
    | binaryLargeObjectStringType
    | bitStringType
    | numericType
    | booleanType
    | datetimeType
    | intervalType
    ;

// <character string type>
characterStringType
    : T__24 (LEFT_PAREN length RIGHT_PAREN)?
    | T__26 (LEFT_PAREN length RIGHT_PAREN)?
    | T__24 T__27 LEFT_PAREN length RIGHT_PAREN
    | T__26 T__27 LEFT_PAREN length RIGHT_PAREN
    | T__28 LEFT_PAREN length RIGHT_PAREN
    | T__24 T__29 T__30 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    | T__26 T__29 T__30 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    | T__31 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    ;

// <length>
length
    : NUMBER
    ;

// <large object length>
largeObjectLength
    : NUMBER (multiplier)?
    | largeObjectLengthToken
    ;

// <multiplier>
multiplier
    : T__32
    | T__33
    | T__34
    ;

// <large object length token>
largeObjectLengthToken
    : NUMBER multiplier
    ;

// <national character string type>
nationalCharacterStringType
    : T__35 T__24 (LEFT_PAREN length RIGHT_PAREN)?
    | T__35 T__26 (LEFT_PAREN length RIGHT_PAREN)?
    | T__36 (LEFT_PAREN length RIGHT_PAREN)?
    | T__35 T__24 T__27 LEFT_PAREN length RIGHT_PAREN
    | T__35 T__26 T__27 LEFT_PAREN length RIGHT_PAREN
    | T__36 T__27 LEFT_PAREN length RIGHT_PAREN
    | T__35 T__24 T__29 T__30 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    | T__36 T__29 T__30 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    | T__37 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
    ;

// <binary large object string type>
binaryLargeObjectStringType
    : T__38 T__29 T__30 (LEFT_PAREN largeObjectLength RIGHT_PAREN)?
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
    : T__46 (LEFT_PAREN precision RIGHT_PAREN)?
    | T__47
    | T__48 T__49
    ;

// <boolean type>
booleanType
    : T__50
    ;

// <datetime type>
datetimeType
    : T__51
    | T__52 (LEFT_PAREN timePrecision RIGHT_PAREN)? (withOrWithoutTimeZone)?
    | T__53 (LEFT_PAREN timestampPrecision RIGHT_PAREN)? (withOrWithoutTimeZone)?
    ;

// <time precision>
timePrecision
    : timeFractionalSecondsPrecision
    ;

// <time fractional seconds precision>
timeFractionalSecondsPrecision
    : NUMBER
    ;

// <with or without time zone>
withOrWithoutTimeZone
    : T__54 T__52 T__55
    | T__56 T__52 T__55
    ;

// <timestamp precision>
timestampPrecision
    : timeFractionalSecondsPrecision
    ;

// <interval type>
intervalType
    : T__57 intervalQualifier
    ;

// <interval qualifier>
intervalQualifier
    : startField T__58 endField
    | singleDatetimeField
    ;

// <start field>
startField
    : non_secondPrimaryDatetimeField (LEFT_PAREN intervalLeadingFieldPrecision RIGHT_PAREN)?
    ;

// <non-second primary datetime field>
non_secondPrimaryDatetimeField
    : T__59
    | T__60
    | T__61
    | T__62
    | T__63
    ;

// <interval leading field precision>
intervalLeadingFieldPrecision
    : NUMBER
    ;

// <end field>
endField
    : non_secondPrimaryDatetimeField
    | T__64 (LEFT_PAREN intervalFractionalSecondsPrecision RIGHT_PAREN)?
    ;

// <interval fractional seconds precision>
intervalFractionalSecondsPrecision
    : NUMBER
    ;

// <single datetime field>
singleDatetimeField
    : non_secondPrimaryDatetimeField (LEFT_PAREN intervalLeadingFieldPrecision RIGHT_PAREN)?
    | T__64 (LEFT_PAREN intervalLeadingFieldPrecision (COMMA intervalFractionalSecondsPrecision)? RIGHT_PAREN)?
    ;

// <row type>
rowType
    : T__65 rowTypeBody
    ;

// <row type body>
rowTypeBody
    : LEFT_PAREN fieldDefinition (COMMA fieldDefinition)* RIGHT_PAREN
    ;

// <field definition>
fieldDefinition
    : IDENTIFIER collectionType (referenceScopeCheck)? (collateClause)?
    ;

// <reference scope check>
referenceScopeCheck
    : T__66 T__1 (T__67)? T__68 (T__20 T__69 referenceScopeCheckAction)?
    ;

// <reference scope check action>
referenceScopeCheckAction
    : referentialAction
    ;

// <referential action>
referentialAction
    : T__70
    | T__25 T__71
    | T__25 T__72
    | T__73
    | T__74 T__75
    ;

// <collate clause>
collateClause
    : T__76 IDENTIFIER
    ;

// <schema qualified name>
schemaQualifiedName
    : (schemaName DOT)? IDENTIFIER
    ;

// <reference type>
referenceType
    : T__77 LEFT_PAREN referencedType RIGHT_PAREN (scopeClause)?
    ;

// <referenced type>
referencedType
    : user_definedType
    ;

// <scope clause>
scopeClause
    : T__78 tableName
    ;

// Merged rules: data type, collection type
collectionType
    : predefinedType
    | rowType
    | user_definedType
    | referenceType
    | collectionType arraySpecification
    ;

// <array specification>
arraySpecification
    : collectionTypeConstructor leftBracketOrTrigraph NUMBER rightBracketOrTrigraph
    ;

// <collection type constructor>
collectionTypeConstructor
    : T__79
    ;

// <left bracket or trigraph>
leftBracketOrTrigraph
    : T__80
    | leftBracketTrigraph
    ;

// <left bracket trigraph>
leftBracketTrigraph
    : T__81 T__81 LEFT_PAREN
    ;

// <right bracket or trigraph>
rightBracketOrTrigraph
    : T__82
    | rightBracketTrigraph
    ;

// <right bracket trigraph>
rightBracketTrigraph
    : T__81 T__81 RIGHT_PAREN
    ;

// <default clause>
defaultClause
    : T__72 defaultOption
    ;

// <default option>
defaultOption
    : literal
    | datetimeValueFunction
    | T__83
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | implicitlyTypedValueSpecification
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

// <sign>
sign
    : T__84
    | T__85
    ;

// <exact numeric literal>
exactNumericLiteral
    : NUMBER (DOT (NUMBER)?)?
    | DOT NUMBER
    ;

// <approximate numeric literal>
approximateNumericLiteral
    : mantissa T__86 exponent
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

// <general literal>
generalLiteral
    : STRING
    | STRING
    | STRING
    | STRING
    | binaryStringLiteral
    | STRING
    | STRING
    | booleanLiteral
    ;

// <simple comment>
simpleComment
    : simpleCommentIntroducer (commentCharacter)?
    ;

// <simple comment introducer>
simpleCommentIntroducer
    : T__85 T__85 (T__85)?
    ;

// <comment character>
commentCharacter
    : nonquoteCharacter
    | T__87
    ;

// <bracketed comment>
bracketedComment
    : bracketedCommentIntroducer bracketedCommentContents bracketedCommentTerminator
    ;

// <bracketed comment introducer>
bracketedCommentIntroducer
    : IDENTIFIER T__88
    ;

// <bracketed comment contents>
bracketedCommentContents
    : (commentCharacter)*
    ;

// <bracketed comment terminator>
bracketedCommentTerminator
    : T__88 IDENTIFIER
    ;

// <binary string literal>
binaryStringLiteral
    : T__89 T__87 (NUMBER NUMBER)* T__87 (T__87 (NUMBER NUMBER)* T__87)*
    ;

// <date string>
dateString
    : T__87 unquotedDateString T__87
    ;

// <unquoted date string>
unquotedDateString
    : dateValue
    ;

// <date value>
dateValue
    : yearsValue T__85 monthsValue T__85 daysValue
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
    : T__87 unquotedTimeString T__87
    ;

// <unquoted time string>
unquotedTimeString
    : timeValue (timeZoneInterval)?
    ;

// <time value>
timeValue
    : hoursValue T__90 minutesValue T__90 secondsValue
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
    : sign hoursValue T__90 minutesValue
    ;

// <timestamp string>
timestampString
    : T__87 unquotedTimestampString T__87
    ;

// <unquoted timestamp string>
unquotedTimestampString
    : unquotedDateString unquotedTimeString
    ;

// <interval string>
intervalString
    : T__87 unquotedIntervalString T__87
    ;

// <unquoted interval string>
unquotedIntervalString
    : (sign)? (year_monthLiteral | day_timeLiteral)
    ;

// <year-month literal>
year_monthLiteral
    : yearsValue
    | (yearsValue T__85)? monthsValue
    ;

// <day-time literal>
day_timeLiteral
    : day_timeInterval
    | timeInterval
    ;

// <day-time interval>
day_timeInterval
    : daysValue (hoursValue (T__90 minutesValue (T__90 secondsValue)?)?)?
    ;

// <time interval>
timeInterval
    : hoursValue (T__90 minutesValue (T__90 secondsValue)?)?
    | minutesValue (T__90 secondsValue)?
    | secondsValue
    ;

// <boolean literal>
booleanLiteral
    : T__91
    | T__92
    | T__93
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

// <current timestamp value function>
currentTimestampValueFunction
    : IDENTIFIER (LEFT_PAREN timestampPrecision RIGHT_PAREN)?
    ;

// <current local time value function>
currentLocalTimeValueFunction
    : T__94 (LEFT_PAREN timePrecision RIGHT_PAREN)?
    ;

// <current local timestamp value function>
currentLocalTimestampValueFunction
    : T__95 (LEFT_PAREN timestampPrecision RIGHT_PAREN)?
    ;

// <implicitly typed value specification>
implicitlyTypedValueSpecification
    : nullSpecification
    | emptySpecification
    ;

// <null specification>
nullSpecification
    : T__71
    ;

// <empty specification>
emptySpecification
    : T__79 leftBracketOrTrigraph rightBracketOrTrigraph
    ;

// <column constraint definition>
columnConstraintDefinition
    : (constraintNameDefinition)? columnConstraint (constraintCharacteristics)?
    ;

// <constraint name definition>
constraintNameDefinition
    : T__96 constraintName
    ;

// <column constraint>
columnConstraint
    : T__67 T__71
    | uniqueSpecification
    | referencesSpecification
    | checkConstraintDefinition
    ;

// <unique specification>
uniqueSpecification
    : T__97
    | T__98 T__99
    ;

// <references specification>
referencesSpecification
    : T__66 referencedTableAndColumns (T__100 matchType)? (referentialTriggeredAction)?
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
    : T__101
    | T__102
    | T__103
    ;

// <referential triggered action>
referentialTriggeredAction
    : updateRule (deleteRule)?
    | deleteRule (updateRule)?
    ;

// <update rule>
updateRule
    : T__20 T__104 referentialAction
    ;

// <delete rule>
deleteRule
    : T__20 T__69 referentialAction
    ;

// <check constraint definition>
checkConstraintDefinition
    : T__105 LEFT_PAREN searchCondition RIGHT_PAREN
    ;

// <search condition>
searchCondition
    : betweenPredicate
    ;

// <value specification>
valueSpecification
    : literal
    | generalValueSpecification
    ;

// <general value specification>
generalValueSpecification
    : hostParameterSpecification
    | sqlParameterReference
    | sqlVariableReference
    | dynamicParameterSpecification
    | embeddedVariableSpecification
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER user_definedType
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__83
    | T__106
    ;

// <host parameter specification>
hostParameterSpecification
    : hostParameterName (indicatorParameter)?
    ;

// <host parameter name>
hostParameterName
    : T__90 IDENTIFIER
    ;

// <indicator parameter>
indicatorParameter
    : (T__107)? hostParameterName
    ;

// <SQL parameter reference>
sqlParameterReference
    : basicIdentifierChain
    ;

// <basic identifier chain>
basicIdentifierChain
    : identifierChain
    ;

// <identifier chain>
identifierChain
    : IDENTIFIER (DOT IDENTIFIER)*
    ;

// <numeric value expression>
numericValueExpression
    : term
    | numericValueExpression T__84 term
    | numericValueExpression T__85 term
    ;

// <term>
term
    : factor
    | term T__88 factor
    | term T__108 factor
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

// <parenthesized value expression>
parenthesizedValueExpression
    : LEFT_PAREN betweenPredicate RIGHT_PAREN
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

// <column reference>
columnReference
    : basicIdentifierChain
    | T__23 DOT IDENTIFIER DOT columnName
    ;

// <set function specification>
setFunctionSpecification
    : T__109 LEFT_PAREN T__88 RIGHT_PAREN
    | generalSetFunction
    | groupingOperation
    ;

// <general set function>
generalSetFunction
    : setFunctionType LEFT_PAREN (setQuantifier)? betweenPredicate RIGHT_PAREN
    ;

// <set function type>
setFunctionType
    : computationalOperation
    ;

// <computational operation>
computationalOperation
    : T__110
    | T__111
    | T__112
    | T__113
    | T__114
    | T__115
    | T__116
    | T__109
    ;

// <set quantifier>
setQuantifier
    : T__117
    | T__118
    ;

// <grouping operation>
groupingOperation
    : T__119 LEFT_PAREN columnReference RIGHT_PAREN
    ;

// <scalar subquery>
scalarSubquery
    : subquery
    ;

// <subquery>
subquery
    : LEFT_PAREN queryExpression RIGHT_PAREN
    ;

// <query expression>
queryExpression
    : (withClause)? non_joinQueryExpression
    ;

// <with clause>
withClause
    : T__54 (T__120)? withList
    ;

// <with list>
withList
    : withListElement (COMMA withListElement)*
    ;

// <with list element>
withListElement
    : IDENTIFIER (LEFT_PAREN withColumnList RIGHT_PAREN)? T__121 LEFT_PAREN queryExpression RIGHT_PAREN (searchOrCycleClause)?
    ;

// <with column list>
withColumnList
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
    : T__122 recursiveSearchOrder T__25 sequenceColumn
    ;

// <recursive search order>
recursiveSearchOrder
    : T__123 T__124 T__125 sortSpecificationList
    | T__126 T__124 T__125 sortSpecificationList
    ;

// <sort specification list>
sortSpecificationList
    : sortSpecification (COMMA sortSpecification)*
    ;

// <sort specification>
sortSpecification
    : sortKey (orderingSpecification)?
    ;

// <sort key>
sortKey
    : betweenPredicate
    ;

// <ordering specification>
orderingSpecification
    : T__127
    | T__128
    ;

// <sequence column>
sequenceColumn
    : columnName
    ;

// <cycle clause>
cycleClause
    : T__129 cycleColumnList T__25 cycleMarkColumn T__58 cycleMarkValue T__72 non_cycleMarkValue T__130 pathColumn
    ;

// <cycle column list>
cycleColumnList
    : cycleColumn (COMMA cycleColumn)*
    ;

// <cycle column>
cycleColumn
    : columnName
    ;

// <cycle mark column>
cycleMarkColumn
    : columnName
    ;

// <cycle mark value>
cycleMarkValue
    : betweenPredicate
    ;

// <non-cycle mark value>
non_cycleMarkValue
    : betweenPredicate
    ;

// <path column>
pathColumn
    : columnName
    ;

// Merged rules: non-join query term, query term
non_joinQueryTerm
    : non_joinQueryPrimary
    | non_joinQueryTerm T__131 (T__118 | T__117)? (correspondingSpec)? queryPrimary
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
    : T__132 (setQuantifier)? selectList tableExpression
    ;

// <select list>
selectList
    : T__88
    | selectSublist (COMMA selectSublist)*
    ;

// <select sublist>
selectSublist
    : derivedColumn
    | qualifiedAsterisk
    ;

// <derived column>
derivedColumn
    : betweenPredicate (asClause)?
    ;

// <as clause>
asClause
    : (T__121)? columnName
    ;

// <qualified asterisk>
qualifiedAsterisk
    : asteriskedIdentifierChain DOT T__88
    | allFieldsReference
    ;

// <asterisked identifier chain>
asteriskedIdentifierChain
    : asteriskedIdentifier (DOT asteriskedIdentifier)*
    ;

// <asterisked identifier>
asteriskedIdentifier
    : IDENTIFIER
    ;

// <all fields reference>
allFieldsReference
    : arrayConcatenation DOT T__88
    ;

// <table expression>
tableExpression
    : fromClause (whereClause)? (groupByClause)? (havingClause)?
    ;

// <from clause>
fromClause
    : T__133 tableReferenceList
    ;

// <table reference list>
tableReferenceList
    : crossJoin (COMMA crossJoin)*
    ;

// <table or query name>
tableOrQueryName
    : tableName
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

// <lateral derived table>
lateralDerivedTable
    : T__134 LEFT_PAREN queryExpression RIGHT_PAREN
    ;

// <collection derived table>
collectionDerivedTable
    : T__135 LEFT_PAREN collectionValueExpression RIGHT_PAREN (T__54 T__136)?
    ;

// <collection value expression>
collectionValueExpression
    : arrayConcatenation
    ;

// <only spec>
onlySpec
    : T__137 LEFT_PAREN tableOrQueryName RIGHT_PAREN
    ;

// Merged rules: table reference, table primary, joined table, cross join, qualified join, natural join, union join
crossJoin
    : tableOrQueryName ((T__121)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?)?
    | derivedTable (T__121)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?
    | lateralDerivedTable (T__121)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?
    | collectionDerivedTable (T__121)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?
    | onlySpec ((T__121)? correlationName (LEFT_PAREN derivedColumnList RIGHT_PAREN)?)?
    ;

// <join type>
joinType
    : T__138
    | outerJoinType (T__139)?
    ;

// <outer join type>
outerJoinType
    : T__140
    | T__141
    | T__101
    ;

// <join specification>
joinSpecification
    : joinCondition
    | namedColumnsJoin
    ;

// <join condition>
joinCondition
    : T__20 searchCondition
    ;

// <named columns join>
namedColumnsJoin
    : T__130 LEFT_PAREN joinColumnList RIGHT_PAREN
    ;

// <join column list>
joinColumnList
    : columnNameList
    ;

// <where clause>
whereClause
    : T__142 searchCondition
    ;

// <group by clause>
groupByClause
    : T__13 T__125 groupingElementList
    ;

// <grouping element list>
groupingElementList
    : groupingElement (COMMA groupingElement)*
    ;

// <grouping element>
groupingElement
    : ordinaryGroupingSet
    | rollupList
    | cubeList
    | groupingSetsSpecification
    | grandTotal
    ;

// <grouping column reference>
groupingColumnReference
    : columnReference (collateClause)?
    ;

// <rollup list>
rollupList
    : T__143 LEFT_PAREN groupingColumnReferenceList RIGHT_PAREN
    ;

// <grouping column reference list>
groupingColumnReferenceList
    : groupingColumnReference (COMMA groupingColumnReference)*
    ;

// <cube list>
cubeList
    : T__144 LEFT_PAREN groupingColumnReferenceList RIGHT_PAREN
    ;

// <grouping sets specification>
groupingSetsSpecification
    : T__119 T__145 LEFT_PAREN groupingSetList RIGHT_PAREN
    ;

// <grouping set list>
groupingSetList
    : groupingSet (COMMA groupingSet)*
    ;

// <grouping set>
groupingSet
    : ordinaryGroupingSet
    | rollupList
    | cubeList
    | groupingSetsSpecification
    | grandTotal
    ;

// <ordinary grouping set>
ordinaryGroupingSet
    : groupingColumnReference
    | LEFT_PAREN groupingColumnReferenceList RIGHT_PAREN
    ;

// <grand total>
grandTotal
    : LEFT_PAREN RIGHT_PAREN
    ;

// <concatenated grouping>
concatenatedGrouping
    : groupingSet COMMA groupingSetList
    ;

// <having clause>
havingClause
    : T__146 searchCondition
    ;

// <table value constructor>
tableValueConstructor
    : T__147 rowValueExpressionList
    ;

// <row value expression list>
rowValueExpressionList
    : betweenPredicate (COMMA betweenPredicate)*
    ;

// <explicit table>
explicitTable
    : T__19 tableName
    ;

// <corresponding spec>
correspondingSpec
    : T__148 (T__125 LEFT_PAREN correspondingColumnList RIGHT_PAREN)?
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
    : T__149 LEFT_PAREN betweenPredicate COMMA betweenPredicate RIGHT_PAREN
    | T__150 LEFT_PAREN betweenPredicate (COMMA betweenPredicate)* RIGHT_PAREN
    ;

// <case specification>
caseSpecification
    : simpleCase
    | searchedCase
    ;

// <simple case>
simpleCase
    : T__151 caseOperand simpleWhenClause (elseClause)? T__152
    ;

// <case operand>
caseOperand
    : betweenPredicate
    ;

// <simple when clause>
simpleWhenClause
    : T__153 whenOperand T__154 result
    ;

// <when operand>
whenOperand
    : betweenPredicate
    ;

// <result>
result
    : resultExpression
    | T__71
    ;

// <result expression>
resultExpression
    : betweenPredicate
    ;

// <else clause>
elseClause
    : T__155 result
    ;

// <searched case>
searchedCase
    : T__151 searchedWhenClause (elseClause)? T__152
    ;

// <searched when clause>
searchedWhenClause
    : T__153 searchCondition T__154 result
    ;

// <cast specification>
castSpecification
    : T__156 LEFT_PAREN castOperand T__121 castTarget RIGHT_PAREN
    ;

// <cast operand>
castOperand
    : betweenPredicate
    | implicitlyTypedValueSpecification
    ;

// <cast target>
castTarget
    : domainName
    | collectionType
    ;

// <subtype treatment>
subtypeTreatment
    : T__157 LEFT_PAREN subtypeOperand T__121 targetSubtype RIGHT_PAREN
    ;

// <subtype operand>
subtypeOperand
    : betweenPredicate
    ;

// <target subtype>
targetSubtype
    : user_definedType
    ;

// <dereference operator>
dereferenceOperator
    : rightArrow
    ;

// <right arrow>
rightArrow
    : IDENTIFIER
    ;

// <SQL argument list>
sqlArgumentList
    : LEFT_PAREN (sqlArgument (COMMA sqlArgument)*)? RIGHT_PAREN
    ;

// <SQL argument>
sqlArgument
    : betweenPredicate
    | generalizedExpression
    | targetSpecification
    ;

// <generalized expression>
generalizedExpression
    : betweenPredicate T__121 user_definedType
    ;

// <target specification>
targetSpecification
    : hostParameterSpecification
    | sqlParameterReference
    | columnReference
    | sqlVariableReference
    | dynamicParameterSpecification
    | embeddedVariableSpecification
    ;

// <reference resolution>
referenceResolution
    : T__158 LEFT_PAREN referenceValueExpression RIGHT_PAREN
    ;

// <reference value expression>
referenceValueExpression
    : arrayConcatenation
    ;

// <array value constructor>
arrayValueConstructor
    : arrayValueListConstructor
    ;

// <array value list constructor>
arrayValueListConstructor
    : T__79 leftBracketOrTrigraph arrayElementList rightBracketOrTrigraph
    ;

// <array element list>
arrayElementList
    : arrayElement (COMMA arrayElement)*
    ;

// <array element>
arrayElement
    : betweenPredicate
    ;

// Merged rules: value expression primary, nonparenthesized value expression primary, attribute or method reference, collection value constructor, array value expression, array concatenation, array value expression 1, array value expression 2, field reference, element reference, method invocation, direct invocation
arrayConcatenation
    : parenthesizedValueExpression
    | unsignedValueSpecification
    | columnReference
    | setFunctionSpecification
    | scalarSubquery
    | caseExpression
    | castSpecification
    | subtypeTreatment
    | referenceResolution
    | routineInvocation
    | staticMethodInvocation
    | newSpecification
    | arrayValueConstructor
    | generalizedInvocation
    ;

// <routine invocation>
routineInvocation
    : IDENTIFIER sqlArgumentList
    ;

// <generalized invocation>
generalizedInvocation
    : LEFT_PAREN arrayConcatenation T__121 collectionType RIGHT_PAREN DOT IDENTIFIER (sqlArgumentList)?
    ;

// <constructor method selection>
constructorMethodSelection
    : routineInvocation
    ;

// <static method invocation>
staticMethodInvocation
    : user_definedType doubleColon IDENTIFIER (sqlArgumentList)?
    ;

// <double colon>
doubleColon
    : T__90 T__90
    ;

// <new specification>
newSpecification
    : T__159 routineInvocation
    ;

// <numeric value function>
numericValueFunction
    : positionExpression
    | extractExpression
    | lengthExpression
    | cardinalityExpression
    | absoluteValueExpression
    | modulusExpression
    ;

// <position expression>
positionExpression
    : stringPositionExpression
    | blobPositionExpression
    ;

// <string position expression>
stringPositionExpression
    : T__160 LEFT_PAREN stringValueExpression T__161 stringValueExpression RIGHT_PAREN
    ;

// <string value expression>
stringValueExpression
    : characterValueExpression
    | bitValueExpression
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

// <string value function>
stringValueFunction
    : characterValueFunction
    | blobValueFunction
    | bitValueFunction
    ;

// <character value function>
characterValueFunction
    : characterSubstringFunction
    | regularExpressionSubstringFunction
    | fold
    | form_of_useConversion
    | characterTranslation
    | trimFunction
    | characterOverlayFunction
    | specificTypeMethod
    ;

// <character substring function>
characterSubstringFunction
    : T__162 LEFT_PAREN characterValueExpression T__133 startPosition (T__14 stringLength)? RIGHT_PAREN
    ;

// <start position>
startPosition
    : numericValueExpression
    ;

// <string length>
stringLength
    : numericValueExpression
    ;

// <regular expression substring function>
regularExpressionSubstringFunction
    : T__162 LEFT_PAREN characterValueExpression T__163 characterValueExpression T__164 escapeCharacter RIGHT_PAREN
    ;

// <escape character>
escapeCharacter
    : characterValueExpression
    ;

// <fold>
fold
    : (T__165 | T__166) LEFT_PAREN characterValueExpression RIGHT_PAREN
    ;

// <form-of-use conversion>
form_of_useConversion
    : T__167 LEFT_PAREN characterValueExpression T__130 form_of_useConversionName RIGHT_PAREN
    ;

// <form-of-use conversion name>
form_of_useConversionName
    : schemaQualifiedName
    ;

// <character translation>
characterTranslation
    : T__168 LEFT_PAREN characterValueExpression T__130 IDENTIFIER RIGHT_PAREN
    ;

// <trim function>
trimFunction
    : T__169 LEFT_PAREN trimOperands RIGHT_PAREN
    ;

// <trim operands>
trimOperands
    : ((trimSpecification)? (trimCharacter)? T__133)? trimSource
    ;

// <trim specification>
trimSpecification
    : T__170
    | T__171
    | T__172
    ;

// <trim character>
trimCharacter
    : characterValueExpression
    ;

// <trim source>
trimSource
    : characterValueExpression
    ;

// <character overlay function>
characterOverlayFunction
    : T__173 LEFT_PAREN characterValueExpression T__174 characterValueExpression T__133 startPosition (T__14 stringLength)? RIGHT_PAREN
    ;

// <specific type method>
specificTypeMethod
    : user_definedTypeValueExpression DOT T__175
    ;

// <user-defined type value expression>
user_definedTypeValueExpression
    : arrayConcatenation
    ;

// <blob value function>
blobValueFunction
    : blobSubstringFunction
    | blobTrimFunction
    | blobOverlayFunction
    ;

// <blob substring function>
blobSubstringFunction
    : T__162 LEFT_PAREN blobConcatenation T__133 startPosition (T__14 stringLength)? RIGHT_PAREN
    ;

// Merged rules: blob value expression, blob concatenation
blobConcatenation
    : blobFactor
    | blobConcatenation concatenationOperator blobFactor
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

// <blob trim function>
blobTrimFunction
    : T__169 LEFT_PAREN blobTrimOperands RIGHT_PAREN
    ;

// <blob trim operands>
blobTrimOperands
    : ((trimSpecification)? (trimOctet)? T__133)? blobTrimSource
    ;

// <trim octet>
trimOctet
    : blobConcatenation
    ;

// <blob trim source>
blobTrimSource
    : blobConcatenation
    ;

// <blob overlay function>
blobOverlayFunction
    : T__173 LEFT_PAREN blobConcatenation T__174 blobConcatenation T__133 startPosition (T__14 stringLength)? RIGHT_PAREN
    ;

// <blob position expression>
blobPositionExpression
    : T__160 LEFT_PAREN blobConcatenation T__161 blobConcatenation RIGHT_PAREN
    ;

// <extract expression>
extractExpression
    : T__176 LEFT_PAREN extractField T__133 extractSource RIGHT_PAREN
    ;

// <extract field>
extractField
    : primaryDatetimeField
    | timeZoneField
    ;

// <primary datetime field>
primaryDatetimeField
    : non_secondPrimaryDatetimeField
    | T__64
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
    | intervalValueExpression T__84 datetimeTerm
    | datetimeValueExpression T__84 intervalTerm
    | datetimeValueExpression T__85 intervalTerm
    ;

// Merged rules: interval term, interval term 2
intervalTerm
    : intervalFactor
    | intervalTerm T__88 factor
    | intervalTerm T__108 factor
    | term T__88 intervalFactor
    ;

// <interval factor>
intervalFactor
    : (sign)? intervalPrimary
    ;

// <interval value function>
intervalValueFunction
    : intervalAbsoluteValueFunction
    ;

// <interval absolute value function>
intervalAbsoluteValueFunction
    : T__177 LEFT_PAREN intervalValueExpression RIGHT_PAREN
    ;

// Merged rules: interval value expression, interval value expression 1
intervalValueExpression
    : intervalValueExpression T__84 intervalTerm1
    | intervalValueExpression T__85 intervalTerm1
    | LEFT_PAREN datetimeValueExpression T__85 datetimeTerm RIGHT_PAREN intervalQualifier
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
    : arrayConcatenation
    | datetimeValueFunction
    ;

// <time zone>
timeZone
    : T__178 timeZoneSpecifier
    ;

// <time zone specifier>
timeZoneSpecifier
    : T__17
    | T__52 T__55 intervalPrimary
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

// <octet length expression>
octetLengthExpression
    : IDENTIFIER LEFT_PAREN stringValueExpression RIGHT_PAREN
    ;

// <cardinality expression>
cardinalityExpression
    : T__179 LEFT_PAREN collectionValueExpression RIGHT_PAREN
    ;

// <absolute value expression>
absoluteValueExpression
    : T__177 LEFT_PAREN numericValueExpression RIGHT_PAREN
    ;

// <modulus expression>
modulusExpression
    : T__180 LEFT_PAREN numericValueExpressionDividend COMMA numericValueExpressionDivisor RIGHT_PAREN
    ;

// <numeric value expression dividend>
numericValueExpressionDividend
    : numericValueExpression
    ;

// <numeric value expression divisor>
numericValueExpressionDivisor
    : numericValueExpression
    ;

// <row subquery>
rowSubquery
    : subquery
    ;

// <comp op>
compOp
    : T__181
    | notEqualsOperator
    | T__182
    | T__183
    | lessThanOrEqualsOperator
    | greaterThanOrEqualsOperator
    ;

// <not equals operator>
notEqualsOperator
    : T__182 T__183
    ;

// <less than or equals operator>
lessThanOrEqualsOperator
    : T__182 T__181
    ;

// <greater than or equals operator>
greaterThanOrEqualsOperator
    : T__183 T__181
    ;

// Merged rules: boolean value expression, boolean term, boolean factor, boolean test, boolean primary, predicate, comparison predicate, row value expression, row value special case, value expression, row value constructor, row value constructor element, row value constructor element list, between predicate, in predicate, null predicate, quantified comparison predicate, match predicate, overlaps predicate, row value expression 1, row value expression 2, distinct predicate, row value expression 3, row value expression 4, parenthesized boolean value expression
betweenPredicate
    : likePredicate
    | existsPredicate
    | uniquePredicate
    | similarPredicate
    | typePredicate
    | valueSpecification
    | numericValueExpression
    | stringValueExpression
    | datetimeValueExpression
    | user_definedTypeValueExpression
    | referenceValueExpression
    | collectionValueExpression
    | rowSubquery
    ;

// <in predicate value>
inPredicateValue
    : tableSubquery
    | LEFT_PAREN inValueList RIGHT_PAREN
    ;

// <in value list>
inValueList
    : betweenPredicate (COMMA betweenPredicate)*
    ;

// <like predicate>
likePredicate
    : characterLikePredicate
    | octetLikePredicate
    ;

// <character like predicate>
characterLikePredicate
    : characterMatchValue (T__67)? T__184 characterPattern (T__164 escapeCharacter)?
    ;

// <character match value>
characterMatchValue
    : characterValueExpression
    ;

// <character pattern>
characterPattern
    : characterValueExpression
    ;

// <octet like predicate>
octetLikePredicate
    : octetMatchValue (T__67)? T__184 octetPattern (T__164 escapeOctet)?
    ;

// <octet match value>
octetMatchValue
    : blobConcatenation
    ;

// <octet pattern>
octetPattern
    : blobConcatenation
    ;

// <escape octet>
escapeOctet
    : blobConcatenation
    ;

// <quantifier>
quantifier
    : all
    | some
    ;

// <all>
all
    : T__118
    ;

// <some>
some
    : T__116
    | T__115
    ;

// <exists predicate>
existsPredicate
    : T__185 tableSubquery
    ;

// <unique predicate>
uniquePredicate
    : T__97 tableSubquery
    ;

// <similar predicate>
similarPredicate
    : characterMatchValue (T__67)? T__163 T__58 similarPattern (T__164 escapeCharacter)?
    ;

// <similar pattern>
similarPattern
    : characterValueExpression
    ;

// <regular expression>
regularExpression
    : regularTerm
    | regularExpression T__186 regularTerm
    ;

// <regular term>
regularTerm
    : regularFactor
    | regularTerm regularFactor
    ;

// <regular factor>
regularFactor
    : regularPrimary
    | regularPrimary T__88
    | regularPrimary T__84
    ;

// <regular primary>
regularPrimary
    : characterSpecifier
    | T__187
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
    : T__188
    | T__80 characterEnumeration T__82
    | T__80 T__189 characterEnumeration T__82
    | T__80 T__90 regularCharacterSetIdentifier T__90 T__82
    ;

// <character enumeration>
characterEnumeration
    : characterSpecifier
    | characterSpecifier T__85 characterSpecifier
    ;

// <regular character set identifier>
regularCharacterSetIdentifier
    : IDENTIFIER
    ;

// <type predicate>
typePredicate
    : user_definedTypeValueExpression T__190 (T__67)? T__191 LEFT_PAREN typeList RIGHT_PAREN
    ;

// <type list>
typeList
    : user_definedTypeSpecification (COMMA user_definedTypeSpecification)*
    ;

// <user-defined type specification>
user_definedTypeSpecification
    : inclusiveUser_definedTypeSpecification
    | exclusiveUser_definedTypeSpecification
    ;

// <inclusive user-defined type specification>
inclusiveUser_definedTypeSpecification
    : user_definedType
    ;

// <exclusive user-defined type specification>
exclusiveUser_definedTypeSpecification
    : T__137 user_definedType
    ;

// <truth value>
truthValue
    : T__91
    | T__92
    | T__93
    ;

// <constraint characteristics>
constraintCharacteristics
    : constraintCheckTime ((T__67)? T__192)?
    | (T__67)? T__192 (constraintCheckTime)?
    ;

// <constraint check time>
constraintCheckTime
    : T__193 T__194
    | T__193 T__195
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
    | T__97 LEFT_PAREN T__106 RIGHT_PAREN
    ;

// <unique column list>
uniqueColumnList
    : columnNameList
    ;

// <referential constraint definition>
referentialConstraintDefinition
    : T__196 T__99 LEFT_PAREN referencingColumns RIGHT_PAREN referencesSpecification
    ;

// <referencing columns>
referencingColumns
    : referenceColumnList
    ;

// <like clause>
likeClause
    : T__184 tableName
    ;

// <self-referencing column specification>
self_referencingColumnSpecification
    : T__77 T__190 self_referencingColumnName referenceGeneration
    ;

// <self-referencing column name>
self_referencingColumnName
    : columnName
    ;

// <reference generation>
referenceGeneration
    : T__197 T__198
    | T__83 T__198
    | T__199
    ;

// <column options>
columnOptions
    : columnName T__54 T__200 columnOptionList
    ;

// <column option list>
columnOptionList
    : (scopeClause)? (defaultClause)? (columnConstraintDefinition)? (collateClause)?
    ;

// <table commit action>
tableCommitAction
    : T__201
    | T__69
    ;

// <module contents>
moduleContents
    : declareCursor
    | externally_invokedProcedure
    | dynamicDeclareCursor
    ;

// <declare cursor>
declareCursor
    : T__16 IDENTIFIER (cursorSensitivity)? (cursorScrollability)? T__202 (cursorHoldability)? (cursorReturnability)? T__14 cursorSpecification
    ;

// <local qualified name>
localQualifiedName
    : (localQualifier DOT)? IDENTIFIER
    ;

// <local qualifier>
localQualifier
    : T__23
    ;

// <cursor sensitivity>
cursorSensitivity
    : T__203
    | T__204
    | T__205
    ;

// <cursor scrollability>
cursorScrollability
    : T__206
    | T__74 T__206
    ;

// <cursor holdability>
cursorHoldability
    : T__54 T__207
    | T__56 T__207
    ;

// <cursor returnability>
cursorReturnability
    : T__54 T__208
    | T__56 T__208
    ;

// <cursor specification>
cursorSpecification
    : queryExpression (orderByClause)? (updatabilityClause)?
    ;

// <order by clause>
orderByClause
    : T__209 T__125 sortSpecificationList
    ;

// <updatability clause>
updatabilityClause
    : T__14 (T__210 T__137 | T__104 (T__191 columnNameList)?)
    ;

// <externally-invoked procedure>
externally_invokedProcedure
    : T__211 procedureName hostParameterDeclarationSetup SEMI sqlProcedureStatement SEMI
    ;

// <procedure name>
procedureName
    : IDENTIFIER
    ;

// <host parameter declaration setup>
hostParameterDeclarationSetup
    : hostParameterDeclarationList
    ;

// <host parameter declaration list>
hostParameterDeclarationList
    : LEFT_PAREN hostParameterDeclaration (COMMA hostParameterDeclaration)* RIGHT_PAREN
    ;

// <host parameter declaration>
hostParameterDeclaration
    : hostParameterName hostParameterDataType
    | statusParameter
    ;

// <host parameter data type>
hostParameterDataType
    : collectionType (locatorIndication)?
    ;

// <locator indication>
locatorIndication
    : T__121 T__212
    ;

// <status parameter>
statusParameter
    : T__213
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
    | translationDefinition
    | assertionDefinition
    | triggerDefinition
    | user_definedTypeDefinition
    | user_definedCastDefinition
    | user_definedOrderingDefinition
    | transformDefinition
    | sQL_serverModuleDefinition
    ;

// <schema definition>
schemaDefinition
    : T__214 T__215 schemaName (schemaCharacterSetOrPath)? (schemaElement)?
    ;

// <schema authorization identifier>
schemaAuthorizationIdentifier
    : IDENTIFIER
    ;

// <schema character set or path>
schemaCharacterSetOrPath
    : schemaCharacterSetSpecification
    | schemaPathSpecification
    | schemaCharacterSetSpecification schemaPathSpecification
    | schemaPathSpecification schemaCharacterSetSpecification
    ;

// <schema character set specification>
schemaCharacterSetSpecification
    : T__72 T__24 T__25 characterSetSpecification
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
    | translationDefinition
    | assertionDefinition
    | triggerDefinition
    | user_definedTypeDefinition
    | schemaRoutine
    | grantStatement
    | roleDefinition
    | user_definedCastDefinition
    | user_definedOrderingDefinition
    | transformDefinition
    ;

// <table definition>
tableDefinition
    : T__214 (tableScope)? T__19 tableName tableContentsSource (T__20 T__21 tableCommitAction T__22)?
    ;

// <table scope>
tableScope
    : globalOrLocal T__18
    ;

// <global or local>
globalOrLocal
    : T__216
    | T__17
    ;

// <table contents source>
tableContentsSource
    : tableElementList
    | T__191 user_definedType (subtableClause)? (tableElementList)?
    ;

// <subtable clause>
subtableClause
    : T__217 supertableClause
    ;

// <supertable clause>
supertableClause
    : supertableName
    ;

// <supertable name>
supertableName
    : tableName
    ;

// <view definition>
viewDefinition
    : T__214 (T__120)? T__218 tableName viewSpecification T__121 queryExpression (T__54 (levelsClause)? T__105 T__219)?
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

// <view column list>
viewColumnList
    : columnNameList
    ;

// <referenceable view specification>
referenceableViewSpecification
    : T__191 user_definedType (subviewClause)? (viewElementList)?
    ;

// <subview clause>
subviewClause
    : T__217 tableName
    ;

// <view element list>
viewElementList
    : LEFT_PAREN (self_referencingColumnSpecification COMMA)? viewElement (COMMA viewElement)* RIGHT_PAREN
    ;

// <view element>
viewElement
    : viewColumnOption
    ;

// <view column option>
viewColumnOption
    : columnName T__54 T__200 scopeClause
    ;

// <levels clause>
levelsClause
    : T__220
    | T__17
    ;

// <domain definition>
domainDefinition
    : T__214 T__221 domainName (T__121)? collectionType (defaultClause)? (domainConstraint)? (collateClause)?
    ;

// <domain constraint>
domainConstraint
    : (constraintNameDefinition)? checkConstraintDefinition (constraintCharacteristics)?
    ;

// <character set definition>
characterSetDefinition
    : T__214 T__24 T__25 IDENTIFIER (T__121)? characterSetSource (collateClause)?
    ;

// <character set source>
characterSetSource
    : T__222 characterSetSpecification
    ;

// <collation definition>
collationDefinition
    : T__214 T__223 IDENTIFIER T__14 characterSetSpecification T__133 existingCollationName (padCharacteristic)?
    ;

// <existing collation name>
existingCollationName
    : IDENTIFIER
    ;

// <pad characteristic>
padCharacteristic
    : T__74 T__224
    | T__224 T__225
    ;

// <translation definition>
translationDefinition
    : T__214 T__226 IDENTIFIER T__14 sourceCharacterSetSpecification T__58 targetCharacterSetSpecification T__133 translationSource
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
    : existingTranslationName
    | translationRoutine
    ;

// <existing translation name>
existingTranslationName
    : IDENTIFIER
    ;

// <translation routine>
translationRoutine
    : specificRoutineDesignator
    ;

// <specific routine designator>
specificRoutineDesignator
    : T__227 routineType IDENTIFIER
    | routineType memberName (T__14 IDENTIFIER)?
    ;

// <routine type>
routineType
    : T__228
    | T__229
    | T__211
    | (T__230 | T__231 | T__232)? T__233
    ;

// <member name>
memberName
    : schemaQualifiedRoutineName (dataTypeList)?
    ;

// <schema qualified routine name>
schemaQualifiedRoutineName
    : schemaQualifiedName
    ;

// <data type list>
dataTypeList
    : LEFT_PAREN (collectionType (COMMA collectionType)*)? RIGHT_PAREN
    ;

// <assertion definition>
assertionDefinition
    : T__214 T__234 constraintName T__105 LEFT_PAREN searchCondition RIGHT_PAREN (constraintCharacteristics)?
    ;

// <trigger definition>
triggerDefinition
    : T__214 T__235 triggerName triggerActionTime triggerEvent T__20 tableName (T__236 oldOrNewValuesAliasList)? triggeredAction
    ;

// <trigger action time>
triggerActionTime
    : T__237
    | T__238
    ;

// <trigger event>
triggerEvent
    : T__239
    | T__69
    | T__104 (T__191 triggerColumnList)?
    ;

// <trigger column list>
triggerColumnList
    : columnNameList
    ;

// <old or new values alias list>
oldOrNewValuesAliasList
    : oldOrNewValuesAlias
    ;

// <old or new values alias>
oldOrNewValuesAlias
    : T__240 (T__65)? (T__121)? oldValuesCorrelationName
    | T__159 (T__65)? (T__121)? newValuesCorrelationName
    | T__240 T__19 (T__121)? oldValuesTableAlias
    | T__159 T__19 (T__121)? newValuesTableAlias
    ;

// <old values correlation name>
oldValuesCorrelationName
    : correlationName
    ;

// <new values correlation name>
newValuesCorrelationName
    : correlationName
    ;

// <old values table alias>
oldValuesTableAlias
    : IDENTIFIER
    ;

// <new values table alias>
newValuesTableAlias
    : IDENTIFIER
    ;

// <triggered action>
triggeredAction
    : (T__14 T__241 (T__65 | T__242))? (T__153 LEFT_PAREN searchCondition RIGHT_PAREN)? triggeredSqlStatement
    ;

// <user-defined type definition>
user_definedTypeDefinition
    : T__214 T__15 user_definedTypeBody
    ;

// <user-defined type body>
user_definedTypeBody
    : IDENTIFIER (subtypeClause)? (T__121 representation)? (instantiableClause)? finality (referenceTypeSpecification)? (refCastOption)? (castOption)? (methodSpecificationList)?
    ;

// <subtype clause>
subtypeClause
    : T__217 supertypeName
    ;

// <supertype name>
supertypeName
    : user_definedType
    ;

// <representation>
representation
    : predefinedType
    | memberList
    ;

// <member list>
memberList
    : LEFT_PAREN member (COMMA member)* RIGHT_PAREN
    ;

// <member>
member
    : attributeDefinition
    ;

// <attribute definition>
attributeDefinition
    : IDENTIFIER collectionType (referenceScopeCheck)? (attributeDefault)? (collateClause)?
    ;

// <attribute default>
attributeDefault
    : defaultClause
    ;

// <instantiable clause>
instantiableClause
    : T__243
    | T__67 T__243
    ;

// <finality>
finality
    : T__244
    | T__67 T__244
    ;

// <reference type specification>
referenceTypeSpecification
    : user_definedRepresentation
    | derivedRepresentation
    | system_generatedRepresentation
    ;

// <user-defined representation>
user_definedRepresentation
    : T__77 T__130 predefinedType
    ;

// <ref cast option>
refCastOption
    : castToRef (castToType)?
    | castToType
    ;

// <cast to ref>
castToRef
    : T__156 LEFT_PAREN T__245 T__121 T__77 RIGHT_PAREN T__54 castToRefIdentifier
    ;

// <cast to ref identifier>
castToRefIdentifier
    : IDENTIFIER
    ;

// <cast to type>
castToType
    : T__156 LEFT_PAREN T__77 T__121 T__245 RIGHT_PAREN T__54 castToTypeIdentifier
    ;

// <cast to type identifier>
castToTypeIdentifier
    : IDENTIFIER
    ;

// <derived representation>
derivedRepresentation
    : T__77 T__133 listOfAttributes
    ;

// <list of attributes>
listOfAttributes
    : LEFT_PAREN IDENTIFIER (COMMA IDENTIFIER)* RIGHT_PAREN
    ;

// <system-generated representation>
system_generatedRepresentation
    : T__77 T__190 T__197 T__198
    ;

// <cast option>
castOption
    : castToDistinct (castToSource)?
    | castToSource
    ;

// <cast to distinct>
castToDistinct
    : T__156 LEFT_PAREN T__245 T__121 T__117 RIGHT_PAREN T__54 castToDistinctIdentifier
    ;

// <cast to distinct identifier>
castToDistinctIdentifier
    : IDENTIFIER
    ;

// <cast to source>
castToSource
    : T__156 LEFT_PAREN T__117 T__121 T__245 RIGHT_PAREN T__54 castToSourceIdentifier
    ;

// <cast to source identifier>
castToSourceIdentifier
    : IDENTIFIER
    ;

// <method specification list>
methodSpecificationList
    : methodSpecification (COMMA methodSpecification)*
    ;

// <method specification>
methodSpecification
    : originalMethodSpecification
    | overridingMethodSpecification
    ;

// <original method specification>
originalMethodSpecification
    : partialMethodSpecification (T__246 T__121 T__247)? (T__246 T__121 T__212)? (methodCharacteristics)?
    ;

// <partial method specification>
partialMethodSpecification
    : (T__230 | T__231 | T__232)? T__233 IDENTIFIER sqlParameterDeclarationList returnsClause (T__227 specificMethodName)?
    ;

// <SQL parameter declaration list>
sqlParameterDeclarationList
    : LEFT_PAREN (sqlParameterDeclaration (COMMA sqlParameterDeclaration)*)? RIGHT_PAREN
    ;

// <SQL parameter declaration>
sqlParameterDeclaration
    : (parameterMode)? (sqlParameterName)? parameterType (T__247)?
    ;

// <parameter mode>
parameterMode
    : T__161
    | T__248
    | T__249
    ;

// <SQL parameter name>
sqlParameterName
    : IDENTIFIER
    ;

// <parameter type>
parameterType
    : collectionType (locatorIndication)?
    ;

// <returns clause>
returnsClause
    : T__250 returnsDataType (resultCast)?
    ;

// <returns data type>
returnsDataType
    : collectionType (locatorIndication)?
    ;

// <result cast>
resultCast
    : T__156 T__133 resultCastFromType
    ;

// <result cast from type>
resultCastFromType
    : collectionType (locatorIndication)?
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

// <parameter style clause>
parameterStyleClause
    : T__251 T__252 parameterStyle
    ;

// <parameter style>
parameterStyle
    : T__10
    | T__253
    ;

// <deterministic characteristic>
deterministicCharacteristic
    : T__254
    | T__67 T__254
    ;

// <SQL-data access indication>
sQL_dataAccessIndication
    : T__74 T__10
    | T__255 T__10
    | T__256 T__10 T__257
    | T__258 T__10 T__257
    ;

// <null-call clause>
null_callClause
    : T__250 T__71 T__20 T__71 T__259
    | T__260 T__20 T__71 T__259
    ;

// <overriding method specification>
overridingMethodSpecification
    : T__261 partialMethodSpecification
    ;

// <schema routine>
schemaRoutine
    : schemaProcedure
    | schemaFunction
    ;

// <schema procedure>
schemaProcedure
    : T__214 sQL_invokedProcedure
    ;

// <SQL-invoked procedure>
sQL_invokedProcedure
    : T__211 schemaQualifiedRoutineName sqlParameterDeclarationList routineCharacteristics routineBody
    ;

// <routine characteristics>
routineCharacteristics
    : (routineCharacteristic)?
    ;

// <routine characteristic>
routineCharacteristic
    : languageClause
    | parameterStyleClause
    | T__227 IDENTIFIER
    | deterministicCharacteristic
    | sQL_dataAccessIndication
    | null_callClause
    | dynamicResultSetsCharacteristic
    ;

// <dynamic result sets characteristic>
dynamicResultSetsCharacteristic
    : T__262 T__247 T__145 maximumDynamicResultSets
    ;

// <maximum dynamic result sets>
maximumDynamicResultSets
    : NUMBER
    ;

// <routine body>
routineBody
    : sqlRoutineBody
    | externalBodyReference
    ;

// <SQL routine body>
sqlRoutineBody
    : sqlProcedureStatement
    ;

// <external body reference>
externalBodyReference
    : T__263 (T__264 externalRoutineName)? (parameterStyleClause)? (transformGroupSpecification)? (externalSecurityClause)?
    ;

// <external routine name>
externalRoutineName
    : IDENTIFIER
    | STRING
    ;

// <external security clause>
externalSecurityClause
    : T__263 T__265 T__266
    | T__263 T__265 T__267
    | T__263 T__265 T__268 T__269
    ;

// <schema function>
schemaFunction
    : T__214 sQL_invokedFunction
    ;

// <SQL-invoked function>
sQL_invokedFunction
    : (functionSpecification | methodSpecificationDesignator) routineBody
    ;

// <function specification>
functionSpecification
    : T__229 schemaQualifiedRoutineName sqlParameterDeclarationList returnsClause routineCharacteristics (dispatchClause)?
    ;

// <dispatch clause>
dispatchClause
    : T__231 T__270
    ;

// <method specification designator>
methodSpecificationDesignator
    : (T__230 | T__231 | T__232)? T__233 IDENTIFIER sqlParameterDeclarationList (returnsClause)? T__14 IDENTIFIER
    ;

// <grant statement>
grantStatement
    : grantPrivilegeStatement
    | grantRoleStatement
    ;

// <grant privilege statement>
grantPrivilegeStatement
    : T__271 privileges T__58 grantee (COMMA grantee)* (T__54 T__272 T__219)? (T__54 T__271 T__219)? (T__273 T__125 grantor)?
    ;

// <privileges>
privileges
    : objectPrivileges T__20 objectName
    ;

// <object privileges>
objectPrivileges
    : T__118 T__274
    | action (COMMA action)*
    ;

// <action>
action
    : T__132
    | T__132 LEFT_PAREN privilegeColumnList RIGHT_PAREN
    | T__132 LEFT_PAREN privilegeMethodList RIGHT_PAREN
    | T__69
    | T__239 (LEFT_PAREN privilegeColumnList RIGHT_PAREN)?
    | T__104 (LEFT_PAREN privilegeColumnList RIGHT_PAREN)?
    | T__66 (LEFT_PAREN privilegeColumnList RIGHT_PAREN)?
    | T__275
    | T__235
    | T__217
    | T__276
    ;

// <privilege column list>
privilegeColumnList
    : columnNameList
    ;

// <privilege method list>
privilegeMethodList
    : specificRoutineDesignator (COMMA specificRoutineDesignator)*
    ;

// <object name>
objectName
    : (T__19)? tableName
    | T__221 domainName
    | T__223 IDENTIFIER
    | T__24 T__25 IDENTIFIER
    | T__23 IDENTIFIER
    | T__226 IDENTIFIER
    | T__15 IDENTIFIER
    | specificRoutineDesignator
    ;

// <grantee>
grantee
    : T__277
    | IDENTIFIER
    ;

// <grantor>
grantor
    : IDENTIFIER
    | IDENTIFIER
    ;

// <grant role statement>
grantRoleStatement
    : T__271 roleGranted (COMMA roleGranted)* T__58 grantee (COMMA grantee)* (T__54 T__278 T__219)? (T__273 T__125 grantor)?
    ;

// <role granted>
roleGranted
    : IDENTIFIER
    ;

// <role definition>
roleDefinition
    : T__214 T__279 IDENTIFIER (T__54 T__278 grantor)?
    ;

// <SQL-invoked routine>
sQL_invokedRoutine
    : schemaRoutine
    | moduleRoutine
    ;

// <user-defined cast definition>
user_definedCastDefinition
    : T__214 T__156 LEFT_PAREN sourceDataType T__121 IDENTIFIER RIGHT_PAREN T__54 castFunction (T__121 T__280)?
    ;

// <source data type>
sourceDataType
    : collectionType
    ;

// <cast function>
castFunction
    : specificRoutineDesignator
    ;

// <user-defined ordering definition>
user_definedOrderingDefinition
    : T__214 T__281 T__14 IDENTIFIER orderingForm
    ;

// <ordering form>
orderingForm
    : equalsOrderingForm
    | fullOrderingForm
    ;

// <equals ordering form>
equalsOrderingForm
    : T__282 T__137 T__125 orderingCategory
    ;

// <ordering category>
orderingCategory
    : relativeCategory
    | mapCategory
    | stateCategory
    ;

// <relative category>
relativeCategory
    : T__283 T__54 relativeFunctionSpecification
    ;

// <relative function specification>
relativeFunctionSpecification
    : specificRoutineDesignator
    ;

// <map category>
mapCategory
    : T__284 T__54 mapFunctionSpecification
    ;

// <map function specification>
mapFunctionSpecification
    : specificRoutineDesignator
    ;

// <state category>
stateCategory
    : T__285 (IDENTIFIER)?
    ;

// <full ordering form>
fullOrderingForm
    : T__209 T__101 T__125 orderingCategory
    ;

// <transform definition>
transformDefinition
    : T__214 (T__12 | T__286) T__14 IDENTIFIER transformGroup
    ;

// <transform group>
transformGroup
    : groupName LEFT_PAREN transformElementList RIGHT_PAREN
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
    : T__58 T__10 T__54 toSqlFunction
    ;

// <to sql function>
toSqlFunction
    : specificRoutineDesignator
    ;

// <from sql>
fromSql
    : T__133 T__10 T__54 fromSqlFunction
    ;

// <from sql function>
fromSqlFunction
    : specificRoutineDesignator
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
    | dropTranslationStatement
    | dropAssertionStatement
    | dropTriggerStatement
    | alterTypeStatement
    | dropDataTypeStatement
    | dropUser_definedOrderingStatement
    | dropTransformStatement
    | dropModuleStatement
    ;

// <drop schema statement>
dropSchemaStatement
    : T__287 T__215 schemaName (dropBehavior)?
    ;

// <drop behavior>
dropBehavior
    : T__70
    | T__73
    ;

// <alter table statement>
alterTableStatement
    : T__288 T__19 tableName alterTableAction
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
    : T__289 (T__290)? columnDefinition
    ;

// <alter column definition>
alterColumnDefinition
    : T__288 (T__290)? columnName alterColumnAction
    ;

// <alter column action>
alterColumnAction
    : setColumnDefaultClause
    | dropColumnDefaultClause
    | addColumnScopeClause
    | dropColumnScopeClause
    ;

// <set column default clause>
setColumnDefaultClause
    : T__25 defaultClause
    ;

// <drop column default clause>
dropColumnDefaultClause
    : T__287 T__72
    ;

// <add column scope clause>
addColumnScopeClause
    : T__289 scopeClause
    ;

// <drop column scope clause>
dropColumnScopeClause
    : T__287 T__78 dropBehavior
    ;

// <drop column definition>
dropColumnDefinition
    : T__287 (T__290)? columnName dropBehavior
    ;

// <add table constraint definition>
addTableConstraintDefinition
    : T__289 tableConstraintDefinition
    ;

// <drop table constraint definition>
dropTableConstraintDefinition
    : T__287 T__96 constraintName dropBehavior
    ;

// <drop table statement>
dropTableStatement
    : T__287 T__19 tableName dropBehavior
    ;

// <drop view statement>
dropViewStatement
    : T__287 T__218 tableName dropBehavior
    ;

// <alter routine statement>
alterRoutineStatement
    : T__288 specificRoutineDesignator alterRoutineCharacteristics alterRoutineBehaviour
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
    | T__264 externalRoutineName
    ;

// <alter routine behaviour>
alterRoutineBehaviour
    : T__73
    ;

// <drop routine statement>
dropRoutineStatement
    : T__287 specificRoutineDesignator dropBehavior
    ;

// <drop user-defined cast statement>
dropUser_definedCastStatement
    : T__287 T__156 LEFT_PAREN sourceDataType T__121 IDENTIFIER RIGHT_PAREN dropBehavior
    ;

// <revoke statement>
revokeStatement
    : revokePrivilegeStatement
    | revokeRoleStatement
    ;

// <revoke privilege statement>
revokePrivilegeStatement
    : T__291 (revokeOptionExtension)? privileges T__133 grantee (COMMA grantee)* (T__273 T__125 grantor)? dropBehavior
    ;

// <revoke option extension>
revokeOptionExtension
    : T__271 T__219 T__14
    | T__272 T__219 T__14
    ;

// <revoke role statement>
revokeRoleStatement
    : T__291 (T__278 T__219 T__14)? roleRevoked (COMMA roleRevoked)* T__133 grantee (COMMA grantee)* (T__273 T__125 grantor)? dropBehavior
    ;

// <role revoked>
roleRevoked
    : IDENTIFIER
    ;

// <drop role statement>
dropRoleStatement
    : T__287 T__279 IDENTIFIER
    ;

// <alter domain statement>
alterDomainStatement
    : T__288 T__221 domainName alterDomainAction
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
    : T__25 defaultClause
    ;

// <drop domain default clause>
dropDomainDefaultClause
    : T__287 T__72
    ;

// <add domain constraint definition>
addDomainConstraintDefinition
    : T__289 domainConstraint
    ;

// <drop domain constraint definition>
dropDomainConstraintDefinition
    : T__287 T__96 constraintName
    ;

// <drop domain statement>
dropDomainStatement
    : T__287 T__221 domainName dropBehavior
    ;

// <drop character set statement>
dropCharacterSetStatement
    : T__287 T__24 T__25 IDENTIFIER
    ;

// <drop collation statement>
dropCollationStatement
    : T__287 T__223 IDENTIFIER dropBehavior
    ;

// <drop translation statement>
dropTranslationStatement
    : T__287 T__226 IDENTIFIER
    ;

// <drop assertion statement>
dropAssertionStatement
    : T__287 T__234 constraintName
    ;

// <drop trigger statement>
dropTriggerStatement
    : T__287 T__235 triggerName
    ;

// <alter type statement>
alterTypeStatement
    : T__288 T__15 IDENTIFIER alterTypeAction
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
    : T__289 T__292 attributeDefinition
    ;

// <drop attribute definition>
dropAttributeDefinition
    : T__287 T__292 IDENTIFIER T__73
    ;

// <add original method specification>
addOriginalMethodSpecification
    : T__289 originalMethodSpecification
    ;

// <add overriding method specification>
addOverridingMethodSpecification
    : T__289 overridingMethodSpecification
    ;

// <drop method specification>
dropMethodSpecification
    : T__287 specificMethodSpecificationDesignator T__73
    ;

// <specific method specification designator>
specificMethodSpecificationDesignator
    : T__227 T__233 specificMethodName
    | (T__230 | T__231 | T__232)? T__233 IDENTIFIER (dataTypeList)?
    ;

// <drop data type statement>
dropDataTypeStatement
    : T__287 T__15 IDENTIFIER dropBehavior
    ;

// <drop user-defined ordering statement>
dropUser_definedOrderingStatement
    : T__287 T__281 T__14 IDENTIFIER dropBehavior
    ;

// <drop transform statement>
dropTransformStatement
    : T__287 (T__12 | T__286) transformsToBeDropped T__14 IDENTIFIER dropBehavior
    ;

// <transforms to be dropped>
transformsToBeDropped
    : T__118
    | transformGroupElement
    ;

// <transform group element>
transformGroupElement
    : groupName
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

// <open statement>
openStatement
    : T__293 IDENTIFIER
    ;

// <fetch statement>
fetchStatement
    : T__294 ((fetchOrientation)? T__133)? IDENTIFIER T__295 fetchTargetList
    ;

// <fetch orientation>
fetchOrientation
    : T__296
    | T__297
    | T__124
    | T__298
    | (T__299 | T__283) simpleValueSpecification
    ;

// <simple value specification>
simpleValueSpecification
    : literal
    | hostParameterName
    | sqlParameterReference
    | sqlVariableReference
    | embeddedVariableName
    ;

// <fetch target list>
fetchTargetList
    : targetSpecification (COMMA targetSpecification)*
    ;

// <close statement>
closeStatement
    : T__300 IDENTIFIER
    ;

// <select statement: single row>
selectStatement_SingleRow
    : T__132 (setQuantifier)? selectList T__295 selectTargetList tableExpression
    ;

// <select target list>
selectTargetList
    : targetSpecification (COMMA targetSpecification)*
    ;

// <free locator statement>
freeLocatorStatement
    : T__301 T__212 locatorReference (COMMA locatorReference)*
    ;

// <locator reference>
locatorReference
    : hostParameterName
    | embeddedVariableName
    ;

// <hold locator statement>
holdLocatorStatement
    : T__207 T__212 locatorReference (COMMA locatorReference)*
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
    : T__69 T__133 targetTable T__142 T__302 T__191 IDENTIFIER
    ;

// <target table>
targetTable
    : tableName
    | (T__137)? LEFT_PAREN tableName RIGHT_PAREN
    ;

// <delete statement: searched>
deleteStatement_Searched
    : T__69 T__133 targetTable (T__142 searchCondition)?
    ;

// <insert statement>
insertStatement
    : T__239 T__295 insertionTarget insertColumnsAndSource
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

// <insert column list>
insertColumnList
    : columnNameList
    ;

// <from constructor>
fromConstructor
    : (LEFT_PAREN insertColumnList RIGHT_PAREN)? (overrideClause)? contextuallyTypedTableValueConstructor
    ;

// <override clause>
overrideClause
    : T__261 T__83 T__106
    | T__261 T__197 T__106
    ;

// <contextually typed table value constructor>
contextuallyTypedTableValueConstructor
    : T__147 contextuallyTypedRowValueExpressionList
    ;

// <contextually typed row value expression list>
contextuallyTypedRowValueExpressionList
    : contextuallyTypedRowValueExpression (COMMA contextuallyTypedRowValueExpression)*
    ;

// <contextually typed row value expression>
contextuallyTypedRowValueExpression
    : betweenPredicate
    | contextuallyTypedRowValueConstructor
    ;

// <contextually typed row value constructor>
contextuallyTypedRowValueConstructor
    : contextuallyTypedRowValueConstructorElement
    | (T__65)? LEFT_PAREN contextuallyTypedRowValueConstructorElementList RIGHT_PAREN
    ;

// <contextually typed row value constructor element>
contextuallyTypedRowValueConstructorElement
    : betweenPredicate
    | contextuallyTypedValueSpecification
    ;

// <contextually typed value specification>
contextuallyTypedValueSpecification
    : implicitlyTypedValueSpecification
    | defaultSpecification
    ;

// <default specification>
defaultSpecification
    : T__72
    ;

// <contextually typed row value constructor element list>
contextuallyTypedRowValueConstructorElementList
    : contextuallyTypedRowValueConstructorElement (COMMA contextuallyTypedRowValueConstructorElement)*
    ;

// <from default>
fromDefault
    : T__72 T__147
    ;

// <update statement: positioned>
updateStatement_Positioned
    : T__104 targetTable T__25 setClauseList T__142 T__302 T__191 IDENTIFIER
    ;

// <set clause list>
setClauseList
    : setClause (COMMA setClause)*
    ;

// <set clause>
setClause
    : updateTarget T__181 updateSource
    | mutatedSetClause T__181 updateSource
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

// <update source>
updateSource
    : betweenPredicate
    | contextuallyTypedValueSpecification
    ;

// Merged rules: mutated set clause, mutated target
mutatedSetClause
    : mutatedSetClause DOT IDENTIFIER
    | objectColumn
    ;

// <update statement: searched>
updateStatement_Searched
    : T__104 targetTable T__25 setClauseList (T__142 searchCondition)?
    ;

// <SQL control statement>
sqlControlStatement
    : callStatement
    | returnStatement
    | assignmentStatement
    | compoundStatement
    | caseStatement
    | ifStatement
    | iterateStatement
    | leaveStatement
    | loopStatement
    | whileStatement
    | repeatStatement
    | forStatement
    ;

// <call statement>
callStatement
    : T__303 routineInvocation
    ;

// <return statement>
returnStatement
    : T__208 returnValue
    ;

// <return value>
returnValue
    : betweenPredicate
    | T__71
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

// <start transaction statement>
startTransactionStatement
    : T__304 T__305 transactionMode (COMMA transactionMode)*
    ;

// <transaction mode>
transactionMode
    : isolationLevel
    | transactionAccessMode
    | diagnosticsSize
    ;

// <isolation level>
isolationLevel
    : T__306 T__307 levelOfIsolation
    ;

// <level of isolation>
levelOfIsolation
    : T__210 T__308
    | T__210 T__309
    | T__310 T__210
    | T__311
    ;

// <transaction access mode>
transactionAccessMode
    : T__210 T__137
    | T__210 T__312
    ;

// <diagnostics size>
diagnosticsSize
    : T__313 T__314 numberOfConditions
    ;

// <number of conditions>
numberOfConditions
    : simpleValueSpecification
    ;

// <set transaction statement>
setTransactionStatement
    : T__25 (T__17)? transactionCharacteristics
    ;

// <transaction characteristics>
transactionCharacteristics
    : T__305 transactionMode (COMMA transactionMode)*
    ;

// <set constraints mode statement>
setConstraintsModeStatement
    : T__25 T__315 constraintNameList (T__194 | T__195)
    ;

// <savepoint statement>
savepointStatement
    : T__316 savepointSpecifier
    ;

// <savepoint specifier>
savepointSpecifier
    : IDENTIFIER
    ;

// <simple target specification>
simpleTargetSpecification
    : hostParameterSpecification
    | sqlParameterReference
    | columnReference
    | sqlVariableReference
    | embeddedVariableName
    ;

// <release savepoint statement>
releaseSavepointStatement
    : T__317 T__316 savepointSpecifier
    ;

// <commit statement>
commitStatement
    : T__21 (T__318)? (T__319 (T__74)? T__320)?
    ;

// <rollback statement>
rollbackStatement
    : T__321 (T__318)? (T__319 (T__74)? T__320)? (savepointClause)?
    ;

// <savepoint clause>
savepointClause
    : T__58 T__316 savepointSpecifier
    ;

// <SQL connection statement>
sqlConnectionStatement
    : connectStatement
    | setConnectionStatement
    | disconnectStatement
    ;

// <connect statement>
connectStatement
    : T__322 T__58 connectionTarget
    ;

// <connection target>
connectionTarget
    : sQL_serverName (T__121 IDENTIFIER)? (T__83 connectionUserName)?
    | T__72
    ;

// <SQL-server name>
sQL_serverName
    : simpleValueSpecification
    ;

// <connection user name>
connectionUserName
    : simpleValueSpecification
    ;

// <set connection statement>
setConnectionStatement
    : T__25 T__323 connectionObject
    ;

// <connection object>
connectionObject
    : T__72
    | IDENTIFIER
    ;

// <disconnect statement>
disconnectStatement
    : T__324 disconnectObject
    ;

// <disconnect object>
disconnectObject
    : connectionObject
    | T__118
    | T__302
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
    ;

// <set session user identifier statement>
setSessionUserIdentifierStatement
    : T__25 T__325 T__326 valueSpecification
    ;

// <set role statement>
setRoleStatement
    : T__25 T__279 roleSpecification
    ;

// <role specification>
roleSpecification
    : valueSpecification
    | T__327
    ;

// <set local time zone statement>
setLocalTimeZoneStatement
    : T__25 T__52 T__55 setTimeZoneValue
    ;

// <set time zone value>
setTimeZoneValue
    : intervalValueExpression
    | T__17
    ;

// <set session characteristics statement>
setSessionCharacteristicsStatement
    : T__25 T__325 T__328 T__121 sessionCharacteristicList
    ;

// <session characteristic list>
sessionCharacteristicList
    : sessionCharacteristic (COMMA sessionCharacteristic)*
    ;

// <session characteristic>
sessionCharacteristic
    : transactionCharacteristics
    ;

// <SQL diagnostics statement>
sqlDiagnosticsStatement
    : getDiagnosticsStatement
    | signalStatement
    | resignalStatement
    ;

// <get diagnostics statement>
getDiagnosticsStatement
    : T__222 T__313 sqlDiagnosticsInformation
    ;

// <SQL diagnostics information>
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
    : simpleTargetSpecification T__181 statementInformationItemName
    ;

// <statement information item name>
statementInformationItemName
    : T__329
    | T__330
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
    : T__331 conditionNumber conditionInformationItem (COMMA conditionInformationItem)*
    ;

// <condition number>
conditionNumber
    : simpleValueSpecification
    ;

// <condition information item>
conditionInformationItem
    : simpleTargetSpecification T__181 conditionInformationItemName
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
    | IDENTIFIER
    ;

// <dereference operation>
dereferenceOperation
    : referenceValueExpression dereferenceOperator IDENTIFIER
    ;

// <method reference>
methodReference
    : arrayConcatenation dereferenceOperator IDENTIFIER sqlArgumentList
    ;

// <method selection>
methodSelection
    : routineInvocation
    ;

// <new invocation>
newInvocation
    : arrayConcatenation
    | routineInvocation
    ;

// <static method selection>
staticMethodSelection
    : routineInvocation
    ;

// <CLI routine>
cLIRoutine
    : cLIRoutineName cLIParameterList (cLIReturnsClause)?
    ;

// <CLI routine name>
cLIRoutineName
    : cLINamePrefix cLIGenericName
    ;

// <CLI name prefix>
cLINamePrefix
    : cLIBy_referencePrefix
    | cLIBy_valuePrefix
    ;

// <CLI by-reference prefix>
cLIBy_referencePrefix
    : T__332
    ;

// <CLI by-value prefix>
cLIBy_valuePrefix
    : T__10
    ;

// <CLI generic name>
cLIGenericName
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
    | implementation_definedCLIGenericName
    ;

// <CLI parameter list>
cLIParameterList
    : LEFT_PAREN cLIParameterDeclaration (COMMA cLIParameterDeclaration)* RIGHT_PAREN
    ;

// <CLI parameter declaration>
cLIParameterDeclaration
    : cLIParameterName cLIParameterMode cLIParameterDataType
    ;

// <CLI parameter mode>
cLIParameterMode
    : T__161
    | T__248
    | T__333
    | T__334
    | T__335
    ;

// <CLI parameter data type>
cLIParameterDataType
    : T__43
    | T__45
    | T__115
    | T__24 LEFT_PAREN length RIGHT_PAREN
    ;

// <CLI returns clause>
cLIReturnsClause
    : T__250 T__45
    ;

// <assignment statement>
assignmentStatement
    : T__25 assignmentTarget T__181 assignmentSource
    ;

// <assignment target>
assignmentTarget
    : targetSpecification
    | modifiedFieldReference
    | mutatedTargetSpecification
    ;

// <SQL variable reference>
sqlVariableReference
    : basicIdentifierChain
    ;

// Merged rules: modified field reference, modified field target
modifiedFieldReference
    : modifiedFieldReference DOT IDENTIFIER
    | targetSpecification
    | LEFT_PAREN targetSpecification RIGHT_PAREN
    ;

// Merged rules: mutator reference, mutated target specification
mutatedTargetSpecification
    : mutatedTargetSpecification DOT IDENTIFIER
    | targetSpecification
    | LEFT_PAREN targetSpecification RIGHT_PAREN
    ;

// <assignment source>
assignmentSource
    : betweenPredicate
    | contextuallyTypedSource
    ;

// <contextually typed source>
contextuallyTypedSource
    : implicitlyTypedValueSpecification
    | contextuallyTypedRowValueExpression
    ;

// <compound statement>
compoundStatement
    : (beginningLabel T__90)? T__336 ((T__67)? T__337)? (localDeclarationList)? (localCursorDeclarationList)? (localHandlerDeclarationList)? (sqlStatementList)? T__152 (endingLabel)?
    ;

// <beginning label>
beginningLabel
    : statementLabel
    ;

// <statement label>
statementLabel
    : IDENTIFIER
    ;

// <local declaration list>
localDeclarationList
    : terminatedLocalDeclaration
    ;

// <terminated local declaration>
terminatedLocalDeclaration
    : localDeclaration SEMI
    ;

// <local declaration>
localDeclaration
    : sqlVariableDeclaration
    | conditionDeclaration
    ;

// <SQL variable declaration>
sqlVariableDeclaration
    : T__16 sqlVariableNameList collectionType (defaultClause)?
    ;

// <SQL variable name list>
sqlVariableNameList
    : sqlVariableName (COMMA sqlVariableName)*
    ;

// <SQL variable name>
sqlVariableName
    : IDENTIFIER
    ;

// <condition declaration>
conditionDeclaration
    : T__16 conditionName T__338 (T__14 sqlstateValue)?
    ;

// <condition name>
conditionName
    : IDENTIFIER
    ;

// <sqlstate value>
sqlstateValue
    : T__213 (T__106)? STRING
    ;

// <local cursor declaration list>
localCursorDeclarationList
    : terminatedLocalCursorDeclaration
    ;

// <terminated local cursor declaration>
terminatedLocalCursorDeclaration
    : declareCursor SEMI
    ;

// <local handler declaration list>
localHandlerDeclarationList
    : terminatedLocalHandlerDeclaration
    ;

// <terminated local handler declaration>
terminatedLocalHandlerDeclaration
    : handlerDeclaration SEMI
    ;

// <handler declaration>
handlerDeclaration
    : T__16 handlerType T__339 T__14 conditionValueList handlerAction
    ;

// <handler type>
handlerType
    : T__340
    | T__341
    | T__342
    ;

// <condition value list>
conditionValueList
    : conditionValue (COMMA conditionValue)*
    ;

// <condition value>
conditionValue
    : sqlstateValue
    | conditionName
    | T__343
    | T__344
    | T__67 T__345
    ;

// <handler action>
handlerAction
    : sqlProcedureStatement
    ;

// <SQL statement list>
sqlStatementList
    : terminatedSqlStatement
    ;

// <terminated SQL statement>
terminatedSqlStatement
    : sqlProcedureStatement SEMI
    ;

// <ending label>
endingLabel
    : statementLabel
    ;

// <case statement>
caseStatement
    : simpleCaseStatement
    | searchedCaseStatement
    ;

// <simple case statement>
simpleCaseStatement
    : T__151 simpleCaseOperand1 simpleCaseStatementWhenClause (caseStatementElseClause)? T__152 T__151
    ;

// <simple case operand 1>
simpleCaseOperand1
    : betweenPredicate
    ;

// <simple case statement when clause>
simpleCaseStatementWhenClause
    : T__153 simpleCaseOperand2 T__154 sqlStatementList
    ;

// <simple case operand 2>
simpleCaseOperand2
    : betweenPredicate
    ;

// <case statement else clause>
caseStatementElseClause
    : T__155 sqlStatementList
    ;

// <searched case statement>
searchedCaseStatement
    : T__151 searchedCaseStatementWhenClause (caseStatementElseClause)? T__152 T__151
    ;

// <searched case statement when clause>
searchedCaseStatementWhenClause
    : T__153 searchCondition T__154 sqlStatementList
    ;

// <if statement>
ifStatement
    : T__346 searchCondition ifStatementThenClause (ifStatementElseifClause)? (ifStatementElseClause)? T__152 T__346
    ;

// <if statement then clause>
ifStatementThenClause
    : T__154 sqlStatementList
    ;

// <if statement elseif clause>
ifStatementElseifClause
    : T__347 searchCondition T__154 sqlStatementList
    ;

// <if statement else clause>
ifStatementElseClause
    : T__155 sqlStatementList
    ;

// <iterate statement>
iterateStatement
    : T__348 statementLabel
    ;

// <leave statement>
leaveStatement
    : T__349 statementLabel
    ;

// <loop statement>
loopStatement
    : (beginningLabel T__90)? T__350 sqlStatementList T__152 T__350 (endingLabel)?
    ;

// <while statement>
whileStatement
    : (beginningLabel T__90)? T__351 searchCondition T__352 sqlStatementList T__152 T__351 (endingLabel)?
    ;

// <repeat statement>
repeatStatement
    : (beginningLabel T__90)? T__353 sqlStatementList T__354 searchCondition T__152 T__353 (endingLabel)?
    ;

// <for statement>
forStatement
    : (beginningLabel T__90)? T__14 forLoopVariableName T__121 (IDENTIFIER (cursorSensitivity)? T__202 T__14)? cursorSpecification T__352 sqlStatementList T__152 T__14 (endingLabel)?
    ;

// <for loop variable name>
forLoopVariableName
    : IDENTIFIER
    ;

// <signal statement>
signalStatement
    : T__355 signalValue (setSignalInformation)?
    ;

// <signal value>
signalValue
    : conditionName
    | sqlstateValue
    ;

// <set signal information>
setSignalInformation
    : T__25 signalInformationItemList
    ;

// <signal information item list>
signalInformationItemList
    : signalInformationItem (COMMA signalInformationItem)*
    ;

// <signal information item>
signalInformationItem
    : conditionInformationItemName T__181 simpleValueSpecification
    ;

// <resignal statement>
resignalStatement
    : T__356 (signalValue)? (setSignalInformation)?
    ;

// <SQL-server module definition>
sQL_serverModuleDefinition
    : T__214 T__23 sQL_serverModuleName (sQL_serverModuleCharacterSetSpecification)? (sQL_serverModuleSchemaClause)? (sQL_serverModulePathSpecification)? (temporaryTableDeclaration)? sQL_serverModuleContents T__152 T__23
    ;

// <SQL-server module name>
sQL_serverModuleName
    : schemaQualifiedName
    ;

// <SQL-server module character set specification>
sQL_serverModuleCharacterSetSpecification
    : T__0 T__1 characterSetSpecification
    ;

// <SQL-server module schema clause>
sQL_serverModuleSchemaClause
    : T__215 defaultSchemaName
    ;

// <default schema name>
defaultSchemaName
    : schemaName
    ;

// <SQL-server module path specification>
sQL_serverModulePathSpecification
    : pathSpecification
    ;

// <SQL-server module contents>
sQL_serverModuleContents
    : sQL_invokedRoutine SEMI
    ;

// <module routine>
moduleRoutine
    : moduleProcedure
    | moduleFunction
    ;

// <module procedure>
moduleProcedure
    : (T__16)? sQL_invokedProcedure
    ;

// <module function>
moduleFunction
    : (T__16)? sQL_invokedFunction
    ;

// <drop module statement>
dropModuleStatement
    : T__287 T__23 sQL_serverModuleName dropBehavior
    ;

// <triggered SQL statement>
triggeredSqlStatement
    : sqlProcedureStatement
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
    | setDescriptorStatement
    | getDescriptorStatement
    ;

// <allocate descriptor statement>
allocateDescriptorStatement
    : T__357 (T__10)? T__358 IDENTIFIER (T__54 T__111 occurrences)?
    ;

// <scope option>
scopeOption
    : T__216
    | T__17
    ;

// <embedded variable name>
embeddedVariableName
    : T__90 hostIdentifier
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

// <occurrences>
occurrences
    : simpleValueSpecification
    ;

// <deallocate descriptor statement>
deallocateDescriptorStatement
    : T__359 (T__10)? T__358 IDENTIFIER
    ;

// <set descriptor statement>
setDescriptorStatement
    : T__25 (T__10)? T__358 IDENTIFIER setDescriptorInformation
    ;

// <set descriptor information>
setDescriptorInformation
    : setHeaderInformation (COMMA setHeaderInformation)*
    | T__106 itemNumber setItemInformation (COMMA setItemInformation)*
    ;

// <set header information>
setHeaderInformation
    : headerItemName T__181 simpleValueSpecification1
    ;

// <header item name>
headerItemName
    : T__109
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
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
    : descriptorItemName T__181 simpleValueSpecification2
    ;

// <descriptor item name>
descriptorItemName
    : T__179
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__257
    | IDENTIFIER
    | IDENTIFIER
    | T__360
    | T__107
    | IDENTIFIER
    | T__361
    | T__307
    | T__264
    | T__362
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__49
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__363
    | IDENTIFIER
    | IDENTIFIER
    | IDENTIFIER
    | T__15
    | T__364
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
    : T__222 (T__10)? T__358 IDENTIFIER getDescriptorInformation
    ;

// <get descriptor information>
getDescriptorInformation
    : getHeaderInformation (COMMA getHeaderInformation)*
    | T__106 itemNumber getItemInformation (COMMA getItemInformation)*
    ;

// <get header information>
getHeaderInformation
    : simpleTargetSpecification1 T__181 headerItemName
    ;

// <simple target specification 1>
simpleTargetSpecification1
    : simpleTargetSpecification
    ;

// <get item information>
getItemInformation
    : simpleTargetSpecification2 T__181 descriptorItemName
    ;

// <simple target specification 2>
simpleTargetSpecification2
    : simpleTargetSpecification
    ;

// <prepare statement>
prepareStatement
    : T__365 sqlStatementName T__133 sqlStatementVariable
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
    : T__359 T__365 sqlStatementName
    ;

// <describe statement>
describeStatement
    : describeInputStatement
    | describeOutputStatement
    ;

// <describe input statement>
describeInputStatement
    : T__366 T__259 sqlStatementName usingDescriptor (nestingOption)?
    ;

// <using descriptor>
usingDescriptor
    : T__130 (T__10)? T__358 IDENTIFIER
    ;

// <nesting option>
nestingOption
    : T__54 T__367
    | T__56 T__367
    ;

// <describe output statement>
describeOutputStatement
    : T__366 (T__368)? describedObject usingDescriptor (nestingOption)?
    ;

// <described object>
describedObject
    : sqlStatementName
    | T__202 IDENTIFIER T__369
    ;

// <execute statement>
executeStatement
    : T__276 sqlStatementName (resultUsingClause)? (parameterUsingClause)?
    ;

// <result using clause>
resultUsingClause
    : outputUsingClause
    ;

// <output using clause>
outputUsingClause
    : intoArguments
    | intoDescriptor
    ;

// <into arguments>
intoArguments
    : T__295 intoArgument (COMMA intoArgument)*
    ;

// <into argument>
intoArgument
    : targetSpecification
    ;

// <dynamic parameter specification>
dynamicParameterSpecification
    : T__81
    ;

// <embedded variable specification>
embeddedVariableSpecification
    : embeddedVariableName (indicatorVariable)?
    ;

// <indicator variable>
indicatorVariable
    : (T__107)? embeddedVariableName
    ;

// <into descriptor>
intoDescriptor
    : T__295 (T__10)? T__358 IDENTIFIER
    ;

// <parameter using clause>
parameterUsingClause
    : inputUsingClause
    ;

// <input using clause>
inputUsingClause
    : usingArguments
    | usingInputDescriptor
    ;

// <using arguments>
usingArguments
    : T__130 usingArgument (COMMA usingArgument)*
    ;

// <using argument>
usingArgument
    : generalValueSpecification
    ;

// <using input descriptor>
usingInputDescriptor
    : usingDescriptor
    ;

// <execute immediate statement>
executeImmediateStatement
    : T__276 T__195 sqlStatementVariable
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

// <allocate cursor statement>
allocateCursorStatement
    : T__357 IDENTIFIER cursorIntent
    ;

// <cursor intent>
cursorIntent
    : statementCursor
    | resultSetCursor
    ;

// <statement cursor>
statementCursor
    : (cursorSensitivity)? (T__206)? T__202 (T__54 T__207)? (T__54 T__208)? T__14 IDENTIFIER
    ;

// <result set cursor>
resultSetCursor
    : T__14 T__211 specificRoutineDesignator
    ;

// <dynamic open statement>
dynamicOpenStatement
    : T__293 dynamicCursorName (inputUsingClause)?
    ;

// <dynamic cursor name>
dynamicCursorName
    : IDENTIFIER
    | IDENTIFIER
    ;

// <dynamic fetch statement>
dynamicFetchStatement
    : T__294 ((fetchOrientation)? T__133)? dynamicCursorName outputUsingClause
    ;

// <dynamic close statement>
dynamicCloseStatement
    : T__300 dynamicCursorName
    ;

// <dynamic delete statement: positioned>
dynamicDeleteStatement_Positioned
    : T__69 T__133 targetTable T__142 T__302 T__191 dynamicCursorName
    ;

// <dynamic update statement: positioned>
dynamicUpdateStatement_Positioned
    : T__104 targetTable T__25 setClauseList T__142 T__302 T__191 dynamicCursorName
    ;

// <double period>
doublePeriod
    : DOT DOT
    ;

// <direct SQL statement>
directSqlStatement
    : directlyExecutableStatement
    ;

// <directly executable statement>
directlyExecutableStatement
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
    : queryExpression (orderByClause)?
    ;

// <set catalog statement>
setCatalogStatement
    : T__25 catalogNameCharacteristic
    ;

// <catalog name characteristic>
catalogNameCharacteristic
    : T__370 valueSpecification
    ;

// <set schema statement>
setSchemaStatement
    : T__25 schemaNameCharacteristic
    ;

// <schema name characteristic>
schemaNameCharacteristic
    : T__215 valueSpecification
    ;

// <set names statement>
setNamesStatement
    : T__25 characterSetNameCharacteristic
    ;

// <character set name characteristic>
characterSetNameCharacteristic
    : T__0 valueSpecification
    ;

// <set path statement>
setPathStatement
    : T__25 sQL_pathCharacteristic
    ;

// <SQL-path characteristic>
sQL_pathCharacteristic
    : T__11 valueSpecification
    ;

// <set transform group statement>
setTransformGroupStatement
    : T__25 transformGroupCharacteristic
    ;

// <transform group characteristic>
transformGroupCharacteristic
    : T__72 T__12 T__13 valueSpecification
    | T__12 T__13 T__14 T__15 user_definedType valueSpecification
    ;

// <embedded SQL declare section>
embeddedSqlDeclareSection
    : embeddedSqlBeginDeclare (embeddedCharacterSetDeclaration)? (hostVariableDefinition)? embeddedSqlEndDeclare
    | embeddedSqlMUMPSDeclare
    ;

// <embedded SQL begin declare>
embeddedSqlBeginDeclare
    : sqlPrefix T__336 T__16 T__371 (sqlTerminator)?
    ;

// <SQL prefix>
sqlPrefix
    : T__372 T__10
    | T__373 T__10 LEFT_PAREN
    ;

// <SQL terminator>
sqlTerminator
    : IDENTIFIER
    | SEMI
    | RIGHT_PAREN
    ;

// <embedded character set declaration>
embeddedCharacterSetDeclaration
    : T__10 T__0 T__1 characterSetSpecification
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

// <Ada variable definition>
adaVariableDefinition
    : adaHostIdentifier (COMMA adaHostIdentifier)* T__90 adaTypeSpecification (adaInitialValue)?
    ;

// <Ada type specification>
adaTypeSpecification
    : adaQualifiedTypeSpecification
    | adaUnqualifiedTypeSpecification
    | adaDerivedTypeSpecification
    ;

// <Ada qualified type specification>
adaQualifiedTypeSpecification
    : IDENTIFIER DOT T__26 (T__24 T__25 (T__190)? characterSetSpecification)? LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | IDENTIFIER DOT T__374 LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | IDENTIFIER DOT T__45
    | IDENTIFIER DOT T__44
    | IDENTIFIER DOT T__47
    | IDENTIFIER DOT IDENTIFIER
    | IDENTIFIER DOT T__50
    | IDENTIFIER DOT IDENTIFIER
    | IDENTIFIER DOT IDENTIFIER
    ;

// <Ada unqualified type specification>
adaUnqualifiedTypeSpecification
    : T__26 LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | T__374 LEFT_PAREN NUMBER doublePeriod length RIGHT_PAREN
    | T__45
    | T__44
    | T__47
    | IDENTIFIER
    | T__50
    | IDENTIFIER
    | IDENTIFIER
    ;

// <Ada derived type specification>
adaDerivedTypeSpecification
    : adaCLOBVariable
    | adaBLOBVariable
    | adaUser_definedTypeVariable
    | adaCLOBLocatorVariable
    | adaBLOBLocatorVariable
    | adaUser_definedTypeLocatorVariable
    | adaArrayLocatorVariable
    | adaREFVariable
    ;

// <Ada CLOB variable>
adaCLOBVariable
    : T__10 T__15 T__190 T__31 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)?
    ;

// <Ada BLOB variable>
adaBLOBVariable
    : T__10 T__15 T__190 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <Ada user-defined type variable>
adaUser_definedTypeVariable
    : T__10 T__15 T__190 user_definedType T__121 predefinedType
    ;

// <Ada CLOB locator variable>
adaCLOBLocatorVariable
    : T__10 T__15 T__190 T__31 T__121 T__212
    ;

// <Ada BLOB locator variable>
adaBLOBLocatorVariable
    : T__10 T__15 T__190 T__39 T__121 T__212
    ;

// <Ada user-defined type locator variable>
adaUser_definedTypeLocatorVariable
    : T__10 T__15 T__190 IDENTIFIER T__121 T__212
    ;

// <Ada array locator variable>
adaArrayLocatorVariable
    : T__10 T__15 T__190 collectionType T__121 T__212
    ;

// <Ada REF variable>
adaREFVariable
    : T__10 T__15 T__190 referenceType
    ;

// <Ada initial value>
adaInitialValue
    : adaAssignmentOperator STRING
    ;

// <Ada assignment operator>
adaAssignmentOperator
    : T__90 T__181
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
    : T__181 STRING
    ;

// <C character variable>
cCharacterVariable
    : cCharacterType (T__24 T__25 (T__190)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? (COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*
    ;

// <C character type>
cCharacterType
    : IDENTIFIER
    | IDENTIFIER IDENTIFIER
    | IDENTIFIER IDENTIFIER
    ;

// <C array specification>
cArraySpecification
    : T__80 length T__82
    ;

// <C derived variable>
cDerivedVariable
    : cVARCHARVariable
    | cNCHARVariable
    | cNCHARVARYINGVariable
    | cCLOBVariable
    | cNCLOBVariable
    | cBLOBVariable
    | cBitVariable
    | cUser_definedTypeVariable
    | cCLOBLocatorVariable
    | cBLOBLocatorVariable
    | cArrayLocatorVariable
    | cUser_definedTypeLocatorVariable
    | cREFVariable
    ;

// <C VARCHAR variable>
cVARCHARVariable
    : T__28 (T__24 T__25 (T__190)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? (COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*
    ;

// <C NCHAR variable>
cNCHARVariable
    : T__36 (T__24 T__25 (T__190)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? (COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*
    ;

// <C NCHAR VARYING variable>
cNCHARVARYINGVariable
    : T__36 T__27 (T__24 T__25 (T__190)? characterSetSpecification)? cHostIdentifier cArraySpecification (cInitialValue)? (COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*
    ;

// <C CLOB variable>
cCLOBVariable
    : T__10 T__15 T__190 T__31 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)? cHostIdentifier (cInitialValue)? (COMMA cHostIdentifier (cInitialValue)?)*
    ;

// <C NCLOB variable>
cNCLOBVariable
    : T__10 T__15 T__190 T__37 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)? cHostIdentifier (cInitialValue)? (COMMA cHostIdentifier (cInitialValue)?)*
    ;

// <C BLOB variable>
cBLOBVariable
    : T__10 T__15 T__190 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN cHostIdentifier (cInitialValue)? (COMMA cHostIdentifier (cInitialValue)?)*
    ;

// <C bit variable>
cBitVariable
    : T__374 cHostIdentifier cArraySpecification (cInitialValue)? (COMMA cHostIdentifier cArraySpecification (cInitialValue)?)*
    ;

// <C user-defined type variable>
cUser_definedTypeVariable
    : T__10 T__15 T__190 user_definedType T__121 predefinedType cHostIdentifier (cInitialValue)? (COMMA cHostIdentifier (cInitialValue)?)*
    ;

// <C CLOB locator variable>
cCLOBLocatorVariable
    : T__10 T__15 T__190 T__31 T__121 T__212 cHostIdentifier (cInitialValue)? (COMMA cHostIdentifier (cInitialValue)?)*
    ;

// <C BLOB locator variable>
cBLOBLocatorVariable
    : T__10 T__15 T__190 T__39 T__121 T__212 cHostIdentifier (cInitialValue)? (COMMA cHostIdentifier (cInitialValue)?)*
    ;

// <C array locator variable>
cArrayLocatorVariable
    : T__10 T__15 T__190 collectionType T__121 T__212 cHostIdentifier (cInitialValue)? (COMMA cHostIdentifier (cInitialValue)?)*
    ;

// <C user-defined type locator variable>
cUser_definedTypeLocatorVariable
    : T__10 T__15 T__190 user_definedType T__121 T__212 cHostIdentifier (cInitialValue)? (COMMA cHostIdentifier (cInitialValue)?)*
    ;

// <C REF variable>
cREFVariable
    : T__10 T__15 T__190 referenceType
    ;

// <COBOL variable definition>
cOBOLVariableDefinition
    : (NUMBER | NUMBER) cOBOLHostIdentifier cOBOLTypeSpecification (STRING)? DOT
    ;

// <COBOL type specification>
cOBOLTypeSpecification
    : cOBOLCharacterType
    | cOBOLNationalCharacterType
    | cOBOLBitType
    | cOBOLNumericType
    | cOBOLIntegerType
    | cOBOLDerivedTypeSpecification
    ;

// <COBOL character type>
cOBOLCharacterType
    : (T__24 T__25 (T__190)? characterSetSpecification)? (T__375 | T__376) (T__190)? (T__89 (LEFT_PAREN length RIGHT_PAREN)?)*
    ;

// <COBOL national character type>
cOBOLNationalCharacterType
    : (T__24 T__25 (T__190)? characterSetSpecification)? (T__375 | T__376) (T__190)? (T__377 (LEFT_PAREN length RIGHT_PAREN)?)*
    ;

// <COBOL bit type>
cOBOLBitType
    : (T__375 | T__376) (T__190)? (T__89 (LEFT_PAREN length RIGHT_PAREN)?)* T__275 (T__190)? T__374
    ;

// <COBOL numeric type>
cOBOLNumericType
    : (T__375 | T__376) (T__190)? T__378 cOBOLNinesSpecification (T__275 (T__190)?)? T__379 T__380 T__170 T__381
    ;

// <COBOL nines specification>
cOBOLNinesSpecification
    : cOBOLNines (T__382 (cOBOLNines)?)?
    | T__382 cOBOLNines
    ;

// <COBOL nines>
cOBOLNines
    : (NUMBER (LEFT_PAREN length RIGHT_PAREN)?)+
    ;

// <COBOL integer type>
cOBOLIntegerType
    : cOBOLBinaryInteger
    ;

// <COBOL binary integer>
cOBOLBinaryInteger
    : (T__375 | T__376) (T__190)? T__378 cOBOLNines (T__275 (T__190)?)? T__38
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
    | cOBOLUser_definedTypeLocatorVariable
    | cOBOLREFVariable
    ;

// <COBOL CLOB variable>
cOBOLCLOBVariable
    : (T__275 (T__190)?)? T__10 T__15 T__190 T__31 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)?
    ;

// <COBOL NCLOB variable>
cOBOLNCLOBVariable
    : (T__275 (T__190)?)? T__10 T__15 T__190 T__37 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)?
    ;

// <COBOL BLOB variable>
cOBOLBLOBVariable
    : (T__275 (T__190)?)? T__10 T__15 T__190 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <COBOL user-defined type variable>
cOBOLUser_definedTypeVariable
    : (T__275 (T__190)?)? T__10 T__15 T__190 user_definedType T__121 predefinedType
    ;

// <COBOL CLOB locator variable>
cOBOLCLOBLocatorVariable
    : (T__275 (T__190)?)? T__10 T__15 T__190 T__31 T__121 T__212
    ;

// <COBOL BLOB locator variable>
cOBOLBLOBLocatorVariable
    : (T__275 (T__190)?)? T__10 T__15 T__190 T__39 T__121 T__212
    ;

// <COBOL array locator variable>
cOBOLArrayLocatorVariable
    : (T__275 (T__190)?)? T__10 T__15 T__190 collectionType T__121 T__212
    ;

// <COBOL user-defined type locator variable>
cOBOLUser_definedTypeLocatorVariable
    : (T__275 (T__190)?)? T__10 T__15 T__190 IDENTIFIER T__121 T__212
    ;

// <COBOL REF variable>
cOBOLREFVariable
    : (T__275 (T__190)?)? T__10 T__15 T__190 referenceType
    ;

// <Fortran variable definition>
fortranVariableDefinition
    : fortranTypeSpecification fortranHostIdentifier (COMMA fortranHostIdentifier)*
    ;

// <Fortran type specification>
fortranTypeSpecification
    : T__24 (T__88 length)? (T__24 T__25 (T__190)? characterSetSpecification)?
    | T__24 T__383 T__181 NUMBER (T__88 length)? (T__24 T__25 (T__190)? characterSetSpecification)?
    | T__374 (T__88 length)?
    | T__43
    | T__47
    | T__48 T__49
    | T__384
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
    | fortranREFVariable
    ;

// <Fortran CLOB variable>
fortranCLOBVariable
    : T__10 T__15 T__190 T__31 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)?
    ;

// <Fortran BLOB variable>
fortranBLOBVariable
    : T__10 T__15 T__190 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <Fortran user-defined type variable>
fortranUser_definedTypeVariable
    : T__10 T__15 T__190 user_definedType T__121 predefinedType
    ;

// <Fortran CLOB locator variable>
fortranCLOBLocatorVariable
    : T__10 T__15 T__190 T__31 T__121 T__212
    ;

// <Fortran BLOB locator variable>
fortranBLOBLocatorVariable
    : T__10 T__15 T__190 T__39 T__121 T__212
    ;

// <Fortran user-defined type locator variable>
fortranUser_definedTypeLocatorVariable
    : T__10 T__15 T__190 IDENTIFIER T__121 T__212
    ;

// <Fortran array locator variable>
fortranArrayLocatorVariable
    : T__10 T__15 T__190 collectionType T__121 T__212
    ;

// <Fortran REF variable>
fortranREFVariable
    : T__10 T__15 T__190 referenceType
    ;

// <MUMPS variable definition>
mUMPSVariableDefinition
    : mUMPSNumericVariable SEMI
    | mUMPSCharacterVariable SEMI
    | mUMPSDerivedTypeSpecification SEMI
    ;

// <MUMPS numeric variable>
mUMPSNumericVariable
    : mUMPSTypeSpecification mUMPSHostIdentifier (COMMA mUMPSHostIdentifier)*
    ;

// <MUMPS type specification>
mUMPSTypeSpecification
    : T__44
    | T__42 (LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN)?
    | T__47
    ;

// <MUMPS character variable>
mUMPSCharacterVariable
    : T__28 mUMPSHostIdentifier mUMPSLengthSpecification (COMMA mUMPSHostIdentifier mUMPSLengthSpecification)*
    ;

// <MUMPS length specification>
mUMPSLengthSpecification
    : LEFT_PAREN length RIGHT_PAREN
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
    | mUMPSREFVariable
    ;

// <MUMPS CLOB variable>
mUMPSCLOBVariable
    : T__10 T__15 T__190 T__31 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)?
    ;

// <MUMPS BLOB variable>
mUMPSBLOBVariable
    : T__10 T__15 T__190 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <MUMPS user-defined type variable>
mUMPSUser_definedTypeVariable
    : T__10 T__15 T__190 user_definedType T__121 predefinedType
    ;

// <MUMPS CLOB locator variable>
mUMPSCLOBLocatorVariable
    : T__10 T__15 T__190 T__31 T__121 T__212
    ;

// <MUMPS BLOB locator variable>
mUMPSBLOBLocatorVariable
    : T__10 T__15 T__190 T__39 T__121 T__212
    ;

// <MUMPS user-defined type locator variable>
mUMPSUser_definedTypeLocatorVariable
    : T__10 T__15 T__190 IDENTIFIER T__121 T__212
    ;

// <MUMPS array locator variable>
mUMPSArrayLocatorVariable
    : T__10 T__15 T__190 collectionType T__121 T__212
    ;

// <MUMPS REF variable>
mUMPSREFVariable
    : T__10 T__15 T__190 referenceType
    ;

// <Pascal variable definition>
pascalVariableDefinition
    : pascalHostIdentifier (COMMA pascalHostIdentifier)* T__90 pascalTypeSpecification SEMI
    ;

// <Pascal type specification>
pascalTypeSpecification
    : T__385 T__79 T__80 NUMBER doublePeriod length T__82 T__191 T__26 (T__24 T__25 (T__190)? characterSetSpecification)?
    | T__385 T__79 T__80 NUMBER doublePeriod length T__82 T__191 T__374
    | T__43
    | T__47
    | T__26 (T__24 T__25 (T__190)? characterSetSpecification)?
    | T__374
    | T__50
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
    | pascalREFVariable
    ;

// <Pascal CLOB variable>
pascalCLOBVariable
    : T__10 T__15 T__190 T__31 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)?
    ;

// <Pascal BLOB variable>
pascalBLOBVariable
    : T__10 T__15 T__190 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <Pascal user-defined type variable>
pascalUser_definedTypeVariable
    : T__10 T__15 T__190 user_definedType T__121 predefinedType
    ;

// <Pascal CLOB locator variable>
pascalCLOBLocatorVariable
    : T__10 T__15 T__190 T__31 T__121 T__212
    ;

// <Pascal BLOB locator variable>
pascalBLOBLocatorVariable
    : T__10 T__15 T__190 T__39 T__121 T__212
    ;

// <Pascal user-defined type locator variable>
pascalUser_definedTypeLocatorVariable
    : T__10 T__15 T__190 IDENTIFIER T__121 T__212
    ;

// <Pascal array locator variable>
pascalArrayLocatorVariable
    : T__10 T__15 T__190 collectionType T__121 T__212
    ;

// <Pascal REF variable>
pascalREFVariable
    : T__10 T__15 T__190 referenceType
    ;

// <PL/I variable definition>
pLIVariableDefinition
    : (T__386 | T__16) (pLIHostIdentifier | LEFT_PAREN pLIHostIdentifier (COMMA pLIHostIdentifier)* RIGHT_PAREN) pLITypeSpecification (STRING)? SEMI
    ;

// <PL/I type specification>
pLITypeSpecification
    : (T__26 | T__24) (T__27)? LEFT_PAREN length RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)?
    | T__374 (T__27)? LEFT_PAREN length RIGHT_PAREN
    | pLITypeFixedDecimal LEFT_PAREN precision (COMMA scale)? RIGHT_PAREN
    | pLITypeFixedBinary (LEFT_PAREN precision RIGHT_PAREN)?
    | pLITypeFloatBinary LEFT_PAREN precision RIGHT_PAREN
    | pLIDerivedTypeSpecification
    ;

// <PL/I type fixed decimal>
pLITypeFixedDecimal
    : (T__42 | T__41) T__387
    | T__387 (T__42 | T__41)
    ;

// <PL/I type fixed binary>
pLITypeFixedBinary
    : (T__388 | T__38) T__387
    | T__387 (T__388 | T__38)
    ;

// <PL/I type float binary>
pLITypeFloatBinary
    : (T__388 | T__38) T__46
    | T__46 (T__388 | T__38)
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
    | pLIREFVariable
    ;

// <PL/I CLOB variable>
pLICLOBVariable
    : T__10 T__15 T__190 T__31 LEFT_PAREN largeObjectLength RIGHT_PAREN (T__24 T__25 (T__190)? characterSetSpecification)?
    ;

// <PL/I BLOB variable>
pLIBLOBVariable
    : T__10 T__15 T__190 T__39 LEFT_PAREN largeObjectLength RIGHT_PAREN
    ;

// <PL/I user-defined type variable>
pLIUser_definedTypeVariable
    : T__10 T__15 T__190 user_definedType T__121 predefinedType
    ;

// <PL/I CLOB locator variable>
pLICLOBLocatorVariable
    : T__10 T__15 T__190 T__31 T__121 T__212
    ;

// <PL/I BLOB locator variable>
pLIBLOBLocatorVariable
    : T__10 T__15 T__190 T__39 T__121 T__212
    ;

// <PL/I user-defined type locator variable>
pLIUser_definedTypeLocatorVariable
    : T__10 T__15 T__190 IDENTIFIER T__121 T__212
    ;

// <PL/I array locator variable>
pLIArrayLocatorVariable
    : T__10 T__15 T__190 collectionType T__121 T__212
    ;

// <PL/I REF variable>
pLIREFVariable
    : T__10 T__15 T__190 referenceType
    ;

// <embedded SQL end declare>
embeddedSqlEndDeclare
    : sqlPrefix T__152 T__16 T__371 (sqlTerminator)?
    ;

// <embedded SQL MUMPS declare>
embeddedSqlMUMPSDeclare
    : sqlPrefix T__336 T__16 T__371 (embeddedCharacterSetDeclaration)? (hostVariableDefinition)? T__152 T__16 T__371 sqlTerminator
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
    | embeddedExceptionDeclaration
    | handlerDeclaration
    | sQL_invokedRoutine
    | sqlProcedureStatement
    ;

// <dynamic declare cursor>
dynamicDeclareCursor
    : T__16 IDENTIFIER (cursorSensitivity)? (cursorScrollability)? T__202 (cursorHoldability)? (cursorReturnability)? T__14 IDENTIFIER
    ;

// <embedded authorization declaration>
embeddedAuthorizationDeclaration
    : T__16 embeddedAuthorizationClause
    ;

// <embedded authorization clause>
embeddedAuthorizationClause
    : T__215 schemaName
    | T__326 embeddedAuthorizationIdentifier (T__14 T__231 (T__137 | T__319 T__262))?
    | T__215 schemaName T__326 embeddedAuthorizationIdentifier (T__14 T__231 (T__137 | T__319 T__262))?
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

// <embedded exception declaration>
embeddedExceptionDeclaration
    : T__389 condition conditionAction
    ;

// <condition>
condition
    : sqlCondition
    ;

// <SQL condition>
sqlCondition
    : majorCategory
    | T__213 LEFT_PAREN sQLSTATEClassValue (COMMA sQLSTATESubclassValue)? RIGHT_PAREN
    | T__96 constraintName
    ;

// <major category>
majorCategory
    : T__343
    | T__344
    | T__67 T__345
    ;

// <SQLSTATE class value>
sQLSTATEClassValue
    : sQLSTATEChar sQLSTATEChar
    ;

// <SQLSTATE char>
sQLSTATEChar
    : simpleLatinUpperCaseLetter
    | NUMBER
    ;

// <SQLSTATE subclass value>
sQLSTATESubclassValue
    : sQLSTATEChar sQLSTATEChar sQLSTATEChar
    ;

// <condition action>
conditionAction
    : T__340
    | goTo
    ;

// <go to>
goTo
    : (T__390 | T__391 T__58) gotoTarget
    ;

// <goto target>
gotoTarget
    : hostLabelIdentifier
    | NUMBER
    | hostPLILabelVariable
    ;

// <interval primary>
intervalPrimary
    : arrayConcatenation (intervalQualifier)?
    | intervalValueFunction
    ;

// <module authorization clause>
moduleAuthorizationClause
    : T__215 schemaName
    | T__326 moduleAuthorizationIdentifier (T__14 T__231 (T__137 | T__319 T__262))?
    | T__215 schemaName T__326 moduleAuthorizationIdentifier (T__14 T__231 (T__137 | T__319 T__262))?
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
    : T__69 (T__133 targetTable)? T__142 T__302 T__191 (scopeOption)? IDENTIFIER
    ;

// <preparable dynamic update statement: positioned>
preparableDynamicUpdateStatement_Positioned
    : T__104 (targetTable)? T__25 setClauseList T__142 T__302 T__191 (scopeOption)? IDENTIFIER
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
hostPLILabelVariable
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
methodName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
savepointName
    : IDENTIFIER
    ;

non_joinQueryExpression
    : non_joinQueryTerm
    ;

// Auto-generated placeholder for undefined rule
equalsOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
extendedCursorName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
simpleLatinUpperCaseLetter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
characterRepresentation
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
nonquoteCharacter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
cOBOLHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
underscore
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
percent
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
pascalHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlCOBOLProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
semicolon
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
directImplementation_definedStatement
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
hostLabelIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
unsignedInteger
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
newline
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
verticalBar
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
characterSetName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitLengthExpression
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
intervalLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
user_definedTypeName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
nationalCharacterStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
queryName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
colon
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
quote
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
qualifiedIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
fieldName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
statementName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
preparableImplementation_definedStatement
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
moduleNameClause
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
concatenationOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
rightParen
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
partnYes
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
unsignedNumericLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
minusSign
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
lessThanOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
pLIHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
greaterThanOperator
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
implementation_definedCLIGenericName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
schemaNameClause
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
collationName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
attributeName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitStringType
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
questionMark
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
hexStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
comma
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
adaHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
escapedCharacter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlAdaProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
rightBracket
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
characterStringLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
asterisk
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
leftBracket
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlFortranProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlMUMPSProgram
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
leftParen
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitValueExpression
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
fortranHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
datetimeLiteral
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
circumflex
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
ampersand
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
cHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
extendedStatementName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
delimitedIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlPLIProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
translationName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
routineName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
roleName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
connectionName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
regularIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
queryExpressionBody
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
identifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
embeddedSqlCProgram
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
plusSign
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
period
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
digit
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
non_escapedCharacter
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
mUMPSHostIdentifier
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
cLIParameterName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
descriptorName
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
solidus
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
bitValueFunction
    : IDENTIFIER
    ;

// Auto-generated placeholder for undefined rule
specificName
    : IDENTIFIER
    ;

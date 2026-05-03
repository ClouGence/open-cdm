/**
 * @deprecated will be removed in future.
 */
export var Legacy_TokenType;
(function (Legacy_TokenType) {
    /**
     * Enclosed in single/double/back quotation, `` Symbol
     * 'abc', "abc", `abc`
     */
    Legacy_TokenType["SingleQuotation"] = "SingleQuotation";
    Legacy_TokenType["DoubleQuotation"] = "DoubleQuotation";
    Legacy_TokenType["BackQuotation"] = "BackQuotation";
    /**
     * Language element type
     */
    Legacy_TokenType["Comment"] = "Comment";
    /**
     * Statement
     */
    Legacy_TokenType["StatementTerminator"] = "StatementTerminator";
    /**
     * Others
     */
    Legacy_TokenType["Error"] = "Error";
    /**
     * Left small Bracket
     */
    Legacy_TokenType["LeftSmallBracket"] = "LeftSmallBracket";
    /**
     * Left small Bracket
     */
    Legacy_TokenType["RightSmallBracket"] = "RightSmallBracket";
    Legacy_TokenType["Comma"] = "Comma";
    Legacy_TokenType["FunctionArguments"] = "FunctionArguments";
})(Legacy_TokenType || (Legacy_TokenType = {}));
/**
 * @deprecated will be removed in future.
 * Token recognition rules
 */
export const Legacy_TokenReg = {
    [Legacy_TokenType.StatementTerminator]: /[;]/,
    [Legacy_TokenType.SingleQuotation]: /['|\']/,
    [Legacy_TokenType.DoubleQuotation]: /["]/,
    [Legacy_TokenType.BackQuotation]: /[`]/,
    [Legacy_TokenType.LeftSmallBracket]: /[(]/,
    [Legacy_TokenType.RightSmallBracket]: /[)]/,
    [Legacy_TokenType.Comma]: /[,]/,
};

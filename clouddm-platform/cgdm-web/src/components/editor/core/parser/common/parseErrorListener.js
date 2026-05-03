export default class ParseErrorListener {
    constructor(errorListener) {
        this._errorListener = errorListener;
    }
    syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e) {
        let endCol = charPositionInLine + 1;
        if (offendingSymbol && offendingSymbol.text !== null) {
            endCol = charPositionInLine + offendingSymbol.text.length;
        }
        if (this._errorListener) {
            this._errorListener({
                startLine: line,
                endLine: line,
                startColumn: charPositionInLine + 1,
                endColumn: endCol + 1,
                message: msg,
            }, {
                e,
                line,
                msg,
                recognizer,
                offendingSymbol,
                charPositionInLine,
            });
        }
    }
}

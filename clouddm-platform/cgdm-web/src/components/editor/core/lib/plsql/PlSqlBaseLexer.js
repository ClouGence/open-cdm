import { Lexer } from "antlr4ts/Lexer";
export default class PlSqlBaseLexer extends Lexer {
    IsNewlineAtPos(pos) {
        const la = this._input.LA(pos);
        return la == -1 || String.fromCharCode(la) == '\n';
    }
}
//# sourceMappingURL=PlSqlBaseLexer.js.map
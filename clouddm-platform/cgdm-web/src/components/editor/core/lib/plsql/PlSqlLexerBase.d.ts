import { Lexer } from "antlr4ts/Lexer";
export default abstract class PlSqlLexerBase extends Lexer {
    self: PlSqlLexerBase;
    IsNewlineAtPos(pos: number): boolean;
}

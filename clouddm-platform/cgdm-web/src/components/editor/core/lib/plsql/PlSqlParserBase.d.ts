import { Parser } from "antlr4ts/Parser";
import { TokenStream } from "antlr4ts/TokenStream";
export default abstract class PlSqlParserBase extends Parser {
    _isVersion10: boolean;
    _isVersion12: boolean;
    self: PlSqlParserBase;
    constructor(input: TokenStream);
    isVersion10(): boolean;
    isVersion12(): boolean;
    setVersion10(value: boolean): void;
    setVersion12(value: boolean): void;
}

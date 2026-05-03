import { Parser } from "antlr4ts/Parser";
import { TokenStream } from "antlr4ts/TokenStream";
export default abstract class PlSqlBaseParser extends Parser {
    private _isVersion10;
    private _isVersion12;
    constructor(input: TokenStream);
    isVersion10(): boolean;
    isVersion12(): boolean;
    setVersion10(value: boolean): void;
    setVersion12(value: boolean): void;
}

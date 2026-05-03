import { Parser } from "antlr4ts/Parser";
export default class PlSqlBaseParser extends Parser {
    constructor(input) {
        super(input);
        this._isVersion10 = false;
        this._isVersion12 = true;
        this._isVersion10 = false;
        this._isVersion12 = true;
    }
    isVersion10() {
        return this._isVersion10;
    }
    isVersion12() {
        return this._isVersion12;
    }
    setVersion10(value) {
        this._isVersion10 = value;
    }
    setVersion12(value) {
        this._isVersion12 = value;
    }
}
//# sourceMappingURL=PlSqlBaseParser.js.map
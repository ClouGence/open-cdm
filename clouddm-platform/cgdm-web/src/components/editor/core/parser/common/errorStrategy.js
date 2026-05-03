import { DefaultErrorStrategy } from 'antlr4ts/DefaultErrorStrategy';
import { InputMismatchException } from 'antlr4ts/InputMismatchException';
import { IntervalSet } from 'antlr4ts/misc/IntervalSet';
/**
 * Base on DefaultErrorStrategy.
 * The difference is that it assigns exception to the context.exception when it encounters error.
 */
export class ErrorStrategy extends DefaultErrorStrategy {
    recover(recognizer, e) {
        // Mark the context as an anomaly
        for (let context = recognizer.context; context; context = context.parent) {
            context.exception = e;
        }
        // Error recovery
        if (this.lastErrorIndex === recognizer.inputStream.index &&
            this.lastErrorStates &&
            this.lastErrorStates.contains(recognizer.state)) {
            recognizer.consume();
        }
        this.lastErrorIndex = recognizer.inputStream.index;
        if (!this.lastErrorStates) {
            this.lastErrorStates = new IntervalSet();
        }
        this.lastErrorStates.add(recognizer.state);
        let followSet = this.getErrorRecoverySet(recognizer);
        this.consumeUntil(recognizer, followSet);
    }
    recoverInline(recognizer) {
        let e;
        if (this.nextTokensContext === undefined) {
            e = new InputMismatchException(recognizer);
        }
        else {
            e = new InputMismatchException(recognizer, this.nextTokensState, this.nextTokensContext);
        }
        // Mark the context as an anomaly
        for (let context = recognizer.context; context; context = context.parent) {
            context.exception = e;
        }
        // Error recovery
        let matchedSymbol = this.singleTokenDeletion(recognizer);
        if (matchedSymbol) {
            recognizer.consume();
            return matchedSymbol;
        }
        if (this.singleTokenInsertion(recognizer)) {
            return this.getMissingSymbol(recognizer);
        }
        throw e;
    }
}

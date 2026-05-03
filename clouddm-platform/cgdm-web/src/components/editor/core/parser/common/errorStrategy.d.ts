import { DefaultErrorStrategy } from 'antlr4ts/DefaultErrorStrategy';
import { Parser } from 'antlr4ts/Parser';
import { RecognitionException } from 'antlr4ts/RecognitionException';
import { Token } from 'antlr4ts/Token';
/**
 * Base on DefaultErrorStrategy.
 * The difference is that it assigns exception to the context.exception when it encounters error.
 */
export declare class ErrorStrategy extends DefaultErrorStrategy {
    recover(recognizer: Parser, e: RecognitionException): void;
    recoverInline(recognizer: Parser): Token;
}

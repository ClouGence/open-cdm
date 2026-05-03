import { Token } from 'antlr4ts';
import { CaretPosition } from '../parser/common/basic-parser-types';
/**
 * find token index via caret position (cursor position)
 * @param caretPosition
 * @param allTokens all the tokens
 * @returns caretTokenIndex
 */
export declare function findCaretTokenIndex(caretPosition: CaretPosition, allTokens: Token[]): number;

<script>
import appLogger from '@/utils/logger';
import * as monaco from 'monaco-editor';
import { markRaw } from 'vue';
import Toast from '@/utils/toast';

export default {
  name: 'JSONEditor',
  props: {
    text: {
      type: String,
      default: ''
    },
    language: {
      type: String,
      default: 'json'
    },
    border: {
      type: Number,
      default: 1
    }
  },
  watch: {
    text(newVal, oldVal) {
      if (newVal && newVal !== oldVal) {
        this.createEditor();
      }
    }
  },
  data() {
    return {
      monacoEditor: null,
      currentDecorations: [],
      hoverDecorations: [],
      parsedJson: null
    };
  },
  mounted() {
    this.createEditor();
  },
  computed: {
    borderStyle() {
      return this.border > 0 ? `${this.border}px solid #ccc` : 'none';
    }
  },
  methods: {
    createEditor() {
      if (this.text) {
        if (this.monacoEditor) {
          appLogger.debug('set', this.text);
          this.monacoEditor.getModel().setValue(this.text);
          // this.updateDecorations();
        } else {
          this.monacoEditor = markRaw(
            monaco.editor.create(this.$refs.jsonEditor, {
              value: this.text, // The editor 's value
              language: this.language,
              fontSize: 14,
              fontWeight: 'bold',
              scrollBeyondLastLine: false,
              readOnly: true,
              theme: 'vs', // Editor theme: vs, hc-black, or vs-dark; more options in the official docs.
              minimap: {
                enabled: false
              },
              automaticLayout: true,
              autoIndent: true, // Auto Indent
              glyphMargin: true // Enable glyph margin to display duplicate icons
            })
          );

          // Listen to mouse click events - Copy entire JSON
          this.monacoEditor.onMouseDown((e) => {
            if (e.target.type === monaco.editor.MouseTargetType.GUTTER_GLYPH_MARGIN) {
              const lineNumber = e.target.position?.lineNumber;
              if (lineNumber) {
                this.copyJsonAtLine(lineNumber);
              }
            }
          });
          //
          // // Listen to mouse moves - Detect suspension on JSON key
          // this.monacoEditor.onMouseMove((e) => {
          //   if (e.target.position) {
          //     this.handleMouseMove(e);
          //   }
          // });
          //
          // // Listen to click - copy key value
          // this.monacoEditor.onMouseDown((e) => {
          //   if (e.target.position && e.event.leftButton) {
          //     this.handleClickCopyValue(e.target.position);
          //   }
          // });
          //
          // // Show a copy icon for the first row of all JSON objects
          this.updateDecorations();
          //
          // // Parsing JSON
          // this.parseJson();
        }
      }
    },

    updateDecorations() {
      if (!this.monacoEditor) {
        return;
      }

      const model = this.monacoEditor.getModel();
      if (!model) {
        return;
      }

      const totalLines = model.getLineCount();
      const jsonStartLines = [];

      // Find the start line for all JSON objects
      for (let i = 1; i <= totalLines; i++) {
        const lineContent = model.getLineContent(i).trim();
        if (lineContent.startsWith('{')) {
          jsonStartLines.push(i);
        }
      }

      // Add a copy icon to the first row of each JSON object
      const newDecorations = jsonStartLines.map((lineNumber) => ({
        range: new monaco.Range(lineNumber, 1, lineNumber, 1),
        options: {
          glyphMarginClassName: 'json-copy-icon',
          glyphMarginHoverMessage: { value: '点击复制当前 JSON' }
        }
      }));

      this.currentDecorations = this.monacoEditor.deltaDecorations(this.currentDecorations, newDecorations);
    },

    copyJsonAtLine(lineNumber) {
      if (!this.monacoEditor) return;

      const model = this.monacoEditor.getModel();
      const totalLines = model.getLineCount();

      // Find lines for starting and ending the current JSON object
      const { startLine, endLine } = this.findJsonBoundaries(lineNumber, totalLines, model);

      if (startLine && endLine) {
        // Extract JSON text
        const jsonText = model.getValueInRange(new monaco.Range(startLine, 1, endLine, model.getLineMaxColumn(endLine)));

        // Copy to Clipboard
        this.copyToClipboard(jsonText);
      }
    },

    findJsonBoundaries(lineNumber, totalLines, model) {
      let startLine = lineNumber;
      let endLine = lineNumber;
      let braceCount = 0;
      let foundStart = false;

      // Find start of JSON object up {
      for (let i = lineNumber; i >= 1; i--) {
        const lineContent = model.getLineContent(i).trim();

        if (!foundStart && lineContent.startsWith('{')) {
          startLine = i;
          foundStart = true;
          break;
        }
      }

      // If you don't find the start, go straight back.
      if (!foundStart) {
        // Could be a single line, JSON, trying to copy the current line directly
        const lineContent = model.getLineContent(lineNumber).trim();
        if (lineContent.startsWith('{') && lineContent.endsWith('}')) {
          return { startLine: lineNumber, endLine: lineNumber };
        }
        return { startLine: null, endLine: null };
      }

      // Recalculate parenthesis matching from the start line
      braceCount = 0;
      for (let i = startLine; i <= totalLines; i++) {
        const lineContent = model.getLineContent(i);

        // Calculates the number of brackets in the current row
        for (let char of lineContent) {
          if (char === '{') braceCount++;
          else if (char === '}') braceCount--;

          if (braceCount === 0) {
            endLine = i;
            return { startLine, endLine };
          }
        }
      }

      // If no match is found, return to the end of the file
      return { startLine, endLine: totalLines };
    },

    parseJson() {
      try {
        // Try to parse all JSON objects throughout the text
        const model = this.monacoEditor.getModel();
        const fullText = model.getValue();
        const jsonObjects = [];

        // Split by Line and find JSON objects
        const lines = fullText.split('\n');
        let currentJson = '';
        let braceCount = 0;
        let startLine = 0;

        for (let i = 0; i < lines.length; i++) {
          const line = lines[i];

          for (let char of line) {
            if (char === '{') {
              if (braceCount === 0) {
                startLine = i + 1;
                currentJson = '';
              }
              braceCount++;
            }

            if (braceCount > 0) {
              currentJson += char;
            }

            if (char === '}') {
              braceCount--;
              if (braceCount === 0 && currentJson) {
                try {
                  const parsed = JSON.parse(currentJson);
                  jsonObjects.push({
                    startLine,
                    endLine: i + 1,
                    data: parsed,
                    text: currentJson
                  });
                } catch (e) {
                  // Ignore parsing failed objects
                }
                currentJson = '';
              }
            }
          }

          if (braceCount > 0) {
            currentJson += '\n';
          }
        }

        this.parsedJson = jsonObjects;
      } catch (e) {
        appLogger.error('JSON 解析失败:', e);
        this.parsedJson = null;
      }
    },

    handleMouseMove(e) {
      if (!this.monacoEditor || !this.parsedJson) return;

      const position = e.target.position;
      const model = this.monacoEditor.getModel();
      const lineContent = model.getLineContent(position.lineNumber);

      const word = model.getWordAtPosition(position);
      if (word) {
        const keyInfo = this.findKeyAtPosition(position, lineContent);

        if (keyInfo) {
          // Highlight on key
          const range = new monaco.Range(position.lineNumber, keyInfo.startColumn, position.lineNumber, keyInfo.endColumn);

          this.hoverDecorations = this.monacoEditor.deltaDecorations(this.hoverDecorations, [
            {
              range,
              options: {
                inlineClassName: 'json-key-hover',
                hoverMessage: { value: '💡 点击复制该字段的值' }
              }
            }
          ]);
          return;
        }
      }

      // Clear Highlight
      this.hoverDecorations = this.monacoEditor.deltaDecorations(this.hoverDecorations, []);
    },

    findKeyAtPosition(position, lineContent) {
      // Match more precisely JSON key to make sure it's key-value in pair
      // Formats like "key": value or key: value

      // First check if the cursor position is in a string with a quote (possibly value)
      let quoteCount = 0;
      let inValue = false;
      let lastColonIndex = -1;

      // From the beginning of the line to the cursor position, calculate the position of the quote and the colon
      for (let i = 0; i < position.column - 1; i++) {
        const char = lineContent.charAt(i);
        if (char === '"' && (i === 0 || lineContent.charAt(i - 1) !== '\\')) {
          quoteCount++;
        }
        if (char === ':') {
          lastColonIndex = i;
          quoteCount = 0; // Reset quote count after colon
        }
      }

      // If the number of quotations is odd after the colon, the string at value Medium
      if (lastColonIndex !== -1 && lastColonIndex < position.column - 1) {
        // Check if in value part
        let quotesAfterColon = 0;
        for (let i = lastColonIndex + 1; i < position.column - 1; i++) {
          if (lineContent.charAt(i) === '"' && (i === 0 || lineContent.charAt(i - 1) !== '\\')) {
            quotesAfterColon++;
          }
        }
        // If within quote sign of value, do not process
        if (quotesAfterColon % 2 === 1) {
          return null;
        }
      }

      // Match key: "key" with quotes:
      const quotedKeyRegex = /"([^"]+)"\s*:/g;
      let match;

      while ((match = quotedKeyRegex.exec(lineContent)) !== null) {
        const keyStartColumn = match.index + 2; // Skip the starting quote
        const keyEndColumn = match.index + 1 + match[1].length + 1; // Including ending quotes
        const colonIndex = match.index + match[0].length - 1;

        // Ensure that the cursor is within the quote of the key and before the colon
        if (position.column >= match.index + 1 && position.column <= keyEndColumn && position.column < colonIndex) {
          return {
            key: match[1],
            startColumn: match.index + 1,
            endColumn: keyEndColumn
          };
        }
      }

      // Matches a key without quotation marks: key:
      const unquotedKeyRegex = /(\w+)\s*:/g;

      while ((match = unquotedKeyRegex.exec(lineContent)) !== null) {
        const keyStartColumn = match.index + 1;
        const keyEndColumn = match.index + match[1].length;
        const colonIndex = match.index + match[0].length - 1;

        // Make sure the cursor is within the key and before the colon
        // And this key is not in quotation marks.
        const beforeKey = lineContent.substring(0, match.index);
        const quotesBeforeKey = (beforeKey.match(/"/g) || []).length;

        // If the previous quotation number is even, not string Internal
        if (quotesBeforeKey % 2 === 0 && position.column >= keyStartColumn && position.column <= keyEndColumn && position.column < colonIndex) {
          return {
            key: match[1],
            startColumn: keyStartColumn,
            endColumn: keyEndColumn + 1
          };
        }
      }

      return null;
    },

    handleClickCopyValue(position) {
      if (!this.monacoEditor || !this.parsedJson) return;

      const model = this.monacoEditor.getModel();
      const lineContent = model.getLineContent(position.lineNumber);
      const keyInfo = this.findKeyAtPosition(position, lineContent);

      if (keyInfo) {
        // Found JSON objects belonging to the current line
        const jsonObj = this.parsedJson.find((obj) => position.lineNumber >= obj.startLine && position.lineNumber <= obj.endLine);

        if (jsonObj) {
          const value = this.getValueByKey(jsonObj.data, keyInfo.key);
          if (value !== undefined) {
            const valueStr = typeof value === 'string' ? value : JSON.stringify(value, null, 2);
            this.copyToClipboard(valueStr, `已复制字段 "${keyInfo.key}" 的值`);
          }
        }
      }
    },

    getValueByKey(obj, key) {
      // Recursively search for key corresponding value
      if (obj && typeof obj === 'object') {
        if (key in obj) {
          return obj[key];
        }

        for (let k in obj) {
          if (typeof obj[k] === 'object') {
            const result = this.getValueByKey(obj[k], key);
            if (result !== undefined) {
              return result;
            }
          }
        }
      }

      return undefined;
    },

    async copyToClipboard(text, message = '已复制到剪贴板') {
      try {
        if (navigator.clipboard && window.isSecureContext) {
          await navigator.clipboard.writeText(text);
        } else {
          // Downscaling programme
          const textArea = document.createElement('textarea');
          textArea.value = text;
          textArea.style.position = 'fixed';
          textArea.style.left = '-999999px';
          document.body.appendChild(textArea);
          textArea.focus();
          textArea.select();

          try {
            document.execCommand('copy');
          } catch (err) {
            appLogger.error('复制失败:', err);
            Toast.error('复制失败');
            return;
          } finally {
            document.body.removeChild(textArea);
          }
        }

        Toast.success(message);
      } catch (err) {
        appLogger.error('复制失败:', err);
        Toast.error('复制失败');
      }
    }
  },
  beforeUnmount() {
    if (this.monacoEditor) {
      this.monacoEditor.dispose();
    }
  }
};
</script>

<template>
  <div class="json-editor" ref="jsonEditor" :style="`border: ${borderStyle};`"></div>
</template>

<style scoped lang="less">
.json-editor {
  width: 100%;
}

:deep(.message) {
  display: none;
}

:deep(.below) {
  display: none;
}

:deep(.json-copy-icon) {
  background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="%23666"><path d="M4 2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2zm0 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V4a1 1 0 0 0-1-1H4z"/><path d="M6 0h6a2 2 0 0 1 2 2v6h-1V2a1 1 0 0 0-1-1H6V0z"/></svg>')
    no-repeat center center;
  background-size: 14px 14px;
  cursor: pointer;
  transition: opacity 0.2s;

  &:hover {
    opacity: 0.7;
  }
}

:deep(.json-key-hover) {
  background-color: #fff3cd;
  border-bottom: 2px solid #ffc107;
  cursor: pointer;
  padding: 2px 4px;
  border-radius: 3px;
}
</style>

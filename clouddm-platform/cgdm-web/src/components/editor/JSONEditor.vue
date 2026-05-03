<script>
import * as monaco from 'monaco-editor';
import { markRaw } from 'vue';
import { Message } from 'view-ui-plus';

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
          console.log('set', this.text);
          this.monacoEditor.getModel().setValue(this.text);
          // this.updateDecorations();
        } else {
          this.monacoEditor = markRaw(
            monaco.editor.create(this.$refs.jsonEditor, {
              value: this.text, // 编辑器的值
              language: this.language,
              fontSize: 14,
              fontWeight: 'bold',
              scrollBeyondLastLine: false,
              readOnly: true,
              theme: 'vs', // 编辑器主题：vs, hc-black, or vs-dark，更多选择详见官网
              minimap: {
                enabled: false
              },
              automaticLayout: true,
              autoIndent: true, // 自动缩进
              glyphMargin: true // 启用 glyph margin 用于显示复制图标
            })
          );

          // 监听鼠标点击事件 - 复制整个 JSON
          this.monacoEditor.onMouseDown((e) => {
            if (e.target.type === monaco.editor.MouseTargetType.GUTTER_GLYPH_MARGIN) {
              const lineNumber = e.target.position?.lineNumber;
              if (lineNumber) {
                this.copyJsonAtLine(lineNumber);
              }
            }
          });
          //
          // // 监听鼠标移动 - 检测悬停在 JSON key 上
          // this.monacoEditor.onMouseMove((e) => {
          //   if (e.target.position) {
          //     this.handleMouseMove(e);
          //   }
          // });
          //
          // // 监听点击 - 复制 key 的 value
          // this.monacoEditor.onMouseDown((e) => {
          //   if (e.target.position && e.event.leftButton) {
          //     this.handleClickCopyValue(e.target.position);
          //   }
          // });
          //
          // // 显示所有 JSON 对象第一行的复制图标
          this.updateDecorations();
          //
          // // 解析 JSON
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

      // 查找所有 JSON 对象的起始行
      for (let i = 1; i <= totalLines; i++) {
        const lineContent = model.getLineContent(i).trim();
        if (lineContent.startsWith('{')) {
          jsonStartLines.push(i);
        }
      }

      // 为每个 JSON 对象的第一行添加复制图标
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

      // 查找当前 JSON 对象的起始和结束行
      const { startLine, endLine } = this.findJsonBoundaries(lineNumber, totalLines, model);

      if (startLine && endLine) {
        // 提取 JSON 文本
        const jsonText = model.getValueInRange(new monaco.Range(startLine, 1, endLine, model.getLineMaxColumn(endLine)));

        // 复制到剪贴板
        this.copyToClipboard(jsonText);
      }
    },

    findJsonBoundaries(lineNumber, totalLines, model) {
      let startLine = lineNumber;
      let endLine = lineNumber;
      let braceCount = 0;
      let foundStart = false;

      // 向上查找 JSON 对象的起始 {
      for (let i = lineNumber; i >= 1; i--) {
        const lineContent = model.getLineContent(i).trim();

        if (!foundStart && lineContent.startsWith('{')) {
          startLine = i;
          foundStart = true;
          break;
        }
      }

      // 如果没找到起始，直接返回
      if (!foundStart) {
        // 可能是单行 JSON，尝试直接复制当前行
        const lineContent = model.getLineContent(lineNumber).trim();
        if (lineContent.startsWith('{') && lineContent.endsWith('}')) {
          return { startLine: lineNumber, endLine: lineNumber };
        }
        return { startLine: null, endLine: null };
      }

      // 从起始行开始，重新计算大括号匹配
      braceCount = 0;
      for (let i = startLine; i <= totalLines; i++) {
        const lineContent = model.getLineContent(i);

        // 计算当前行的大括号数量
        for (let char of lineContent) {
          if (char === '{') braceCount++;
          else if (char === '}') braceCount--;

          if (braceCount === 0) {
            endLine = i;
            return { startLine, endLine };
          }
        }
      }

      // 如果没找到匹配的结束，返回到文件末尾
      return { startLine, endLine: totalLines };
    },

    parseJson() {
      try {
        // 尝试解析整个文本中的所有 JSON 对象
        const model = this.monacoEditor.getModel();
        const fullText = model.getValue();
        const jsonObjects = [];

        // 按行分割并查找 JSON 对象
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
                  // 忽略解析失败的对象
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
        console.error('JSON 解析失败:', e);
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
          // 在 key 上显示高亮
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

      // 清除高亮
      this.hoverDecorations = this.monacoEditor.deltaDecorations(this.hoverDecorations, []);
    },

    findKeyAtPosition(position, lineContent) {
      // 更精确地匹配 JSON key，确保是 key-value 对中的 key
      // 格式如 "key": value 或 key: value

      // 首先检查光标位置是否在一个引号包裹的字符串中（可能是 value）
      let quoteCount = 0;
      let inValue = false;
      let lastColonIndex = -1;

      // 从行首到光标位置，计算引号和冒号的位置
      for (let i = 0; i < position.column - 1; i++) {
        const char = lineContent.charAt(i);
        if (char === '"' && (i === 0 || lineContent.charAt(i - 1) !== '\\')) {
          quoteCount++;
        }
        if (char === ':') {
          lastColonIndex = i;
          quoteCount = 0; // 冒号后重置引号计数
        }
      }

      // 如果在冒号之后且引号数为奇数，说明在 value 的字符串中
      if (lastColonIndex !== -1 && lastColonIndex < position.column - 1) {
        // 检查是否在 value 部分
        let quotesAfterColon = 0;
        for (let i = lastColonIndex + 1; i < position.column - 1; i++) {
          if (lineContent.charAt(i) === '"' && (i === 0 || lineContent.charAt(i - 1) !== '\\')) {
            quotesAfterColon++;
          }
        }
        // 如果在 value 的引号内，不处理
        if (quotesAfterColon % 2 === 1) {
          return null;
        }
      }

      // 匹配带引号的 key: "key":
      const quotedKeyRegex = /"([^"]+)"\s*:/g;
      let match;

      while ((match = quotedKeyRegex.exec(lineContent)) !== null) {
        const keyStartColumn = match.index + 2; // 跳过开始的引号
        const keyEndColumn = match.index + 1 + match[1].length + 1; // 包括结束的引号
        const colonIndex = match.index + match[0].length - 1;

        // 确保光标在 key 的引号范围内，且在冒号之前
        if (position.column >= match.index + 1 && position.column <= keyEndColumn && position.column < colonIndex) {
          return {
            key: match[1],
            startColumn: match.index + 1,
            endColumn: keyEndColumn
          };
        }
      }

      // 匹配不带引号的 key: key:
      const unquotedKeyRegex = /(\w+)\s*:/g;

      while ((match = unquotedKeyRegex.exec(lineContent)) !== null) {
        const keyStartColumn = match.index + 1;
        const keyEndColumn = match.index + match[1].length;
        const colonIndex = match.index + match[0].length - 1;

        // 确保光标在 key 的范围内，且在冒号之前
        // 并且这个 key 前面不是在引号内（避免匹配 value 中的内容）
        const beforeKey = lineContent.substring(0, match.index);
        const quotesBeforeKey = (beforeKey.match(/"/g) || []).length;

        // 如果前面的引号数是偶数，说明不在字符串内
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
        // 找到当前行所属的 JSON 对象
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
      // 递归查找 key 对应的 value
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
          // 降级方案
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
            console.error('复制失败:', err);
            Message.error('复制失败');
            return;
          } finally {
            document.body.removeChild(textArea);
          }
        }

        Message.success(message);
      } catch (err) {
        console.error('复制失败:', err);
        Message.error('复制失败');
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

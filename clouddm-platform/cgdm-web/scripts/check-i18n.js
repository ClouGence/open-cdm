#!/usr/bin/env node

const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

// 加载配置文件
let config = {};
const configPath = path.join(process.cwd(), '.i18n-check.config.js');
if (fs.existsSync(configPath)) {
  config = require(configPath);
}

// 中文字符正则表达式
const CHINESE_REGEX = /[\u4e00-\u9fff]+/g;

// 默认配置
const DEFAULT_CONFIG = {
  fileExtensions: ['.vue', '.js', '.ts'],
  excludePatterns: [
    'node_modules',
    'dist',
    '.git',
    'src/locales',
    'src/assets/iconfont',
    'public/luckysheet',
    '.husky',
    'scripts',
    'src/styles',
    'public/css'
  ],
  excludeLinePatterns: [
    /^\s*\/\//, // 单行注释
    /^\s*\*/, // 多行注释
    /^\s*\/\*/, // 多行注释开始
    /^\s*\*\//, // 多行注释结束
    /\/\/.*[\u4e00-\u9fff]/, // 行内注释（// 后面的中文）
    /\/\*[\s\S]*?[\u4e00-\u9fff][\s\S]*?\*\//, // 多行注释中的中文
    /console\.(log|warn|error|info)/, // 控制台输出
    /TODO/, // TODO标记
    /FIXME/, // FIXME标记
    /NOTE/, // NOTE标记
    /^\s*<!--/, // HTML注释
    /^\s*-->/ // HTML注释结束
  ],
  excludeTerms: [],
  failOnError: true,
  verbose: true
};

// 合并配置
const finalConfig = { ...DEFAULT_CONFIG, ...config };

// 获取git暂存区的文件
function getStagedFiles() {
  try {
    const output = execSync('git diff --cached --name-only', { encoding: 'utf8' });
    return output
      .trim()
      .split('\n')
      .filter((file) => file.length > 0);
  } catch (error) {
    console.error('获取暂存区文件失败:', error.message);
    return [];
  }
}

// 检查文件是否应该被排除
function shouldExcludeFile(filePath) {
  return finalConfig.excludePatterns.some((pattern) => filePath.includes(pattern));
}

// 检查行是否应该被排除
function shouldExcludeLine(line) {
  // 首先检查是否匹配排除模式
  if (finalConfig.excludeLinePatterns.some((pattern) => pattern.test(line))) {
    return true;
  }

  // 检查行内注释（// 后面的内容）
  const inlineCommentMatch = line.match(/\/\/(.*)$/);
  if (inlineCommentMatch) {
    const commentContent = inlineCommentMatch[1];
    // 如果注释部分包含中文，则排除整行
    if (/[\u4e00-\u9fff]/.test(commentContent)) {
      return true;
    }
  }

  // 检查多行注释块
  if (line.includes('/*') && line.includes('*/')) {
    const commentMatch = line.match(/\/\*([\s\S]*?)\*\//);
    if (commentMatch) {
      const commentContent = commentMatch[1];
      if (/[\u4e00-\u9fff]/.test(commentContent)) {
        return true;
      }
    }
  }

  return false;
}

// 检查文件是否包含硬编码中文
function checkFileForChinese(filePath) {
  if (!fs.existsSync(filePath)) {
    return [];
  }

  const content = fs.readFileSync(filePath, 'utf8');
  const lines = content.split('\n');
  const issues = [];
  let inMultiLineComment = false;

  lines.forEach((line, index) => {
    // 跳过空行
    if (line.trim() === '') {
      return;
    }

    // 处理多行注释状态
    if (inMultiLineComment) {
      // 如果当前在多行注释中，检查是否结束
      if (line.includes('*/')) {
        inMultiLineComment = false;
      }
      return; // 跳过注释内容
    }

    // 检查多行注释开始
    if (line.includes('/*')) {
      inMultiLineComment = true;
      // 如果同一行就结束了，检查是否应该排除
      if (line.includes('*/')) {
        inMultiLineComment = false;
        if (shouldExcludeLine(line)) {
          return;
        }
      } else {
        return; // 多行注释开始，跳过这行
      }
    }

    // 检查是否应该排除
    if (shouldExcludeLine(line)) {
      return;
    }

    // 检查是否包含中文字符
    const matches = line.match(CHINESE_REGEX);
    if (matches) {
      // 过滤掉一些常见的中文技术术语或不需要国际化的内容
      const filteredMatches = matches.filter((match) => {
        return !finalConfig.excludeTerms.includes(match);
      });

      if (filteredMatches.length > 0) {
        issues.push({
          line: index + 1,
          content: line.trim(),
          matches: filteredMatches
        });
      }
    }
  });

  return issues;
}

// 主函数
function main() {
  console.log('🔍 检查国际化...\n');

  const stagedFiles = getStagedFiles();
  if (stagedFiles.length === 0) {
    console.log('✅ 没有暂存的文件需要检查');
    return;
  }

  const issues = [];
  const checkedFiles = [];

  stagedFiles.forEach((file) => {
    // 检查文件扩展名
    const ext = path.extname(file);
    if (!finalConfig.fileExtensions.includes(ext)) {
      return;
    }

    // 检查是否应该排除
    if (shouldExcludeFile(file)) {
      return;
    }

    checkedFiles.push(file);
    const fileIssues = checkFileForChinese(file);
    if (fileIssues.length > 0) {
      issues.push({
        file,
        issues: fileIssues
      });
    }
  });

  if (checkedFiles.length === 0) {
    console.log('✅ 没有需要检查的文件');
    return;
  }

  console.log(`📁 检查了 ${checkedFiles.length} 个文件:`);
  checkedFiles.forEach((file) => console.log(`   - ${file}`));
  console.log('');

  if (issues.length === 0) {
    console.log('✅ 所有文件都通过了国际化检查！');
    return;
  }

  console.log('❌ 发现以下国际化问题:\n');

  issues.forEach(({ file, issues: fileIssues }) => {
    console.log(`📄 ${file}:`);
    fileIssues.forEach(({ line, content, matches }) => {
      console.log(`   第 ${line} 行: ${content}`);
      console.log(`   包含中文: ${matches.join(', ')}`);
      console.log('');
    });
  });

  console.log('💡 建议:');
  console.log('   1. 将硬编码的中文文本替换为 $t() 函数调用');
  console.log('   2. 在 src/locales/zh.json 和 src/locales/en.json 中添加对应的翻译');
  console.log('   3. 使用拼音命名方式作为国际化键值');
  console.log('');

  if (finalConfig.failOnError) {
    process.exit(1);
  }
}

// 运行主函数
if (require.main === module) {
  main();
}

module.exports = { checkFileForChinese, getStagedFiles };

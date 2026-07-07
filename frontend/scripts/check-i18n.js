#!/usr/bin/env node

const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

// Load profile
let config = {};
const configPath = path.join(process.cwd(), '.i18n-check.config.js');
if (fs.existsSync(configPath)) {
  config = require(configPath);
}

// Chinese character regular expression
const CHINESE_REGEX = /[\u4e00-\u9fff]+/g;

// Default Configuration
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
    /^\s*\/\//, // Single-line comment
    /^\s*\*/, // Multi-line comment
    /^\s*\/\*/, // Start of multi-line comment
    /^\s*\*\//, // End of multi-line comment
    /\/\/.*[\u4e00-\u9fff]/, // Line Comment (in Chinese at the end of/ after)
    /\/\*[\s\S]*?[\u4e00-\u9fff][\s\S]*?\*\//, // Chinese in multiline notes
    /console\.(log|warn|error|info)/, // Console output
    /TODO/, // TODO marker
    /FIXME/, // FIXME marker
    /NOTE/, // NOTE marker
    /^\s*<!--/, // HTML comment
    /^\s*-->/ // End of HTML comment
  ],
  excludeTerms: [],
  failOnError: true,
  verbose: true
};

// Merge Configuration
const finalConfig = { ...DEFAULT_CONFIG, ...config };

// Fetch file for the Git hold area
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

// Check if the file should be excluded
function shouldExcludeFile(filePath) {
  return finalConfig.excludePatterns.some((pattern) => filePath.includes(pattern));
}

// Check if the line should be excluded
function shouldExcludeLine(line) {
  // First check to match exclusion mode
  if (finalConfig.excludeLinePatterns.some((pattern) => pattern.test(line))) {
    return true;
  }

  // Check the line note (refer to content)
  const inlineCommentMatch = line.match(/\/\/(.*)$/);
  if (inlineCommentMatch) {
    const commentContent = inlineCommentMatch[1];
    // If the comment contains Chinese, exclude the whole line
    if (/[\u4e00-\u9fff]/.test(commentContent)) {
      return true;
    }
  }

  // Check multiline comment blocks
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

// Check if the file contains hard-coded Chinese
function checkFileForChinese(filePath) {
  if (!fs.existsSync(filePath)) {
    return [];
  }

  const content = fs.readFileSync(filePath, 'utf8');
  const lines = content.split('\n');
  const issues = [];
  let inMultiLineComment = false;

  lines.forEach((line, index) => {
    // Skip empty lines
    if (line.trim() === '') {
      return;
    }

    // Process multiline comment state
    if (inMultiLineComment) {
      // Check if the current multiline comment is over
      if (line.includes('*/')) {
        inMultiLineComment = false;
      }
      return; // Skip Comment
    }

    // Check multiline comments start
    if (line.includes('/*')) {
      inMultiLineComment = true;
      // If the same line is over, should the check be excluded?
      if (line.includes('*/')) {
        inMultiLineComment = false;
        if (shouldExcludeLine(line)) {
          return;
        }
      } else {
        return; // Start with multiline notes, skip this line.
      }
    }

    // Check to exclude
    if (shouldExcludeLine(line)) {
      return;
    }

    // Check to include Chinese characters
    const matches = line.match(CHINESE_REGEX);
    if (matches) {
      // Filter some common Chinese technical terms or content that does not require internationalization
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

// Main Functions
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
    // Check File Extensions
    const ext = path.extname(file);
    if (!finalConfig.fileExtensions.includes(ext)) {
      return;
    }

    // Check to exclude
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

// Run main function
if (require.main === module) {
  main();
}

module.exports = { checkFileForChinese, getStagedFiles };

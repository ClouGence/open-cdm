#!/usr/bin/env node

/**
 * Check Tool: Verify that all functions called by this.$services.xx and service.xx are available at http/api directory Down
 * Output result to markdown file
 */

const fs = require('fs');
const path = require('path');

// Configuration
const API_DIR = path.join(__dirname, 'src/services/http/api');
const SRC_DIR = path.join(__dirname, 'src');
const OUTPUT_FILE = path.join(__dirname, 'services-check-report.md');

// Store all valid API functions and their source files
const validApiFunctions = new Map(); // { functionName: fileName }

// Store all used $[services] calls
const servicesCalls = new Map(); // { functionName: [{ file, line, code }] }

/**
 * Recursively read all files in directory
 */
function getAllFiles(dir, fileList = [], extensions = ['.js', '.vue']) {
  if (!fs.existsSync(dir)) {
    return fileList;
  }

  const files = fs.readdirSync(dir);

  files.forEach((file) => {
    const filePath = path.join(dir, file);
    const stat = fs.statSync(filePath);

    if (stat.isDirectory()) {
      // Skip directories such as node modules, dist, target
      if (file !== 'node_modules' && file !== 'dist' && file !== 'target' && file !== '.git') {
        getAllFiles(filePath, fileList, extensions);
      }
    } else {
      const ext = path.extname(file);
      if (extensions.includes(ext)) {
        fileList.push(filePath);
      }
    }
  });

  return fileList;
}

/**
 * Export function name extracted from API file
 */
function extractApiNames(filePath) {
  try {
    const content = fs.readFileSync(filePath, 'utf-8');
    const fileName = path.basename(filePath);

    // Method 1: Match export const xxApi = {...}
    const exportPattern = /export\s+const\s+(\w+)\s*=\s*\{([^}]*(?:\{[^}]*\}[^}]*)*)\}/gs;
    let match;

    while ((match = exportPattern.exec(content)) !== null) {
      const objectContent = match[2];
      // Extract keyname from object
      const keyPattern = /^\s*([a-zA-Z_$][a-zA-Z0-9_$]*)\s*:/gm;
      let keyMatch;

      while ((keyMatch = keyPattern.exec(objectContent)) !== null) {
        const functionName = keyMatch[1];
        validApiFunctions.set(functionName, fileName);
      }
    }
  } catch (error) {
    console.error(`解析 API 文件失败: ${filePath}`, error.message);
  }
}

/**
 * Extract this. $services.xx and service.xx calls from the Vue/JS file
 */
function extractServicesCalls(filePath) {
  try {
    const content = fs.readFileSync(filePath, 'utf-8');
    const isVueFile = filePath.endsWith('.vue');

    // Split into rows for subsequent location
    const lines = content.split('\n');

    // For Vue files, only check the script block content.
    let scriptStartLine = 0;
    let scriptContent = content;

    if (isVueFile) {
      const scriptMatch = content.match(/<script[^>]*>/);
      if (!scriptMatch) return;

      const scriptStart = content.indexOf(scriptMatch[0]);
      scriptStartLine = content.substring(0, scriptStart).split('\n').length - 1;

      const fullScriptMatch = content.match(/<script[^>]*>([\s\S]*?)<\/script>/);
      if (fullScriptMatch) {
        scriptContent = fullScriptMatch[1];
      }
    }

    // Define modalities to match:this.$services.xx and serviceces.xx
    const patterns = [
      { regex: /this\.\$services\.([a-zA-Z_$][a-zA-Z0-9_$]*)/g, type: 'this.$services' },
      { regex: /(?<![a-zA-Z0-9_$])services\.([a-zA-Z_$][a-zA-Z0-9_$]*)/g, type: 'services' }
    ];

    patterns.forEach(({ regex, type }) => {
      let match;
      // Reset lastIndex
      regex.lastIndex = 0;

      while ((match = regex.exec(scriptContent)) !== null) {
        const functionName = match[1];
        const position = match.index;

        // Calculate line numbers in scriptContant
        const beforeMatch = scriptContent.substring(0, position);
        const lineNumber = beforeMatch.split('\n').length;
        const actualLineNumber = scriptStartLine + lineNumber;

        // Fetch code contents for the line
        const lineContent = lines[actualLineNumber - 1]?.trim() || '';

        if (!servicesCalls.has(functionName)) {
          servicesCalls.set(functionName, []);
        }

        servicesCalls.get(functionName).push({
          file: path.relative(SRC_DIR, filePath),
          line: actualLineNumber,
          code: lineContent.substring(0, 120), // Limit Length
          type: type
        });
      }
    });
  } catch (error) {
    console.error(`解析文件失败: ${filePath}`, error.message);
  }
}

/**
 * Generate Markdown report
 */
function generateMarkdownReport(missingFunctions, validFunctions, stats) {
  const timestamp = new Date().toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' });

  let md = `# $services 函数调用检查报告\n\n`;
  md += `**生成时间**: ${timestamp}\n\n`;
  md += `---\n\n`;

  // Statistical information
  md += `## 📊 统计信息\n\n`;
  md += `| 项目 | 数量 |\n`;
  md += `|------|------|\n`;
  md += `| API 文件中定义的函数 | ${stats.totalApiFunctions} |\n`;
  md += `| 代码中使用的函数 | ${stats.totalUsedFunctions} |\n`;
  md += `| ✅ 有效的调用 | ${stats.validCalls} |\n`;
  md += `| ❌ 无效的调用 | ${stats.invalidCalls} |\n`;
  md += `| 总调用次数 | ${stats.totalCallCount} |\n\n`;

  // Check results
  if (missingFunctions.length === 0) {
    md += `## ✅ 检查结果\n\n`;
    md += `**所有 $services 调用都有对应的 API 定义！**\n\n`;
  } else {
    md += `## ❌ 发现问题\n\n`;
    md += `找到 **${missingFunctions.length}** 个不存在的函数调用，详情如下：\n\n`;

    missingFunctions.forEach(({ functionName, calls }, index) => {
      md += `### ${index + 1}. \`${functionName}\`\n\n`;
      md += `**调用次数**: ${calls.length}\n\n`;
      md += `**调用位置**:\n\n`;

      calls.forEach(({ file, line, code, type }) => {
        md += `- **${file}:${line}** \`${type}\`\n`;
        md += `  \`\`\`javascript\n`;
        md += `  ${code}\n`;
        md += `  \`\`\`\n\n`;
      });
    });
  }

  // List of valid functions
  md += `---\n\n`;
  md += `## ✅ 有效的函数调用 (${validFunctions.length})\n\n`;
  md += `<details>\n`;
  md += `<summary>点击展开查看所有有效的函数调用</summary>\n\n`;

  validFunctions.sort();
  validFunctions.forEach((funcName, index) => {
    const apiFile = validApiFunctions.get(funcName) || '未知';
    const calls = servicesCalls.get(funcName) || [];
    md += `${index + 1}. \`${funcName}\` - 定义于 \`${apiFile}\` (调用 ${calls.length} 次)\n`;
  });

  md += `\n</details>\n\n`;

  // List of all API functions
  md += `---\n\n`;
  md += `## 📚 所有可用的 API 函数 (${stats.totalApiFunctions})\n\n`;
  md += `<details>\n`;
  md += `<summary>点击展开查看所有 API 函数</summary>\n\n`;

  const apiByFile = new Map();
  for (const [funcName, fileName] of validApiFunctions.entries()) {
    if (!apiByFile.has(fileName)) {
      apiByFile.set(fileName, []);
    }
    apiByFile.get(fileName).push(funcName);
  }

  const sortedFiles = Array.from(apiByFile.keys()).sort();
  sortedFiles.forEach((fileName) => {
    const functions = apiByFile.get(fileName).sort();
    md += `\n### ${fileName} (${functions.length})\n\n`;
    functions.forEach((funcName) => {
      md += `- \`${funcName}\`\n`;
    });
  });

  md += `\n</details>\n\n`;

  md += `---\n\n`;
  md += `*报告由 check-services.js 自动生成*\n`;

  return md;
}

/**
 * Main Functions
 */
function main() {
  console.log('🔍 开始检查 this.$services 和 services 函数调用...\n');

  // Step 1: Collect all API functions
  console.log('📚 步骤 1: 收集 API 函数名...');
  const apiFiles = getAllFiles(API_DIR, [], ['.js']);
  console.log(`   找到 ${apiFiles.length} 个 API 文件`);
  apiFiles.forEach((file) => {
    extractApiNames(file);
  });
  console.log(`   提取到 ${validApiFunctions.size} 个有效的 API 函数\n`);

  // Step 2: Scan all source files and find $services calling
  console.log('🔎 步骤 2: 扫描源文件中的 $services 调用 (this.$services.xxx 和 services.xxx)...');
  const srcFiles = getAllFiles(SRC_DIR, [], ['.js', '.vue']);
  console.log(`   找到 ${srcFiles.length} 个源文件`);
  srcFiles.forEach((file) => {
    extractServicesCalls(file);
  });
  console.log(`   找到 ${servicesCalls.size} 个不同的函数调用\n`);

  // Step 3: Check and classify
  console.log('✅ 步骤 3: 分析检查结果...\n');

  const missingFunctions = [];
  const validFunctions = [];
  let totalCallCount = 0;

  for (const [functionName, calls] of servicesCalls.entries()) {
    totalCallCount += calls.length;
    if (!validApiFunctions.has(functionName)) {
      missingFunctions.push({ functionName, calls });
    } else {
      validFunctions.push(functionName);
    }
  }

  const stats = {
    totalApiFunctions: validApiFunctions.size,
    totalUsedFunctions: servicesCalls.size,
    validCalls: validFunctions.length,
    invalidCalls: missingFunctions.length,
    totalCallCount: totalCallCount
  };

  // Generate Markdown report
  console.log('📝 步骤 4: 生成 Markdown 报告...');
  const markdownContent = generateMarkdownReport(missingFunctions, validFunctions, stats);
  fs.writeFileSync(OUTPUT_FILE, markdownContent, 'utf-8');
  console.log(`   报告已保存到: ${OUTPUT_FILE}\n`);

  // Show summary on console
  console.log('='.repeat(80));
  console.log(`\n📊 检查摘要:`);
  console.log(`   - API 文件中定义的函数: ${stats.totalApiFunctions} 个`);
  console.log(`   - 代码中使用的函数: ${stats.totalUsedFunctions} 个`);
  console.log(`   - 有效的调用: ${stats.validCalls} 个`);
  console.log(`   - 无效的调用: ${stats.invalidCalls} 个`);
  console.log(`   - 总调用次数: ${stats.totalCallCount} 次`);
  console.log('='.repeat(80));

  if (missingFunctions.length > 0) {
    console.log(`\n❌ 发现 ${missingFunctions.length} 个不存在的函数调用，详情请查看报告文件\n`);
    missingFunctions.slice(0, 5).forEach(({ functionName, calls }) => {
      console.log(`   - ${functionName} (${calls.length} 处调用)`);
    });
    if (missingFunctions.length > 5) {
      console.log(`   ... 还有 ${missingFunctions.length - 5} 个，详见报告文件`);
    }
    process.exit(1);
  } else {
    console.log(`\n✅ 所有 $services 调用都有对应的 API 定义！\n`);
    process.exit(0);
  }
}

// Run main function
main();

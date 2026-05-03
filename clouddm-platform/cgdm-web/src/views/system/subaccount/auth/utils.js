import axios from 'axios';
import { START_RECORD_NAMES_CONUT } from './constant';

/**
 *
 * @param {tree}
 * @returns flatteenTree
 */
function flattenTree(tree) {
  const result = [];
  function traverse(nodes) {
    nodes.forEach((node) => {
      result.push(node);
      if (node?.children && node?.children?.length > 0) {
        traverse(node.children);
      }
    });
  }
  traverse(tree);
  return result;
}

/**
 *
 * @param {tree, key}
 * @returns node || null
 */
function findNodeByKey(tree, key, maxDepth = 7, currentDepth = 0) {
  if (!Array.isArray(tree) || currentDepth >= maxDepth) return null;

  for (const node of tree) {
    if (node.key === key) {
      return node;
    }
    if (node.children) {
      const found = findNodeByKey(node.children, key, maxDepth, currentDepth + 1);
      if (found) return found;
    }
  }
  return null;
}

/**
 *
 * @param {node} 传入node树节点、该树节点需至少有levels、objName、parent字段
 * @returns string[], 返回namesList
 * @description 传入树node节点，当前节点开始往上层遍历，直到层级START_RECORD_NAMES_CONUT,最后找到objNames字段并最后组合返回namesList
 */
function getResTypeToNames(node = {}) {
  let nodeLevelsLength = node?.levels?.length;
  let curNode = node;
  const resNames = [];
  while (nodeLevelsLength > START_RECORD_NAMES_CONUT && curNode?.parent) {
    resNames.push(curNode?.objName);
    curNode = curNode?.parent;
    nodeLevelsLength--;
  }
  return resNames.reverse();
}

const fetchWithTimeout = (requestFunc, timeout = 20000) => {
  const source = axios.CancelToken.source();

  return new Promise((resolve, reject) => {
    const timeoutId = setTimeout(() => {
      source.cancel('请求超时');
      reject(new Error('请求超时'));
    }, timeout);

    requestFunc({
      cancelToken: source.token
    })
      .then((response) => {
        clearTimeout(timeoutId);
        resolve(response);
      })
      .catch((error) => {
        clearTimeout(timeoutId);
        if (axios.isCancel(error)) {
          reject(new Error(`请求取消: ${error.message}`));
        } else {
          reject(error);
        }
      });
  });
};

export { flattenTree, getResTypeToNames, findNodeByKey, fetchWithTimeout };

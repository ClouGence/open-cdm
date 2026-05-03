import Mapping from '@/views/util';

export default {
  name: 'UtilMixin',
  methods: {
    getShowNameByDeployTypeAndDsName(instanceType, type) {
      const typeNameList = Mapping.deployDsMap[instanceType] || {};
      return typeNameList[type] === undefined ? type : typeNameList[type];
    },
    highlight(text, word, tag = 'span') {
      if (word && word.length) {
        const re = new RegExp(word, 'g');
        if (re.test(text)) {
          text = text.replace(re, `<${tag} class="highlight">$&</${tag}>`);
        }
      }
      return text;
    },
    highlightStrict(text, word, style = '', tag = 'span') {
      if (word && word.length) {
        if (text.includes(word)) {
          text = text.replace(word, `<${tag} style="${style}">${word}</${tag}>`);
        }
      }
      return text;
    }
  }
};

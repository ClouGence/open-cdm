import html2canvas from 'html2canvas';
import i18n from '../i18n';

const exportMonitorMixin = {
  data() {
    return {
      originalStyle: null,
      exportImgLoading: false
    };
  },
  methods: {
    // 临时修改样式确保元素完整显示
    prepareElementForCapture(element, index) {
      this.originalStyle = {
        overflow: element.style.overflow,
        height: element.style.height,
        maxHeight: element.style.maxHeight,
        position: element.style.position,
        top: element.style.top,
        left: element.style.left,
        zIndex: element.style.zIndex
      };

      // 临时修改样式：移除溢出隐藏，确保元素完整显示
      element.style.overflow = 'visible';
      element.style.maxHeight = 'none';

      // 计算元素实际高度（包括被隐藏的部分）
      const actualHeight = element.scrollHeight;
      element.style.height = `${actualHeight}px`;

      // 确保元素不被遮挡
      if (element.offsetParent) {
        element.style.position = 'relative';
        element.style.top = '0';
        element.style.left = '0';
        element.style.zIndex = '9999';
      }
    },

    // 恢复元素原始样式
    restoreElementStyle(element) {
      if (this.originalStyle) {
        element.style.overflow = this.originalStyle.overflow;
        element.style.height = this.originalStyle.height;
        element.style.maxHeight = this.originalStyle.maxHeight;
        element.style.position = this.originalStyle.position;
        element.style.top = this.originalStyle.top;
        element.style.left = this.originalStyle.left;
        element.style.zIndex = this.originalStyle.zIndex;
      }
    },
    // 下载图片的通用方法
    downloadImage(canvas, filename) {
      // 创建下载链接
      const link = document.createElement('a');
      // 将canvas转为PNG图片
      link.href = canvas.toDataURL('image/png');
      // 设置文件名（去除特殊字符）
      link.download = `${filename}.png`;

      // 触发下载
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },
    async handleExportMonitorImg(filename) {
      try {
        console.log('handleExportMonitorImg');
        this.exportImgLoading = true;

        // 等待 Vue 完成 DOM 更新，确保 loading 状态显示
        await this.$nextTick();
        // 额外延迟确保浏览器完成渲染
        await new Promise((resolve) => setTimeout(resolve, 50));

        const container = this.$refs.grid;
        if (!container) {
          this.exportImgLoading = false;
          return;
        }

        // 保存原始滚动位置
        const originalScrollTop = window.pageYOffset || document.documentElement.scrollTop;

        // 准备元素
        this.prepareElementForCapture(container);

        // 滚动到元素位置
        // container.scrollIntoView({ behavior: 'auto', block: 'start' });
        // await new Promise((resolve) => setTimeout(resolve, 300));

        // 捕获元素为图片
        const canvas = await html2canvas(container, {
          scale: 2, // 高分辨率
          useCORS: true,
          logging: false,
          windowWidth: container.scrollWidth,
          windowHeight: container.scrollHeight,
          allowTaint: true
        });

        // 恢复样式和滚动位置
        this.restoreElementStyle(container);
        // window.scrollTo(0, originalScrollTop);

        // 下载图片
        this.downloadImage(canvas, `${filename}_` + new Date().toLocaleString());

        this.exportImgLoading = false;
      } catch (error) {
        this.exportImgLoading = false;
        console.error(this.$t('shi-bai'), error);
        // alert('图表导出失败，请重试');
        this.$Modal.error({
          title: this.$t('cao-zuo-shi-bai'),
          content: this.$t('tu-biao-dao-chu-shi-bai-qing-chong-shi')
        });
      } finally {
        this.exportImgLoading = false;
      }
    }
  }
};

export default exportMonitorMixin;

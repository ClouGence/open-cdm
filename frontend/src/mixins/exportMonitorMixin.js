import appLogger from '@/utils/logger';
import i18n from '../i18n';

const exportMonitorMixin = {
  data() {
    return {
      originalStyle: null,
      exportImgLoading: false
    };
  },
  methods: {
    // Temporary change style to ensure complete display of elements
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

      // Temporary change style: remove the spill hide and ensure that the elements are shown in full
      element.style.overflow = 'visible';
      element.style.maxHeight = 'none';

      // Calculate the actual height of the element (including the hidden part)
      const actualHeight = element.scrollHeight;
      element.style.height = `${actualHeight}px`;

      // Make sure the elements are not covered.
      if (element.offsetParent) {
        element.style.position = 'relative';
        element.style.top = '0';
        element.style.left = '0';
        element.style.zIndex = '9999';
      }
    },

    // Restore Element Original Style
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
    // Common method for downloading pictures
    downloadImage(canvas, filename) {
      // Create Download Link
      const link = document.createElement('a');
      // Change Canvas to PNG Image
      link.href = canvas.toDataURL('image/png');
      // Set filename (delegate special characters)
      link.download = `${filename}.png`;

      // Trigger Download
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },
    async handleExportMonitorImg(filename) {
      try {
        appLogger.debug('handleExportMonitorImg');
        this.exportImgLoading = true;

        // Waiting for Vue to complete DOM update to make sure status display
        await this.$nextTick();
        // Additional delay to ensure browser render
        await new Promise((resolve) => setTimeout(resolve, 50));

        const container = this.$refs.grid;
        if (!container) {
          this.exportImgLoading = false;
          return;
        }

        // Save original scroll position
        const originalScrollTop = window.pageYOffset || document.documentElement.scrollTop;

        // Prepare elements
        this.prepareElementForCapture(container);

        // Scroll to Element Position
        // container.scrollIntoView({ behavior: 'auto', block: 'start' });
        // await new Promise((resolve) => setTimeout(resolve, 300));

        // Capture elements as pictures
        const { default: html2canvas } = await import('html2canvas');
        const canvas = await html2canvas(container, {
          scale: 2, // High Resolution
          useCORS: true,
          logging: false,
          windowWidth: container.scrollWidth,
          windowHeight: container.scrollHeight,
          allowTaint: true
        });

        // Restore Styles and Scroll Positions
        this.restoreElementStyle(container);
        // window.scrollTo(0, originalScrollTop);

        // Download Pictures
        this.downloadImage(canvas, `${filename}_` + new Date().toLocaleString());

        this.exportImgLoading = false;
      } catch (error) {
        this.exportImgLoading = false;
        appLogger.error(this.$t('shi-bai'), error);
        // alert('Chart export failed, please try again');
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

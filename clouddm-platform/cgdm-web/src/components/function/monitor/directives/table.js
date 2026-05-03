export default function link(scope, elem) {
  const ctrl = scope.ctrl;
  let data;
  const panel = ctrl.panel;
  let pageCount = 0;

  function getTableHeight() {
    let panelHeight = ctrl.height;

    if (pageCount > 1) {
      panelHeight -= 26;
    }

    return `${panelHeight - 31}px`;
  }

  function appendTableRows(tbodyElem) {
    ctrl.renderer.setTable(data);
    tbodyElem.empty();
    tbodyElem.html(ctrl.renderer.render(ctrl.pageIndex));
  }

  function switchPage(e) {
    const el = $(e.currentTarget);

    ctrl.pageIndex = parseInt(el.text(), 10) - 1;
    renderPanel();
  }

  function appendPaginationControls(footerElem) {
    footerElem.empty();

    const pageSize = panel.pageSize || 100;

    pageCount = Math.ceil(data.rows.length / pageSize);
    if (pageCount === 1) {
      return;
    }

    const startPage = Math.max(ctrl.pageIndex - 3, 0);
    const endPage = Math.min(pageCount, startPage + 9);

    const paginationList = $('<ul></ul>');

    for (let i = startPage; i < endPage; i++) {
      const activeClass = i === ctrl.pageIndex ? 'active' : '';
      const pageLinkElem = $(
        `<li><a class="table-panel-page-link pointer ${activeClass}">${i + 1}</a></li>`
      );

      paginationList.append(pageLinkElem);
    }

    footerElem.append(paginationList);
  }

  function renderPanel() {
    const panelElem = elem.parents('.panel');
    const rootElem = elem.find('.table-panel-scroll');
    const tbodyElem = elem.find('tbody');
    const footerElem = elem.find('.table-panel-footer');

    elem.css({ 'font-size': panel.fontSize });
    panelElem.addClass('table-panel-wrapper');

    appendTableRows(tbodyElem);
    appendPaginationControls(footerElem);

    rootElem.css({ 'max-height': panel.scroll ? getTableHeight() : '' });
  }

  // hook up link tooltips
  elem.tooltip({
    selector: '[data-link-tooltip]'
  });

  function addFilterClicked(e) {
    const filterData = $(e.currentTarget).data();
    const options = {
      datasource: panel.datasource,
      key: data.columns[filterData.column].text,
      value: data.rows[filterData.row][filterData.column],
      operator: filterData.operator
    };

    ctrl.variableSrv.setAdhocFilter(options);
  }

  elem.on('click', '.table-panel-page-link', switchPage);
  elem.on('click', '.table-panel-filter-link', addFilterClicked);

  var unbindDestroy = scope.$on('$destroy', () => {
    elem.off('click', '.table-panel-page-link');
    elem.off('click', '.table-panel-filter-link');
    unbindDestroy();
  });

  ctrl.events.on('render', (renderData) => {
    data = renderData || data;
    if (data) {
      renderPanel();
    }
    ctrl.renderingCompleted();
  });
}

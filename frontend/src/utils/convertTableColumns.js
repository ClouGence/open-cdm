export function convertTableColumns(columns = []) {
  return columns.map((col, index) => {
    const key = col.key || col.dataIndex || col.slot || `col-${index}`;
    return {
      title: col.title,
      dataIndex: col.key || col.dataIndex,
      key,
      width: col.width || col.minWidth,
      align: col.align,
      fixed: col.fixed,
      ellipsis: col.ellipsis,
      __slot: col.slot
    };
  });
}

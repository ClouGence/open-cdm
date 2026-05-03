const ELEMENT_TYPE_MAP = {
  Instance: 'INSTANCE',
  Catalog: 'CATALOG',
  Schema: 'SCHEMA',
  Table: 'TABLE'
};
const ELEMENT_TYPE_REF_MAP = {
  Instance: 'instanceTree',
  CATALOG: 'catalogTree',
  SCHEMA: 'schemaTree',
  TABLE: 'tableTree'
};
const ELEMENT_REVERSE_TYPE_MAP = {
  INSTANCE: 'Instance',
  CATALOG: 'Catalog',
  SCHEMA: 'Schema',
  TABLE: 'Table'
};

// 从CATLOG/SCHEMA开始记录其names
const START_RECORD_NAMES_CONUT = 2;

export { ELEMENT_TYPE_MAP, ELEMENT_TYPE_REF_MAP, ELEMENT_REVERSE_TYPE_MAP, START_RECORD_NAMES_CONUT };

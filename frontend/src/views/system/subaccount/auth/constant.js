const ELEMENT_TYPE_MAP = {
  Instance: 'INSTANCE',
  INSTANCE: 'INSTANCE',
  Catalog: 'CATALOG',
  CATALOG: 'CATALOG',
  EXTERNAL_CATALOG: 'CATALOG',
  Schema: 'SCHEMA',
  SCHEMA: 'SCHEMA',
  EXTERNAL_SCHEMA: 'SCHEMA',
  Table: 'TABLE',
  TABLE: 'TABLE',
  Column: 'COLUMN',
  COLUMN: 'COLUMN',
  View: 'VIEW',
  VIEW: 'VIEW',
  Materialized: 'MATERIALIZED',
  MATERIALIZED: 'MATERIALIZED',
  Sequence: 'SEQUENCE',
  SEQUENCE: 'SEQUENCE',
  Synonym: 'SYNONYM',
  SYNONYM: 'SYNONYM',
  Function: 'FUNC',
  FUNC: 'FUNC',
  Procedure: 'PROC',
  PROC: 'PROC',
  Trigger: 'TRIGGER',
  TRIGGER: 'TRIGGER',
  Key: 'KEY',
  KEY: 'KEY'
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
  TABLE: 'Table',
  COLUMN: 'Column',
  VIEW: 'View',
  MATERIALIZED: 'Materialized',
  SEQUENCE: 'Sequence',
  SYNONYM: 'Synonym',
  FUNC: 'Function',
  PROC: 'Procedure',
  TRIGGER: 'Trigger',
  KEY: 'Key'
};

const AUTH_ELEMENT_TYPES = [
  'Instance',
  'Catalog',
  'Schema',
  'Table',
  'Column',
  'View',
  'Materialized',
  'Sequence',
  'Synonym',
  'Function',
  'Procedure',
  'Trigger'
];

// From CATLOG/SCHEMA to record their names
const START_RECORD_NAMES_CONUT = 2;

export { AUTH_ELEMENT_TYPES, ELEMENT_TYPE_MAP, ELEMENT_TYPE_REF_MAP, ELEMENT_REVERSE_TYPE_MAP, START_RECORD_NAMES_CONUT };

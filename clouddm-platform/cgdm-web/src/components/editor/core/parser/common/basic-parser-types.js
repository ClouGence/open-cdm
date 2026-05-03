/**
 * Syntax context type at caret position
 */
export var SyntaxContextType;
(function (SyntaxContextType) {
    /** catalog name */
    SyntaxContextType["CATALOG"] = "catalog";
    /** catalog name that will be created  */
    SyntaxContextType["CATALOG_CREATE"] = "catalogCreate";
    /** database name path, such as catalog.db */
    SyntaxContextType["DATABASE"] = "database";
    /** database name path that will be created  */
    SyntaxContextType["DATABASE_CREATE"] = "databaseCreate";
    /** table name path, such as catalog.db.tb */
    SyntaxContextType["TABLE"] = "table";
    /** table name path that will be created */
    SyntaxContextType["TABLE_CREATE"] = "tableCreate";
    /** view name path, such as db.tb.view */
    SyntaxContextType["VIEW"] = "view";
    /** view name path that will be created */
    SyntaxContextType["VIEW_CREATE"] = "viewCreate";
    /** function name */
    SyntaxContextType["FUNCTION"] = "function";
    /** function name that will be created */
    SyntaxContextType["FUNCTION_CREATE"] = "functionCreate";
    /** procedure name */
    SyntaxContextType["PROCEDURE"] = "procedure";
    /** procedure name that will be created */
    SyntaxContextType["PROCEDURE_CREATE"] = "procedureCreate";
    /** column name */
    SyntaxContextType["COLUMN"] = "column";
    /** column name that will be created */
    SyntaxContextType["COLUMN_CREATE"] = "columnCreate";
    SyntaxContextType["SCHEMA"] = "schema";
    SyntaxContextType["SCHEMA_CREATE"] = "schemaCreate";
    SyntaxContextType["PARTITION"] = "partition";
    SyntaxContextType["Key"] = "key";
})(SyntaxContextType || (SyntaxContextType = {}));

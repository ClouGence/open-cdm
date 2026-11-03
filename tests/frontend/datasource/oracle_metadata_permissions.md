# Oracle metadata with ordinary database accounts

## Purpose

Prevent ORA-00942 when ordinary Oracle users browse table or view columns. Keep database visibility restrictions intact and avoid requiring catalog privileges for routine metadata.

## Scope

- Oracle column queries for the legacy and current version branches, column comments, private DBLinks and DBMS_JOB details.
- Oracle role metadata: accounts unable to read DBA_ROLES see no role entries/details; unrelated SQL errors still propagate. Administrators retain role visibility.
- The equivalent YashanDB column/job SQL is adapted, but requires separate live verification against a YashanDB server.
- No SQL parser, business permission, frontend layout or API contract changes.

## Preconditions

- Local CloudDM Console and Worker are ready; use the in-app Browser and an existing account allowed to manage disposable datasources.
- Use an explicitly authorized Oracle test database. Obtain host, port, service and credentials through local private configuration; do not put credentials in this document or screenshots.
- Verify the actual Oracle server version. Do not equate successful compilation or running a legacy projection on a newer server with testing an 11g server.
- Build the changed Oracle common module and all plugin consumers. Verify nested common JAR contents and deploy the matching plugin packages to the actual runtime plugin directory; restart the exact IDEA configuration using pika-idea-control.

## Test Data

- Create uniquely named owner and reader users in a test PDB. Grant CREATE SESSION to both, object creation only to the owner, and a bounded quota in the test tablespace. A reader-owned table for the owner-filter check additionally requires CREATE TABLE and a bounded quota for the reader.
- Neither ordinary user may have DBA, SELECT_CATALOG_ROLE or SELECT ANY DICTIONARY. Verify SESSION_ROLES and SESSION_PRIVS and confirm that reading SYS.DBA_TAB_COLS is denied.
- Owner table C425_COLUMNS: identity NUMBER primary key, LABEL VARCHAR2(40 CHAR) DEFAULT 'hello' NOT NULL, AMOUNT NUMBER(10,2), NOTE NVARCHAR2(20), and a virtual DOUBLED column based on AMOUNT. Add a Chinese column comment to LABEL and a function-based index on UPPER(LABEL) to produce a hidden system column.
- Owner view C425_VIEW selects ID and LABEL. Grant SELECT on this table and view to the reader. Create an ungranted C425_PRIVATE table, and a reader-owned C425_COLUMNS table with a single READER_ONLY column.
- Optional owner-only fixtures: an unused private connected-user DBLink with a non-resolving test connect identifier, and a DBMS_JOB scheduled far in the future with a no-op body. Do not open the DBLink or run the job.
- Register a disposable Oracle datasource in local CloudDM using the ordinary reader. Record its unique name and both fixture schemas without recording its password.

## Suites

### ORA-META-01 Ordinary account table structure

- Priority/risk: P0; ordinary connection success must not be followed by dictionary access failure.
- Initial state: datasource form with the isolated reader, correct service and compatible installed JDBC driver; no business datasource edits.
- Preparation: create the fixtures above. For before/after comparison retain the original plugin package outside the repository.
- Steps: test the connection and save the disposable datasource. Enter /#/sql, expand it, open the owner schema, right-click C425_COLUMNS and select Design table. Open Columns and LABEL details. Repeat after loading the patched plugin.
- Expected: the original plugin reproduces ORA-00942. The patched plugin loads the real table name and five visible columns in order: ID, LABEL, AMOUNT, NOTE, DOUBLED. Hidden SYS_NC columns are absent. Chinese comment, primary/non-null flags, default 'hello', 40-character length and precision 10/scale 2 match the database. No previewed change is submitted.
- Recovery: cancel the detail dialog and do not save table changes.

### ORA-META-02 Visibility, view and owner boundaries

- Priority/risk: P0; replacing DBA views must not expose ungranted objects or mix same-named tables across schemas.
- Initial state: reader datasource connected, SQL pane for the owner schema.
- Steps: inspect the authorized view's two columns. Open the reader schema and inspect its single-column same-named table. Check that C425_PRIVATE is absent from reader-visible tables. Repeat through the production metadata provider with the same ordinary connection; compare with an administrator connection.
- Expected: only granted and owned objects are visible; owner/reader table columns are not mixed. Table/view metadata remains available to administrators. Loading an absent table through the table-detail path returns no table.
- Recovery: leave the fixture schemas unchanged.

### ORA-META-03 Restricted administration metadata

- Priority/risk: P1; missing role dictionary privileges must not tear down a usable query workspace.
- Initial state: ordinary reader query window, connection usable.
- Steps: select Roles in the object menu, then return to Tables and refresh. Check list/detail role methods with an administrator. With the ordinary owner fixture, check DBLink and Job list/detail metadata without executing either resource.
- Expected: a reader without DBA_ROLES access gets no visible role entries/details and can continue browsing tables. An administrator can still list/read roles. The owner can read its private DBLink and future job using accessible dictionary views. SQL errors unrelated to dictionary permission denial are not suppressed.
- Recovery: restore the Tables object type; do not trigger a job or follow a DBLink.

### ORA-META-04 Repeat, lifecycle and failures

- Priority/risk: P1; refreshed or reopened windows must use the same permissions and newly deployed plugin.
- Initial state: patched plugin loaded, fixture query and structure tabs open.
- Steps: refresh the page, reopen the table structure, repeatedly switch Tables/Views/Roles and return to Tables. Execute only SELECT USER FROM DUAL in a fixture query tab. Verify driver/connection failures remain distinguishable from empty role visibility.
- Expected: column metadata remains available, ordinary roles remain unavailable without a connection-failed banner, query execution works, and original user SQL drafts remain unchanged.
- Boundaries: empty/ungranted table results and same-name owner boundaries are covered above; batch/concurrent DDL, write SQL and extreme-volume testing are outside this read-only metadata patch.
- Recovery: close only fixture tabs or retain them explicitly for user review.

## Cleanup

- Cancel editor dialogs without applying DDL. Remove only the disposable datasource and users created by this run when review is complete; dropping those users also removes their fixture objects and future jobs.
- Never remove shared users, datasource configurations or existing data. Preserve unrelated SQL tabs and drafts.
- Stop only database containers started for this task, retaining their volumes. Private temporary credentials and original plugin backups stay outside Git.

## Skip Conditions

- No ordinary account, admin fixture preparation access, matching runtime artifact, Browser login or authorized database: mark the dependent scenario blocked, not passed.
- No 11g/YashanDB/other Oracle-family server: record those live compatibility gaps separately from successful compilation and SQL inspection.
- The shared loadColumns helper currently throws a null-pointer exception when explicitly asked for a nonexistent table. Record that separate pre-existing edge case; do not use it to claim successful empty-column behavior or expand this patch into the shared metadata helper.

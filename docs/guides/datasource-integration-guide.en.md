# open-cdm Data Source Integration Guide

Use this guide to add a data source or complete an existing implementation. It covers shared steps and considerations only. Keep database-specific versions, differences, development progress, and acceptance results in `tests/datasource/<name>/`, and UI test procedures in `tests/frontend/`. This guide has not been converted into a skill.

The steps below are organized around relational data sources. For non-relational sources, reuse the scope, plugin, connection, permission, UI, and delivery steps. Adapt the SQL, schema, transaction, and editing sections to the actual protocol instead of forcing a JDBC model. Explain why any capability is excluded.

## How to Use This Guide

- Inspect before implementing. At each step, trace “existing implementation → caller → UI entry point → actual behavior.” A class or SPI being present does not mean the capability is complete.
- Reuse existing database families and platform workflows, implementing only actual differences. Protocol compatibility does not guarantee identical metadata, types, permissions, or DDL.
- When changing shared interfaces or execution paths, identify affected data sources and run regression checks. Evaluate shared performance, queue, and state-machine issues separately to avoid expanding the integration into a broader redesign.
- Track implementation progress separately from acceptance results. Continue independent development when an environment is unavailable, but keep unexecuted cases as `NOT RUN`. Making an entry point visible does not mean product acceptance has passed.

## Integration Steps

### 1. Define the Scope and Validation Baseline

- **Work**: Specify the database product, exact version, compatibility mode, deployment model, driver version, authentication method, and initial capability scope. Prepare an isolated environment, regular and administrative accounts, and disposable sample data.
- **Considerations**: Evaluate cloud services and self-hosted deployments separately. Versions listed in the driver catalog are not automatically certified. Decide separately whether querying, editing, governance, tickets, and CI/CD are in scope; do not implicitly promise every advanced object type.
- **Acceptance**: Create a capability matrix covering at least “capability/entry point, implementation location, support boundaries, version combination, validation method, result/evidence.” Update it throughout the work instead of defining test coverage at the end.

### 2. Inventory Existing Implementations and Reuse Boundaries

- **Work**: Inspect product enums, plugin modules, drivers, SQL engines, frontend entry points, and test assets. Select the closest existing family and identify what must change, what can be reused, and what is out of scope.
- **Considerations**: For existing but hidden implementations, investigate why they were hidden, empty implementations, inherited defaults, and frontend type-specific branches. Preserve stable product identifiers, driver families, engine names, and configuration formats so existing connections continue to work.
- **Acceptance**: Produce a difference checklist supported by source code. New integrations should not duplicate existing capabilities; completion work should not reimplement historical gaps that have already been fixed.

### 3. Register Modules, Plugins, and SQL Engines

- **Work**: Create `clouddm-ds/ds-<name>` as needed and register it in `settings.gradle`. Configure `DataSourceType`, `DsType`, `@Plugin`, `DsPluginBinder`, type mappings, and the required SPIs.
- **Considerations**: Prefer placing an independent dialect in `clouddm-sql/sql-<name>`, with the data source module depending on it. When reusing an engine, bind its actual name. Check service discovery files, `includePackages`, shared dependencies, and plugin class-loading boundaries to avoid duplicate driver packaging or inconsistent parsers.
- **Acceptance**: Module compilation, plugin packaging, and plugin discovery succeed. Product identifiers, serialization providers, driver factories, and engine bindings correspond correctly; their names do not all need to be identical.

### 4. Connect Drivers, Configuration, and Secure Connections

- **Work**: Implement the configuration model, dynamic form, serialization, and `DsFactory`. Configure `drivers.xml` and the factory service declaration, and map address, account, database/schema, timeout, TLS/SSH, and related properties.
- **Considerations**: Check units, encoding, default ports, and precedence between a custom URL and other fields. Certificate files must be available on the actual execution node. Do not log credentials or URLs containing sensitive parameters. A `compileOnly` dependency does not mean the runtime driver is installed.
- **Acceptance**: Creating, saving, reopening, editing, and testing connections work through the product. Invalid credentials, unreachable ports, slow connections, and invalid certificates fail as expected; initialization failures release connections. Online preparation and offline delivery procedures are defined.

### 5. Align Session, Transaction, and Resource Lifecycles

- **Work**: Reuse session factories, Session/Hooks, and resource managers. Adapt initial context, auto-commit, isolation, read-only mode, parameter binding, update counts, multiple result sets, streaming reads, and cancellation.
- **Considerations**: Define the relationship between query tabs, platform Sessions, and physical connections, including when they are closed or recreated. DDL commit boundaries, read-only behavior, and isolation levels must match the target implementation. Do not blindly retry writes with unknown commit outcomes or rely on transparent reconnection to preserve session state.
- **Acceptance**: Use two connections to verify commit/rollback visibility and session parameters; with two sessions, cancellation affects only the intended target. Cover repeated/late cancellation, interrupted fetching, disconnections, and initialization/close failures. Preserve original errors, reset execution state, and release connections, Statements, ResultSets, and background tasks.

### 6. Align Metadata, Object Paths, and Types

- **Work**: Implement `MetaService` and metadata providers, define the catalog/database/schema hierarchy, and cover the initial set of objects, columns, defaults, comments, keys, indexes, routine parameters, and type mappings.
- **Considerations**: Distinguish the connection target from context levels that can be switched within a connection. Object paths must agree with authorization and lineage. Do not join system views using only unqualified object names; constraint aggregation must preserve object identity and column order. Do not invent mappings for unknown types or assume an indexed object is always a table.
- **Acceptance**: Verify regular-account visibility, identically named objects across databases/schemas, multicolumn constraints, quoting, and case handling. Check numeric precision, time zones/temporal precision, Unicode, binary values, LOBs, and NULL. The object tree and details match the actual database structure.

### 7. Complete the SQL Dialect and Language Services

- **Work**: Implement or reuse `Dialect`, `DslProvider`, `SplitAnalysisSpi`, `DsLanguageSpi`, and `RewriteSpi`. Provide keywords, completion, validation, statement splitting, formatting, and query-limit rewriting.
- **Considerations**: Statement splitting, structural validation, semantic analysis, and formatting have separate responsibilities and support boundaries. Do not replace routine-body parsing with simple regular expressions or splitting on semicolons. Completion must respect authorization scope; editor positions must account for Unicode/UTF-16 differences.
- **Acceptance**: Cover quoting, semicolons inside comments/strings, routine bodies, nested blocks, CTEs, set queries, and invalid input. Rewriting preserves semantics and existing stricter limits; pagination neither duplicates nor omits rows. Formatting preserves meaning, supports undo, is stable when repeated, and retains the original text on failure.

### 8. Integrate Behavior Analysis, Authorization, SQL Review, Lineage, and Masking

- **Work**: Integrate `BehaviorAnalysisSpi`, `SecDomainResolveSpi`, rule support, and `LineageAnalysisSpi` so objects read, written, or invoked, along with output-column origins, enter the platform's governance pipeline.
- **Considerations**: Validate platform authorization separately from database-account permissions. Define handling for dynamic SQL, unanalyzable statements, columns without known origins, and routines returning multiple result sets. Analysis failure must not be treated as no resource access or a successful SQL review. Also check rule degradation caused by missing SPIs.
- **Acceptance**: Restricted accounts cannot bypass authorization through aliases, subqueries, cross-schema access, complex DML, or routine calls. Rules detect and block matching statements. Multitable queries, expressions, wildcards, views, partial masking exemptions, and result exports do not incorrectly remove masking.

### 9. Complete Structure Editing and Object Scripts

- **Work**: Align `EditorProvider` / `SqlBuilder`, UI panels, DDL conversion, and object scripts. Cover table creation/alteration, keys/indexes, and other object operations within scope.
- **Considerations**: Check menus, metadata, and SQL generators together. A template is not a retrieved definition of an existing object. An unchanged structure should produce no DDL; clearing a comment/default must be distinguished from leaving it unchanged. Reject or clearly flag lossy conversions instead of silently dropping properties.
- **Acceptance**: Complete “preview → execute → refresh and read back,” checking column order, types, constraints, and defaults. Verify equivalent reconstruction from scripts in an isolated environment. Unimplemented property/editing entry points must not show empty pages or false success.

### 10. Complete Data Editing, Import, and Export

- **Work**: Establish safe identification of the original row, and connect value conversion, batch editing, partial import failure handling, paginated/streaming export, and SQL replay.
- **Considerations**: Prohibit updates/deletes when a row cannot be identified uniquely. Changes to key values must still locate the original row. The frontend and shared INSERT builder must distinguish missing fields, explicit NULL, and actual values. Keep dialect-specific syntax, such as inserts using only defaults, in the data source implementation. Enforce generated-column and read-only-object restrictions.
- **Acceptance**: Composite-key operations modify only the intended row. High-precision numbers, temporal values, binary values, Unicode, and LOBs survive round trips. Exports neither omit nor duplicate data and do not bypass masking. Cancellation/disconnection releases resources, partial failures are reported accurately, and truncated display values are not presented as complete exported data.

### 11. Connect Execution Plans, Tickets, and CI/CD

- **Work**: Adapt native plan generation, retrieval, and cleanup. Verify the platform's existing analysis, approval, execution, and result-display workflows.
- **Considerations**: Plan analysis must not execute the original write statement, and concurrent requests must not mix plans. Label native plans separately from statement-based inference, and distinguish zero estimates from unknown estimates. For transactions mixing DDL and other statements, partial commits, retries, and repeated scheduling, compare database effects with platform state.
- **Acceptance**: Insufficient permissions for regular accounts and plan-retrieval failures are reported. Temporary records are cleaned up on success and failure. Complete an actual ticket workflow. If a shared issue is deferred, record its scope, impact, and follow-up separately; completing the plugin does not resolve that issue.

### 12. Align UI Capabilities and Internationalization

- **Work**: Check names/icons, grouping, connection forms, object menus, editors, toolbars, rules, and export entry points. Reuse shared components and backend capability descriptions.
- **Considerations**: Trace each capability through “SPI → backend response → button/menu → actual call,” avoiding capabilities hard-coded by data source type. Put frontend text in `frontend/src/locales/` and plugin text in backend i18n resources. Returning true alone does not establish support.
- **Acceptance**: Chinese and English interfaces, common desktop/narrow layouts, tab switching, and tab restoration work correctly. Unsupported features are disabled, hidden, or clearly explained. Complete the Web build and browser checks required by repository rules, and regression-test other data sources when shared components change.

### 13. Validate Release Packages, Expose the Entry Point, and Hand Over

- **Work**: Validate agreed versions and deployment modes. Check `customFatJar`, SQL/shared dependencies, service registrations, and resources. Choose online download, offline provisioning, or bundled drivers, and adjust `@Plugin.display` as agreed.
- **Considerations**: `drivers.xml` and `built-in-drivers.xml` serve different purposes; offline availability does not imply bundling. When bundling a driver, check its version, artifact coordinates, and distribution terms. Each execution node in Console/Sidecar deployments needs the required drivers and certificates; checking only the development classpath is insufficient.
- **Acceptance**: Actual installation packages load plugins, establish connections, recover after restart, and read existing configurations. Complete queries and agreed management/governance workflows through the normal add-data-source entry point. Record versions, artifact hashes, and uncovered cases. If the entry point is exposed early, record the explicit decision without changing unexecuted test statuses.

## Validation and Delivery Conventions

| Evidence level | What it establishes | What it cannot replace |
| --- | --- | --- |
| Compilation, existing unit tests, offline parsing/proxy probes | Code, local contracts, and sample behavior | Real database and product workflows |
| Direct driver connections | Specific driver and database behavior | Platform configuration, authorization, sessions, and execution-node integration |
| Product tests against a real database and UI checks | User workflows for a specific combination | Other versions, accounts, or deployment models |
| Installation/restart of actual release packages | Delivery and recovery for specific artifacts | Uncovered capabilities and untested versions |

- Record PASS, PARTIAL, FAIL, NOT RUN, or BLOCKED in the dedicated matrix, with supporting evidence. `NO-SOURCE` does not mean tests passed. State implementation/product boundaries for unsupported capabilities; do not classify missing implementation as “not applicable.”
- Reuse and extend existing samples and tests; do not add test classes without an explicit user request. Expand regression coverage to callers of changed shared modules instead of testing only the new data source.
- Keep reusable UI procedures limited to actions, assertions, and cleanup steps; put results in the dedicated matrix. Agents use @Browser according to `frontend/AGENTS.md`; record the handoff when the user arranges manual acceptance testing.
- Deliver the support scope, deployment/driver-preparation instructions, capability matrix, and repeatable test procedures. Do not commit credentials, vendor binaries, runtime data, or generated logs. Adding a data source does not automatically require new application tables or changes to historical migrations.

Common commands are shown below. Replace `<name>` and run each command independently from the repository root. Repository rules govern any additional requirements:

```bash
cd backend && ./gradlew ':ds-<name>:build' ':ds-<name>:customFatJar'
cd backend && ./gradlew ':sql-<name>:test'  # Independent SQL modules only
cd backend && ./gradlew :s-test:test      # Shared data source test module
cd package && ./all_build.sh web         # Must succeed after frontend source changes
cd package && ./package.sh --build       # Release packaging; not a substitute for testing
```

## Repository Entry Points

| Purpose | Location |
| --- | --- |
| Modules and identifiers | [settings.gradle](../../backend/settings.gradle), [DataSourceType](../../backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/base/metadata/ds/DataSourceType.java), [DsType](../../backend/clouddm-utils/cg-schema/src/main/java/com/clougence/schema/DsType.java) |
| Data source and SQL implementations | [clouddm-ds](../../backend/clouddm-plugins/clouddm-ds/), [clouddm-sql](../../backend/clouddm-plugins/clouddm-sql/), [Plugin SDK](../../backend/clouddm-platform/cgdm-plugin-sdk/) |
| Types and structure conversion | [cg-schema](../../backend/clouddm-utils/cg-schema/) |
| UI data source lists and editor capabilities | [DmHomeController](../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/controller/system/DmHomeController.java), [QueryEditorController](../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/controller/editor/query/QueryEditorController.java) |
| Packaging and bundled drivers | [Plugin build conventions](../../backend/buildSrc/src/main/groovy/com.clougence.plugin-conventions.gradle), [package/pkg](../../package/pkg/), [built-in-drivers.xml](../../package/pkg/builtin-drivers/built-in-drivers.xml) |
| Testing and engineering rules | [Data source test conventions](../../tests/datasource/README.md), [Frontend test procedure index](../../tests/frontend/README.md), [AGENTS.md](../../AGENTS.md), [frontend/AGENTS.md](../../frontend/AGENTS.md) |

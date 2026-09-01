/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.sdk.sql.analysis.behavior;

/**
 * Semantic action between one behavior subject and one or more behavior objects.
 *
 * <p>An action only describes what the statement does. Authorization requirements are derived
 * from both the action and its resource.</p>
 */
public enum BehaviorAction {

    // Object definition actions.
    CREATE, // Creates a database object or resource.
    ALTER,  // Changes the definition, structure, or attributes of an object or resource.
    DROP,   // Removes a database object or resource.
    RENAME, // Renames a database object or resource.

    // Data access and mutation actions.
    READ,    // Reads data, metadata, status, or configuration information.
    INSERT,  // Inserts new data records.
    UPDATE,  // Updates existing data records.
    DELETE,  // Deletes existing data records.
    MERGE,   // Merges data by inserting or updating matching records.
    REPLACE, // Replaces existing data or creates new data when necessary.
    IMPORT,  // Imports external data into the current database or instance.
    EXPORT,  // Exports data from the current database or instance.

    // Program execution actions.
    CALL,  // Invokes a function, procedure, or another executable program object.
    RAISE, // Raises or re-raises a database condition or exception.

    // Authorization actions.
    GRANT,    // Grants privileges to a user or role.
    REVOKE,   // Revokes privileges from a user or role.
    TRANSFER, // Transfers privileges, ownership, or authorization relationships.

    // Resource placement and locking actions.
    COPY,   // Copies data, objects, or resources while preserving the source.
    MOVE,   // Moves data, objects, or resources to another location or owner.
    LOCK,   // Locks an object, resource, session, or instance.
    UNLOCK, // Releases a lock from an object, resource, session, or instance.

    // Configuration and state transition actions.
    CONFIGURE, // Changes system, session, instance, or resource configuration.
    SWITCH,    // Switches the active role, resource group, operating mode, or state.
    RESET,     // Resets state, configuration, log positions, or counters.

    // Object maintenance actions.
    ANALYZE,  // Analyzes an object and collects statistics.
    CHECKSUM, // Calculates a checksum for data or an object.
    OPTIMIZE, // Optimizes a table, index, or another database object.
    REPAIR,   // Repairs a damaged or inconsistent database object.
    VALIDATE, // Validates the correctness and consistency of data, objects, or resources.

    // Instance, storage, and runtime maintenance actions.
    APPLY,      // Applies logs, changes, or pending content.
    CHECKPOINT, // Triggers a database or instance checkpoint.
    FLUSH,      // Flushes caches, logs, or pending state to their destination.
    LOAD,       // Loads indexes, caches, or other runtime resources.
    PURGE,      // Purges obsolete logs, historical data, or resources.
    REFRESH,    // Refreshes materialized results, caches, or object state.
    RECOVER,    // Recovers a database from logs, media, or another recovery source.
    RESTORE,    // Restores data or objects from a backup, snapshot, or historical version.

    // Runtime lifecycle actions.
    START,     // Starts a service, replication process, task, or another runtime unit.
    STOP,      // Stops a service, replication process, task, or another runtime unit.
    TERMINATE, // Forcefully terminates a session, process, operation, or task.

    // Safety valve actions.
    UNSAFE,  // Marks a recognized behavior that is explicitly known to be high risk.
    UNKNOWN; // Marks behavior whose semantics are unclear or not yet classified.
}

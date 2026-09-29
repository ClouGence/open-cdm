# Compose and TGZ Upgrades

## Docker Compose

1. Resolve the active project name and config files from `docker compose ls` and container labels. Work from the existing installation directory.
2. Back up the current Compose files, protected environment files, logical database, configuration, and persistent data. Record current service health and table inventory.
3. Download the target Compose file from the verified release without overwriting the old file.
4. Run `docker compose config` for both versions. Compare images, services, ports, networks, project name, and named volumes. Preserve custom values rather than replacing them with release defaults.
5. Pull the target images before stopping the old services.
6. Use the same project name for cutover. Stop with `down` only; never use `-v`. Start the target file with `up -d`.
7. Validate version, container state, database inventory, HTTP, logs, and repeated stability probes. Keep the old Compose file and backups.

If the new file changes service or volume names, stop and resolve the mapping before cutover.

## TGZ

1. Identify whether the installation is Alone or Console plus Sidecar. Record install paths, current version, ports, process IDs, config locations, database settings, and startup commands without printing secrets.
2. Download Console and Sidecar artifacts from the same verified release. Never mix releases.
3. Back up the application database logically, configuration directories, data directories, and current installation directories.
4. Stop Sidecar before Console. For Alone, use its own shutdown script.
5. Extract the target packages into new directories, restore configuration intentionally, and preserve the old directories under versioned names.
6. Run the target database initialization/upgrade script once. Do not modify historical migrations or repeatedly rerun a failed migration without diagnosis.
7. Start Console before Sidecar, or start Alone with its packaged startup script.
8. Validate processes, target version, database inventory, HTTP, logs, and repeated stability probes.

If paths, scripts, or topology do not match these patterns, stop and ask for the environment-specific runbook.

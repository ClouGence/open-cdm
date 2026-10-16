# Direct Docker Alone Upgrade

Use this workflow only when preflight confirms a directly managed `cgdm-alone` container with persistent mounts.

## Prepare without downtime

1. Record the current image, version, container state, restart policy, network, port bindings, mount destinations, application environment key names, HTTP status, database status, and database table count.
2. Build the target image name from the verified release/CD output and the appropriate registry. Pull it while the old container remains online.
3. Inspect the pulled image digest and verify its embedded version with an overridden entrypoint. Do not attach production volumes during this check.
4. Confirm enough free space remains for the image and backups.

## Back up while online

Create a unique root-owned backup directory outside the Docker data filesystem when possible. Store:

- `docker inspect` output with mode `600`;
- an env file with mode `600`, containing only application keys matching `APP_*`, `DB_*`, and `MYSQL_*`;
- the current version and table count;
- a compressed logical dump of the application database using credentials inside the container without printing them;
- compressed archives of configuration, application data, and logs.

Verify every expected backup exists and is non-empty. Keep the old container running if backup creation fails.

## Cut over

1. Stop the old container with a bounded graceful timeout.
2. Archive the stopped embedded-MySQL volume and write a checksum. This is the consistent raw rollback copy.
3. Rename the old container to a unique versioned backup name instead of deleting it.
4. Start the target image under the original container name, recreating the exact network, restart policy, ports, and named-volume destinations.
5. Load the protected application env file. Do not carry forward base-image variables such as `PATH`, `JAVA_HOME`, `JAVA_VERSION`, or locale defaults.
6. If container creation itself fails before the new image touches the volumes, rename and restart the old container. Otherwise preserve logs and assess database compatibility before rollback.

Never delete or recreate named volumes during cutover.

## Validate

- Wait for the container to remain `running`, the persisted version file to equal the target, and HTTP to return success.
- Confirm embedded MySQL is alive and the table count matches the pre-upgrade value.
- Inspect current-container logs for startup completion, worker heartbeat, migrations, errors, and exceptions.
- Compare suspicious messages with logs from the preserved old container.
- Check HTTP both on the remote loopback address and from the operator machine.
- Run three stability probes over about 40 seconds and require zero unexpected restarts.

Keep the backup directory and stopped old container in the final handoff.

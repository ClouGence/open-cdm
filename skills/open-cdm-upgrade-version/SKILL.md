---
name: open-cdm-upgrade-version
description: Upgrade and validate a remote Open-CDM testing environment over SSH, including release/CD gating, deployment detection, backups, cutover, health checks, and rollback preservation. Use when asked to upgrade, update, deploy, or refresh Open-CDM/CloudDM on a test server. Require the user to explicitly provide both the SSH login username and host in the current request; never infer or reuse either value from history.
---

# Open CDM Upgrade Version

## Enforce the SSH target gate

1. Read the SSH username and host only from the current user request. Accept either `user@host` or separately named values.
2. If either value is missing, stop before any network call and ask for both in one concise question.
3. Never recover the username or host from conversation history, SSH config, environment variables, repository files, previous deployments, or remembered servers.
4. Reject values containing newlines or beginning with `-`. Do not interpolate untrusted values into a remote shell command.
5. Treat the target as a test environment only. If inventory clearly indicates production or the environment identity is ambiguous, pause and obtain explicit confirmation before mutation.

## Resolve the target release

- If the user supplies a GitHub Actions/CD run, monitor it with `gh run view` or `gh run watch --exit-status`. Start the server upgrade only after the entire run completes successfully. Stop on cancellation, failure, or missing artifacts.
- If the user supplies a version, use that exact version after verifying the release or image exists.
- Otherwise resolve the current stable GitHub Release and state that choice before mutation. Browse or query GitHub because release state is time-sensitive.
- Derive artifact and image names from the successful workflow or release. Do not guess a registry or reuse a stale tag.

## Inspect before changing state

Run the bundled read-only preflight first:

```bash
scripts/remote_preflight.sh --user <ssh-user> --host <ssh-host>
```

Confirm all of the following:

- current Open-CDM version and deployment form: direct Docker container, Docker Compose, or TGZ;
- container/process names, ports, mounts, restart policy, and Compose project identity;
- free space on both the Docker data filesystem and backup destination;
- current HTTP reachability and database availability;
- a concrete rollback path that preserves the current runtime and data.

Do not print environment values, passwords, tokens, JWT secrets, or full container inspection data to the conversation.

## Execute the matching workflow

- For a directly managed `cgdm-alone` Docker container, read and follow [references/docker-alone.md](references/docker-alone.md) completely.
- For Docker Compose or TGZ installations, read and follow [references/compose-and-tgz.md](references/compose-and-tgz.md) completely.
- If the installation does not match a documented workflow, stop after inspection and ask for direction. Do not improvise a destructive migration.

Send concise progress updates at least once per minute during CD waits, image pulls, backups, shutdown, migrations, and startup.

## Preserve safety invariants

- Pull and validate the target artifact before stopping the current service.
- Create a logical database backup plus configuration/data backups before cutover. Create a stopped raw database-volume backup when using embedded MySQL.
- Store backups on a filesystem with sufficient free space and protect files containing environment values with mode `600` or stricter.
- Preserve the old container or installation directory under a unique backup name. Do not delete it after a successful test upgrade.
- Never run `docker compose down -v`, delete named volumes, overwrite an only copy of configuration, or expose secret values.
- Preserve ports, mounts, networks, application environment, and restart policy. Do not copy base-image runtime variables such as `PATH` or `JAVA_HOME` into the new container.
- If the new version fails after a database migration, do not start the old version against the migrated database unless compatibility is established. Use the pre-upgrade database backup for rollback.

## Validate the result

Require all checks below before reporting success:

1. Reported runtime version equals the requested target.
2. Container/process remains running with no unexpected restart.
3. Database responds, and the schema/table inventory is consistent with the pre-upgrade snapshot.
4. Local and externally addressed HTTP checks succeed.
5. Recent logs contain a normal startup completion and heartbeat or equivalent readiness signal.
6. New errors are compared with the previous runtime logs so pre-existing warnings are not misclassified as regressions.
7. Repeat state, version, restart-count, and HTTP checks at least three times over roughly 40 seconds.

## Report the handoff

Return the SSH target, source and target versions, deployment method, artifact/image digest when available, validation results, backup path, preserved rollback container/directory, and any known warnings. Never include credentials or secret environment values.

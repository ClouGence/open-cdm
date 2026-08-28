---
name: open-cdm-change-version
description: Prepare an open-cdm release for a user-specified version by deriving changes from Git tags, generating bilingual release notes with community-contributor attribution, and updating current version references. Use for open-cdm version bumps or release preparation; do not use for unrelated projects or rewriting earlier release history.
---

# Open CDM Change Version

Prepare an open-cdm version from an exact Git range. The target version must come from the user's request; never infer it only from the current branch name.

## Establish the Release Range

1. Locate the intended repository, read its applicable instructions, and inspect the working tree before editing.
2. Refresh tags from the configured remote when current remote state is required. Do not switch branches merely to inspect a tag or release branch.
3. Prefer the exact target tag `v<target-version>` as the end ref. If the tag does not exist because release notes are being prepared before tagging, require an explicit end ref such as the current release branch or `HEAD`; do not silently substitute it.
4. Use the newest stable SemVer tag reachable from the end ref and lower than the target version as the default start tag. Stable release notes intentionally ignore intermediate `-rc` tags so the range remains cumulative. Override the start tag only when the user or release topology requires it.
5. Confirm the start tag is an ancestor of the end ref and that the end ref belongs to the checkout being edited.

Resolve the bundled scripts relative to this `SKILL.md`; do not assume a fixed installation or repository path.

## Collect Changes and Contributors

Run the read-only collector before changing version files:

```bash
python3 <resolved-skill-directory>/scripts/collect_release_changes.py \
  --repo <open-cdm-repository-root> \
  --version <target-version> \
  --github
```

The collector defaults to `v<target-version>` and chooses the previous stable tag. Use `--to-ref` for an explicitly approved pre-tag ref and `--from-tag` when the default previous tag is not the intended release boundary.

`--github` resolves each PR's login and `authorAssociation` through the authenticated GitHub CLI. Treat `OWNER`, `MEMBER`, and `COLLABORATOR` as project members. Treat `CONTRIBUTOR`, `FIRST_TIMER`, `FIRST_TIME_CONTRIBUTOR`, and `NONE` as community contributors, excluding bot accounts. If PR attribution cannot be resolved, do not guess from a display name or email; resolve it before finalizing the notes.

Commit messages and PR titles are source material, not release-note prose. Inspect commit bodies or diffs when a message is vague, and do not invent user-visible behavior. Re-run the collector with `--include-files` only when changed paths would help resolve an unclear commit.

## Generate Release Notes

Before drafting, read [references/release-notes.md](references/release-notes.md). Create or update only the target version's files:

- `docs/release-notes/v<target-version>/index.cn.md`
- `docs/release-notes/v<target-version>/index.en.md`

Map every material commit in the range to a note or record why it is intentionally omitted. Every included community contribution must mention and link the contributor in the same bullet. Do not silently omit a community-authored PR merely because it is documentation, maintenance, or difficult to classify.

Keep the Chinese and English files semantically aligned. Never modify earlier version directories as part of a new release.

## Update Version References

After both release-note files exist, dry-run the version updater, review its file list and counts, then apply it:

```bash
python3 <resolved-skill-directory>/scripts/change_open_cdm_version.py \
  --repo <open-cdm-repository-root> \
  --version <target-version> \
  --dry-run
```

Run the same command without `--dry-run` only when the scope is correct. The updater changes the Gradle main version, current-version tables, supported Docker image tags, and the release-note index. It inserts the index row only when both target release-note files exist.

Do not broaden a routine release to dependency properties, CI tuning, generated output, or historical release-note content. Do not commit, tag, switch branches, or push unless the user explicitly asks.

## Validate

- Review the full diff and ensure unrelated pre-existing changes remain untouched.
- Re-run the collector and account for every commit and every community contributor in the chosen range.
- Verify Chinese and English notes cover the same changes, PR links resolve to the correct repository, and each community `@username` links to that GitHub profile.
- Confirm the release-note index points to two existing files.
- Search current public documentation for stale version tables and supported image tags; ignore intentional historical versions.
- Report the start tag, end ref, commit count, community contributors, changed files, and validation performed.

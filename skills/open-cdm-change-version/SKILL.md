---
name: open-cdm-change-version
description: Generate bilingual open-cdm release notes, update version references, or prepare a complete release for a user-specified version. Use for release-note creation, version bumps, or release preparation in open-cdm; do not use for unrelated projects or rewriting earlier release history.
---

# Open CDM Change Version

Generate release notes and update public version references without broadening the user's requested scope. The target version must come from the user's request; never infer it only from the current branch name.

## Choose the Requested Scope

- **Release notes only:** derive changes from an exact Git range, create the Chinese and English release-note files, and add their index row. Do not change Gradle, README, deployment, FAQ, Docker, or `llms-full.txt` version references.
- **Version references only:** update the Gradle main version and public version references. Do not create release-note content. Index the version only when both local release-note files already exist.
- **Complete release:** generate the bilingual release notes first, then update version references and the release-note index.

When the request is ambiguous, preserve the narrower operation supported by its wording. A request to “add release notes” means release notes only; a request to “change/bump the version” means version references only; “prepare the release” means the complete release workflow.

## Establish the Release Range for Release Notes

Skip this section for a version-references-only request.

1. Locate the intended repository, read its applicable instructions, and inspect the working tree before editing.
2. Refresh tags from the configured remote when current remote state is required. Do not switch branches merely to inspect a tag or release branch.
3. Prefer the exact target tag `v<target-version>` as the end ref. If the tag does not exist because release notes are being prepared before tagging, require an explicit end ref such as the current release branch or `HEAD`; do not silently substitute it.
4. Use the newest stable SemVer tag reachable from the end ref and lower than the target version as the default start tag. Stable release notes intentionally ignore intermediate `-rc` tags so the range remains cumulative. Override the start tag only when the user or release topology requires it.
5. Confirm the start tag is an ancestor of the end ref and that the end ref belongs to the checkout being edited.

Resolve the bundled scripts relative to this `SKILL.md`; do not assume a fixed installation or repository path.

## Collect Changes and Contributors

Skip this section for a version-references-only request.

Run the read-only collector before drafting release-note files:

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

Skip this section for a version-references-only request.

Before drafting, read [references/release-notes.md](references/release-notes.md). Create or update only the target version's files:

- `docs/release-notes/v<target-version>/index.cn.md`
- `docs/release-notes/v<target-version>/index.en.md`

Map every material commit in the range to a note or record why it is intentionally omitted. Every included community contribution must mention and link the contributor in the same bullet. Do not silently omit a community-authored PR merely because it is documentation, maintenance, or difficult to classify.

Keep the Chinese and English files semantically aligned. Never modify earlier version directories as part of a new release.

## Update the Release-Note Index Only

For a release-notes-only request, update the index after both language files exist:

```bash
python3 <resolved-skill-directory>/scripts/change_open_cdm_version.py \
  --repo <open-cdm-repository-root> \
  --version <target-version> \
  --index-only \
  --dry-run
```

Review the dry run, then repeat without `--dry-run`. In this workflow both local files must exist, and `--index-only` never changes other version references.

## Update Version References

Skip this section for a release-notes-only request.

For a complete release, run this only after both release-note files exist. Dry-run the version updater, review its file list and counts, then apply it:

```bash
python3 <resolved-skill-directory>/scripts/change_open_cdm_version.py \
  --repo <open-cdm-repository-root> \
  --version <target-version> \
  --dry-run
```

Run the same command without `--dry-run` only when the scope is correct. The updater changes the Gradle main version, current-version tables, supported Docker image tags, and the release-note index. It inserts or repairs the index row only when both local release-note files exist.

For a version-references-only request with no local release-note pair, add `--skip-release-notes-index` to both the dry run and the applied command.

Do not broaden a routine release to dependency properties, CI tuning, generated output, or historical release-note content. Do not commit, tag, switch branches, or push unless the user explicitly asks.

## Validate

- Review the full diff and ensure unrelated pre-existing changes remain untouched.
- When release notes were requested, re-run the collector and account for every commit and every community contributor in the chosen range.
- When release notes were requested, verify Chinese and English notes cover the same changes, PR links resolve to the correct repository, and each community `@username` links to that GitHub profile.
- When the index was updated, confirm it points to two existing files.
- When version references were requested, search current public documentation for stale version tables and supported image tags; ignore intentional historical versions.
- Report only the range and validation relevant to the requested scope, along with all changed files.

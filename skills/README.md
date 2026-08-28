# Skills

This directory contains project-specific skills for maintaining open-cdm.

## Available Skills

| Skill | Purpose |
| --- | --- |
| [`open-cdm-change-version`](open-cdm-change-version/SKILL.md) | Prepare an open-cdm release from an exact Git range, generate bilingual release notes, attribute community contributors, and update current version references. |

## Usage

Invoke a skill with an explicit target version:

```text
$open-cdm-change-version 4.1.2
```

The skill keeps commit, tag, branch, and push operations outside the release-preparation workflow unless the user explicitly requests them.

## Structure

Each skill keeps its entry instructions in `SKILL.md`. Optional `agents/`, `references/`, and `scripts/` directories provide UI metadata, focused guidance, and deterministic helpers used by that skill.

When updating a skill, keep links relative to its directory, avoid generated output and credentials, and validate any changed helper scripts before submitting the change.

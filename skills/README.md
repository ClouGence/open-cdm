# Skills

This directory contains project-specific skills for maintaining open-cdm.

## Available Skills

| Skill | Purpose |
| --- | --- |
| [Open CDM Release Version](open-cdm-release-version/SKILL.md) (`open-cdm-release-version`) | Update a version and public references, generate bilingual release notes from commit messages, and credit verified community contributors. |

## Usage

Invoke a skill with an explicit target version:

```text
$open-cdm-release-version 4.3.0
```

By default, a version update also prepares Chinese and English release notes and updates their index. Explicit requests for release notes only or version references only keep that narrower scope.

The skill keeps commit, tag, branch, push, and PR operations outside the release-preparation workflow unless the user explicitly requests them.

## Structure

Each skill keeps its entry instructions in `SKILL.md`. Optional `agents/`, `references/`, and `scripts/` directories provide UI metadata, focused guidance, and deterministic helpers used by that skill.

When updating a skill, keep links relative to its directory, avoid generated output and credentials, and validate any changed helper scripts before submitting the change.

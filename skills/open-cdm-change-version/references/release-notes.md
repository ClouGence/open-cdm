# Open CDM Release Note Guide

Use the latest complete release-note pair in `docs/release-notes/` as the repository's live style reference. The `v4.1.0` pair is the baseline when a later complete pair is unavailable.

## Structure

Create two files with matching content and ordering:

| Chinese | English |
| --- | --- |
| `## 亮点` | `## Highlights` |
| `## 新增` | `## Added` |
| `## 优化` | `## Improved` |
| `## 修复` | `## Fixed` |

Use two to four concise highlights for the most important release themes. Highlights summarize outcomes and normally omit PR links. Omit an empty detail section rather than inventing content, but keep both languages structurally aligned.

## Turn Commits into Notes

- Describe user-visible capability, behavior, reliability, compatibility, or operator impact. Do not copy raw Conventional Commit prefixes into the note.
- Classify `feat` as Added when it introduces a capability and as Improved when it extends an existing one. Classify `fix` as Fixed. Use the actual behavior rather than the prefix when they disagree.
- Consolidate closely related commits into one coherent bullet, retaining all relevant PR links and community acknowledgements.
- Exclude release bumps, formatting-only changes, tests, and internal chores unless they materially affect users or contributors.
- Inspect the commit body or diff when the subject is too vague. If the behavior still cannot be established, surface the uncertainty instead of guessing.
- Account for every commit in the selected range. Keep a temporary coverage mapping while drafting when the range is non-trivial.

Use the repository slug detected from `origin` when building links. Match the existing release-note convention for PR or issue references:

```markdown
[#123](https://github.com/OWNER/REPOSITORY/issues/123)
```

## Community Attribution

Use PR metadata rather than Git author names or email addresses. A non-bot PR author with `authorAssociation` equal to `CONTRIBUTOR`, `FIRST_TIMER`, `FIRST_TIME_CONTRIBUTOR`, or `NONE` is a community contributor. `OWNER`, `MEMBER`, and `COLLABORATOR` are project members.

Every release-note bullet containing a community contribution must include the linked GitHub login before its PR links.

Chinese pattern:

```markdown
- 新增或修复的内容，由社区贡献者 [@octocat](https://github.com/octocat) 提交，感谢贡献（[#123](https://github.com/OWNER/REPOSITORY/issues/123)）。
```

English pattern:

```markdown
- Added or fixed behavior, contributed by community contributor [@octocat](https://github.com/octocat)—thank you! ([#123](https://github.com/OWNER/REPOSITORY/issues/123)).
```

When consolidating work from multiple community contributors, mention every contributor exactly once in that bullet. Do not silently drop a community-authored change; include it in an appropriate section or explicitly ask how it should be represented.

## Bilingual Quality

- Preserve the same facts, scope, PR references, contributor mentions, and order in both languages.
- Write natural Chinese and English rather than translating word for word.
- Keep product terms and identifiers consistent with existing documentation, including SQL, CI/CD, Sidecar, datasource names, and code identifiers in backticks.
- Avoid marketing claims that are not supported by the release range.

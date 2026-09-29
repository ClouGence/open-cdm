#!/usr/bin/env python3
"""Collect read-only Git evidence for bilingual Open CDM release notes."""

from __future__ import annotations

import argparse
import json
import re
import subprocess
import sys
from pathlib import Path

sys.dont_write_bytecode = True
from change_open_cdm_version import detect_repo, normalize_version


STABLE_TAG_RE = re.compile(r"^v?(\d+)\.(\d+)\.(\d+)$")
PR_SUFFIX_RE = re.compile(r"\(#(\d+)\)\s*$")
MERGE_PR_RE = re.compile(r"^Merge pull request #(\d+)\b")
CONVENTIONAL_RE = re.compile(r"^(\w+)(?:\([^)]*\))?!?:")


def git(repo: Path, *args: str) -> str:
    return subprocess.check_output(
        ["git", "-C", str(repo), *args], text=True, encoding="utf-8", stderr=subprocess.PIPE
    ).rstrip("\n")


def resolve_commit(repo: Path, ref: str) -> str:
    return git(repo, "rev-parse", "--verify", "--end-of-options", f"{ref}^{{commit}}")


def choose_base(repo: Path, version: str, head: str) -> str:
    if git(repo, "rev-parse", "--is-shallow-repository") == "true":
        raise ValueError("shallow history: obtain complete release history or supply a verified --base")

    core = re.split(r"[-+]", version, maxsplit=1)[0]
    target = tuple(int(part) for part in core.split("."))
    candidates = []
    for tag in git(repo, "tag", "--merged", head).splitlines():
        match = STABLE_TAG_RE.fullmatch(tag)
        if match:
            release = tuple(int(part) for part in match.groups())
            if release < target:
                candidates.append((release, tag))
    if not candidates:
        raise ValueError("no reachable stable version tag below the target; supply a verified --base")

    newest = max(release for release, _ in candidates)
    tags = sorted(tag for release, tag in candidates if release == newest)
    if len({resolve_commit(repo, tag) for tag in tags}) != 1:
        raise ValueError(f"conflicting tags for the previous version: {', '.join(tags)}; specify --base")
    return tags[0]


def collect_commits(repo: Path, base: str, head: str) -> list[dict]:
    raw = git(
        repo,
        "log",
        "--first-parent",
        "-z",
        "--format=%H%x00%P%x00%an%x00%ae%x00%s%x00%b",
        f"{base}..{head}",
        "--",
    )
    if not raw:
        return []
    fields = raw.removesuffix("\0").split("\0")
    if len(fields) % 6:
        raise ValueError("unexpected Git log record format")

    commits = []
    for offset in range(0, len(fields), 6):
        sha, parent_text, author, email, subject, body = fields[offset:offset + 6]
        parents = parent_text.split()
        pr = PR_SUFFIX_RE.search(subject) or MERGE_PR_RE.search(subject)
        conventional = CONVENTIONAL_RE.match(subject)
        category = "review"
        if conventional:
            if conventional[1] == "feat":
                category = "added"
            elif conventional[1] == "fix":
                category = "fixed"
        paths = git(repo, "diff", "--name-only", parents[0], sha, "--").splitlines()
        commits.append({
            "sha": sha,
            "parents": parents,
            "subject": subject,
            "body": body.rstrip("\n"),
            "author_name": author,
            "author_email": email,
            "pr_number": int(pr[1]) if pr else None,
            "category_hint": category,
            "changed_paths": paths,
        })
    return commits


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--repo", help="Open CDM repository root; defaults to the current checkout")
    parser.add_argument("--version", required=True, help="Target release version, for example 4.3.0")
    parser.add_argument("--base", help="Explicit previous release tag or commit")
    parser.add_argument("--head", default="HEAD", help="Release head ref (default: HEAD)")
    args = parser.parse_args()

    try:
        repo = detect_repo(args.repo)
        version = normalize_version(args.version)
        head = resolve_commit(repo, args.head)
        base_ref = args.base or choose_base(repo, version, head)
        base = resolve_commit(repo, base_ref)
        ancestor = subprocess.run(
            ["git", "-C", str(repo), "merge-base", "--is-ancestor", base, head],
            capture_output=True, text=True,
        )
        if ancestor.returncode == 1:
            raise ValueError(f"base {base_ref!r} is not an ancestor of {args.head!r}")
        ancestor.check_returncode()
        commits = collect_commits(repo, base, head)
        notes_dir = repo / "docs/release-notes" / f"v{version}"
        result = {
            "repo": str(repo),
            "version": version,
            "base_ref": base_ref,
            "base_sha": base,
            "head_ref": args.head,
            "head_sha": head,
            "range": f"{base}..{head}",
            "selection": "first-parent; inspect merge PR metadata or constituent commits when needed",
            "target_notes": {
                name: (notes_dir / name).is_file()
                for name in ("index.cn.md", "index.en.md")
            },
            "commit_count": len(commits),
            "commits": commits,
        }
    except (ValueError, FileNotFoundError, subprocess.CalledProcessError) as exc:
        detail = str(exc)
        if isinstance(exc, subprocess.CalledProcessError) and exc.stderr:
            detail = exc.stderr.strip()
        print(f"error: {detail}", file=sys.stderr)
        return 2

    print(json.dumps(result, ensure_ascii=False, indent=2))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

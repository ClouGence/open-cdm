#!/usr/bin/env python3
"""Collect Git and GitHub metadata for an Open CDM release range."""

from __future__ import annotations

import argparse
import json
import re
import shutil
import subprocess
import sys
from pathlib import Path

from change_open_cdm_version import detect_repo, normalize_version


STABLE_TAG_RE = re.compile(r"^v(?P<major>\d+)\.(?P<minor>\d+)\.(?P<patch>\d+)$")
BASE_VERSION_RE = re.compile(r"^(?P<major>\d+)\.(?P<minor>\d+)\.(?P<patch>\d+)")
PR_NUMBER_RES = [
    re.compile(r"\(#(?P<number>\d+)\)\s*$"),
    re.compile(r"^Merge pull request #(?P<number>\d+)\b"),
]
INTERNAL_ASSOCIATIONS = {"OWNER", "MEMBER", "COLLABORATOR"}
COMMUNITY_ASSOCIATIONS = {
    "CONTRIBUTOR",
    "FIRST_TIMER",
    "FIRST_TIME_CONTRIBUTOR",
    "NONE",
}


def run(repo: Path, command: list[str], check: bool = True) -> subprocess.CompletedProcess[str]:
    result = subprocess.run(
        command,
        cwd=repo,
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        check=False,
    )
    if check and result.returncode:
        detail = result.stderr.strip() or result.stdout.strip()
        raise RuntimeError(f"command failed ({' '.join(command)}): {detail}")
    return result


def git(repo: Path, *args: str) -> str:
    return run(repo, ["git", *args]).stdout.strip()


def ref_exists(repo: Path, ref: str) -> bool:
    result = run(
        repo,
        ["git", "rev-parse", "--verify", "--quiet", f"{ref}^{{commit}}"],
        check=False,
    )
    return result.returncode == 0


def version_key(version: str) -> tuple[int, int, int]:
    match = BASE_VERSION_RE.match(version)
    if not match:
        raise ValueError(f"cannot derive a stable version key from {version!r}")
    return tuple(int(match.group(name)) for name in ("major", "minor", "patch"))


def resolve_target_ref(repo: Path, version: str, explicit_ref: str | None) -> str:
    if explicit_ref:
        if not ref_exists(repo, explicit_ref):
            raise ValueError(f"target ref {explicit_ref!r} does not exist")
        return explicit_ref

    target_tag = f"v{version}"
    if not ref_exists(repo, target_tag):
        raise ValueError(
            f"target tag {target_tag!r} does not exist; pass --to-ref for an approved pre-tag ref"
        )
    return target_tag


def resolve_start_tag(
    repo: Path,
    version: str,
    target_ref: str,
    explicit_tag: str | None,
) -> str:
    if explicit_tag:
        start_tag = explicit_tag if explicit_tag.startswith("v") else f"v{explicit_tag}"
        if not ref_exists(repo, start_tag):
            raise ValueError(f"start tag {start_tag!r} does not exist")
        return start_tag

    target_key = version_key(version)
    candidates: list[tuple[tuple[int, int, int], str]] = []
    for tag in git(repo, "tag", "--merged", target_ref, "--list", "v*").splitlines():
        match = STABLE_TAG_RE.match(tag)
        if not match:
            continue
        key = tuple(int(match.group(name)) for name in ("major", "minor", "patch"))
        if key < target_key:
            candidates.append((key, tag))

    if not candidates:
        raise ValueError(f"no previous stable tag is reachable from {target_ref!r}")
    return max(candidates)[1]


def ensure_ancestor(repo: Path, start_tag: str, target_ref: str) -> None:
    result = run(
        repo,
        ["git", "merge-base", "--is-ancestor", start_tag, target_ref],
        check=False,
    )
    if result.returncode:
        raise ValueError(f"start tag {start_tag!r} is not an ancestor of {target_ref!r}")


def detect_github_repository(repo: Path) -> str | None:
    remote_url = git(repo, "remote", "get-url", "origin")
    match = re.search(r"github\.com[/:](?P<slug>[^/\s]+/[^/\s]+?)(?:\.git)?$", remote_url)
    if not match:
        return None
    return match.group("slug")


def extract_pr_number(subject: str) -> int | None:
    for pattern in PR_NUMBER_RES:
        match = pattern.search(subject)
        if match:
            return int(match.group("number"))
    return None


def resolve_pr(repo: Path, repository: str, number: int) -> tuple[dict[str, object] | None, str | None]:
    result = run(
        repo,
        ["gh", "api", f"repos/{repository}/pulls/{number}"],
        check=False,
    )
    if result.returncode:
        detail = result.stderr.strip() or result.stdout.strip()
        return None, detail

    payload = json.loads(result.stdout)
    login = payload["user"]["login"]
    association = payload.get("author_association")
    is_bot = login.endswith("[bot]")
    community: bool | None = None
    if is_bot or association in INTERNAL_ASSOCIATIONS:
        community = False
    elif association in COMMUNITY_ASSOCIATIONS:
        community = True

    return {
        "number": number,
        "title": payload["title"],
        "url": payload["html_url"],
        "author_login": login,
        "author_url": payload["user"]["html_url"],
        "author_association": association,
        "community_contributor": community,
    }, None


def collect_commit(repo: Path, commit: str, include_files: bool) -> dict[str, object]:
    raw = git(repo, "show", "-s", "--format=%an%x00%ae%x00%aI%x00%s%x00%b", commit)
    author_name, author_email, authored_at, subject, body = raw.split("\x00", 4)
    result: dict[str, object] = {
        "sha": commit,
        "short_sha": commit[:10],
        "authored_at": authored_at,
        "author_name": author_name,
        "author_email": author_email,
        "subject": subject,
        "body": body.strip(),
        "pr_number": extract_pr_number(subject),
        "pr": None,
    }
    if include_files:
        parents = git(repo, "rev-list", "--parents", "-n", "1", commit).split()
        files: list[str] = []
        if len(parents) > 1:
            files = git(repo, "diff", "--name-only", parents[1], commit).splitlines()
        result["files"] = files
    return result


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--version", required=True, help="Target version, for example 1.2.3")
    parser.add_argument("--repo", help="Path to the open-cdm repository root")
    parser.add_argument("--from-tag", help="Override the previous stable release tag")
    parser.add_argument("--to-ref", help="Override the target tag with an explicit Git ref")
    parser.add_argument(
        "--github",
        action="store_true",
        help="Resolve PR authors and community status with the authenticated GitHub CLI",
    )
    parser.add_argument(
        "--include-files",
        action="store_true",
        help="Include each commit's changed paths for investigating vague messages",
    )
    args = parser.parse_args()

    try:
        version = normalize_version(args.version)
        repo = detect_repo(args.repo)
        target_ref = resolve_target_ref(repo, version, args.to_ref)
        start_tag = resolve_start_tag(repo, version, target_ref, args.from_tag)
        ensure_ancestor(repo, start_tag, target_ref)
        repository = detect_github_repository(repo)
        if args.github and not shutil.which("gh"):
            raise RuntimeError("--github requires the GitHub CLI")
        if args.github and not repository:
            raise RuntimeError("cannot derive a GitHub repository from the origin remote")
    except Exception as exc:
        print(f"error: {exc}", file=sys.stderr)
        return 2

    commit_ids = git(repo, "rev-list", "--reverse", "--first-parent", f"{start_tag}..{target_ref}").splitlines()
    commits = [collect_commit(repo, commit, args.include_files) for commit in commit_ids]
    warnings: list[str] = []

    if args.github and repository:
        for commit in commits:
            number = commit["pr_number"]
            if number is None:
                warnings.append(f"{commit['short_sha']}: no PR number found in commit subject")
                continue
            pr, error = resolve_pr(repo, repository, number)
            commit["pr"] = pr
            if error:
                warnings.append(f"PR #{number}: {error}")

    community_contributors = sorted(
        {
            commit["pr"]["author_login"]
            for commit in commits
            if commit["pr"] and commit["pr"]["community_contributor"] is True
        },
        key=str.casefold,
    )
    unresolved_attribution = [
        commit["short_sha"]
        for commit in commits
        if commit["pr_number"] is None
        or (args.github and commit["pr"] is None)
        or (
            commit["pr"]
            and commit["pr"]["community_contributor"] is None
        )
    ]
    current_branch_result = run(
        repo,
        ["git", "symbolic-ref", "--quiet", "--short", "HEAD"],
        check=False,
    )
    target_in_current_head = run(
        repo,
        ["git", "merge-base", "--is-ancestor", target_ref, "HEAD"],
        check=False,
    ).returncode == 0

    output = {
        "repository_root": str(repo),
        "github_repository": repository,
        "current_branch": current_branch_result.stdout.strip() or None,
        "current_head": git(repo, "rev-parse", "HEAD"),
        "target_version": version,
        "start_tag": start_tag,
        "start_commit": git(repo, "rev-parse", f"{start_tag}^{{commit}}"),
        "target_ref": target_ref,
        "target_commit": git(repo, "rev-parse", f"{target_ref}^{{commit}}"),
        "target_reachable_from_current_head": target_in_current_head,
        "commit_count": len(commits),
        "github_metadata_requested": args.github,
        "attribution_complete": args.github and not unresolved_attribution,
        "community_contributors": community_contributors,
        "unresolved_attribution": unresolved_attribution,
        "warnings": warnings,
        "commits": commits,
    }
    json.dump(output, sys.stdout, ensure_ascii=False, indent=2)
    sys.stdout.write("\n")
    if args.github and unresolved_attribution:
        return 3
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

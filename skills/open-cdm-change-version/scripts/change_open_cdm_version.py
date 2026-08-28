#!/usr/bin/env python3
"""Update the Open CDM build version and public version references."""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path


VERSION_RE = re.compile(r"^v?(\d+\.\d+\.\d+(?:[-+][0-9A-Za-z][0-9A-Za-z._-]*)?)$")

TARGET_FILES = [
    "backend/gradle.properties",
    "README.md",
    "docs/README.cn.md",
    "docs/README.en.md",
    "docs/guides/deployment.cn.md",
    "docs/guides/deployment.en.md",
    "docs/reference/faq.cn.md",
    "docs/reference/faq.en.md",
    "docs/release-notes/README.md",
    "llms-full.txt",
]

MAIN_VERSION_PROPERTY_RE = re.compile(
    r"(?P<prefix>^[ \t]*cg\.clouddm\.main\.version[ \t]*=[ \t]*)"
    r"(?P<version>[^\s#]+)"
    r"(?P<suffix>[ \t]*(?:#.*)?$)",
    flags=re.MULTILINE,
)

IMAGE_PREFIX_RE = re.compile(
    r"(?P<prefix>"
    r"(?:bladepipe|clougence)/cgdm-(?:alone|console|sidecar):"
    r"|cloudcanal-registry\.cn-shanghai\.cr\.aliyuncs\.com/clougence/cgdm-(?:alone|console|sidecar):"
    r")"
    r"(?P<arch>(?:x86_64|aarch64|arm64|amd64)-)?"
    r"(?P<version>\d+\.\d+\.\d+(?:[-+][0-9A-Za-z][0-9A-Za-z._-]*)?)"
)

CURRENT_VERSION_ROW_RE = re.compile(
    r"(?P<prefix>\|\s*(?:Current version|当前版本)\s*\|\s*)"
    r"(?P<version>[^|\n]+?)"
    r"(?P<suffix>\s*\|)"
)


def normalize_version(raw: str) -> str:
    match = VERSION_RE.match(raw.strip())
    if not match:
        raise ValueError(
            f"invalid version {raw!r}; expected explicit SemVer like 1.2.3"
        )
    return match.group(1)


def detect_repo(explicit_repo: str | None) -> Path:
    if explicit_repo:
        repo = Path(explicit_repo).expanduser().resolve()
    else:
        cwd = Path.cwd().resolve()
        candidates = [cwd, *cwd.parents]
        repo = next(
            (
                path
                for path in candidates
                if (path / "backend/gradle.properties").is_file()
                and (path / "docs").is_dir()
                and (path / "README.md").is_file()
            ),
            cwd,
        )

    if (
        not (repo / "backend/gradle.properties").is_file()
        or not (repo / "README.md").is_file()
        or not (repo / "docs").is_dir()
    ):
        raise FileNotFoundError(f"{repo} does not look like the open-cdm repository root")
    return repo


def update_release_notes_index(text: str, version: str) -> tuple[str, int]:
    row = f"| v{version} | [中文](v{version}/index.cn.md) | [English](v{version}/index.en.md) |"
    if re.search(rf"^\|\s*v{re.escape(version)}\s*\|", text, flags=re.MULTILINE):
        return text, 0

    lines = text.splitlines(keepends=True)
    for idx, line in enumerate(lines):
        if re.match(r"^\|\s*-+\s*\|\s*-+\s*\|\s*-+\s*\|", line):
            newline = "\n" if line.endswith("\n") else ""
            lines.insert(idx + 1, row + newline)
            return "".join(lines), 1

    if text and not text.endswith("\n"):
        text += "\n"
    return text + row + "\n", 1


def update_text(
    relative_path: str,
    text: str,
    version: str,
    release_notes_ready: bool,
) -> tuple[str, int]:
    change_count = 0

    if relative_path == "backend/gradle.properties":
        def replace_main_version(match: re.Match[str]) -> str:
            nonlocal change_count
            if match.group("version") == version:
                return match.group(0)
            change_count += 1
            return f"{match.group('prefix')}{version}{match.group('suffix')}"

        return MAIN_VERSION_PROPERTY_RE.sub(replace_main_version, text), change_count

    def replace_version_row(match: re.Match[str]) -> str:
        nonlocal change_count
        old = match.group("version").strip()
        if old == version:
            return match.group(0)
        change_count += 1
        return f"{match.group('prefix')}{version}{match.group('suffix')}"

    text = CURRENT_VERSION_ROW_RE.sub(replace_version_row, text)

    def replace_image_tag(match: re.Match[str]) -> str:
        nonlocal change_count
        old = match.group("version")
        if old == version:
            return match.group(0)
        change_count += 1
        return f"{match.group('prefix')}{match.group('arch') or ''}{version}"

    text = IMAGE_PREFIX_RE.sub(replace_image_tag, text)

    if relative_path == "docs/release-notes/README.md" and release_notes_ready:
        text, inserted = update_release_notes_index(text, version)
        change_count += inserted

    return text, change_count


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--version", required=True, help="Target version, for example 1.2.3")
    parser.add_argument("--repo", help="Path to the open-cdm repository root")
    parser.add_argument("--dry-run", action="store_true", help="Print changes without writing files")
    args = parser.parse_args()

    try:
        version = normalize_version(args.version)
        repo = detect_repo(args.repo)
    except Exception as exc:
        print(f"error: {exc}", file=sys.stderr)
        return 2

    total_changes = 0
    changed_files: list[tuple[str, int]] = []
    missing_files: list[str] = []
    release_notes_dir = repo / "docs/release-notes" / f"v{version}"
    release_notes_ready = all(
        (release_notes_dir / filename).is_file()
        for filename in ("index.cn.md", "index.en.md")
    )

    for relative_path in TARGET_FILES:
        path = repo / relative_path
        if not path.exists():
            missing_files.append(relative_path)
            continue
        original = path.read_text(encoding="utf-8")
        updated, count = update_text(
            relative_path,
            original,
            version,
            release_notes_ready,
        )
        if count:
            total_changes += count
            changed_files.append((relative_path, count))
            if not args.dry_run:
                path.write_text(updated, encoding="utf-8")

    mode = "dry-run" if args.dry_run else "updated"
    print(f"{mode}: repo={repo} version={version} changes={total_changes}")
    for relative_path, count in changed_files:
        print(f"  {relative_path}: {count}")
    if missing_files:
        print("missing:")
        for relative_path in missing_files:
            print(f"  {relative_path}")
    if not release_notes_ready:
        print(f"release notes index skipped: expected {release_notes_dir}/index.cn.md and index.en.md")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())

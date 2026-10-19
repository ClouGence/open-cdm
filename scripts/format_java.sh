#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
MODE=apply
FORMAT_ONLY=false

usage() {
    cat <<'EOF'
Usage: ./scripts/format_java.sh [--dry-run | --check] [--format-only]

Optimize imports with IntelliJ IDEA, then format using codeformat.xml.
Includes staged, unstaged and untracked files; formats each whole file.

  --dry-run  List selected files without loading Gradle or modifying files.
  --check    Check formatting without writing; exit 1 if changes are needed.
  --format-only  Skip IDEA; only apply/check Eclipse code formatting.
  --help     Show this help.

Requires Git and JDK 17+. The first format/check downloads dependencies.
IDEA must be running with the project open and indexing completed.
On macOS, the running IDEA application is detected by its bundle identifier.
Set IDEA_BIN to the executable when multiple IDEA versions are running.
Save files in your editor before running. Git's index is never updated.
IDEA's built-in ideScript command is used; no additional plugin is needed.
EOF
}

for argument in "$@"; do
    case "$argument" in
        --dry-run|--check)
            if [[ "$MODE" != apply ]]; then
                usage >&2
                exit 2
            fi
            if [[ "$argument" == --dry-run ]]; then
                MODE=list
            else
                MODE=check
            fi
            ;;
        --format-only) FORMAT_ONLY=true ;;
        --help|-h) usage; exit 0 ;;
        *) usage >&2; exit 2 ;;
    esac
done

cd "$REPO_ROOT"
TEMP_DIR="$(mktemp -d "${TMPDIR:-/tmp}/open-cdm-format.XXXXXX")"
trap 'rm -rf "$TEMP_DIR"' EXIT

# Read both diffs separately: staged and working-tree edits can cancel each other.
git diff --cached --name-only --no-renames --diff-filter=ACMT -z -- '*.java' > "$TEMP_DIR/candidates"
git diff --name-only --no-renames --diff-filter=ACMT -z -- '*.java' >> "$TEMP_DIR/candidates"
git ls-files --others --exclude-standard -z -- '*.java' >> "$TEMP_DIR/candidates"

FILES=()
while IFS= read -r -d '' path; do
    case "/$path/" in
        */build/*|*/target/*|*/out/*|*/gen/*|*/node_modules/*|*/dist/*|*/.gradle/*) continue ;;
    esac
    [[ -f "$path" && ! -L "$path" ]] || continue
    duplicate=false
    for selected in "${FILES[@]+"${FILES[@]}"}"; do
        if [[ "$selected" == "$path" ]]; then
            duplicate=true
            break
        fi
    done
    if [[ "$duplicate" == false ]]; then
        FILES+=("$path")
    fi
done < "$TEMP_DIR/candidates"

if [[ ${#FILES[@]} -eq 0 ]]; then
    echo 'No changed Java files.'
    exit 0
fi

printf 'Selected %s Java file(s):\n' "${#FILES[@]}"
printf '  %s\n' "${FILES[@]}"
if [[ "$MODE" == list ]]; then
    exit 0
fi

printf '%s\0' "${FILES[@]}" > "$TEMP_DIR/files"
IDEA_LAUNCHER=''
if [[ "$FORMAT_ONLY" == false ]]; then
    IDEA_LAUNCHER="${IDEA_BIN:-}"
    if [[ "$(uname -s)" == Darwin ]]; then
        if ! RUNNING_IDEA_OUTPUT="$(/usr/bin/osascript -l JavaScript <<'JXA'
ObjC.import('AppKit');
var applications = $.NSWorkspace.sharedWorkspace.runningApplications;
var executables = [];
for (var i = 0; i < applications.count; i++) {
    var application = applications.objectAtIndex(i);
    var bundleId = ObjC.unwrap(application.bundleIdentifier);
    if (bundleId && /^com\.jetbrains\.intellij(?:[.-]|$)/.test(bundleId)) {
        executables.push(ObjC.unwrap(application.executableURL.path));
    }
}
executables.join('\n');
JXA
        )"; then
            echo 'Cannot query running macOS applications. See the system error above.' >&2
            exit 1
        fi
        RUNNING_IDEA_EXECUTABLES=()
        while IFS= read -r executable; do
            if [[ -n "$executable" ]]; then
                RUNNING_IDEA_EXECUTABLES+=("$executable")
            fi
        done <<< "$RUNNING_IDEA_OUTPUT"
        if [[ ${#RUNNING_IDEA_EXECUTABLES[@]} -eq 0 ]]; then
            echo 'macOS reports no running IntelliJ IDEA application. Start IDEA and open the project.' >&2
            exit 1
        fi
        if [[ -z "$IDEA_LAUNCHER" ]]; then
            if [[ ${#RUNNING_IDEA_EXECUTABLES[@]} -gt 1 ]]; then
                printf 'Multiple IDEA versions are running. Set IDEA_BIN to one executable:\n' >&2
                printf '  %s\n' "${RUNNING_IDEA_EXECUTABLES[@]}" >&2
                exit 1
            fi
            IDEA_LAUNCHER="${RUNNING_IDEA_EXECUTABLES[0]}"
        fi
    fi
    if [[ -z "$IDEA_LAUNCHER" ]]; then
        IDEA_LAUNCHER="$(command -v idea || true)"
    fi
    if [[ -z "$IDEA_LAUNCHER" && -x '/Applications/IntelliJ IDEA.app/Contents/MacOS/idea' ]]; then
        IDEA_LAUNCHER='/Applications/IntelliJ IDEA.app/Contents/MacOS/idea'
    fi
    if [[ ! -x "$IDEA_LAUNCHER" ]]; then
        echo 'IDEA executable not found. Set IDEA_BIN, or use --format-only.' >&2
        exit 1
    fi
    printf 'Using IDEA: %s\n' "$IDEA_LAUNCHER"
fi
"$REPO_ROOT/backend/gradlew" --quiet --console=plain \
    --project-dir "$SCRIPT_DIR/java_formatter" \
    -PformatFiles="$TEMP_DIR/files" -PformatMode="$MODE" \
    -PideaLauncher="$IDEA_LAUNCHER" formatChanged

# Contributing to CloudDM

[English](docs/en/CONTRIBUTING.en.md) | [中文](docs/cn/CONTRIBUTING.cn.md)

Thank you for your interest in **CloudDM**! We welcome all forms of contribution, including but not limited to bug reports, feature suggestions, code contributions, and documentation improvements.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How to Contribute](#how-to-contribute)
  - [Report a Bug](#report-a-bug)
  - [Suggest a Feature](#suggest-a-feature)
  - [Code Contributions](#code-contributions)
- [Development Environment Setup](#development-environment-setup)
- [Coding Standards](#coding-standards)
- [PR Submission Process](#pr-submission-process)
- [Commit Conventions](#commit-conventions)
- [Branch Management](#branch-management)
- [FAQ](#faq)

## Code of Conduct

All contributors to this project should uphold the spirit of open-source collaboration — be respectful, constructive, and courteous in all communication. See [Code of Conduct](CODE_OF_CONDUCT.md) (if available) for details.

## How to Contribute

### Report a Bug

If you find a bug, please submit it via GitHub/Gitee Issues with as much of the following as possible:

- Clear title and description
- Reproduction steps (code snippets welcome)
- Expected vs. actual behavior
- Environment details (OS, JDK version, database type, etc.)
- Relevant logs or screenshots

### Suggest a Feature

Feature suggestions are welcome. Please submit an Issue prefixed with `[Feature]` and describe:

- The problem the feature would solve
- Desired behavior or outcome
- Any similar implementations to reference

### Code Contributions

1. **Fork** the repository to your GitHub/Gitee account
2. Create a feature branch (see [Branch Management](#branch-management))
3. Write code and ensure existing tests pass
4. Submit a Pull Request

## Development Environment Setup

### Prerequisites

- **JDK 21+**
- **Gradle 9.5.0+**
- **Node.js 22.22.1**
- **IntelliJ IDEA** or Eclipse (IntelliJ IDEA recommended)
- **Git**
- **Linux / macOS** or Windows with Bash
- **MySQL 8.0+** (runtime database)

### Local Build

```bash
# Full build (including frontend assets)
cd package && ./all_build.sh

# Frontend assets only
cd package && ./all_build.sh web

# Compile and generate tgz install packages
cd package && ./package.sh --build

# Compile, generate tgz install packages, and build Docker images plus deployment manifests for all platforms
cd package && ./package.sh --build --docker

# Compile, generate tgz install packages, and build only one target architecture
cd package && ./package.sh --build --docker arm64
cd package && ./package.sh --build --docker x86_64

# If tgz packages already exist under package/build, build only Docker images and deployment manifests
cd package && ./package.sh --docker
cd package && ./package.sh --docker arm64
cd package && ./package.sh --docker x86_64
```

> **Version**: Defined in `backend/gradle.properties` under `cg.clouddm.main.version`.
>
> **Build output directory**: `package/build`, which contains `cgdm-*.tar.gz`, `docker-*.tar`, `docker-*.yml`, and `k8s-*.yml`.

### Project Structure

| Module | Description |
|--------|-------------|
| `backend/` | Gradle root with backend, plugins, launchers, utilities, and test modules |
| `frontend/` | Web frontend project |
| `package/` | Repository build entry, tgz packaging, Docker images, compose templates, and delivery artifacts |
| `docs/` | Documentation and image assets |

### Import into IDE

1. Clone the project and open the `backend/` directory with IntelliJ IDEA.
2. IDEA will automatically detect the Gradle project and begin importing.
3. Wait for dependencies to download, then you're ready to develop.

### Debugging or Running

1. Run `cd package && ./all_build.sh` in the project root directory.
2. Go to `backend/clouddm-boot/boot-alone` and run `com.clougence.clouddm.boot.DmAloneLauncher` to start the application, and visit `http://localhost:8222`.
3. **[First Run]** Open the web page for initial setup and fill in the database and other required information. When you see the message **"Waiting for application restart"**, repeat Step 2.

## Coding Standards

### Java Code Formatting

- The project uses Eclipse code formatting configuration (`codeformat.xml`)
- IntelliJ IDEA users should install the **Eclipse Code Formatter** plugin and import `codeformat.xml`
- Indentation: 4 spaces
- Encoding: UTF-8
- Maintain consistent file headers with copyright notice

### Coding Style

- Database entities use the `DO` suffix, form objects use `FO`, and request/response types use `VO`
- Follow existing naming conventions; avoid introducing new naming systems
- Organize packages by responsibility; prefer `controller`, `service`, `dal`, `model`, `config`, `util`, `global`, etc.
- Use project infrastructure (`com.clougence.utils`) and existing SDK / platform utilities; avoid reinventing the same functionality
- Always use Lombok's `@Slf4j`; log errors with context and stack traces; avoid meaningless exceptions
- Minimize visibility — expose APIs deliberately; default to `private`; use `public` sparingly
- Strict single responsibility; enforce stable unidirectional dependencies; avoid circular dependencies
- Avoid giant classes and methods unless exceptional; keep classes around 500 formatted lines
- Do not over-split into tiny methods or fragmented classes

### Testing

- New features should include appropriate unit or integration tests
- Ensure `cd package && ./all_build.sh` passes before submitting

## PR Submission Process

1. **Ensure your fork is in sync with upstream**
2. **Create a new branch** (see branch management below)
3. **Write and commit your code**
4. **Run tests** and make sure they all pass
5. **Submit a Pull Request** to the target branch
   - PR title should clearly describe the change
   - Link related Issues in the PR description (if any)
   - Specify the change type (bugfix / feature / refactor / docs, etc.)
6. **Wait for review** and address feedback
7. Branches will be deleted **after merge**

## Commit Conventions

We recommend the [Conventional Commits](https://www.conventionalcommits.org/) format:

```
<type>(<scope>): <subject>

<body>
```

Common types:

| Type | Description |
|------|-------------|
| `feat` | New feature |
| `fix` | Bug fix |
| `docs` | Documentation changes |
| `refactor` | Code refactoring |
| `test` | Test-related changes |
| `chore` | Build / tooling changes |
| `style` | Code formatting (no functional impact) |

Examples:

```
feat(mysql): support prepared statement for MySQL driver
fix(oracle): resolve NPE when connection is null
docs: update README with quick start guide
```

## Branch Management

- `main` / `master`: stable release branch
- `dev`: main development branch
- `feat/<feature-name>`: feature development branch
- `fix/<bug-name>`: bug fix branch

## FAQ

### How do I add support for a new database?

Refer to existing data source implementations under `backend/clouddm-plugins/clouddm-ds/` (e.g., `ds-mysql`), create a new data source plugin module, and register it in `backend/settings.gradle`.

### How do I create custom audit rules?

The project supports custom rule scripting for extension. See the official documentation for details.

---

Thank you again for contributing! If you have any questions, feel free to reach out via Issues.

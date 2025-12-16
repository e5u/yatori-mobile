# Contributing to Yatori Mobile

Thank you for your interest in contributing to Yatori Mobile! This document provides guidelines and instructions for contributing.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [How to Contribute](#how-to-contribute)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Testing](#testing)
- [Documentation](#documentation)

## Code of Conduct

We are committed to providing a welcoming and inclusive experience for everyone. Please be respectful and professional in all interactions.

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/yatori-mobile.git
   cd yatori-mobile
   ```
3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/e5u/yatori-mobile.git
   ```
4. **Create a branch** for your changes:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Development Setup

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK (API 34)
- Git

### Setup Steps

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Compile the yatori-go-console binary (see [BUILD_BINARY.md](docs/BUILD_BINARY.md))
4. Place the binary in `app/src/main/assets/`
5. Build and run the app

## How to Contribute

### Reporting Bugs

When reporting bugs, please include:

- **Clear title and description**
- **Steps to reproduce** the issue
- **Expected behavior**
- **Actual behavior**
- **Screenshots** (if applicable)
- **Device information**:
  - Android version
  - Device model
  - App version
- **Logs** (from LogCat or app logs)

### Suggesting Features

When suggesting features:

- **Explain the problem** the feature would solve
- **Describe the proposed solution**
- **Provide examples** of how it would work
- **Consider alternatives** you've thought about

### Code Contributions

We welcome code contributions! Areas where you can help:

- **Bug fixes**
- **New features**
- **Performance improvements**
- **UI/UX enhancements**
- **Documentation improvements**
- **Test coverage**
- **Translations**

## Coding Standards

### Kotlin Style

Follow the [official Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html):

- Use 4 spaces for indentation
- Use meaningful variable and function names
- Keep functions small and focused
- Add KDoc comments for public APIs

**Example:**
```kotlin
/**
 * Extracts the binary from assets to internal storage.
 *
 * @return Result containing the binary path on success, or an exception on failure
 */
fun extractBinaryFromAssets(): Result<String> {
    // Implementation
}
```

### Android Best Practices

- Use **ViewBinding** instead of findViewById
- Use **coroutines** for asynchronous operations
- Follow **Material Design 3** guidelines
- Handle **lifecycle** events properly
- Request **permissions** at runtime when needed
- Use **Jetpack libraries** where appropriate

### Code Organization

```
app/src/main/java/com/e5u/yatori/mobile/
├── YatoriApplication.kt        # Application class
├── ui/                         # Activities and UI
├── service/                    # Services
├── manager/                    # Business logic
└── util/                       # Utility classes
```

## Commit Guidelines

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Build process or auxiliary tool changes

**Example:**
```
feat(ui): Add search functionality to log viewer

- Implement search bar in LogViewerActivity
- Add text highlighting for search results
- Include search navigation buttons

Closes #123
```

### Best Practices

- Write clear, concise commit messages
- Keep commits atomic (one logical change per commit)
- Reference issues in commit messages
- Sign your commits (optional but recommended)

## Pull Request Process

1. **Update your fork** with upstream changes:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Test your changes**:
   - Run unit tests: `./gradlew test`
   - Run instrumentation tests: `./gradlew connectedAndroidTest`
   - Run lint: `./gradlew lint`
   - Test on a physical device

3. **Push your branch**:
   ```bash
   git push origin feature/your-feature-name
   ```

4. **Create a Pull Request** on GitHub with:
   - **Clear title** describing the change
   - **Description** explaining what and why
   - **Reference to issues** being addressed
   - **Screenshots** for UI changes
   - **Testing notes**

5. **Address review feedback**:
   - Make requested changes
   - Push additional commits
   - Respond to comments

6. **After approval**:
   - Maintainer will merge your PR
   - Delete your feature branch

## Testing

### Writing Tests

- **Unit tests** in `app/src/test/` for business logic
- **Instrumentation tests** in `app/src/androidTest/` for UI

**Unit Test Example:**
```kotlin
@Test
fun testFormatLogSize_kilobytes() {
    val formatted = logManager.formatLogSize(2048)
    assertEquals("2.00 KB", formatted)
}
```

**Instrumentation Test Example:**
```kotlin
@Test
fun testFabIsDisplayed() {
    onView(withId(R.id.fab))
        .check(matches(isDisplayed()))
}
```

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumentation tests (device/emulator required)
./gradlew connectedAndroidTest

# Specific test class
./gradlew test --tests com.e5u.yatori.mobile.manager.LogManagerTest
```

## Documentation

### Code Documentation

- Add **KDoc comments** for all public classes and methods
- Explain **complex logic** with inline comments
- Keep documentation **up to date** with code changes

### User Documentation

Update relevant documentation files:

- **README.md** - Project overview and quick start
- **BUILDING.md** - Build instructions
- **docs/USAGE.md** - User guide
- **docs/BUILD_BINARY.md** - Binary compilation guide

## Questions?

If you have questions:

- **Check existing issues** on GitHub
- **Open a discussion** in GitHub Discussions
- **Ask in the pull request** if related to your changes

## Thank You!

Your contributions help make Yatori Mobile better for everyone. We appreciate your time and effort!

---

**Note**: This is an open-source project maintained by volunteers. Please be patient and respectful.

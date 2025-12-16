# Changelog

All notable changes to Yatori Mobile will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- Binary auto-update functionality
- Multiple binary profiles support
- Enhanced log filtering options
- Dark theme improvements
- Scheduled execution support
- Widget for quick access

## [1.0.0] - 2024-12-16

### Added
- Initial release of Yatori Mobile
- Complete Android project structure
- Material Design 3 UI implementation
- Binary execution with foreground service
- Real-time log viewing and management
- Log export functionality via FileProvider
- Settings screen with PreferenceFragment
- Runtime permission handling for Android 8.0-14
- Binary architecture detection (ARM64, ARMv7, x86_64)
- Automatic binary extraction from assets
- Background execution with notification updates
- Scoped storage support for Android 10+
- ProGuard rules for release builds
- ViewBinding throughout the app
- Comprehensive documentation:
  - README.md with project overview
  - BUILDING.md for developers
  - BUILD_BINARY.md for binary compilation
  - USAGE.md user guide
  - CONTRIBUTING.md for contributors
  - PROJECT_SUMMARY.md
- CI/CD workflows:
  - Android build workflow
  - CodeQL security scanning
- Test infrastructure:
  - Unit tests for managers
  - Instrumentation tests for UI
- Adaptive launcher icons with Material Design colors

### Technical Details
- **minSdk**: 26 (Android 8.0)
- **targetSdk**: 34 (Android 14)
- **Kotlin**: 1.9.21
- **Gradle**: 8.2
- **Android Gradle Plugin**: 8.2.1

### Dependencies
- AndroidX Core KTX 1.12.0
- AppCompat 1.6.1
- Material Components 1.11.0
- ConstraintLayout 2.1.4
- Lifecycle Runtime KTX 2.7.0
- Coroutines 1.7.3
- Preference KTX 1.2.1
- WorkManager 2.9.0

### Security
- All permissions properly requested at runtime
- FileProvider for secure file sharing
- ProGuard obfuscation for release builds
- CodeQL security scanning in CI/CD
- Zero security vulnerabilities detected

### Known Issues
- Binary must be compiled separately by users
- No auto-update mechanism yet
- Single binary instance only

### Notes
- Users must compile yatori-go-console binary separately
- See BUILD_BINARY.md for compilation instructions
- Launcher icons use default Material Design colors

---

## Version History

### [1.0.0] - 2024-12-16
- Initial public release

---

**Legend:**
- `Added` - New features
- `Changed` - Changes in existing functionality
- `Deprecated` - Soon-to-be removed features
- `Removed` - Now removed features
- `Fixed` - Bug fixes
- `Security` - Security improvements

For more details, see the [commit history](https://github.com/e5u/yatori-mobile/commits/main).

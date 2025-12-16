# Yatori Mobile 📱

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-26-orange.svg)](https://developer.android.com/about/versions/oreo)

Android application wrapper for [yatori-go-console](https://github.com/e5u/yatori-go-core), providing a mobile interface for executing and managing Yatori operations on Android devices.

## ✨ Features

- 🚀 **Native Binary Execution** - Run yatori-go-console binary directly on Android
- 📊 **Real-time Logging** - View execution logs in real-time
- 🔔 **Background Service** - Execute tasks in background with foreground service
- 💾 **Log Management** - Save, view, and export execution logs
- 🎨 **Material Design 3** - Modern UI following Material Design guidelines
- 🔐 **Permission Management** - Proper runtime permission handling
- ⚙️ **Configurable Settings** - Customize app behavior
- 📱 **Multi-Architecture Support** - ARM64 and ARMv7 support

## 📋 Requirements

- Android 8.0 (API 26) or higher
- ARM64 or ARMv7 device architecture
- Storage permissions for log management
- Compiled yatori-go-console binary (see [BUILD_BINARY.md](docs/BUILD_BINARY.md))

## 🔨 Building the Binary

The app requires the `yatori-go-console` binary compiled for Android. Follow the comprehensive guide in [docs/BUILD_BINARY.md](docs/BUILD_BINARY.md) to:

1. Set up Android NDK
2. Cross-compile yatori-go-console for ARM architectures
3. Place the binary in `app/src/main/assets/`

## 📦 Installation

### From Source

1. **Clone the repository:**
   ```bash
   git clone https://github.com/e5u/yatori-mobile.git
   cd yatori-mobile
   ```

2. **Compile the binary:**
   Follow instructions in [docs/BUILD_BINARY.md](docs/BUILD_BINARY.md)

3. **Place binary in assets:**
   ```bash
   cp path/to/yatori-go-console app/src/main/assets/
   ```

4. **Open in Android Studio:**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory
   - Wait for Gradle sync

5. **Build and run:**
   - Connect your Android device or start an emulator
   - Click "Run" or press Shift+F10

### Using Pre-built APK

Check the [Releases](https://github.com/e5u/yatori-mobile/releases) page for pre-built APK files (when available).

## 📱 Usage

Detailed usage instructions are available in [docs/USAGE.md](docs/USAGE.md).

### Quick Start

1. **Launch the app** - Open Yatori Mobile
2. **Grant permissions** - Allow storage and notification permissions
3. **Verify binary** - App will check if binary is properly installed
4. **Execute commands** - Tap the FAB to start execution
5. **View logs** - Access logs from the main screen

## 🏗️ Project Structure

```
yatori-mobile/
├── app/                          # Application module
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml
│   │       ├── assets/           # Place compiled binary here
│   │       ├── java/com/e5u/yatori/mobile/
│   │       │   ├── YatoriApplication.kt
│   │       │   ├── ui/          # Activities
│   │       │   ├── service/     # Background services
│   │       │   ├── manager/     # Business logic
│   │       │   └── util/        # Utilities
│   │       └── res/             # Resources
│   └── build.gradle.kts
├── docs/                        # Documentation
│   ├── BUILD_BINARY.md
│   └── USAGE.md
├── build.gradle.kts            # Root build script
├── settings.gradle.kts         # Gradle settings
└── gradle.properties           # Gradle properties
```

## 🛠️ Development

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK with API 34
- Kotlin 1.9.21

### Build Configuration

- **minSdk**: 26 (Android 8.0)
- **targetSdk**: 34 (Android 14)
- **compileSdk**: 34
- **Kotlin**: 1.9.21
- **Gradle**: 8.2

### Key Dependencies

- AndroidX Core KTX
- Material Design Components
- AndroidX Lifecycle
- Kotlin Coroutines

### Building

```bash
# Debug build
./gradlew assembleDebug

# Release build (requires signing config)
./gradlew assembleRelease

# Run tests
./gradlew test

# Run instrumentation tests
./gradlew connectedAndroidTest
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Write unit tests for new features
- Ensure all tests pass before submitting PR

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [yatori-go-core](https://github.com/e5u/yatori-go-core) - The core functionality
- Material Design Components
- AndroidX Libraries

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/e5u/yatori-mobile/issues)
- **Discussions**: [GitHub Discussions](https://github.com/e5u/yatori-mobile/discussions)

## ⚠️ Disclaimer

This is an unofficial Android wrapper for yatori-go-console. Use at your own risk. Ensure you comply with all applicable terms of service and laws when using this application.

---

Made with ❤️ by the Yatori community
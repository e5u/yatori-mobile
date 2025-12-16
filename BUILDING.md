# Building Yatori Mobile

This document explains how to build the Yatori Mobile Android application.

## Prerequisites

### Required Software

1. **Android Studio** (Hedgehog 2023.1.1 or later)
   - Download from: https://developer.android.com/studio

2. **Java Development Kit (JDK) 17**
   - Included with Android Studio
   - Or download from: https://adoptium.net/

3. **Android SDK**
   - API Level 34 (Android 14)
   - Install via Android Studio SDK Manager

4. **Git**
   - For cloning the repository

### System Requirements

- **OS**: Windows 10/11, macOS 10.14+, or Linux
- **RAM**: 8 GB minimum (16 GB recommended)
- **Disk Space**: 8 GB minimum for Android Studio and SDK

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/e5u/yatori-mobile.git
cd yatori-mobile
```

### 2. Compile the Binary

⚠️ **IMPORTANT**: Before building the app, you must compile the `yatori-go-console` binary for Android.

See [docs/BUILD_BINARY.md](docs/BUILD_BINARY.md) for detailed instructions.

Quick summary:
```bash
# Navigate to yatori-go-core repository
cd ../yatori-go-core

# Build for ARM64 (most common)
export CGO_ENABLED=1
export GOOS=android
export GOARCH=arm64
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android21-clang

go build -o yatori-go-console-arm64 -ldflags="-s -w" ./cmd/console

# Copy to app assets
cp yatori-go-console-arm64 ../yatori-mobile/app/src/main/assets/yatori-go-console
```

### 3. Add App Icons (Optional but Recommended)

The project includes placeholder files for app icons. For a production build, you should:

1. Create launcher icons in the following sizes:
   - **mdpi**: 48x48 px
   - **hdpi**: 72x72 px
   - **xhdpi**: 96x96 px
   - **xxhdpi**: 144x144 px
   - **xxxhdpi**: 192x192 px

2. Place icons in respective directories:
   - `app/src/main/res/mipmap-mdpi/ic_launcher.png`
   - `app/src/main/res/mipmap-hdpi/ic_launcher.png`
   - etc.

3. Use Android Studio's **Image Asset** tool:
   - Right-click on `res` folder
   - Select **New → Image Asset**
   - Choose **Launcher Icons** type
   - Upload your icon image
   - Click **Next** and **Finish**

## Building the App

### Method 1: Using Android Studio (Recommended)

1. **Open Android Studio**

2. **Open the project**:
   - Click **File → Open**
   - Navigate to the `yatori-mobile` directory
   - Click **OK**

3. **Wait for Gradle sync** to complete (first time may take several minutes)

4. **Connect a device or start an emulator**:
   - Physical device: Enable USB debugging and connect via USB
   - Emulator: Start an AVD from **Tools → Device Manager**

5. **Build and run**:
   - Click the green **Run** button (▶️) in the toolbar
   - Or press **Shift + F10** (Windows/Linux) or **Control + R** (macOS)

### Method 2: Using Command Line

#### Debug Build

```bash
# Unix/macOS/Linux
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

The APK will be created at:
```
app/build/outputs/apk/debug/app-debug.apk
```

#### Release Build

For a release build, you need to configure signing:

1. Create a keystore file:
```bash
keytool -genkey -v -keystore yatori-release.keystore -alias yatori -keyalg RSA -keysize 2048 -validity 10000
```

2. Create `keystore.properties` in the project root:
```properties
storeFile=../yatori-release.keystore
storePassword=YOUR_STORE_PASSWORD
keyAlias=yatori
keyPassword=YOUR_KEY_PASSWORD
```

3. Build the release APK:
```bash
./gradlew assembleRelease
```

The signed APK will be at:
```
app/build/outputs/apk/release/app-release.apk
```

### Method 3: CI/CD (GitHub Actions)

The project includes GitHub Actions workflows that automatically build the app on every push:

1. Push your code to GitHub
2. Navigate to **Actions** tab in the repository
3. View build results and download artifacts

## Installing the APK

### On Device

1. Transfer the APK to your Android device
2. Open the APK file
3. Allow installation from unknown sources if prompted
4. Tap **Install**

### Using ADB

```bash
adb install app/build/outputs/apk/debug/app-debug.apk

# Or to replace existing app
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Troubleshooting

### Gradle Sync Failed

**Problem**: Gradle sync fails with dependency resolution errors

**Solutions**:
1. Check your internet connection
2. Click **File → Invalidate Caches / Restart**
3. Delete `.gradle` and `build` directories and sync again
4. Update Gradle plugin version in `build.gradle.kts`

### SDK Not Found

**Problem**: Android SDK not found errors

**Solutions**:
1. Open **Tools → SDK Manager**
2. Install **Android SDK Platform 34**
3. Install **Android SDK Build-Tools 34.0.0**
4. Create `local.properties` in project root:
   ```properties
   sdk.dir=/path/to/your/Android/sdk
   ```

### Build Takes Too Long

**Problem**: Build process is very slow

**Solutions**:
1. Enable Gradle daemon (already configured in `gradle.properties`)
2. Increase Gradle memory: Edit `gradle.properties`:
   ```properties
   org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
   ```
3. Enable parallel builds (already configured)
4. Use **Build → Make Project** instead of full rebuilds

### Binary Not Found Error

**Problem**: App shows "Binary Missing" on launch

**Solutions**:
1. Verify binary exists: `app/src/main/assets/yatori-go-console`
2. Rebuild the app: **Build → Rebuild Project**
3. Clean and rebuild: **Build → Clean Project**, then rebuild
4. Check binary permissions (should be set automatically by app)

### Signing Error

**Problem**: Cannot generate signed APK

**Solutions**:
1. Verify `keystore.properties` exists and has correct paths
2. Check keystore file exists at specified location
3. Verify passwords are correct
4. Ensure keystore alias matches

## Testing

### Running Unit Tests

```bash
./gradlew test
```

### Running Instrumentation Tests

```bash
# Connect a device or start an emulator first
./gradlew connectedAndroidTest
```

### Running Lint Checks

```bash
./gradlew lint
```

View the lint report at:
```
app/build/reports/lint-results-debug.html
```

## Build Variants

The project includes two build types:

### Debug
- **Package**: `com.e5u.yatori.mobile.debug`
- **Features**: Debuggable, no obfuscation
- **Use case**: Development and testing

### Release
- **Package**: `com.e5u.yatori.mobile`
- **Features**: Optimized, obfuscated with ProGuard
- **Use case**: Production distribution

Switch between variants in Android Studio:
- **Build → Select Build Variant**
- Choose **debug** or **release**

## Build Output

After a successful build, you'll find:

```
app/build/
├── outputs/
│   ├── apk/
│   │   ├── debug/
│   │   │   └── app-debug.apk
│   │   └── release/
│   │       └── app-release.apk
│   └── logs/
└── reports/
    ├── lint-results-debug.html
    └── tests/
```

## Next Steps

After building:

1. **Install on device** and test basic functionality
2. **Review logs** to ensure binary extraction works
3. **Test permissions** by granting storage and notification access
4. **Execute binary** and verify output in logs
5. **Check settings** and adjust as needed

## Additional Resources

- [Android Developer Documentation](https://developer.android.com/docs)
- [Gradle Build Tool Documentation](https://docs.gradle.org/)
- [BUILD_BINARY.md](docs/BUILD_BINARY.md) - Binary compilation guide
- [USAGE.md](docs/USAGE.md) - User guide

## Support

For build issues:
- Check [GitHub Issues](https://github.com/e5u/yatori-mobile/issues)
- Review [troubleshooting section](#troubleshooting)
- Ask in [GitHub Discussions](https://github.com/e5u/yatori-mobile/discussions)

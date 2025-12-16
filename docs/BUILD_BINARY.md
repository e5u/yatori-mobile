# Building yatori-go-console for Android

This guide explains how to compile the `yatori-go-console` binary for Android devices.

## Prerequisites

### Required Tools

1. **Go Programming Language** (1.19 or later)
   - Download from: https://golang.org/dl/
   - Verify installation: `go version`

2. **Android NDK** (r23 or later recommended)
   - Download from: https://developer.android.com/ndk/downloads
   - Or install via Android Studio SDK Manager

3. **Git**
   - For cloning the yatori-go-core repository

### Supported Architectures

- **ARM64** (arm64-v8a) - Most modern Android devices
- **ARMv7** (armeabi-v7a) - Older Android devices
- **x86_64** - Android emulators (optional)

## Setup

### 1. Install Android NDK

#### Via Android Studio
1. Open Android Studio
2. Go to **Tools → SDK Manager**
3. Click on **SDK Tools** tab
4. Check **NDK (Side by side)**
5. Click **OK** to install

#### Manual Installation
1. Download NDK from https://developer.android.com/ndk/downloads
2. Extract to a directory (e.g., `~/android-ndk`)
3. Set environment variable:
   ```bash
   export ANDROID_NDK_HOME=~/android-ndk/android-ndk-r25c
   ```

### 2. Set Environment Variables

Add to your `~/.bashrc` or `~/.zshrc`:

```bash
# Android NDK
export ANDROID_NDK_HOME=$HOME/Android/Sdk/ndk/25.2.9519653  # Adjust version
export PATH=$PATH:$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64/bin

# Or for macOS:
# export PATH=$PATH:$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin
```

Reload your shell:
```bash
source ~/.bashrc  # or ~/.zshrc
```

## Compilation

### Clone yatori-go-core Repository

```bash
git clone https://github.com/e5u/yatori-go-core.git
cd yatori-go-core
```

### Build for ARM64 (Recommended)

ARM64 is the most common architecture for modern Android devices (Android 5.0+).

```bash
# Set CGO environment
export CGO_ENABLED=1
export GOOS=android
export GOARCH=arm64
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android21-clang

# Build
go build -o yatori-go-console-arm64 -ldflags="-s -w" ./cmd/console
```

### Build for ARMv7 (Legacy Devices)

For older Android devices (Android 4.1+):

```bash
# Set CGO environment
export CGO_ENABLED=1
export GOOS=android
export GOARCH=arm
export GOARM=7
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi21-clang

# Build
go build -o yatori-go-console-arm -ldflags="-s -w" ./cmd/console
```

### Build for x86_64 (Emulator)

For Android emulators:

```bash
# Set CGO environment
export CGO_ENABLED=1
export GOOS=android
export GOARCH=amd64
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android21-clang

# Build
go build -o yatori-go-console-x86_64 -ldflags="-s -w" ./cmd/console
```

### macOS Build Commands

For macOS users, replace `linux-x86_64` with `darwin-x86_64` in the CC path:

```bash
# ARM64 on macOS
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android21-clang

# ARMv7 on macOS
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi21-clang
```

## Installation

### Copy Binary to Android App

1. **Copy the binary** to the app's assets directory:

   ```bash
   # For ARM64 devices (most common)
   cp yatori-go-console-arm64 /path/to/yatori-mobile/app/src/main/assets/yatori-go-console
   
   # Or keep architecture-specific names
   cp yatori-go-console-arm64 /path/to/yatori-mobile/app/src/main/assets/yatori-go-console-arm64
   cp yatori-go-console-arm /path/to/yatori-mobile/app/src/main/assets/yatori-go-console-arm
   ```

2. **Verify file size**:
   ```bash
   ls -lh /path/to/yatori-mobile/app/src/main/assets/
   ```

3. The app will automatically detect the device architecture and use the appropriate binary.

## Automated Build Script

Create a build script `build-android.sh`:

```bash
#!/bin/bash

# Configuration
NDK_PATH=${ANDROID_NDK_HOME:-$HOME/Android/Sdk/ndk/25.2.9519653}
OUTPUT_DIR="./build/android"
APP_ASSETS_DIR="../yatori-mobile/app/src/main/assets"

# Detect host OS
if [[ "$OSTYPE" == "darwin"* ]]; then
    HOST_TAG="darwin-x86_64"
else
    HOST_TAG="linux-x86_64"
fi

TOOLCHAIN_PATH="$NDK_PATH/toolchains/llvm/prebuilt/$HOST_TAG/bin"

# Create output directory
mkdir -p "$OUTPUT_DIR"

echo "Building yatori-go-console for Android..."

# Build ARM64
echo "Building for ARM64..."
export CGO_ENABLED=1
export GOOS=android
export GOARCH=arm64
export CC="$TOOLCHAIN_PATH/aarch64-linux-android21-clang"

go build -o "$OUTPUT_DIR/yatori-go-console-arm64" \
    -ldflags="-s -w" \
    ./cmd/console

if [ $? -eq 0 ]; then
    echo "✓ ARM64 build successful"
else
    echo "✗ ARM64 build failed"
    exit 1
fi

# Build ARMv7
echo "Building for ARMv7..."
export GOARCH=arm
export GOARM=7
export CC="$TOOLCHAIN_PATH/armv7a-linux-androideabi21-clang"

go build -o "$OUTPUT_DIR/yatori-go-console-arm" \
    -ldflags="-s -w" \
    ./cmd/console

if [ $? -eq 0 ]; then
    echo "✓ ARMv7 build successful"
else
    echo "✗ ARMv7 build failed"
fi

# Copy to app assets if directory exists
if [ -d "$APP_ASSETS_DIR" ]; then
    echo "Copying binaries to app assets..."
    cp "$OUTPUT_DIR/yatori-go-console-arm64" "$APP_ASSETS_DIR/yatori-go-console"
    echo "✓ Binaries copied to app assets"
fi

echo "Build complete! Binaries are in $OUTPUT_DIR"
ls -lh "$OUTPUT_DIR"
```

Make it executable and run:

```bash
chmod +x build-android.sh
./build-android.sh
```

## Verification

### Test on Device

1. Build and install the Android app
2. Launch the app
3. Check the binary status in the main screen
4. View logs to verify extraction

### Manual Test (via ADB)

```bash
# Push binary to device
adb push yatori-go-console-arm64 /data/local/tmp/yatori-go-console

# Set permissions
adb shell chmod +x /data/local/tmp/yatori-go-console

# Test execution
adb shell /data/local/tmp/yatori-go-console --version
```

## Troubleshooting

### Error: "clang: not found"

**Solution**: Verify NDK installation and ensure `CC` path is correct:
```bash
ls $ANDROID_NDK_HOME/toolchains/llvm/prebuilt/
```

### Error: "exec format error"

**Cause**: Wrong architecture binary for device.

**Solution**: Verify device architecture:
```bash
adb shell getprop ro.product.cpu.abi
```
Build for the correct architecture.

### Error: "permission denied"

**Solution**: Binary needs executable permissions. The app handles this automatically, but for manual testing:
```bash
adb shell chmod +x /data/local/tmp/yatori-go-console
```

### CGO Errors

**Solution**: Ensure CGO is enabled:
```bash
export CGO_ENABLED=1
```

### Linking Errors

**Solution**: Use the correct API level (21+ recommended):
```bash
# Change the API level in CC path
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android21-clang
```

## Build Flags Explained

- `-ldflags="-s -w"`: Strip debug information and reduce binary size
  - `-s`: Omit symbol table and debug information
  - `-w`: Omit DWARF symbol table

- `CGO_ENABLED=1`: Enable C interop (required for Android)
- `GOOS=android`: Target Android OS
- `GOARCH=arm64/arm/amd64`: Target CPU architecture

## Additional Resources

- [Android NDK Documentation](https://developer.android.com/ndk/guides)
- [Go Cross Compilation](https://golang.org/doc/install/source#environment)
- [CGO Documentation](https://golang.org/cmd/cgo/)

## Support

For issues related to:
- **Binary compilation**: Check yatori-go-core repository
- **App integration**: Check yatori-mobile repository
- **NDK setup**: Refer to Android NDK documentation

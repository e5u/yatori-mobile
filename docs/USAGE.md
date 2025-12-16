# Yatori Mobile - User Guide

Complete guide to using Yatori Mobile on your Android device.

## Table of Contents

- [Installation](#installation)
- [First Launch](#first-launch)
- [Main Features](#main-features)
- [Executing Commands](#executing-commands)
- [Viewing Logs](#viewing-logs)
- [Settings](#settings)
- [Troubleshooting](#troubleshooting)
- [FAQ](#faq)

## Installation

### From APK

1. **Download** the APK file from the releases page
2. **Enable** installation from unknown sources:
   - Go to **Settings → Security**
   - Enable **Unknown Sources** or **Install Unknown Apps**
3. **Open** the APK file
4. **Tap Install** and wait for completion
5. **Open** the app from your app drawer

### From Source

1. **Clone** the repository
2. **Compile** the binary (see [BUILD_BINARY.md](BUILD_BINARY.md))
3. **Place binary** in `app/src/main/assets/`
4. **Open project** in Android Studio
5. **Build** and install to your device

## First Launch

### Initial Setup

When you first launch Yatori Mobile:

1. **Grant Permissions**
   - The app will request necessary permissions
   - **Storage**: Required for saving logs
   - **Notifications**: Required for execution status updates
   - Tap **Allow** for each permission

2. **Binary Check**
   - The app automatically checks for the binary
   - If binary is found: Status shows "Binary Ready" ✓
   - If binary is missing: Follow instructions to compile and add it

### Binary Status Indicators

- 🟢 **Green (Ready)**: Binary is properly installed and ready
- 🔴 **Red (Missing)**: Binary not found or invalid
- 🟡 **Yellow (Checking)**: Binary verification in progress

## Main Features

### Home Screen

The main screen displays:

- **Binary Status Card**: Shows if binary is ready
- **Execution Status Card**: Current execution state
- **Recent Activity**: Last execution information
- **Floating Action Button (FAB)**: Start/Stop execution

### Menu Options

- **View Logs**: Open log viewer
- **Settings**: Configure app behavior

## Executing Commands

### Start Execution

1. **Ensure binary is ready** (green status indicator)
2. **Tap the Play FAB** (▶) at the bottom-right
3. **Execution begins** in the background
4. **Notification appears** showing execution progress

### During Execution

- A persistent notification keeps you informed
- The app can be minimized
- Execution continues in the background
- Status updates appear in real-time

### Stop Execution

1. **Tap the Pause FAB** (⏸) or
2. **Tap "Stop"** in the notification
3. **Confirm** when prompted
4. Execution terminates gracefully

### Execution States

- **Stopped**: No execution in progress
- **Running**: Binary is executing
- **Completed**: Execution finished successfully
- **Failed**: Execution encountered an error

## Viewing Logs

### Access Logs

**Method 1**: From Main Screen
- Tap the **menu icon** (⋮) in the toolbar
- Select **View Logs**

**Method 2**: From Notification
- Tap **View** in completion notification

### Log Viewer Features

#### Reading Logs

- Logs are displayed in monospace font for clarity
- Timestamps show when each event occurred
- Automatic scrolling keeps latest logs visible
- Horizontal scroll for long log lines

#### Search Logs

1. Tap the **search icon** in the toolbar
2. Enter search term
3. Matching lines are highlighted
4. Use navigation to jump between matches

#### Export Logs

1. Tap the **share FAB** at the bottom-right
2. Choose how to share (Email, Drive, etc.)
3. Logs are exported as plain text file

#### Clear Logs

1. Tap **menu icon** (⋮) in the toolbar
2. Select **Clear Logs**
3. **Confirm** the action
4. All logs for current day are deleted

### Log File Structure

```
[2024-12-16 10:30:00] Application started
[2024-12-16 10:30:01] Binary extracted successfully
[2024-12-16 10:30:02] Starting execution: /data/user/0/com.e5u.yatori.mobile/files/yatori-go-console
[2024-12-16 10:30:05] Execution output...
[2024-12-16 10:35:00] Execution completed with exit code: 0
```

## Settings

Access settings from the main menu.

### General Settings

#### Enable Notifications
- **Purpose**: Show execution status notifications
- **Default**: On
- **Recommendation**: Keep enabled for execution feedback

### Execution Settings

#### Auto Start
- **Purpose**: Automatically start execution on app launch
- **Default**: Off
- **Use Case**: For automated/scheduled tasks

### Log Settings

#### Keep Logs
- **Options**: 1, 3, 7, 14, 30 days, or Forever
- **Default**: 7 days
- **Purpose**: Automatically delete old logs
- **Recommendation**: 7-14 days for most users

#### Log Level
- **Options**: Debug, Info, Warning, Error
- **Default**: Info
- **Purpose**: Control log verbosity
- **Debug**: All logs (very detailed)
- **Info**: Normal operations
- **Warning**: Potential issues
- **Error**: Only errors

### About

- **Version**: Current app version
- **Licenses**: View open source licenses

## Troubleshooting

### Binary Not Found

**Problem**: Red status indicator, "Binary Missing" message

**Solutions**:
1. Verify binary exists in assets
2. Rebuild and reinstall the app
3. Check compilation (see [BUILD_BINARY.md](BUILD_BINARY.md))
4. Verify device architecture compatibility

### Execution Fails Immediately

**Problem**: Execution starts but stops with error

**Solutions**:
1. Check logs for error messages
2. Verify binary has execute permissions
3. Ensure device architecture matches binary
4. Try re-extracting binary (clear app data)

### Logs Not Displaying

**Problem**: Log viewer shows "No logs available"

**Solutions**:
1. Execute the binary first
2. Check storage permissions
3. Verify app has write access
4. Check available storage space

### Notifications Not Appearing

**Problem**: No notifications during execution

**Solutions**:
1. Grant notification permission in settings
2. Check system notification settings
3. Ensure "Enable Notifications" is on in app settings
4. Battery optimization may block notifications

### Permission Denied Errors

**Problem**: App can't access storage or other resources

**Solutions**:
1. Go to Android **Settings → Apps → Yatori Mobile → Permissions**
2. Grant all required permissions
3. Restart the app
4. Clear app cache if needed

### App Crashes on Start

**Problem**: App closes immediately after opening

**Solutions**:
1. Clear app cache and data
2. Reinstall the app
3. Check Android version compatibility (8.0+)
4. View crash logs via ADB

## FAQ

### Q: Do I need root access?
**A**: No, Yatori Mobile works on non-rooted devices.

### Q: Which Android versions are supported?
**A**: Android 8.0 (API 26) and above.

### Q: How much storage does the app need?
**A**: Base app: ~10-20 MB, Binary: 5-15 MB, Logs: Variable (depends on usage)

### Q: Can I run multiple instances?
**A**: No, only one execution can run at a time.

### Q: Will execution continue if I close the app?
**A**: Yes, execution runs as a foreground service and continues in the background.

### Q: How do I update the binary?
**A**: Compile new binary, replace in assets, rebuild and reinstall app.

### Q: Can I use this on an emulator?
**A**: Yes, compile binary for x86_64 architecture.

### Q: Where are logs stored?
**A**: Internal storage: `/data/data/com.e5u.yatori.mobile/files/logs/`

### Q: Can I schedule automatic execution?
**A**: Currently not supported directly. Use Android's Tasker or similar apps.

### Q: Is my data safe?
**A**: All data stays on your device. No cloud sync or external transmission.

### Q: How do I report bugs?
**A**: Open an issue on the GitHub repository with:
  - Android version
  - Device model
  - App version
  - Steps to reproduce
  - Relevant logs

### Q: Can I contribute?
**A**: Yes! See the main README for contribution guidelines.

## Advanced Usage

### ADB Commands

#### View App Logs
```bash
adb logcat | grep "YatoriMobile"
```

#### Check Binary Location
```bash
adb shell ls -l /data/data/com.e5u.yatori.mobile/files/
```

#### Pull Logs from Device
```bash
adb pull /data/data/com.e5u.yatori.mobile/files/logs/ ./local-logs/
```

#### Force Stop Service
```bash
adb shell am force-stop com.e5u.yatori.mobile
```

### Integration with Other Apps

#### Tasker Integration

Create a Tasker task to launch Yatori:

1. **Task → Add Action → App → Launch App**
2. Select **Yatori Mobile**
3. Set trigger (time, event, etc.)

#### Automation with Intents

```bash
# Start app
adb shell am start -n com.e5u.yatori.mobile/.ui.MainActivity

# View logs
adb shell am start -n com.e5u.yatori.mobile/.ui.LogViewerActivity
```

## Best Practices

1. **Regular Log Cleanup**: Review and clear logs weekly
2. **Monitor Storage**: Check available space regularly
3. **Update Binary**: Keep binary updated with latest features
4. **Backup Logs**: Export important logs before clearing
5. **Check Notifications**: Enable for execution feedback
6. **Review Settings**: Adjust based on your usage patterns

## Support

- **GitHub Issues**: Report bugs and feature requests
- **Discussions**: Ask questions and share experiences
- **Documentation**: Keep this guide handy for reference

## Changelog

See [GitHub Releases](https://github.com/e5u/yatori-mobile/releases) for version history and updates.

---

**Last Updated**: 2024-12-16  
**Version**: 1.0.0
